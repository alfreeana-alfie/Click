package com.ketekmall.ketekmall.pages.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.Order_BuyerAdapter;
import com.ketekmall.ketekmall.data.MySingleton;
import com.ketekmall.ketekmall.data.Order;
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBuying extends AppCompatActivity {

    public static final String ID = "id";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String ITEM_LOCATION = "district";
    public static final String PHOTO = "photo";

    private static String URL_READ_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer_done.php";
    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_EDIT_ORDER = "https://ketekmall.com/ketekmall/edit_order.php";
    private static String URL_NOTI = "https://ketekmall.com/ketekmall/onesignal_noti.php";
    private static String URL_GET_PLAYERID = "https://ketekmall.com/ketekmall/getPlayerID.php";


    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    RecyclerView gridView;
    Order_BuyerAdapter adapter_item;
    List<Order> itemList;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;
    TextView textView10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybuying);
        Declare();
        ToolbarSettings();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        Buying_List();
    }

    private void Declare() {
        itemList = new ArrayList<>();
        gridView = findViewById(R.id.recyclerView);
        gridView.setLayoutManager(new LinearLayoutManager(this));

        textView10 = findViewById(R.id.textView10);
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(MyBuying.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(MyBuying.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(MyBuying.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
    }

    private void Buying_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                if(jsonArray.length() == 0){
                                    textView10.setVisibility(View.VISIBLE);
                                }
                                textView10.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");

                                    final String seller_division = object.getString("seller_division");
                                    final String seller_district = object.getString("seller_district");

                                    final String image_item = object.getString("photo");
                                    final String item_id = object.getString("item_id").trim();
                                    final String order_date = object.getString("order_date").trim();
                                    final String date = object.getString("date").trim();
                                    final String quantity = object.getString("quantity").trim();
                                    final String status = object.getString("remarks").trim();
                                    final String tracking_no = object.getString("tracking_no");
                                    final String delivery_price = object.getString("delivery_price");
                                    final String delivery_date = object.getString("delivery_date");


                                    Order item = new Order(id,
                                            seller_id,
                                            ad_detail,
                                            main_category,
                                            sub_category,
                                            String.format("%.2f", price),
                                            division,
                                            district,
                                            image_item,
                                            item_id,
                                            customer_id,
                                            order_date,
                                            date,
                                            quantity,
                                            status);
                                    item.setSeller_division(seller_division);
                                    item.setSeller_district(seller_district);
                                    item.setTracking_no(tracking_no);
                                    item.setDelivery_price(delivery_price);
                                    item.setDelivery_date(delivery_date);
                                    itemList.add(item);
                                }
                                adapter_item = new Order_BuyerAdapter(MyBuying.this, itemList);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.sortArrayHighest();
                                adapter_item.setOnItemClickListener(new Order_BuyerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onCancelClick(int position) {
                                        Order order = itemList.get(position);

                                        final String strOrder_Id = order.getId();
                                        final String strSeller_id = order.getSeller_id();
                                        final String strOrder_Date = order.getOrder_date();

                                        final String remarks = "Cancel";
                                        Update_Order(strOrder_Date, remarks);

                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_DELETE_ORDER,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if (success.equals("1")) {
                                                                Toast.makeText(MyBuying.this, R.string.order_canceled, Toast.LENGTH_SHORT).show();
                                                                GetPlayerData(strSeller_id, "KM" + strOrder_Id);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(MyBuying.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        try {

                                                            if (error instanceof TimeoutError ) {
                                                                //Time out error

                                                            }else if(error instanceof NoConnectionError){
                                                                //net work error

                                                            } else if (error instanceof AuthFailureError) {
                                                                //error

                                                            } else if (error instanceof ServerError) {
                                                                //Erroor
                                                            } else if (error instanceof NetworkError) {
                                                                //Error

                                                            } else if (error instanceof ParseError) {
                                                                //Error

                                                            }else{
                                                                //Error
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id", strOrder_Id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(MyBuying.this);
                                        requestQueue.add(stringRequest1);
                                    }

                                    @Override
                                    public void onReviewClick(int position) {
                                        Order order = itemList.get(position);

                                        final String strOrder_Id = order.getId();
                                        final String strSeller_id = order.getSeller_id();
                                        final String strCustomer_id = order.getCustomer_id();
                                        final String strItem_id = order.getItem_id();
                                        final String strMain_category = order.getMain_category();
                                        final String strSub_category = order.getSub_category();
                                        final String strAd_Detail = order.getAd_detail();
                                        final Double strPrice = Double.valueOf(order.getPrice());
                                        final String strDivision = order.getDivision();
                                        final String strDistrict = order.getDistrict();
                                        final String photo = order.getPhoto();

                                        final String strSellerDivision = order.getSeller_division();
                                        final String strSellerDistrict = order.getSeller_district();

                                        final String strPhoto = order.getPhoto();
                                        final String strOrder_Date = order.getOrder_date();
                                        final String strDate = order.getDate();
                                        final String strQuantity = order.getQuantity();
                                        final String strStatus = order.getStatus();

                                        final String strTracking = order.getTracking_no();
                                        final String strDelivery_Price = order.getDelivery_price();
                                        final String strDelivery_Date = order.getDelivery_date();

                                        Intent intent1 = new Intent(MyBuying.this, Review_Page.class);
                                        intent1.putExtra("seller_id", strSeller_id);
                                        intent1.putExtra("customer_id", strCustomer_id);
                                        intent1.putExtra("item_id", strItem_id);
                                        intent1.putExtra("remarks", strStatus);
                                        intent1.putExtra("order_date", strOrder_Date);
                                        intent1.putExtra("order_id", strOrder_Id);
                                        intent1.putExtra("tracking_no", strTracking);
                                        intent1.putExtra("delivery_date", strDelivery_Date);
                                        intent1.putExtra("ad_detail", strAd_Detail);
                                        intent1.putExtra("price", String.format("%.2f", strPrice));
                                        intent1.putExtra("quantity", strQuantity);
                                        intent1.putExtra("ship_price", strDelivery_Price);
                                        intent1.putExtra("photo", photo);
                                        intent1.putExtra("seller_division", strSellerDivision);
                                        intent1.putExtra("division", strDivision);
                                        startActivity(intent1);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(MyBuying.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error

                            }else if(error instanceof NoConnectionError){
                                //net work error

                            } else if (error instanceof AuthFailureError) {
                                //error

                            } else if (error instanceof ServerError) {
                                //Erroor
                            } else if (error instanceof NetworkError) {
                                //Error

                            } else if (error instanceof ParseError) {
                                //Error

                            }else{
                                //Error
                            }
                            //End


                        } catch (Exception e) {


                        }
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
                                Toast.makeText(MyBuying.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                params.put("Words", "Order " + OrderID + " have been canceled!");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.buying));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Update_Order(final String strOrder_Date, final String remarks) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(MyBuying.this, R.string.success_update, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyBuying.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(MyBuying.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("order_date", strOrder_Date);
                params.put("remarks", remarks);
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
