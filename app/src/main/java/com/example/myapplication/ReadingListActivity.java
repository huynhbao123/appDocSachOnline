package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.BookAdapter;

public class ReadingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        LibraryManager libraryManager = LibraryManager.getInstance();
        BookAdapter bookAdapter = new BookAdapter(libraryManager.getReadingList(), book -> {
            Intent intent = new Intent(ReadingListActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("coverResourceId", book.getCoverResourceId());
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(bookAdapter);
    }
}