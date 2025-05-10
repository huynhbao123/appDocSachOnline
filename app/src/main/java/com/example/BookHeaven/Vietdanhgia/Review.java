package com.example.BookHeaven.Vietdanhgia;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Review implements Serializable {
    private String userId;
    private String userName;
    private int userImage;
    private float rating;
    private String comment;
    private String bookId;
    private long timestamp;

    // Default constructor required for Firebase
    public Review() {
    }

    public Review(String userName, int userImage, float rating, String comment) {
        this.userName = userName;
        this.userImage = userImage;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
    }

    public Review(String userId, String userName, int userImage, float rating, String comment, String bookId) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.rating = rating;
        this.comment = comment;
        this.bookId = bookId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
