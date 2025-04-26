package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.models.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Xử lý click cho CardView
        findViewById(R.id.readingListCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, ReadingListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.favoritesCard).setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Lấy danh sách từ LibraryManager
        LibraryManager libraryManager = LibraryManager.getInstance();

        // Nếu danh sách rỗng, thêm dữ liệu mẫu (giải pháp tạm thời)
        if (libraryManager.getReadingList().isEmpty()) {
            libraryManager.addToReadingList(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don));
            libraryManager.addToReadingList(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem));
            libraryManager.addToReadingList(new Book("3", "Không Gia Đình", R.drawable.book_khong_gia_dinh));
            libraryManager.addToReadingList(new Book("10", "Nhà Giả Kim", R.drawable.book_nha_gia_kim));
            libraryManager.addToReadingList(new Book("4", "Bí Quyết Phát Triển", R.drawable.book_bi_quyet_phat_trien));
            libraryManager.addToReadingList(new Book("5", "Kinh Doanh Online", R.drawable.book_kinh_doanh_online));
        }

        if (libraryManager.getFavoritesList().isEmpty()) {
            libraryManager.addToFavorites(new Book("6", "Khởi Nghiệp", R.drawable.book_khoi_nghiep));
            libraryManager.addToFavorites(new Book("7", "Tình Yêu Đầu Đời", R.drawable.book_tinh_yeu_dau_doi));
            libraryManager.addToFavorites(new Book("8", "Mùa Hè Năm Ấy", R.drawable.book_mua_he_nam_ay));
            libraryManager.addToFavorites(new Book("9", "Lá Thư Tình", R.drawable.book_la_thu_tinh));
            libraryManager.addToFavorites(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don));
            libraryManager.addToFavorites(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem));
        }

        // Thiết lập RecyclerView cho Danh sách đọc (giới hạn 6 sách)
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Book> readingListLimited = getLimitedList(libraryManager.getReadingList(), 6);
        BookAdapter readingListAdapter = new BookAdapter(readingListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("coverResourceId", book.getCoverResourceId());
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(readingListAdapter);

        // Thiết lập RecyclerView cho Danh sách yêu thích (giới hạn 6 sách)
        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Book> favoritesListLimited = getLimitedList(libraryManager.getFavoritesList(), 6);
        BookAdapter favoritesAdapter = new BookAdapter(favoritesListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("coverResourceId", book.getCoverResourceId());
            startActivity(intent);
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivityTien.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_menu) {
                return true;

            }
            return false;
        });
    }

    // Hàm tiện ích để giới hạn danh sách sách
    private List<Book> getLimitedList(List<Book> books, int limit) {
        if (books.size() <= limit) {
            return new ArrayList<>(books);
        }
        return new ArrayList<>(books.subList(0, limit));
    }
}