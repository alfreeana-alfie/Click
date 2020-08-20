package com.example.click;

import android.content.Intent;
import android.media.SubtitleData;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.data.SessionManager;
import com.example.click.pages.Main_Order_Other;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Review_Page_Other extends AppCompatActivity {

    private static String URL_REVIEW = "https://ketekmall.com/ketekmall/add_review.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_remarks_done.php";

//    TextView ordered, pending, shipped, received,
//            tracking_notext, order_datetext, order_id1,
//            address_user, text_addetail, text_price, ship_date,
//            text_quantity, item_price, shipping_price, grandTotal;
    TextView OrderID,Rejected, Finished, TrackingNo, AddressUser, DateOrder, DateReceived, Ordered, Pending, Shippped, Received, AdDetail, Price, Quantity, SubTotal, ShipTotal, GrandTotal;
    ImageView Photo , OrderedBlack, OrderedGreen,
            PendingBlack, PendingGreen,
            ShippedBlack, ShippedGreen,
            ReceivedBlack, ReceivedGreen;

    EditText edit_review;
    Button btn_submit, btn_cancel, btn_received;
    String getId;
    SessionManager sessionManager;
    RelativeLayout review_layout;
    ScrollView order_layout;
    RatingBar ratingBar;
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
        String strPrice = intent.getStringExtra("price");
        String strQuantity = intent.getStringExtra("quantity");
        String strShipping = intent.getStringExtra("ship_price");
        String strPhoto = intent.getStringExtra("photo");
        Rejected = findViewById(R.id.rejected);
        Finished = findViewById(R.id.finished);

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

        OrderID.setText("KM" + order_id);

        TrackingNo.setText("PL" + strTracking);
        DateOrder.setText(order_date);
        DateReceived.setText(strDelivery_Date);

        AdDetail.setText(strAd_Detail);
        Price.setText("MYR" + strPrice);
        Quantity.setText("x" + strQuantity);
        Picasso.get().load(strPhoto).into(Photo);

        ratingBar = findViewById(R.id.ratingBar);



        Double sub_total = 0.00;
        sub_total = Double.parseDouble(strPrice) * Integer.parseInt(strQuantity);

        SubTotal.setText("MYR" + String.format("%.2f", sub_total));
        ShipTotal.setText("MYR" + strShipping);


        Double grandtotal = 0.00;
        grandtotal = sub_total + Double.parseDouble(strShipping);

        GrandTotal.setText("MYR" + String.format("%.2f", grandtotal));
//
//        grandTotal.setText("MYR" + String.format("%.2f", grandtotal));
//
//
        order_layout = findViewById(R.id.order_layout);
        review_layout = findViewById(R.id.review_layout);

        TrackingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://www.tracking.my/poslaju/" + "PL"+ strTracking));
                startActivity(intent1);
            }
        });

        getUserDetail(getId);

        if(remarks.equals("Ordered")){
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if(remarks.equals("Pending")){
            OrderedBlack.setVisibility(View.GONE);
            OrderedGreen.setVisibility(View.VISIBLE);
            PendingBlack.setVisibility(View.GONE);
            PendingGreen.setVisibility(View.VISIBLE);
            Ordered.setTextColor(getResources().getColor(R.color.colorGreen));
            Pending.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if(remarks.equals("Shipped")){
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
        if(remarks.equals("Received")){
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
        }if(remarks.equals("Reject")){
            Rejected.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewList2(strSeller_ID, getId, strItem_ID);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review_Page_Other.this, Main_Order_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

        btn_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Received(order_date);
                order_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Back");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review_Page_Other.this, Main_Order_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    private void ViewList2(final String strSeller_ID, final String strCustomer_ID, final String strItem_ID) {
        final String reviewtext = edit_review.getText().toString();
        numofStar = ratingBar.getNumStars();
        getRating = ratingBar.getRating();

        if (reviewtext.isEmpty()) {
            edit_review.requestFocus();
            edit_review.setError("Fields cannot be empty!");
        }else {
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

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REVIEW,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("success");

                                                            if (success.equals("1")) {
                                                                Toast.makeText(Review_Page_Other.this, "Saved", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(Review_Page_Other.this, Main_Order_Other.class);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(Review_Page_Other.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(Review_Page_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Review_Page_Other.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                                        RequestQueue requestQueue = Volley.newRequestQueue(Review_Page_Other.this);
                                        requestQueue.add(stringRequest);
                                    }
                                } else {
                                    Toast.makeText(Review_Page_Other.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
            RequestQueue requestQueue = Volley.newRequestQueue(Review_Page_Other.this);
            requestQueue.add(stringRequest);
        }

    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Received(final String strOrder_Date){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Review_Page_Other.this, "Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Review_Page_Other.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Review_Page_Other.this);
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
                                Toast.makeText(Review_Page_Other.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
}
