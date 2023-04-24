package vttp2023.miniproject2.server.models;

public class PokemonTrainer {
    
    private String username;
    private String email;
    private String password;
    private String image;
    private String date;

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

}
