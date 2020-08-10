package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.click.Order;
import com.example.click.R;
import com.example.click.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Buyer_OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> itemList;
    private List<Order> itemListFull;
    private OnItemClickListener mListerner;

    public Buyer_OrderAdapter(Context context, List<Order> itemList) {
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
        convertView = inflater.inflate(R.layout.buyer_order_listview, null);
        Order item = itemList.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button Cancel;

        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);

        Cancel = convertView.findViewById(R.id.view_item);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onCancelClick(position);
                }
            }
        });

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);
        TV_addetail.setText(item.getAd_detail());
        return convertView;
    }

    public interface OnItemClickListener {
        void onCancelClick(int position);
    }
}
