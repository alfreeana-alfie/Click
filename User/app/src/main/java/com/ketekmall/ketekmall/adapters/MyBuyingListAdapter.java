package com.ketekmall.ketekmall.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.models.Order;
import com.ketekmall.ketekmall.R;
import com.squareup.picasso.Picasso;

import java.util.*;

public class MyBuyingListAdapter extends RecyclerView.Adapter<MyBuyingListAdapter.ViewHolder> {

    Context context;
    private List<Order> item_all_details;
    private OnItemClickListener mListener;


    public MyBuyingListAdapter(Context context, List<Order> item_all_details) {
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
    public MyBuyingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mybuying, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyBuyingListAdapter.ViewHolder holder, int position) {
        Order order = item_all_details.get(position);

        holder.text_order_id.setText("KM" + order.getId());
        Picasso.get().load(order.getPhoto()).into(holder.photo);
        holder.text_ad_detail.setText(order.getAd_detail());
        holder.text_price.setText("RM"+order.getPrice());
        holder.text_quantity.setText("x" + order.getQuantity());
        holder.text_placed_date.setText("Date" + order.getDate());
        holder.text_status.setText(order.getStatus());
        holder.text_ship_placed.setText("Ships to"+ order.getDistrict());

        if(order.getStatus().equals("Received")){
            holder.btn_cancel.setVisibility(View.GONE);
        }else if(order.getStatus().equals("Cancelled") || order.getStatus().equals("Unsuccessful") || order.getStatus().equals("Rejected")){
            holder.btn_cancel.setVisibility(View.GONE);
            holder.btn_review.setVisibility(View.GONE);
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
