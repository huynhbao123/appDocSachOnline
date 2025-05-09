package com.example.BookHeaven.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.BookHeaven.R;
import com.example.BookHeaven.models.Categorylinh;
import java.util.List;

public class CategoryAdapterlinh extends RecyclerView.Adapter<CategoryAdapterlinh.CategoryViewHolder> {
    private List<Categorylinh> categories;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Categorylinh categorylinh);
    }

    public CategoryAdapterlinh(List<Categorylinh> categories) {
        this.categories = categories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_categorylinh, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categorylinh categorylinh = categories.get(position);
        holder.bind(categorylinh, listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView tvCategoryName;
        private TextView tvBookCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
        }

        public void bind(Categorylinh categorylinh, OnItemClickListener listener) {
            tvCategoryName.setText(categorylinh.getName());
            tvBookCount.setText(categorylinh.getBookCount() + " cuốn sách");

            if (categorylinh.getImageUrl() != null && !categorylinh.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(categorylinh.getImageUrl())
                        .into(imgCategory);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(categorylinh);
                }
            });
        }
    }
}