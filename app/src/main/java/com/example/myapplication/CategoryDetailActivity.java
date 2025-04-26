package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.models.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {
    private RecyclerView booksGrid;
    private BookAdapter bookAdapter;
    private TextView categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        String categoryId = getIntent().getStringExtra("category_id");
        String title = getIntent().getStringExtra("category_title");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText(title);

        booksGrid = findViewById(R.id.booksGrid);
        booksGrid.setLayoutManager(new GridLayoutManager(this, 2));

        setupBooks(categoryId);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivityTien.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_menu) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupBooks(String categoryId) {
        List<Book> books = new ArrayList<>();

        if (categoryId.equals("1")) { // Sách Tiểu thuyết
            books.add(new Book("1", "Trăm Năm Cô Đơn", R.drawable.book_tram_nam_co_don));
            books.add(new Book("2", "Sông Đông Êm Đềm", R.drawable.book_song_dong_em_dem));
            books.add(new Book("3", "Không Gia Đình", R.drawable.book_khong_gia_dinh));
            books.add(new Book("10", "Nhà Giả Kim", R.drawable.book_nha_gia_kim));
        } else if (categoryId.equals("2")) { // Sách Kinh tế
            books.add(new Book("4", "Bí Quyết Phát Triển", R.drawable.book_bi_quyet_phat_trien));
            books.add(new Book("5", "Kinh Doanh Online", R.drawable.book_kinh_doanh_online));
            books.add(new Book("6", "Khởi Nghiệp", R.drawable.book_khoi_nghiep));
        } else if (categoryId.equals("3")) { // Sách Tình cảm
            books.add(new Book("7", "Tình Yêu Đầu Đời", R.drawable.book_tinh_yeu_dau_doi));
            books.add(new Book("8", "Mùa Hè Năm Ấy", R.drawable.book_mua_he_nam_ay));
            books.add(new Book("9", "Lá Thư Tình", R.drawable.book_la_thu_tinh));
        }

        bookAdapter = new BookAdapter(books, book -> {
            Intent intent = new Intent(CategoryDetailActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("bookTitle", book.getTitle());
            intent.putExtra("coverResourceId", book.getCoverResourceId());
            startActivity(intent);
        });
        booksGrid.setAdapter(bookAdapter);
    }
}