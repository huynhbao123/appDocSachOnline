package com.example.myapplication.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}