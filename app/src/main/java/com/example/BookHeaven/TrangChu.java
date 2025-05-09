package com.example.BookHeaven;

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

import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.ThongTinCaNhan.AccountMenuPopup;
import com.example.BookHeaven.TimKiem.SearchActivity;
import com.example.BookHeaven.adapter.BookAdapter;
import com.example.BookHeaven.models.Book;
import com.example.BookHeaven.sach.ChiTietSach;
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
                        Intent intent = new Intent(TrangChu.this, LibraryActivity.class);
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
        featuredBooks.add(new Book("1", "The Great Gatsby", R.drawable.book_tram_nam_co_don, "F. Scott Fitzgerald", "A classic novel...", "01/01/1925", 180, "1K", "500", 4.5f));
        featuredBooks.add(new Book("2", "1984", R.drawable.book_song_dong_em_dem, "George Orwell", "A dystopian novel...", "01/01/1949", 328, "2K", "1K", 4.7f));
        featuredBooks.add(new Book("3", "To Kill a Mockingbird", R.drawable.book_khong_gia_dinh, "Harper Lee", "A novel about racial injustice...", "01/01/1960", 281, "1.5K", "800", 4.8f));
        featuredBooks.add(new Book("4", "Pride and Prejudice", R.drawable.book_nha_gia_kim, "Jane Austen", "A romantic novel...", "01/01/1813", 432, "1.2K", "600", 4.6f));
        featuredBooks.add(new Book("5", "The Hobbit", R.drawable.book_bi_quyet_phat_trien, "J.R.R. Tolkien", "A fantasy novel...", "01/01/1937", 310, "1.8K", "900", 4.7f));
        featuredBooks.add(new Book("6", "Harry Potter", R.drawable.book_kinh_doanh_online, "J.K. Rowling", "A magical adventure...", "01/01/1997", 320, "3K", "1.5K", 4.9f));

        BookAdapter featuredAdapter = new BookAdapter(featuredBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        economicBooks.add(new Book("7", "Rich Dad Poor Dad", R.drawable.book_khoi_nghiep, "Robert Kiyosaki", "A personal finance book...", "01/01/1997", 336, "2K", "1K", 4.5f));
        economicBooks.add(new Book("8", "The Intelligent Investor", R.drawable.book_tinh_yeu_dau_doi, "Benjamin Graham", "A book on value investing...", "01/01/1949", 640, "1.5K", "800", 4.6f));
        economicBooks.add(new Book("9", "Think and Grow Rich", R.drawable.book_mua_he_nam_ay, "Napoleon Hill", "A self-help book...", "01/01/1937", 238, "1.8K", "900", 4.4f));
        economicBooks.add(new Book("10", "The Wealth of Nations", R.drawable.book_la_thu_tinh, "Adam Smith", "A classic economics book...", "01/01/1776", 1200, "1K", "500", 4.3f));
        economicBooks.add(new Book("11", "Capital", R.drawable.book_tram_nam_co_don, "Karl Marx", "A critique of political economy...", "01/01/1867", 1152, "1.2K", "600", 4.2f));
        economicBooks.add(new Book("12", "Freakonomics", R.drawable.book_song_dong_em_dem, "Steven D. Levitt", "A book on economics...", "01/01/2005", 320, "1.5K", "700", 4.4f));

        BookAdapter economicAdapter = new BookAdapter(economicBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        emotionalBooks.add(new Book("13", "The Alchemist", R.drawable.book_khong_gia_dinh, "Paulo Coelho", "A novel about following dreams...", "01/01/1988", 208, "2K", "1K", 4.6f));
        emotionalBooks.add(new Book("14", "The Little Prince", R.drawable.book_nha_gia_kim, "Antoine de Saint-Exupéry", "A philosophical tale...", "01/01/1943", 96, "1.8K", "900", 4.7f));
        emotionalBooks.add(new Book("15", "The Art of Happiness", R.drawable.book_bi_quyet_phat_trien, "Dalai Lama", "A guide to happiness...", "01/01/1998", 336, "1.5K", "800", 4.5f));
        emotionalBooks.add(new Book("16", "The Power of Now", R.drawable.book_kinh_doanh_online, "Eckhart Tolle", "A spiritual guide...", "01/01/1997", 236, "1.7K", "850", 4.6f));
        emotionalBooks.add(new Book("17", "The Road Less Traveled", R.drawable.book_khoi_nghiep, "M. Scott Peck", "A book on personal growth...", "01/01/1978", 316, "1.4K", "700", 4.4f));
        emotionalBooks.add(new Book("18", "Man's Search for Meaning", R.drawable.book_tinh_yeu_dau_doi, "Viktor E. Frankl", "A memoir on finding purpose...", "01/01/1946", 184, "2K", "1K", 4.8f));

        BookAdapter emotionalAdapter = new BookAdapter(emotionalBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        novelBooks.add(new Book("19", "The Catcher in the Rye", R.drawable.book_mua_he_nam_ay, "J.D. Salinger", "A coming-of-age novel...", "01/01/1951", 277, "1.5K", "800", 4.3f));
        novelBooks.add(new Book("20", "One Hundred Years of Solitude", R.drawable.book_la_thu_tinh, "Gabriel García Márquez", "A magical realist novel...", "01/01/1967", 417, "2K", "1K", 4.7f));
        novelBooks.add(new Book("21", "The Brothers Karamazov", R.drawable.book_tram_nam_co_don, "Fyodor Dostoevsky", "A philosophical novel...", "01/01/1880", 796, "1.2K", "600", 4.6f));
        novelBooks.add(new Book("22", "War and Peace", R.drawable.book_song_dong_em_dem, "Leo Tolstoy", "A historical novel...", "01/01/1869", 1225, "1K", "500", 4.5f));
        novelBooks.add(new Book("23", "Crime and Punishment", R.drawable.book_khong_gia_dinh, "Fyodor Dostoevsky", "A psychological novel...", "01/01/1866", 671, "1.8K", "900", 4.6f));
        novelBooks.add(new Book("24", "The Count of Monte Cristo", R.drawable.book_nha_gia_kim, "Alexandre Dumas", "An adventure novel...", "01/01/1844", 1276, "1.5K", "800", 4.7f));

        BookAdapter novelAdapter = new BookAdapter(novelBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        horrorBooks.add(new Book("25", "The Shining", R.drawable.book_bi_quyet_phat_trien, "Stephen King", "A horror novel...", "01/01/1977", 447, "1.5K", "800", 4.5f));
        horrorBooks.add(new Book("26", "Dracula", R.drawable.book_kinh_doanh_online, "Bram Stoker", "A gothic horror novel...", "01/01/1897", 418, "1.2K", "600", 4.4f));
        horrorBooks.add(new Book("27", "Frankenstein", R.drawable.book_khoi_nghiep, "Mary Shelley", "A classic horror novel...", "01/01/1818", 280, "1K", "500", 4.3f));
        horrorBooks.add(new Book("28", "The Exorcist", R.drawable.book_tinh_yeu_dau_doi, "William Peter Blatty", "A horror novel...", "01/01/1971", 340, "1.5K", "800", 4.5f));
        horrorBooks.add(new Book("29", "It", R.drawable.book_mua_he_nam_ay, "Stephen King", "A horror novel...", "01/01/1986", 1138, "1.8K", "900", 4.6f));
        horrorBooks.add(new Book("30", "The Haunting of Hill House", R.drawable.book_la_thu_tinh, "Shirley Jackson", "A gothic horror novel...", "01/01/1959", 246, "1.2K", "600", 4.4f));

        BookAdapter horrorAdapter = new BookAdapter(horrorBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        historyBooks.add(new Book("31", "Sapiens", R.drawable.book_tram_nam_co_don, "Yuval Noah Harari", "A history of humankind...", "01/01/2011", 443, "2K", "1K", 4.7f));
        historyBooks.add(new Book("32", "Guns, Germs, and Steel", R.drawable.book_song_dong_em_dem, "Jared Diamond", "A history book...", "01/01/1997", 494, "1.5K", "800", 4.5f));
        historyBooks.add(new Book("33", "A People's History of the United States", R.drawable.book_khong_gia_dinh, "Howard Zinn", "A history book...", "01/01/1980", 729, "1.2K", "600", 4.4f));
        historyBooks.add(new Book("34", "The Silk Roads", R.drawable.book_nha_gia_kim, "Peter Frankopan", "A history book...", "01/01/2015", 656, "1K", "500", 4.6f));
        historyBooks.add(new Book("35", "The Rise and Fall of the Third Reich", R.drawable.book_bi_quyet_phat_trien, "William L. Shirer", "A history book...", "01/01/1960", 1245, "1.5K", "800", 4.5f));
        historyBooks.add(new Book("36", "The History of the Ancient World", R.drawable.book_kinh_doanh_online, "Susan Wise Bauer", "A history book...", "01/01/2007", 896, "1.2K", "600", 4.4f));

        BookAdapter historyAdapter = new BookAdapter(historyBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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
        scienceBooks.add(new Book("37", "A Brief History of Time", R.drawable.book_khoi_nghiep, "Stephen Hawking", "A science book...", "01/01/1988", 212, "2K", "1K", 4.7f));
        scienceBooks.add(new Book("38", "The Selfish Gene", R.drawable.book_tinh_yeu_dau_doi, "Richard Dawkins", "A science book...", "01/01/1976", 224, "1.5K", "800", 4.5f));
        scienceBooks.add(new Book("39", "The Origin of Species", R.drawable.book_mua_he_nam_ay, "Charles Darwin", "A science book...", "01/01/1859", 502, "1.2K", "600", 4.4f));
        scienceBooks.add(new Book("40", "The Elegant Universe", R.drawable.book_la_thu_tinh, "Brian Greene", "A science book...", "01/01/1999", 464, "1K", "500", 4.6f));
        scienceBooks.add(new Book("41", "The Double Helix", R.drawable.book_tram_nam_co_don, "James D. Watson", "A science book...", "01/01/1968", 226, "1.5K", "800", 4.5f));
        scienceBooks.add(new Book("42", "The Emperor's New Mind", R.drawable.book_song_dong_em_dem, "Roger Penrose", "A science book...", "01/01/1989", 602, "1.2K", "600", 4.4f));

        BookAdapter scienceAdapter = new BookAdapter(scienceBooks, book -> {
            if (isLoggedIn()) {
                Intent intent = new Intent(TrangChu.this, ChiTietSach.class);
                intent.putExtra("book", book);
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