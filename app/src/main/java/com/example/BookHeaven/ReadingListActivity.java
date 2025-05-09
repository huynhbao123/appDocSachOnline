package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
import com.squareup.picasso.Picasso;

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
            Intent intent = new Intent(ReadingListActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(bookAdapter);
    }
}