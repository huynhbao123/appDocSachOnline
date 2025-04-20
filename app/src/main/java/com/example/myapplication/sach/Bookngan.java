package com.example.myapplication.sach;


import java.io.Serializable;

public class Bookngan implements Serializable {
    private String title;
    private String author;
    private int coverImage; // Sử dụng int thay vì String
    private String description;
    private String publicationDate;
    private int pageCount;
    private String views;
    private String likes;
    private float averageRating;

    public Bookngan(String title, String author, int coverImage, String description, String publicationDate, String views, String likes, float averageRating) {
        this.title = title;
        this.author = author;
        this.coverImage = coverImage;
        this.description = description;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.views = views;
        this.likes = likes;
        this.averageRating = averageRating;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getCoverImage() { return coverImage; }
    public String getDescription() { return description; }
    public String getPublicationDate() { return publicationDate; }
    public int getPageCount() {
        return pageCount;
    }
    public String getViews() { return views; }
    public String getLikes() { return likes; }
    public float getAverageRating() { return averageRating; }


}