package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    int mQuantity = 1;
    private List<Review> item_all_details;

    public ReviewAdapter(Context context, List<Review> item_all_details) {
        this.context = context;
        this.item_all_details = item_all_details;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review order = item_all_details.get(position);

        holder.customer_name.setText(order.getCustomer_Name());
        holder.review.setText(order.getReview());
        holder.ratingBar.setRating(order.getRating());

        String image_default = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

        Picasso.get().load(image_default).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name, review;
        RatingBar ratingBar;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customer_name = itemView.findViewById(R.id.customer_name);
            review = itemView.findViewById(R.id.review);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            image = itemView.findViewById(R.id.image);
        }
    }
}
