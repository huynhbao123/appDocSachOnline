package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView contentText;
    private float currentTextSize = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        contentText = findViewById(R.id.contentText);
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton textSizeButton = findViewById(R.id.textSizeButton);


        contentText.setText("Cuốn sách mang đến cho bạn 4 ý tưởng sâu sắc mà nếu thấu hiểu được, " +
                "bạn sẽ có được tiềm thức và sức mạnh để tạo dựng một doanh nghiệp nhỏ phát triển bền vững. " +
                "Còn nếu bỏ qua, bạn sẽ giống như hàng nghìn người đầu tư công sức, tiền bạc và cả cuộc sống " +
                "để khởi nghiệp nhưng vẫn thất bại, hay phải vật vã mãi để duy trì sự tồn tại nhạt nhoà cho " +
                "doanh nghiệp của mình...");

        // Xử lý sự kiện nút back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý sự kiện nút thay đổi kích thước chữ
        textSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTextSize += 2;
                if (currentTextSize > 28) {
                    currentTextSize = 16;
                }
                contentText.setTextSize(currentTextSize);
            }
        });
    }
}