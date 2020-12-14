package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Item_ByCategory_Adapter extends BaseAdapter {

    List<Item_All_Details> itemListFull, itemListFull02;
    private OnItemClickListener mListerner;

    public Item_ByCategory_Adapter(List<Item_All_Details> itemList, Context context) {
        this.itemListFull = itemList;
        itemListFull02 = new ArrayList<>();
        if (itemListFull != null) {
            this.itemListFull02.addAll(itemListFull);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public void sortArrayLowest() {
        Collections.sort(itemListFull, new Comparator<Item_All_Details>() {
            @Override
            public int compare(Item_All_Details o1, Item_All_Details o2) {
                return Double.compare(Double.parseDouble(o1.getPrice()), Double.parseDouble(o2.getPrice()));
            }
        });
        notifyDataSetChanged();

    }

    public void sortArrayHighest() {
        Collections.sort(itemListFull, new Comparator<Item_All_Details>() {
            @Override
            public int compare(Item_All_Details o1, Item_All_Details o2) {
                return Double.compare(Double.parseDouble(o2.getPrice()), Double.parseDouble(o1.getPrice()));
            }
        });
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return itemListFull.size();
    }

    @Override
    public Object getItem(int position) {
        return itemListFull.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemListFull.indexOf(getItem(position));
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.cardview_category, null);
        Item_All_Details item = itemListFull.get(position);

        ImageButton fav_item, add_to_cart;
        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button view_item;
        RatingBar ratingBar;

        ratingBar = convertView.findViewById(R.id.ratingBar);
        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
        view_item = convertView.findViewById(R.id.view_item);
        fav_item = convertView.findViewById(R.id.fav_item);
        add_to_cart = convertView.findViewById(R.id.add_to_cart_item);


        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onAddtoCartClick(position);
                }
            }
        });

        fav_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onAddtoFavClick(position);
                }
            }
        });
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onAddtoCartClick(position);
                }
            }
        });

        img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onViewClick(position);
                }
            }
        });

        float flo = 0.0F;
        flo = Float.parseFloat(item.getRating());
        ratingBar.setRating(flo);

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);
        return convertView;
    }

    public interface OnItemClickListener {
        void onViewClick(int position);

        void onAddtoFavClick(int position);

        void onAddtoCartClick(int position);
    }
}
