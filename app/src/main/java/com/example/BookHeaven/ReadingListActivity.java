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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        TextView emptyMessage = findViewById(R.id.emptyMessage);
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        LibraryManager libraryManager = LibraryManager.getInstance();
        BookAdapter bookAdapter = new BookAdapter(libraryManager.getReadingList(), book -> {
            Intent intent = new Intent(ReadingListActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(bookAdapter);

        // Hiển thị thông báo nếu danh sách rỗng
        if (libraryManager.getReadingList().isEmpty()) {
            emptyMessage.setVisibility(TextView.VISIBLE);
            readingListRecyclerView.setVisibility(RecyclerView.GONE);
        } else {
            emptyMessage.setVisibility(TextView.GONE);
            readingListRecyclerView.setVisibility(RecyclerView.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại RecyclerView khi quay lại activity
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        TextView emptyMessage = findViewById(R.id.emptyMessage);
        LibraryManager libraryManager = LibraryManager.getInstance();
        BookAdapter adapter = (BookAdapter) readingListRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (libraryManager.getReadingList().isEmpty()) {
                emptyMessage.setVisibility(TextView.VISIBLE);
                readingListRecyclerView.setVisibility(RecyclerView.GONE);
            } else {
                emptyMessage.setVisibility(TextView.GONE);
                readingListRecyclerView.setVisibility(RecyclerView.VISIBLE);
            }
        }
    }
}