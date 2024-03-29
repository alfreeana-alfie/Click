package com.ketekmall.ketekmall.adapter;

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

public class Item_UserAdapter extends BaseAdapter{

    private Context context;
    private List<Item_All_Details> itemList;
    private List<Item_All_Details> itemListFull;
    private OnItemClickListener mListerner;

    public Item_UserAdapter(List<Item_All_Details> itemList, Context context) {
        this.itemList = itemList;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.edit_item_listview, null);
        Item_All_Details item = itemListFull.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location, Delivery_Status;
        Button edit_item, delete_item, boost_ad;
        RatingBar ratingBar;

        ratingBar = convertView.findViewById(R.id.ratingBar);
        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
        Delivery_Status = convertView.findViewById(R.id.delivery_status);
        boost_ad = convertView.findViewById(R.id.btn_BoostAds);


        if (item.getDelivery_status().equals("0")) {
            Delivery_Status.setVisibility(View.VISIBLE);
            boost_ad.setVisibility(View.GONE);
        } else {
            Delivery_Status.setVisibility(View.GONE);
            boost_ad.setVisibility(View.VISIBLE);
        }

        edit_item = convertView.findViewById(R.id.edit_item);
        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onEditClick(position);
                }
            }
        });
        delete_item = convertView.findViewById(R.id.delete_item);
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
        Float flo = 0.0F;
        flo = Float.parseFloat(item.getRating());
        ratingBar.setRating(flo);

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);

        return convertView;
    }
/*

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = itemList.size();
                    filterResults.values = itemList;
                } else {
                    String strSearch = constraint.toString().toLowerCase();
                    String strSEARCH = constraint.toString().toUpperCase();
                    String str = constraint.toString();
                    List<Item_All_Details> resultData = new ArrayList<>();
                    for (Item_All_Details item : itemList) {
                        if (item.getAd_detail().toLowerCase().contains(strSearch)) {
                            resultData.add(item);
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemListFull = (List<Item_All_Details>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
*/

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onBoostClick(int position);

        void onDeleteClick(int position);
    }
}
