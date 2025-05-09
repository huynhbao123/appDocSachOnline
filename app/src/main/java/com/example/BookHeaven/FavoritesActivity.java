package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.adapter.BookAdapter;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        LibraryManager libraryManager = LibraryManager.getInstance();
        BookAdapter bookAdapter = new BookAdapter(libraryManager.getFavoritesList(), book -> {
            Intent intent = new Intent(FavoritesActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("coverResourceId", book.getCoverResourceId());
            startActivity(intent);
        });
        favoritesRecyclerView.setAdapter(bookAdapter);
    }
}