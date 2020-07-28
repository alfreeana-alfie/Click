package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.click.helper.Item_All_Details;
import com.example.click.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Item_Adapter extends BaseAdapter {

    private Context context;
    private List<Item_All_Details> itemList;
    private List<Item_All_Details> itemListFull;
    private OnItemClickListener mListerner;
    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item_All_Details> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (Item_All_Details item : itemListFull) {
                    if (item.getAd_detail().toLowerCase().contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemListFull.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public Item_Adapter(Context context, List<Item_All_Details> itemList) {
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

    public Filter getFilter() {
        return itemFilter;
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
        convertView = inflater.inflate(R.layout.fragment_edit_item_listview, null);
        Item_All_Details item = itemList.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button edit_item, delete_item;

        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
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

        TV_addetail.setText(item.getAd_detail());
        TV_price.setText("MYR" + item.getPrice());
        TV_item_location.setText(item.getDistrict());

        Picasso.get().load(item.getPhoto()).into(img_item);

        return convertView;
    }

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }
}
