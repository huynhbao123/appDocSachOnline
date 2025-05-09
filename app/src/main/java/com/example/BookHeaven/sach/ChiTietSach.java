package com.example.BookHeaven.sach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.MainActivitybao;
import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.DanhGiaActivity;
import com.example.BookHeaven.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChiTietSach extends AppCompatActivity {

    private Book book;
    private RecyclerView relatedBooksRecyclerView;
    private RelatedBooksAdapter relatedBooksAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sach);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            book = (Book) intent.getSerializableExtra("book");
        } else if (intent.hasExtra("bookId") && intent.hasExtra("bookTitle") && intent.hasExtra("imageUrl")) {
            String bookId = intent.getStringExtra("bookId");
            String bookTitle = intent.getStringExtra("bookTitle");
            String imageUrl = intent.getStringExtra("imageUrl");
            book = new Book(bookId, bookTitle, imageUrl); // Sử dụng constructor mới
        } else {
            Toast.makeText(this, "Không nhận được dữ liệu sách!", Toast.LENGTH_LONG).show();
            finish();
            return;
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

        // Tải hình ảnh từ imageUrl bằng Picasso
        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.book_khoi_nghiep)
                .error(R.drawable.book_tinh_yeu_dau_doi)
                .into(bookCover);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Nếu book chỉ có id, title, imageUrl, lấy dữ liệu đầy đủ từ Firebase
        if (book.getAuthor() == null || book.getDescription() == null) {
            DatabaseReference bookRef = databaseReference.child(book.getId());
            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        book = snapshot.getValue(Book.class);
                        if (book != null) {
                            // Cập nhật giao diện với dữ liệu đầy đủ
                            bookTitle.setText(book.getTitle());
                            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
                            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
                            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
                            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
                            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));
                            ratingBar.setRating(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0f);
                            averageRating.setText(String.valueOf(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChiTietSach.this, "Lỗi tải dữ liệu sách: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Nếu đã có dữ liệu đầy đủ, gán trực tiếp
            bookTitle.setText(book.getTitle());
            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));
            ratingBar.setRating(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0f);
            averageRating.setText(String.valueOf(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0));
        }

        // Thiết lập RecyclerView cho sách cùng tác giả
        relatedBooksRecyclerView = findViewById(R.id.related_books_recycler_view);
        relatedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadRelatedBooks();

        // Xử lý sự kiện cho nút "Đọc sách"
        readLabel.setOnClickListener(v -> {
            if (book == null || book.getChapters() == null || book.getChapters().isEmpty()) {
                Toast.makeText(ChiTietSach.this, "Không có nội dung để đọc", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent readIntent = new Intent(ChiTietSach.this, MainActivitybao.class);
            readIntent.putExtra("book", book); // Truyền toàn bộ đối tượng Book
            startActivity(readIntent);
        });

        // Xử lý sự kiện cho nút "Đánh giá"
        reviewLabel.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
            reviewIntent.putExtra("book_title", book.getTitle());
            reviewIntent.putExtra("book_author", book.getAuthor());
            reviewIntent.putExtra("book_cover", book.getImageUrl());
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

    private void loadRelatedBooks() {
        if (book.getAuthor() == null) {
            relatedBooksAdapter = new RelatedBooksAdapter(this, new ArrayList<>());
            relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);
            return;
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> relatedBooks = new ArrayList<>();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book b = bookSnapshot.getValue(Book.class);
                    if (b != null && !b.getId().equals(book.getId()) && b.getAuthor() != null && b.getAuthor().equals(book.getAuthor())) {
                        relatedBooks.add(b);
                    }
                }
                relatedBooksAdapter = new RelatedBooksAdapter(ChiTietSach.this, relatedBooks);
                relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}