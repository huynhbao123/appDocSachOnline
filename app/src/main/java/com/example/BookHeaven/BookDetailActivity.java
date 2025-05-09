package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detaillinh);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");
        String bookTitle = intent.getStringExtra("bookTitle");
        // coverResourceId được gửi nhưng không dùng trong giao diện gốc
        // int coverResourceId = intent.getIntExtra("coverResourceId", 0);

        // Ánh xạ các thành phần giao diện
        TextView tvBookId = findViewById(R.id.tvBookId);
        TextView tvBookTitle = findViewById(R.id.tvBookTitle);

        // Hiển thị thông tin sách
        tvBookId.setText(bookId != null ? "Book ID: " + bookId : "Book ID: N/A");
        tvBookTitle.setText(bookTitle != null ? "Title: " + bookTitle : "Title: N/A");
    }
}