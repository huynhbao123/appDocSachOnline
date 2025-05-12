package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.ThongTinCaNhan.AccountMenuPopup;
import com.example.BookHeaven.TimKiem.SearchActivity;
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

public class CategoryActivitylinh extends AppCompatActivity {
    private RecyclerView rvNovelBooks, rvEconomicBooks, rvRomanceBooks, rvHorrorBooks,
            rvHistoryBooks, rvScienceBooks;
    private AccountMenuPopup accountMenuPopup;
    private ImageView avatarImageView;
    private LinearLayout categoryHeaderLayout;
    private DatabaseReference databaseReference;
    private TextView tvNovelCategory, tvEconomicCategory, tvRomanceCategory,
            tvHorrorCategory, tvHistoryCategory, tvScienceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylinh);

        initializeRecyclerViews();
        initializeTextViews(); // Khởi tạo TextView
        setupLayoutManagers();
        setupAdapters();
        setupBottomNavigation();
        setupCategoryClickListeners(); // Thiết lập sự kiện click cho TextView

        accountMenuPopup = new AccountMenuPopup(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(v -> accountMenuPopup.show(avatarImageView));

        categoryHeaderLayout = findViewById(R.id.categoryHeaderLayout);
        categoryHeaderLayout.setVisibility(View.VISIBLE);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        loadBooksFromFirebase();
    }

    private void initializeTextViews() {
        tvNovelCategory = findViewById(R.id.tvNovelCategory);
        tvEconomicCategory = findViewById(R.id.tvEconomicCategory);
        tvRomanceCategory = findViewById(R.id.tvRomanceCategory);
        tvHorrorCategory = findViewById(R.id.tvHorrorCategory);
        tvHistoryCategory = findViewById(R.id.tvHistoryCategory);
        tvScienceCategory = findViewById(R.id.tvScienceCategory);
    }

    private void setupCategoryClickListeners() {
        tvNovelCategory.setOnClickListener(v -> openCategoryDetail("1", "Sách Tiểu thuyết"));
        tvEconomicCategory.setOnClickListener(v -> openCategoryDetail("2", "Sách Kinh tế"));
        tvRomanceCategory.setOnClickListener(v -> openCategoryDetail("3", "Sách Tình cảm"));
        tvHorrorCategory.setOnClickListener(v -> openCategoryDetail("4", "Sách Kinh dị"));
        tvHistoryCategory.setOnClickListener(v -> openCategoryDetail("5", "Sách Lịch sử"));
        tvScienceCategory.setOnClickListener(v -> openCategoryDetail("6", "Sách Khoa học"));
    }

    private void openCategoryDetail(String categoryId, String categoryTitle) {
        Intent intent = new Intent(this, CategoryDetailActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("category_title", categoryTitle);
        startActivity(intent);
    }

    private void setupAdapters() {
        // Có thể để trống nếu không cần thiết
    }

    private void initializeRecyclerViews() {
        rvNovelBooks = findViewById(R.id.rvNovelBooks);
        rvEconomicBooks = findViewById(R.id.rvEconomicBooks);
        rvRomanceBooks = findViewById(R.id.rvRomanceBooks);
        rvHorrorBooks = findViewById(R.id.rvHorrorBooks);
        rvHistoryBooks = findViewById(R.id.rvHistoryBooks);
        rvScienceBooks = findViewById(R.id.rvScienceBooks);
    }

    private void setupLayoutManagers() {
        LinearLayoutManager novelLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager economicLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager romanceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horrorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager scienceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvNovelBooks.setLayoutManager(novelLayoutManager);
        rvEconomicBooks.setLayoutManager(economicLayoutManager);
        rvRomanceBooks.setLayoutManager(romanceLayoutManager);
        rvHorrorBooks.setLayoutManager(horrorLayoutManager);
        rvHistoryBooks.setLayoutManager(historyLayoutManager);
        rvScienceBooks.setLayoutManager(scienceLayoutManager);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
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

    private void loadBooksFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Book> allBooks = new ArrayList<>();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        allBooks.add(book);
                    }
                }

                rvNovelBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "novel"), CategoryActivitylinh.this::openBookDetail));
                rvEconomicBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "economic"), CategoryActivitylinh.this::openBookDetail));
                rvRomanceBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "emotional"), CategoryActivitylinh.this::openBookDetail));
                rvHorrorBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "horror"), CategoryActivitylinh.this::openBookDetail));
                rvHistoryBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "history"), CategoryActivitylinh.this::openBookDetail));
                rvScienceBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "science"), CategoryActivitylinh.this::openBookDetail));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private List<Book> filterBooksByCategory(List<Book> books, String category) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book != null && category.equals(book.getCategory())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    private void openBookDetail(Book book) {
        Intent intent = new Intent(this, ChiTietSach.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }
}