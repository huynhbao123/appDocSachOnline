package com.example.BookHeaven;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Kích hoạt persistence ngay khi ứng dụng khởi động
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}