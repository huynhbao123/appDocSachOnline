package com.example.myapplication;

import com.example.myapplication.model.Book;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private static LibraryManager instance;
    private List<Book> readingList;
    private List<Book> favoritesList;

    private LibraryManager() {
        readingList = new ArrayList<>();
        favoritesList = new ArrayList<>();
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    public List<Book> getReadingList() {
        return readingList;
    }

    public List<Book> getFavoritesList() {
        return favoritesList;
    }

    public void addToReadingList(Book book) {
        if (!readingList.contains(book)) {
            readingList.add(book);
        }
    }

    public void addToFavorites(Book book) {
        if (!favoritesList.contains(book)) {
            favoritesList.add(book);
        }
    }

    public void removeFromReadingList(Book book) {
        readingList.remove(book);
    }

    public void removeFromFavorites(Book book) {
        favoritesList.remove(book);
    }
}