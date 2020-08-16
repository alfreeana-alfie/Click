package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.data.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Other_After_Place_Order extends AppCompatActivity {

    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_order_buyer.php";

    Button Continue_Shopping;

    String getId;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_place_order);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);


        Continue_Shopping = findViewById(R.id.btn_continue);
        Continue_Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    if (success.equals("1")) {
                                        Intent intent = new Intent(Other_After_Place_Order.this, Homepage.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Other_After_Place_Order.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Other_After_Place_Order.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                        params.put("customer_id", getId);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Other_After_Place_Order.this);
                requestQueue.add(stringRequest);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
