package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Item_Grid_Search extends BaseAdapter implements Filterable {

    private List<Item> itemList;
    private List<Item> itemListFull;
    private Context context;

    public Adapter_Item_Grid_Search(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.itemListFull = itemList;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.fragment_view_item_listview, null);
        Item item = itemListFull.get(position);

        final ItemViewHolder holder = new ItemViewHolder(convertView);
        holder.TV_addetail.setText(item.getAd_detail());
        holder.TV_price.setText(item.getPrice());
        holder.TV_item_location.setText(item.getItem_location());

        Picasso.get().load(item.getPhoto()).into(holder.img_item);

        return convertView;
    }

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
                    List<Item> resultData = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getAd_detail().contains(strSearch)) {
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
                itemListFull = (List<Item>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;

        public ItemViewHolder(View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.img_item);
            TV_addetail = itemView.findViewById(R.id.ad_details_item);
            TV_price = itemView.findViewById(R.id.price_item);
            TV_item_location = itemView.findViewById(R.id.item_location_item);
        }
    }
}
