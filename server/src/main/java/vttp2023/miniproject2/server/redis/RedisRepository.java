package vttp2023.miniproject2.server.redis;

import java.util.List;
import java.util.Optional;

import vttp2023.miniproject2.server.models.PokemonAttribute;

public interface RedisRepository {
    
    public void savePokeListToRedis(List<PokemonAttribute> pokemonList);

    public Optional<List<PokemonAttribute>> checkPokeListRedis(List<String> pokeNames);

}
