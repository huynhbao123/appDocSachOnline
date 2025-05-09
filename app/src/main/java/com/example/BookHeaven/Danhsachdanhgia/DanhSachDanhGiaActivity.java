package com.example.BookHeaven.Danhsachdanhgia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.DanhGiaActivity;
import com.example.BookHeaven.Vietdanhgia.Review;

import java.util.ArrayList;
import java.util.List;

public class DanhSachDanhGiaActivity extends AppCompatActivity {

    private RatingBar averageRatingBar;
    private TextView averageRatingText;
    private TextView reviewLabel;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_danh_gia);

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        averageRatingBar = findViewById(R.id.average_rating_bar);
        averageRatingText = findViewById(R.id.average_rating);
        reviewLabel = findViewById(R.id.review_label);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);

        // Xử lý sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent (đánh giá trung bình và danh sách đánh giá)
        reviewList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String newUserName = extras.getString("new_user_name", "Người dùng mới");
            float newRating = extras.getFloat("new_rating", 0f);
            String newComment = extras.getString("new_comment", "");

            // Thêm các đánh giá mẫu
            reviewList.add(new Review("Trần Tuệ Minh", R.drawable.img_2, 5.0f, "Phải công nhận là Mắt Biếc hay, đọc mà mình cứ ám ảnh mãi. Tình cảm dại dột, nông nổi nhưng không kém phần mãnh liệt."));
            reviewList.add(new Review("Trần Tuệ Minh", R.drawable.img_2, 4.0f, "Phải công nhận là Mắt Biếc hay, đọc mà mình cứ ám ảnh mãi. Tình cảm dại dột, nông nổi nhưng không kém phần mãnh liệt."));
            reviewList.add(new Review("Trần Tuệ Minh", R.drawable.img_2, 4.0f, "Phải công nhận là Mắt Biếc hay, đọc mà mình cứ ám ảnh mãi. Tình cảm dại dột, nông nổi nhưng không kém phần mãnh liệt."));

            // Thêm đánh giá mới (nếu có) vào đầu danh sách
            if (newRating > 0 && !newComment.isEmpty()) {
                reviewList.add(0, new Review(newUserName, R.drawable.img_2, newRating, newComment));
            }
        }

        // Tính toán điểm đánh giá trung bình
        float averageRating = calculateAverageRating();
        averageRatingBar.setRating(averageRating);
        averageRatingText.setText(String.format("%.2f", averageRating));

        // Thiết lập RecyclerView
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        // Xử lý sự kiện cho nút "Đánh giá"
        reviewLabel.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachDanhGiaActivity.this, DanhGiaActivity.class);
            intent.putExtra("book_title", "Mắt biếc");
            intent.putExtra("book_author", "Nguyễn Nhật Ánh");
            intent.putExtra("book_cover", R.drawable.img);
            startActivity(intent);
        });
    }

    private float calculateAverageRating() {
        if (reviewList.isEmpty()) {
            return 0f;
        }
        float totalRating = 0f;
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }
        return totalRating / reviewList.size();
    }
}