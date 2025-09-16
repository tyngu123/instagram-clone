package br.com.yago.socialmedia_api.dto;

import br.com.yago.socialmedia_api.model.Media;

public class MediaResponse {
    private Long id;
    private String imageUrl;

    public MediaResponse(Media media) {
        this.id = media.getId();
        this.imageUrl = media.getImageUrl();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}