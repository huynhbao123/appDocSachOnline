package com.example.BookHeaven.sach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.LibraryManager;
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
    private ImageButton favoriteButton;
    private RecyclerView relatedBooksRecyclerView;
    private RelatedBooksAdapter relatedBooksAdapter;
    private DatabaseReference databaseReference;
    private TextView reviewLabel;

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
            book = new Book(bookId, bookTitle, imageUrl);
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
        reviewLabel = findViewById(R.id.review_label);
        Button btnSeeAll = findViewById(R.id.btn_see_all);
        favoriteButton = findViewById(R.id.favoriteButton);
        TextView bookPageCount = findViewById(R.id.book_page_count);

        // Tải hình ảnh từ imageUrl bằng Picasso
        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.book_khoi_nghiep)
                .error(R.drawable.book_tinh_yeu_dau_doi)
                .into(bookCover);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Đảm bảo nút đánh giá luôn hiển thị
        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }

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
                            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
                            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
                            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));
                            ratingBar.setRating(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0f);
                            averageRating.setText(String.valueOf(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0));
                            // Cập nhật trạng thái yêu thích sau khi dữ liệu từ Firebase tải xong
                            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                                boolean isFavorite = LibraryManager.getInstance().isBookFavorite(book.getId());
                                book.setFavorites(isFavorite);
                                updateFavoriteButton();
                            }));

                            // Đảm bảo nút đánh giá luôn hiển thị sau khi tải dữ liệu
                            if (reviewLabel != null) {
                                reviewLabel.setVisibility(View.VISIBLE);
                            }
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
            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));
            ratingBar.setRating(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0f);
            averageRating.setText(String.valueOf(book.getAverageRating() > 0 ? book.getAverageRating() : 0.0));
            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                boolean isFavorite = LibraryManager.getInstance().isBookFavorite(book.getId());
                book.setFavorites(isFavorite);
                updateFavoriteButton();
            }));
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

            // Increment view count when user reads the book
            LibraryManager libraryManager = LibraryManager.getInstance();
            libraryManager.incrementBookViews(book);

            // Update the view count in UI
            bookViews.setText(book.getViews());

            if (libraryManager.addToReadingList(ChiTietSach.this, book)) {
                Intent readIntent = new Intent(ChiTietSach.this, MainActivitybao.class);
                readIntent.putExtra("book", book);
                startActivity(readIntent);
            }
        });

        // Xử lý sự kiện cho nút "Đánh giá"
        if (reviewLabel != null) {
            reviewLabel.setOnClickListener(v -> {
                if (book == null) {
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đảm bảo rằng book.getId() không null trước khi chuyển đến màn hình đánh giá
                if (book.getId() == null) {
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy ID sách!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Log để debug
                Log.d("ChiTietSach", "Chuyển đến màn hình đánh giá với bookId: " + book.getId());
                Log.d("ChiTietSach", "Tiêu đề sách: " + book.getTitle());

                Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
                reviewIntent.putExtra("book_id", book.getId());
                reviewIntent.putExtra("book_title", book.getTitle());
                reviewIntent.putExtra("book_author", book.getAuthor());
                reviewIntent.putExtra("book_cover", book.getImageUrl());
                startActivity(reviewIntent);
            });
        }

        // Xử lý sự kiện cho nút "Xem tất cả"
        btnSeeAll.setOnClickListener(v -> {
            if (book == null) {
                Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Intent seeAllIntent = new Intent(ChiTietSach.this, DanhSachDanhGiaActivity.class);
                    seeAllIntent.putExtra("book_id", book.getId());
                    seeAllIntent.putExtra("book_title", book.getTitle());
                    seeAllIntent.putExtra("book_author", book.getAuthor());
                    seeAllIntent.putExtra("book_cover", book.getImageUrl());
                    seeAllIntent.putExtra("average_rating", book.getAverageRating());
                    startActivity(seeAllIntent);
                } catch (Exception e) {
                    Toast.makeText(ChiTietSach.this, "Lỗi khi mở DanhSachDanhGiaActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        // Xử lý sự kiện cho nút "Yêu thích"
        favoriteButton.setOnClickListener(v -> {
            LibraryManager libraryManager = LibraryManager.getInstance();
            boolean isFavorite = book.isFavorites();
            Log.d("ChiTietSach", "Book " + book.getId() + " is favorite: " + isFavorite);
            if (isFavorite) {
                if (libraryManager.removeFromFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    book.setFavorites(false); // Đồng bộ trạng thái
                    updateFavoriteButton();
                    // Update likes count in UI
                    bookLikes.setText(book.getLikes());
                }
            } else {
                if (libraryManager.addToFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    book.setFavorites(true); // Đồng bộ trạng thái
                    updateFavoriteButton();
                    // Update likes count in UI
                    bookLikes.setText(book.getLikes());
                }
            }
        });

        // Add this at the end of onCreate() method, after loading book data
        updateBookStats();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if book is in favorites list and update UI accordingly
        if (book != null && book.getId() != null) {
            boolean isFavorite = LibraryManager.getInstance().isBookFavorite(book.getId());
            book.setFavorites(isFavorite);
            updateFavoriteButton();
        }

        // Đảm bảo nút đánh giá luôn hiển thị khi quay lại màn hình
        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }
    }

    private void updateFavoriteButton() {
        boolean isFavorite = book.isFavorites();
        Log.d("ChiTietSach", "Updating favorite button for book " + book.getId() + ". Is favorite: " + isFavorite);
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.tim_day);
        } else {
            favoriteButton.setImageResource(R.drawable.tim);
        }
    }

    private void loadRelatedBooks() {
        if (book == null || book.getAuthor() == null) {
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
                    if (b != null && b.getId() != null && book.getId() != null &&
                            !b.getId().equals(book.getId()) &&
                            b.getAuthor() != null && book.getAuthor() != null &&
                            b.getAuthor().equals(book.getAuthor())) {
                        relatedBooks.add(b);
                    }
                }
                relatedBooksAdapter = new RelatedBooksAdapter(ChiTietSach.this, relatedBooks);
                relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("ChiTietSach", "Error loading related books: " + databaseError.getMessage());
            }
        });
    }

    private void updateBookStats() {
        if (book != null && book.getId() != null) {
            DatabaseReference bookRef = databaseReference.child(book.getId());
            bookRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Update only the stats fields
                        if (snapshot.child("views").exists()) {
                            String views = snapshot.child("views").getValue(String.class);
                            if (views != null) {
                                book.setViews(views);
                                TextView bookViews = findViewById(R.id.book_views);
                                if (bookViews != null) {
                                    bookViews.setText(views);
                                }
                            }
                        }

                        if (snapshot.child("likes").exists()) {
                            String likes = snapshot.child("likes").getValue(String.class);
                            if (likes != null) {
                                book.setLikes(likes);
                                TextView bookLikes = findViewById(R.id.book_likes);
                                if (bookLikes != null) {
                                    bookLikes.setText(likes);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ChiTietSach", "Error updating book stats: " + error.getMessage());
                }
            });
        }
    }
}
