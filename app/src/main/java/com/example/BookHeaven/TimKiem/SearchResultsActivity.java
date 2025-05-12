package com.example.BookHeaven.TimKiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.adapter.BookAdapterlinh;
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

public class SearchResultsActivity extends AppCompatActivity {
    private AutoCompleteTextView edtSearch;
    private ImageButton btnBack;
    private ImageButton btnSearch;
    private TextView tvResultsCount;
    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private BookAdapterlinh bookAdapterlinh;
    private String currentQuery;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Khởi tạo Firebase
        booksRef = FirebaseDatabase.getInstance().getReference("books");

        currentQuery = getIntent().getStringExtra("search_query");

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchHistory();

        if (currentQuery != null && !currentQuery.isEmpty()) {
            edtSearch.setText(currentQuery);
            performSearch(currentQuery);
        }
    }

    private void initializeViews() {
        edtSearch = findViewById(R.id.edtSearch);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        tvResultsCount = findViewById(R.id.tvResultsCount);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
//        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this,2));
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bookAdapterlinh = new BookAdapterlinh(new ArrayList<>(), false);
        bookAdapterlinh.setOnItemClickListener(book -> {
            // Tải dữ liệu Book đầy đủ từ Firebase
            progressBar.setVisibility(View.VISIBLE);
            booksRef.child(book.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.GONE);
                    if (snapshot.exists()) {
                        Book fullBook = snapshot.getValue(Book.class);
                        if (fullBook != null) {
                            Intent intent = new Intent(SearchResultsActivity.this, ChiTietSach.class);
                            intent.putExtra("book", fullBook);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SearchResultsActivity.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchResultsActivity.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SearchResultsActivity.this, "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        searchResultsRecyclerView.setAdapter(bookAdapterlinh);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SearchResultsActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            } else {
                Toast.makeText(SearchResultsActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch(edtSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        // Tìm kiếm theo thời gian thực
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    bookAdapterlinh.updateBooks(new ArrayList<>());
                    tvResultsCount.setText("Kết quả tìm kiếm (0)");
                }
            }
        });
    }

    private void setupSearchHistory() {
        SharedPreferences prefs = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        Set<String> history = prefs.getStringSet("history", new HashSet<>());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(history));
        edtSearch.setAdapter(adapter);
        edtSearch.setThreshold(1);
    }

    private void saveSearchQuery(String query) {
        SharedPreferences prefs = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        Set<String> history = new HashSet<>(prefs.getStringSet("history", new HashSet<>()));
        history.add(query);
        prefs.edit().putStringSet("history", history).apply();
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            saveSearchQuery(query);
            booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Booklinh> searchResults = new ArrayList<>();
                    Set<String> seenBookIds = new HashSet<>(); // Lưu các ID đã thấy để lọc trùng lặp
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Book book = snapshot.getValue(Book.class);
                        if (book != null && book.getTitle() != null &&
                                (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                        (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(query.toLowerCase())))) {
                            // Chỉ thêm nếu ID sách chưa được thấy
                            if (!seenBookIds.contains(book.getId())) {
                                String bookKey = snapshot.getKey();
                                Booklinh booklinh = new Booklinh(bookKey, book.getTitle(), book.getImageUrl(), book.getCategory());
                                searchResults.add(booklinh);
                                seenBookIds.add(book.getId());
                                Log.d("SearchResults", "Book Key: " + bookKey + ", ID: " + book.getId() + ", Title: " + book.getTitle());
                            } else {
                                Log.d("SearchResults", "Skipped duplicate book: ID=" + book.getId() + ", Title=" + book.getTitle());
                            }
                        }
                    }
                    bookAdapterlinh.updateBooks(searchResults);
                    tvResultsCount.setText("Kết quả tìm kiếm (" + searchResults.size() + ")");
                    progressBar.setVisibility(View.GONE);
                    if (searchResults.isEmpty()) {
                        Toast.makeText(SearchResultsActivity.this, "Không tìm thấy kết quả!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("SearchResults", "Search error: " + databaseError.getMessage());
                    Toast.makeText(SearchResultsActivity.this, "Lỗi khi tìm kiếm: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
        }
    }
}