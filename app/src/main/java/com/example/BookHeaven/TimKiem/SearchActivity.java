package com.example.BookHeaven.TimKiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.TrangChu;
import com.example.BookHeaven.R;
import com.example.BookHeaven.adapter.PopularBookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private AutoCompleteTextView edtSearch;
    private ImageButton btnBack;
    private ImageButton btnSearch;
    private RecyclerView popularBooksRecyclerView;
    private ProgressBar progressBar;
    private PopularBookAdapter popularBookAdapter;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_search);
        } catch (Exception e) {
            Log.e("SearchActivity", "Error setting content view: " + e.getMessage());
            Toast.makeText(this, "Lỗi tải giao diện!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            booksRef = FirebaseDatabase.getInstance().getReference("books");
        } catch (Exception e) {
            Log.e("SearchActivity", "Error initializing Firebase: " + e.getMessage());
            Toast.makeText(this, "Lỗi kết nối cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchHistory();
        loadPopularBooks();
    }

    private void initializeViews() {
        try {
            edtSearch = findViewById(R.id.edtSearch);
            btnBack = findViewById(R.id.btnBack);
            btnSearch = findViewById(R.id.btnSearch);
            popularBooksRecyclerView = findViewById(R.id.popularBooksRecyclerView);
            progressBar = findViewById(R.id.progressBar);
        } catch (Exception e) {
            Log.e("SearchActivity", "Error initializing views: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo giao diện!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        try {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            popularBooksRecyclerView.setLayoutManager(layoutManager);

            popularBookAdapter = new PopularBookAdapter(new ArrayList<>());
            popularBookAdapter.setOnItemClickListener(book -> {
                if (book != null && book.getId() != null) {
                    // Ghi log dữ liệu sách trước khi gửi
                    Log.d("SearchActivity", "Sending book to ChiTietSach: " +
                            "ID: " + book.getId() +
                            ", Title: " + book.getTitle() +
                            ", Author: " + book.getAuthor() +
                            ", Description: " + book.getDescription() +
                            ", Views: " + book.getViews() +
                            ", ImageUrl: " + book.getImageUrl());
                    Intent intent = new Intent(SearchActivity.this, ChiTietSach.class);
                    intent.putExtra("book", book);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("SearchActivity", "Error starting ChiTietSach: " + e.getMessage());
                        Toast.makeText(SearchActivity.this, "Lỗi khi mở chi tiết sách!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Dữ liệu sách không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            });
            popularBooksRecyclerView.setAdapter(popularBookAdapter);
        } catch (Exception e) {
            Log.e("SearchActivity", "Error setting up RecyclerView: " + e.getMessage());
            Toast.makeText(this, "Lỗi thiết lập danh sách sách!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(SearchActivity.this, TrangChu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try {
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("SearchActivity", "Error starting TrangChu: " + e.getMessage());
                    Toast.makeText(this, "Lỗi khi quay lại trang chủ!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                String query = edtSearch != null ? edtSearch.getText().toString().trim() : "";
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                    intent.putExtra("search_query", query);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("SearchActivity", "Error starting SearchResultsActivity: " + e.getMessage());
                        Toast.makeText(SearchActivity.this, "Lỗi khi mở kết quả tìm kiếm!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (edtSearch != null) {
            edtSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    performSearch();
                    return true;
                }
                return false;
            });
        }
    }

    private void setupSearchHistory() {
        if (edtSearch == null) {
            Log.w("SearchActivity", "edtSearch is null, skipping search history setup");
            return;
        }
        try {
            SharedPreferences prefs = getSharedPreferences("SearchHistory", MODE_PRIVATE);
            Set<String> history = prefs.getStringSet("history", new HashSet<>());
            if (history != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(history));
                edtSearch.setAdapter(adapter);
                edtSearch.setThreshold(1);
            } else {
                Log.w("SearchActivity", "Search history is null");
            }
        } catch (Exception e) {
            Log.e("SearchActivity", "Error setting up search history: " + e.getMessage());
        }
    }

    private void performSearch() {
        String query = edtSearch != null ? edtSearch.getText().toString().trim() : "";
        if (!TextUtils.isEmpty(query)) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("SearchActivity", "Error performing search: " + e.getMessage());
                Toast.makeText(this, "Lỗi khi mở kết quả tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPopularBooks() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> books = new ArrayList<>();
                Set<String> seenBookIds = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    if (book != null && book.getId() != null && !seenBookIds.contains(book.getId())) {
                        int views = 0;
                        try {
                            views = Integer.parseInt(book.getViews() != null ? book.getViews() : "0");
                        } catch (NumberFormatException e) {
                            Log.e("SearchActivity", "Invalid views format for book: " + book.getId());
                        }

                        if (views > 800) {
                            books.add(book);
                            seenBookIds.add(book.getId());
                            Log.d("SearchActivity", "Popular Book ID: " + book.getId() +
                                    ", Title: " + book.getTitle() +
                                    ", Author: " + book.getAuthor() +
                                    ", Description: " + book.getDescription() +
                                    ", Views: " + book.getViews() +
                                    ", ImageUrl: " + book.getImageUrl());
                        }
                    }
                }
                if (popularBookAdapter != null) {
                    popularBookAdapter.updateBooks(books);
                }
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if (books.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Không có sách nào có lượt đọc trên 800!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                Log.e("SearchActivity", "Error loading popular books: " + databaseError.getMessage());
                Toast.makeText(SearchActivity.this, "Lỗi khi tải sách: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}