package com.ketekmall.ketekmall.pages.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ketekmall.ketekmall.adapter.MyIncome_Adapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.OrderDone;
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

public class MyIncome extends AppCompatActivity {
    private static String URL_READ_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer_done_profile.php";
    private static String URL_READ_ORDERTWO = "https://ketekmall.com/ketekmall/read_order_two.php";
    TextView sold;

    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;
    RecyclerView recyclerView;

    MyIncome_Adapter orderAdapter;
    List<OrderDone> orderList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myincome);
        Declare();
        getSession();
        ToolbarSettings();
        getIncome();

        Approval_List();
    }

    private void getIncome() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_READ_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            Double grantotal = 0.00;
                            Double grantotal2 = 0.00;
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String seller_division = object.getString("seller_division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String seller_id = object.getString("seller_id");
                                    final String item_id = object.getString("item_id");
                                    final String quantity = object.getString("quantity");
                                    final Double delivery_price = Double.valueOf(object.getString("delivery_price"));

                                    grantotal += (price * Integer.parseInt(quantity) + delivery_price);
                                    sold.setText(String.format("%.2f", grantotal));

                                    final Item_All_Details item = new Item_All_Details(id,seller_id, main_category, sub_category,ad_detail, String.format("%.2f", price), division, district, image_item);
                                    item.setQuantity(quantity);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                    if (error instanceof TimeoutError) {
                        //Time out error

                    }else if(error instanceof NoConnectionError){
                        //net work error

                    } else if (error instanceof AuthFailureError) {
                        //error

                    } else if (error instanceof ServerError) {
                        //Erroor
                    } else if (error instanceof NetworkError) {
                        //Error

                    } else if (error instanceof ParseError) {
                        //Error

                    }else{
                        //Error
                    }
                    //End


                } catch (Exception e) {


                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
    }

    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_income));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Declare() {
        orderList = new ArrayList<>();
        recyclerView = findViewById(R.id.order_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sold = findViewById(R.id.sold_text);
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(MyIncome.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(MyIncome.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(MyIncome.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
    }

    private void Approval_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_ORDERTWO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");


                            Double grantotal = 0.00;
                            Double gran2total = 0.00;
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    Toast.makeText(MyIncome.this, "JSON Parsing Error: ", Toast.LENGTH_SHORT).show();
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String image_item = object.getString("photo");
                                    final String date = object.getString("date").trim();
                                    final String quantity = object.getString("quantity").trim();
                                    final String status = object.getString("status").trim();
                                    final String delivery_price = object.getString("delivery_price").trim();
                                    final String delivery_date = object.getString("delivery_date").trim();
                                    final String delivery_addr = object.getString("delivery_addr").trim();
                                    final String photo = object.getString("photo");
                                    Double grand = 0.00;
                                    grand += (price * Integer.parseInt(quantity))+ Double.parseDouble(delivery_price);


                                    OrderDone orderDone = new OrderDone();
                                    orderDone.setItemImage(photo);
                                    orderDone.setItemName(ad_detail);
                                    orderDone.setItemPrice(String.valueOf(price));
                                    orderDone.setDeliveryPrice(delivery_price);
                                    orderDone.setDeliveryTime(delivery_date);
                                    orderDone.setDeliveryAddress(delivery_addr);
                                    orderDone.setStatus(status);
                                    orderDone.setGrandtotal(String.format("%.2f", grand));
                                    orderDone.setQuantity(quantity);
                                    orderList.add(orderDone);
                                }
                                orderAdapter = new MyIncome_Adapter(MyIncome.this, orderList);
                                orderAdapter.sortArrayHighest();
                                recyclerView.setAdapter(orderAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyIncome.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error

                            }else if(error instanceof NoConnectionError){
                                //net work error

                            } else if (error instanceof AuthFailureError) {
                                //error

                            } else if (error instanceof ServerError) {
                                //Erroor
                            } else if (error instanceof NetworkError) {
                                //Error

                            } else if (error instanceof ParseError) {
                                //Error

                            }else{
                                //Error
                            }
                            //End


                        } catch (Exception e) {


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
