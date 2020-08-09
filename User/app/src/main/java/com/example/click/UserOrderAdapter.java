package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {

    Context context;
    int mQuantity = 1;
    private List<Item_All_Details> item_all_details;

    public UserOrderAdapter(Context context, List<Item_All_Details> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Item_All_Details itemAllDetails = item_all_details.get(position);

        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText("MYR" + price);
        holder.SubTotal.setText("MYR" + price);
        holder.Quantity.setText("1");

        Picasso.get().load(photo_URL).into(holder.ItemImageView);
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, UnitPrice, SubTotal, Quantity;

        public ViewHolder(View view) {
            super(view);

            ItemImageView = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            SubTotal = view.findViewById(R.id.subtotal_display);
            Quantity = view.findViewById(R.id.integer_number);
        }
    }
}
