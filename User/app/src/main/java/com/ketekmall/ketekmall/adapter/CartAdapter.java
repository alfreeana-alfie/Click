package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    int mQuantity;
    private List<Item_All_Details> item_all_details;
    private OnItemClickListener mListerner;

    public CartAdapter(Context context, List<Item_All_Details> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public void sortArrayHighest() {
        Collections.sort(item_all_details, new Comparator<Item_All_Details>() {
            @Override
            public int compare(Item_All_Details o1, Item_All_Details o2) {
                return Double.compare(Double.parseDouble(o2.getId()), Double.parseDouble(o1.getId()));
            }
        });
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cart, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Item_All_Details itemAllDetails = item_all_details.get(position);

        String ad_detail = itemAllDetails.getAd_detail();
        String price = itemAllDetails.getPrice();
        String photo_URL = itemAllDetails.getPhoto();

        holder.AdDetail.setText(ad_detail);
        holder.UnitPrice.setText("MYR" + price);
        holder.SubTotal.setText("MYR" + price);
        holder.Quantity.setText(itemAllDetails.getQuantity());

        holder.checkBox.setChecked(false);

        mQuantity = Integer.parseInt(itemAllDetails.getQuantity());
        Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * Integer.parseInt(itemAllDetails.getQuantity());
        holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onAddClick(position);
                    int mQuantity2 = Integer.parseInt(holder.Quantity.getText().toString());
                    mQuantity2++;
                    itemAllDetails.setQuantity(String.valueOf(mQuantity2));
                    Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity2;
                    holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                    holder.Quantity.setText(String.valueOf(mQuantity2));

                    if (mQuantity2 == Integer.parseInt(itemAllDetails.getMax_order())) {
                        holder.increase.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    int mQuantity2 = Integer.parseInt(holder.Quantity.getText().toString());
                    mListerner.onMinusClick(position);

                    if(mQuantity2 == 1){
                        itemAllDetails.setQuantity(String.valueOf(mQuantity2));
                        Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity2;
                        holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                        holder.Quantity.setText(String.valueOf(mQuantity2));
                    }else{
                        mQuantity2--;
                        itemAllDetails.setQuantity(String.valueOf(mQuantity2));
                        Double priceint = Double.parseDouble(itemAllDetails.getPrice()) * mQuantity2;
                        holder.SubTotal.setText("MYR" + String.format("%.2f", priceint));
                        holder.Quantity.setText(String.valueOf(mQuantity2));
                    }


                    if (mQuantity2 != Integer.parseInt(itemAllDetails.getMax_order())) {
                        holder.increase.setVisibility(View.VISIBLE);
                    }

                    if(mQuantity2 == 0 ){
                        if (mListerner != null) {
                            mListerner.onDeleteClick(position);
                        }
                        holder.Quantity.setText("1");
                    }
                }
                if (Integer.parseInt(itemAllDetails.getQuantity()) == 0) {
                    if (mListerner != null) {
                        mListerner.onDeleteClick(position);
                    }
                    holder.Quantity.setText("1");
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

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    mListerner.onClick(position);
                    holder.increase.setVisibility(View.INVISIBLE);
                    holder.decrease.setVisibility(View.INVISIBLE);
                } else {
                    mListerner.onDeleteCart_Temp(position);
                    holder.increase.setVisibility(View.VISIBLE);
                    holder.decrease.setVisibility(View.VISIBLE);

                    if (mQuantity >= Integer.parseInt(itemAllDetails.getMax_order())) {
                        holder.increase.setVisibility(View.INVISIBLE);
                    }
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

        void onDeleteCart_Temp(int position);

        void onClick(int position);

        void onAddClick(int position);

        void onMinusClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, UnitPrice, SubTotal, Quantity;
        Button decrease, increase;
        ImageView DeleteCart;

        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);

            ItemImageView = view.findViewById(R.id.item_image);
            AdDetail = view.findViewById(R.id.ad_detail_display);
            UnitPrice = view.findViewById(R.id.unit_price_display);
            SubTotal = view.findViewById(R.id.subtotal_display);
            Quantity = view.findViewById(R.id.integer_number);
            DeleteCart = view.findViewById(R.id.delete_cart_item);

            checkBox = view.findViewById(R.id.checkBox);

            decrease = view.findViewById(R.id.decrease);
            increase = view.findViewById(R.id.increase);
        }
    }
}
