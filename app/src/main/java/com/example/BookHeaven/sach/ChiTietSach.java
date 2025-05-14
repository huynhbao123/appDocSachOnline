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

        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            book = (Book) intent.getSerializableExtra("book");
            Log.d("ChiTietSach", "Book received from Intent: " + book.getTitle() + ", Author: " + book.getAuthor());
        } else if (intent.hasExtra("bookId") && intent.hasExtra("bookTitle") && intent.hasExtra("imageUrl")) {
            String bookId = intent.getStringExtra("bookId");
            String bookTitle = intent.getStringExtra("bookTitle");
            String imageUrl = intent.getStringExtra("imageUrl");
            String bookAuthor = intent.getStringExtra("bookAuthor");
            book = new Book(bookId, bookTitle, imageUrl);
            if (bookAuthor != null && !bookAuthor.trim().isEmpty()) {
                book.setAuthor(bookAuthor);
            }
            Log.d("ChiTietSach", "Book created with minimal data, author set to: " + book.getAuthor());
        } else {
            Toast.makeText(this, "Không nhận được dữ liệu sách!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String originalAuthor = book.getAuthor();
        Log.d("ChiTietSach", "Original author from Intent: " + originalAuthor);

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
        TextView authorName = findViewById(R.id.author_name);
        TextView authorStats = findViewById(R.id.author_stats);

        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.book_khoi_nghiep)
                .error(R.drawable.book_tinh_yeu_dau_doi)
                .into(bookCover);

        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }

        if (book.getAuthor().equals("Unknown Author") || book.getDescription() == null) {
            DatabaseReference bookRef = databaseReference.child(book.getId());
            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Book firebaseBook = snapshot.getValue(Book.class);
                        if (firebaseBook != null) {
                            if (book.getAuthor().equals("Unknown Author") && firebaseBook.getAuthor() != null && !firebaseBook.getAuthor().trim().isEmpty()) {
                                book.setAuthor(firebaseBook.getAuthor());
                                Log.d("ChiTietSach", "Updated author from Firebase: " + book.getAuthor());
                            } else {
                                book.setAuthor(originalAuthor);
                                Log.d("ChiTietSach", "Restored original author: " + originalAuthor);
                            }
                            book.setDescription(firebaseBook.getDescription());
                            book.setPublicationDate(firebaseBook.getPublicationDate());
                            book.setPageCount(firebaseBook.getPageCount());
                            book.setViews(firebaseBook.getViews());
                            book.setLikes(firebaseBook.getLikes());
                            book.setAverageRating(firebaseBook.getAverageRating());
                            book.setCategory(firebaseBook.getCategory());
                            book.setChapters(firebaseBook.getChapters());

                            bookTitle.setText(book.getTitle());
                            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
                            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
                            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
                            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
                            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
                            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));

                            authorName.setText(book.getAuthor() != null ? book.getAuthor() : "Không xác định");
                            authorStats.setText("Thông tin tác giả đang được cập nhật");

                            float roundedRating = 0.0f;
                            try {
                                String avgRatingStr = String.valueOf(book.getAverageRating()).replace(",", ".");
                                roundedRating = Float.parseFloat(avgRatingStr);
                            } catch (NumberFormatException e) {
                                Log.e("ChiTietSach", "Error parsing averageRating: " + e.getMessage());
                            }
                            ratingBar.setRating(roundedRating > 0 ? roundedRating : 0.0f);
                            averageRating.setText(String.format("%.1f", roundedRating));

                            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                                updateFavoriteButton();
                            }));

                            if (reviewLabel != null) {
                                reviewLabel.setVisibility(View.VISIBLE);
                            }

                            loadRelatedBooks();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ChiTietSach", "Lỗi tải dữ liệu sách: " + error.getMessage());
                    Toast.makeText(ChiTietSach.this, "Lỗi tải dữ liệu sách: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));

            authorName.setText(book.getAuthor() != null ? book.getAuthor() : "Không xác định");
            authorStats.setText("Thông tin tác giả đang được cập nhật");

            float roundedRating = 0.0f;
            try {
                String avgRatingStr = String.valueOf(book.getAverageRating()).replace(",", ".");
                roundedRating = Float.parseFloat(avgRatingStr);
            } catch (NumberFormatException e) {
                Log.e("ChiTietSach", "Error parsing averageRating: " + e.getMessage());
            }
            ratingBar.setRating(roundedRating > 0 ? roundedRating : 0.0f);
            averageRating.setText(String.format("%.1f", roundedRating));

            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                updateFavoriteButton();
            }));

            loadRelatedBooks();
        }

        relatedBooksRecyclerView = findViewById(R.id.related_books_recycler_view);
        relatedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedBooksAdapter = new RelatedBooksAdapter(this, new ArrayList<>());
        relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);

        readLabel.setOnClickListener(v -> {
            if (book == null || book.getChapters() == null || book.getChapters().isEmpty()) {
                Toast.makeText(ChiTietSach.this, "Không có nội dung để đọc", Toast.LENGTH_SHORT).show();
                return;
            }

            LibraryManager libraryManager = LibraryManager.getInstance();
            libraryManager.incrementBookViews(book);
            bookViews.setText(book.getViews());

            if (libraryManager.addToReadingList(ChiTietSach.this, book)) {
                Toast.makeText(ChiTietSach.this, "Đã thêm vào lịch sử đọc", Toast.LENGTH_SHORT).show();
            }

            Intent readIntent = new Intent(ChiTietSach.this, MainActivitybao.class);
            readIntent.putExtra("book", book);
            startActivity(readIntent);
        });

        if (reviewLabel != null) {
            reviewLabel.setOnClickListener(v -> {
                if (book == null || book.getId() == null) {
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
                reviewIntent.putExtra("book_id", book.getId());
                reviewIntent.putExtra("book_title", book.getTitle());
                reviewIntent.putExtra("book_author", book.getAuthor());
                reviewIntent.putExtra("book_cover", book.getImageUrl());
                startActivity(reviewIntent);
            });
        }

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

        favoriteButton.setOnClickListener(v -> {
            LibraryManager libraryManager = LibraryManager.getInstance();
            boolean isFavorite = libraryManager.isBookFavorite(book.getId());
            if (isFavorite) {
                if (libraryManager.removeFromFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    updateFavoriteButton();
                    bookLikes.setText(book.getLikes());
                }
            } else {
                if (libraryManager.addToFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    updateFavoriteButton();
                    bookLikes.setText(book.getLikes());
                }
            }
        });

        updateBookStats();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (book != null && book.getId() != null) {
            updateFavoriteButton();
        }

        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }
    }

    private void updateFavoriteButton() {
        boolean isFavorite = LibraryManager.getInstance().isBookFavorite(book.getId());
        Log.d("ChiTietSach", "Updating favorite button for book " + book.getId() + ". Is favorite: " + isFavorite);
        favoriteButton.setImageResource(isFavorite ? R.drawable.tim_day : R.drawable.tim);
    }

    private void loadRelatedBooks() {
        if (book == null || book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            Log.e("ChiTietSach", "Book or author is null/empty, book: " + (book != null ? book.getTitle() : "null") + ", author: " + (book != null ? book.getAuthor() : "null"));
            relatedBooksAdapter.updateBooks(new ArrayList<>());
            relatedBooksRecyclerView.setVisibility(View.GONE);
            return;
        }

        String currentAuthor = book.getAuthor().trim().toLowerCase();
        Log.d("ChiTietSach", "Loading related books for book: " + book.getTitle() + ", Author (normalized): " + currentAuthor);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> relatedBooks = new ArrayList<>();
                Log.d("ChiTietSach", "Total books in Firebase: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book b = bookSnapshot.getValue(Book.class);
                    if (b != null && b.getId() != null && b.getAuthor() != null && !b.getAuthor().trim().isEmpty()) {
                        String firebaseAuthor = b.getAuthor().trim().toLowerCase();
                        if (!b.getId().equals(book.getId()) && firebaseAuthor.equals(currentAuthor)) {
                            relatedBooks.add(b);
                            Log.d("ChiTietSach", "Added related book: " + b.getTitle() + " by " + b.getAuthor());
                        }
                    }
                }

                Log.d("ChiTietSach", "Total related books found: " + relatedBooks.size() + " for author: " + currentAuthor);
                relatedBooksAdapter.updateBooks(relatedBooks);
                relatedBooksRecyclerView.setVisibility(relatedBooks.isEmpty() ? View.GONE : View.VISIBLE);

                if (relatedBooks.isEmpty()) {
                    Log.w("ChiTietSach", "No related books found for author: " + currentAuthor);
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy sách cùng tác giả.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChiTietSach", "Error loading related books: " + databaseError.getMessage());
                Toast.makeText(ChiTietSach.this, "Lỗi tải sách liên quan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                relatedBooksAdapter.updateBooks(new ArrayList<>());
                relatedBooksRecyclerView.setVisibility(View.GONE);
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