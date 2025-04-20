package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.BookAdapter_Tien;

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
        BookAdapter_Tien bookAdapterTien = new BookAdapter_Tien(libraryManager.getReadingList(), book -> {
            // Handle book click (e.g., open book details)
        });
        readingListRecyclerView.setAdapter(bookAdapterTien);
    }
}