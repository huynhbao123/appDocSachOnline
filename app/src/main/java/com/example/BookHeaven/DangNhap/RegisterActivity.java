package com.example.BookHeaven.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private ImageView btnBack;
    private FirebaseAuth mAuth;
    private String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // Lấy source activity từ Intent
        sourceActivity = getIntent().getStringExtra("source");

        // Hiển thị bàn phím tự động
        showKeyboard(edtEmail);

        // Sự kiện nút Đăng ký
        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user with Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Send email verification
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(verifyTask -> {
                                            if (verifyTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Vui lòng kiểm tra email để lấy mã xác minh",
                                                        Toast.LENGTH_LONG).show();
                                                // Navigate to OtpActivity
                                                Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                                                intent.putExtra("source", "RegisterActivity");
                                                intent.putExtra("email", email);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Không thể gửi mã xác minh: " + verifyTask.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                // Registration failed
                                String errorMessage = "Đăng ký thất bại";
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    errorMessage = "Email đã được sử dụng";
                                } else if (task.getException() != null) {
                                    errorMessage = task.getException().getMessage();
                                }
                                Log.e("RegisterActivity", "Registration failed: ", task.getException());
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Sự kiện nút Quay lại
        btnBack.setOnClickListener(view -> {
            Intent intent;
            if ("forgotpassActivity".equals(sourceActivity)) {
                intent = new Intent(RegisterActivity.this, forgotpassActivity.class);
            } else {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
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