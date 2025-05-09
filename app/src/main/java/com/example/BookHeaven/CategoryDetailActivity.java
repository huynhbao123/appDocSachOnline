package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {
    private RecyclerView booksGrid;
    private BookAdapter bookAdapter;
    private TextView categoryTitle;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        String categoryId = getIntent().getStringExtra("category_id");
        String title = getIntent().getStringExtra("category_title");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText(title);

        booksGrid = findViewById(R.id.booksGrid);
        booksGrid.setLayoutManager(new GridLayoutManager(this, 2));

        setupBottomNavigation();

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        loadBooksFromFirebase(categoryId);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivityTien.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_menu) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadBooksFromFirebase(String categoryId) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> allBooks = new ArrayList<>();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        allBooks.add(book);
                    }
                }

                List<Book> filteredBooks = new ArrayList<>();
                String categoryName = "";
                if ("1".equals(categoryId)) categoryName = "novel";
                else if ("2".equals(categoryId)) categoryName = "economic";
                else if ("3".equals(categoryId)) categoryName = "emotional";

                for (Book book : allBooks) {
                    if (book != null && categoryName.equals(book.getCategory())) {
                        filteredBooks.add(book);
                    }
                }

                bookAdapter = new BookAdapter(filteredBooks, book -> {
                    Intent intent = new Intent(CategoryDetailActivity.this, ChiTietSach.class);
                    intent.putExtra("book", book);
                    startActivity(intent);
                });
                booksGrid.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}