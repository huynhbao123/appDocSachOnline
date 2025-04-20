package com.example.appds.model;

import java.util.List;

public class Category {
    private String id;
    private String title;
    private List<Book> books;

    public Category(String id, String title, List<Book> books) {
        this.id = id;
        this.title = title;
        this.books = books;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Book> getBooks() {
        return books;
    }
} 