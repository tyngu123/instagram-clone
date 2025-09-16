package br.com.yago.socialmedia_api.dto;

public class UserSummaryResponse {
    private Long id;
    private String username;
    private String profilePictureUrl; // <-- NOVO CAMPO ADICIONADO

    public UserSummaryResponse(Long id, String username, String profilePictureUrl) { // <-- CONSTRUTOR ATUALIZADO
        this.id = id;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl; // <-- ATRIBUINDO O VALOR
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getProfilePictureUrl() { return profilePictureUrl; } // <-- GETTER PARA O NOVO CAMPO
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}