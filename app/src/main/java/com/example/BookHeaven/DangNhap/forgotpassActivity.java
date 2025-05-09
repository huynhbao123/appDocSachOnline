package com.example.BookHeaven.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassActivity extends AppCompatActivity {

    private EditText edtEmail;
    private ImageView btnBack;
    private Button btnGuima;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmailqmk);
        btnBack = findViewById(R.id.btnBack);
        btnGuima = findViewById(R.id.btnGuima);
        tvRegister = findViewById(R.id.tvRegister);

        // Hiển thị bàn phím tự động
        showKeyboard(edtEmail);

        // Xử lý sự kiện gửi email đặt lại mật khẩu
        btnGuima.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            hideKeyboard();

            // Send password reset email
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotpassActivity.this,
                                        "Email đặt lại mật khẩu đã được gửi",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(forgotpassActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(forgotpassActivity.this,
                                        task.getException() != null ? task.getException().getMessage() : "Gửi email thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Xử lý sự kiện quay lại LoginActivity
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(forgotpassActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện chuyển sang RegisterActivity
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(forgotpassActivity.this, RegisterActivity.class);
            intent.putExtra("source", "forgotpassActivity");
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