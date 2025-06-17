package com.example.en.model;

public class RssFeed {
    private String title;
    private String url;
    private String description;
    private String imageUrl;

    public RssFeed(String title, String url, String description, String imageUrl) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setUrl(String url) { this.url = url; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
} 