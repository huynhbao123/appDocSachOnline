package com.example.myapplication.model;

public class Category {
    private String id;
    private String name;
    private String imageUrl;
    private int bookCount;

    public Category(String id, String name, String imageUrl, int bookCount) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.bookCount = bookCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}