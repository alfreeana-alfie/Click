package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.click.pages.Row_Add;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDelivery extends AppCompatActivity {

    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery.php";


    RecyclerView recyclerCricketers;
    ArrayList<Delivery> cricketersList = new ArrayList<>();

    String getId;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        recyclerCricketers = findViewById(R.id.recycler_cricketers);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        recyclerCricketers.setHasFixedSize(true);
        recyclerCricketers.setLayoutManager(new LinearLayoutManager(ActivityDelivery.this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCricketers.setLayoutManager(layoutManager);

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

                                    Delivery delivery = new Delivery(division, String.format("%.2f", price), days);
                                    cricketersList.add(delivery);
                                }
                                if (cricketersList.size() == 0) {
                                    TextView textView = findViewById(R.id.textView4);
                                    textView.setText("Opps, No delivery is added");

                                    Button Add_Delivery = findViewById(R.id.btn_goto_delivery);
                                    Button Edit = findViewById(R.id.btn_edit_delivery);
                                    Edit.setVisibility(View.GONE);
                                    Add_Delivery.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ActivityDelivery.this, Row_Add.class);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerCricketers.setVisibility(View.GONE);
                                } else {
                                    TextView textView = findViewById(R.id.textView4);

                                    Button Edit = findViewById(R.id.btn_edit_delivery);
                                    Edit.setVisibility(View.VISIBLE);
                                    Edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ActivityDelivery.this, Row_Add.class);
                                            startActivity(intent);
                                        }
                                    });
                                    Button Add_Delivery = findViewById(R.id.btn_goto_delivery);
                                    textView.setVisibility(View.GONE);
                                    Add_Delivery.setVisibility(View.GONE);
                                    recyclerCricketers.setAdapter(new DeliveryAdapter(cricketersList));
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
                        Toast.makeText(ActivityDelivery.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityDelivery.this);
        requestQueue.add(stringRequest);

    }
}
