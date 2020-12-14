package com.ketekmall.ketekmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Rating;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Rating_Adapter extends RecyclerView.Adapter<Rating_Adapter.ViewHolder> {

    Context context;
    private List<Rating> item_all_details;

    public Rating_Adapter(Context context, List<Rating> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_myrating, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Rating itemAllDetails = item_all_details.get(position);
        String addetail = itemAllDetails.getAd_detail();
        String CustomerName = itemAllDetails.getCustomer_Name();
        String Review = itemAllDetails.getReview();
        String ItemImage = itemAllDetails.getPhoto();
        String Customer_Image = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";
        Float ratingBarValue = itemAllDetails.getRating();

        Picasso.get().load(ItemImage).into(holder.ItemImageView);
        holder.AdDetail.setText(addetail);

        Picasso.get().load(Customer_Image).into(holder.CustomerImage);
        holder.CustomerName.setText(CustomerName);
        holder.Review.setText(Review);

        holder.rating.setRating(ratingBarValue);

    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView, CustomerImage;
        TextView AdDetail, CustomerName, Review;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemImageView = itemView.findViewById(R.id.item_image);
            AdDetail = itemView.findViewById(R.id.ad_detail);

            CustomerImage = itemView.findViewById(R.id.customer_image);
            CustomerName = itemView.findViewById(R.id.customer_name);
            Review = itemView.findViewById(R.id.review);
            rating = itemView.findViewById(R.id.ratingBar);

        }
    }
}
