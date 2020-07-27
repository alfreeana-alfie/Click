package com.example.click;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.helper.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.ItemViewHolder> {

    private Context context;
    private List<Item> itemList;

    public Adapter_Item(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_view_item_listview, null);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.TV_addetail.setText(item.getAd_detail());
        holder.TV_price.setText(item.getPrice());
        holder.TV_item_location.setText(item.getItem_location());

        Picasso.get().load(item.getPhoto()).into(holder.img_item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
