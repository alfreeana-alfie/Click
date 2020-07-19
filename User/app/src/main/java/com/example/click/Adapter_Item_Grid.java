package com.example.click;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Item_Grid extends BaseAdapter {

    private Context context;
    private List<ItemFull> itemList;
    private List<ItemFull> itemListFull;
    private OnItemClickListener mListerner;

    public interface OnItemClickListener{
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListerner = listener;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItemFull> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (ItemFull item : itemListFull) {
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

    public Adapter_Item_Grid(Context context, List<ItemFull> itemList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.fragment_edit_item_listview, null);
        ItemFull item = itemList.get(position);

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button edit_item;

        img_item = convertView.findViewById(R.id.img_item);
        TV_addetail = convertView.findViewById(R.id.ad_details_item);
        TV_price = convertView.findViewById(R.id.price_item);
        TV_item_location = convertView.findViewById(R.id.item_location_item);
        edit_item = convertView.findViewById(R.id.edit_item);
        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListerner != null){
                    Log.d("GRID", "SUCCESS");
                    mListerner.onEditClick(position);
                }
            }
        });

//        final ItemViewHolder holder = new ItemViewHolder(convertView);
        TV_addetail.setText(item.getAd_detail());
        TV_price.setText(item.getPrice());
        TV_item_location.setText(item.getItem_location());

        Picasso.get().load(item.getPhoto()).into(img_item);

        return convertView;
    }

/*    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img_item;
        TextView TV_addetail, TV_price, TV_item_location;
        Button edit_item;

        public ItemViewHolder(View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.img_item);
            TV_addetail = itemView.findViewById(R.id.ad_details_item);
            TV_price = itemView.findViewById(R.id.price_item);
            TV_item_location = itemView.findViewById(R.id.item_location_item);
            edit_item = itemView.findViewById(R.id.edit_item);
        }
    }*/
}
