package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class Item_Adapter_Main extends BaseAdapter {

    List<Item_All_Details> itemListFull, itemListFull02;
    private OnItemClickListener mListerner;

    public Item_Adapter_Main(List<Item_All_Details> itemList) {
        this.itemListFull = itemList;
        itemListFull02 = new ArrayList<>();
        if (itemListFull != null) {
            this.itemListFull02.addAll(itemListFull);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @Override
    public int getCount() {
        return itemListFull.size();
    }

    public void sortArrayHighest() {
        Collections.sort(itemListFull, new Comparator<Item_All_Details>() {
            @Override
            public int compare(Item_All_Details o1, Item_All_Details o2) {
                return Double.compare(Double.parseDouble(o2.getSold()), Double.parseDouble(o1.getSold()));
            }
        });
        notifyDataSetChanged();

    }

    @Override
    public Object getItem(int position) {
        return itemListFull.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemListFull.indexOf(getItem(position));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.cardview_hot_shocking_sameshop, null);
        Item_All_Details item = itemListFull.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_Price;
        Button btn_view;
        RatingBar ratingBar;


        ratingBar = convertView.findViewById(R.id.ratingBar);
        img_item = convertView.findViewById(R.id.image_item);
        TV_addetail = convertView.findViewById(R.id.item_name);
        TV_Price = convertView.findViewById(R.id.item_price);
        btn_view = convertView.findViewById(R.id.btn_view);

        TV_addetail.setText(item.getAd_detail());
        TV_Price.setText(item.getPrice());
        ratingBar.setRating(Float.parseFloat(item.getRating()));

        Picasso.get().load(item.getPhoto()).into(img_item);

        btn_view.setOnClickListener(new View.OnClickListener() {
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

        return convertView;
    }

    public interface OnItemClickListener {
        void onViewClick(int position);

        void onAddtoCartClick(int position);
    }
}
