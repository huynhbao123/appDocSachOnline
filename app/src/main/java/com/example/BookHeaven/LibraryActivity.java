package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.TimKiem.SearchActivity;
import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

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
                Intent intent = new Intent(this, TrangChu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, LibraryActivity.class));
                return true;
            }
            return false;
        });
    }

    private List<Book> getLimitedList(List<Book> books, int limit) {
        if (books.size() <= limit) {
            return new ArrayList<>(books);
        }
        return new ArrayList<>(books.subList(0, limit));
    }
}