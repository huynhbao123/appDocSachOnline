package com.example.BookHeaven.sach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Book;

import java.util.List;

public class RelatedBooksAdapter extends RecyclerView.Adapter<RelatedBooksAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;

    public RelatedBooksAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
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
        holder.bookCover.setImageResource(book.getCoverResourceId());
        holder.bookTitle.setText(book.getTitle());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover); // Sửa thành bookCover
            bookTitle = itemView.findViewById(R.id.bookTitle); // Sửa thành bookTitle
        }
    }
}