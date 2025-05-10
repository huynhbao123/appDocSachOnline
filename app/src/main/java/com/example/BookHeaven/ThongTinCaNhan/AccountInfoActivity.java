package com.example.BookHeaven.ThongTinCaNhan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.R;

public class AccountInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        // Khởi tạo các TextView
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvUsername = findViewById(R.id.tvUsername);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", "Không có email");
        String username = prefs.getString("username", "Không có tên người dùng");

        // Log để debug
        Log.d("AccountInfo", "Email: " + email);
        Log.d("AccountInfo", "Username: " + username);

        // Gán dữ liệu vào TextView
        tvEmail.setText(email);
        tvUsername.setText(username);
    }
}