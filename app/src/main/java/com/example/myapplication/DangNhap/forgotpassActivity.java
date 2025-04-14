package com.example.myapplication.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class forgotpassActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnGuima;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        btnGuima = findViewById(R.id.btnGuima);
        tvRegister = findViewById(R.id.tvRegister);

        // Xử lý sự kiện quay lại LoginActivity
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(forgotpassActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện chuyển sang OtpActivity
        btnGuima.setOnClickListener(v -> {
            hideKeyboard();
            Intent otpIntent = new Intent(forgotpassActivity.this, OtpActivity.class);
            otpIntent.putExtra("source", "forgotpassActivity");
            startActivity(otpIntent);
            finish();
        });

        // Xử lý sự kiện chuyển sang RegisterActivity
        tvRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(forgotpassActivity.this, RegisterActivity.class);
            registerIntent.putExtra("source", "forgotpassActivity");
            startActivity(registerIntent);
            finish();
        });
    }

    // Phương thức ẩn bàn phím
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}