package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DangNhap.LoginActivity;
import com.example.myapplication.ThongTinCaNhan.AccountMenuPopup;
import com.example.myapplication.TimKiem.SearchActivity;
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.model.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class TrangChu extends AppCompatActivity {
    private RecyclerView featuredBooksRecyclerView, economicBooksRecyclerView, emotionalBooksRecyclerView,
            novelBooksRecyclerView, horrorBooksRecyclerView, historyBooksRecyclerView, scienceBooksRecyclerView;
    private ImageView avatarImageView;
    private AccountMenuPopup accountMenuPopup;
    private void updateUI() {
        float alpha = isLoggedIn() ? 1.0f : 0.5f;

        featuredBooksRecyclerView.setAlpha(alpha);
        economicBooksRecyclerView.setAlpha(alpha);
        emotionalBooksRecyclerView.setAlpha(alpha);
        novelBooksRecyclerView.setAlpha(alpha);
        horrorBooksRecyclerView.setAlpha(alpha);
        historyBooksRecyclerView.setAlpha(alpha);
        scienceBooksRecyclerView.setAlpha(alpha);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        avatarImageView = findViewById(R.id.avatarImageView);
        accountMenuPopup = new AccountMenuPopup(this);

        // RecyclerViews
        featuredBooksRecyclerView = findViewById(R.id.featuredBooksRecyclerView);
        economicBooksRecyclerView = findViewById(R.id.economicBooksRecyclerView);
        emotionalBooksRecyclerView = findViewById(R.id.emotionalBooksRecyclerView);
        novelBooksRecyclerView = findViewById(R.id.novelBooksRecyclerView);
        horrorBooksRecyclerView = findViewById(R.id.horrorBooksRecyclerView);
        historyBooksRecyclerView = findViewById(R.id.historyBooksRecyclerView);
        scienceBooksRecyclerView = findViewById(R.id.scienceBooksRecyclerView);

        // Setup
        setupLayoutManagers();
        setupAdapters();
        setupBottomNavigation();

        // Avatar click → nếu đã login thì mở menu, chưa login thì chuyển LoginActivity
        avatarImageView.setOnClickListener(v -> {
            if (isLoggedIn()) {
                accountMenuPopup.show(avatarImageView);
            } else {
                startActivity(new Intent(TrangChu.this, LoginActivity.class));
            }
        });

        updateAvatar();
        updateUI();
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("isLoggedIn", false);
        Log.d("MainActivity", "isLoggedIn: " + loggedIn);
        return loggedIn;
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Cập nhật lại ảnh đại diện
        updateAvatar();

        // Làm mờ các RecyclerView
        featuredBooksRecyclerView.setAlpha(0.5f);
        economicBooksRecyclerView.setAlpha(0.5f);
        emotionalBooksRecyclerView.setAlpha(0.5f);
        novelBooksRecyclerView.setAlpha(0.5f);
        horrorBooksRecyclerView.setAlpha(0.5f);
        historyBooksRecyclerView.setAlpha(0.5f);
        scienceBooksRecyclerView.setAlpha(0.5f);
    }

    private void updateAvatar() {
        if (isLoggedIn()) {
            avatarImageView.setImageResource(R.drawable.avt_login);
        } else {
            avatarImageView.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    private void setupLayoutManagers() {
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager economicLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager emotionalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager novelLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horrorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager scienceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (featuredBooksRecyclerView != null) featuredBooksRecyclerView.setLayoutManager(featuredLayoutManager);
        if (economicBooksRecyclerView != null) economicBooksRecyclerView.setLayoutManager(economicLayoutManager);
        if (emotionalBooksRecyclerView != null) emotionalBooksRecyclerView.setLayoutManager(emotionalLayoutManager);
        if (novelBooksRecyclerView != null) novelBooksRecyclerView.setLayoutManager(novelLayoutManager);
        if (horrorBooksRecyclerView != null) horrorBooksRecyclerView.setLayoutManager(horrorLayoutManager);
        if (historyBooksRecyclerView != null) historyBooksRecyclerView.setLayoutManager(historyLayoutManager);
        if (scienceBooksRecyclerView != null) scienceBooksRecyclerView.setLayoutManager(scienceLayoutManager);
    }

    private void setupAdapters() {
        setupFeaturedBooks();
        setupEconomicBooks();
        setupEmotionalBooks();
        setupNovelBooks();
        setupHorrorBooks();
        setupHistoryBooks();
        setupScienceBooks();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    if (isLoggedIn()) {
                        Intent intent = new Intent(TrangChu.this, SearchActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (itemId == R.id.navigation_library) {
                    if (isLoggedIn()) {
                        Intent intent = new Intent(TrangChu.this, LibraryActivitylinh.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    private void setupFeaturedBooks() {
        if (featuredBooksRecyclerView == null) return;

        List<Book> featuredBooks = new ArrayList<>();
        featuredBooks.add(new Book("1", "The Great Gatsby", "https://example.com/gatsby.jpg", "Featured"));
        featuredBooks.add(new Book("2", "1984", "https://example.com/1984.jpg", "Featured"));
        featuredBooks.add(new Book("3", "To Kill a Mockingbird", "https://example.com/mockingbird.jpg", "Featured"));
        featuredBooks.add(new Book("4", "Pride and Prejudice", "https://example.com/pride.jpg", "Featured"));
        featuredBooks.add(new Book("5", "The Hobbit", "https://example.com/hobbit.jpg", "Featured"));
        featuredBooks.add(new Book("6", "Harry Potter", "https://example.com/harry.jpg", "Featured"));

        BookAdapter featuredAdapter = new BookAdapter(featuredBooks, true);
        featuredAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        featuredBooksRecyclerView.setAdapter(featuredAdapter);
        setupFeaturedScrollListener();
    }

    private void setupFeaturedScrollListener() {
        if (featuredBooksRecyclerView == null) return;

        featuredBooksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

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

    private void setupEconomicBooks() {
        if (economicBooksRecyclerView == null) return;

        List<Book> economicBooks = new ArrayList<>();
        economicBooks.add(new Book("7", "Rich Dad Poor Dad", "https://example.com/richdad.jpg", "Economic"));
        economicBooks.add(new Book("8", "The Intelligent Investor", "https://example.com/investor.jpg", "Economic"));
        economicBooks.add(new Book("9", "Think and Grow Rich", "https://example.com/think.jpg", "Economic"));
        economicBooks.add(new Book("10", "The Wealth of Nations", "https://example.com/wealth.jpg", "Economic"));
        economicBooks.add(new Book("11", "Capital", "https://example.com/capital.jpg", "Economic"));
        economicBooks.add(new Book("12", "Freakonomics", "https://example.com/freak.jpg", "Economic"));

        BookAdapter economicAdapter = new BookAdapter(economicBooks, false);
        economicAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        economicBooksRecyclerView.setAdapter(economicAdapter);
    }

    private void setupEmotionalBooks() {
        if (emotionalBooksRecyclerView == null) return;

        List<Book> emotionalBooks = new ArrayList<>();
        emotionalBooks.add(new Book("13", "The Alchemist", "https://example.com/alchemist.jpg", "Emotional"));
        emotionalBooks.add(new Book("14", "The Little Prince", "https://example.com/prince.jpg", "Emotional"));
        emotionalBooks.add(new Book("15", "The Art of Happiness", "https://example.com/happiness.jpg", "Emotional"));
        emotionalBooks.add(new Book("16", "The Power of Now", "https://example.com/now.jpg", "Emotional"));
        emotionalBooks.add(new Book("17", "The Road Less Traveled", "https://example.com/road.jpg", "Emotional"));
        emotionalBooks.add(new Book("18", "Man's Search for Meaning", "https://example.com/meaning.jpg", "Emotional"));

        BookAdapter emotionalAdapter = new BookAdapter(emotionalBooks, false);
        emotionalAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        emotionalBooksRecyclerView.setAdapter(emotionalAdapter);
    }

    private void setupNovelBooks() {
        if (novelBooksRecyclerView == null) return;

        List<Book> novelBooks = new ArrayList<>();
        novelBooks.add(new Book("19", "The Catcher in the Rye", "https://example.com/catcher.jpg", "Novel"));
        novelBooks.add(new Book("20", "One Hundred Years of Solitude", "https://example.com/solitude.jpg", "Novel"));
        novelBooks.add(new Book("21", "The Brothers Karamazov", "https://example.com/karamazov.jpg", "Novel"));
        novelBooks.add(new Book("22", "War and Peace", "https://example.com/warpeace.jpg", "Novel"));
        novelBooks.add(new Book("23", "Crime and Punishment", "https://example.com/crime.jpg", "Novel"));
        novelBooks.add(new Book("24", "The Count of Monte Cristo", "https://example.com/montecristo.jpg", "Novel"));

        BookAdapter novelAdapter = new BookAdapter(novelBooks, false);
        novelAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        novelBooksRecyclerView.setAdapter(novelAdapter);
    }

    private void setupHorrorBooks() {
        if (horrorBooksRecyclerView == null) return;

        List<Book> horrorBooks = new ArrayList<>();
        horrorBooks.add(new Book("25", "The Shining", "https://example.com/shining.jpg", "Horror"));
        horrorBooks.add(new Book("26", "Dracula", "https://example.com/dracula.jpg", "Horror"));
        horrorBooks.add(new Book("27", "Frankenstein", "https://example.com/frankenstein.jpg", "Horror"));
        horrorBooks.add(new Book("28", "The Exorcist", "https://example.com/exorcist.jpg", "Horror"));
        horrorBooks.add(new Book("29", "It", "https://example.com/it.jpg", "Horror"));
        horrorBooks.add(new Book("30", "The Haunting of Hill House", "https://example.com/hillhouse.jpg", "Horror"));

        BookAdapter horrorAdapter = new BookAdapter(horrorBooks, false);
        horrorAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        horrorBooksRecyclerView.setAdapter(horrorAdapter);
    }

    private void setupHistoryBooks() {
        if (historyBooksRecyclerView == null) return;

        List<Book> historyBooks = new ArrayList<>();
        historyBooks.add(new Book("31", "Sapiens", "https://example.com/sapiens.jpg", "History"));
        historyBooks.add(new Book("32", "Guns, Germs, and Steel", "https://example.com/guns.jpg", "History"));
        historyBooks.add(new Book("33", "A People's History of the United States", "https://example.com/peoples.jpg", "History"));
        historyBooks.add(new Book("34", "The Silk Roads", "https://example.com/silk.jpg", "History"));
        historyBooks.add(new Book("35", "The Rise and Fall of the Third Reich", "https://example.com/reich.jpg", "History"));
        historyBooks.add(new Book("36", "The History of the Ancient World", "https://example.com/ancient.jpg", "History"));

        BookAdapter historyAdapter = new BookAdapter(historyBooks, false);
        historyAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        historyBooksRecyclerView.setAdapter(historyAdapter);
    }

    private void setupScienceBooks() {
        if (scienceBooksRecyclerView == null) return;

        List<Book> scienceBooks = new ArrayList<>();
        scienceBooks.add(new Book("37", "A Brief History of Time", "https://example.com/time.jpg", "Science"));
        scienceBooks.add(new Book("38", "The Selfish Gene", "https://example.com/gene.jpg", "Science"));
        scienceBooks.add(new Book("39", "The Origin of Species", "https://example.com/origin.jpg", "Science"));
        scienceBooks.add(new Book("40", "The Elegant Universe", "https://example.com/universe.jpg", "Science"));
        scienceBooks.add(new Book("41", "The Double Helix", "https://example.com/helix.jpg", "Science"));
        scienceBooks.add(new Book("42", "The Emperor's New Mind", "https://example.com/emperor.jpg", "Science"));

        BookAdapter scienceAdapter = new BookAdapter(scienceBooks, false);
        scienceAdapter.setOnItemClickListener(book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                startActivity(intent);
            } else {
                Intent intent = new Intent(TrangChu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        scienceBooksRecyclerView.setAdapter(scienceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAvatar();
        updateUI();
    }
}