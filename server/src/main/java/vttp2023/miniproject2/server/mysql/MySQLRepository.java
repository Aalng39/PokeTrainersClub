package vttp2023.miniproject2.server.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.beans.factory.annotation.Autowired;

import vttp2023.miniproject2.server.models.ForumEntry;
import vttp2023.miniproject2.server.models.PokemonAdded;
import vttp2023.miniproject2.server.models.PokemonAttribute;
import vttp2023.miniproject2.server.models.PokemonTrainer;
import vttp2023.miniproject2.server.models.UserPost;

import org.springframework.stereotype.Repository;

import static vttp2023.miniproject2.server.mysql.Queries.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Repository 
public class MySQLRepository {
    
    @Autowired
	private JdbcTemplate jdbcTemplate;

	public String insertUser(PokemonTrainer user){
		
		Integer userInserted = 0;

		try{
		userInserted = jdbcTemplate.update(SQL_INSERT_TRAINERS, 
        user.getUsername(), user.getEmail(), user.getPassword(), user.getImage(), LocalDate.now());

		} catch(Exception e){
			System.out.println(e.getMessage());
		}

        if(userInserted > 0){
            return user.getUsername();
        }
		
		return null;
    }

    public PokemonTrainer logInUser(String email) {
		
		PokemonTrainer pokemonTrainer = new PokemonTrainer();

		try{
			SqlRowSet rs = jdbcTemplate.queryForRowSet(
            SQL_CHECK_FOR_USER, email);

			if (rs.first()){
				pokemonTrainer.setUsername(rs.getString("username"));
				pokemonTrainer.setEmail(rs.getString("email"));
				pokemonTrainer.setImage(rs.getString("image"));
				pokemonTrainer.setDate(rs.getString("date"));
				return pokemonTrainer;
			}
		}	catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return pokemonTrainer;			 
    }

	public boolean insertUserPokemon(PokemonAdded pokemon){

		Integer userInserted = 0;

		try{
		userInserted = jdbcTemplate.update(SQL_INSERT_POKEMONTEAM, 
        pokemon.getUsername(), pokemon.getPokemonName(), pokemon.getPokemonUrl(), pokemon.getPokemonTypes());
		}	catch(Exception e){
			System.out.println(e.getMessage());
		}

        if(userInserted > 0){
            return true;
        }

		return false;
    }

	public List<PokemonAttribute> getPokemonForUser(String username) {

		List<PokemonAttribute> userPokemonTeam = new LinkedList<>();

		try{
			SqlRowSet rs = jdbcTemplate.queryForRowSet(
				SQL_GETPOKEMON_FOR_USER, username);

			while (rs.next()){
				PokemonAttribute pokemonAttribute = new PokemonAttribute();
				pokemonAttribute.setName(rs.getString("pokemon_name"));
				pokemonAttribute.setImageUrl(rs.getString("pokemon_url"));
				
				String types = rs.getString("pokemon_type");
				if (types != null) {
					String[] typeArr = types.split(",");
					List<String> typeList = new LinkedList<>(Arrays.asList(typeArr));
					pokemonAttribute.setTypes(typeList);
				} else {
					
				}
				userPokemonTeam.add(pokemonAttribute);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());}

		return userPokemonTeam;			 
    }

	public boolean deleteUserPokemon(PokemonAdded pokemon){

		Integer userDeleted = 0;

		try{
			userDeleted = jdbcTemplate.update(SQL_DELETE_POKEMONTEAM, 
			pokemon.getUsername(), pokemon.getPokemonName());

		}	catch(Exception e){
			System.out.println(e.getMessage());
		}

        if(userDeleted > 0){
            return true;
        }
		
		return false;
    }

	public boolean deleteUser1(PokemonTrainer trainer){

		Integer	user1Deleted = jdbcTemplate.update(SQL_DELETE_USER1, 
			trainer.getUsername());
			


        if(user1Deleted > 0){
            return true;
        }
		
		return false;
    }

	public boolean deleteUser2(PokemonTrainer trainer){

		Integer user2Deleted = jdbcTemplate.update(SQL_DELETE_USER2, 
		trainer.getEmail());

        if(user2Deleted > 0 ){
            return true;
        }
		
		return false;
    }

	public boolean deleteUser3(PokemonTrainer trainer){

		Integer user3Deleted = jdbcTemplate.update(SQL_DELETE_USER3, 
		trainer.getUsername());

        if(user3Deleted > 0 ){
            return true;
        }
		
		return false;
    }

	public List<ForumEntry> getForum() {
		
		List<ForumEntry> forumEntries = new LinkedList<>();

		try{
			SqlRowSet rs = jdbcTemplate.queryForRowSet(
            SQL_GET_FORUM);

			while (rs.next()){
				ForumEntry forumEntry = new ForumEntry();
				forumEntry.setTitle(rs.getString("title"));
				forumEntry.setContent(rs.getString("content"));
				forumEntry.setUsername(rs.getString("username"));
				forumEntry.setImageUrl(rs.getString("image"));
				String date = (rs.getString("date"));
				if(date != null){
				forumEntry.setDate(date.replace("T", " "));
				}
				
				forumEntries.add(forumEntry);
			}
			return forumEntries;

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return forumEntries;			 
    }

	public boolean insertPost(ForumEntry forumEntry){

		Integer userInserted = 0;

		try{
		userInserted = jdbcTemplate.update(SQL_INSERT_INTO_FORUM, 
        forumEntry.getTitle(), forumEntry.getContent(), forumEntry.getUsername(), 
		forumEntry.getImageUrl(), LocalDateTime.now());

		}	catch(Exception e){
			System.out.println(e.getMessage());
		}

        if(userInserted > 0){
            return true;
        }

		return false;
    }

	public List<ForumEntry> getForumByUser(String username) {
		
		List<ForumEntry> forumEntries = new LinkedList<>();

		try{
			SqlRowSet rs = jdbcTemplate.queryForRowSet(
            SQL_GET_FORUM_BY_USER, username);

			while (rs.next()){
				ForumEntry forumEntry = new ForumEntry();
				forumEntry.setTitle(rs.getString("title"));
				forumEntry.setContent(rs.getString("content"));
				forumEntry.setUsername(rs.getString("username"));
				forumEntry.setImageUrl(rs.getString("image"));
				String date = (rs.getString("date"));
				if(date != null){
				forumEntry.setDate(date.replace("T", " "));
				}
				
				forumEntries.add(forumEntry);
			}
			return forumEntries;

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return forumEntries;			 
    }

	public boolean deleteUserPost(UserPost userPost) {
		
		Integer userInserted = 0;

		try{
			userInserted = jdbcTemplate.update(
            SQL_DELETE_USER_POST, userPost.getUsername(), userPost.getDate().replace(" ", "T"));

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		if(userInserted > 0){
            return true;
        }

		return false;	 
    }

}

