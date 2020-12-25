package com.ketekmall.ketekmall.pages.buyer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.ketekmall.ketekmall.pages.seller.Selling_Detail;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Review_Page extends AppCompatActivity {

    private static String URL_REVIEW = "https://ketekmall.com/ketekmall/add_review.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_remarks_done.php";
    private static String URL_NOTI = "https://ketekmall.com/ketekmall/onesignal_noti.php";
    private static String URL_GET_PLAYERID = "https://ketekmall.com/ketekmall/getPlayerID.php";

    TextView OrderID, Rejected, Finished, Cancel, TrackingNo, AddressUser, DateOrder, DateReceived, Ordered, Pending, Shippped, Received, AdDetail, Price, Quantity, SubTotal, ShipTotal, GrandTotal;
    ImageView Photo, OrderedBlack, OrderedGreen,
            PendingBlack, PendingGreen,
            ShippedBlack, ShippedGreen,
            ReceivedBlack, ReceivedGreen;

    BottomNavigationView bottomNav;
    EditText edit_review;
    Button btn_submit, btn_cancel, btn_received;
    String getId;
    SessionManager sessionManager;
    RelativeLayout review_layout;
    ScrollView order_layout;
    RatingBar ratingBar;
    ProgressBar loading;
    int numofStar;
    float getRating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page_other);

        getSession();
        ToolbarSetting();

        Intent intent = getIntent();
        final String strSeller_ID = intent.getStringExtra("seller_id");
        String strCustomer_ID = intent.getStringExtra("customer_id");
        final String strItem_ID = intent.getStringExtra("item_id");
        final String remarks = intent.getStringExtra("remarks");
        final String order_date = intent.getStringExtra("order_date");
        final String order_id = intent.getStringExtra("order_id");
        final String strTracking = intent.getStringExtra("tracking_no");
        final String strDelivery_Date = intent.getStringExtra("delivery_date");
        String strAd_Detail = intent.getStringExtra("ad_detail");
        final String strPrice = intent.getStringExtra("price");
        final String strQuantity = intent.getStringExtra("quantity");
        final String strShipping = intent.getStringExtra("ship_price");
        String strPhoto = intent.getStringExtra("photo");
        final String strSeller_Division = intent.getStringExtra("seller_division");
        final String strDivision = intent.getStringExtra("division");

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Review_Page.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Review_Page.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Review_Page.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Rejected = findViewById(R.id.rejected);
        Finished = findViewById(R.id.finished);
        Cancel = findViewById(R.id.cancel);

        OrderedBlack = findViewById(R.id.ordered_black);
        OrderedGreen = findViewById(R.id.ordered_green);

        PendingBlack = findViewById(R.id.pending_black);
        PendingGreen = findViewById(R.id.pending_green);

        ShippedBlack = findViewById(R.id.shipped_black);
        ShippedGreen = findViewById(R.id.shipped_green);

        ReceivedBlack = findViewById(R.id.received_black);
        ReceivedGreen = findViewById(R.id.received_green);

        edit_review = findViewById(R.id.editText_review);
        btn_received = findViewById(R.id.btn_received);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        OrderID = findViewById(R.id.order_id);
        TrackingNo = findViewById(R.id.tracking_id);
        AddressUser = findViewById(R.id.address_user);
        DateOrder = findViewById(R.id.date_order);
        DateReceived = findViewById(R.id.date_received);

        Ordered = findViewById(R.id.ordered);
        Pending = findViewById(R.id.pending);
        Shippped = findViewById(R.id.shipped);
        Received = findViewById(R.id.received);

        Photo = findViewById(R.id.photo);
        AdDetail = findViewById(R.id.text_ad_detail);
        Price = findViewById(R.id.text_price);
        Quantity = findViewById(R.id.text_quantity);

        SubTotal = findViewById(R.id.sub_total);
        ShipTotal = findViewById(R.id.ship_total);
        GrandTotal = findViewById(R.id.grand_total);

        loading = findViewById(R.id.loading);
        order_layout = findViewById(R.id.order_layout);
        review_layout = findViewById(R.id.review_layout);

        OrderID.setText("KM" + order_id);

        TrackingNo.setText(strTracking);
        DateOrder.setText(order_date);
        DateReceived.setText(strDelivery_Date);

        AdDetail.setText(strAd_Detail);
        Price.setText("MYR" + strPrice);
        Quantity.setText("x" + strQuantity);
        Picasso.get().load(strPhoto).into(Photo);

        ratingBar = findViewById(R.id.ratingBar);

        assert strDivision != null;

        if(strDivision.equals(strSeller_Division)){
            Price.setText("MYR" + strPrice);
            Double sub_total = 0.00;
            sub_total = Double.parseDouble(strPrice) * Integer.parseInt(strQuantity);

            SubTotal.setText("MYR" + String.format("%.2f", sub_total));
            ShipTotal.setText("MYR0.00");


            Double grandtotal = 0.00;
            grandtotal = sub_total;

            GrandTotal.setText("MYR" + String.format("%.2f", sub_total));
        }else {
            Double sub_total = 0.00;
            sub_total = Double.parseDouble(strPrice) * Integer.parseInt(strQuantity);

            SubTotal.setText("MYR" + String.format("%.2f", sub_total));
            ShipTotal.setText("MYR" + strShipping);


            Double grandtotal = 0.00;
            grandtotal = sub_total + Double.parseDouble(strShipping);

            GrandTotal.setText("MYR" + String.format("%.2f", grandtotal));

        }
        TrackingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://www.tracking.my/poslaju/" + strTracking));
                startActivity(intent1);
            }
        });

        getUserDetail(getId);

        if (remarks.equals("Ordered")) {
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if (remarks.equals("Pending")) {
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            PendingBlack.setVisibility(View.GONE);
            PendingGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
            Pending.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if (remarks.equals("Shipped")) {
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
        if (remarks.equals("Received")) {
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
            btn_received.setVisibility(View.INVISIBLE);
        }
        if (remarks.equals("Reject")) {
            Rejected.setVisibility(View.VISIBLE);
        }
        if (remarks.equals("Cancel")) {
            Cancel.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewList2(strSeller_ID, getId, strItem_ID, order_id);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review_Page.this, Me_Page.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        btn_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strDivision.equals(strSeller_Division)){
                    Received(order_date, "0.00");
                }else {
                    Received(order_date, strShipping);
                }


                order_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ToolbarSetting() {
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

    private void ViewList2(final String strSeller_ID, final String strCustomer_ID, final String strItem_ID, final String OrderID) {
        final String reviewtext = edit_review.getText().toString();
        numofStar = ratingBar.getNumStars();
        getRating = ratingBar.getRating();

        loading.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);

        if (reviewtext.isEmpty()) {
            edit_review.requestFocus();
            edit_review.setError("Fields cannot be empty!");
        } else {
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

                                        final String strName = object.getString("name").trim();
                                        String strEmail = object.getString("email").trim();
                                        String strPhoto = object.getString("photo");

                                        GetPlayerData(strSeller_ID, "KM" + OrderID);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REVIEW,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("success");

                                                            if (success.equals("1")) {
                                                                loading.setVisibility(View.GONE);
                                                                btn_submit.setVisibility(View.VISIBLE);
                                                                Toast.makeText(Review_Page.this, R.string.success_update, Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(Review_Page.this, Me_Page.class);
                                                                startActivity(intent);
                                                            } else {
                                                                loading.setVisibility(View.GONE);
                                                                btn_submit.setVisibility(View.VISIBLE);
                                                                Toast.makeText(Review_Page.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            loading.setVisibility(View.GONE);
                                                            btn_submit.setVisibility(View.VISIBLE);
                                                            e.printStackTrace();
//                                                            Toast.makeText(Review_Page.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                params.put("seller_id", strSeller_ID);
                                                params.put("customer_id", getId);
                                                params.put("customer_name", strName);
                                                params.put("item_id", strItem_ID);
                                                params.put("review", reviewtext);
                                                params.put("rating", String.valueOf(getRating));

                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Review_Page.this);
                                        requestQueue.add(stringRequest);
                                    }
                                } else {
                                    Toast.makeText(Review_Page.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
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
//                                            Toast.makeText(Rev.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", getId);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Review_Page.this);
            requestQueue.add(stringRequest);
        }

    }

    private void GetPlayerData(final String CustomerUserID, final String OrderID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PLAYERID,
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

                                    String PlayerID = object.getString("PlayerID");
                                    String Name = object.getString("Name");
                                    String UserID = object.getString("UserID");

                                    OneSignalNoti(PlayerID, Name, OrderID);
                                }
                            } else {
                                Toast.makeText(Review_Page.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", CustomerUserID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void OneSignalNoti(final String PlayerUserID, final String Name, final String OrderID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NOTI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("POST", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
                                //net work error
                                System.out.println("" + error);
                            } else if (error instanceof AuthFailureError) {
                                //error
                                System.out.println("" + error);
                            } else if (error instanceof ServerError) {
                                //Error
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
                params.put("PlayerID", PlayerUserID);
                params.put("Name", Name);
                params.put("Words", "Order " + OrderID + " have been received and reviewed!");
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

    private void Received(final String strOrder_Date, final String strPrice) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Review_Page.this, R.string.success_update, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Review_Page.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put("order_date", strOrder_Date);
                params.put("delivery_price", strPrice);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Review_Page.this);
        requestQueue.add(stringRequest);
    }

    private void getUserDetail(final String user_id) {
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
                                    String strCity = object.getString("division");
                                    AddressUser.setText(strCity);
                                }
                            } else {
                                Toast.makeText(Review_Page.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put("id", user_id);
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
