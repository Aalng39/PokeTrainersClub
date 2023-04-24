package vttp2023.miniproject2.server.models;

public class PokemonAdded {
    
    private String username;
    private String pokemonName;
    private String pokemonUrl;
    private String pokemonTypes;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPokemonName() { return pokemonName; }
    public void setPokemonName(String pokemonName) { this.pokemonName = pokemonName; }
    
    public String getPokemonUrl() { return pokemonUrl; }
    public void setPokemonUrl(String pokemonUrl) { this.pokemonUrl = pokemonUrl; }
    
    public String getPokemonTypes() { return pokemonTypes; }
    public void setPokemonTypes(String pokemonTypes) { this.pokemonTypes = pokemonTypes; }

}