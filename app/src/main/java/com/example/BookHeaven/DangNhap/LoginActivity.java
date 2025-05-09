package com.example.BookHeaven.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.TrangChu;
import com.example.BookHeaven.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ImageView btnBack;
    private TextView tvForgotPassword, tvRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Đảm bảo đây đúng là file bạn gửi

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // 👇 Sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Giả định đăng nhập thành công
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            // Quay lại MainActivity
            Intent intent = new Intent(LoginActivity.this, TrangChu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 👇 Sự kiện nút Quay lại
        btnBack.setOnClickListener(view -> {
            finish(); // Quay về màn hình trước (MainActivity)
        });

        // 👇 Sự kiện "Quên mật khẩu"
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, forgotpassActivity.class);
            startActivity(intent);
        });

        // 👇 Sự kiện "Đăng ký"
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Mở bàn phím mặc định cho người dùng dễ nhập
        autoShowKeyboard(edtEmail);
    }

    private void autoShowKeyboard(EditText editText) {
        editText.requestFocus();
        editText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }
}
