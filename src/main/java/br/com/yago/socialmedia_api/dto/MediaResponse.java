package br.com.yago.socialmedia_api.dto;

import br.com.yago.socialmedia_api.model.Media;

public class MediaResponse {
    private Long id;
    private String mediaUrl;
    private Media.MediaType mediaType;

    public MediaResponse(Media media) {
        this.id = media.getId();
        this.mediaUrl = media.getMediaUrl();
        this.mediaType = media.getMediaType();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public Media.MediaType getMediaType() { return mediaType; }
    public void setMediaType(Media.MediaType mediaType) { this.mediaType = mediaType; }
}