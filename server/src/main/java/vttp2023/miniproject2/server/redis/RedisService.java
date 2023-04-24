package vttp2023.miniproject2.server.redis;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import vttp2023.miniproject2.server.models.PokemonAttribute;

@Service
public class RedisService implements RedisRepository{
    
    @Autowired
    // @Qualifier("marvelcache")   // must match bean name in RedisConfig
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void savePokeListToRedis(List<PokemonAttribute> pokemonList) {
        for(PokemonAttribute pokemonAttribute: pokemonList){
        redisTemplate.opsForValue().setIfAbsent(pokemonAttribute.getName(), pokemonAttribute);
        redisTemplate.expire(pokemonAttribute.getName(), 15, TimeUnit.MINUTES);
        }
    }

    @Override
    public Optional<List<PokemonAttribute>> checkPokeListRedis(List<String> pokeNames) {
        
        List<PokemonAttribute> pokeList = new LinkedList<>();

        for(String pokeName: pokeNames){
        
            if(redisTemplate.hasKey(pokeName)){
                PokemonAttribute pokemonAttribute = new PokemonAttribute();
                pokemonAttribute = (PokemonAttribute) redisTemplate.opsForValue().get(pokeName);
                pokeList.add(pokemonAttribute);

            }else{
                return Optional.empty();
            }
        }

        return Optional.of(pokeList);
    }

}
