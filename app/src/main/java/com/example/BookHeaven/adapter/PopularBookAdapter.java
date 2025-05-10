package com.example.BookHeaven.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.BookHeaven.DangNhap.LoginActivity;
import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Booklinh;

import java.util.ArrayList;
import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.ViewHolder> {
    private List<Booklinh> booklinhs;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Booklinh booklinh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PopularBookAdapter(List<Booklinh> booklinhs) {
        this.booklinhs = booklinhs != null ? booklinhs : new ArrayList<>();
    }

    // Thêm phương thức updateBooks để cập nhật danh sách sách
    public void updateBooks(List<Booklinh> newBooklinhs) {
        this.booklinhs = newBooklinhs != null ? newBooklinhs : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booklinh booklinh = booklinhs.get(position);

        // Load book image using Glide
        Glide.with(context)
                .load(booklinh.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_camera) // Thay bằng R.drawable.book_placeholder khi có
                .error(android.R.drawable.ic_menu_gallery) // Thay bằng R.drawable.book_error khi có
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgBook);

        // Load book title
        holder.tvTitle.setText(booklinh.getTitle() != null ? booklinh.getTitle() : "Unknown Title");

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                if (listener != null) {
                    listener.onItemClick(booklinh);
                }
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booklinhs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBook;
        TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.imgBook);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}