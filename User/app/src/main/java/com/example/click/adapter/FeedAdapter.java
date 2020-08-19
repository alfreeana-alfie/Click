package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.Delivery_Combine;
import com.example.click.Feed;
import com.example.click.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    Context context;
    private List<Feed> item_all_details,item_all_details2;

    public FeedAdapter(Context context, List<Feed> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Feed itemAllDetails = item_all_details.get(position);

        String photo_URL = itemAllDetails.getPhoto();
        Picasso.get().load(photo_URL).into(holder.feed_image);
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView feed_image;

        public ViewHolder(View view) {
            super(view);

            feed_image = view.findViewById(R.id.feed_image);
        }
    }
}
