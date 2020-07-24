package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static com.example.click.Activity_All_View.AD_DETAIL;
import static com.example.click.Activity_All_View.ITEM_LOCATION;
import static com.example.click.Activity_All_View.PHOTO;
import static com.example.click.Activity_All_View.PRICE;

public class Activity_View_Item extends AppCompatActivity {

    private ImageView img_item;
    private TextView ad_detail_item, price_item, item_location_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Declare();

        Intent intent = getIntent();
        final String ad_detail = intent.getStringExtra(AD_DETAIL);
        final String price = intent.getStringExtra(PRICE);
        final String item_location = intent.getStringExtra(ITEM_LOCATION);
        final String photo = intent.getStringExtra(PHOTO);

        ad_detail_item.setText(ad_detail);
        price_item.setText("MYR" + price);
        item_location_item.setText(item_location);

        Picasso.get().load(photo).into(img_item);
    }

    private void Declare() {
        img_item = findViewById(R.id.img_item);
        ad_detail_item = findViewById(R.id.ad_details_item);
        price_item = findViewById(R.id.price_item);
        item_location_item = findViewById(R.id.item_location_item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Activity_View_Item.this, Activity_All_View.class);
        startActivity(intent);
    }
}
