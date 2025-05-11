package com.example.BookHeaven.sach;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelatedBooksAdapter extends RecyclerView.Adapter<RelatedBooksAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;

    public RelatedBooksAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    // Phương thức để cập nhật danh sách sách
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged(); // Làm mới RecyclerView
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        Picasso.get()
                .load(book.getImageUrl())
                .placeholder(R.drawable.book_khoi_nghiep)
                .error(R.drawable.book_tinh_yeu_dau_doi)
                .into(holder.bookCover);
        holder.bookTitle.setText(book.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSach.class);
            intent.putExtra("book", book); // Đảm bảo book chứa đầy đủ author
            Log.d("RelatedBooksAdapter", "Passing book to ChiTietSach: " + book.getTitle() + ", Author: " + book.getAuthor());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }
    }
}