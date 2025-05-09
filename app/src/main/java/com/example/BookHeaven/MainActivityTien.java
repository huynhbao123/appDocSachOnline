package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.adapter.CategoryAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.models.Category;
import com.example.BookHeaven.sach.ChiTietSach;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityTien extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tien);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigation();

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        loadCategoriesFromFirebase();

        LibraryManager libraryManager = LibraryManager.getInstance();
        // Thêm sách vào danh sách đọc và yêu thích (dựa trên dữ liệu từ Firebase nếu có)
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadCategoriesFromFirebase() {
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

                List<Category> categories = new ArrayList<>();
                // Lọc sách theo danh mục
                categories.add(new Category("1", "Sách Tiểu thuyết", filterBooksByCategory(allBooks, "novel")));
                categories.add(new Category("2", "Sách Kinh tế", filterBooksByCategory(allBooks, "economic")));
                categories.add(new Category("3", "Sách Tình cảm", filterBooksByCategory(allBooks, "emotional")));

                categoryAdapter = new CategoryAdapter(categories, new CategoryAdapter.OnCategoryClickListener() {
                    @Override
                    public void onCategoryClick(Category category) {
                        Intent intent = new Intent(MainActivityTien.this, CategoryDetailActivity.class);
                        intent.putExtra("category_id", category.getId());
                        intent.putExtra("category_title", category.getTitle());
                        startActivity(intent);
                    }

                    @Override
                    public void onBookClick(Category category, int bookPosition) {
                        Book book = category.getBooks().get(bookPosition);
                        Intent intent = new Intent(MainActivityTien.this, ChiTietSach.class);
                        intent.putExtra("book", book);
                        startActivity(intent);
                    }
                });
                categoriesRecyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private List<Book> filterBooksByCategory(List<Book> books, String category) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book != null && category.equals(book.getCategory())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
}