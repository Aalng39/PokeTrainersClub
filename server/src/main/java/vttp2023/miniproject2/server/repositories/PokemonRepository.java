package vttp2023.miniproject2.server.repositories;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.miniproject2.server.models.PokemonAttribute;
import vttp2023.miniproject2.server.redis.RedisService;

@Repository
public class PokemonRepository { 

    @Autowired
    private RedisService redisSvc;

    RestTemplate restTemplate = new RestTemplate();

    public List<PokemonAttribute> getPokemonDisplay(int offset){
        //Get Pokemon Name limit to 20 for now
        ResponseEntity<String> response = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/?offset=" + offset + "&limit=10"), String.class);
        String payload = response.getBody();
        StringReader stringR = new StringReader(payload);
        JsonReader jsonR = Json.createReader(stringR);
        JsonObject jsonObj = jsonR.readObject();
        JsonArray jsonArr = jsonObj.getJsonArray("results");
        
        List<PokemonAttribute> pokeAttList = new LinkedList<>();
        List<String> pokeNames = new LinkedList<>();

        for (int i = 0; i < jsonArr.size(); i++){
            JsonObject jsonOb = jsonArr.get(i).asJsonObject();

            String pokeName = jsonOb.getString("name");

            // Change Name to First Letter Caps
            String capPokeName = pokeName.substring(0, 1).toUpperCase() + pokeName.substring(1);
            pokeNames.add(capPokeName);
        }
        Optional<List<PokemonAttribute>> opt = redisSvc.checkPokeListRedis(pokeNames);

        if(opt.isEmpty()){
            for (int i = 0; i < jsonArr.size(); i++){
                JsonObject jsonOb = jsonArr.get(i).asJsonObject();
                String pokeName = jsonOb.getString("name");

                ResponseEntity<String> respD = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/" + pokeName), String.class);
                String payloadD = respD.getBody();
                StringReader stringRD = new StringReader(payloadD);
                JsonReader jsonRD = Json.createReader(stringRD);
                JsonObject jsonObjD = jsonRD.readObject();

                pokeAttList.add(PokemonAttribute.create(jsonObjD)); 
                
            }
            
            redisSvc.savePokeListToRedis(pokeAttList);
            
            return pokeAttList;  
        }

        return opt.get();
    }
    
    public PokemonAttribute getPokemonDetails(String nameOrId){

        PokemonAttribute pokeDetails = new PokemonAttribute();

        String pokeSearchName = nameOrId.toLowerCase();
        ResponseEntity<String> respD; 

        try{
        respD = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/" + pokeSearchName), String.class);
        }catch(Exception e){
            
            return pokeDetails;
        }

        String payloadD = respD.getBody();
        StringReader stringRD = new StringReader(payloadD);
        JsonReader jsonRD = Json.createReader(stringRD);
        JsonObject jsonObjD = jsonRD.readObject();
           
        pokeDetails = PokemonAttribute.createDetails(jsonObjD);
    
        return pokeDetails;
        
    }

    public List<PokemonAttribute> getPokemonListByType(String type, int offSet){

        String pokeType = type.toLowerCase();

        ResponseEntity<String> responseT = restTemplate.getForEntity("https://pokeapi.co/api/v2/type/" + pokeType, String.class);
        String payloadT = responseT.getBody();
        StringReader stringReaderT = new StringReader(payloadT);
        JsonReader jsonReaderT = Json.createReader(stringReaderT);
        JsonObject jsonObjectT = jsonReaderT.readObject();
        JsonArray jsonArrayT = jsonObjectT.getJsonArray("pokemon");

        List<PokemonAttribute> pokeAttList = new LinkedList<>();
        List<String> pokeNames = new LinkedList<>();

        for (int i = offSet; i < offSet+10; i++){
            JsonObject jsonObj = jsonArrayT.get(i).asJsonObject();
            JsonObject pokemon = jsonObj.getJsonObject("pokemon");    
            String pokeName = pokemon.getString("name");

            String capPokeName = pokeName.substring(0, 1).toUpperCase() + pokeName.substring(1);
            pokeNames.add(capPokeName);
        }
            
        Optional<List<PokemonAttribute>> opt = redisSvc.checkPokeListRedis(pokeNames);

        if(opt.isEmpty()){
            for (int i = offSet; i < offSet+20; i++){
                JsonObject jsonObj = jsonArrayT.get(i).asJsonObject();
                JsonObject pokemon = jsonObj.getJsonObject("pokemon");    
                String pokeName = pokemon.getString("name");
    
                PokemonAttribute pokeDetails = new PokemonAttribute();;
        
                ResponseEntity<String> respD = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/" + pokeName), String.class);
                String payloadD = respD.getBody();
                StringReader stringRD = new StringReader(payloadD);
                JsonReader jsonRD = Json.createReader(stringRD);
                JsonObject jsonObjD = jsonRD.readObject();

                pokeDetails = PokemonAttribute.create(jsonObjD);
                pokeAttList.add(pokeDetails);
            }
            redisSvc.savePokeListToRedis(pokeAttList);
            return pokeAttList;
        }

    return opt.get();
    }
}