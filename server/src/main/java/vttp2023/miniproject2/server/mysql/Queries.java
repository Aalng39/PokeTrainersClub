package vttp2023.miniproject2.server.mysql;

public class Queries {
    
    public static final String SQL_CHECK_FOR_USER = "SELECT username, email, image, date FROM pokemontrainers where email = ?";

    public static final String SQL_INSERT_TRAINERS = "INSERT into pokemontrainers(username, email, password, image, date) values(?, ?, ?, ?, ?)";
    
    public static final String SQL_INSERT_POKEMONTEAM = "INSERT into pokemonteam(username, pokemon_name, pokemon_url, pokemon_type) values(?, ?, ?, ?)";

    public static final String SQL_DELETE_POKEMONTEAM = "DELETE FROM pokemonteam where username = ? and pokemon_name = ?";

    public static final String SQL_GETPOKEMON_FOR_USER = "SELECT pokemon_name, pokemon_url, pokemon_type FROM pokemonteam where username = ?";

    public static final String SQL_DELETE_USER1 = "DELETE FROM pokemonteam where username = ?";

    public static final String SQL_DELETE_USER2 = "DELETE FROM pokemontrainers where email = ?";

    public static final String SQL_DELETE_USER3 = "DELETE FROM pokemonforums where username = ?";

    public static final String SQL_GET_FORUM = "SELECT * FROM pokemonforums";

    public static final String SQL_INSERT_INTO_FORUM = "INSERT into pokemonforums(title, content, username, image, date) values(?, ?, ?, ?, ?)";

    public static final String SQL_GET_FORUM_BY_USER = "SELECT * FROM pokemonforums where username = ?";

    public static final String SQL_DELETE_USER_POST = "DELETE FROM pokemonforums where username = ? and date = ?";
}   
