package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.models.Book;
import com.example.myapplication.models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityTien extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tien);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupCategories();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;

            }
            return false;
        });

        LibraryManager libraryManager = LibraryManager.getInstance();
        // Thêm 6 sách vào Danh sách đọc (không trùng lặp)
        libraryManager.addToReadingList(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don));
        libraryManager.addToReadingList(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem));
        libraryManager.addToReadingList(new Book("3", "Không Gia Đình", R.drawable.book_khong_gia_dinh));
        libraryManager.addToReadingList(new Book("10", "Nhà Giả Kim", R.drawable.book_nha_gia_kim));
        libraryManager.addToReadingList(new Book("4", "Bí Quyết Phát Triển", R.drawable.book_bi_quyet_phat_trien));
        libraryManager.addToReadingList(new Book("5", "Kinh Doanh Online", R.drawable.book_kinh_doanh_online));

        // Thêm 6 sách vào Danh sách yêu thích (không trùng lặp)
        libraryManager.addToFavorites(new Book("6", "Khởi Nghiệp", R.drawable.book_khoi_nghiep));
        libraryManager.addToFavorites(new Book("7", "Tình Yêu Đầu Đời", R.drawable.book_tinh_yeu_dau_doi));
        libraryManager.addToFavorites(new Book("8", "Mùa Hè Năm Ấy", R.drawable.book_mua_he_nam_ay));
        libraryManager.addToFavorites(new Book("9", "Lá Thư Tình", R.drawable.book_la_thu_tinh));
        libraryManager.addToFavorites(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don));
        libraryManager.addToFavorites(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem));
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();

        // Sách Tiểu thuyết
        List<Book> novels = Arrays.asList(
                new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don),
                new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem),
                new Book("3", "Không Gia Đình", R.drawable.book_khong_gia_dinh),
                new Book("10", "Nhà Giả Kim", R.drawable.book_nha_gia_kim)
        );
        categories.add(new Category("1", "Sách Tiểu thuyết", novels));

        // Sách Kinh tế
        List<Book> economics = Arrays.asList(
                new Book("4", "Bí Quyết Phát Triển", R.drawable.book_bi_quyet_phat_trien),
                new Book("5", "Kinh Doanh Online", R.drawable.book_kinh_doanh_online),
                new Book("6", "Khởi Nghiệp", R.drawable.book_khoi_nghiep)
        );
        categories.add(new Category("2", "Sách Kinh tế", economics));

        // Sách Tình cảm
        List<Book> romance = Arrays.asList(
                new Book("7", "Tình Yêu Đầu Đời", R.drawable.book_tinh_yeu_dau_doi),
                new Book("8", "Mùa Hè Năm Ấy", R.drawable.book_mua_he_nam_ay),
                new Book("9", "Lá Thư Tình", R.drawable.book_la_thu_tinh)
        );
        categories.add(new Category("3", "Sách Tình cảm", romance));

        categoryAdapter = new CategoryAdapter(categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                Intent intent = new Intent(MainActivityTien.this, CategoryDetailActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_title", category.getTitle());
                startActivity(intent);
            }

            @Override
            public void onBookClick(Category category, int bookPosition) {
                Book book = category.getBooks().get(bookPosition);
                Intent intent = new Intent(MainActivityTien.this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookTitle", book.getTitle());
                intent.putExtra("coverResourceId", book.getCoverResourceId());
                startActivity(intent);
            }
        });

        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}