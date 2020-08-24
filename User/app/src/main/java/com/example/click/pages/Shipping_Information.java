package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.adapter.DeliveryAdapter_Single;
import com.example.click.data.Delivery;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shipping_Information extends AppCompatActivity {

    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single.php";

    RecyclerView recyclerView;
    DeliveryAdapter_Single adapter_item;
    List<Delivery> itemList;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_info);

        final Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String userid = intent.getStringExtra("user_id");
        String strMain_category = intent.getStringExtra("main_category");
        String strSub_category = intent.getStringExtra("sub_category");
        String ad_detail = intent.getStringExtra("ad_detail");
        String strPrice = intent.getStringExtra("price");
        String division = intent.getStringExtra("division");
        String district = intent.getStringExtra("district");
        String photo = intent.getStringExtra("photo");

        final String item_id = intent.getStringExtra("item_id");
        itemList = new ArrayList<>();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Shipping_Information.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(Shipping_Info.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Shipping_Information.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Shipping_Information.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Shipping_Information.this));

        ToolbarSetting();

        View_Shipping(item_id);
    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.shipping_information));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Shipping_Information.this, View_Product.class);

                final Intent intent4 = getIntent();
                String id1 = intent4.getStringExtra("id");
                String stock = intent4.getStringExtra("stock");
                String brand = intent4.getStringExtra("brand_material");
                String inner = intent4.getStringExtra("inner_material");
                String desc = intent4.getStringExtra("description");
                String division = intent4.getStringExtra("division");
                String district = intent4.getStringExtra("district");

                String userid1 = intent4.getStringExtra("user_id");
                String strMain_category1 = intent4.getStringExtra("main_category");
                String strSub_category1 = intent4.getStringExtra("sub_category");
                String ad_detail1 = intent4.getStringExtra("ad_detail");
                String strPrice1 = intent4.getStringExtra("price");
                String division1 = intent4.getStringExtra("division");
                String district1 = intent4.getStringExtra("district");
                String photo1 = intent4.getStringExtra("photo");
                String item_id = intent4.getStringExtra("item_id");

                intent1.putExtra("item_id", item_id);
                intent1.putExtra("id", id1);
                intent1.putExtra("user_id", userid1);
                intent1.putExtra("main_category", strMain_category1);
                intent1.putExtra("sub_category", strSub_category1);
                intent1.putExtra("ad_detail", ad_detail1);
                intent1.putExtra("price", strPrice1);
                intent1.putExtra("division", division1);
                intent1.putExtra("district", district1);
                intent1.putExtra("photo", photo1);



                intent1.putExtra("id", id1);
                intent1.putExtra("stock", stock);
                intent1.putExtra("brand_material", brand);
                intent1.putExtra("inner_material", inner);
                intent1.putExtra("description", desc);
                intent1.putExtra("division", division);
                intent1.putExtra("district", district);

                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    private void View_Shipping(final String item_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_DELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String user_id = object.getString("user_id").trim();
                                    final String division = object.getString("division").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String days = object.getString("days");
                                    final String item_id = object.getString("item_id");

                                    Delivery delivery = new Delivery(division, String.format("%.2f", price), days, item_id);
                                    itemList.add(delivery);
                                }
                                adapter_item = new DeliveryAdapter_Single(itemList);
                                recyclerView.setAdapter(adapter_item);

                            } else {
                                Toast.makeText(Shipping_Information.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", item_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Shipping_Information.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(Shipping_Information.this, View_Product.class);

        final Intent intent4 = getIntent();
        String id1 = intent4.getStringExtra("id");
        String stock = intent4.getStringExtra("stock");
        String brand = intent4.getStringExtra("brand_material");
        String inner = intent4.getStringExtra("inner_material");
        String desc = intent4.getStringExtra("description");
        String division = intent4.getStringExtra("division");
        String district = intent4.getStringExtra("district");

        String userid1 = intent4.getStringExtra("user_id");
        String strMain_category1 = intent4.getStringExtra("main_category");
        String strSub_category1 = intent4.getStringExtra("sub_category");
        String ad_detail1 = intent4.getStringExtra("ad_detail");
        String strPrice1 = intent4.getStringExtra("price");
        String division1 = intent4.getStringExtra("division");
        String district1 = intent4.getStringExtra("district");
        String photo1 = intent4.getStringExtra("photo");
        String item_id = intent4.getStringExtra("item_id");

        intent1.putExtra("item_id", item_id);
        intent1.putExtra("id", id1);
        intent1.putExtra("user_id", userid1);
        intent1.putExtra("main_category", strMain_category1);
        intent1.putExtra("sub_category", strSub_category1);
        intent1.putExtra("ad_detail", ad_detail1);
        intent1.putExtra("price", strPrice1);
        intent1.putExtra("division", division1);
        intent1.putExtra("district", district1);
        intent1.putExtra("photo", photo1);


        intent1.putExtra("id", id1);
        intent1.putExtra("stock", stock);
        intent1.putExtra("brand_material", brand);
        intent1.putExtra("inner_material", inner);
        intent1.putExtra("description", desc);
        intent1.putExtra("division", division);
        intent1.putExtra("district", district);

        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
    }
}
