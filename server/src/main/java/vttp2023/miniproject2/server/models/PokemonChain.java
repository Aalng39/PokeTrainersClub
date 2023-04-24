package vttp2023.miniproject2.server.models;

import java.io.Serializable;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class PokemonChain implements Serializable{
    private String name;
    private String imageUrl;
    private List<String> types = new LinkedList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getTypes() { return types; }
    public void setTypes(List<String> types) { this.types = types; }

    public static PokemonChain createChain(String evolutionName) {

        RestTemplate restTemplate = new RestTemplate();
        final PokemonChain pokemonChain = new PokemonChain();

        String capPokeName = evolutionName.substring(0, 1).toUpperCase() + evolutionName.substring(1);
        pokemonChain.setName(capPokeName);
            
        //Use Pokemon Name to get new URL 
        ResponseEntity<String> respN = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/" + evolutionName), String.class);
        String payloadN = respN.getBody();
        StringReader stringRN = new StringReader(payloadN);
        JsonReader jsonRN = Json.createReader(stringRN);
        JsonObject jsonObjN = jsonRN.readObject();

        //Getting Pokemon Img
        JsonObject sprites = jsonObjN.getJsonObject("sprites");
        JsonObject other = sprites.getJsonObject("other");
        JsonObject offArt = other.getJsonObject("official-artwork");
        
        pokemonChain.setImageUrl(offArt.getString("front_default"));

        //Getting Pokemon Types
        JsonArray jsonArrayN = jsonObjN.getJsonArray("types");
        List<String> pokeTypes = new LinkedList<>();
        for (int y = 0; y < jsonArrayN.size(); y++){
            JsonObject types = jsonArrayN.get(y).asJsonObject();
            
            JsonObject type = types.getJsonObject("type");
            String pokeType = type.getString("name");
            String capPokeType = pokeType.substring(0, 1).toUpperCase() + pokeType.substring(1);
            pokeTypes.add(capPokeType);                
        }
        pokemonChain.setTypes(pokeTypes);

        return pokemonChain;
    }

    public JsonObject toChainJson() {
        JsonArray typeArr = null;
        JsonArrayBuilder bld = Json.createArrayBuilder();
        for(String x : getTypes())
            bld.add(x);
            typeArr = bld.build();

        return Json.createObjectBuilder()
            .add("name", getName())
            .add("imageUrl", getImageUrl())
            .add("types", typeArr)
            .build();
    }
}
