package com.example.appds;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appds.adapter.BookAdapter;
import com.example.appds.model.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Giữ nguyên xử lý click cho các CardView
        findViewById(R.id.readingListCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, ReadingListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.favoritesCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Lấy danh sách từ LibraryManager
        LibraryManager libraryManager = LibraryManager.getInstance();

        // Thiết lập RecyclerView cho Danh sách đọc (giới hạn 3 sách)
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        List<Book> readingListLimited = getLimitedList(libraryManager.getReadingList(), 6);
        BookAdapter readingListAdapter = new BookAdapter(readingListLimited, book -> {
            // Xử lý khi nhấn vào sách (nếu cần)
        });
        readingListRecyclerView.setAdapter(readingListAdapter);

        // Thiết lập RecyclerView cho Danh sách yêu thích (giới hạn 3 sách)
        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        List<Book> favoritesListLimited = getLimitedList(libraryManager.getFavoritesList(), 6);
        BookAdapter favoritesAdapter = new BookAdapter(favoritesListLimited, book -> {
            // Xử lý khi nhấn vào sách (nếu cần)
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Thiết lập bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivityTien.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_menu) {
                return true;
            }
            return false;
        });
    }

    // Hàm tiện ích để giới hạn danh sách sách
    private List<Book> getLimitedList(List<Book> books, int limit) {
        if (books.size() <= limit) {
            return new ArrayList<>(books);
        }
        return new ArrayList<>(books.subList(0, limit));
    }
}