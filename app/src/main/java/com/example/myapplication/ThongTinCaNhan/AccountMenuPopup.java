package com.example.myapplication.ThongTinCaNhan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;

import com.example.myapplication.CategoryActivity;
import com.example.myapplication.DangNhap.ChangePasswordActivity;
import com.example.myapplication.LibraryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class AccountMenuPopup {
    private Context context;
    private PopupWindow popupWindow;
    private View popupView;

    public AccountMenuPopup(Context context) {
        this.context = context;
        initializePopup();
    }

    private void initializePopup() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_account_menu, null);

        // Khởi tạo PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                true
        );

        // Thiết lập background và animation
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setElevation(10);

        // Xử lý sự kiện click cho các menu item
        setupMenuItemClicks();
    }

    private void setupMenuItemClicks() {
        // Thông tin tài khoản
        popupView.findViewById(R.id.tvAccountInfo).setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.myapplication.ThongTinCaNhan.AccountInfoActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();
        });

        // Thư viện đọc
        popupView.findViewById(R.id.tvLibrary).setOnClickListener(v -> {
            Intent intent = new Intent(context, LibraryActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();
        });

        // Đổi mật khẩu
        popupView.findViewById(R.id.tvChangePassword).setOnClickListener(v -> {
            Intent intent = new Intent(context, ChangePasswordActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();
        });

        // Thể loại
        popupView.findViewById(R.id.tvCategory).setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();
        });

        // Đăng xuất
        popupView.findViewById(R.id.tvLogout).setOnClickListener(v -> {
            // ✅ Đặt lại trạng thái đã đăng nhập
            SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false); // reset login
            editor.apply();

            // ✅ Quay về MainActivity (với giao diện chưa đăng nhập)
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            popupWindow.dismiss();
        });

    }

    public void show(View anchorView) {
        // Hiển thị popup bên dưới avatar
        popupWindow.showAsDropDown(anchorView, 0, 10, Gravity.END);
    }
}