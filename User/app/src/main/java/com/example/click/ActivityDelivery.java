package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.example.click.data.SessionManager;
import com.example.click.pages.Find_My_Items_Other;
import com.example.click.pages.Homepage;
import com.example.click.pages.Row_Add;
import com.example.click.pages.Sell_Items_Other;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDelivery extends AppCompatActivity {

    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_delivery_two.php";
    private static String URL_READ_PRODUCT_SINGLE = "https://ketekmall.com/ketekmall/read_products.php";


    RecyclerView recyclerCricketers;
    ArrayList<Delivery> cricketersList = new ArrayList<>();
    DeliveryAdapter deliveryAdapter;

    Button btn_accept, btn_back, btn_add;

    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        final Intent intent = getIntent();
        String item_id = intent.getStringExtra("item_id");
        String ad_detail = intent.getStringExtra("ad_detail");

        btn_accept = findViewById(R.id.accept_delivery);
        btn_back = findViewById(R.id.back_delivery);
        btn_add = findViewById(R.id.btn_add);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(ActivityDelivery.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(ActivityDelivery.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(ActivityDelivery.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(ActivityDelivery.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityDelivery.this, Homepage.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityDelivery.this, Find_My_Items_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);

            }
        });
        recyclerCricketers = findViewById(R.id.recycler_cricketers);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        recyclerCricketers.setHasFixedSize(true);
        recyclerCricketers.setLayoutManager(new LinearLayoutManager(ActivityDelivery.this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCricketers.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Setup Delivery");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Read_Product(ad_detail);
    }

    private void Read_Product(final String ad_detail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCT_SINGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String ad_detail = object.getString("ad_detail");

                                    Read_Delivery(id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityDelivery.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityDelivery.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ActivityDelivery.this, "Please setup delivery using this page instead", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(ActivityDelivery.this, Find_My_Items_Other.class);
                        startActivity(intent1);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                params.put("ad_detail", ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
        requestQueue.add(stringRequest);
    }

    private void Read_Delivery(final String item_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_DELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

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
                                    delivery.setId(id);
                                    cricketersList.add(delivery);
                                }
                                if (cricketersList.size() == 0) {
                                    TextView textView = findViewById(R.id.textView4);
                                    textView.setText("Opps, No delivery is added");

                                    Button Add_Delivery = findViewById(R.id.btn_goto_delivery);
                                    Button Edit = findViewById(R.id.btn_add);
                                    Edit.setVisibility(View.GONE);
                                    Add_Delivery.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = getIntent();
                                            final String ad_detail = intent1.getStringExtra("ad_detail");

                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCT_SINGLE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                String success = jsonObject.getString("success");
                                                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                if (success.equals("1")) {
                                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                                        JSONObject object = jsonArray.getJSONObject(i);

                                                                        final String id = object.getString("id").trim();

                                                                        Intent intent = new Intent(ActivityDelivery.this, Row_Add.class);
                                                                        intent.putExtra("item_id", id);
                                                                        intent.putExtra("ad_detail", ad_detail);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ActivityDelivery.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(ActivityDelivery.this, error.toString(), Toast.LENGTH_SHORT).show(); }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("user_id", getId);
                                                    params.put("ad_detail", ad_detail);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
                                            requestQueue.add(stringRequest);


                                        }
                                    });
//                                    recyclerCricketers.setVisibility(View.GONE);
                                } else {
                                    TextView textView = findViewById(R.id.textView4);

                                    final Button btn_add = findViewById(R.id.btn_add);
                                    btn_add.setVisibility(View.VISIBLE);
                                    btn_add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = getIntent();
                                            final String item_id = intent1.getStringExtra("item_id");
                                            final String ad_detail = intent1.getStringExtra("ad_detail");

                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCT_SINGLE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                String success = jsonObject.getString("success");
                                                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                if (success.equals("1")) {
                                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                                        JSONObject object = jsonArray.getJSONObject(i);

                                                                        final String id = object.getString("id").trim();

                                                                        Intent intent = new Intent(ActivityDelivery.this, Row_Add.class);
                                                                        intent.putExtra("item_id", id);
                                                                        intent.putExtra("ad_detail", ad_detail);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ActivityDelivery.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(ActivityDelivery.this, error.toString(), Toast.LENGTH_SHORT).show(); }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("user_id", getId);
                                                    params.put("ad_detail", ad_detail);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
                                            requestQueue.add(stringRequest);
                                        }
                                    });
                                    final Button Add_Delivery = findViewById(R.id.btn_goto_delivery);
                                    textView.setVisibility(View.GONE);
                                    Add_Delivery.setVisibility(View.GONE);
                                    deliveryAdapter = new DeliveryAdapter(cricketersList);
                                    recyclerCricketers.setAdapter(deliveryAdapter);
                                    deliveryAdapter.setOnItemClickListener(new DeliveryAdapter.OnItemClickListener() {
                                        @Override
                                        public void onEditClick(int position) {
                                            Delivery delivery = cricketersList.get(position);

                                            Intent intent1 = getIntent();
                                            final String item_id = intent1.getStringExtra("item_id");
                                            final String ad_detail = intent1.getStringExtra("ad_detail");

                                            Intent intent = new Intent(ActivityDelivery.this, Edit_Delivery.class);

                                            intent.putExtra("item_id",item_id);
                                            intent.putExtra("ad_detail", ad_detail);
                                            intent.putExtra("id", delivery.getId());
                                            intent.putExtra("division", delivery.getDivision());
                                            intent.putExtra("price", delivery.getPrice());
                                            intent.putExtra("days", delivery.getDays());

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onDeleteClick(final int position) {
                                            final Delivery delivery = cricketersList.get(position);

                                            final String id = delivery.getId();

                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                String success = jsonObject.getString("success");
                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ActivityDelivery.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                                                    cricketersList.remove(position);
                                                                    deliveryAdapter.notifyDataSetChanged();
                                                                } else {
                                                                    Toast.makeText(ActivityDelivery.this, "Failed to Update", Toast.LENGTH_SHORT).show();
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
                                                    params.put("id", id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
                                            requestQueue.add(stringRequest);
                                        }
                                    });

                                }
                            } else {
                                Toast.makeText(ActivityDelivery.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityDelivery.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityDelivery.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", item_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
