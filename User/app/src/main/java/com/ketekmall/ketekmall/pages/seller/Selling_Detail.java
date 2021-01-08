package com.ketekmall.ketekmall.pages.seller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.Element;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class Selling_Detail extends AppCompatActivity implements OneSignal.OSNotificationOpenedHandler{
    // Header
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET};

    private static String URL_SEND = "https://ketekmall.com/ketekmall/sendEmail_product_accept.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_tracking_no.php";
    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order_seller.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_NOTI = "https://ketekmall.com/ketekmall/onesignal_noti.php";
    private static String URL_GET_PLAYERID = "https://ketekmall.com/ketekmall/getPlayerID.php";


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


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_detail);
        getSession();
        ToolbarSetting();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId("6236bfc3-df4d-4f44-82d6-754332044779");

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
                ActivityCompat.requestPermissions(Selling_Detail.this, PERMISSIONS, 112);
                Log.v("TAG", "onCreate() Method invoked ");

                if(!hasPermissions(Selling_Detail.this, PERMISSIONS)){
                    Log.v("TAG", "download() Method DON'T HAVE PERMISSIONS ");

                }else{
                    PosLajuGetData(strCustomer_ID, strID,"admin@ketekmall.com", "8800001234", strOrder_Date, strID);
                    GetPlayerData(strCustomer_ID, "KM" + strID);
                }

//                ViewList(strCustomer_ID, strID, strOrder_Date);
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

    private void TrackingNo(final String ConnoteNo, final String strOrder_Date, final String strID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                Toast.makeText(Selling_Detail.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(Selling_Detail.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
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
                params.put("tracking_no", ConnoteNo);
                params.put("id", strID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Selling_Detail.this);
        requestQueue.add(stringRequest);
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
                            e.printStackTrace();
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

    private void PosLajuGetData(final String customerID,
                                final String OrderID,
                                final String subscriptionCode,
                                final String AccountNo,
                                final String strOrder_Date,
                                final String strID){
        Intent intent = getIntent();

        final String Quantity = intent.getStringExtra("quantity");
        final String Amount = intent.getStringExtra("TotalAmount");
        final String Weight = intent.getStringExtra("Weight");

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

                                    final String SellerName = object.getString("name");
                                    final String SellerEmail = object.getString("email");
                                    final String SellerAddress01 = object.getString("address_01");
                                    final String SellerAddress02 = object.getString("address_02");
                                    final String SellerDivision = object.getString("division");
                                    final String SellerPostCode = object.getString("postcode");
                                    final String SellerPhoneNo = object.getString("phone_no");

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

                                                                String ReceiverName = object.getString("name");
                                                                String ReceiverEmail = object.getString("email");
                                                                String ReceiverAddress01 = object.getString("address_01");
                                                                String ReceiverAddress02 = object.getString("address_02");
                                                                String ReceiverDivision = object.getString("division");
                                                                String ReceiverPostCode = object.getString("postcode");
                                                                String ReceiverPhoneNo = object.getString("phone_no");

                                                                RoutingCode(
                                                                        SellerPostCode,
                                                                        ReceiverPostCode,
                                                                        "KM00" + OrderID,
                                                                        subscriptionCode,
                                                                        AccountNo,
                                                                        SellerName,
                                                                        SellerPhoneNo,
                                                                        SellerAddress01 + "," + SellerAddress02 + "," + SellerPostCode + " " + SellerDivision,
                                                                        SellerPostCode + OrderID,
                                                                        SellerPhoneNo,
                                                                        SellerAddress01 + "," + SellerAddress02 + "," + SellerPostCode + " " + SellerDivision,
                                                                        SellerPostCode,
                                                                        Quantity,
                                                                        Weight,
                                                                        Amount,
                                                                        ReceiverName,
                                                                        ReceiverAddress01 + "," + ReceiverAddress02 + "," + ReceiverPostCode + " " + ReceiverDivision,
                                                                        ReceiverPostCode,
                                                                        ReceiverPhoneNo,
                                                                        SellerAddress02,
                                                                        SellerDivision,
                                                                        SellerEmail,
                                                                        ReceiverName,
                                                                        ReceiverName,
                                                                        ReceiverAddress02,
                                                                        ReceiverDivision,
                                                                        ReceiverDivision,
                                                                        ReceiverAddress01,
                                                                        ReceiverAddress02,
                                                                        ReceiverEmail,
                                                                        strOrder_Date,
                                                                        strID);

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
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("id", customerID);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(stringRequest);

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
                            e.printStackTrace();
                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    String HTTP_RoutingCode = "https://apis.pos.com.my/apigateway/as01/api/routingcode/v1";
    String serverKey_RoutingCode = "aWFGekJBMXUyRFFmTmNxUEpmcXhwR0hXYnY5cWdCTmE=";
    private void RoutingCode(String Origin,
                             String Destination,
                             final String OrderID,
                             final String subcriptionCode,
                             final String AccountNo,
                             final String SellerName,
                             final String SellerPhone,
                             final String SellerAddress,
                             final String PickupLocationID,
                             final String ContactPerson,
                             final String PickupAddress,
                             final String Postcode,
                             final String TotalQuantityToPickup,
                             final String Weight,
                             final String Amount,
                             final String ReceiverName,
                             final String ReceiverAddress,
                             final String ReceiverPostcode,
                             final String ReceiverPhone,
                             final String PickupDistrict,
                             final String PickupProvince,
                             final String PickupEmail,
                             final String ReceiverFirstName,
                             final String ReceiverLastName,
                             final String ReceiverDistrict,
                             final String ReceiverProvince,
                             final String ReceiverCity,
                             final String ReceiverAddress01,
                             final String ReceiverAddress02,
                             final String ReceiverEmail,
                             final String strOrder_Date,
                             final String strID) {
//        GenConnote(TotalQuantityToPickup,
//                OrderID,
//                subcriptionCode,
//                AccountNo,
//                SellerName,
//                SellerPhone,
//                SellerAddress,
//                PickupLocationID,
//                ContactPerson,
//                PickupAddress,
//                Postcode,
//                "1",
//                TotalQuantityToPickup,
//                Weight,
//                Amount,
//                ReceiverName,
//                ReceiverAddress,
//                ReceiverPostcode,
//                ReceiverPhone,
//                PickupDistrict,
//                PickupProvince,
//                PickupEmail,
//                ReceiverFirstName,
//                ReceiverLastName,
//                ReceiverDistrict,
//                ReceiverProvince,
//                ReceiverCity,
//                ReceiverAddress01,
//                ReceiverAddress02,
//                ReceiverEmail,
//                "KCU-SB-SBW");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, HTTP_RoutingCode +
                "?Origin=" + Origin +
                "&Destination=" + Destination,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Routingcode       = jsonObject.getString("RoutingCode");
                            String StatusCode      = jsonObject.getString("StatusCode");
                            String Message         = jsonObject.getString("Message");

                            GenConnote(TotalQuantityToPickup,
                                        OrderID,
                                        subcriptionCode,
                                        AccountNo,
                                        SellerName,
                                        SellerPhone,
                                        SellerAddress,
                                        PickupLocationID,
                                        ContactPerson,
                                        PickupAddress,
                                        Postcode,
                                        "1",
                                        TotalQuantityToPickup,
                                        Weight,
                                        Amount,
                                        ReceiverName,
                                        ReceiverAddress,
                                        ReceiverPostcode,
                                        ReceiverPhone,
                                        PickupDistrict,
                                        PickupProvince,
                                        PickupEmail,
                                        ReceiverFirstName,
                                        ReceiverLastName,
                                        ReceiverDistrict,
                                        ReceiverProvince,
                                        ReceiverCity,
                                        ReceiverAddress01,
                                        ReceiverAddress02,
                                        ReceiverEmail,
                                        Routingcode,
                                    strOrder_Date,
                                    strID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_RoutingCode);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Origin", "93050");
                params.put("Destination", "96000");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_GenerateConnote = "https://apis.pos.com.my/apigateway/as01/api/genconnote/v1";
    String serverKey_GenerateConnote = "MmpkbDI0MFpuTVpuZDRXb3J0VUk4M25ZTkY1a2NqSFU=";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GenConnote(String numberOfItem,
                           final String OrderID,
                           final String subcriptionCode,
                           final String AccountNo,
                           final String SellerName,
                           final String SellerPhone,
                           final String SellerAddress,
                           final String PickupLocationID,
                           final String ContactPerson,
                           final String PickupAddress,
                           final String Postcode,
                           final String ItemType,
                           final String TotalQuantityToPickup,
                           final String Weight,
                           final String Amount,
                           final String ReceiverName,
                           final String ReceiverAddress,
                           final String ReceiverPostcode,
                           final String ReceiverPhone,
                           final String PickupDistrict,
                           final String PickupProvince,
                           final String PickupEmail,
                           final String ReceiverFirstName,
                           final String ReceiverLastName,
                           final String ReceiverDistrict,
                           final String ReceiverProvince,
                           final String ReceiverCity,
                           final String ReceiverAddress01,
                           final String ReceiverAddress02,
                           final String ReceiverEmail,
                           final String RoutingCode,
                           final String strOrderDate,
                           final String strID) {



        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String Prefix = "ERC";
        String ApplicationCode = "HNM";
        String Secretid = "HM@$343";
        String username = "HMNNadhir";

//        PreAcceptanceSingle(
//                subcriptionCode,
//                AccountNo,
//                SellerName,
//                SellerPhone,
//                SellerAddress,
//                PickupLocationID,
//                ContactPerson,
//                PickupAddress,
//                Postcode,
//                ItemType,
//                TotalQuantityToPickup,
//                Weight,
//                "ER000249760MY",
//                Amount,
//                ReceiverName,
//                ReceiverAddress,
//                ReceiverPostcode,
//                ReceiverPhone,
//                PickupDistrict,
//                PickupProvince,
//                PickupEmail,
//                ReceiverFirstName,
//                ReceiverLastName,
//                ReceiverDistrict,
//                ReceiverProvince,
//                ReceiverCity);
//
//        GeneratePDF(
//                date,
//                Weight,
//                OrderID,
//                SellerName,
//                SellerPhone,
//                SellerAddress,
//                Postcode,
//                ReceiverName,
//                ReceiverPhone,
//                ReceiverPostcode,
//                AccountNo,
//                ReceiverAddress,
//                ReceiverAddress01,
//                ReceiverAddress02,
//                ReceiverCity,
//                ReceiverProvince,
//                ReceiverEmail,
//                OrderID,
//                "Document",
//                RoutingCode,
//                "ER000249760MY",
//                date);

        String API = HTTP_GenerateConnote +
                "?numberOfItem=" + numberOfItem +
                "&Prefix=" + Prefix +
                "&ApplicationCode=" + ApplicationCode +
                "&Secretid=" + Secretid +
                "&Orderid=" + OrderID +
                "&username=" + username;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.i("jsonObjectRequest", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String ConnoteNo       = jsonObject.getString("ConnoteNo");
                    String StatusCode      = jsonObject.getString("StatusCode");
                    String Message         = jsonObject.getString("Message");

                    Log.i("ObjectRequest", StatusCode);
                    Log.i("ObjectRequest", ConnoteNo);
                    Log.i("ObjectRequest", Message);

                    TrackingNo(ConnoteNo, strOrderDate, strID);

                    PreAcceptanceSingle(
                            subcriptionCode,
                            AccountNo,
                            SellerName,
                            SellerPhone,
                            SellerAddress,
                            PickupLocationID,
                            ContactPerson,
                            PickupAddress,
                            Postcode,
                            ItemType,
                            TotalQuantityToPickup,
                            Weight,
                            ConnoteNo,
                            Amount,
                            ReceiverName,
                            ReceiverAddress,
                            ReceiverPostcode,
                            ReceiverPhone,
                            PickupDistrict,
                            PickupProvince,
                            PickupEmail,
                            ReceiverFirstName,
                            ReceiverLastName,
                            ReceiverDistrict,
                            ReceiverProvince,
                            ReceiverCity);

                    GeneratePDF(
                            date,
                            Weight,
                            OrderID,
                            SellerName,
                            SellerPhone,
                            SellerAddress,
                            Postcode,
                            ReceiverName,
                            ReceiverPhone,
                            ReceiverPostcode,
                            AccountNo,
                            ReceiverAddress,
                            ReceiverAddress01,
                            ReceiverAddress02,
                            ReceiverCity,
                            ReceiverProvince,
                            ReceiverEmail,
                            OrderID,
                            "Document",
                            RoutingCode,
                            ConnoteNo,
                            date);

                }catch(JSONException e){
                    e.printStackTrace();
                    Log.i("jsonObjectRequest", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GenConnoteObjectRequest", "Status Code " + error.networkResponse.statusCode);
                Log.i("GenConnoteObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("GenConnoteObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                Log.i("GenConnoteObjectRequest", error.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-User-Key", serverKey_GenerateConnote);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_PreAcceptanceSingle = "https://apis.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
    String serverKey_PreAcceptanceSingle = "S0FFRHRLRXhQOVlFWVRzWjhyN0FzZnNCdmRxTElvTkI=";
    private void PreAcceptanceSingle(String subcriptionCode,
                                     String AccountNo,
                                     String SellerName,
                                     String SellerPhone,
                                     String SellerAddress,
                                     String PickupLocationID,
                                     String ContactPerson,
                                     String PickupAddress,
                                     String Postcode,
                                     String ItemType,
                                     String TotalQuantityToPickup,
                                     String Weight,
                                     String ConsignmentNoteNumber,
                                     String Amount,
                                     String ReceiverName,
                                     String ReceiverAddress,
                                     String ReceiverPostcode,
                                     String ReceiverPhone,
                                     String PickupDistrict,
                                     String PickupProvince,
                                     String PickupEmail,
                                     String ReceiverFirstName,
                                     String ReceiverLastName,
                                     String ReceiverDistrict,
                                     String ReceiverProvince,
                                     String ReceiverCity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_PreAcceptanceSingle +
                "?subscriptionCode=" +
                subcriptionCode +
                "&requireToPickup=FALSE"+
                "&requireWebHook=FALSE"+
                "&accountNo=" +
                AccountNo +
                "&callerName=" +
                SellerName +
                "&callerPhone=" +
                SellerPhone +
                "&pickupLocationID=" +
                PickupLocationID +
                "&pickupLocationName=" +
                SellerAddress +
                "&contactPerson=" +
                ContactPerson+
                "&phoneNo=" +
                SellerPhone +
                "&pickupAddress=" +
                PickupAddress +
                "&postCode=" +
                Postcode +
                "&ItemType=" +
                ItemType +
                "&totalQuantityToPickup=" +
                TotalQuantityToPickup +
                "&totalWeight=" +
                Weight +
                "&consignmentNoteNumber=" +
                ConsignmentNoteNumber +
                "&PaymentType=2"+
                "&amount=" +
                Amount +
                "&readyToCollectAt=08:00 AM"+
                "&closeAt=06:00 PM"+
                "&receiverName=" +
                ReceiverName +
                "&receiverID=" +
                "&receiverAddress=" +
                ReceiverAddress +
                "&receiverPostCode=" +
                ReceiverPostcode+
                "&receiverEmail=" +
                ReceiverPostcode +
                "&receiverPhone01=" +
                ReceiverPhone +
                "&receiverPhone02=" +
                ReceiverPhone +
                "&sellerReferenceNo="+
                "&itemDescription="+
                "&sellerOrderNo="+
                "&comments="+
                "&pickupDistrict=" +
                PickupDistrict +
                "&pickupProvince=" +
                PickupProvince +
                "&pickupEmail=" +
                PickupEmail +
                "&pickupCountry=MY"+
                "&pickupLocation="+
                "&receiverFname=" +
                ReceiverFirstName+
                "&receiverLname=" +
                ReceiverLastName+
                "&receiverAddress2=" +
                ReceiverAddress +
                "&receiverDistrict=" +
                ReceiverDistrict +
                "&receiverProvince=" +
                ReceiverProvince +
                "&receiverCity=" +
                ReceiverCity +
                "&receiverCountry=MY"+
                "&ShipmentName=PosLaju"+
                "&currency=MYR"+
                "&countryCode=MY",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String RoutingCode       = jsonObject.getString("RoutingCode");

                            Log.i("ObjectRequest", RoutingCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PreAcceptanceSingle);
                return params;
            }

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("subscriptionCode", "admin@ketekmall.com");
//                params.put("requireToPickup", "FALSE");
//                params.put("requireWebHook", "FALSE");
//                params.put("accountNo", "4799110862895245");
//                params.put("callerName", "Alfreeana Alfie");
//                params.put("callerPhone", "0138940023");
//                params.put("pickupLocationID", "M34123998");
//                params.put("pickupLocationName", "Sibu");
//                params.put("contactPerson", "0138940023");
//                params.put("phoneNo", "0138940023");
//                params.put("pickupAddress", "LOT245, NO.3G, LORONG SIBU JAYA 6");
//                params.put("ItemType", "2");
//                params.put("totalQuantityToPickup", "2");
//                params.put("totalWeight", "1.01");
//                params.put("PaymentType", "2");
//                params.put("Amount", "10.00");
//                params.put("readyToCollectAt", "12.00PM");
//                params.put("closeAt", "6.00PM");
//                params.put("receiverName", "Kalina Ann");
//                params.put("receiverID", "");
//                params.put("receiverAddress", "LO24, NO.3, LORONG SIBU JAYA 6");
//                params.put("receiverPostCode", "96000");
//                params.put("receiverEmail", "");
//                params.put("receiverPhone01", "0138940023");
//                params.put("receiverPhone02", "0189232002");
//                params.put("sellerReferenceNo", "");
//                params.put("itemDescription", "");
//                params.put("sellerOrderNo", "");
//                params.put("comments", "Fragile");
//                params.put("pickupDistrict", "SIBU JAYA");
//                params.put("pickupProvince", "SIBU");
//                params.put("pickupEmail", "annkalina53@gmail.com");
//                params.put("pickupCountry", "MY");
//                params.put("pickupLocation", "");
//                params.put("receiverFname", "Kalina");
//                params.put("receiverLname", "Ann");
//                params.put("receiverAddress2", "");
//                params.put("receiverDistrict", "Lorong Sibu Jaya 6");
//                params.put("receiverProvince", "Sibu Jaya");
//                params.put("receiverCity", "Sibu");
//                params.put("receiverCountry", "MY");
//                params.put("packDesc", "");
//                params.put("packVol", "");
//                params.put("packLeng", "");
//                params.put("postCode", "");
//                params.put("ConsignmentNoteNumber", "ER000249760MY");
//                params.put("packWidth", "");
//                params.put("packHeight", "");
//                params.put("packTotalitem", "");
//                params.put("orderDate", "");
//                params.put("packDeliveryType", "");
//                params.put("ShipmentName", "PosLaju");
//                params.put("pickupProv", "");
//                params.put("deliveryProv", "");
//                params.put("postalCode", "");
//                params.put("currency", "MYR");
//                params.put("countryCode", "MY");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    int pageWidth = 420;
    int pageHeight = 595;

    Bitmap PosLajuBitMap, ScaledPosLajuBitMap;
    Bitmap KetekMallBitmap, ScaledKetekMallBitMap;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void GeneratePDF(String ShipDate,
                             String Weight,
                             String OrderID,
                             String SenderName,
                             String SenderPhone,
                             String SenderAddress,
                             String SenderPostcode,
                             String RecipientName,
                             String RecipientPhone,
                             String RecipientPostcode,
                             String RecipientAccoutNo,
                             String RecipientAddress,
                             String RecipientAddress01,
                             String RecipientAddress02,
                             String RecipientCity,
                             String RecipientState,
                             String RecipientEmail,
                             String ProductCode,
                             String Type,
                             String RoutingCode,
                             String ConnoteNo,
                             String ConnoteDate){





        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        PosLajuBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.poslaju_black, options);
        KetekMallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ketekmallx52_black, options);

        ScaledPosLajuBitMap = Bitmap.createScaledBitmap(PosLajuBitMap, 100, 45, false);
        ScaledKetekMallBitMap = Bitmap.createScaledBitmap(KetekMallBitmap, 50, 50, false);

        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Paint TitleTag = new Paint();
        Paint Details = new Paint();

        //Design - Outer Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(5, 5, pageWidth-5, pageHeight-5, paint);

        canvas.drawBitmap(ScaledPosLajuBitMap, 13, 20, paint);

        canvas.drawBitmap(ScaledKetekMallBitMap, 113, 20, paint);

        // Logo & Barcode
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 9, pageWidth-12, 75, paint);

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(ConnoteNo, BarcodeFormat.CODE_128,128, 37, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            canvas.drawBitmap(bitmap,230, 14, paint);
            Details.setTextAlign(Paint.Align.LEFT);
            Details.setColor(Color.BLACK);
            Details.setTextSize(12f);
            Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(ConnoteNo, 270, 66, Details);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Order Details - 01
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 82, 271, 174, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 82, 271, 97, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Order Details", 14, 93, TitleTag);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Ship By Date:", 14, 108, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Weight (kg):", 14, 119, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Order ID:", 14, 130, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(ShipDate, 84, 108, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(Weight, 84, 119, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(OrderID, 84, 130, Details);

        // Order Details - 02
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(278, 82, 408, 174, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(278, 82, 408, 97, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Order Details (Courier)", 279, 93, TitleTag);

        // LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(14f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Account Number:", 281, 111, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(14f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("8800472220", 281, 126, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Product:", 281, 140, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Domestic", 281, 150, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Type:", 281, 162, Details);

        // Right
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Courier Charges", 320, 140, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(Type, 310, 162, Details);

        // Sender, Receiver, POD Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 177, 271, pageHeight-18, paint);
        // Sender Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 177, 271, 333, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 177, 271, 192, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Sender Details (Pengirim)", 14, 188, TitleTag);

        // LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Name:", 14, 203, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Phone:", 14, 238, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Address:", 14, 253, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Postcode:", 14, 330, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderName, 64, 203, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderPhone, 64, 238, Details);

        TextPaint mTextPaintSender=new TextPaint();
        mTextPaintSender.setTextSize(10f);
        mTextPaintSender.setTextAlign(Paint.Align.LEFT);
        StaticLayout mTextLayoutSender = new StaticLayout(SenderAddress, mTextPaintSender, 170, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(64, 245);
        mTextLayoutSender.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(SenderPostcode, 64, 330, Details);

        // Recipient Details
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 333, 271, 489, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 333, 271, 348, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Recipient Details (Penerima)", 14, 344, TitleTag);

        //LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Name:", 14, 359, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Phone:", 14, 394, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Address:", 14, 409, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Postcode:", 14, 486, Details);

        // RIGHT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientName, 64, 359, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientPhone, 64, 394, Details);

        TextPaint mTextPaintRecipient=new TextPaint();
        mTextPaintRecipient.setTextSize(10f);
        mTextPaintRecipient.setTextAlign(Paint.Align.LEFT);
        StaticLayout mTextLayoutRecipient = new StaticLayout(RecipientAddress, mTextPaintRecipient, 170, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(64, 400);
        mTextLayoutRecipient.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(RecipientPostcode, 64, 486, Details);

        // POD
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(12, 489, 271, pageHeight-18, paint);
        // TITLE
        TitleTag.setTextAlign(Paint.Align.LEFT);
        TitleTag.setColor(Color.rgb(74, 74, 74));
        canvas.drawRect(12, 489, 271, 504, TitleTag);
        TitleTag.setColor(Color.WHITE);
        TitleTag.setTextSize(12f);
        TitleTag.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("POD", 14, 500, TitleTag);

        //LEFT
        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Name:", 14, 515, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("I.C.:", 14, 530, Details);

        Details.setTextAlign(Paint.Align.LEFT);
        Details.setColor(Color.BLACK);
        Details.setTextSize(10f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Signature:", 14, 545, Details);

        // QR CODE
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 1.5);
        canvas.drawRect(278, 177, pageWidth-12, pageHeight-18, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.5);
        canvas.drawRect(278, 192, pageWidth-12, pageHeight-18, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0.5);
        canvas.drawRect(278, 242, pageWidth-12, pageHeight-18, paint);

        TextPaint mTextPaint=new TextPaint();
        mTextPaint.setTextSize(22f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        StaticLayout mTextLayout = new StaticLayout(RoutingCode, mTextPaint, 80, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();

        canvas.translate(342, 192);
        mTextLayout.draw(canvas);
        canvas.restore();

        Details.setTextAlign(Paint.Align.CENTER);
        Details.setColor(Color.BLACK);
        Details.setTextSize(32f);
        Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(RecipientPostcode, 342, 292, Details);

        try {
            // String to produce information of the QR CODE
            String productId = "A2^"+
                    ConnoteNo +
                    "^" +
                    ConnoteDate +
                    "^" +
                    "MY^" +
                    ProductCode +
                    "^" +
                    SenderName +
                    "^" +
                    SenderPhone +
                    "^"+
                    "^" +
                    SenderPostcode +
                    "^" +
                    RecipientAccoutNo +
                    "^" +
                    RecipientName +
                    "^" +
                    "^" +
                    RecipientAddress01 +
                    "^" +
                    RecipientAddress02 +
                    "^" +
                    RecipientPostcode +
                    "^" +
                    RecipientCity +
                    "^" +
                    RecipientState +
                    "^" +
                    RecipientPhone +
                    "^" +
                    RecipientEmail +
                    "^" +
                    Weight +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    "^" +
                    Type +
                    "^";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.QR_CODE,15, 15, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            // Put QR CODE inside the PDF
            canvas.drawBitmap(bitmap,320, 430, paint);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(ConnoteNo, BarcodeFormat.CODE_128,128, 37, hintMap);
            int width = 110;
            int height = 37;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            canvas.drawBitmap(bitmap,285, 515, paint);
            Details.setTextAlign(Paint.Align.LEFT);
            Details.setColor(Color.BLACK);
            Details.setTextSize(12f);
            Details.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(ConnoteNo, 295, 565, Details);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        document.finishPage(page);

        String directory_path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        final File file = new File(directory_path);

        final String dateFull = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        try {
            file.mkdirs();
            File filePath = new File(file,"PosLajuConsignmentNote.pdf");
            filePath.createNewFile();
            document.writeTo(new FileOutputStream(filePath));

            Log.i("Pathfile", filePath.toString());
            Toast.makeText(this, "Please check your Download Folder in File Manager", Toast.LENGTH_LONG).show();

            // close the document
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String filename = "PosLajuConsignmentNote.pdf";
                File file1 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                File file2 = new File(file1, filename);

                Intent target = new Intent(Intent.ACTION_VIEW);

                Uri PDFpath;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PDFpath = FileProvider.getUriForFile(Selling_Detail.this, getApplicationContext().getPackageName() + ".provider", file2);
                } else {
                    PDFpath = Uri.fromFile(file2);
                }

                target.setDataAndType(PDFpath,"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Log.i("Pathfile1", PDFpath.toString());

                try {
                    startActivity(target);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }
        }, 6000);
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
                params.put("Words", "Your order " + OrderID + " have been shipped");
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

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        OSNotificationAction.ActionType actionType = result.getAction().getType();
        JSONObject data = result.getNotification().getAdditionalData();
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.getAction().getActionId());

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
    }
}
