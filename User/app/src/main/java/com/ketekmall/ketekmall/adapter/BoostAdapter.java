package com.ketekmall.ketekmall.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BoostAdapter extends RecyclerView.Adapter<BoostAdapter.ViewHolder> {

    Context context;
    private List<Item_All_Details> item_all_details;
    private OnItemClickListener mListerner;

    public BoostAdapter(Context context, List<Item_All_Details> item_all_detailsList) {
        this.context = context;
        this.item_all_details = item_all_detailsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_boostad, parent, false);
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
        holder.Price.setText("MYR" + ItemPrice);


        if (itemAllDetails.getShocking().equals("1")) {
            String newString = "<font color='#3DDC84'>" + "Approved" + "</font>";
            holder.Shocking.setText(Html.fromHtml(newString));
        }else if (itemAllDetails.getShocking().equals("2")){
            String newString = "<font color='#FF3333'>" + "Rejected" + "</font>";
            holder.Shocking.setText(Html.fromHtml(newString));
        } else {
            String newString = "<font color='#FF3333'>" + "Pending Request" + "</font>";
            holder.Shocking.setText(Html.fromHtml(newString));
        }

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListerner != null) {
                    mListerner.onCancelClick(position);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_all_details.size();
    }

    public interface OnItemClickListener {
        void onCancelClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImageView;
        TextView AdDetail, Price, Shocking;
        Button btn_cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemImageView = itemView.findViewById(R.id.item_image);
            AdDetail = itemView.findViewById(R.id.item_name);
            Shocking = itemView.findViewById(R.id.item_shocking);

            Price = itemView.findViewById(R.id.item_price);

            btn_cancel = itemView.findViewById(R.id.btn_cancel);

        }
    }
}
