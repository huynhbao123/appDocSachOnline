package com.example.myapplication.sach;


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
import com.example.myapplication.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.myapplication.R;
import com.example.myapplication.Vietdanhgia.DanhGiaActivity;

import java.util.ArrayList;
import java.util.List;

public class ChiTietSach extends AppCompatActivity {

    private Bookngan book;
    private RecyclerView relatedBooksRecyclerView;
    private RelatedBooksAdapter relatedBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sach);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            book = (Bookngan) intent.getSerializableExtra("book");
        } else {
            // Dữ liệu mẫu nếu không nhận được từ Intent
            book = new Bookngan(
                    "Mắt biếc",
                    "Nguyễn Nhật Ánh",
                    R.drawable.img, // Sử dụng ID tài nguyên
                    "Một tác phẩm được nhiều người bình chọn là hay nhất của nhà văn này. Một tác phẩm đang được dịch và giới thiệu tại Nhật Bản (theo thông tin từ các báo)… Bởi sự trong sáng của một tình cảm, bởi cái kết thúc rất, rất buồn khi suốt câu chuyện vẫn là những điều vui, buồn lẫn lộn (cái kết thúc không như mong đợi của mọi người). Cũng bởi, mắt biếc… năm xưa nay đâu (theo lời một bài hát).",
                    "01/01/1990",

                    "1,1K",
                    "6,8",
                    4.06f

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
                .load(book.getCoverImage())
                .placeholder(R.drawable.img)
                .error(R.drawable.img_3)
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
            Toast.makeText(ChiTietSach.this, "Bắt đầu đọc sách: " + book.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện cho nút "Đánh giá"
        reviewLabel.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
            reviewIntent.putExtra("book_title", book.getTitle());
            reviewIntent.putExtra("book_author", book.getAuthor());
            reviewIntent.putExtra("book_cover", book.getCoverImage()); // Truyền ID tài nguyên
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

    private List<Bookngan> getRelatedBooks() {
        List<Bookngan> relatedBooks = new ArrayList<>();
        relatedBooks.add(new Bookngan(
                "Tôi thấy hoa vàng trên cỏ xanh",
                "Nguyễn Nhật Ánh",
                R.drawable.img_3, // Sử dụng ID tài nguyên
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Bookngan(
                "Cho tôi xin một vé đi tuổi thơ",
                "Nguyễn Nhật Ánh",
                R.drawable.img_4, // Sử dụng ID tài nguyên
                "Hành trình trở về tuổi thơ đầy kỷ niệm...",
                "01/01/2008",
                "1.5K",
                "8",
                4.3f
        ));
        relatedBooks.add(new Bookngan(
                "Ngày xưa có một chuyện tình",
                "Nguyễn Nhật Ánh",
                R.drawable.img_5, // Sử dụng ID tài nguyên
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Bookngan(
                "CÒn chút gì để nhớ",
                "Nguyễn Nhật Ánh",
                R.drawable.img_7, // Sử dụng ID tài nguyên
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                "2K",
                "10",
                4.2f
        ));
        relatedBooks.add(new Bookngan(
                "Hạ đỏ",
                "Nguyễn Nhật Ánh",
                R.drawable.img_6, // Sử dụng ID tài nguyên
                "Một câu chuyện cảm động về tuổi thơ...",
                "01/01/2010",
                "2K",
                "10",
                4.2f
        ));
        return relatedBooks;
    }
}