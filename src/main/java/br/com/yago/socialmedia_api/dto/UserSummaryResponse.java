package br.com.yago.socialmedia_api.dto;

public class UserSummaryResponse {
    private Long id;
    private String username;

    public UserSummaryResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}