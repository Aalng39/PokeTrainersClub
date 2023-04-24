package vttp2023.miniproject2.server.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2023.miniproject2.server.models.ForumEntry;
import vttp2023.miniproject2.server.models.PokemonAdded;
import vttp2023.miniproject2.server.models.PokemonAttribute;
import vttp2023.miniproject2.server.models.PokemonTrainer;
import vttp2023.miniproject2.server.models.UserPost;
import vttp2023.miniproject2.server.mysql.MySQLService;
import vttp2023.miniproject2.server.mysql.UserException;
import vttp2023.miniproject2.server.services.GmailService;
import vttp2023.miniproject2.server.services.PokemonService;
import vttp2023.miniproject2.server.services.S3Service;

@Controller
public class PokemonController {
    
    @Autowired
    private PokemonService pokemonSvc;

    @Autowired
    private MySQLService sqlSvc;

    @Autowired
    private S3Service s3Svc;

    @Autowired
    private GmailService gmailSvc;
  
    @GetMapping(path="/pokemon", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPokemonDisplayPage(@RequestParam int offset){

        List<PokemonAttribute> pokemonList = pokemonSvc.getPokemonDisplay(offset);
        
        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        pokemonList.stream()
                .forEach(v -> {
                    arrBuilder.add(v.toJson());
                });

        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @GetMapping(path="/pokemon/{nameOrId}", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPokemonDetails(@PathVariable String nameOrId){

        PokemonAttribute pokemonDetails = pokemonSvc.getPokemonDetails(nameOrId);
        
        return ResponseEntity.ok(pokemonDetails.toDetailsJson().toString());
    }

    @GetMapping(path="/pokemon/types/{type}", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPokemonListByType(@PathVariable String type, @RequestParam int offset){

        List<PokemonAttribute> pokemonList = pokemonSvc.getPokemonListByType(type, offset);
        
        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        pokemonList.stream()
                .forEach(v -> {
                    arrBuilder.add(v.toJson());
                });

        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @PostMapping(path="/pokemon/user",
    produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> insertUser( @RequestPart String username,
        @RequestPart String email,
        @RequestPart String password,
        @RequestPart MultipartFile image){

        String key = "";
        PokemonTrainer pokemonTrainer = new PokemonTrainer();

        try {
            key = s3Svc.upload(image);           
            pokemonTrainer.setImage("https://youthosai.sgp1.digitaloceanspaces.com/myobjects/" + key);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        pokemonTrainer.setUsername(username);
        pokemonTrainer.setEmail(email);
        pokemonTrainer.setPassword(password);
        
        String user = sqlSvc.insertUser(pokemonTrainer);
        
        StringBuilder text = new StringBuilder();
        text.append("You have registered an account with us!\n");
        text.append("Thank you for supporting our application!\n\n");
        text.append("PokéTrainerClub\n");
        text.append("Visit Our Website: poke-trainers-club-production.up.railway.app/");

        gmailSvc.sendSimpleMessage(email, "PokéTrainersClub Account Created", text.toString());

        JsonObject o = Json.createObjectBuilder()
                        .add("username", user)
                        .build();

        return  ResponseEntity.ok(o.toString());
    }

    @PutMapping(path="/pokemon/user",
    produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> LogInUser(@RequestBody String email){


        PokemonTrainer pokemonTrainer = sqlSvc.logInUser(email);
        
        JsonObject o = Json.createObjectBuilder()
                        .add("username", pokemonTrainer.getUsername())
                        .add("email", pokemonTrainer.getEmail())
                        .add("imageUrl", pokemonTrainer.getImage())
                        .add("date", pokemonTrainer.getDate())
                        .build();

        return  ResponseEntity.ok(o.toString());
    }

    @PostMapping(path="/pokemon/myteam/added",
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> insertUserPokemon(@RequestBody PokemonAdded pokemonAdded){

        JsonObject o = Json.createObjectBuilder()
                        .add("inserted", sqlSvc.insertUserPokemon(pokemonAdded))
                        .build();

        return  ResponseEntity.ok(o.toString());
    }
    
    @GetMapping(path="/pokemon/myteam", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getUserPokemonTeam(@RequestParam String username){

        List<PokemonAttribute> userPokemonTeam = sqlSvc.getPokemonForUser(username);
        
        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        userPokemonTeam.stream()
                .forEach(v -> {
                    arrBuilder.add(v.toJson());
                });

        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @PutMapping(path="/pokemon/myteam/removed",
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteUserPokemon(@RequestBody PokemonAdded pokemonAdded){

        JsonObject o = Json.createObjectBuilder()
                        .add("deleted", sqlSvc.deleteUserPokemon(pokemonAdded))
                        .build();

        return  ResponseEntity.ok(o.toString());
    }

    @PutMapping(path="/pokemon/deleteUser",
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody PokemonTrainer pokemonTrainer){

        boolean userDeleted = false;
        try {
			userDeleted = sqlSvc.deleteUser(pokemonTrainer);

            StringBuilder text = new StringBuilder();
            text.append("You have deleted your account!\n\n");
            text.append("Sorry to see you go...\n");
            text.append("Hope we can serve you better next time!\n\n");
            text.append("PokéTrainerClub\n");
            text.append("Visit Our Website: poke-trainers-club-production.up.railway.app/");

            gmailSvc.sendSimpleMessage(pokemonTrainer.getEmail(), "PokéTrainersClub Account Deleted", text.toString());

		} catch (UserException ex) {
			JsonObject error = Json.createObjectBuilder()
                                .add("error", ex.getMessage())
                                .build();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(error.toString());
		}

        JsonObject o = Json.createObjectBuilder()
                        .add("User deleted", userDeleted)
                        .build();

        return  ResponseEntity.ok(o.toString());
    }

    @GetMapping(path="/pokemon/forum", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPokemonForumPage(){

        List<ForumEntry> forumEntries = sqlSvc.getForum();
        
        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        forumEntries.stream()
                .forEach(v -> {
                    arrBuilder.add(v.toJson());
                });

        return ResponseEntity.ok(arrBuilder.build().toString());
    }
    
    @PostMapping(path="/pokemon/forum",
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addPost(@RequestBody ForumEntry forumEntry){

        JsonObject o = Json.createObjectBuilder()
                        .add("inserted", sqlSvc.insertPost(forumEntry))
                        .build();

        return  ResponseEntity.ok(o.toString());
    }

    @GetMapping(path="/pokemon/forum/user", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getForumPostsByUser(@RequestParam String username){

        List<ForumEntry> forumEntries = sqlSvc.getForumByUser(username);
        
        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        forumEntries.stream()
                .forEach(v -> {
                    arrBuilder.add(v.toJson());
                });

        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @PutMapping(path="/pokemon/forum/user", 
    produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getDeletePostByUser(@RequestBody UserPost userPost){

        JsonObject o = Json.createObjectBuilder()
                        .add("deleted", sqlSvc.deleteUserPost((userPost)))
                        .build();

        return  ResponseEntity.ok(o.toString());
    }
}
