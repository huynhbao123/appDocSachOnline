package com.example.BookHeaven.TimKiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.TrangChu;
import com.example.BookHeaven.R;
import com.example.BookHeaven.adapter.PopularBookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.models.Booklinh;
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
    private PopularBookAdapter popularBookAdapter;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo Firebase
        booksRef = FirebaseDatabase.getInstance().getReference("books");

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchHistory();
        loadPopularBooks();
    }

    private void initializeViews() {
        edtSearch = findViewById(R.id.edtSearch);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        popularBooksRecyclerView = findViewById(R.id.popularBooksRecyclerView);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        popularBooksRecyclerView.setLayoutManager(layoutManager);

        popularBookAdapter = new PopularBookAdapter(new ArrayList<>());
        popularBookAdapter.setOnItemClickListener(book -> {
            Intent intent = new Intent(SearchActivity.this, ChiTietSach.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("imageUrl", book.getImageUrl());
            startActivity(intent);
        });
        popularBooksRecyclerView.setAdapter(popularBookAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, TrangChu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
            } else {
                Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void setupSearchHistory() {
        SharedPreferences prefs = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        Set<String> history = prefs.getStringSet("history", new HashSet<>());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(history));
        edtSearch.setAdapter(adapter);
        edtSearch.setThreshold(1);
    }

    private void performSearch() {
        String query = edtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(query)) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPopularBooks() {
        booksRef.orderByChild("category").equalTo("Featured").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Booklinh> books = new ArrayList<>();
                Set<String> seenBookIds = new HashSet<>(); // Lưu các ID đã thấy để lọc trùng lặp
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    if (book != null && !seenBookIds.contains(book.getId())) {
                        Booklinh booklinh = new Booklinh(book.getId(), book.getTitle(), book.getImageUrl(), book.getCategory());
                        books.add(booklinh);
                        seenBookIds.add(book.getId());
                        Log.d("SearchActivity", "Book ID: " + book.getId() + ", Title: " + book.getTitle());
                    } else if (book != null) {
                        Log.d("SearchActivity", "Skipped duplicate book: ID=" + book.getId() + ", Title=" + book.getTitle());
                    }
                }
                popularBookAdapter.updateBooks(books);
                if (books.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Không có sách phổ biến nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Lỗi khi tải sách: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}