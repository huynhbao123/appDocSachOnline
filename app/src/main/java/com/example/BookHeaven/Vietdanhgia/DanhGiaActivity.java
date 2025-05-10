package com.example.BookHeaven.Vietdanhgia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DanhGiaActivity extends AppCompatActivity {

    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private ImageView backButton;

    private String bookId;
    private DatabaseReference reviewsRef;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);

        // Khởi tạo Firebase
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        booksRef = FirebaseDatabase.getInstance().getReference("books");

        // Khởi tạo các view
        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        ratingBar = findViewById(R.id.rating_bar);
        commentEditText = findViewById(R.id.comment_edit_text);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);

        // Xử lý sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            bookId = intent.getStringExtra("book_id");
            String title = intent.getStringExtra("book_title");
            String author = intent.getStringExtra("book_author");
            String coverUrl = intent.getStringExtra("book_cover");

            // Log để debug
            Log.d("DanhGiaActivity", "Nhận dữ liệu từ Intent:");
            Log.d("DanhGiaActivity", "bookId: " + bookId);
            Log.d("DanhGiaActivity", "title: " + title);
            Log.d("DanhGiaActivity", "author: " + author);
            Log.d("DanhGiaActivity", "coverUrl: " + coverUrl);

            // Hiển thị thông tin sách
            if (title != null) {
                bookTitle.setText(title);
            }

            if (author != null) {
                bookAuthor.setText(author);
            }

            // Tải hình ảnh bìa sách
            if (coverUrl != null) {
                if (coverUrl.startsWith("http")) {
                    Picasso.get().load(coverUrl).into(bookCover);
                } else {
                    // Nếu là resource ID
                    try {
                        int resourceId = Integer.parseInt(coverUrl);
                        bookCover.setImageResource(resourceId);
                    } catch (NumberFormatException e) {
                        // Fallback to default image
                        bookCover.setImageResource(R.drawable.book_khoi_nghiep);
                    }
                }
            } else {
                int coverResourceId = intent.getIntExtra("book_cover", R.drawable.book_khoi_nghiep);
                bookCover.setImageResource(coverResourceId);
            }

            // Nếu có bookId, tải thông tin sách từ Firebase để đảm bảo dữ liệu đầy đủ
            if (bookId != null && !bookId.isEmpty()) {
                loadBookDetails(bookId);
            }
        }

        // Xử lý sự kiện khi nhấn nút gửi đánh giá
        submitButton.setOnClickListener(v -> submitReview());
    }

    private void loadBookDetails(String bookId) {
        booksRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    if (book != null) {
                        // Cập nhật UI với dữ liệu đầy đủ từ Firebase
                        bookTitle.setText(book.getTitle());
                        bookAuthor.setText(book.getAuthor());

                        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                            Picasso.get().load(book.getImageUrl()).into(bookCover);
                        }

                        Log.d("DanhGiaActivity", "Đã tải thông tin sách từ Firebase: " + book.getTitle());
                    }
                } else {
                    Log.e("DanhGiaActivity", "Không tìm thấy sách với ID: " + bookId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DanhGiaActivity", "Lỗi tải thông tin sách: " + databaseError.getMessage());
            }
        });
    }

    private void submitReview() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }

        float rating = ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm log để debug
        Log.d("DanhGiaActivity", "Đang gửi đánh giá cho sách ID: " + bookId);
        Log.d("DanhGiaActivity", "Tiêu đề sách: " + bookTitle.getText().toString());

        if (bookId == null || bookId.isEmpty()) {
            // Nếu không có bookId, tìm kiếm sách theo tiêu đề
            String title = bookTitle.getText().toString();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Tiêu đề sách không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            findBookIdByTitle(title, rating, comment);
        } else {
            // Nếu có bookId, lưu đánh giá trực tiếp
            saveReview(bookId, rating, comment);
        }
    }

    private void findBookIdByTitle(String title, float rating, String comment) {
        Log.d("DanhGiaActivity", "Tìm kiếm sách với tiêu đề: " + title);

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(DanhGiaActivity.this, "Tiêu đề sách không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    if (book != null && book.getTitle() != null &&
                            book.getTitle().equalsIgnoreCase(title.trim())) {
                        Log.d("DanhGiaActivity", "Đã tìm thấy sách: " + book.getId() + " - " + book.getTitle());
                        saveReview(book.getId(), rating, comment);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Log.e("DanhGiaActivity", "Không tìm thấy sách với tiêu đề: " + title);
                    Toast.makeText(DanhGiaActivity.this, "Không tìm thấy thông tin sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DanhGiaActivity", "Lỗi tìm kiếm sách: " + databaseError.getMessage());
                Toast.makeText(DanhGiaActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveReview(String bookId, float rating, String comment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        // Sửa lỗi biến không phải final trong lambda expression
        final String userName = user.getDisplayName();
        final String displayName = TextUtils.isEmpty(userName) ? "Người dùng " + userId.substring(0, 5) : userName;

        // Tạo đối tượng Review
        Review review = new Review(userId, displayName, R.drawable.img_2, rating, comment, bookId);

        // Lưu đánh giá vào Firebase
        String reviewId = reviewsRef.push().getKey();
        if (reviewId != null) {
            reviewsRef.child(reviewId).setValue(review)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(DanhGiaActivity.this, "Đã gửi đánh giá thành công", Toast.LENGTH_SHORT).show();

                        // Cập nhật điểm đánh giá trung bình của sách
                        updateBookAverageRating(bookId);

                        // Chuyển đến màn hình danh sách đánh giá
                        Intent intent = new Intent(DanhGiaActivity.this, DanhSachDanhGiaActivity.class);
                        intent.putExtra("book_id", bookId);
                        intent.putExtra("new_user_name", displayName);
                        intent.putExtra("new_rating", rating);
                        intent.putExtra("new_comment", comment);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DanhGiaActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateBookAverageRating(String bookId) {
        reviewsRef.orderByChild("bookId").equalTo(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float totalRating = 0;
                    int count = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Review review = snapshot.getValue(Review.class);
                        if (review != null) {
                            totalRating += review.getRating();
                            count++;
                        }
                    }

                    if (count > 0) {
                        float averageRating = totalRating / count;

                        // Cập nhật điểm đánh giá trung bình của sách
                        booksRef.child(bookId).child("averageRating").setValue(averageRating)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("DanhGiaActivity", "Đã cập nhật điểm đánh giá trung bình: " + averageRating);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DanhGiaActivity", "Lỗi cập nhật điểm đánh giá: " + e.getMessage());
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DanhGiaActivity", "Lỗi tính điểm đánh giá: " + databaseError.getMessage());
            }
        });
    }
}
