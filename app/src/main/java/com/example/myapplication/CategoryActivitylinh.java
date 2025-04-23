package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ThongTinCaNhan.AccountMenuPopup;
import com.example.myapplication.TimKiem.SearchActivity;
import com.example.myapplication.adapter.BookAdapterlinh;
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
                startActivity(new Intent(this, LibraryActivitylinh.class));
                return true;
            }
            return false;
        });
    }

    private void setupAdapters() {
        BookAdapterlinh economicAdapter = new BookAdapterlinh(getEconomicBooks(), false);
        economicAdapter.setOnItemClickListener(this::openBookDetail);
        economicBooksRecyclerView.setAdapter(economicAdapter);

        BookAdapterlinh emotionalAdapter = new BookAdapterlinh(getEmotionalBooks(), false);
        emotionalAdapter.setOnItemClickListener(this::openBookDetail);
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);

        BookAdapterlinh novelAdapter = new BookAdapterlinh(getNovelBooks(), false);
        novelAdapter.setOnItemClickListener(this::openBookDetail);
        novelBooksRecyclerView.setAdapter(novelAdapter);

        BookAdapterlinh horrorAdapter = new BookAdapterlinh(getHorrorBooks(), false);
        horrorAdapter.setOnItemClickListener(this::openBookDetail);
        horrorBooksRecyclerView.setAdapter(horrorAdapter);

        BookAdapterlinh historyAdapter = new BookAdapterlinh(getHistoryBooks(), false);
        historyAdapter.setOnItemClickListener(this::openBookDetail);
        historyBooksRecyclerView.setAdapter(historyAdapter);

        BookAdapterlinh scienceAdapter = new BookAdapterlinh(getScienceBooks(), false);
        scienceAdapter.setOnItemClickListener(this::openBookDetail);
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    private void openBookDetail(com.example.myapplication.model.Booklinh booklinh) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", booklinh.getId());
        intent.putExtra("bookTitle", booklinh.getTitle());
        startActivity(intent);
    }

    private List<com.example.myapplication.model.Booklinh> getEconomicBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("7", "Economic Book 1", "url7", "Economic"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("8", "Economic Book 2", "url8", "Economic"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("9", "Economic Book 3", "url9", "Economic"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("10", "Economic Book 4", "url10", "Economic"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("11", "Economic Book 5", "url11", "Economic"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("12", "Economic Book 6", "url12", "Economic"));
        return booklinhs;
    }

    private List<com.example.myapplication.model.Booklinh> getEmotionalBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("13", "Emotional Book 1", "url13", "Emotional"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("14", "Emotional Book 2", "url14", "Emotional"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("15", "Emotional Book 3", "url15", "Emotional"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("16", "Emotional Book 4", "url16", "Emotional"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("17", "Emotional Book 5", "url17", "Emotional"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("18", "Emotional Book 6", "url18", "Emotional"));
        return booklinhs;
    }

    private List<com.example.myapplication.model.Booklinh> getNovelBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("19", "Novel Book 1", "url19", "Novel"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("20", "Novel Book 2", "url20", "Novel"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("21", "Novel Book 3", "url21", "Novel"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("22", "Novel Book 4", "url22", "Novel"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("23", "Novel Book 5", "url23", "Novel"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("24", "Novel Book 6", "url24", "Novel"));
        return booklinhs;
    }

    private List<com.example.myapplication.model.Booklinh> getHorrorBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("25", "Horror Book 1", "url25", "Horror"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("26", "Horror Book 2", "url26", "Horror"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("27", "Horror Book 3", "url27", "Horror"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("28", "Horror Book 4", "url28", "Horror"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("29", "Horror Book 5", "url29", "Horror"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("30", "Horror Book 6", "url30", "Horror"));
        return booklinhs;
    }

    private List<com.example.myapplication.model.Booklinh> getHistoryBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("31", "History Book 1", "url31", "History"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("32", "History Book 2", "url32", "History"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("33", "History Book 3", "url33", "History"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("34", "History Book 4", "url34", "History"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("35", "History Book 5", "url35", "History"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("36", "History Book 6", "url36", "History"));
        return booklinhs;
    }

    private List<com.example.myapplication.model.Booklinh> getScienceBooks() {
        List<com.example.myapplication.model.Booklinh> booklinhs = new ArrayList<>();
        booklinhs.add(new com.example.myapplication.model.Booklinh("37", "Science Book 1", "url37", "Science"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("38", "Science Book 2", "url38", "Science"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("39", "Science Book 3", "url39", "Science"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("40", "Science Book 4", "url40", "Science"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("41", "Science Book 5", "url41", "Science"));
        booklinhs.add(new com.example.myapplication.model.Booklinh("42", "Science Book 6", "url42", "Science"));
        return booklinhs;
    }
}