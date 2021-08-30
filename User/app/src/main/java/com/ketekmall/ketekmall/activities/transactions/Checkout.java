package com.ketekmall.ketekmall.activities.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.activities.list.CartList;
import com.ketekmall.ketekmall.configs.Setup;
import com.ketekmall.ketekmall.utils.ResultDelegate;
import com.ketekmall.ketekmall.models.Checkout_Data;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapters.CheckoutListAdapter;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.activities.users.UserProfileCheckout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.ipay.IPayIH;
import com.ipay.IPayIHPayment;

import static com.ketekmall.ketekmall.configs.Constant.checkout;
import static com.ketekmall.ketekmall.configs.Constant.sADDRESS_01;
import static com.ketekmall.ketekmall.configs.Constant.sADDRESS_02;
import static com.ketekmall.ketekmall.configs.Constant.sAD_DETAIL;
import static com.ketekmall.ketekmall.configs.Constant.sCUSTOMER_ID;
import static com.ketekmall.ketekmall.configs.Constant.sDELIVERY_ADDRESS;
import static com.ketekmall.ketekmall.configs.Constant.sDELIVERY_DATE;
import static com.ketekmall.ketekmall.configs.Constant.sDELIVERY_PRICE;
import static com.ketekmall.ketekmall.configs.Constant.sDISTRICT;
import static com.ketekmall.ketekmall.configs.Constant.sDIVISION;
import static com.ketekmall.ketekmall.configs.Constant.s2DP;
import static com.ketekmall.ketekmall.configs.Constant.sDT_FORMAT;
import static com.ketekmall.ketekmall.configs.Constant.sEMAIL;
import static com.ketekmall.ketekmall.configs.Constant.sID;
import static com.ketekmall.ketekmall.configs.Constant.sITEM_ID;
import static com.ketekmall.ketekmall.configs.Constant.sMAIN_CATEGORY;
import static com.ketekmall.ketekmall.configs.Constant.sNAME;
import static com.ketekmall.ketekmall.configs.Constant.sONE;
import static com.ketekmall.ketekmall.configs.Constant.sPHONE_NO;
import static com.ketekmall.ketekmall.configs.Constant.sPHOTO;
import static com.ketekmall.ketekmall.configs.Constant.sPOSTCODE;
import static com.ketekmall.ketekmall.configs.Constant.sPRICE;
import static com.ketekmall.ketekmall.configs.Constant.sQUANTITY;
import static com.ketekmall.ketekmall.configs.Constant.sREAD;
import static com.ketekmall.ketekmall.configs.Constant.sREF_NO;
import static com.ketekmall.ketekmall.configs.Constant.sSELLER_DISTRICT;
import static com.ketekmall.ketekmall.configs.Constant.sSELLER_DIVISION;
import static com.ketekmall.ketekmall.configs.Constant.sSELLER_ID;
import static com.ketekmall.ketekmall.configs.Constant.sSUB_CATEGORY;
import static com.ketekmall.ketekmall.configs.Constant.sSUCCESS;
import static com.ketekmall.ketekmall.configs.Constant.sTOTAL;
import static com.ketekmall.ketekmall.configs.Constant.sWEIGHT;
import static com.ketekmall.ketekmall.configs.Constant.sXUserKey;
import static com.ketekmall.ketekmall.configs.Constant.serverPoslajuDomesticbyPostcode;
import static com.ketekmall.ketekmall.configs.Link.*;

public class Checkout extends AppCompatActivity implements Serializable{
    String RefID = UUID.randomUUID().toString();

    Button btnCheckout;
    TextView tvGrandTot, tvGrandTot02, tvCustomerAddr, tvEmptyAddr;
    LinearLayout llMain;

    RecyclerView rvOrderProduct;
    CheckoutListAdapter checkoutListAdapter;
    ArrayList<Checkout_Data> alCheckout;
    RelativeLayout rlAddr;
    Checkout_Data checkoutData;

    String getId, Price, Delivery_Date, ProductDesription;
    String item_id;

    Double doubGrandTot;
    BottomNavigationView bnvMenu;
    ProgressBar pbLoading;

    ArrayList productList = new ArrayList();
    ArrayList itemIdList = new ArrayList();
    
    Setup setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(checkout);
        Declare();
        getUserDetail();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TEMP_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                            if (success.equals(sONE)) {
                                pbLoading.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String pId = object.getString(sID).trim();
                                    final String pAdDetail = object.getString(sAD_DETAIL).trim();
                                    final Double pPrice = Double.valueOf(object.getString(sPRICE).trim());
                                    final String pDivision = object.getString(sDIVISION);
                                    final String pPhoto = object.getString(sPHOTO);
                                    final String pSellerId = object.getString(sSELLER_ID);
                                    final String pItemId = object.getString(sITEM_ID);
                                    final String pQuantity = object.getString(sQUANTITY);
                                    final String pPostcode = object.getString(sPOSTCODE);
                                    final String pWeight = object.getString(sWEIGHT);

                                    String description = pAdDetail + " x" + pQuantity;
                                    String itemCode = "KM00" + pId;

                                    productList.add(description);
                                    itemIdList.add(itemCode);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_DETAILS,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString(sSUCCESS);
                                                        JSONArray jsonArray = jsonObject.getJSONArray(sREAD);


                                                        if (success.equals(sONE)) {
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject object = jsonArray.getJSONObject(i);

                                                                String uName = object.getString(sNAME).trim();
                                                                String uPhoneNo = object.getString(sPHONE_NO).trim();
                                                                String uAddress01 = object.getString(sADDRESS_01);
                                                                String uAddress02 = object.getString(sADDRESS_02);
                                                                final String uDivision = object.getString(sDIVISION);
                                                                String uPostcode = object.getString(sPOSTCODE);

                                                                String fullAddress = uName + " | " + uPhoneNo + "\n" + uAddress01 + " " + uAddress02 + "\n" + uPostcode + " " + uDivision;

                                                                if(uPostcode.isEmpty()){
                                                                    fullAddress = "Incomplete Information";
                                                                    btnCheckout.setVisibility(View.GONE);
                                                                }else{
                                                                    btnCheckout.setVisibility(View.VISIBLE);
                                                                }

                                                                tvCustomerAddr.setText(fullAddress);
                                                                double newWeight = Double.parseDouble(pWeight) * Integer.parseInt(pQuantity);

                                                                String API = POSLAJU_DOMESTIC_BY_POSTCODE + "?postcodeFrom=" + pPostcode + "&postcodeTo=" + uPostcode + "&Weight=" + newWeight;

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
                                                                                        double NewTotalAmount = Double.parseDouble(totalAmount);
                                                                                        double RoundedTotalAmount = Math.ceil(NewTotalAmount);

                                                                                        double weightDouble = Double.parseDouble(pWeight);

                                                                                        double deliveryCharge = RoundedTotalAmount;

                                                                                        Price = String.format(s2DP, deliveryCharge);

                                                                                        Log.i("jsonObjectRequest", Price);

                                                                                        checkoutData = new Checkout_Data();
                                                                                        checkoutData.setId(pId);
                                                                                        checkoutData.setDelivery_item_id(pItemId);
                                                                                        checkoutData.setSeller_id(pSellerId);
                                                                                        checkoutData.setAd_detail(pAdDetail);
                                                                                        checkoutData.setPhoto(pPhoto);
                                                                                        checkoutData.setPrice(String.valueOf(pPrice));
                                                                                        checkoutData.setDivision(pDivision);
                                                                                        checkoutData.setQuantity(pQuantity);
                                                                                        checkoutData.setDelivery_price(String.format(s2DP, deliveryCharge));
                                                                                        checkoutData.setDelivery_division(uDivision);
                                                                                        checkoutData.setDelivery_division1(pDivision + " to " + uDivision);

                                                                                        doubGrandTot += (pPrice * Integer.parseInt(pQuantity) + deliveryCharge);
                                                                                        tvGrandTot.setText("RM" + String.format(s2DP, doubGrandTot));
                                                                                        tvGrandTot02.setText(String.format(s2DP, doubGrandTot));

                                                                                        alCheckout.add(checkoutData);

                                                                                    }
                                                                                    checkoutListAdapter = new CheckoutListAdapter(Checkout.this, alCheckout);
                                                                                    rvOrderProduct.setAdapter(checkoutListAdapter);
                                                                                    checkoutListAdapter.setOnItemClickListener(new CheckoutListAdapter.OnItemClickListener() {
                                                                                        @Override
                                                                                        public void onSelfClick(int position) {
                                                                                            checkoutData = new Checkout_Data();
                                                                                            checkoutData.setId(pId);
                                                                                            checkoutData.setDelivery_item_id(pItemId);
                                                                                            checkoutData.setSeller_id(pSellerId);
                                                                                            checkoutData.setAd_detail(pAdDetail);
                                                                                            checkoutData.setPhoto(pPhoto);
                                                                                            checkoutData.setPrice(String.valueOf(pPrice));
                                                                                            checkoutData.setDivision(pDivision);
                                                                                            checkoutData.setQuantity(pQuantity);
                                                                                            checkoutData.setDelivery_price("0.00");
                                                                                            checkoutData.setDelivery_division(pDivision);

                                                                                            String delivery_text;
                                                                                            delivery_text = "<font color='#999999'>RM0.00</font>";
                                                                                            checkoutData.setDelivery_price2(Html.fromHtml(delivery_text));
                                                                                            checkoutData.setDelivery_division1(pDivision + " to " + pDivision);

                                                                                            doubGrandTot -= Double.parseDouble(Price);
                                                                                            tvGrandTot02.setText(String.format(s2DP, doubGrandTot));
                                                                                            tvGrandTot.setText("RM" + String.format(s2DP, doubGrandTot));

                                                                                            Price = "0.00";
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
                                                                                Toast.makeText(Checkout.this, R.string.incomplete_information, Toast.LENGTH_LONG).show();
//                                                                                Log.i("STAGINGERROR", error.toString());
                                                                                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                                                                                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                                                                                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
//                                                                                Toast.makeText(Checkout.this, "Request error", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }) {
                                                                    @Override
                                                                    public Map<String, String> getHeaders() {
                                                                        Map<String, String> params = new HashMap<>();
                                                                        params.put(sXUserKey, serverPoslajuDomesticbyPostcode);
                                                                        return params;
                                                                    }

                                                                };
                                                                RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
                                                                requestQueue.add(stringRequest);
                                                            }
                                                        } else {
                                                            Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                            params.put(sID, getId);
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
                params.put(sCUSTOMER_ID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Declare() {
        setup = new Setup(this);
        getId = setup.getUserId();
        
        btnCheckout = findViewById(R.id.btn_place_order);
        tvEmptyAddr = findViewById(R.id.no_address);
        llMain = findViewById(R.id.linear2);
        pbLoading = findViewById(R.id.loading);

        tvGrandTot = findViewById(R.id.grandtotal);
        tvGrandTot02 = findViewById(R.id.grandtotal2);
        tvCustomerAddr = findViewById(R.id.address);

        doubGrandTot = 0.00;

        bnvMenu = findViewById(R.id.bottom_nav);
        bnvMenu.getMenu().getItem(0).setCheckable(false);
        bnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        DeleteOrder_Single3();
                        Intent intent4 = new Intent(Checkout.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        DeleteOrder_Single3();
                        Intent intent6 = new Intent(Checkout.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        DeleteOrder_Single3();
                        Intent intent1 = new Intent(Checkout.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        rlAddr = findViewById(R.id.address_layout);
        rlAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, UserProfileCheckout.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rvOrderProduct = findViewById(R.id.item_view);
        rvOrderProduct.setHasFixedSize(true);
        rvOrderProduct.setLayoutManager(new LinearLayoutManager(Checkout.this));
        rvOrderProduct.setNestedScrollingEnabled(false);
        alCheckout = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.checkout));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder_Single2();
                Intent intent = new Intent(Checkout.this, CartList.class);
                startActivity(intent);
//                finish();
            }
        });

    }

    private void getUserDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            JSONArray jsonArray = jsonObject.getJSONArray(sREAD);


                            if (success.equals(sONE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String strName = object.getString(sNAME).trim();
                                    final String strEmail = object.getString(sEMAIL).trim();
                                    final String strPhone_no = object.getString(sPHONE_NO).trim();
                                    String strAddress01 = object.getString(sADDRESS_01);
                                    String strAddress02 = object.getString(sADDRESS_02);
                                    final String strCity = object.getString(sDIVISION);
                                    String strPostCode = object.getString(sPOSTCODE);

                                    String Address, Address2;
                                    if(strAddress01.contains("") && strAddress02.contains("") && strCity.contains("") && strPostCode.contains("")){
                                        Address = "Incomplete Information";
                                        btnCheckout.setVisibility(View.GONE);
                                    }
                                    Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;
                                    Address2 = strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                    tvCustomerAddr.setText(Address);

                                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ProductDesription = TextUtils.join(", ", productList);
                                            item_id = TextUtils.join(", ", itemIdList);

                                            Log.d("NANA", ProductDesription);
                                            String backendPostURL2 = "https://ketekmall.com/ketekmall/backendURL.php";
                                            try{
                                                IPayIHPayment payment = new IPayIHPayment();
                                                payment.setMerchantKey ("8bgBOjTkij");
                                                payment.setMerchantCode ("M29640");
                                                payment.setPaymentId ("");
                                                payment.setCurrency ("MYR");
                                                payment.setRefNo (RefID);
                                                payment.setAmount (tvGrandTot02.getText().toString());
                                                payment.setProdDesc (ProductDesription);
                                                payment.setUserName (strName);
                                                payment.setUserEmail (strEmail);
                                                payment.setRemark ("Product Purchased: " + ProductDesription);
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
                                Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(sID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getUserDetail2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            JSONArray jsonArray = jsonObject.getJSONArray(sREAD);


                            if (success.equals(sONE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String strName = object.getString(sNAME).trim();
                                    final String strEmail = object.getString(sEMAIL).trim();
                                    final String strPhone_no = object.getString(sPHONE_NO).trim();
                                    String strAddress01 = object.getString(sADDRESS_01);
                                    String strAddress02 = object.getString(sADDRESS_02);
                                    final String strCity = object.getString(sDIVISION);
                                    String strPostCode = object.getString(sPOSTCODE);

                                    final String Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;
                                    final String Address2 = strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                    tvCustomerAddr.setText(Address);

                                    AddOrder(strCity, Address2, strEmail);
                                }
                            } else {
                                Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(sID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //DO NOT DELETE
    private void AddOrder(final String User_Division, final String Address, final String Email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TEMP_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                            if (success.equals(sONE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String id = object.getString(sID).trim();
                                    final String customer_id = object.getString(sCUSTOMER_ID).trim();
                                    final String main_category = object.getString(sMAIN_CATEGORY).trim();
                                    final String sub_category = object.getString(sSUB_CATEGORY).trim();
                                    final String ad_detail = object.getString(sAD_DETAIL).trim();
                                    final Double price = Double.valueOf(object.getString(sPRICE).trim());
                                    final String seller_division = object.getString(sDIVISION);
                                    final String seller_district = object.getString(sDISTRICT);
                                    final String image_item = object.getString(sPHOTO);
                                    final String seller_id = object.getString(sSELLER_ID);
                                    final String item_id = object.getString(sITEM_ID);
                                    final String quantity = object.getString(sQUANTITY);
                                    final String postcode = object.getString(sPOSTCODE);
                                    final String weight = object.getString(sWEIGHT);

                                    Date date = Calendar.getInstance().getTime();

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sDT_FORMAT, Locale.getDefault());
                                    String oneDate = simpleDateFormat.format(date);

                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(sDT_FORMAT);
                                    Calendar c = Calendar.getInstance();
                                    try {
                                        c.setTime(simpleDateFormat1.parse(oneDate));
                                    }catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    c.add(Calendar.DATE, 10);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(sDT_FORMAT);
                                    Delivery_Date = simpleDateFormat2.format(c.getTime());

//                                    Log.d("DATE", Delivery_Date);

                                    final Double TotalPrice = Double.parseDouble(Price) + (price * Integer.parseInt(quantity));
                                    //

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_TO_CHECKOUT,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString(sSUCCESS);

                                                        if (success.equals(sONE)) {

                                                            DeleteOrder_Single();
                                                            sendEmailBuyer(String.format(s2DP, price), Price, quantity, String.format(s2DP, TotalPrice), Email);
                                                            Intent intent = new Intent(Checkout.this, PlacingOrder.class);
                                                            intent.putExtra(sSELLER_ID, seller_id);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            double newprice = Double.parseDouble(weight);
                                            int quan = Integer.parseInt(quantity);
                                            double Delivery_Price = newprice * quan;
                                            String.valueOf(Delivery_Price);

                                            Map<String, String> params = new HashMap<>();
                                            params.put(sSELLER_ID, seller_id);
                                            params.put(sCUSTOMER_ID, getId);
                                            params.put(sAD_DETAIL, ad_detail);
                                            params.put(sMAIN_CATEGORY, main_category);
                                            params.put(sSUB_CATEGORY, sub_category);
                                            params.put(sPRICE, String.format(s2DP, price));
                                            params.put(sDIVISION, User_Division);
                                            params.put(sDISTRICT, User_Division);
                                            params.put(sSELLER_DIVISION, seller_division);
                                            params.put(sSELLER_DISTRICT, seller_district);
                                            params.put(sPHOTO, image_item);
                                            params.put(sITEM_ID, item_id);
                                            params.put(sQUANTITY, quantity);
                                            params.put(sDELIVERY_PRICE, Price);
                                            params.put(sDELIVERY_DATE, Delivery_Date);
                                            params.put(sDELIVERY_ADDRESS, Address);
                                            params.put(sWEIGHT, weight);
                                            params.put(sREF_NO, RefID);
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
                params.put(sCUSTOMER_ID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendEmailBuyer(final String Price, final String DeliveryPrice, final String Quantity, final String Total, final String Email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_ORDER_SUMMARY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
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
                params.put(sEMAIL, Email);
                params.put(sID, item_id);
                params.put(sAD_DETAIL, ProductDesription);
                params.put(sPRICE, Price);
                params.put(sDELIVERY_PRICE, DeliveryPrice);
                params.put(sTOTAL, Total);
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
        Intent intent = new Intent(Checkout.this, CartList.class);
        startActivity(intent);
    }

    private void DeleteOrder_Single() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);

                            if (success.equals(sONE)) {

                            } else {
                                Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put(sCUSTOMER_ID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void DeleteOrder_Single2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_TEMP_USER_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);

                            if (success.equals(sONE)) {

                            } else {
                                Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put(sCUSTOMER_ID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void DeleteOrder_Single3() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_TEMP_USER_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);

                            if (success.equals(sONE)) {

                            } else {
                                Toast.makeText(Checkout.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Checkout.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sCUSTOMER_ID, getId);
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
