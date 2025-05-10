package com.example.BookHeaven.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Booklinh;

import java.util.ArrayList;
import java.util.List;

public class BookAdapterlinh extends RecyclerView.Adapter<BookAdapterlinh.BookViewHolder> {
    private List<Booklinh> booklinhs;
    private boolean isFeatured;
    private Context context;
    private OnItemClickListener listener;
    private static final String TAG = "BookAdapterlinh";

    public interface OnItemClickListener {
        void onItemClick(Booklinh booklinh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookAdapterlinh(List<Booklinh> booklinhs, boolean isFeatured) {
        this.booklinhs = booklinhs != null ? booklinhs : new ArrayList<>();
        this.isFeatured = isFeatured;
    }

    public void updateBooks(List<Booklinh> newBooklinhs) {
        this.booklinhs = newBooklinhs != null ? newBooklinhs : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if (isFeatured) {
            view = LayoutInflater.from(context).inflate(R.layout.item_featured_book, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_book_linh, parent, false);
        }
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Booklinh booklinh = booklinhs.get(position);

        // Log book data for debugging
        Log.d(TAG, "Binding book: ID=" + booklinh.getId() + ", Title=" + booklinh.getTitle() + ", ImageUrl=" + booklinh.getImageUrl());

        // Load book image
        String imageUrl = booklinh.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.book_placeholder) // Đảm bảo có drawable này
                    .error(R.drawable.book_error) // Đảm bảo có drawable này
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.bookImage);
        } else {
            Log.w(TAG, "Image URL is null or empty for book ID: " + booklinh.getId());
            holder.bookImage.setImageResource(R.drawable.book_error); // Hiển thị ảnh lỗi
        }

        // Load book title (nếu có TextView trong layout)
        if (holder.bookTitle != null) {
            String title = booklinh.getTitle() != null ? booklinh.getTitle() : "Unknown Title";
            holder.bookTitle.setText(title);
        }

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "Clicked book: ID=" + booklinh.getId() + ", Title=" + booklinh.getTitle());

            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                if (listener != null) {
                    listener.onItemClick(booklinh);
                } else {
                    Log.w(TAG, "OnItemClickListener is null");
                    Toast.makeText(context, "Không thể mở chi tiết sách!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "User not logged in, redirecting to LoginActivity");
                Intent intent = new Intent(context, LoginActivity.class);
                // Lưu ID sách để quay lại sau khi đăng nhập (tùy chọn)
                intent.putExtra("bookId", booklinh.getId());
                context.startActivity(intent);
                Toast.makeText(context, "Vui lòng đăng nhập để xem chi tiết sách!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set scale for featured books
        if (isFeatured) {
            holder.itemView.setScaleX(1.0f);
            holder.itemView.setScaleY(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return booklinhs.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle; // Có thể null nếu layout không có TextView

        BookViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            bookTitle = itemView.findViewById(R.id.bookTitle); // Có thể null
        }
    }
}