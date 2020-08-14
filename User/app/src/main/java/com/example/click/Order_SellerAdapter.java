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

import com.example.click.adapter.Seller_OrderAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Order_SellerAdapter extends RecyclerView.Adapter<Order_SellerAdapter.ViewHolder> {

    Context context;
    int mQuantity = 1;
    private List<Order> item_all_details;
    private Seller_OrderAdapter.OnItemClickListener mListener;


    public Order_SellerAdapter(Context context, List<Order> item_all_details) {
        this.context = context;
        this.item_all_details = item_all_details;
    }

    public void setOnItemClickListener(Seller_OrderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Order_SellerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_seller_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_SellerAdapter.ViewHolder holder, int position) {
        Order order = item_all_details.get(position);

        holder.text_order_id.setText("ID" + order.getId());
        Picasso.get().load(order.getPhoto()).into(holder.photo);
        holder.text_ad_detail.setText(order.getAd_detail());
        holder.text_price.setText("MYR"+order.getPrice());
        holder.text_quantity.setText("x" + order.getQuantity());
        holder.text_placed_date.setText("Order Placed on " + order.getDate());
        holder.text_status.setText(order.getStatus());
        holder.text_ship_placed.setText("Shipped out to "+ order.getDistrict());
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_order_id, text_ad_detail, text_price, text_quantity;
        TextView text_placed_date, text_status, text_ship_placed;
        ImageView photo;

        Button btn_accept, btn_reject;

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

            btn_accept = itemView.findViewById(R.id.btn_cancel);
            btn_reject = itemView.findViewById(R.id.btn_reject);

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onAcceptClick(position);
                        }
                    }
                }
            });

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onRejectClick(position);
                        }
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onAcceptClick(int position);

        void onRejectClick(int position);
    }
}
