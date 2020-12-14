package com.ketekmall.ketekmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.List;

public class MyProducts_Adapter extends BaseAdapter{

    private Context context;
    private List<Item_All_Details> itemListFull;
    private OnItemClickListener mListerner;

    public MyProducts_Adapter(List<Item_All_Details> itemList, Context context) {
        this.itemListFull = itemList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @Override
    public int getCount() {
        return itemListFull.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.cardview_myproducts, null);
        Item_All_Details item = itemListFull.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location, Pending_approved, Is_approved, Is_Reject;
        Button edit_item, delete_item, boost_ad;
        RatingBar ratingBar;

        ratingBar = convertView.findViewById(R.id.ratingBar);
        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
        Pending_approved = convertView.findViewById(R.id.pending_approved);
        Is_approved = convertView.findViewById(R.id.is_approved);
        Is_Reject = convertView.findViewById(R.id.is_reject);
        boost_ad = convertView.findViewById(R.id.btn_BoostAds);

        delete_item = convertView.findViewById(R.id.delete_item);
        edit_item = convertView.findViewById(R.id.edit_item);

        if (item.getDelivery_status().equals("0")) {
            Pending_approved.setVisibility(View.VISIBLE);
            Is_approved.setVisibility(View.GONE);
            boost_ad.setVisibility(View.GONE);
            Is_Reject.setVisibility(View.GONE);
        } else if(item.getDelivery_status().equals("3")){
            delete_item.setVisibility(View.GONE);
            edit_item.setVisibility(View.GONE);
            Pending_approved.setVisibility(View.GONE);
            Is_approved.setVisibility(View.GONE);
            boost_ad.setVisibility(View.GONE);
            Is_Reject.setVisibility(View.VISIBLE);
        } else {
            Pending_approved.setVisibility(View.GONE);
            Is_approved.setVisibility(View.VISIBLE);
            boost_ad.setVisibility(View.VISIBLE);
            Is_Reject.setVisibility(View.GONE);
        }

        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onEditClick(position);
                }
            }
        });
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onDeleteClick(position);
                }
            }
        });

        boost_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onBoostClick(position);
                }
            }
        });
        float flo;
        flo = Float.parseFloat(item.getRating());
        ratingBar.setRating(flo);

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);

        return convertView;
    }

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onBoostClick(int position);

        void onDeleteClick(int position);
    }
}
