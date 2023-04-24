package vttp2023.miniproject2.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ForumEntry {
    
    private String title;
    private String content;
    private String username;
    private String imageUrl;
    private String date;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public JsonObject toJson() {

        return Json.createObjectBuilder()
            .add("title", getTitle())
            .add("content", getContent())
            .add("username", getUsername())
            .add("imageUrl", getImageUrl())
            .add("date", getDate())
            .build();
    }
}
