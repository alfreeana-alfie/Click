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

import com.example.click.Order;
import com.example.click.OrderDone;
import com.example.click.R;
import com.example.click.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    int mQuantity;
    private List<OrderDone> item_all_details;
    private OnItemClickListener mListerner;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public OrderAdapter(Context context, List<OrderDone> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_listview, parent, false);
        return new ViewHolder(view);
    }

    public void sortArrayHighest() {
        Collections.sort(item_all_details, new Comparator<OrderDone>() {
            @Override
            public int compare(OrderDone o1, OrderDone o2) {
                return o2.getStatus().compareToIgnoreCase(o1.getStatus());
            }
        });
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final OrderDone itemAllDetails = item_all_details.get(position);
        String addetail = itemAllDetails.getItemName();
        String ItemPrice = itemAllDetails.getItemPrice();
        String ItemImage = itemAllDetails.getItemImage();

        String delivery_Addr = itemAllDetails.getDeliveryAddress();
        String delivery_date = itemAllDetails.getDeliveryTime();
        String delivery_price = itemAllDetails.getDeliveryPrice();

        Picasso.get().load(ItemImage).into(holder.ItemImageView);
        holder.AdDetail.setText(addetail);
        holder.Price.setText(ItemPrice);
        holder.Delivery_Price.setText("MYR"+ delivery_price);
        holder.Delivery_Time.setText(delivery_date);
        holder.Delivery_Address.setText(delivery_Addr);
        holder.GrandTotal.setText("MYR"+ itemAllDetails.getGrandtotal());
        holder.Status.setText(itemAllDetails.getStatus());
        holder.Quantity.setText("x"+ itemAllDetails.getQuantity());
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, Price, Delivery_Address, Delivery_Time, Delivery_Price, GrandTotal, Status, Quantity;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemImageView = itemView.findViewById(R.id.item_image);
            AdDetail = itemView.findViewById(R.id.item_name);
            Price = itemView.findViewById(R.id.item_price);
            Quantity = itemView.findViewById(R.id.item_quantity);
            Delivery_Address = itemView.findViewById(R.id.delivery_addr);
            Delivery_Time = itemView.findViewById(R.id.delivery_time);
            Delivery_Price = itemView.findViewById(R.id.delivery_price);
            GrandTotal = itemView.findViewById(R.id.grandtotal);
            Status = itemView.findViewById(R.id.status);

        }
    }

    public interface OnItemClickListener {
        void onCancelClick(int position);
    }
}
