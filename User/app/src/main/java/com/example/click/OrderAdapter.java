package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Item_All_Details> itemList;
    private List<Item_All_Details> itemListFull;
    private OnItemClickListener mListerner;

    public OrderAdapter(Context context, List<Item_All_Details> itemList) {
        this.context = context;
        this.itemList = itemList;
        itemListFull = new ArrayList<>(itemList);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.order_listview, null);
        Item_All_Details item = itemList.get(position);

        TextView TV_addetail;
        Button view_item, delete_fav_item;

        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        view_item = convertView.findViewById(R.id.view_item);
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onAcceptClick(position);
                }
            }
        });
        delete_fav_item = convertView.findViewById(R.id.delete_fav_item);
        delete_fav_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onRejectClick(position);
                }
            }
        });

        TV_addetail.setText(item.getAd_detail());
        return convertView;
    }

    public interface OnItemClickListener {
        void onAcceptClick(int position);

        void onRejectClick(int position);
    }
}
