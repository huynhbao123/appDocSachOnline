package com.example.BookHeaven.Danhsachdanhgia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    //danh sách đánh giá
    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    //reviewholder là để gi các thành phần giao diện cho mục đánh giá
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }
    //gán các đánh giá vào giao diện của danh sách đánh giá
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (holder.userImage != null) {
            holder.userImage.setImageResource(review.getUserImage());
        }
        if (holder.userName != null) {
            holder.userName.setText(review.getUserName());
        }
        if (holder.ratingBar != null) {
            holder.ratingBar.setRating(review.getRating());
        }
        if (holder.comment != null) {
            holder.comment.setText(review.getComment());
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    //Hiển thị trên giao diện
    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        RatingBar ratingBar;
        TextView comment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.review_user_image);
            userName = itemView.findViewById(R.id.review_user_name);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);
            comment = itemView.findViewById(R.id.comment); // Đổi từ review_comment thành comment
        }

    }
}