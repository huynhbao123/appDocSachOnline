package com.example.BookHeaven.TimKiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.BookDetailActivity;
import com.example.BookHeaven.TrangChu;
import com.example.BookHeaven.R;
import com.example.BookHeaven.adapter.PopularBookAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageButton btnBack;
    private ImageButton btnSearch;
    private RecyclerView popularBooksRecyclerView;
    private PopularBookAdapter popularBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
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

        popularBookAdapter = new PopularBookAdapter(getPopularBooks());
        popularBookAdapter.setOnItemClickListener(book -> {
            Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
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

            if (!query.isEmpty()) {
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

    private void performSearch() {
        String query = edtSearch.getText().toString().trim();
        if (!query.isEmpty()) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            startActivity(intent);
        }
    }

    private List<com.example.BookHeaven.models.Booklinh> getPopularBooks() {
        List<com.example.BookHeaven.models.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("1", "Saga", "url1", "Featured"));
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("2", "Cây cam ngọt của tôi", "url2", "Featured"));
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("3", "Thời niên thiếu của anh và em", "url3", "Featured"));
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("4", "Hạnh trình của thằng năm", "url4", "Featured"));
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("5", "Cảm ơn anh đã rời xa tôi", "url5", "Featured"));
        booklinhs.add(new com.example.BookHeaven.models.Booklinh("6", "Tôi thấy hoa vàng trên cỏ xanh", "url6", "Featured"));
        return booklinhs;
    }
}