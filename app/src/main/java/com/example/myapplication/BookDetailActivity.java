package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;


public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");
        String bookTitle = intent.getStringExtra("bookTitle");

        // Ánh xạ các thành phần giao diện
        TextView tvBookId = findViewById(R.id.tvBookId);
        TextView tvBookTitle = findViewById(R.id.tvBookTitle);

        // Hiển thị thông tin sách
        tvBookId.setText("Book ID: " + bookId);
        tvBookTitle.setText("Title: " + bookTitle);
    }
}