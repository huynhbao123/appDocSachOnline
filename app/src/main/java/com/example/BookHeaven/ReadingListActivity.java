package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;

public class ReadingListActivity extends AppCompatActivity {

    private RecyclerView readingListRecyclerView;
    private TextView emptyMessage;
    private BookAdapter bookAdapter;
    private LibraryManager libraryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        emptyMessage = findViewById(R.id.emptyMessage);
        readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        libraryManager = LibraryManager.getInstance();
        bookAdapter = new BookAdapter(libraryManager.getReadingList(), book -> {
            Intent intent = new Intent(ReadingListActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(bookAdapter);

        // Set listener to update UI when data is loaded
        libraryManager.setOnDataLoadedListener(this::updateUI);

        // Update UI immediately if data is already loaded
        if (!libraryManager.isLoading()) {
            updateUI();
        }
    }

    private void updateUI() {
        if (libraryManager.getReadingList().isEmpty()) {
            emptyMessage.setVisibility(TextView.VISIBLE);
            readingListRecyclerView.setVisibility(RecyclerView.GONE);
        } else {
            emptyMessage.setVisibility(TextView.GONE);
            readingListRecyclerView.setVisibility(RecyclerView.VISIBLE);
        }
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update UI when resuming, in case data changed
        updateUI();
    }
}