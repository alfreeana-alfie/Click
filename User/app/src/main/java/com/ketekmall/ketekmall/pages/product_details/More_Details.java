package com.ketekmall.ketekmall.pages.product_details;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ketekmall.ketekmall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;

public class More_Details extends AppCompatActivity {

    TextView Stock, Brand, Inner, Ship_From, Description;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);

        ToolbarSetting();

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

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(More_Details.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(More_Details.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(More_Details.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Stock.setText(stock);
        Brand.setText(brand);
        Inner.setText(inner);
        Ship_From.setText(ship_detail);
        Description.setText(desc);

    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.more_details));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent1 = new Intent(More_Details.this, View_Product.class);
//
//                final Intent intent4 = getIntent();
//                String id1 = intent4.getStringExtra("id");
//                String stock = intent4.getStringExtra("stock");
//                String brand = intent4.getStringExtra("brand_material");
//                String inner = intent4.getStringExtra("inner_material");
//                String desc = intent4.getStringExtra("description");
//                String division = intent4.getStringExtra("division");
//                String district = intent4.getStringExtra("district");
//
//                String userid1 = intent4.getStringExtra("user_id");
//                String strMain_category1 = intent4.getStringExtra("main_category");
//                String strSub_category1 = intent4.getStringExtra("sub_category");
//                String ad_detail1 = intent4.getStringExtra("ad_detail");
//                String strPrice1 = intent4.getStringExtra("price");
//                String division1 = intent4.getStringExtra("division");
//                String district1 = intent4.getStringExtra("district");
//                String photo1 = intent4.getStringExtra("photo");
//                String item_id = intent4.getStringExtra("item_id");
//
//                intent1.putExtra("item_id", item_id);
//                intent1.putExtra("id", id1);
//                intent1.putExtra("user_id", userid1);
//                intent1.putExtra("main_category", strMain_category1);
//                intent1.putExtra("sub_category", strSub_category1);
//                intent1.putExtra("ad_detail", ad_detail1);
//                intent1.putExtra("price", strPrice1);
//                intent1.putExtra("division", division1);
//                intent1.putExtra("district", district1);
//                intent1.putExtra("photo", photo1);
//
//
//
//                intent1.putExtra("id", id1);
//                intent1.putExtra("stock", stock);
//                intent1.putExtra("brand_material", brand);
//                intent1.putExtra("inner_material", inner);
//                intent1.putExtra("description", desc);
//                intent1.putExtra("division", division);
//                intent1.putExtra("district", district);
//
//                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        Intent intent1 = new Intent(More_Details.this, View_Product.class);
//
//        final Intent intent4 = getIntent();
//        String id1 = intent4.getStringExtra("id");
//        String stock = intent4.getStringExtra("stock");
//        String brand = intent4.getStringExtra("brand_material");
//        String inner = intent4.getStringExtra("inner_material");
//        String desc = intent4.getStringExtra("description");
//        String division = intent4.getStringExtra("division");
//        String district = intent4.getStringExtra("district");
//
//        String userid1 = intent4.getStringExtra("user_id");
//        String strMain_category1 = intent4.getStringExtra("main_category");
//        String strSub_category1 = intent4.getStringExtra("sub_category");
//        String ad_detail1 = intent4.getStringExtra("ad_detail");
//        String strPrice1 = intent4.getStringExtra("price");
//        String division1 = intent4.getStringExtra("division");
//        String district1 = intent4.getStringExtra("district");
//        String photo1 = intent4.getStringExtra("photo");
//        String item_id = intent4.getStringExtra("item_id");
//
//        intent1.putExtra("item_id", item_id);
//        intent1.putExtra("id", id1);
//        intent1.putExtra("user_id", userid1);
//        intent1.putExtra("main_category", strMain_category1);
//        intent1.putExtra("sub_category", strSub_category1);
//        intent1.putExtra("ad_detail", ad_detail1);
//        intent1.putExtra("price", strPrice1);
//        intent1.putExtra("division", division1);
//        intent1.putExtra("district", district1);
//        intent1.putExtra("photo", photo1);
//
//
//
//        intent1.putExtra("id", id1);
//        intent1.putExtra("stock", stock);
//        intent1.putExtra("brand_material", brand);
//        intent1.putExtra("inner_material", inner);
//        intent1.putExtra("description", desc);
//        intent1.putExtra("division", division);
//        intent1.putExtra("district", district);
//
//        startActivity(intent1);
    }
}
