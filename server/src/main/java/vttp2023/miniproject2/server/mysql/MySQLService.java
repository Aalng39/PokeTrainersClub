package vttp2023.miniproject2.server.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.miniproject2.server.models.ForumEntry;
import vttp2023.miniproject2.server.models.PokemonAdded;
import vttp2023.miniproject2.server.models.PokemonAttribute;
import vttp2023.miniproject2.server.models.PokemonTrainer;
import vttp2023.miniproject2.server.models.UserPost;

@Service
public class MySQLService {

    @Autowired
    private MySQLRepository sqlRepo;
  
    public String insertUser(PokemonTrainer user){
        return sqlRepo.insertUser(user);
    }

    public PokemonTrainer logInUser(String email){
        return sqlRepo.logInUser(email);
    }

    public boolean insertUserPokemon(PokemonAdded pokemon){
        return sqlRepo.insertUserPokemon(pokemon);
    }
    
    public List<PokemonAttribute> getPokemonForUser(String username) {
        return sqlRepo.getPokemonForUser(username);
    }

    public boolean deleteUserPokemon(PokemonAdded pokemon){
        return sqlRepo.deleteUserPokemon(pokemon);
    }

    @Transactional(rollbackFor = UserException.class)
    public boolean deleteUser(PokemonTrainer trainer) throws UserException{

        boolean user1 = false;
        boolean user2 = false;
        boolean user3 = false;

    
        user1 = sqlRepo.deleteUser1(trainer);
        user2 = sqlRepo.deleteUser2(trainer);
        user3 = sqlRepo.deleteUser3(trainer);

        if(user1 && user2 && user3){
            return true;
        }
        return false;
    }

    public List<ForumEntry> getForum(){
        return sqlRepo.getForum();
    }

    public boolean insertPost(ForumEntry forumEntry){
        return sqlRepo.insertPost(forumEntry);
    }

    public List<ForumEntry> getForumByUser(String username){
        return sqlRepo.getForumByUser(username);
    }

    public boolean deleteUserPost(UserPost userPost){
        return sqlRepo.deleteUserPost(userPost);
    }
}

