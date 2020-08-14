package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.R;
import com.example.click.data.Item_All_Details;
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

        String order_id = itemAllDetails.getSeller_id();
        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

        holder.Order_ID.setText(order_id);
        Picasso.get().load(photo_URL).into(holder.photo);

        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText("MYR" + price);
        holder.Quantity.setText(itemAllDetails.getQuantity());
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView Order_ID, AdDetail, UnitPrice, Quantity;

        public ViewHolder(View view) {
            super(view);

            Order_ID = view.findViewById(R.id.text_order_id);
            photo = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            Quantity = view.findViewById(R.id.quantity_display);
        }
    }
}
