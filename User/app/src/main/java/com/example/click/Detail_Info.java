package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.StorageReference;

public class Detail_Info extends AppCompatActivity {

    TextView Stock, Brand, Inner, Ship_From, Description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);

        Intent intent = getIntent();
        String division = intent.getStringExtra("division");
        String district = intent.getStringExtra("district");
        String brand = intent.getStringExtra("brand_material");
        String inner = intent.getStringExtra("inner_material");
        String stock = intent.getStringExtra("stock");
        String desc = intent.getStringExtra("description");

        String ship_detail = division + "," + district;

        Stock = findViewById(R.id.stock_text);
        Brand = findViewById(R.id.brand_text);
        Inner = findViewById(R.id.inner_text);
        Ship_From = findViewById(R.id.ship_text);
        Description = findViewById(R.id.desc_text);

        Stock.setText(stock);
        Brand.setText(brand);
        Inner.setText(inner);
        Ship_From.setText(ship_detail);
        Description.setText(desc);

    }
}
