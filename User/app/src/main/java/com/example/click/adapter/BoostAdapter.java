package com.example.click.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.R;
import com.example.click.Rating;
import com.example.click.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BoostAdapter extends RecyclerView.Adapter<BoostAdapter.ViewHolder> {

    Context context;
    int mQuantity;
    private List<Item_All_Details> item_all_details;
    private OnItemClickListener mListerner;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public BoostAdapter(Context context, List<Item_All_Details> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boostad_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Item_All_Details itemAllDetails = item_all_details.get(position);
        String addetail = itemAllDetails.getAd_detail();
        String ItemPrice = itemAllDetails.getPrice();
        String ItemImage = itemAllDetails.getPhoto();

        Picasso.get().load(ItemImage).into(holder.ItemImageView);
        holder.AdDetail.setText(addetail);
        holder.Price.setText(ItemPrice);

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onCancelClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, Price;
        Button btn_cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemImageView = itemView.findViewById(R.id.item_image);
            AdDetail = itemView.findViewById(R.id.ad_detail);

            Price = itemView.findViewById(R.id.item_price);

            btn_cancel = itemView.findViewById(R.id.btn_cancel);

        }
    }

    public interface OnItemClickListener {
        void onCancelClick(int position);
    }
}
