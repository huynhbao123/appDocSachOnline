package com.example.BookHeaven.Danhsachdanhgia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.DanhGiaActivity;
import com.example.BookHeaven.Vietdanhgia.Review;
import com.example.BookHeaven.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DanhSachDanhGiaActivity extends AppCompatActivity {

    //biến giao diện
    private RatingBar averageRatingBar;
    private TextView averageRatingText;
    private TextView reviewLabel;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private ImageView backButton;

    //dữ liệu logic
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCoverUrl;
    private DatabaseReference reviewsRef;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_danh_gia);

        // Khởi tạo Firebase
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        booksRef = FirebaseDatabase.getInstance().getReference("books");

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        averageRatingBar = findViewById(R.id.average_rating_bar);
        averageRatingText = findViewById(R.id.average_rating);
        reviewLabel = findViewById(R.id.review_label);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);

        // Xử lý sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener(v -> finish());

        // Khởi tạo danh sách đánh giá
        reviewList = new ArrayList<>();

        // Thiết lập RecyclerView
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookId = extras.getString("book_id");
            bookTitle = extras.getString("book_title", "");
            bookAuthor = extras.getString("book_author", "");
            bookCoverUrl = extras.getString("book_cover", "");

            // Log để debug
            Log.d("DanhSachDanhGiaActivity", "Nhận dữ liệu từ Intent:");
            Log.d("DanhSachDanhGiaActivity", "bookId: " + bookId);
            Log.d("DanhSachDanhGiaActivity", "bookTitle: " + bookTitle);

            float averageRating = extras.getFloat("average_rating", 0f);
            if (averageRating > 0) {
                averageRatingBar.setRating(averageRating);
                averageRatingText.setText(String.format("%.2f", averageRating));
            }

            // Nếu có đánh giá mới, thêm vào danh sách tạm thời
            String newUserName = extras.getString("new_user_name", "");
            float newRating = extras.getFloat("new_rating", 0f);
            String newComment = extras.getString("new_comment", "");

            if (newRating > 0 && !newComment.isEmpty()) {
                Review newReview = new Review(newUserName, R.drawable.img_2, newRating, newComment);
                reviewList.add(newReview);
                reviewAdapter.notifyDataSetChanged(); //làm mới recyclerview ể hiển thị đánh giá mới
            }
        }

        // Tải đánh giá từ Firebase
        loadReviews();

        // Xử lý sự kiện cho nút "Đánh giá"
        reviewLabel.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachDanhGiaActivity.this, DanhGiaActivity.class);
            intent.putExtra("book_id", bookId);
            intent.putExtra("book_title", bookTitle);
            intent.putExtra("book_author", bookAuthor);
            intent.putExtra("book_cover", bookCoverUrl);
            startActivity(intent);
        });
    }

    private void loadReviews() {
        if (bookId != null && !bookId.isEmpty()) {
            // Tải đánh giá theo bookId
            loadReviewsByBookId(bookId);
        } else if (bookTitle != null && !bookTitle.isEmpty()) {
            // Tìm bookId theo tiêu đề sách
            findBookIdByTitle(bookTitle);
        } else {
            // Nếu không có thông tin sách, hiển thị đánh giá mẫu
            loadSampleReviews();
        }
    }

    private void findBookIdByTitle(String title) {
        Log.d("DanhSachDanhGiaActivity", "Tìm kiếm sách với tiêu đề: " + title);

        if (TextUtils.isEmpty(title)) //nếu title là null hoặc chuỗi rỗng ("").
        {
            loadSampleReviews(); //hiển thị đánh giá mẫu
            return;
        }

        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    if (book != null && book.getTitle() != null &&
                            book.getTitle().equalsIgnoreCase(title.trim())) // so sánh title trong firebase vs title cua book mà k phân biệt cữ hoa, chữ thường
                    {
                        bookId = book.getId();
                        bookTitle = book.getTitle();
                        bookAuthor = book.getAuthor();
                        bookCoverUrl = book.getImageUrl();

                        Log.d("DanhSachDanhGiaActivity", "Đã tìm thấy sách: " + bookId + " - " + bookTitle);

                        loadReviewsByBookId(bookId);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Log.e("DanhSachDanhGiaActivity", "Không tìm thấy sách với tiêu đề: " + title);
                    loadSampleReviews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DanhSachDanhGiaActivity", "Lỗi tìm kiếm sách: " + databaseError.getMessage());
                loadSampleReviews();
            }
        });
    }

    private void loadReviewsByBookId(String bookId) {
        Log.d("DanhSachDanhGiaActivity", "Tải đánh giá cho sách ID: " + bookId);

        Query query = reviewsRef.orderByChild("bookId").equalTo(bookId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Review review = snapshot.getValue(Review.class);
                        if (review != null) {
                            reviewList.add(review);
                        }
                    }

                    Log.d("DanhSachDanhGiaActivity", "Đã tải " + reviewList.size() + " đánh giá");

                    // Sắp xếp đánh giá theo thời gian (mới nhất lên đầu)
                    Collections.sort(reviewList, (r1, r2) -> Long.compare(r2.getTimestamp(), r1.getTimestamp()));

                    // Cập nhật adapter
                    reviewAdapter.notifyDataSetChanged();

                    // Cập nhật điểm đánh giá trung bình
                    updateAverageRating();
                } else {
                    Log.d("DanhSachDanhGiaActivity", "Không có đánh giá nào cho sách ID: " + bookId);
                    // Nếu không có đánh giá, hiển thị đánh giá mẫu
                    loadSampleReviews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DanhSachDanhGiaActivity", "Lỗi tải đánh giá: " + databaseError.getMessage());
                Toast.makeText(DanhSachDanhGiaActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadSampleReviews();
            }
        });
    }

    private void loadSampleReviews() {
        // Chỉ tải đánh giá mẫu nếu danh sách rỗng
        if (reviewList.isEmpty()) {
            Log.d("DanhSachDanhGiaActivity", "Tải đánh giá mẫu");

            reviewList.add(new Review("Trần Tuệ Minh", R.drawable.img_2, 5.0f, "Phải công nhận là Mắt Biếc hay, đọc mà mình cứ ám ảnh mãi. Tình cảm dại dột, nông nổi nhưng không kém phần mãnh liệt."));
            reviewList.add(new Review("Nguyễn Văn A", R.drawable.img_2, 4.0f, "Cuốn sách rất hay, tôi đã đọc đi đọc lại nhiều lần."));
            reviewList.add(new Review("Lê Thị B", R.drawable.img_2, 4.5f, "Tác giả viết rất tình cảm, làm tôi cảm động."));

            reviewAdapter.notifyDataSetChanged();
            updateAverageRating();
        }
    }
    //Tính và hiển thị xếp hạng trung bình của sách, cập nhật vào Firebase.

    private void updateAverageRating() {
        if (reviewList.isEmpty()) {
            averageRatingBar.setRating(0f);
            averageRatingText.setText("0.00");
            return;
        }

        float totalRating = 0f;
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }

        float averageRating = totalRating / reviewList.size();
        averageRatingBar.setRating(averageRating);
        averageRatingText.setText(String.format("%.2f", averageRating));

        // Cập nhật điểm đánh giá trung bình của sách trong Firebase
        if (bookId != null && !bookId.isEmpty()) {
            booksRef.child(bookId).child("averageRating").setValue(averageRating)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DanhSachDanhGiaActivity", "Đã cập nhật điểm đánh giá trung bình: " + averageRating);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("DanhSachDanhGiaActivity", "Lỗi cập nhật điểm đánh giá: " + e.getMessage());
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại đánh giá khi quay lại màn hình
        if (bookId != null && !bookId.isEmpty()) {
            loadReviewsByBookId(bookId);
        }
    }
}
