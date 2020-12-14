package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.data.Checkout_Data;
import com.ketekmall.ketekmall.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Checkout_Adapter extends RecyclerView.Adapter<Checkout_Adapter.ViewHolder> {

    Context context;
    private List<Checkout_Data> item_all_details;
    private OnItemClickListener mListerner;

    public Checkout_Adapter(Context context, List<Checkout_Data> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_checkout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Checkout_Data itemAllDetails = item_all_details.get(position);


        String order_id = itemAllDetails.getId();
        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

        holder.Order_ID.setText("KM" + order_id);
        Picasso.get().load(photo_URL).into(holder.photo);

        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText(String.format("%.2f", Double.parseDouble(price)));
        holder.Quantity.setText("x" + itemAllDetails.getQuantity());
        holder.location_to.setText(itemAllDetails.getDelivery_division1());
        holder.btn_self.setVisibility(View.GONE);

        if (itemAllDetails.getDivision().equalsIgnoreCase(itemAllDetails.getDelivery_division())) {
            holder.btn_self.setVisibility(View.VISIBLE);
        }

        holder.shippin_price.setText(itemAllDetails.getDelivery_price());

        holder.btn_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onSelfClick(position);
                    holder.shippin_price.setText("MYR0.00");
                }
                holder.btn_self.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public interface OnItemClickListener {
        void onSelfClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView Order_ID, AdDetail, UnitPrice, Quantity, shippin_price, location_to;
        Button btn_self;

        public ViewHolder(View view) {
            super(view);

            Order_ID = view.findViewById(R.id.text_order_id);
            photo = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            Quantity = view.findViewById(R.id.quantity_display);
            shippin_price = view.findViewById(R.id.shippin_price);
            location_to = view.findViewById(R.id.location_to);
            btn_self = view.findViewById(R.id.btn_self);

        }
    }
}
