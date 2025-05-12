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
import com.example.BookHeaven.models.Book;

import java.util.ArrayList;
import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.ViewHolder> {
    private List<Book> books;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PopularBookAdapter(List<Book> books) {
        this.books = books != null ? books : new ArrayList<>();
    }

    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Sử dụng item_book_linh.xml thay vì item_popular_book.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_linh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        // Load book image using Glide
        Glide.with(context)
                .load(book.getImageUrl())
                .placeholder(R.drawable.book_placeholder) // Sử dụng placeholder từ item_book_linh.xml
                .error(R.drawable.book_placeholder) // Sử dụng error placeholder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgBook);

        // Load book title
        holder.tvTitle.setText(book.getTitle() != null ? book.getTitle() : "Unknown Title");

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                if (listener != null) {
                    listener.onItemClick(book);
                }
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBook;
        TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            // Sử dụng các ID từ item_book_linh.xml
            imgBook = itemView.findViewById(R.id.bookImage);
            tvTitle = itemView.findViewById(R.id.bookTitle);
        }
    }
}