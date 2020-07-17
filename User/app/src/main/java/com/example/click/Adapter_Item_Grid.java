package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Item_Grid extends BaseAdapter {

    private Context context;
    private List<Item> itemList;
    private List<Item> itemListFull;
    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (Item item : itemListFull) {
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

    public Adapter_Item_Grid(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        itemListFull = new ArrayList<>(itemList);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.fragment_view_item_listview, null);
        Item item = itemList.get(position);

        final ItemViewHolder holder = new ItemViewHolder(convertView);
        holder.TV_addetail.setText(item.getAd_detail());
        holder.TV_price.setText(item.getPrice());
        holder.TV_item_location.setText(item.getItem_location());

        Picasso.get().load(item.getPhoto()).into(holder.img_item);

        return convertView;
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
