package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.example.click.adapter.CartAdapter;
import com.example.click.adapter.Item_Adapter;
import com.example.click.data.Item_All_Details;
import com.example.click.pages.Cart;
import com.example.click.pages.Homepage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shipping_Info extends AppCompatActivity {

    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single.php";

    RecyclerView recyclerView;
    DeliveryAdapter_Single adapter_item;
    List<Delivery> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_info);
        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Shipping_Info.this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shipping Information");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String item_id = intent.getStringExtra("item_id");

//        Toast.makeText(Shipping_Info.this, item_id, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(Shipping_Info.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Shipping_Info.this);
        requestQueue.add(stringRequest);
    }
}
