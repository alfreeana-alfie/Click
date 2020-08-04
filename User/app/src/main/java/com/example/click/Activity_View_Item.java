package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.click.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.click.Activity_All_View.AD_DETAIL;
import static com.example.click.Activity_All_View.DISTRICT;
import static com.example.click.Activity_All_View.DIVISION;
import static com.example.click.Activity_All_View.MAIN_CATE;
import static com.example.click.Activity_All_View.PHOTO;
import static com.example.click.Activity_All_View.PRICE;
import static com.example.click.Activity_All_View.SUB_CATE;
import static com.example.click.Activity_All_View.USERID;

public class Activity_View_Item extends AppCompatActivity {

    private ImageView img_item;
    private TextView ad_detail_item, price_item, contact_seller;
    ArrayList<String> al = new ArrayList<>();
    private static String URL_ADD_CART = "https://annkalina53.000webhostapp.com/android_register_login/add_to_cart.php";
    private static String URL_READ = "https://annkalina53.000webhostapp.com/android_register_login/read_detail.php";
    String userid, ad_detail, division, district, strMain_category, strSub_category, strPrice, photo, getId;
    SessionManager sessionManager;
    Button add_to_cart_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Declare();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        Intent intent = getIntent();
        userid = intent.getStringExtra(USERID);
        strMain_category = intent.getStringExtra(MAIN_CATE);
        strSub_category = intent.getStringExtra(SUB_CATE);
        ad_detail = intent.getStringExtra(AD_DETAIL);
        strPrice = intent.getStringExtra(PRICE);
        division = intent.getStringExtra(DIVISION);
        district = intent.getStringExtra(DISTRICT);
        photo = intent.getStringExtra(PHOTO);

        String Price_Text = "MYR" + strPrice;

//        getUserDetail();
        ad_detail_item.setText(ad_detail);
        price_item.setText(Price_Text);
//        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();
        Picasso.get().load(photo).into(img_item);
    }

    private void Declare() {
        Intent intent = getIntent();
        userid = intent.getStringExtra(USERID);
        strMain_category = intent.getStringExtra(MAIN_CATE);
        strSub_category = intent.getStringExtra(SUB_CATE);
        ad_detail = intent.getStringExtra(AD_DETAIL);
        strPrice = intent.getStringExtra(PRICE);
        division = intent.getStringExtra(DIVISION);
        district = intent.getStringExtra(DISTRICT);
        photo = intent.getStringExtra(PHOTO);

        img_item = findViewById(R.id.img_item);
        ad_detail_item = findViewById(R.id.ad_details_item);
        price_item = findViewById(R.id.price_item);
        contact_seller = findViewById(R.id.contact_seller);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_View_Item.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(response);
                                    String success = jsonObject1.getString("success");

                                    if (success.equals("1")) {
                                        Toast.makeText(Activity_View_Item.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Activity_View_Item.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Activity_View_Item.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", getId);
                        params.put("main_category", strMain_category);
                        params.put("sub_category", strSub_category);
                        params.put("ad_detail", ad_detail);
                        params.put("price", strPrice);
                        params.put("division", division);
                        params.put("district", district);
                        params.put("photo", photo);
                        params.put("seller_id", userid);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Activity_View_Item.this);
                requestQueue.add(stringRequest2);
            }
        });

        contact_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserDetail();
            }
        });
    }

    private void getUserDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
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

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strPhoto = object.getString("photo");

                                    UserDetails.chatWith = strName;
                                    Intent intent = new Intent(Activity_View_Item.this, Chat.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(Activity_View_Item.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Activity_All_View.this, "JSON Parsing Eror: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Activity_All_View.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().getBackStackEntryCount();
    }
}
