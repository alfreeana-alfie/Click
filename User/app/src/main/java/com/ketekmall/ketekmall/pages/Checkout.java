package com.ketekmall.ketekmall.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.ketekmall.ketekmall.ResultDelegate;
import com.ketekmall.ketekmall.data.Delivery_Combine;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.UserOrderAdapter_Other;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.MySingleton;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.user.Edit_Profile_Address;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;
import com.ipay.IPayIHR;

public class Checkout extends AppCompatActivity implements Serializable{
    String RefID = UUID.randomUUID().toString();
    public static final long serialVersionUID = 0;

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
    TextView Grand_Total, Grand_Total2, AddressUser, No_Address;
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
    ProgressBar loading;

    String HTTP_PoslajuDomesticbyPostcode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/poslajudomesticbypostcode/v1";
    String serverKey_PoslajuDomesticbyPostcode = "a1g2cmM2VmowNm00N1lZekFmTGR0MldpRHhKaFRHSks=";

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
                                loading.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString("id").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String image_item = object.getString("photo");
                                    final String seller_id = object.getString("seller_id");
                                    final String item_id = object.getString("item_id");
                                    final String quantity = object.getString("quantity");
                                    final String postCode = object.getString("postcode");
                                    final String weight = object.getString("weight");


//                                    if(postCode.contains("")) {
//                                        delivery_combine = new Delivery_Combine();
//                                        delivery_combine.setId(id);
//                                        delivery_combine.setDelivery_item_id(item_id);
//                                        delivery_combine.setSeller_id(seller_id);
//                                        delivery_combine.setAd_detail(ad_detail);
//                                        delivery_combine.setPhoto(image_item);
//                                        delivery_combine.setPrice(String.valueOf(price));
//                                        delivery_combine.setDivision(division);
//                                        delivery_combine.setQuantity(quantity);
//                                        delivery_combine.setDelivery_price("MYR0.00");
//                                        delivery_combine.setDelivery_division(division);
//
////                                      delivery_combine.setDelivery_price2(Html.fromHtml(delivery_text));
//                                        delivery_combine.setDelivery_division1("Not Available");
//
//                                        item_all_detailsList.add(delivery_combine);
//                                    }

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
                                                                String API = HTTP_PoslajuDomesticbyPostcode + "?postcodeFrom=" + "93050" + "&postcodeTo=" + strPostCode + "&Weight=" + quantity;

                                                                if(!postCode.contains("") && !weight.contains("0.00")){
                                                                    API = HTTP_PoslajuDomesticbyPostcode + "?postcodeFrom=" + postCode + "&postcodeTo=" + strPostCode + "&Weight=" + weight;
                                                                }

                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                Log.i("jsonObjectRequest", response);
                                                                                try{
                                                                                    JSONArray jsonarray = new JSONArray(response);
                                                                                    for(int i=0; i < jsonarray.length(); i++) {
                                                                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                                                        String totalAmount       = jsonobject.getString("totalAmount");
                                                                                        Log.i("jsonObjectRequest", totalAmount);

                                                                                        delivery_combine = new Delivery_Combine();
                                                                                        delivery_combine.setId(id);
                                                                                        delivery_combine.setDelivery_item_id(item_id);
                                                                                        delivery_combine.setSeller_id(seller_id);
                                                                                        delivery_combine.setAd_detail(ad_detail);
                                                                                        delivery_combine.setPhoto(image_item);
                                                                                        delivery_combine.setPrice(String.valueOf(price));
                                                                                        delivery_combine.setDivision(division);
                                                                                        delivery_combine.setQuantity(quantity);
                                                                                        delivery_combine.setDelivery_price(totalAmount);
                                                                                        delivery_combine.setDelivery_division(division);
                                                                                        delivery_combine.setDelivery_division1(division + " to " + strCity);

                                                                                        item_all_detailsList.add(delivery_combine);

                                                                                    }
                                                                                    userOrderAdapter = new UserOrderAdapter_Other(Checkout.this, item_all_detailsList, item_all_detailsList);
                                                                                    recyclerView.setAdapter(userOrderAdapter);
                                                                                    userOrderAdapter.setOnItemClickListener(new UserOrderAdapter_Other.OnItemClickListener() {
                                                                                        @Override
                                                                                        public void onSelfClick(int position) {
                                                                                            delivery_combine = new Delivery_Combine();
                                                                                            delivery_combine.setId(id);
                                                                                            delivery_combine.setDelivery_item_id(item_id);
                                                                                            delivery_combine.setSeller_id(seller_id);
                                                                                            delivery_combine.setAd_detail(ad_detail);
                                                                                            delivery_combine.setPhoto(image_item);
                                                                                            delivery_combine.setPrice(String.valueOf(price));
                                                                                            delivery_combine.setDivision(division);
                                                                                            delivery_combine.setQuantity(quantity);
                                                                                            delivery_combine.setDelivery_price("0.00");
                                                                                            delivery_combine.setDelivery_division(division);

                                                                                            String delivery_text;
                                                                                            delivery_text = "<font color='#999999'>MYR0.00</font>";
                                                                                            delivery_combine.setDelivery_price2(Html.fromHtml(delivery_text));
                                                                                            delivery_combine.setDelivery_division1(division + " to " + division);

                                                                                            grandtotal -= Double.parseDouble(Price);
                                                                                            Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));
                                                                                        }
                                                                                    });
                                                                                }catch(JSONException e){
                                                                                    e.printStackTrace();
                                                                                    Log.i("jsonObjectRequest", e.toString());
                                                                                }
                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                                                                                Log.i("STAGINGERROR", error.toString());
                                                                                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                                                                                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                                                                                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }) {
                                                                    @Override
                                                                    public Map<String, String> getHeaders() {
                                                                        Map<String, String> params = new HashMap<>();
                                                                        params.put("X-User-Key", serverKey_PoslajuDomesticbyPostcode);
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
                                                    try {

                                                        if (error instanceof TimeoutError) {
                                                            //Time out error
                                                            System.out.println("" + error);
                                                        } else if (error instanceof NoConnectionError) {
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
                                                        } else {
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
                try {

                    if (error instanceof TimeoutError) {
                        //Time out error
                        System.out.println("" + error);
                    } else if (error instanceof NoConnectionError) {
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
                    } else {
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
        loading = findViewById(R.id.loading);

        Grand_Total = findViewById(R.id.grandtotal);
        Grand_Total2 = findViewById(R.id.grandtotal2);
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

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Checkout.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Checkout.this, Me_Page.class);
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
                finish();
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

                                    final String strName = object.getString("name").trim();
                                    final String strEmail = object.getString("email").trim();
                                    final String strPhone_no = object.getString("phone_no").trim();
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
                                            Log.d("TAG", Grand_Total2.getText().toString());
                                            String backendPostURL2 = "https://ketekmall.com/ketekmall/backendURL.php";
                                            try{
                                                IPayIHPayment payment = new IPayIHPayment();
                                                payment.setMerchantKey ("8bgBOjTkij");
                                                payment.setMerchantCode ("M29640");
                                                payment.setPaymentId ("");
                                                payment.setCurrency ("MYR");
                                                payment.setRefNo (RefID);
                                                payment.setAmount (Grand_Total2.getText().toString());
                                                payment.setProdDesc ("KetekMall");
                                                payment.setUserName (strName);
                                                payment.setUserEmail (strEmail);
                                                payment.setRemark ("KetekMall");
                                                payment.setLang ("ISO-8859-1");
                                                payment.setCountry ("MY");
                                                payment.setBackendPostURL (backendPostURL2);
                                                Intent checkoutIntent = IPayIH.getInstance().checkout(payment
                                                        , Checkout.this, new ResultDelegate(), IPayIH.PAY_METHOD_CREDIT_CARD);
                                                startActivityForResult(checkoutIntent, 1);
                                            }catch (Exception e){
                                                Log.d("ERROR", e.toString());
                                            }
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

    private void getUserDetail2() {
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
                                    final String strEmail = object.getString("email").trim();
                                    final String strPhone_no = object.getString("phone_no").trim();
                                    String strAddress01 = object.getString("address_01");
                                    String strAddress02 = object.getString("address_02");
                                    final String strCity = object.getString("division");
                                    String strPostCode = object.getString("postcode");

                                    final String Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;
                                    final String Address2 = strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                    AddressUser.setText(Address);

                                    AddOrder(strCity, Address2);
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

    private String decodeBase64(String text) {
        String result = "";
        byte[] data = Base64.decode(text, Base64.DEFAULT);
        try {
            result = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //DO NOT DELETE
    private void AddOrder(final String User_Division, final String Address){
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
                                                                                        final String strEmail = object.getString("email").trim();
                                                                                        final String strPhone_no = object.getString("phone_no").trim();

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
                                                                    params.put("id", seller_id);
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                            requestQueue.add(stringRequest);
                                                            DeleteOrder_Single();
                                                            Intent intent = new Intent(Checkout.this, Place_Order.class);
                                                            intent.putExtra("seller_id", seller_id);
                                                            startActivity(intent);
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
                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    requestQueue.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
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

    private void PoslajuDomesticbyPostcode(String postcodeFrom, String postcodeTo, String Weight){

        String API = HTTP_PoslajuDomesticbyPostcode + "?postcodeFrom=" + postcodeFrom + "&postcodeTo=" + postcodeTo + "&Weight=" + Weight;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                        try{
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String totalAmount       = jsonobject.getString("totalAmount");
                                Log.i("jsonObjectRequest", totalAmount);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.i("jsonObjectRequest", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PoslajuDomesticbyPostcode);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeleteOrder_Single2();
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || data == null) {
            Log.d("TAG", "NULL");
        }else{
            getUserDetail2();
        }
    }
}
