package com.example.myapplication.TimKiem;

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

import com.example.myapplication.BookDetailActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.PopularBookAdapter;
import com.example.myapplication.model.Book;
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
        // Set up grid layout with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        popularBooksRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter
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
        // Back button click
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Search button click

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();

            if (!query.isEmpty()) {
                Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                intent.putExtra("search_query", query);  // Gửi dữ liệu tìm kiếm
                startActivity(intent);
            } else {
                Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle search action from keyboard
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
            // TODO: Implement search functionality
            // For now, just show a message or navigate to results
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("query", query);
            startActivity(intent);
        }
    }

    private List<Book> getPopularBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("1", "Saga", "url1", "Featured"));
        books.add(new Book("2", "Cây cam ngọt của tôi", "url2", "Featured"));
        books.add(new Book("3", "Thời niên thiếu của anh và em", "url3", "Featured"));
        books.add(new Book("4", "Hạnh trình của thằng năm", "url4", "Featured"));
        books.add(new Book("5", "Cảm ơn anh đã rời xa tôi", "url5", "Featured"));
        books.add(new Book("6", "Tôi thấy hoa vàng trên cỏ xanh", "url6", "Featured"));
        return books;
    }
}
