package com.ketekmall.ketekmall.pages.seller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.BoostAdapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BoostAd extends AppCompatActivity {

    private static String URL_READ_BOOST = "https://ketekmall.com/ketekmall/read_products_boost.php";
    private static String URL_EDIT_BOOST = "https://ketekmall.com/ketekmall/edit_boost_ad_cancel.php";

    RecyclerView recyclerView;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;

    List<Item_All_Details> item_all_details;
    BoostAdapter boostAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boost_ad);
        Declare();
        ToolbarSettings();

        getSession();
        getBoost();
    }

    private void getBoost(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_BOOST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                final JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String id = object.getString("id").trim();
                                        String seller_id = object.getString("user_id").trim();
                                        String main_category = object.getString("main_category").trim();
                                        String sub_category = object.getString("sub_category").trim();
                                        String ad_detail = object.getString("ad_detail").trim();

                                        String price = object.getString("price").trim();
                                        String division = object.getString("division");
                                        String district = object.getString("district");
                                        String image_item = object.getString("photo");
                                        String shocking = object.getString("shocking_sale");

                                        Item_All_Details item = new Item_All_Details(
                                                id,
                                                seller_id,
                                                main_category,
                                                sub_category,
                                                ad_detail,
                                                price,
                                                division,
                                                district,
                                                image_item);
                                        item.setShocking(shocking);
                                        item_all_details.add(item);
                                    }
                                    boostAdapter = new BoostAdapter(BoostAd.this, item_all_details);
                                    recyclerView.setAdapter(boostAdapter);
                                    boostAdapter.setOnItemClickListener(new BoostAdapter.OnItemClickListener() {
                                        @Override
                                        public void onCancelClick(int position) {
                                            final Item_All_Details item = item_all_details.get(position);
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_EDIT_BOOST,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            if(response == null){
                                                                Log.e("onResponse", "Return NULL");
                                                            }else {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    String success = jsonObject.getString("success");

                                                                    if (success.equals("1")) {
                                                                        Toast.makeText(BoostAd.this, R.string.success_add, Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(BoostAd.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
//                                                                    Toast.makeText(BoostAd.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            try {
                                                                if (error instanceof TimeoutError) {
                                                                    //Time out error
                                                                    System.out.println("" + error);
                                                                } else if (error instanceof NoConnectionError) {
                                                                    //net work error
                                                                    System.out.println("" + error);
                                                                } else if (error instanceof AuthFailureError) {
                                                                    //error
                                                                    System.out.println("" + error);
                                                                } else if (error instanceof ServerError) {
                                                                    //Erroor
                                                                    System.out.println("" + error);
                                                                } else if (error instanceof NetworkError) {
                                                                    //Error
                                                                    System.out.println("" + error);
                                                                } else if (error instanceof ParseError) {
                                                                    //Error
                                                                    System.out.println("" + error);
                                                                } else {
                                                                    //Error
                                                                    System.out.println("" + error);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("id", item.getId());
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(BoostAd.this);
                                            requestQueue.add(stringRequest1);
                                        }
                                    });
                                } else {
                                    Toast.makeText(BoostAd.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {
                                //Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
                                //net work error
                                System.out.println("" + error);
                            } else if (error instanceof AuthFailureError) {
                                //error
                                System.out.println("" + error);
                            } else if (error instanceof ServerError) {
                                //Erroor
                                System.out.println("" + error);
                            } else if (error instanceof NetworkError) {
                                //Error
                                System.out.println("" + error);
                            } else if (error instanceof ParseError) {
                                //Error
                                System.out.println("" + error);
                            } else {
                                //Error
                                System.out.println("" + error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Declare(){
        item_all_details = new ArrayList<>();
        recyclerView = findViewById(R.id.boost_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(BoostAd.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(BoostAd.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(BoostAd.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.boost_ad));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
