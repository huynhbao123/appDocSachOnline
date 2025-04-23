package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.ViewHolder> {
    private List<com.example.myapplication.model.Booklinh> booklinhs;
    private OnItemClickListener listener;

    public PopularBookAdapter(List<com.example.myapplication.model.Booklinh> booklinhs) {
        this.booklinhs = booklinhs;
    }

    public interface OnItemClickListener {
        void onItemClick(com.example.myapplication.model.Booklinh booklinh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.example.myapplication.model.Booklinh booklinh = booklinhs.get(position);
        holder.bind(booklinh, listener);
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

        void bind(final com.example.myapplication.model.Booklinh booklinh, final OnItemClickListener listener) {
            // Set book cover image
            // imgBook.setImageResource(book.getCoverResourceId());

            // Set book title
            tvTitle.setText(booklinh.getTitle());

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(booklinh);
                }
            });
        }
    }
}