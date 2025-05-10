package com.example.BookHeaven;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private static LibraryManager instance;
    private List<Book> readingList;
    private List<Book> favoritesList;
    private DatabaseReference booksRef;
    private boolean isLoading = false;
    private OnDataLoadedListener dataLoadedListener;
    private int loadedLists = 0;

    public interface OnDataLoadedListener {
        void onDataLoaded();
    }

    private LibraryManager() {
        readingList = new ArrayList<>();
        favoritesList = new ArrayList<>();
        booksRef = FirebaseDatabase.getInstance().getReference("books");
        loadListsFromFirebase();
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.dataLoadedListener = listener;
    }

    public List<Book> getReadingList() {
        return readingList;
    }

    public List<Book> getFavoritesList() {
        return favoritesList;
    }

    public boolean addToReadingList(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && !isBookInReadingList(book.getId())) {
            book.setReadingList(true);
            readingList.add(book);
            updateBookInFirebase(book);
        }
        return true;
    }

    public boolean addToFavorites(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && !isBookInFavorites(book.getId())) {
            book.setFavorites(true);
            favoritesList.add(book);

            // Increment likes count
            int currentLikes = book.getLikesCount();
            book.setLikes(String.valueOf(currentLikes + 1));

            // Update the likes count in Firebase first
            booksRef.child(book.getId()).child("likes").setValue(book.getLikes())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("LibraryManager", "Updated book likes in Firebase: " + book.getId() + " - Likes: " + book.getLikes());
                        // Then update the full book object
                        updateBookInFirebase(book);
                    })
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update book likes: " + e.getMessage()));
        }
        return true;
    }

    public boolean removeFromReadingList(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && isBookInReadingList(book.getId())) {
            book.setReadingList(false);
            removeBookFromReadingList(book.getId());
            updateBookInFirebase(book);
        }
        return true;
    }

    public boolean removeFromFavorites(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && isBookInFavorites(book.getId())) {
            book.setFavorites(false);
            removeBookFromFavorites(book.getId());

            // Decrement likes count, but ensure it doesn't go below 0
            int currentLikes = book.getLikesCount();
            if (currentLikes > 0) {
                book.setLikes(String.valueOf(currentLikes - 1));
            }

            // Update the likes count in Firebase first
            booksRef.child(book.getId()).child("likes").setValue(book.getLikes())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("LibraryManager", "Updated book likes in Firebase: " + book.getId() + " - Likes: " + book.getLikes());
                        // Then update the full book object
                        updateBookInFirebase(book);
                    })
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update book likes: " + e.getMessage()));

            return true;
        }
        return true;
    }

    private boolean isBookInReadingList(String bookId) {
        for (Book book : readingList) {
            if (book.getId() != null && book.getId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBookInFavorites(String bookId) {
        for (Book book : favoritesList) {
            if (book.getId() != null && book.getId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    private void removeBookFromReadingList(String bookId) {
        readingList.removeIf(book -> book.getId() != null && book.getId().equals(bookId));
    }

    private void removeBookFromFavorites(String bookId) {
        favoritesList.removeIf(book -> book.getId() != null && book.getId().equals(bookId));
    }

    private void updateBookInFirebase(Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified() && book != null && book.getId() != null) {
            // First update the book's favorite status in Firebase
            booksRef.child(book.getId()).child("favorites").setValue(book.isFavorites())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("LibraryManager", "Updated book favorite status in Firebase: " + book.getId() + " - isFavorite: " + book.isFavorites());

                        // Then update the full book object
                        booksRef.child(book.getId()).setValue(book)
                                .addOnSuccessListener(aVoid2 -> {
                                    Log.d("LibraryManager", "Updated book in Firebase: " + book.getId());
                                    // Đồng bộ lại favoritesList và readingList sau khi cập nhật Firebase
                                    if (book.isFavorites() && !isBookInFavorites(book.getId())) {
                                        favoritesList.add(book);
                                    } else if (!book.isFavorites() && isBookInFavorites(book.getId())) {
                                        removeBookFromFavorites(book.getId());
                                    }
                                    if (book.isReadingList() && !isBookInReadingList(book.getId())) {
                                        readingList.add(book);
                                    } else if (!book.isReadingList() && isBookInReadingList(book.getId())) {
                                        removeBookFromReadingList(book.getId());
                                    }
                                })
                                .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update book: " + e.getMessage()));
                    })
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update book favorite status: " + e.getMessage()));
        }
    }

    private void loadListsFromFirebase() {
        isLoading = true;
        loadedLists = 0;
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");

        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isLoading) {
                    readingList.clear();
                    favoritesList.clear();
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                        Book book = bookSnapshot.getValue(Book.class);
                        if (book != null) {
                            if (book.isReadingList()) readingList.add(book);
                            if (book.isFavorites()) favoritesList.add(book);
                        }
                    }
                }

                loadedLists = 2;
                checkIfLoadingComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LibraryManager", "Error loading lists: " + error.getMessage());
                isLoading = false;
                checkIfLoadingComplete();
            }
        });
    }

    private void checkIfLoadingComplete() {
        if (loadedLists == 2) {
            isLoading = false;
            Log.d("LibraryManager", "Data loading complete. Favorites: " + favoritesList.size() + ", Reading: " + readingList.size());
            if (dataLoadedListener != null) {
                dataLoadedListener.onDataLoaded();
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isBookFavorite(String bookId) {
        if (bookId == null) return false;

        // First check in our local list
        for (Book book : favoritesList) {
            if (book.getId() != null && book.getId().equals(bookId)) {
                return true;
            }
        }

        // If not found and we're still loading, check directly in Firebase
        if (isLoading) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.isEmailVerified()) {
                // Note: This is a synchronous check, which is not ideal but necessary for immediate UI feedback
                try {
                    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("books").child(bookId);
                    DataSnapshot dataSnapshot = bookRef.get().getResult();
                    if (dataSnapshot.exists()) {
                        Boolean isFavorite = dataSnapshot.child("favorites").getValue(Boolean.class);
                        return isFavorite != null && isFavorite;
                    }
                } catch (Exception e) {
                    Log.e("LibraryManager", "Error checking favorite status: " + e.getMessage());
                }
            }
        }

        return false;
    }

    // Add a method to increment view count
    public void incrementBookViews(Book book) {
        if (book != null && book.getId() != null) {
            int currentViews = book.getViewsCount();
            book.setViews(String.valueOf(currentViews + 1));

            // Update only the views field in Firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.isEmailVerified()) {
                booksRef.child(book.getId()).child("views").setValue(book.getViews())
                        .addOnSuccessListener(aVoid -> {
                            Log.d("LibraryManager", "Updated book views in Firebase: " + book.getId() + " - Views: " + book.getViews());
                        })
                        .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update book views: " + e.getMessage()));
            }
        }
    }
}
