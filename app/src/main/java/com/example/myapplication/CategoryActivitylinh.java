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
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.model.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivitylinh extends AppCompatActivity {
    private RecyclerView featuredBooksRecyclerView, economicBooksRecyclerView, emotionalBooksRecyclerView,
            novelBooksRecyclerView, horrorBooksRecyclerView, historyBooksRecyclerView, scienceBooksRecyclerView;
    private AccountMenuPopup accountMenuPopup;
    private ImageView avatarImageView;
    private ImageButton btnBack; // Thêm biến cho ImageButton
    private LinearLayout categoryHeaderLayout; // Thêm biến cho LinearLayout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginthanhcong);

        // Initialize RecyclerViews
        initializeRecyclerViews();

        // Setup layout managers
        setupLayoutManagers();

        // Setup adapters
        setupAdapters();

        // Setup bottom navigation
        setupBottomNavigation();

        // Khởi tạo AccountMenuPopup
        accountMenuPopup = new AccountMenuPopup(this);

        // Tìm ImageView avatar
        avatarImageView = findViewById(R.id.avatarImageView);

        // Thiết lập sự kiện click cho avatar
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountMenuPopup.show(avatarImageView);
            }
        });
        // Ánh xạ categoryHeaderLayout và btnBack
        categoryHeaderLayout = findViewById(R.id.categoryHeaderLayout);
        btnBack = findViewById(R.id.btnBack);

        // Đảm bảo categoryHeaderLayout hiển thị trong CategoryActivity
        categoryHeaderLayout.setVisibility(View.VISIBLE);

        // Thiết lập sự kiện click cho btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại và quay lại activity trước đó
            }
        });
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
        // Featured Books
//        BookAdapter featuredAdapter = new BookAdapter(getFeaturedBooks(), true);
//        featuredAdapter.setOnItemClickListener(book -> openBookDetail(book));
//        featuredBooksRecyclerView.setAdapter(featuredAdapter);
//
//        // Setup featured books scroll animation
//        setupFeaturedScrollListener();

        // Economic Books
        BookAdapter economicAdapter = new BookAdapter(getEconomicBooks(), false);
        economicAdapter.setOnItemClickListener(book -> openBookDetail(book));
        economicBooksRecyclerView.setAdapter(economicAdapter);

        // Emotional Books
        BookAdapter emotionalAdapter = new BookAdapter(getEmotionalBooks(), false);
        emotionalAdapter.setOnItemClickListener(book -> openBookDetail(book));
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);

        // Novel Books
        BookAdapter novelAdapter = new BookAdapter(getNovelBooks(), false);
        novelAdapter.setOnItemClickListener(book -> openBookDetail(book));
        novelBooksRecyclerView.setAdapter(novelAdapter);

        // Horror Books
        BookAdapter horrorAdapter = new BookAdapter(getHorrorBooks(), false);
        horrorAdapter.setOnItemClickListener(book -> openBookDetail(book));
        horrorBooksRecyclerView.setAdapter(horrorAdapter);

        // History Books
        BookAdapter historyAdapter = new BookAdapter(getHistoryBooks(), false);
        historyAdapter.setOnItemClickListener(book -> openBookDetail(book));
        historyBooksRecyclerView.setAdapter(historyAdapter);

        // Science Books
        BookAdapter scienceAdapter = new BookAdapter(getScienceBooks(), false);
        scienceAdapter.setOnItemClickListener(book -> openBookDetail(book));
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    private void setupFeaturedScrollListener() {
        featuredBooksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                    // Reset scale for all visible items
                    for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                        View item = layoutManager.findViewByPosition(i);
                        if (item != null) {
                            item.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(200)
                                    .start();
                        }
                    }

                    // Scale up the center item
                    int centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2;
                    View centerItem = layoutManager.findViewByPosition(centerPosition);
                    if (centerItem != null) {
                        centerItem.animate()
                                .scaleX(1.2f)
                                .scaleY(1.2f)
                                .setDuration(200)
                                .start();
                    }
                }
            }
        });
    }

    // Hàm mở chi tiết sách
    private void openBookDetail(Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", book.getId());
        intent.putExtra("bookTitle", book.getTitle());
        startActivity(intent);
    }


    private void setupOtherAdapters() {
        // Emotional Books
        BookAdapter emotionalAdapter = new BookAdapter(getEmotionalBooks(), false);
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);

        // Novel Books
        BookAdapter novelAdapter = new BookAdapter(getNovelBooks(), false);
        novelBooksRecyclerView.setAdapter(novelAdapter);

        // Horror Books
        BookAdapter horrorAdapter = new BookAdapter(getHorrorBooks(), false);
        horrorBooksRecyclerView.setAdapter(horrorAdapter);

        // History Books
        BookAdapter historyAdapter = new BookAdapter(getHistoryBooks(), false);
        historyBooksRecyclerView.setAdapter(historyAdapter);

        // Science Books
        BookAdapter scienceAdapter = new BookAdapter(getScienceBooks(), false);
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    private List<Book> getFeaturedBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("1", "Featured Book 1", "url1", "Featured"));
        books.add(new Book("2", "Featured Book 2", "url2", "Featured"));
        books.add(new Book("3", "Featured Book 3", "url3", "Featured"));
        books.add(new Book("4", "Featured Book 4", "url4", "Featured"));
        books.add(new Book("5", "Featured Book 5", "url5", "Featured"));
        books.add(new Book("6", "Featured Book 6", "url6", "Featured"));
        return books;
    }

    private List<Book> getEconomicBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("7", "Economic Book 1", "url7", "Economic"));
        books.add(new Book("8", "Economic Book 2", "url8", "Economic"));
        books.add(new Book("9", "Economic Book 3", "url9", "Economic"));
        books.add(new Book("10", "Economic Book 4", "url10", "Economic"));
        books.add(new Book("11", "Economic Book 5", "url11", "Economic"));
        books.add(new Book("12", "Economic Book 6", "url12", "Economic"));
        return books;
    }

    private List<Book> getEmotionalBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("13", "Emotional Book 1", "url13", "Emotional"));
        books.add(new Book("14", "Emotional Book 2", "url14", "Emotional"));
        books.add(new Book("15", "Emotional Book 3", "url15", "Emotional"));
        books.add(new Book("16", "Emotional Book 4", "url16", "Emotional"));
        books.add(new Book("17", "Emotional Book 5", "url17", "Emotional"));
        books.add(new Book("18", "Emotional Book 6", "url18", "Emotional"));
        return books;
    }

    private List<Book> getNovelBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("19", "Novel Book 1", "url19", "Novel"));
        books.add(new Book("20", "Novel Book 2", "url20", "Novel"));
        books.add(new Book("21", "Novel Book 3", "url21", "Novel"));
        books.add(new Book("22", "Novel Book 4", "url22", "Novel"));
        books.add(new Book("23", "Novel Book 5", "url23", "Novel"));
        books.add(new Book("24", "Novel Book 6", "url24", "Novel"));
        return books;
    }

    private List<Book> getHorrorBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("25", "Horror Book 1", "url25", "Horror"));
        books.add(new Book("26", "Horror Book 2", "url26", "Horror"));
        books.add(new Book("27", "Horror Book 3", "url27", "Horror"));
        books.add(new Book("28", "Horror Book 4", "url28", "Horror"));
        books.add(new Book("29", "Horror Book 5", "url29", "Horror"));
        books.add(new Book("30", "Horror Book 6", "url30", "Horror"));
        return books;
    }

    private List<Book> getHistoryBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("31", "History Book 1", "url31", "History"));
        books.add(new Book("32", "History Book 2", "url32", "History"));
        books.add(new Book("33", "History Book 3", "url33", "History"));
        books.add(new Book("34", "History Book 4", "url34", "History"));
        books.add(new Book("35", "History Book 5", "url35", "History"));
        books.add(new Book("36", "History Book 6", "url36", "History"));
        return books;
    }

    private List<Book> getScienceBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("37", "Science Book 1", "url37", "Science"));
        books.add(new Book("38", "Science Book 2", "url38", "Science"));
        books.add(new Book("39", "Science Book 3", "url39", "Science"));
        books.add(new Book("40", "Science Book 4", "url40", "Science"));
        books.add(new Book("41", "Science Book 5", "url41", "Science"));
        books.add(new Book("42", "Science Book 6", "url42", "Science"));
        return books;
    }

}