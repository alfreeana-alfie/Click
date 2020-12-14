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

import com.ketekmall.ketekmall.data.Order;
import com.ketekmall.ketekmall.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Order_BuyerAdapter extends RecyclerView.Adapter<Order_BuyerAdapter.ViewHolder> {

    Context context;
    private List<Order> item_all_details;
    private OnItemClickListener mListener;


    public Order_BuyerAdapter(Context context, List<Order> item_all_details) {
        this.context = context;
        this.item_all_details = item_all_details;
    }

    public void sortArrayHighest() {
        Collections.sort(item_all_details, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Double.compare(Double.parseDouble(o2.getId()), Double.parseDouble(o1.getId()));
            }
        });
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Order_BuyerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mybuying, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Order_BuyerAdapter.ViewHolder holder, int position) {
        Order order = item_all_details.get(position);

        holder.text_order_id.setText("KM" + order.getId());
        Picasso.get().load(order.getPhoto()).into(holder.photo);
        holder.text_ad_detail.setText(order.getAd_detail());
        holder.text_price.setText("MYR"+order.getPrice());
        holder.text_quantity.setText("x" + order.getQuantity());
        holder.text_placed_date.setText("Order Placed on " + order.getDate());
        holder.text_status.setText(order.getStatus());
        holder.text_ship_placed.setText("Shipped out to "+ order.getDistrict());

        if(order.getStatus().equals("Received")){
            holder.btn_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_order_id, text_ad_detail, text_price, text_quantity;
        TextView text_placed_date, text_status, text_ship_placed;
        ImageView photo;

        Button btn_cancel, btn_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_order_id = itemView.findViewById(R.id.text_order_id);
            photo = itemView.findViewById(R.id.photo);

            text_ad_detail = itemView.findViewById(R.id.text_ad_detail);
            text_price = itemView.findViewById(R.id.text_price);
            text_quantity = itemView.findViewById(R.id.text_quantity);

            text_placed_date = itemView.findViewById(R.id.text_placed_date);
            text_status = itemView.findViewById(R.id.text_status);

            text_ship_placed = itemView.findViewById(R.id.text_ship_placed);

            btn_review = itemView.findViewById(R.id.btn_review);

            btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onReviewClick(position);
                        }
                    }
                }
            });

            btn_cancel = itemView.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onCancelClick(position);
                            btn_cancel.setVisibility(View.GONE);
                        }
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onCancelClick(int position);
        void onReviewClick(int position);
    }
}
