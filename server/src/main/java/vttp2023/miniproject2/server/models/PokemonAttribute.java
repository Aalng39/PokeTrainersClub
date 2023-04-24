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

public class PokemonAttribute implements Serializable {
    private int offSet;
    private String pokemonId;    
    private String nameOrId;
    private String name;
    private String imageUrl;
    private String height;
    private String weight; 
    private String description;
    private String species;
    private List<String> types = new LinkedList<>();
    private List<String> abilities = new LinkedList<>();
    private PokemonStats baseStats;
    private List<PokemonChain> evolutionList = new LinkedList<>();
   
    public PokemonStats getBaseStats() { return baseStats; }
    public void setBaseStats(PokemonStats baseStats) { this.baseStats = baseStats; }

    public String getPokemonId() { return pokemonId; }
    public void setPokemonId(String pokemonId) { this.pokemonId = pokemonId; }

    public int getOffSet() { return offSet; }
    public void setOffSet(int offSet) { this.offSet = offSet; }

    public List<String> getAbilities() { return abilities; }
    public void setAbilities(List<String> abilities) { this.abilities = abilities; }

    public String getNameOrId() { return nameOrId; }
    public void setNameOrId(String nameOrId) { this.nameOrId = nameOrId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getTypes() { return types; }
    public void setTypes(List<String> types) { this.types = types; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public List<PokemonChain> getEvolutionList() { return evolutionList; }

    public void setEvolutionList(List<PokemonChain> evolutionList) { this.evolutionList = evolutionList; }
    
    public static PokemonAttribute create(JsonObject doc) {

        RestTemplate restTemplate = new RestTemplate();
        final PokemonAttribute pokemonAttribute = new PokemonAttribute();

        String pokeName = doc.getString("name");

            // Change Name to First Letter Caps
            String capPokeName = pokeName.substring(0, 1).toUpperCase() + pokeName.substring(1);
            pokemonAttribute.setName(capPokeName);
            
            //Use Pokemon Name to get new URL 
            ResponseEntity<String> respN = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon/" + pokeName), String.class);
            String payloadN = respN.getBody();
            StringReader stringRN = new StringReader(payloadN);
            JsonReader jsonRN = Json.createReader(stringRN);
            JsonObject jsonObjN = jsonRN.readObject();

            //Getting Pokemon Img
            JsonObject sprites = jsonObjN.getJsonObject("sprites");
            JsonObject other = sprites.getJsonObject("other");
            JsonObject offArt = other.getJsonObject("official-artwork");
            
            pokemonAttribute.setImageUrl(offArt.getString("front_default"));
    
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
            pokemonAttribute.setTypes(pokeTypes);

        return pokemonAttribute;
    }

    public static PokemonAttribute createDetails(JsonObject doc) {

        RestTemplate restTemplate = new RestTemplate();
        final PokemonAttribute pokemonAttribute = new PokemonAttribute();

        JsonArray jsonArray = doc.getJsonArray("forms");
        JsonObject firstForm = jsonArray.get(0).asJsonObject();
        String pokeName = firstForm.getString("name");
        
            // Change Name to First Letter Caps
            String capPokeName = pokeName.substring(0, 1).toUpperCase() + pokeName.substring(1);
            pokemonAttribute.setName(capPokeName);
            
            // GET POKEMON ID
            String pokeId = doc.getJsonNumber("id").toString();
            pokemonAttribute.setPokemonId(pokeId);

            // GET Height & Weight
            String height = (doc.getJsonNumber("height")).toString();
            pokemonAttribute.setHeight(height);
            String weight = doc.getJsonNumber("weight").toString();
            pokemonAttribute.setWeight(weight);

            //Getting Pokemon Img
            JsonObject sprites = doc.getJsonObject("sprites");
            JsonObject other = sprites.getJsonObject("other");
            JsonObject offArt = other.getJsonObject("official-artwork");
            
            pokemonAttribute.setImageUrl(offArt.getString("front_default"));
    
            //Getting Pokemon Types
            JsonArray jsonArrayN = doc.getJsonArray("types");
            List<String> pokeTypes = new LinkedList<>();
            for (int y = 0; y < jsonArrayN.size(); y++){
                JsonObject types = jsonArrayN.get(y).asJsonObject();
                
                JsonObject type = types.getJsonObject("type");
                String pokeType = type.getString("name");
                String capPokeType = pokeType.substring(0, 1).toUpperCase() + pokeType.substring(1);
                pokeTypes.add(capPokeType);                
            }
            pokemonAttribute.setTypes(pokeTypes);

            //Getting Pokemon Abilities
            JsonArray jsonArrayA = doc.getJsonArray("abilities");
            List<String> pokeAbi = new LinkedList<>();
                for (int y = 0; y < jsonArrayA.size(); y++){
                JsonObject abilities = jsonArrayA.get(y).asJsonObject();
                
                JsonObject ability = abilities.getJsonObject("ability");
                String capPokeAbi = (ability.getString("name")).substring(0, 1).toUpperCase() 
                                    + (ability.getString("name")).substring(1);
                pokeAbi.add(capPokeAbi);                
            };
            pokemonAttribute.setAbilities(pokeAbi);

            //Get Description
                ResponseEntity<String> respDesc = restTemplate.getForEntity(("https://pokeapi.co/api/v2/pokemon-species/" + pokeName), String.class);
                String payloadDesc = respDesc.getBody();
                StringReader stringRDesc = new StringReader(payloadDesc);
                JsonReader jsonRDesc = Json.createReader(stringRDesc);
                JsonObject jsonObjDesc = jsonRDesc.readObject();
                JsonArray jsonArrDesc = jsonObjDesc.getJsonArray("flavor_text_entries");
                int enIndex = 0;
                for (int x = 0; x < jsonArrDesc.size(); x++){
                    JsonObject descObj = jsonArrDesc.get(x).asJsonObject();
                    JsonObject lang = descObj.getJsonObject("language");
                    String enLang = lang.getString("name");
                    if(enLang.equals("en")){
                        enIndex = x;
                    }       
                }            
                JsonObject lang = jsonArrDesc.get(enIndex).asJsonObject();
                String desc = lang.getString("flavor_text").replace("\f", " ");
                pokemonAttribute.setDescription(desc);

            //Get Species
                JsonArray jsonArrGenera = jsonObjDesc.getJsonArray("genera");
                int enId = 0;
                for (int x = 0; x < jsonArrGenera.size(); x++){
                    JsonObject descObj = jsonArrGenera.get(x).asJsonObject();
                    JsonObject lang1 = descObj.getJsonObject("language");
                    String enLang = lang1.getString("name");
                    if(enLang.equals("en")){
                        enId = x;
                    }       
                }            
                JsonObject lang1 = jsonArrGenera.get(enId).asJsonObject();
                String genus = lang1.getString("genus");
                pokemonAttribute.setSpecies(genus);

            // GET STATS
            PokemonStats stats = new PokemonStats();
            JsonArray jsonArrayStat = doc.getJsonArray("stats");
            
            stats.setHp(((jsonArrayStat.get(0)).asJsonObject()).getJsonNumber("base_stat").toString());
            stats.setAttack(((jsonArrayStat.get(1)).asJsonObject()).getJsonNumber("base_stat").toString());
            stats.setDefense(((jsonArrayStat.get(2)).asJsonObject()).getJsonNumber("base_stat").toString());
            stats.setSpecialAttack(((jsonArrayStat.get(3)).asJsonObject()).getJsonNumber("base_stat").toString());
            stats.setSpecialDefense(((jsonArrayStat.get(4)).asJsonObject()).getJsonNumber("base_stat").toString());
            stats.setSpeed(((jsonArrayStat.get(5)).asJsonObject()).getJsonNumber("base_stat").toString());

            pokemonAttribute.setBaseStats(stats);
            
            //EVOLUTION ATTEND LATER----------------------------------------------
            JsonObject evolutionChain = jsonObjDesc.getJsonObject("evolution_chain");
            String evolutionUrl = evolutionChain.getString("url");

            ResponseEntity<String> respEvolution = restTemplate.getForEntity(evolutionUrl, String.class);
            String payloadEvolution = respEvolution.getBody();
            StringReader stringREvolution = new StringReader(payloadEvolution);
            JsonReader jsonREvolution = Json.createReader(stringREvolution);
            JsonObject jsonObjEvolution = jsonREvolution.readObject();
            JsonObject chain = jsonObjEvolution.getJsonObject("chain");
            JsonObject species = chain.getJsonObject("species");
            String evolutionName = species.getString("name");

            List<String> evolutionList = new LinkedList<>();

            JsonArray evolution2 = (chain.getJsonArray("evolves_to").asJsonArray());
            

            if(evolution2.isEmpty()){
                evolutionList.add(evolutionName);

            }else{
            evolutionList.add(evolutionName);
            JsonObject species2 = evolution2.get(0).asJsonObject().getJsonObject("species");
            String evolutionName2 = species2.getString("name");
            JsonArray evolution3 = (evolution2.get(0).asJsonObject().getJsonArray("evolves_to").asJsonArray());

                if(evolution3.isEmpty()){
                    evolutionList.add(evolutionName2);

                }else{
                    evolutionList.add(evolutionName2);
                    JsonObject species3 = evolution3.get(0).asJsonObject().getJsonObject("species");
                    String evolutionName3 = species3.getString("name");
                 
                    evolutionList.add(evolutionName3);         
                }     
            }

            List<PokemonChain> pokemonChains = new LinkedList<>();
                for(String x : evolutionList){
                pokemonChains.add(PokemonChain.createChain(x));
                }

            pokemonAttribute.setEvolutionList(pokemonChains);

        return pokemonAttribute;
    }

    public JsonObject toJson() {
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

    public JsonObject toDetailsJson() {

        JsonArray typeArr = null;
        JsonArrayBuilder bld = Json.createArrayBuilder();
        for(String x : getTypes())
            bld.add(x);
            typeArr = bld.build();
        

        JsonArray abilitiesArr = null;
        JsonArrayBuilder jbld = Json.createArrayBuilder();
        for(String x : getAbilities())
            jbld.add(x);
            abilitiesArr = jbld.build();
        

        JsonArray evolutionArr = null;
        JsonArrayBuilder jsbld = Json.createArrayBuilder();
        for(PokemonChain x: getEvolutionList())
            jsbld.add(x.toChainJson());
            evolutionArr = jsbld.build();
        
            
    
        return Json.createObjectBuilder()
            .add("id", getPokemonId())
            .add("name", getName())
            .add("description", getDescription())
            .add("species", getSpecies())
            .add("height", getHeight())
            .add("weight", getWeight())
            .add("imageUrl", getImageUrl())
            .add("types", typeArr)
            .add("abilities", abilitiesArr)
            .add("baseStats", getBaseStats().toStatsJson())
            .add("evolutionChain", evolutionArr)
            .build();
    }
}
