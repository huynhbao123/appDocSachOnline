package com.example.appds.model;

public class Book {
    private String id;
    private String title;
    private int coverResourceId;

    public Book(String id, String title, int coverResourceId) {
        this.id = id;
        this.title = title;
        this.coverResourceId = coverResourceId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }
} 