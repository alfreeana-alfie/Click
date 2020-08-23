package com.example.click.adapter;

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

import com.example.click.Order;
import com.example.click.R;
import com.example.click.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Item_Single_Adapter extends BaseAdapter{

    List<Item_All_Details> itemListFull, itemListFull02;
    private Context context;
    private OnItemClickListener mListerner;

    public Item_Single_Adapter(List<Item_All_Details> itemList, Context context) {
        this.itemListFull = itemList;
        this.context = context;
        itemListFull02 = new ArrayList<>();
        if (itemListFull != null) {
            this.itemListFull02.addAll(itemListFull);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public interface OnItemClickListener {
        void onViewClick(int position);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.view_item_single_listview, null);
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
                    mListerner.onViewClick(position);
                }
            }
        });
        return convertView;
    }
}
