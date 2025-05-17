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
    private DatabaseReference userFavoritesRef;
    private DatabaseReference userReadingListRef;
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
        updateUserReferences();
        loadListsFromFirebase();
    }

    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    private void updateUserReferences() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            userFavoritesRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid())
                    .child("favorites");
            userReadingListRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid())
                    .child("readingList");
        } else {
            userFavoritesRef = null;
            userReadingListRef = null;
            favoritesList.clear();
            readingList.clear();
        }
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.dataLoadedListener = listener;
    }

    public List<Book> getReadingList() {
        return new ArrayList<>(readingList);
    }

    public List<Book> getFavoritesList() {
        return new ArrayList<>(favoritesList);
    }

    public boolean addToReadingList(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && !isBookInReadingList(book.getId())) {
            readingList.add(book);
            userReadingListRef.child(book.getId()).setValue(book)
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Added to reading list: " + book.getId()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to add to reading list: " + e.getMessage()));
            return true;
        }
        return false;
    }

    public boolean addToFavorites(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && !isBookInFavorites(book.getId())) {
            favoritesList.add(book);
            int currentLikes = book.getLikesCount();
            book.setLikes(String.valueOf(currentLikes + 1));
            booksRef.child(book.getId()).child("likes").setValue(book.getLikes())
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Updated book likes: " + book.getId() + " - Likes: " + book.getLikes()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update likes: " + e.getMessage()));
            userFavoritesRef.child(book.getId()).setValue(book)
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Added to favorites: " + book.getId()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to add favorite: " + e.getMessage()));
            return true;
        }
        return false;
    }

    public boolean removeFromReadingList(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && isBookInReadingList(book.getId())) {
            removeBookFromReadingList(book.getId());
            userReadingListRef.child(book.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Removed from reading list: " + book.getId()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to remove from reading list: " + e.getMessage()));
            return true;
        }
        return false;
    }

    public boolean removeFromFavorites(Context context, Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        }

        if (book != null && isBookInFavorites(book.getId())) {
            removeBookFromFavorites(book.getId());
            int currentLikes = book.getLikesCount();
            if (currentLikes > 0) {
                book.setLikes(String.valueOf(currentLikes - 1));
            }
            booksRef.child(book.getId()).child("likes").setValue(book.getLikes())
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Updated book likes: " + book.getId() + " - Likes: " + book.getLikes()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update likes: " + e.getMessage()));
            userFavoritesRef.child(book.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Removed from favorites: " + book.getId()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to remove favorite: " + e.getMessage()));
            return true;
        }
        return false;
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

    private void loadListsFromFirebase() {
        isLoading = true;
        loadedLists = 0;

        // Load reading list from users/<uid>/readingList
        if (userReadingListRef != null) {
            userReadingListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("LibraryManager", "Loading readingList node: " + snapshot.getRef() + ", Data: " + snapshot.getValue());
                    readingList.clear();
                    for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                        Log.d("LibraryManager", "ReadingList child: " + bookSnapshot.getKey() + ", Value: " + bookSnapshot.getValue());
                        try {
                            Book book = bookSnapshot.getValue(Book.class);
                            if (book != null && isValidBook(book)) {
                                readingList.add(book);
                            } else {
                                Log.w("LibraryManager", "Invalid book data skipped in readingList: " + bookSnapshot.getKey());
                            }
                        } catch (Exception e) {
                            Log.e("LibraryManager", "Error parsing book in readingList: " + bookSnapshot.getKey() + ", Error: " + e.getMessage());
                        }
                    }
                    loadedLists++;
                    checkIfLoadingComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("LibraryManager", "Error loading reading list: " + error.getMessage());
                    loadedLists++;
                    checkIfLoadingComplete();
                }
            });
        } else {
            readingList.clear();
            loadedLists++;
            checkIfLoadingComplete();
        }

        // Load favorites from users/<uid>/favorites
        if (userFavoritesRef != null) {
            userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("LibraryManager", "Loading favorites node: " + snapshot.getRef() + ", Data: " + snapshot.getValue());
                    favoritesList.clear();
                    for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                        Log.d("LibraryManager", "Favorites child: " + bookSnapshot.getKey() + ", Value: " + bookSnapshot.getValue());
                        try {
                            Book book = bookSnapshot.getValue(Book.class);
                            if (book != null && isValidBook(book)) {
                                favoritesList.add(book);
                            } else {
                                Log.w("LibraryManager", "Invalid book data skipped in favorites: " + bookSnapshot.getKey());
                            }
                        } catch (Exception e) {
                            Log.e("LibraryManager", "Error parsing book in favorites: " + bookSnapshot.getKey() + ", Error: " + e.getMessage());
                        }
                    }
                    loadedLists++;
                    checkIfLoadingComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("LibraryManager", "Error loading favorites: " + error.getMessage());
                    loadedLists++;
                    checkIfLoadingComplete();
                }
            });
        } else {
            favoritesList.clear();
            loadedLists++;
            checkIfLoadingComplete();
        }
    }

    private boolean isValidBook(Book book) {
        return book != null &&
                book.getId() != null &&
                book.getTitle() != null &&
                book.getAuthor() != null &&
                book.getImageUrl() != null;
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

        for (Book book : favoritesList) {
            if (book.getId() != null && book.getId().equals(bookId)) {
                return true;
            }
        }

        return false;
    }

    public void incrementBookViews(Book book) {
        if (book != null && book.getId() != null) {
            int currentViews = book.getViewsCount();
            book.setViews(String.valueOf(currentViews + 1));
            booksRef.child(book.getId()).child("views").setValue(book.getViews())
                    .addOnSuccessListener(aVoid -> Log.d("LibraryManager", "Updated book views: " + book.getId() + " - Views: " + book.getViews()))
                    .addOnFailureListener(e -> Log.e("LibraryManager", "Failed to update views: " + e.getMessage()));
        }
    }

    public void onAuthStateChanged() {
        updateUserReferences();
        loadListsFromFirebase();
    }
}