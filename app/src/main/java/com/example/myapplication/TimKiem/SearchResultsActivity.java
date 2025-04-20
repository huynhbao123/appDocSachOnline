package com.example.myapplication.TimKiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BookDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.model.Book;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;


public class SearchResultsActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageButton btnBack;
    private ImageButton btnSearch;
    private TextView tvResultsCount;
    private RecyclerView searchResultsRecyclerView;
    private BookAdapter bookAdapter;
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get the search query from intent
        currentQuery = getIntent().getStringExtra("query");

        initializeViews();
        setupRecyclerView();
        setupClickListeners();

        // Set the search query in the EditText
        if (currentQuery != null) {
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
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(new ArrayList<>(), false);
        bookAdapter.setOnItemClickListener(book -> {
            Intent intent = new Intent(SearchResultsActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            startActivity(intent);
        });
        searchResultsRecyclerView.setAdapter(bookAdapter);
    }

    private void setupClickListeners() {
        // Back button click
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SearchResultsActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();

        });

        // Search button click
        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();

            if (!query.isEmpty()) {
                Intent intent = new Intent(SearchResultsActivity.this, SearchActivity.class);
                intent.putExtra("search_query", query);  // Gửi dữ liệu tìm kiếm
                startActivity(intent);
            } else {
                Toast.makeText(SearchResultsActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });








        // Handle search action from keyboard
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch(edtSearch.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            // TODO: Implement actual search functionality
            // For now, we'll just show some sample results
            List<Book> searchResults = getSampleSearchResults(query);
            bookAdapter.updateBooks(searchResults);
            tvResultsCount.setText("Kết quả tìm kiếm (" + searchResults.size() + ")");
        }
    }

    private List<Book> getSampleSearchResults(String query) {
        List<Book> results = new ArrayList<>();
        // Add sample books based on the query
        // This is just for demonstration
        if (query.toLowerCase().contains("saga")) {
            results.add(new Book("1", "Saga", "url1", "Featured"));
        }
        if (query.toLowerCase().contains("cam")) {
            results.add(new Book("2", "Cây cam ngọt của tôi", "url2", "Featured"));
        }
        if (query.toLowerCase().contains("niên thiếu")) {
            results.add(new Book("3", "Thời niên thiếu của anh và em", "url3", "Featured"));
        }
        return results;
    }
}