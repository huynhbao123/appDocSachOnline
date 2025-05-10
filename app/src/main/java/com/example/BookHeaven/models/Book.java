package com.example.BookHeaven.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Book implements Serializable {
    private String id;
    private String title;
    private String imageUrl;
    private String author;
    private String description;
    private String publicationDate;
    private int pageCount;
    private String views;
    private String likes;
    private float averageRating;
    private String category;
    private List<Chapter> chapters;
    private boolean favorites;
    private boolean readingList;

    public static class Chapter implements Serializable {
        private String title;
        private String content;

        public Chapter() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public Book(String id, String title, String imageUrl, String author, String description,
                String publicationDate, int pageCount, String views, String likes, float averageRating,
                String category, List<Chapter> chapters, boolean favorites, boolean readingList) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.author = author;
        this.description = description;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.views = views;
        this.likes = likes;
        this.averageRating = averageRating;
        this.category = category;
        this.chapters = chapters;
        this.favorites = favorites;
        this.readingList = readingList;
    }

    public Book() {}

    public Book(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.author = "Unknown Author";
        this.description = "No description available.";
        this.publicationDate = "Unknown Date";
        this.pageCount = 0;
        this.views = "0";
        this.likes = "0";
        this.averageRating = 0.0f;
        this.category = "uncategorized";
        this.chapters = null;
        this.favorites = false;
        this.readingList = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getPublicationDate() { return publicationDate; }
    public int getPageCount() { return pageCount; }
    public String getViews() { return views; }
    public String getLikes() { return likes; }
    public float getAverageRating() { return averageRating; }
    public String getCategory() { return category; }
    public List<Chapter> getChapters() { return chapters; }
    public boolean isFavorites() { return favorites; }
    public boolean isReadingList() { return readingList; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }
    public void setPageCount(int pageCount) { this.pageCount = pageCount; }
    public void setViews(Object views) {
        if (views instanceof Number) {
            this.views = String.valueOf(((Number) views).intValue());
        } else if (views instanceof String) {
            this.views = (String) views;
        } else {
            this.views = "0";
        }
    }
    public void setLikes(Object likes) {
        if (likes instanceof Number) {
            this.likes = String.valueOf(((Number) likes).intValue());
        } else if (likes instanceof String) {
            this.likes = (String) likes;
        } else {
            this.likes = "0";
        }
    }
    public void setAverageRating(Object averageRating) {
        if (averageRating instanceof Number) {
            this.averageRating = ((Number) averageRating).floatValue();
        } else if (averageRating instanceof String) {
            try {
                this.averageRating = Float.parseFloat((String) averageRating);
            } catch (NumberFormatException e) {
                this.averageRating = 0.0f;
            }
        } else {
            this.averageRating = 0.0f;
        }
    }
    public void setCategory(String category) { this.category = category; }
    public void setChapters(List<Chapter> chapters) {
        if (chapters != null) {
            this.chapters = new ArrayList<>();
            for (Chapter chapter : chapters) {
                if (chapter != null) {
                    this.chapters.add(chapter);
                }
            }
        } else {
            this.chapters = null;
        }
    }
    public void setFavorites(boolean favorites) { this.favorites = favorites; }
    public void setReadingList(boolean readingList) { this.readingList = readingList; }

    // Add methods to get numeric values for views and likes
    public int getViewsCount() {
        try {
            return Integer.parseInt(views.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getLikesCount() {
        try {
            return Integer.parseInt(likes.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
