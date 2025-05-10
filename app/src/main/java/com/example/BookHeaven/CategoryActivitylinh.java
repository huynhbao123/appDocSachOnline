package com.example.BookHeaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private RecyclerView rvNovelBooks, rvEconomicBooks, rvRomanceBooks, rvFeaturedBooks, rvHorrorBooks,
            rvHistoryBooks, rvScienceBooks;
    private AccountMenuPopup accountMenuPopup;
    private ImageView avatarImageView;
//    private ImageButton btnBack;
    private LinearLayout categoryHeaderLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylinh);

        initializeRecyclerViews();
        setupLayoutManagers();
        setupAdapters();
        setupBottomNavigation();

        accountMenuPopup = new AccountMenuPopup(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(v -> accountMenuPopup.show(avatarImageView));

        categoryHeaderLayout = findViewById(R.id.categoryHeaderLayout);
        //btnBack = findViewById(R.id.btnBack);
        categoryHeaderLayout.setVisibility(View.VISIBLE);

//        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        loadBooksFromFirebase();
    }

    private void setupAdapters() {

    }

    private void initializeRecyclerViews() {
        rvNovelBooks = findViewById(R.id.rvNovelBooks);
        rvEconomicBooks = findViewById(R.id.rvEconomicBooks);
        rvRomanceBooks = findViewById(R.id.rvRomanceBooks);
//        rvFeaturedBooks = findViewById(R.id.rvFeaturedBooks); // Thêm nếu có
        rvHorrorBooks = findViewById(R.id.rvHorrorBooks); // Thêm nếu có
        rvHistoryBooks = findViewById(R.id.rvHistoryBooks); // Thêm nếu có
        rvScienceBooks = findViewById(R.id.rvScienceBooks); // Thêm nếu có
    }

    private void setupLayoutManagers() {
        LinearLayoutManager novelLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager economicLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager romanceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horrorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager scienceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvNovelBooks.setLayoutManager(novelLayoutManager);
        rvEconomicBooks.setLayoutManager(economicLayoutManager);
        rvRomanceBooks.setLayoutManager(romanceLayoutManager);
//        rvFeaturedBooks.setLayoutManager(featuredLayoutManager);
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
//                rvFeaturedBooks.setAdapter(new BookAdapter(filterBooksByCategory(allBooks, "featured"), CategoryActivitylinh.this::openBookDetail));
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