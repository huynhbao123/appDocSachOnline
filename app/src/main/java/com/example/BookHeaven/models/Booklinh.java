package com.example.BookHeaven.models;

public class Booklinh {
    private String id;
    private String title;
    private String imageUrl;
    private String category;

    public Booklinh(String id, String title, String imageUrl, String category) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}