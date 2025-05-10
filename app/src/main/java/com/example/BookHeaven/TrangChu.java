package com.example.BookHeaven;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.ThongTinCaNhan.AccountMenuPopup;
import com.example.BookHeaven.TimKiem.SearchActivity;
import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TrangChu extends AppCompatActivity {
    private RecyclerView featuredBooksRecyclerView, economicBooksRecyclerView, emotionalBooksRecyclerView,
            novelBooksRecyclerView, horrorBooksRecyclerView, historyBooksRecyclerView, scienceBooksRecyclerView;
    private ImageView avatarImageView;
    private AccountMenuPopup accountMenuPopup;

    private List<Book> featuredBooks = new ArrayList<>();
    private List<Book> economicBooks = new ArrayList<>();
    private List<Book> emotionalBooks = new ArrayList<>();
    private List<Book> novelBooks = new ArrayList<>();
    private List<Book> horrorBooks = new ArrayList<>();
    private List<Book> historyBooks = new ArrayList<>();
    private List<Book> scienceBooks = new ArrayList<>();

    private void updateUI() {
        float alpha = isLoggedIn() ? 1.0f : 0.5f;

        featuredBooksRecyclerView.setAlpha(alpha);
        economicBooksRecyclerView.setAlpha(alpha);
        emotionalBooksRecyclerView.setAlpha(alpha);
        novelBooksRecyclerView.setAlpha(alpha);
        horrorBooksRecyclerView.setAlpha(alpha);
        historyBooksRecyclerView.setAlpha(alpha);
        scienceBooksRecyclerView.setAlpha(alpha);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        avatarImageView = findViewById(R.id.avatarImageView);
        accountMenuPopup = new AccountMenuPopup(this);

        featuredBooksRecyclerView = findViewById(R.id.featuredBooksRecyclerView);
        economicBooksRecyclerView = findViewById(R.id.economicBooksRecyclerView);
        emotionalBooksRecyclerView = findViewById(R.id.emotionalBooksRecyclerView);
        novelBooksRecyclerView = findViewById(R.id.novelBooksRecyclerView);
        horrorBooksRecyclerView = findViewById(R.id.horrorBooksRecyclerView);
        historyBooksRecyclerView = findViewById(R.id.historyBooksRecyclerView);
        scienceBooksRecyclerView = findViewById(R.id.scienceBooksRecyclerView);

        // Khởi động LibraryManager để tải dữ liệu sớm
        LibraryManager.getInstance();

        setupLayoutManagers();
        loadBooksFromFirebase();
        setupBottomNavigation();

        avatarImageView.setOnClickListener(v -> {
            if (isLoggedIn()) {
                accountMenuPopup.show(avatarImageView);
            } else {
                startActivity(new Intent(TrangChu.this, LoginActivity.class));
            }
        });

        updateAvatar();
        updateUI();
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("isLoggedIn", false);

        // Đồng bộ với FirebaseAuth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (loggedIn && (user == null || !user.isEmailVerified())) {
            // Nếu SharedPreferences cho rằng đã đăng nhập nhưng Firebase không có user hoặc email chưa xác minh
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            loggedIn = false;
        }

        Log.d("MainActivity", "isLoggedIn: " + loggedIn);
        return loggedIn;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut(); // Đăng xuất Firebase
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        updateAvatar();

        featuredBooksRecyclerView.setAlpha(0.5f);
        economicBooksRecyclerView.setAlpha(0.5f);
        emotionalBooksRecyclerView.setAlpha(0.5f);
        novelBooksRecyclerView.setAlpha(0.5f);
        horrorBooksRecyclerView.setAlpha(0.5f);
        historyBooksRecyclerView.setAlpha(0.5f);
        scienceBooksRecyclerView.setAlpha(0.5f);
    }

    private void updateAvatar() {
        if (isLoggedIn()) {
            avatarImageView.setImageResource(R.drawable.avt2);
        } else {
            avatarImageView.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    private void setupLayoutManagers() {
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager economicLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager emotionalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager novelLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horrorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager scienceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (featuredBooksRecyclerView != null) featuredBooksRecyclerView.setLayoutManager(featuredLayoutManager);
        if (economicBooksRecyclerView != null) economicBooksRecyclerView.setLayoutManager(economicLayoutManager);
        if (emotionalBooksRecyclerView != null) emotionalBooksRecyclerView.setLayoutManager(emotionalLayoutManager);
        if (novelBooksRecyclerView != null) novelBooksRecyclerView.setLayoutManager(novelLayoutManager);
        if (horrorBooksRecyclerView != null) horrorBooksRecyclerView.setLayoutManager(horrorLayoutManager);
        if (historyBooksRecyclerView != null) historyBooksRecyclerView.setLayoutManager(historyLayoutManager);
        if (scienceBooksRecyclerView != null) scienceBooksRecyclerView.setLayoutManager(scienceLayoutManager);
    }

    private void setupAdapters() {
        BookAdapter featuredAdapter = new BookAdapter(featuredBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        featuredBooksRecyclerView.setAdapter(featuredAdapter);
        setupFeaturedScrollListener();

        BookAdapter economicAdapter = new BookAdapter(economicBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        economicBooksRecyclerView.setAdapter(economicAdapter);

        BookAdapter emotionalAdapter = new BookAdapter(emotionalBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);

        BookAdapter novelAdapter = new BookAdapter(novelBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        novelBooksRecyclerView.setAdapter(novelAdapter);

        BookAdapter horrorAdapter = new BookAdapter(horrorBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        horrorBooksRecyclerView.setAdapter(horrorAdapter);

        BookAdapter historyAdapter = new BookAdapter(historyBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        historyBooksRecyclerView.setAdapter(historyAdapter);

        BookAdapter scienceAdapter = new BookAdapter(scienceBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    private void loadBooksFromFirebase() {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");

        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                featuredBooks.clear();
                economicBooks.clear();
                emotionalBooks.clear();
                novelBooks.clear();
                horrorBooks.clear();
                historyBooks.clear();
                scienceBooks.clear();

                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        String category = bookSnapshot.child("category").getValue(String.class);
                        switch (category != null ? category.toLowerCase() : "") {
                            case "featured":
                                featuredBooks.add(book);
                                break;
                            case "economic":
                                economicBooks.add(book);
                                break;
                            case "emotional":
                                emotionalBooks.add(book);
                                break;
                            case "novel":
                                novelBooks.add(book);
                                break;
                            case "horror":
                                horrorBooks.add(book);
                                break;
                            case "history":
                                historyBooks.add(book);
                                break;
                            case "science":
                                scienceBooks.add(book);
                                break;
                        }
                    }
                }

                setupAdapters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TrangChu", "Failed to load books: " + error.getMessage());
            }
        });
    }

    private void setupFeaturedScrollListener() {
        if (featuredBooksRecyclerView == null) return;

        featuredBooksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                    for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                        View item = layoutManager.findViewByPosition(i);
                        if (item != null) {
                            item.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(200)
                                    .start();
                        }
                    }

                    int centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2;
                    View centerItem = layoutManager.findViewByPosition(centerPosition);
                    if (centerItem != null) {
                        centerItem.animate()
                                .scaleX(1.2f)
                                .scaleY(1.2f)
                                .setDuration(200)
                                .start();
                    }
                }
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    if (isLoggedIn()) {
                        Intent intent = new Intent(TrangChu.this, SearchActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (itemId == R.id.navigation_library) {
                    if (isLoggedIn()) {
                        Intent intent = new Intent(TrangChu.this, LibraryActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAvatar();
        updateUI();
    }
}