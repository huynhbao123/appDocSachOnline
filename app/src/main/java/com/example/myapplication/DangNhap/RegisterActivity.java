package com.example.myapplication.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private ImageView btnBack;
    private String sourceActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các thành phần giao diện
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // Lấy source activity từ Intent
        sourceActivity = getIntent().getStringExtra("source");

        // Hiển thị bàn phím tự động khi màn hình được mở
        showKeyboard(edtEmail);

        // Xử lý sự kiện nút quay lại
        btnBack.setOnClickListener(view -> {
            Intent intent = null;

            if ("forgotpassActivity".equals(sourceActivity)) {
                intent = new Intent(RegisterActivity.this, forgotpassActivity.class);
            } else {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện nút đăng ký
        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang màn hình OTP với source là RegisterActivity
            Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
            intent.putExtra("source", "RegisterActivity");
            startActivity(intent);
            finish();
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