package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class LibraryActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Xử lý click cho CardView
        findViewById(R.id.readingListCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, ReadingListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.favoritesCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        loadInitialDataFromFirebase();

        // Thiết lập RecyclerView cho Danh sách đọc (giới hạn 6 sách)
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        LibraryManager libraryManager = LibraryManager.getInstance();
        List<Book> readingListLimited = getLimitedList(libraryManager.getReadingList(), 6);
        BookAdapter readingListAdapter = new BookAdapter(readingListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(readingListAdapter);

        // Thiết lập RecyclerView cho Danh sách yêu thích (giới hạn 6 sách)
        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Book> favoritesListLimited = getLimitedList(libraryManager.getFavoritesList(), 6);
        BookAdapter favoritesAdapter = new BookAdapter(favoritesListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, TrangChu.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_menu) {
                return true;
            }
            return false;
        });
    }

    private void loadInitialDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LibraryManager libraryManager = LibraryManager.getInstance();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        // Thêm dữ liệu mẫu vào danh sách đọc nếu chưa có
                        if (libraryManager.getReadingList().isEmpty() && (book.getId().equals("1") || book.getId().equals("2") || book.getId().equals("3") ||
                                book.getId().equals("10") || book.getId().equals("4") || book.getId().equals("5"))) {
                            libraryManager.addToReadingList(book);
                        }
                        // Thêm dữ liệu mẫu vào danh sách yêu thích nếu chưa có
                        if (libraryManager.getFavoritesList().isEmpty() && (book.getId().equals("6") || book.getId().equals("7") || book.getId().equals("8") ||
                                book.getId().equals("9") || book.getId().equals("1") || book.getId().equals("2"))) {
                            libraryManager.addToFavorites(book);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private List<Book> getLimitedList(List<Book> books, int limit) {
        if (books.size() <= limit) {
            return new ArrayList<>(books);
        }
        return new ArrayList<>(books.subList(0, limit));
    }
}