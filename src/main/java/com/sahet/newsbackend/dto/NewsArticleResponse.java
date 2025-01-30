package com.sahet.newsbackend.dto;

import java.time.LocalDateTime;

public class NewsArticleResponse {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    private Long id;
    private String title;
    private String imageUrl;
    private String categoryName;
    private LocalDateTime publishedAt;

    // Constructor
    public NewsArticleResponse(Long id, String title, String imageUrl, String categoryName, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.publishedAt = publishedAt;
    }

    // Getters and setters
}

