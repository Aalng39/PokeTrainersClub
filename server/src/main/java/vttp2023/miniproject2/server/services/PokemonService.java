package vttp2023.miniproject2.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2023.miniproject2.server.models.PokemonAttribute;
import vttp2023.miniproject2.server.repositories.PokemonRepository;

@Service
public class PokemonService {
    
    @Autowired
    private PokemonRepository pokemonRepo;
    
    public List<PokemonAttribute> getPokemonDisplay(int offset){
        return pokemonRepo.getPokemonDisplay(offset);
    }

    public PokemonAttribute getPokemonDetails(String nameOrId){
        return pokemonRepo.getPokemonDetails(nameOrId);
    }
    public List<PokemonAttribute> getPokemonListByType(String type, int offSet){
        return pokemonRepo.getPokemonListByType(type, offSet);
    }


}
