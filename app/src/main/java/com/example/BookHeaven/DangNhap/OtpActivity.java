package com.example.BookHeaven.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OtpActivity extends AppCompatActivity {

    private EditText edtOtp;
    private Button btnVerify;
    private TextView tvResend;
    private ImageView btnBack;
    private String sourceActivity;
    private String email;
    private int attemptCount = 0;
    private static final int MAX_ATTEMPTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Ánh xạ View
        edtOtp = findViewById(R.id.edtEmailqmk);
        btnVerify = findViewById(R.id.btnGuima);
        tvResend = findViewById(R.id.tvRegister);
        btnBack = findViewById(R.id.btnBack);

        // Hiển thị bàn phím khi nhấn vào EditText
        showKeyboard(edtOtp);

        // Nhận dữ liệu màn hình trước
        sourceActivity = getIntent().getStringExtra("source");
        email = getIntent().getStringExtra("email");

        // Xử lý nút "Xác nhận"
        btnVerify.setOnClickListener(view -> {
            String code = edtOtp.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Vui lòng nhập mã xác thực!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate OTP verification by checking Firebase email verification status
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (user.isEmailVerified()) {
                        // Verification successful
                        Toast.makeText(OtpActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Verification failed
                        attemptCount++;
                        if (attemptCount >= MAX_ATTEMPTS) {
                            Toast.makeText(OtpActivity.this,
                                    "Xác thực thất bại quá 3 lần. Vui lòng đăng ký lại.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OtpActivity.this, RegisterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OtpActivity.this,
                                    "Mã xác thực không đúng. Còn " + (MAX_ATTEMPTS - attemptCount) + " lần thử.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(OtpActivity.this, "Lỗi: Vui lòng đăng ký lại.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút "Gửi lại mã"
        tvResend.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(OtpActivity.this, "Đã gửi lại mã xác thực", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OtpActivity.this,
                                        "Không thể gửi mã xác thực: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(OtpActivity.this, "Lỗi: Vui lòng đăng ký lại.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút "Quay lại"
        btnBack.setOnClickListener(view -> {
            Intent intent;
            if ("RegisterActivity".equals(sourceActivity)) {
                intent = new Intent(OtpActivity.this, RegisterActivity.class);
            } else if ("forgotpassActivity".equals(sourceActivity)) {
                intent = new Intent(OtpActivity.this, forgotpassActivity.class);
            } else {
                intent = new Intent(OtpActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        });
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}