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

import com.example.click.item.Item_All_Details;
import com.example.click.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Item_Cart_Adapter extends RecyclerView.Adapter<Item_Cart_Adapter.ViewHolder> {

    Context context;
    int mQuantity = 1;
    private List<Item_All_Details> item_all_details;
    private OnItemClickListener mListerner;

    public Item_Cart_Adapter(Context context, List<Item_All_Details> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Item_All_Details itemAllDetails = item_all_details.get(position);

        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

//        final Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * 2;
        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText("MYR" + price);
        holder.SubTotal.setText("MYR" + price);
        holder.Quantity.setText(String.valueOf(mQuantity));
        if(mQuantity == 0){
            mQuantity = 1;
            Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity;
            holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
            holder.Quantity.setText(String.valueOf(mQuantity));
        }

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity = mQuantity + 1;
                Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity;
                holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                holder.Quantity.setText(String.valueOf(mQuantity));
            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuantity == 0){
                    mQuantity += 1;
                    Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity;
                    holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                    holder.Quantity.setText(String.valueOf(mQuantity));
                }else{
                    mQuantity = mQuantity - 1;
                    Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity;
                    holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                    holder.Quantity.setText(String.valueOf(mQuantity));
                }
            }
        });

        holder.DeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onDeleteClick(position);
                }
            }
        });


        Picasso.get().load(photo_URL).into(holder.ItemImageView);
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, UnitPrice, SubTotal, Quantity;
        Button decrease, increase;
        ImageView DeleteCart;

        public ViewHolder(View view) {
            super(view);

            ItemImageView = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            SubTotal = view.findViewById(R.id.subtotal_display);
            Quantity = view.findViewById(R.id.integer_number);
            DeleteCart = view.findViewById(R.id.delete_cart_item);

            decrease = view.findViewById(R.id.decrease);
            increase = view.findViewById(R.id.increase);


        }
    }
}
