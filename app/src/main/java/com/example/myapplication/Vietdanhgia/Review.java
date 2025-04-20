package com.example.myapplication.Vietdanhgia;

public class Review {
    private String userName;
    private int userImage;
    private float rating;
    private String comment;

    public Review(String userName, int userImage, float rating, String comment) {
        this.userName = userName;
        this.userImage = userImage;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}