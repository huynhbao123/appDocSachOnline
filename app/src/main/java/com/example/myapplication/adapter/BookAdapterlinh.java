package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.DangNhap.LoginActivity;
import com.example.myapplication.R;

import java.util.List;

public class BookAdapterlinh extends RecyclerView.Adapter<BookAdapterlinh.BookViewHolder> {
    private List<com.example.myapplication.model.Booklinh> booklinhs;
    private boolean isFeatured;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(com.example.myapplication.model.Booklinh booklinh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookAdapterlinh(List<com.example.myapplication.model.Booklinh> booklinhs, boolean isFeatured) {
        this.booklinhs = booklinhs;
        this.isFeatured = isFeatured;
    }

    public void updateBooks(List<com.example.myapplication.model.Booklinh> newBooklinhs) {
        this.booklinhs = newBooklinhs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if (isFeatured) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_featured_book, parent, false);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_book_linh, parent, false);
        }
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        com.example.myapplication.model.Booklinh booklinh = booklinhs.get(position);

        // Load book image
        Glide.with(context)
                .load(booklinh.getImageUrl())
                .placeholder(R.drawable.book_placeholder)
                .into(holder.bookImage);

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

        // Set scale for featured books
        if (isFeatured) {
            holder.itemView.setScaleX(1.0f);
            holder.itemView.setScaleY(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return booklinhs != null ? booklinhs.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;

        BookViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
        }
    }
}

