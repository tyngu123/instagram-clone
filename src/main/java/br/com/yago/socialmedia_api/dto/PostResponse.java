package br.com.yago.socialmedia_api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.yago.socialmedia_api.model.Post;

public class PostResponse {
    private Long id;
    private String caption;
    private LocalDateTime createdAt;
    private UserSummaryResponse author;
    private List<MediaResponse> media;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.caption = post.getCaption();
        this.createdAt = post.getCreatedAt();
        // --- LINHA MODIFICADA ABAIXO ---
        // Agora passamos a URL da foto do autor para o construtor
        this.author = new UserSummaryResponse(post.getAuthor().getId(), post.getAuthor().getUsername(), post.getAuthor().getProfilePictureUrl());
        this.media = post.getMedia().stream().map(MediaResponse::new).collect(Collectors.toList());
    }

    // Getters e Setters (não precisam de mudança)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public UserSummaryResponse getAuthor() { return author; }
    public void setAuthor(UserSummaryResponse author) { this.author = author; }
    public List<MediaResponse> getMedia() { return media; }
    public void setMedia(List<MediaResponse> media) { this.media = media; }
}