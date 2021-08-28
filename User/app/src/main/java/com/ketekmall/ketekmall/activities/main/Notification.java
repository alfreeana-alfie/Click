package com.ketekmall.ketekmall.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ketekmall.ketekmall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.activities.chats.InboxNotification;
import com.ketekmall.ketekmall.activities.list.ProductList;
import com.ketekmall.ketekmall.activities.list.MyBuyingList;

import static com.ketekmall.ketekmall.configs.Link.*;

public class Notification extends AppCompatActivity {

    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readcart_single.php";

    String URL_READ_CATEGORY_MAIN = "https://ketekmall.com/ketekmall/category/";
    String URL_READ_CATEGORY_SEARCH_MAIN = "https://ketekmall.com/ketekmall/search/";
    String URL_READ_CATEGORY_FILTER_DISTRICT_MAIN = "https://ketekmall.com/ketekmall/filter_district/";
    String URL_READ_CATEGORY_FILTER_DIVISION_MAIN = "https://ketekmall.com/ketekmall/filter_division/";
    String URL_READ_CATEGORY_FILTER_SEARCH_MAIN = "https://ketekmall.com/ketekmall/filter_search_division/";

    String[] CATEGORY_LIST = {
            "read_cake.php",
            "read_process.php",
            "read_handicraft.php",
            "read_retail.php",
            "read_agri.php",
            "read_service.php",
            "read_health.php",
            "read_home.php",
            "read_fashion.php",
            "read_pepper.php",
            "readall.php",
            "readall_sold.php",
            "readall_shocking.php",
            "readall.php",
            "read_pickup.php"};

    LinearLayout promotion_layout, social_layout, update_layout;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        ToolbarSetting();

        promotion_layout = findViewById(R.id.promotion_layout);
        social_layout = findViewById(R.id.social_layout);
        update_layout = findViewById(R.id.updates_layout);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_noti);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Notification.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Notification.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Notification.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        promotion_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Notification.this, ProductList.class);
                intent1.putExtra("GET_CATEGORY", GET_CATEGORY + CATEGORY_LIST[12]);
                intent1.putExtra("ADD_TO_FAVOURITE", ADD_TO_FAVOURITE);
                intent1.putExtra("ADD_TO_CART", ADD_TO_CART);
                intent1.putExtra("GET_SEARCH_CATEGORY", GET_SEARCH_CATEGORY + CATEGORY_LIST[12]);
                intent1.putExtra("GET_FILTER_DISTRICT", GET_FILTER_DISTRICT + CATEGORY_LIST[12]);
                intent1.putExtra("GET_FILTER_DIVISION", GET_FILTER_DIVISION + CATEGORY_LIST[12]);
                intent1.putExtra("GET_SEARCH_FILTER_CATEGORY", GET_SEARCH_FILTER_CATEGORY + CATEGORY_LIST[12]);
                intent1.putExtra("GET_SINGLE_CART_ITEM", GET_SINGLE_CART_ITEM);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        social_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Notification.this, InboxNotification.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        update_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Notification.this, MyBuyingList.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(Notification.this, Home.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent4 = new Intent(Notification.this, Home.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent4);
        finish();
    }
}
