package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.Delivery_Combine;
import com.example.click.R;
import com.example.click.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserOrderAdapter_Other extends RecyclerView.Adapter<UserOrderAdapter_Other.ViewHolder> {

    Context context;
    private List<Delivery_Combine> item_all_details,item_all_details2;

    public UserOrderAdapter_Other(Context context, List<Delivery_Combine> item_all_detailsList, List<Delivery_Combine> item_all_detailsList2) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
        this.item_all_details2 = item_all_detailsList2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Delivery_Combine itemAllDetails = item_all_details.get(position);
        final Delivery_Combine itemAllDetails2 = item_all_details2.get(position);


        String order_id = itemAllDetails.getId();
        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

        holder.Order_ID.setText("KM" + order_id);
        Picasso.get().load(photo_URL).into(holder.photo);

        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText("MYR" + String.format("%.2f", Double.parseDouble(price)));
        holder.Quantity.setText("x"+itemAllDetails.getQuantity());
        holder.shippin_price.setText("MYR" + itemAllDetails.getDelivery_price());

    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView Order_ID, AdDetail, UnitPrice, Quantity, shippin_price;

        public ViewHolder(View view) {
            super(view);

            Order_ID = view.findViewById(R.id.text_order_id);
            photo = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            Quantity = view.findViewById(R.id.quantity_display);
            shippin_price = view.findViewById(R.id.shippin_price);

        }
    }
}
