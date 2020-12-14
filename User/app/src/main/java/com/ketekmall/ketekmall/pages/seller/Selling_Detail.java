package com.ketekmall.ketekmall.pages.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Selling_Detail extends AppCompatActivity {

    private static String URL_SEND = "https://ketekmall.com/ketekmall/sendEmail_product_accept.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_tracking_no.php";
    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order_seller.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";

    EditText edit_review;
    Button btn_submit, btn_cancel;
    ImageView photo;
    TextView text_order_id, text_ad_detail, text_price, text_quantity;
    TextView text_placed_date, text_status, text_ship_placed,
            customer_name, customer_address, customer_phone,Rejected, Cancel, Finished, Ordered, Pending, Shippped, Received, WithText;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;
    ProgressBar loading;
    ImageView Photo , OrderedBlack, OrderedGreen,
            PendingBlack, PendingGreen,
            ShippedBlack, ShippedGreen,
            ReceivedBlack, ReceivedGreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_detail);
        getSession();
        ToolbarSetting();
        Rejected = findViewById(R.id.rejected);
        Finished = findViewById(R.id.finished);
        Cancel = findViewById(R.id.cancel);

        Ordered = findViewById(R.id.ordered);
        Pending = findViewById(R.id.pending);
        Shippped = findViewById(R.id.shipped);
        Received = findViewById(R.id.received);

        OrderedBlack = findViewById(R.id.ordered_black);
        OrderedGreen = findViewById(R.id.ordered_green);

        PendingBlack = findViewById(R.id.pending_black);
        PendingGreen = findViewById(R.id.pending_green);

        ShippedBlack = findViewById(R.id.shipped_black);
        ShippedGreen = findViewById(R.id.shipped_green);

        ReceivedBlack = findViewById(R.id.received_black);
        ReceivedGreen = findViewById(R.id.received_green);

        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_addr);
        customer_phone = findViewById(R.id.customer_phone);

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

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Selling_Detail.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Selling_Detail.this, Me_Page.class);
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
        final String strCustomer_ID = intent.getStringExtra("customer_id");

        getCustomerDetailTwo(strCustomer_ID);
        WithText = findViewById(R.id.withText);
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
        loading = findViewById(R.id.loading);

//        Toast.makeText(Selling_Detail.this, strTracking_NO, Toast.LENGTH_SHORT).show();
        edit_review.setText(strTracking_NO);
        text_order_id.setText("ID" + strID);
        Picasso.get().load(strPhoto).into(photo);
        text_ad_detail.setText(strAd_Detail);
        text_price.setText("MYR" + strPrice);
        text_quantity.setText("x" + strQuantity);
        text_placed_date.setText("Order Placed on " + strOrder_Date);
        text_status.setText(strStatus);
        text_ship_placed.setText("Shipped out to " + strDivision);

        if(strStatus.equals("Pending")){
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if(strStatus.equals("Shipped")){
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            PendingBlack.setVisibility(View.GONE);
            PendingGreen.setVisibility(View.VISIBLE);
            ShippedBlack.setVisibility(View.GONE);
            ShippedGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
            Pending.setTextColor(getResources().getColor(R.color.colorGreen));
            Shippped.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if(strStatus.equals("Received")){
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            PendingBlack.setVisibility(View.GONE);
            PendingGreen.setVisibility(View.VISIBLE);
            ShippedBlack.setVisibility(View.GONE);
            ShippedGreen.setVisibility(View.VISIBLE);
            ReceivedBlack.setVisibility(View.GONE);
            ReceivedGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
            Pending.setTextColor(getResources().getColor(R.color.colorGreen));
            Shippped.setTextColor(getResources().getColor(R.color.colorGreen));
            Received.setTextColor(getResources().getColor(R.color.colorGreen));
            Finished.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);
            edit_review.setFocusable(false);
            edit_review.setFocusableInTouchMode(false);
        }if(strStatus.equals("Cancel")){
            Cancel.setVisibility(View.VISIBLE);
            WithText.setVisibility(View.GONE);
            edit_review.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        }
        if(strStatus.equals("Reject")){
            Rejected.setVisibility(View.VISIBLE);
            WithText.setVisibility(View.GONE);
            edit_review.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Selling_Detail.this, newPage.class);
//                startActivity(intent);
                ViewList(strCustomer_ID, strID, strOrder_Date);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Selling_Detail.this, MySelling.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.back));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ViewList(final String CustomerID, final String strOrder_ID, final String strOrder_Date) {
        loading.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);
        final String reviewtext = this.edit_review.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                btn_submit.setVisibility(View.VISIBLE);
                                Toast.makeText(Selling_Detail.this, "Updated", Toast.LENGTH_SHORT).show();

                                getCustomerDetail(CustomerID, strOrder_ID);
                                Intent intent = new Intent(Selling_Detail.this, MySelling.class);
                                startActivity(intent);
                            } else {
                                loading.setVisibility(View.GONE);
                                btn_submit.setVisibility(View.VISIBLE);
                                Toast.makeText(Selling_Detail.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
                            e.printStackTrace();
//                                    Toast.makeText(Sell_Items_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.VISIBLE);
                        try {

                            if (error instanceof TimeoutError) {
                                //Time out error
                                System.out.println("" + error);
                            }else if(error instanceof NoConnectionError){
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
                            }else{
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


                        }
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

    private void Delete_Order(final String strOrder_ID) {
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
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error
                                System.out.println("" + error);
                            }else if(error instanceof NoConnectionError){
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
                            }else{
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


                        }
                    }
                }) {
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

    private void getCustomerDetail(final String customerID, final String OrderID) {
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

                                    String strName = object.getString("name");
                                    String strEmail = object.getString("email");
                                    String strAddr1 = object.getString("address_01");
                                    String strAddr2 = object.getString("address_02");
                                    String strDivision = object.getString("division");
                                    String strPostCode = object.getString("postcode");
                                    String strPhone_NO = object.getString("phone_no");

                                    customer_name.setText(strName);
                                    customer_address.setText(strAddr1 + strAddr2 + "\n" + strDivision + strPostCode);
                                    customer_phone.setText(strPhone_NO);

                                    sendEmail(strEmail, OrderID);
                                }
                            } else {
                                Toast.makeText(Selling_Detail.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error
                                System.out.println("" + error);
                            }else if(error instanceof NoConnectionError){
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
                            }else{
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", customerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCustomerDetailTwo(final String customerID) {
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

                                    String strName = object.getString("name");
                                    String strEmail = object.getString("email");
                                    String strAddr1 = object.getString("address_01");
                                    String strAddr2 = object.getString("address_02");
                                    String strDivision = object.getString("division");
                                    String strPostCode = object.getString("postcode");
                                    String strPhone_NO = object.getString("phone_no");

                                    customer_name.setText(strName);
                                    customer_address.setText(strAddr1 + strAddr2 + "\n" + strDivision + strPostCode);
                                    customer_phone.setText(strPhone_NO);
                                }
                            } else {
                                Toast.makeText(Selling_Detail.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error
                                System.out.println("" + error);
                            }else if(error instanceof NoConnectionError){
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
                            }else{
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", customerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendEmail(final String email, final String OrderID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
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
                params.put("email", email);
                params.put("order_id", OrderID);
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
