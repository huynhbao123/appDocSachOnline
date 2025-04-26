package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.models.Book;
import com.example.myapplication.sach.ChiTietSach;
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
            libraryManager.addToReadingList(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don, "Gabriel García Márquez", "A magical realist novel...", "01/01/1967", 417, "2K", "1K", 4.7f));
            libraryManager.addToReadingList(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem, "Mikhail Sholokhov", "A historical novel...", "01/01/1928", 1350, "1.5K", "800", 4.5f));
            libraryManager.addToReadingList(new Book("3", "Không Gia Đình", R.drawable.book_khong_gia_dinh, "Hector Malot", "A classic novel...", "01/01/1878", 470, "1.2K", "600", 4.6f));
            libraryManager.addToReadingList(new Book("4", "Nhà Giả Kim", R.drawable.book_nha_gia_kim, "Paulo Coelho", "A novel about dreams...", "01/01/1988", 208, "1.8K", "900", 4.6f));
            libraryManager.addToReadingList(new Book("5", "Bí Quyết Phát Triển", R.drawable.book_bi_quyet_phat_trien, "Unknown Author", "A self-help book...", "01/01/2020", 200, "1K", "500", 4.3f));
            libraryManager.addToReadingList(new Book("6", "Kinh Doanh Online", R.drawable.book_kinh_doanh_online, "Unknown Author", "A business book...", "01/01/2021", 220, "1.2K", "600", 4.2f));
        }

        if (libraryManager.getFavoritesList().isEmpty()) {
            libraryManager.addToFavorites(new Book("7", "Khởi Nghiệp", R.drawable.book_khoi_nghiep, "Unknown Author", "A startup book...", "01/01/2019", 250, "1.5K", "800", 4.4f));
            libraryManager.addToFavorites(new Book("8", "Tình Yêu Đầu Đời", R.drawable.book_tinh_yeu_dau_doi, "Unknown Author", "A romance novel...", "01/01/2018", 180, "1.2K", "600", 4.3f));
            libraryManager.addToFavorites(new Book("9", "Mùa Hè Năm Ấy", R.drawable.book_mua_he_nam_ay, "Unknown Author", "A coming-of-age story...", "01/01/2017", 200, "1K", "500", 4.2f));
            libraryManager.addToFavorites(new Book("10", "Lá Thư Tình", R.drawable.book_la_thu_tinh, "Unknown Author", "A romantic tale...", "01/01/2016", 150, "1.2K", "600", 4.3f));
            libraryManager.addToFavorites(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don, "Gabriel García Márquez", "A magical realist novel...", "01/01/1967", 417, "2K", "1K", 4.7f));
            libraryManager.addToFavorites(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem, "Mikhail Sholokhov", "A historical novel...", "01/01/1928", 1350, "1.5K", "800", 4.5f));
        }

        // Thiết lập RecyclerView cho Danh sách đọc (giới hạn 6 sách)
        RecyclerView readingListRecyclerView = findViewById(R.id.readingListRecyclerView);
        readingListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Book> readingListLimited = getLimitedList(libraryManager.getReadingList(), 6);
        BookAdapter readingListAdapter = new BookAdapter(readingListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        readingListRecyclerView.setAdapter(readingListAdapter);

        // Thiết lập RecyclerView cho Danh sách yêu thích (giới hạn 6 sách)
        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Book> favoritesListLimited = getLimitedList(libraryManager.getFavoritesList(), 6);
        BookAdapter favoritesAdapter = new BookAdapter(favoritesListLimited, book -> {
            Intent intent = new Intent(LibraryActivity.this, ChiTietSach.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, TrangChu.class));
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