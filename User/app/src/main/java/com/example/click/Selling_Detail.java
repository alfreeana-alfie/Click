package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.click.pages.Main_Order_Other;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Selling_Detail extends AppCompatActivity {

    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_tracking_no.php";
    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order_seller.php";

    EditText edit_review;
    Button btn_submit, btn_cancel;
    ImageView photo;
    TextView text_order_id, text_ad_detail, text_price, text_quantity;
    TextView text_placed_date, text_status, text_ship_placed;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_detail);
        getSession();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Selling_Detail.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_feed:
                        Intent intent5 = new Intent(Selling_Detail.this, Feed_page.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent5);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Selling_Detail.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Selling_Detail.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Intent intent = getIntent();

        final String strID = intent.getStringExtra("id");
        final String strPhoto = intent.getStringExtra("photo");
        final String strAd_Detail = intent.getStringExtra("ad_detail");
        final String strPrice = intent.getStringExtra("price");
        final String strQuantity = intent.getStringExtra("quantity");
        final String strDivision = intent.getStringExtra("division");
        final String strStatus = intent.getStringExtra("status");
        final String strOrder_Date = intent.getStringExtra("order_date");
        final String strTracking_NO = intent.getStringExtra("tracking_no");

        edit_review = findViewById(R.id.editText_review);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        text_order_id = findViewById(R.id.text_order_id);
        photo = findViewById(R.id.photo);

        text_ad_detail = findViewById(R.id.text_ad_detail);
        text_price = findViewById(R.id.text_price);
        text_quantity = findViewById(R.id.text_quantity);

        text_placed_date = findViewById(R.id.text_placed_date);
        text_status = findViewById(R.id.text_status);

        text_ship_placed = findViewById(R.id.text_ship_placed);

//        Toast.makeText(Selling_Detail.this, strTracking_NO, Toast.LENGTH_SHORT).show();
        edit_review.setText(strTracking_NO);
        text_order_id.setText("ID" + strID);
        Picasso.get().load(strPhoto).into(photo);
        text_ad_detail.setText(strAd_Detail);
        text_price.setText("MYR"+strPrice);
        text_quantity.setText("x" + strQuantity);
        text_placed_date.setText("Order Placed on " + strOrder_Date);
        text_status.setText(strStatus);
        text_ship_placed.setText("Shipped out to "+ strDivision);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewList(strID, strOrder_Date);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Selling_Detail.this, Main_Order_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

    }

    private void ViewList(final String strOrder_ID, final String strOrder_Date) {
        final String reviewtext = this.edit_review.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Selling_Detail.this, "Updated", Toast.LENGTH_SHORT).show();

//                                Delete_Order(strOrder_ID);

                                Intent intent = new Intent(Selling_Detail.this, Main_Order_Other.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Selling_Detail.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                                    Toast.makeText(Sell_Items_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("order_date", strOrder_Date);
                params.put("tracking_no", reviewtext);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Selling_Detail.this);
        requestQueue.add(stringRequest);
    }

    private void Delete_Order(final String strOrder_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Selling_Detail.this, "Your order has been updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Selling_Detail.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Selling_Detail.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", strOrder_ID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Selling_Detail.this);
        requestQueue.add(stringRequest);
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }
}
