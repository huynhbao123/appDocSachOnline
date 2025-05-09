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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivitylinh extends AppCompatActivity {
    private RecyclerView featuredBooksRecyclerView, economicBooksRecyclerView, emotionalBooksRecyclerView,
            novelBooksRecyclerView, horrorBooksRecyclerView, historyBooksRecyclerView, scienceBooksRecyclerView;
    private AccountMenuPopup accountMenuPopup;
    private ImageView avatarImageView;
    private ImageButton btnBack;
    private LinearLayout categoryHeaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginthanhcong);

        initializeRecyclerViews();
        setupLayoutManagers();
        setupAdapters();
        setupBottomNavigation();

        accountMenuPopup = new AccountMenuPopup(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(v -> accountMenuPopup.show(avatarImageView));

        categoryHeaderLayout = findViewById(R.id.categoryHeaderLayout);
        btnBack = findViewById(R.id.btnBack);
        categoryHeaderLayout.setVisibility(View.VISIBLE);

        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeRecyclerViews() {
        featuredBooksRecyclerView = findViewById(R.id.featuredBooksRecyclerView);
        economicBooksRecyclerView = findViewById(R.id.economicBooksRecyclerView);
        emotionalBooksRecyclerView = findViewById(R.id.emotionalBooksRecyclerView);
        novelBooksRecyclerView = findViewById(R.id.novelBooksRecyclerView);
        horrorBooksRecyclerView = findViewById(R.id.horrorBooksRecyclerView);
        historyBooksRecyclerView = findViewById(R.id.historyBooksRecyclerView);
        scienceBooksRecyclerView = findViewById(R.id.scienceBooksRecyclerView);
    }

    private void setupLayoutManagers() {
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager economicLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager emotionalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager novelLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horrorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager scienceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        featuredBooksRecyclerView.setLayoutManager(featuredLayoutManager);
        economicBooksRecyclerView.setLayoutManager(economicLayoutManager);
        emotionalBooksRecyclerView.setLayoutManager(emotionalLayoutManager);
        novelBooksRecyclerView.setLayoutManager(novelLayoutManager);
        horrorBooksRecyclerView.setLayoutManager(horrorLayoutManager);
        historyBooksRecyclerView.setLayoutManager(historyLayoutManager);
        scienceBooksRecyclerView.setLayoutManager(scienceLayoutManager);
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

    private void setupAdapters() {
        BookAdapter economicAdapter = new BookAdapter(getEconomicBooks(), this::openBookDetail);
        economicBooksRecyclerView.setAdapter(economicAdapter);

        BookAdapter emotionalAdapter = new BookAdapter(getEmotionalBooks(), this::openBookDetail);
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);

        BookAdapter novelAdapter = new BookAdapter(getNovelBooks(), this::openBookDetail);
        novelBooksRecyclerView.setAdapter(novelAdapter);

        BookAdapter horrorAdapter = new BookAdapter(getHorrorBooks(), this::openBookDetail);
        horrorBooksRecyclerView.setAdapter(horrorAdapter);

        BookAdapter historyAdapter = new BookAdapter(getHistoryBooks(), this::openBookDetail);
        historyBooksRecyclerView.setAdapter(historyAdapter);

        BookAdapter scienceAdapter = new BookAdapter(getScienceBooks(), this::openBookDetail);
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    private void openBookDetail(Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", book.getId());
        intent.putExtra("bookTitle", book.getTitle());
        intent.putExtra("coverResourceId", book.getCoverResourceId());
        startActivity(intent);
    }

    private List<Book> getEconomicBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("7", "Economic Book 1", R.drawable.book_khoi_nghiep));
        books.add(new Book("8", "Economic Book 2", R.drawable.book_tinh_yeu_dau_doi));
        books.add(new Book("9", "Economic Book 3", R.drawable.book_mua_he_nam_ay));
        books.add(new Book("10", "Economic Book 4", R.drawable.book_la_thu_tinh));
        books.add(new Book("11", "Economic Book 5", R.drawable.book_tram_nam_co_don));
        books.add(new Book("12", "Economic Book 6", R.drawable.book_song_dong_em_dem));
        return books;
    }

    private List<Book> getEmotionalBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("13", "Emotional Book 1", R.drawable.book_khong_gia_dinh));
        books.add(new Book("14", "Emotional Book 2", R.drawable.book_nha_gia_kim));
        books.add(new Book("15", "Emotional Book 3", R.drawable.book_bi_quyet_phat_trien));
        books.add(new Book("16", "Emotional Book 4", R.drawable.book_kinh_doanh_online));
        books.add(new Book("17", "Emotional Book 5", R.drawable.book_khoi_nghiep));
        books.add(new Book("18", "Emotional Book 6", R.drawable.book_tinh_yeu_dau_doi));
        return books;
    }

    private List<Book> getNovelBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("19", "Novel Book 1", R.drawable.book_mua_he_nam_ay));
        books.add(new Book("20", "Novel Book 2", R.drawable.book_la_thu_tinh));
        books.add(new Book("21", "Novel Book 3", R.drawable.book_tram_nam_co_don));
        books.add(new Book("22", "Novel Book 4", R.drawable.book_song_dong_em_dem));
        books.add(new Book("23", "Novel Book 5", R.drawable.book_khong_gia_dinh));
        books.add(new Book("24", "Novel Book 6", R.drawable.book_nha_gia_kim));
        return books;
    }

    private List<Book> getHorrorBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("25", "Horror Book 1", R.drawable.book_bi_quyet_phat_trien));
        books.add(new Book("26", "Horror Book 2", R.drawable.book_kinh_doanh_online));
        books.add(new Book("27", "Horror Book 3", R.drawable.book_khoi_nghiep));
        books.add(new Book("28", "Horror Book 4", R.drawable.book_tinh_yeu_dau_doi));
        books.add(new Book("29", "Horror Book 5", R.drawable.book_mua_he_nam_ay));
        books.add(new Book("30", "Horror Book 6", R.drawable.book_la_thu_tinh));
        return books;
    }

    private List<Book> getHistoryBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("31", "History Book 1", R.drawable.book_tram_nam_co_don));
        books.add(new Book("32", "History Book 2", R.drawable.book_song_dong_em_dem));
        books.add(new Book("33", "History Book 3", R.drawable.book_khong_gia_dinh));
        books.add(new Book("34", "History Book 4", R.drawable.book_nha_gia_kim));
        books.add(new Book("35", "History Book 5", R.drawable.book_bi_quyet_phat_trien));
        books.add(new Book("36", "History Book 6", R.drawable.book_kinh_doanh_online));
        return books;
    }

    private List<Book> getScienceBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("37", "Science Book 1", R.drawable.book_khoi_nghiep));
        books.add(new Book("38", "Science Book 2", R.drawable.book_tinh_yeu_dau_doi));
        books.add(new Book("39", "Science Book 3", R.drawable.book_mua_he_nam_ay));
        books.add(new Book("40", "Science Book 4", R.drawable.book_la_thu_tinh));
        books.add(new Book("41", "Science Book 5", R.drawable.book_tram_nam_co_don));
        books.add(new Book("42", "Science Book 6", R.drawable.book_song_dong_em_dem));
        return books;
    }
}