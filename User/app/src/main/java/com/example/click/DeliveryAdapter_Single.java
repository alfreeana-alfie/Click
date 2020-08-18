package com.example.click;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter_Single extends RecyclerView.Adapter<DeliveryAdapter_Single.ViewHolder> {

    List<Delivery> arrayList;

    public DeliveryAdapter_Single(List<Delivery> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_info_listview,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Delivery delivery = arrayList.get(position);

        holder.division.setText(delivery.getDivision());
        holder.price.setText("MYR "  + delivery.getPrice());
        holder.days.setText(delivery.getDays() + " Days");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView division, price, days;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            division = itemView.findViewById(R.id.text_division);
            price = itemView.findViewById(R.id.text_price);
            days = itemView.findViewById(R.id.text_days);
        }
    }
}
