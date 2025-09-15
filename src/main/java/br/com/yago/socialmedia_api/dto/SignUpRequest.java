package br.com.yago.socialmedia_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String fullName;
    @NotBlank @Size(min = 3, max = 50)
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 100)
    private String password;

    // Getters e Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}