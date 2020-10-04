package com.ketekmall.ketekmall.adapter;

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

public class Item_Adapter extends BaseAdapter implements Filterable {

    List<Item_All_Details> itemListFull, itemListFull02;
    private Context context;
    private OnItemClickListener mListerner;

    public Item_Adapter(List<Item_All_Details> itemList, Context context) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.view_item_listview, null);
        Item_All_Details item = itemListFull.get(position);

        ImageButton fav_item, fav_item_filled, add_to_cart;
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
        fav_item_filled = convertView.findViewById(R.id.fav_item_filled);
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

//        fav_item_filled.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListerner != null) {
//                    mListerner.onAddtoFavClick(position);
//                }
//            }
//        });

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

        Float flo = 0.0F;
        flo = Float.parseFloat(item.getRating());
        ratingBar.setRating(flo);

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = itemListFull02.size();
                    filterResults.values = itemListFull02;
                } else {
                    String strSearch = constraint.toString().toLowerCase();
                    List<Item_All_Details> resultData = new ArrayList<>();
                    for (Item_All_Details item : itemListFull) {
                        String fulltext01 = item.getDivision().toLowerCase() + item.getAd_detail().toLowerCase() + item.getDistrict().toLowerCase();
                        String fulltext02 = item.getDivision().toLowerCase() + item.getDistrict().toLowerCase() + item.getAd_detail().toLowerCase();
                        if (fulltext01.toLowerCase().contains(strSearch)) {
                            resultData.add(item);
                        } else if (fulltext02.toLowerCase().contains(strSearch)) {
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

    public interface OnItemClickListener {
        void onViewClick(int position);

        void onAddtoFavClick(int position);

        void onAddtoCartClick(int position);
    }
}
