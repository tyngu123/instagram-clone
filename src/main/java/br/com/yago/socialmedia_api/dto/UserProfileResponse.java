package br.com.yago.socialmedia_api.dto;

import br.com.yago.socialmedia_api.model.User;

public class UserProfileResponse {
    private Long id;
    private String username;
    private String fullName;
    private long followerCount;
    private long followingCount;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.followerCount = user.getFollowers().size();
        this.followingCount = user.getFollowing().size();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public long getFollowerCount() { return followerCount; }
    public void setFollowerCount(long followerCount) { this.followerCount = followerCount; }
    public long getFollowingCount() { return followingCount; }
    public void setFollowingCount(long followingCount) { this.followingCount = followingCount; }
}