package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyLikes_Adapter extends BaseAdapter {

    private Context context;
    private List<Item_All_Details> itemList;
    private OnItemClickListener mListerner;

    public MyLikes_Adapter(List<Item_All_Details> itemList, Context context) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.cardview_mylikes, null);
        Item_All_Details item = itemList.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button view_item, delete_fav_item;

        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
        view_item = convertView.findViewById(R.id.view_item);
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onViewClick(position);
                }
            }
        });
        delete_fav_item = convertView.findViewById(R.id.delete_fav_item);
        delete_fav_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onDeleteClick(position);
                }
            }
        });

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);

        return convertView;
    }

    public interface OnItemClickListener {
        void onViewClick(int position);

        void onDeleteClick(int position);
    }
}
