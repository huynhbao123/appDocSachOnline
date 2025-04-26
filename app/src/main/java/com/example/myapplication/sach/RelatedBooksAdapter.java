package com.example.myapplication.sach;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class RelatedBooksAdapter extends RecyclerView.Adapter<RelatedBooksAdapter.BookViewHolder> {

    private List<Bookngan> bookList;
    private Context context;

    public RelatedBooksAdapter(Context context, List<Bookngan> bookList) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Bookngan book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());

        // Tải hình ảnh từ ID tài nguyên
        Glide.with(context)
                .load(book.getCoverImage())
                .placeholder(R.drawable.img)
                .error(R.drawable.img_3)
                .into(holder.bookCover);

        // Xử lý sự kiện khi nhấn vào sách
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSach.class); // Sửa từ ChitietSach thành ChiTietSach
            intent.putExtra("book", book);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
        }
    }
}