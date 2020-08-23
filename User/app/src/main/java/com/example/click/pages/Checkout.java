package com.example.click.pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.Delivery_Combine;
import com.example.click.Noti_Page;
import com.example.click.Profile_Page;
import com.example.click.R;
import com.example.click.adapter.UserOrderAdapter_Other;
import com.example.click.data.Item_All_Details;
import com.example.click.data.MySingleton;
import com.example.click.data.SessionManager;
import com.example.click.user.Edit_Profile;
import com.example.click.user.Edit_Profile_Address;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single_delivery.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_order_buyer.php";

    private static String URL_DELETE_SINGLE = "https://ketekmall.com/ketekmall/delete_order.php";

    private static String URL_CHECKOUT = "https://ketekmall.com/ketekmall/add_to_checkout.php";

    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart_temp.php";
    private static String URL_CART_TWO = "https://ketekmall.com/ketekmall/readcart_temp_two.php";
    private static String URL_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer.php";
    private static String URL_RECEIPTS = "https://ketekmall.com/ketekmall/add_receipt.php";
    private static String URL_READ_RECEIPTS = "https://ketekmall.com/ketekmall/read_receipts.php";
    private static String URL_APPROVAL = "https://ketekmall.com/ketekmall/add_approval.php";
    private static String URL_DELETE_TEMP = "https://ketekmall.com/ketekmall/delete_cart_temp.php";
    private static String URL_DELETE_TEMP_USER = "https://ketekmall.com/ketekmall/delete_cart_temp_user.php";


    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    Button Button_Checkout;
    TextView Grand_Total, AddressUser, No_Address;
    LinearLayout linear2;

    RecyclerView recyclerView;
    UserOrderAdapter_Other userOrderAdapter;
    ArrayList<Delivery_Combine> item_all_detailsList;
    RelativeLayout address_layout;
    Item_All_Details item;
    Delivery_Combine delivery_combine;

    String getId, Price, Delivery_Date;
    SessionManager sessionManager;

    Double aFloat, grandtotal;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        Declare();
        getUserDetail();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        final HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART,
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
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String seller_id = object.getString("seller_id");
                                    final String item_id = object.getString("item_id");
                                    final String quantity = object.getString("quantity");

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
                                                                String strPhone_no = object.getString("phone_no").trim();
                                                                String strAddress01 = object.getString("address_01");
                                                                String strAddress02 = object.getString("address_02");
                                                                final String strCity = object.getString("division");
                                                                String strPostCode = object.getString("postcode");

                                                                String Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                                                AddressUser.setText(Address);

                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_DELIVERY,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                    String success = jsonObject.getString("success");
                                                                                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                                    if (success.equals("1")) {
                                                                                        if(jsonArray.length() ==0 ){

                                                                                            delivery_combine = new Delivery_Combine();
                                                                                            delivery_combine.setId(id);
                                                                                            delivery_combine.setDelivery_item_id(item_id);
                                                                                            delivery_combine.setSeller_id(seller_id);
                                                                                            delivery_combine.setAd_detail(ad_detail);
                                                                                            delivery_combine.setPhoto(image_item);
                                                                                            delivery_combine.setPrice(String.valueOf(price));
                                                                                            delivery_combine.setDivision(division);
                                                                                            delivery_combine.setQuantity(quantity);

                                                                                            String delivery_text;
                                                                                            delivery_text = "<font color='#FF3333'>Not Supported for selected area</font>";
                                                                                            delivery_combine.setDelivery_price2(Html.fromHtml(delivery_text));
                                                                                            delivery_combine.setDelivery_division1("");

                                                                                            item_all_detailsList.add(delivery_combine);
                                                                                            Button_Checkout.setVisibility(View.GONE);
                                                                                            linear2.setVisibility(View.GONE);
                                                                                            No_Address.setVisibility(View.VISIBLE);
//                                                                                            Toast.makeText(Checkout.this, "lol", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                                                            JSONObject object = jsonArray.getJSONObject(i);
                                                                                            String strDelivery_ID = object.getString("id").trim();
                                                                                            String strUser_ID = object.getString("user_id").trim();
                                                                                            String strDivision = object.getString("division");
                                                                                            Price = object.getString("price");
                                                                                            String strDays = object.getString("days");

                                                                                            grandtotal += (price * Integer.parseInt(quantity) + Double.parseDouble(Price));
                                                                                            Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));

                                                                                            Date date = Calendar.getInstance().getTime();

                                                                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                                                            String oneDate = simpleDateFormat.format(date);

                                                                                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                                                                            Calendar c = Calendar.getInstance();
                                                                                            try {
                                                                                                c.setTime(simpleDateFormat1.parse(oneDate));
                                                                                            }catch (ParseException e){
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            c.add(Calendar.DATE, Integer.parseInt(strDays));
                                                                                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                                                                                            Delivery_Date = simpleDateFormat2.format(c.getTime());

                                                                                            delivery_combine = new Delivery_Combine();
                                                                                            delivery_combine.setId(id);
                                                                                            delivery_combine.setDelivery_item_id(item_id);
                                                                                            delivery_combine.setSeller_id(seller_id);
                                                                                            delivery_combine.setAd_detail(ad_detail);
                                                                                            delivery_combine.setPhoto(image_item);
                                                                                            delivery_combine.setPrice(String.valueOf(price));
                                                                                            delivery_combine.setDivision(division);
                                                                                            delivery_combine.setQuantity(quantity);
                                                                                            delivery_combine.setDelivery_price(Price);
                                                                                            delivery_combine.setDelivery_division(strDivision);

                                                                                            String delivery_text;
                                                                                            delivery_text = "<font color='#999999'>MYR</font>"+Price;
                                                                                            delivery_combine.setDelivery_price2(Html.fromHtml(delivery_text));
                                                                                            delivery_combine.setDelivery_division1(division + " to " + strDivision);

                                                                                            item_all_detailsList.add(delivery_combine);
                                                                                        }
                                                                                        userOrderAdapter = new UserOrderAdapter_Other(Checkout.this, item_all_detailsList, item_all_detailsList);
                                                                                        recyclerView.setAdapter(userOrderAdapter);
                                                                                        userOrderAdapter.setOnItemClickListener(new UserOrderAdapter_Other.OnItemClickListener() {
                                                                                            @Override
                                                                                            public void onSelfClick(int position) {
                                                                                                delivery_combine = new Delivery_Combine();

                                                                                                delivery_combine.setDelivery_price(Price);
                                                                                                delivery_combine.setDelivery_division(division);

                                                                                                item_all_detailsList.add(delivery_combine);
                                                                                            }
                                                                                        });
                                                                                    } else {
                                                                                        Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(Checkout.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(Checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }) {
                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                                        Map<String, String> params = new HashMap<>();
                                                                        params.put("item_id", item_id);
                                                                        params.put("division", strCity);
                                                                        return params;
                                                                    }
                                                                };
                                                                RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                                requestQueue.add(stringRequest);

                                                            }
                                                        } else {
                                                            Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                            params.put("id", getId);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Declare() {
        Button_Checkout = findViewById(R.id.btn_place_order);
        No_Address = findViewById(R.id.no_address);
        linear2 = findViewById(R.id.linear2);

        Grand_Total = findViewById(R.id.grandtotal);
        AddressUser = findViewById(R.id.address);

        aFloat = 0.00;
        grandtotal = 0.00;

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Checkout.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(Edit_Item.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Checkout.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Checkout.this, Profile_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        address_layout = findViewById(R.id.address_layout);
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, Edit_Profile_Address.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.item_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Checkout.this));
        recyclerView.setNestedScrollingEnabled(false);
        item_all_detailsList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.checkout));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder_Single2();
                Intent intent = new Intent(Checkout.this, Cart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
                                    String strPhone_no = object.getString("phone_no").trim();
                                    String strAddress01 = object.getString("address_01");
                                    String strAddress02 = object.getString("address_02");
                                    final String strCity = object.getString("division");
                                    String strPostCode = object.getString("postcode");

                                    final String Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;
                                    final String Address2 = strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                    AddressUser.setText(Address);

                                    Button_Checkout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AddOrder(strCity, Address2);

                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addReceipt() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ORDER,
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

                                    final String order_id = object.getString("id").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String seller_id_cart = object.getString("seller_id").trim();
                                    final String item_id_cart = object.getString("item_id").trim();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECEIPTS,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        final JSONObject Object = new JSONObject(response);
                                                        String success = Object.getString("success");

                                                        if (success.equals("1")) {
//                                                            Toast.makeText(Checkout.this, "Success Receipt!", Toast.LENGTH_SHORT).show();
                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_RECEIPTS,
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

                                                                                        final String receipt_id = object.getString("id").trim();
                                                                                        final String seller_id_receipt = object.getString("seller_id").trim();
                                                                                        final String item_id_receipt = object.getString("item_id").trim();
                                                                                        final String receipt_date = object.getString("date");

                                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_APPROVAL,
                                                                                                new Response.Listener<String>() {
                                                                                                    @Override
                                                                                                    public void onResponse(String response) {
                                                                                                        try {
                                                                                                            final JSONObject Object = new JSONObject(response);
                                                                                                            String success = Object.getString("success");

                                                                                                            if (success.equals("1")) {
//                                                                                                                Toast.makeText(Checkout.this, "Success Approved!", Toast.LENGTH_SHORT).show();
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

                                                                                                                                            String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                                                                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                                                                                                @Override
                                                                                                                                                public void onResponse(String s) {
                                                                                                                                                    try {
                                                                                                                                                        JSONObject obj = new JSONObject(s);

                                                                                                                                                        TOPIC = obj.getJSONObject(strName).get("token").toString();
                                                                                                                                                        NOTIFICATION_TITLE = strName;
                                                                                                                                                        NOTIFICATION_MESSAGE = "You have new order";

                                                                                                                                                        JSONObject notification = new JSONObject();
                                                                                                                                                        JSONObject notifcationBody = new JSONObject();
                                                                                                                                                        try {
                                                                                                                                                            notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                                                                                                            notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                                                                                                                            notification.put("to", TOPIC);
                                                                                                                                                            notification.put("data", notifcationBody);
                                                                                                                                                            sendNotification(notification);

                                                                                                                                                            Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
                                                                                                                                                        } catch (JSONException e) {
                                                                                                                                                            Log.e(TAG, "onCreate: " + e.getMessage());
                                                                                                                                                        }
                                                                                                                                                    } catch (JSONException e) {
                                                                                                                                                        e.printStackTrace();
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }, new Response.ErrorListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onErrorResponse(VolleyError volleyError) {
                                                                                                                                                    System.out.println("" + volleyError);
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                            RequestQueue rQueue = Volley.newRequestQueue(Checkout.this);
                                                                                                                                            rQueue.add(request);
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                                                                                                        params.put("id", seller_id_receipt);
                                                                                                                        return params;
                                                                                                                    }
                                                                                                                };
                                                                                                                RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                                                                                requestQueue.add(stringRequest);
                                                                                                            } else {
                                                                                                                Toast.makeText(Checkout.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                                            }

                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                },
                                                                                                new Response.ErrorListener() {
                                                                                                    @Override
                                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                                        Toast.makeText(Checkout.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }) {
                                                                                            @Override
                                                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                                                Map<String, String> params = new HashMap<>();
                                                                                                params.put("seller_id", seller_id_receipt);
                                                                                                params.put("customer_id", getId);
                                                                                                params.put("item_id", item_id_receipt);
                                                                                                params.put("receipt_id", receipt_id);
                                                                                                params.put("receipt_date", receipt_date);
                                                                                                params.put("status", "Pending");
                                                                                                return params;
                                                                                            }
                                                                                        };
                                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                                                        requestQueue.add(stringRequest);

                                                                                    }
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                                Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }) {
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> params = new HashMap<>();
                                                                    params.put("customer_id", getId);
                                                                    params.put("item_id", item_id_cart);
                                                                    params.put("order_id", order_id);
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                            requestQueue.add(stringRequest);
                                                        } else {
                                                            Toast.makeText(Checkout.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Checkout.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("customer_id", getId);
                                            params.put("seller_id", seller_id_cart);
                                            params.put("item_id", item_id_cart);
                                            params.put("order_id", order_id);
                                            params.put("quantity", "1");
                                            params.put("grand_total", String.format("%.2f", price));
                                            params.put("status", "Pending");
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(Checkout.this);
                                    requestQueue2.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Checkout.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void AddOrder(final String User_Division, final String Address){
//        Toast.makeText(Checkout.this, Address, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART,
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
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String seller_division = object.getString("division");
                                    final String seller_district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String seller_id = object.getString("seller_id");
                                    final String item_id = object.getString("item_id");
                                    final String quantity = object.getString("quantity");

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECKOUT,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString("success");

                                                        if (success.equals("1")) {
                                                            //addReceipt();
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

                                                                                        String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                                            @Override
                                                                                            public void onResponse(String s) {
                                                                                                try {
                                                                                                    JSONObject obj = new JSONObject(s);

                                                                                                    TOPIC = obj.getJSONObject(strName).get("token").toString();
                                                                                                    NOTIFICATION_TITLE = strName;
                                                                                                    NOTIFICATION_MESSAGE = "You have new order";

                                                                                                    JSONObject notification = new JSONObject();
                                                                                                    JSONObject notifcationBody = new JSONObject();
                                                                                                    try {
                                                                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                                                                        notification.put("to", TOPIC);
                                                                                                        notification.put("data", notifcationBody);
                                                                                                        sendNotification(notification);

                                                                                                        Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
                                                                                                    } catch (JSONException e) {
                                                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                                                    }
                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                        }, new Response.ErrorListener() {
                                                                                            @Override
                                                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                                                System.out.println("" + volleyError);
                                                                                            }
                                                                                        });
                                                                                        RequestQueue rQueue = Volley.newRequestQueue(Checkout.this);
                                                                                        rQueue.add(request);

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
                                                                                                                    String strDelivery_ID = object.getString("id").trim();
                                                                                                                    String strUser_ID = object.getString("user_id").trim();
                                                                                                                    String strDivision = object.getString("division");
                                                                                                                    Price = object.getString("price");
                                                                                                                    String strDays = object.getString("days");

                                                                                                                    Date date = Calendar.getInstance().getTime();

                                                                                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                                                                                    String oneDate = simpleDateFormat.format(date);

                                                                                                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                                                                                                    Calendar c = Calendar.getInstance();
                                                                                                                    try {
                                                                                                                        c.setTime(simpleDateFormat1.parse(oneDate));
                                                                                                                    }catch (ParseException e){
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                    c.add(Calendar.DATE, Integer.parseInt(strDays));
                                                                                                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                                                                                                    Delivery_Date = simpleDateFormat2.format(c.getTime());
                                                                                                                }
                                                                                                            } else {
                                                                                                                Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                            Toast.makeText(Checkout.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                                                                        }
                                                                                                    }
                                                                                                },
                                                                                                new Response.ErrorListener() {
                                                                                                    @Override
                                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                                        Toast.makeText(Checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }) {
                                                                                            @Override
                                                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                                                Map<String, String> params = new HashMap<>();
                                                                                                params.put("item_id", item_id);
                                                                                                params.put("division", User_Division);
                                                                                                return params;
                                                                                            }
                                                                                        };
                                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                                                        requestQueue.add(stringRequest);
                                                                                    }
                                                                                } else {
                                                                                    Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                                                    params.put("id", seller_id);
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                            requestQueue.add(stringRequest);
                                                            DeleteOrder_Single();
                                                            Intent intent = new Intent(Checkout.this, After_Place_Order.class);
                                                            intent.putExtra("seller_id", seller_id);
                                                            startActivity(intent);
//                                                            Toast.makeText(Checkout.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(Checkout.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("seller_id", seller_id);
                                            params.put("customer_id", getId);
                                            params.put("ad_detail", ad_detail);
                                            params.put("main_category", main_category);
                                            params.put("sub_category", sub_category);
                                            params.put("price", String.format("%.2f", price));
                                            params.put("division", User_Division);
                                            params.put("district", User_Division);
                                            params.put("seller_division", seller_division);
                                            params.put("seller_district", seller_district);
                                            params.put("photo", image_item);
                                            params.put("item_id", item_id);
                                            params.put("quantity", quantity);
                                            params.put("delivery_price", Price);
                                            params.put("delivery_date", Delivery_Date);
                                            params.put("delivery_addr", Address);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addDelivery(final String Division) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ORDER,
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
                                    final String order_id = object.getString("id").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String seller_id_cart = object.getString("seller_id").trim();
                                    final String item_id_cart = object.getString("item_id").trim();

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
                                                                String strDelivery_ID = object.getString("id").trim();
                                                                String strUser_ID = object.getString("user_id").trim();
                                                                String strDivision = object.getString("division");
                                                                String strPrice = object.getString("price");
                                                                String strDays = object.getString("days");
                                                            }
                                                        } else {
                                                            Toast.makeText(Checkout.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(Checkout.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("user_id", seller_id_cart);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Checkout.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeleteOrder_Single2();
        Intent intent = new Intent(Checkout.this, Cart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void DeleteOrder_Single() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                            } else {
                                Toast.makeText(Checkout.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void DeleteOrder_Single2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                            } else {
                                Toast.makeText(Checkout.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

}
