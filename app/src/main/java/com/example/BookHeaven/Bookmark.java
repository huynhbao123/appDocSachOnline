package com.example.BookHeaven;

public class Bookmark {
    private int chapter;
    private int scrollPosition;

    public Bookmark(int chapter, int scrollPosition) {
        this.chapter = chapter;
        this.scrollPosition = scrollPosition;
    }

    public int getChapter() {
        return chapter;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }
}