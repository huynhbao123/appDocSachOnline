package com.example.myapplication.models;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String title;
    private int coverResourceId;
    private String author;
    private String description;
    private String publicationDate;
    private int pageCount;
    private String views;
    private String likes;
    private float averageRating;

    // Constructor đầy đủ với 10 tham số
    public Book(String id, String title, int coverResourceId, String author, String description,
                String publicationDate, int pageCount, String views, String likes, float averageRating) {
        this.id = id;
        this.title = title;
        this.coverResourceId = coverResourceId;
        this.author = author;
        this.description = description;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.views = views;
        this.likes = likes;
        this.averageRating = averageRating;
    }

    // Constructor mới với 3 tham số, đặt giá trị mặc định cho các tham số còn lại
    public Book(String id, String title, int coverResourceId) {
        this.id = id;
        this.title = title;
        this.coverResourceId = coverResourceId;
        this.author = "Unknown Author";
        this.description = "No description available.";
        this.publicationDate = "Unknown Date";
        this.pageCount = 0;
        this.views = "0";
        this.likes = "0";
        this.averageRating = 0.0f;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getViews() {
        return views;
    }

    public String getLikes() {
        return likes;
    }

    public float getAverageRating() {
        return averageRating;
    }
}