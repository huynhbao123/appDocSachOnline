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
import com.example.myapplication.adapter.BookAdapterlinh;
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

        featuredBooksRecyclerView = findViewById(R.id.featuredBooksRecyclerView);
        economicBooksRecyclerView = findViewById(R.id.economicBooksRecyclerView);
        emotionalBooksRecyclerView = findViewById(R.id.emotionalBooksRecyclerView);
        novelBooksRecyclerView = findViewById(R.id.novelBooksRecyclerView);
        horrorBooksRecyclerView = findViewById(R.id.horrorBooksRecyclerView);
        historyBooksRecyclerView = findViewById(R.id.historyBooksRecyclerView);
        scienceBooksRecyclerView = findViewById(R.id.scienceBooksRecyclerView);

        setupLayoutManagers();
        setupAdapters();
        setupBottomNavigation();

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

        updateAvatar();

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

        List<com.example.myapplication.model.Booklinh> featuredBooklinhs = new ArrayList<>();
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("1", "The Great Gatsby", "https://example.com/gatsby.jpg", "Featured"));
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("2", "1984", "https://example.com/1984.jpg", "Featured"));
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("3", "To Kill a Mockingbird", "https://example.com/mockingbird.jpg", "Featured"));
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("4", "Pride and Prejudice", "https://example.com/pride.jpg", "Featured"));
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("5", "The Hobbit", "https://example.com/hobbit.jpg", "Featured"));
        featuredBooklinhs.add(new com.example.myapplication.model.Booklinh("6", "Harry Potter", "https://example.com/harry.jpg", "Featured"));

        BookAdapterlinh featuredAdapter = new BookAdapterlinh(featuredBooklinhs, true);
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

        List<com.example.myapplication.model.Booklinh> economicBooklinhs = new ArrayList<>();
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("7", "Rich Dad Poor Dad", "https://example.com/richdad.jpg", "Economic"));
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("8", "The Intelligent Investor", "https://example.com/investor.jpg", "Economic"));
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("9", "Think and Grow Rich", "https://example.com/think.jpg", "Economic"));
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("10", "The Wealth of Nations", "https://example.com/wealth.jpg", "Economic"));
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("11", "Capital", "https://example.com/capital.jpg", "Economic"));
        economicBooklinhs.add(new com.example.myapplication.model.Booklinh("12", "Freakonomics", "https://example.com/freak.jpg", "Economic"));

        BookAdapterlinh economicAdapter = new BookAdapterlinh(economicBooklinhs, false);
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

        List<com.example.myapplication.model.Booklinh> emotionalBooklinhs = new ArrayList<>();
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("13", "The Alchemist", "https://example.com/alchemist.jpg", "Emotional"));
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("14", "The Little Prince", "https://example.com/prince.jpg", "Emotional"));
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("15", "The Art of Happiness", "https://example.com/happiness.jpg", "Emotional"));
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("16", "The Power of Now", "https://example.com/now.jpg", "Emotional"));
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("17", "The Road Less Traveled", "https://example.com/road.jpg", "Emotional"));
        emotionalBooklinhs.add(new com.example.myapplication.model.Booklinh("18", "Man's Search for Meaning", "https://example.com/meaning.jpg", "Emotional"));

        BookAdapterlinh emotionalAdapter = new BookAdapterlinh(emotionalBooklinhs, false);
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

        List<com.example.myapplication.model.Booklinh> novelBooklinhs = new ArrayList<>();
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("19", "The Catcher in the Rye", "https://example.com/catcher.jpg", "Novel"));
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("20", "One Hundred Years of Solitude", "https://example.com/solitude.jpg", "Novel"));
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("21", "The Brothers Karamazov", "https://example.com/karamazov.jpg", "Novel"));
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("22", "War and Peace", "https://example.com/warpeace.jpg", "Novel"));
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("23", "Crime and Punishment", "https://example.com/crime.jpg", "Novel"));
        novelBooklinhs.add(new com.example.myapplication.model.Booklinh("24", "The Count of Monte Cristo", "https://example.com/montecristo.jpg", "Novel"));

        BookAdapterlinh novelAdapter = new BookAdapterlinh(novelBooklinhs, false);
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

        List<com.example.myapplication.model.Booklinh> horrorBooklinhs = new ArrayList<>();
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("25", "The Shining", "https://example.com/shining.jpg", "Horror"));
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("26", "Dracula", "https://example.com/dracula.jpg", "Horror"));
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("27", "Frankenstein", "https://example.com/frankenstein.jpg", "Horror"));
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("28", "The Exorcist", "https://example.com/exorcist.jpg", "Horror"));
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("29", "It", "https://example.com/it.jpg", "Horror"));
        horrorBooklinhs.add(new com.example.myapplication.model.Booklinh("30", "The Haunting of Hill House", "https://example.com/hillhouse.jpg", "Horror"));

        BookAdapterlinh horrorAdapter = new BookAdapterlinh(horrorBooklinhs, false);
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

        List<com.example.myapplication.model.Booklinh> historyBooklinhs = new ArrayList<>();
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("31", "Sapiens", "https://example.com/sapiens.jpg", "History"));
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("32", "Guns, Germs, and Steel", "https://example.com/guns.jpg", "History"));
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("33", "A People's History of the United States", "https://example.com/peoples.jpg", "History"));
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("34", "The Silk Roads", "https://example.com/silk.jpg", "History"));
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("35", "The Rise and Fall of the Third Reich", "https://example.com/reich.jpg", "History"));
        historyBooklinhs.add(new com.example.myapplication.model.Booklinh("36", "The History of the Ancient World", "https://example.com/ancient.jpg", "History"));

        BookAdapterlinh historyAdapter = new BookAdapterlinh(historyBooklinhs, false);
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

        List<com.example.myapplication.model.Booklinh> scienceBooklinhs = new ArrayList<>();
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("37", "A Brief History of Time", "https://example.com/time.jpg", "Science"));
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("38", "The Selfish Gene", "https://example.com/gene.jpg", "Science"));
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("39", "The Origin of Species", "https://example.com/origin.jpg", "Science"));
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("40", "The Elegant Universe", "https://example.com/universe.jpg", "Science"));
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("41", "The Double Helix", "https://example.com/helix.jpg", "Science"));
        scienceBooklinhs.add(new com.example.myapplication.model.Booklinh("42", "The Emperor's New Mind", "https://example.com/emperor.jpg", "Science"));

        BookAdapterlinh scienceAdapter = new BookAdapterlinh(scienceBooklinhs, false);
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