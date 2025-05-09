package com.example.BookHeaven.Vietdanhgia;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.R;

public class DanhGiaActivity extends AppCompatActivity {

    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private RatingBar ratingBar;
    private EditText reviewComment;
    private Button submitReviewButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        ratingBar = findViewById(R.id.rating_bar);
        reviewComment = findViewById(R.id.review_comment);
        submitReviewButton = findViewById(R.id.btn_submit_review);

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("book_title", "Mắt biếc");
            String author = extras.getString("book_author", "Nguyễn Nhật Ánh");
            int coverImage = extras.getInt("book_cover", R.drawable.img);

            // Gán dữ liệu vào giao diện
            bookTitle.setText(title);
            bookAuthor.setText(author);
            bookCover.setImageResource(coverImage);
        }

        // Xử lý sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        // Xử lý sự kiện khi nhấn nút "Gửi"
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String comment = reviewComment.getText().toString().trim();

                if (rating == 0) {
                    Toast.makeText(DanhGiaActivity.this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (comment.isEmpty()) {
                    Toast.makeText(DanhGiaActivity.this, "Vui lòng nhập nhận xét!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển sang màn hình DanhSachDanhGiaActivity và truyền dữ liệu đánh giá mới
                Intent intent = new Intent(DanhGiaActivity.this, DanhSachDanhGiaActivity.class);
                intent.putExtra("average_rating", 4.06f); // Thay bằng giá trị thực tế nếu cần
                intent.putExtra("new_user_name", "Người dùng mới");
                intent.putExtra("new_rating", rating);
                intent.putExtra("new_comment", comment);
                startActivity(intent);

                // Kết thúc activity hiện tại
                finish();
            }
        });
    }
}