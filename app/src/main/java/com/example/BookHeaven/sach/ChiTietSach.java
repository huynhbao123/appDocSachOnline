package com.example.BookHeaven.sach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.MainActivitybao;
import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.DanhGiaActivity;
import com.example.BookHeaven.models.Book;

import java.util.ArrayList;
import java.util.List;

public class ChiTietSach extends AppCompatActivity {

    private Book book;
    private RecyclerView relatedBooksRecyclerView;
    private RelatedBooksAdapter relatedBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sach);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            book = (Book) intent.getSerializableExtra("book");
        } else if (intent.hasExtra("bookId") && intent.hasExtra("bookTitle") && intent.hasExtra("coverResourceId")) {
            // Nhận dữ liệu từ các Intent khác (như từ CategoryActivitylinh)
            String bookId = intent.getStringExtra("bookId");
            String bookTitle = intent.getStringExtra("bookTitle");
            int coverResourceId = intent.getIntExtra("coverResourceId", R.drawable.book_khoi_nghiep);
            book = new Book(
                    bookId,
                    bookTitle,
                    coverResourceId,
                    "Nguyễn Nhật Ánh", // Giá trị mặc định
                    "Một tác phẩm được nhiều người bình chọn là hay nhất của nhà văn này.", // Mô tả mặc định
                    "01/01/1990", // Ngày xuất bản mặc định
                    1100, // Số trang mặc định
                    "1,1K", // Lượt xem mặc định
                    "6,8", // Lượt thích mặc định
                    4.06f // Điểm trung bình mặc định
            );
        } else {
            // Dữ liệu mẫu nếu không nhận được từ Intent
            book = new Book(
                    "1", // id
                    "Mắt biếc", // title
                    R.drawable.book_khoi_nghiep, // Sử dụng hình ảnh có sẵn
                    "Nguyễn Nhật Ánh", // author
                    "Một tác phẩm được nhiều người bình chọn là hay nhất của nhà văn này. Một tác phẩm đang được dịch và giới thiệu tại Nhật Bản (theo thông tin từ các báo)… Bởi sự trong sáng của một tình cảm, bởi cái kết thúc rất, rất buồn khi suốt câu chuyện vẫn là những điều vui, buồn lẫn lộn (cái kết thúc không như mong đợi của mọi người). Cũng bởi, mắt biếc… năm xưa nay đâu (theo lời một bài hát).", // description
                    "01/01/1990", // publicationDate
                    1100, // pageCount
                    "1,1K", // views
                    "6,8", // likes
                    4.06f // averageRating
            );
        }

        // Gán dữ liệu vào giao diện
        ImageView bookCover = findViewById(R.id.book_cover);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView bookViews = findViewById(R.id.book_views);
        TextView bookLikes = findViewById(R.id.book_likes);
        TextView bookDescription = findViewById(R.id.book_description);
        TextView publicationDate = findViewById(R.id.publication_date);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        TextView averageRating = findViewById(R.id.average_rating);
        TextView readLabel = findViewById(R.id.read_label);
        TextView reviewLabel = findViewById(R.id.review_label);
        Button btnSeeAll = findViewById(R.id.btn_see_all);

        // Tải hình ảnh từ ID tài nguyên bằng Glide
        Glide.with(this)
                .load(book.getCoverResourceId())
                .placeholder(R.drawable.book_khoi_nghiep) // Hình ảnh mặc định
                .error(R.drawable.book_tinh_yeu_dau_doi) // Hình ảnh khi lỗi
                .into(bookCover);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText("Tác giả: " + book.getAuthor());
        bookViews.setText(book.getViews());
        bookLikes.setText(book.getLikes());
        bookDescription.setText(book.getDescription());
        publicationDate.setText("Ngày xuất bản: " + book.getPublicationDate());
        ratingBar.setRating(book.getAverageRating());
        averageRating.setText(String.valueOf(book.getAverageRating()));

        // Thiết lập RecyclerView cho sách cùng tác giả
        relatedBooksRecyclerView = findViewById(R.id.related_books_recycler_view);
        relatedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedBooksAdapter = new RelatedBooksAdapter(this, getRelatedBooks());
        relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);

        // Xử lý sự kiện cho nút "Đọc sách"
        readLabel.setOnClickListener(v -> {
            Intent readIntent = new Intent(ChiTietSach.this, MainActivitybao.class);
            readIntent.putExtra("book_title", book.getTitle());
            readIntent.putExtra("book_author", book.getAuthor());
            readIntent.putExtra("book_id", book.getId());
            startActivity(readIntent);
        });

        // Xử lý sự kiện cho nút "Đánh giá"
        reviewLabel.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
            reviewIntent.putExtra("book_title", book.getTitle());
            reviewIntent.putExtra("book_author", book.getAuthor());
            reviewIntent.putExtra("book_cover", book.getCoverResourceId()); // Truyền ID tài nguyên
            startActivity(reviewIntent);
        });

        // Xử lý sự kiện cho nút "Xem tất cả"
        btnSeeAll.setOnClickListener(v -> {
            if (book == null) {
                Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                Intent seeAllIntent = new Intent(ChiTietSach.this, DanhSachDanhGiaActivity.class);
                seeAllIntent.putExtra("average_rating", book.getAverageRating());
                startActivity(seeAllIntent);
            } catch (Exception e) {
                Toast.makeText(ChiTietSach.this, "Lỗi khi mở DanhSachDanhGiaActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    private List<Book> getRelatedBooks() {
        List<Book> relatedBooks = new ArrayList<>();
        relatedBooks.add(new Book(
                "2",
                "Tôi thấy hoa vàng trên cỏ xanh",
                R.drawable.book_khoi_nghiep, // Sử dụng hình ảnh có sẵn
                "Nguyễn Nhật Ánh",
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                250,
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Book(
                "3",
                "Cho tôi xin một vé đi tuổi thơ",
                R.drawable.book_tinh_yeu_dau_doi, // Sử dụng hình ảnh có sẵn
                "Nguyễn Nhật Ánh",
                "Hành trình trở về tuổi thơ đầy kỷ niệm...",
                "01/01/2008",
                200,
                "1.5K",
                "8",
                4.3f
        ));
        relatedBooks.add(new Book(
                "4",
                "Ngày xưa có một chuyện tình",
                R.drawable.book_mua_he_nam_ay, // Sử dụng hình ảnh có sẵn
                "Nguyễn Nhật Ánh",
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                300,
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Book(
                "5",
                "Còn chút gì để nhớ",
                R.drawable.book_la_thu_tinh, // Sử dụng hình ảnh có sẵn
                "Nguyễn Nhật Ánh",
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                150,
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Book(
                "6",
                "Hạ đỏ",
                R.drawable.book_tram_nam_co_don, // Sử dụng hình ảnh có sẵn
                "Nguyễn Nhật Ánh",
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                180,
                "2K",
                "10",
                4.2f
        ));
        return relatedBooks;
    }
}