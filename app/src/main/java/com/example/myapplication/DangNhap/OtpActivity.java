package com.example.myapplication.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class OtpActivity extends AppCompatActivity {

    private EditText edtOtp;
    private Button btnVerify;
    private TextView tvResend;
    private ImageView btnBack;
    private String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Ánh xạ View
        edtOtp = findViewById(R.id.edtEmailotp);
        btnVerify = findViewById(R.id.btnVerify);
        tvResend = findViewById(R.id.tvGuilai);
        btnBack = findViewById(R.id.btnBack);

        // Hiển thị bàn phím khi nhấn vào EditText
        showKeyboard(edtOtp);

        // Nhận dữ liệu màn hình trước
        sourceActivity = getIntent().getStringExtra("source");

        // Xử lý nút "Xác nhận"
        btnVerify.setOnClickListener(view -> {
            String code = edtOtp.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Vui lòng nhập mã xác thực!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OtpActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút "Gửi lại mã"
        tvResend.setOnClickListener(view -> {
            Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý nút "Quay lại"
        btnBack.setOnClickListener(view -> {
            Intent intent = null;

            if ("RegisterActivity".equals(sourceActivity)) {
                intent = new Intent(OtpActivity.this, RegisterActivity.class);
            } else if ("forgotpassActivity".equals(sourceActivity)) {
                intent = new Intent(OtpActivity.this, forgotpassActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                finish();
            }
        });
    }

    // Phương thức hiển thị bàn phím
    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}