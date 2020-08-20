package com.example.click.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.Feed_page;
import com.example.click.Noti_Page;
import com.example.click.R;
import com.example.click.adapter.CartAdapter;
import com.example.click.adapter.UserOrderAdapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_cart.php";

    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_CART_TEMP = "https://ketekmall.com/ketekmall/readcart_temp.php";

    private static String URL_CHECKOUT = "https://ketekmall.com/ketekmall/add_to_checkout.php";
    private static String URL_READ_PRODUCTS = "https://ketekmall.com/ketekmall/read_products_two.php";
    private static String URL_ADD_CART_TEMP = "https://ketekmall.com/ketekmall/add_to_cart_temp.php";


    private static String URL_READ_RECEIPTS = "https://ketekmall.com/ketekmall/read_receipts.php";
    private static String URL_APPROVAL = "https://ketekmall.com/ketekmall/add_approval.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_cart.php";
    private static String URL_DELETE_TWO = "https://ketekmall.com/ketekmall/delete_order_buyer.php";

    private static String URL_DELETE_TEMP = "https://ketekmall.com/ketekmall/delete_cart_temp.php";
    private static String URL_DELETE_TEMP_USER = "https://ketekmall.com/ketekmall/delete_cart_temp_user.php";

    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order.php";
    private static String URL_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer.php";
    private static String URL_EDIT_ORDER = "https://ketekmall.com/ketekmall/edit_to_checkout.php";


    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    ArrayList<Item_All_Details> itemAllDetailsArrayList;
    ArrayList<Double> doubles = new ArrayList<>();

    RecyclerView recyclerView;
    CartAdapter _cart_adapter;
    Button Button_Checkout;
    TextView Grand_Total;

    String getId;
    SessionManager sessionManager;
    int number;
    String[] mylist;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        Declare();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        View_Item();
    }

    private void Declare() {
        Grand_Total = findViewById(R.id.grandtotal);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Cart.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_feed:
                        Intent intent5 = new Intent(Cart.this, Feed_page.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent5);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Cart.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Cart.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Button_Checkout = findViewById(R.id.btn_checkout);
        Button_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(Cart.this, Checkout.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.cart_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Cart.this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder_Single();
                Intent intent = new Intent(Cart.this, Homepage.class);
                startActivity(intent);
            }
        });
        itemAllDetailsArrayList = new ArrayList<>();
    }

    private void View_Item() {
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
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String quantity = object.getString("quantity");
                                    final String item_id = object.getString("item_id");

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCTS,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        final JSONObject Object = new JSONObject(response);
                                                        String success = Object.getString("success");
                                                        JSONArray jsonArray = Object.getJSONArray("read");

                                                        if (success.equals("1")) {
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject object = jsonArray.getJSONObject(i);
                                                                final String max_order = object.getString("max_order");

                                                                Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, String.format("%.2f", price), division, district, image_item);
                                                                item.setQuantity(quantity);
                                                                item.setItem_id(item_id);
                                                                item.setMax_order(max_order);
                                                                number = Integer.parseInt(item.getQuantity());
                                                                itemAllDetailsArrayList.add(item);
                                                            }
                                                            _cart_adapter = new CartAdapter(Cart.this, itemAllDetailsArrayList);
                                                            recyclerView.setAdapter(_cart_adapter);
                                                            _cart_adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onDeleteClick(final int position) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this, R.style.MyDialogTheme);
                                                                    builder.setTitle("Are you sure?");
                                                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                                                                                    new Response.Listener<String>() {
                                                                                        @Override
                                                                                        public void onResponse(String response) {
                                                                                            try {
                                                                                                JSONObject jsonObject = new JSONObject(response);
                                                                                                String success = jsonObject.getString("success");

                                                                                                if (success.equals("1")) {
                                                                                                    _cart_adapter.notifyDataSetChanged();
                                                                                                    itemAllDetailsArrayList.remove(position);
                                                                                                } else {
//                                                                        Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            } catch (JSONException e) {
                                                                                                e.printStackTrace();
                                                                                                Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                                                    params.put("id", item.getId());
                                                                                    params.put("cart_id", item.getId());
                                                                                    return params;
                                                                                }
                                                                            };
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                            requestQueue.add(stringRequest);
                                                                        }
                                                                    });

                                                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            number = 1;
                                                                            dialog.cancel();
                                                                        }
                                                                    });
                                                                    AlertDialog alertDialog = builder.create();
                                                                    alertDialog.show();
                                                                }

                                                                @Override
                                                                public void onDeleteCart_Temp(final int position) {
                                                                    final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        String success = jsonObject.getString("success");

                                                                                        if (success.equals("1")) {

                                                                                        } else {
//                                                                        Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                                            params.put("cart_id", item.getId());
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                    requestQueue.add(stringRequest);

                                                                    doubles.remove(Double.parseDouble(item.getPrice()) * number);
                                                                    Double fi = 0.00;
                                                                    for(int i = 0; i<doubles.size();i++){
                                                                        fi += doubles.get(i);
                                                                        Grand_Total.setText("MYR" + String.format("%.2f", fi));
                                                                    }
                                                                    if(doubles.size() == 0){
                                                                        Grand_Total.setText("MYR0.00");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onClick(int position) {
                                                                    final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                    final Double price = Double.valueOf(item.getPrice());

                                                                    //Add to cart_temp
                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_CART_TEMP,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        final JSONObject Object = new JSONObject(response);
                                                                                        String success = Object.getString("success");

                                                                                        if (success.equals("1")) {
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                                                        } else {
                                                                                            Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String, String> params = new HashMap<>();
                                                                            params.put("customer_id", getId);
                                                                            params.put("main_category", item.getMain_category());
                                                                            params.put("sub_category", item.getSub_category());
                                                                            params.put("ad_detail", item.getAd_detail());
                                                                            params.put("price", String.format("%.2f", price));
                                                                            params.put("division", item.getDivision());
                                                                            params.put("district", item.getDistrict());
                                                                            params.put("photo", item.getPhoto());
                                                                            params.put("seller_id", item.getSeller_id());
                                                                            params.put("item_id", item.getItem_id());
                                                                            params.put("quantity", String.valueOf(number));
                                                                            params.put("cart_id", item.getId());
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                    requestQueue.add(stringRequest);

                                                                    doubles.add(Double.parseDouble(item.getPrice()) * number);
                                                                    Double fi = 0.00;
                                                                    for(int i = 0; i<doubles.size();i++){
                                                                        fi += doubles.get(i);
                                                                        Grand_Total.setText("MYR" + String.format("%.2f", fi));
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAddClick(final int position) {
                                                                    final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                                                    ++number;
                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCTS,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {

                                                                                    try {
                                                                                        final JSONObject Object = new JSONObject(response);
                                                                                        String success = Object.getString("success");
                                                                                        JSONArray jsonArray = Object.getJSONArray("read");

                                                                                        if (success.equals("1")) {
                                                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                                                JSONObject object = jsonArray.getJSONObject(i);
                                                                                                final String max_order = object.getString("max_order");

                                                                                                if (number >= Integer.parseInt(max_order)) {
                                                                                                    Toast.makeText(Cart.this, "Reached Maximum Order for " + item.getAd_detail() + ": " + max_order, Toast.LENGTH_SHORT).show();
                                                                                                } else {
                                                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                                                                                            new Response.Listener<String>() {
                                                                                                                @Override
                                                                                                                public void onResponse(String response) {
                                                                                                                    try {
                                                                                                                        final JSONObject Object = new JSONObject(response);
                                                                                                                        String success = Object.getString("success");

                                                                                                                        if (success.equals("1")) {
                                                                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART_TEMP,
                                                                                                                                    new Response.Listener<String>() {
                                                                                                                                        @Override
                                                                                                                                        public void onResponse(String response) {
                                                                                                                                            try {
                                                                                                                                                JSONObject jsonObject = new JSONObject(response);
                                                                                                                                                String success = jsonObject.getString("success");
                                                                                                                                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                                                                                                Double grandtotal = 0.00;
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

//                                                                                                                                                        grandtotal += (price * Integer.parseInt(quantity));
//                                                                                                                                                        Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));

                                                                                                                                                        final Item_All_Details item = new Item_All_Details(id,seller_id, main_category, sub_category,ad_detail, String.format("%.2f", price), division, district, image_item);
                                                                                                                                                        item.setQuantity(quantity);
                                                                                                                                                        item.setMax_order(max_order);
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
                                                                                                                            RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                                                                            requestQueue.add(stringRequest);
                                                                                                                        } else {
                                                                                                                            Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                                                        }

                                                                                                                    } catch (JSONException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                            },
                                                                                                            new Response.ErrorListener() {
                                                                                                                @Override
                                                                                                                public void onErrorResponse(VolleyError error) {
                                                                                                                    Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }) {
                                                                                                        @Override
                                                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                                                            Map<String, String> params = new HashMap<>();
                                                                                                            params.put("id", item.getId());
                                                                                                            params.put("cart_id", item.getId());
                                                                                                            params.put("quantity", String.valueOf(number));
                                                                                                            return params;
                                                                                                        }
                                                                                                    };
                                                                                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                                                    requestQueue.add(stringRequest);
                                                                                                }
                                                                                            }
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                                                        } else {
                                                                                            Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String, String> params = new HashMap<>();
                                                                            params.put("ad_detail", item.getAd_detail());
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                    requestQueue.add(stringRequest);
//
                                                                }

                                                                @Override
                                                                public void onMinusClick(final int position) {
                                                                    final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                    --number;
                                                                    if (number == 0) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this, R.style.MyDialogTheme);
                                                                        builder.setTitle("Are you sure?");
                                                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                                                                                        new Response.Listener<String>() {
                                                                                            @Override
                                                                                            public void onResponse(String response) {
                                                                                                try {
                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                    String success = jsonObject.getString("success");

                                                                                                    if (success.equals("1")) {
                                                                                                        itemAllDetailsArrayList.remove(position);
                                                                                                        _cart_adapter.notifyItemRemoved(position);
                                                                                                    } else {
//                                                                        Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                    Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                                                        params.put("id", item.getId());
                                                                                        return params;
                                                                                    }
                                                                                };
                                                                                RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                                requestQueue.add(stringRequest);
                                                                            }
                                                                        });

                                                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });
                                                                        AlertDialog alertDialog = builder.create();
                                                                        alertDialog.show();
                                                                    } else {
                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                                                                new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        try {
                                                                                            final JSONObject Object = new JSONObject(response);
                                                                                            String success = Object.getString("success");

                                                                                            if (success.equals("1")) {
                                                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART_TEMP,
                                                                                                        new Response.Listener<String>() {
                                                                                                            @Override
                                                                                                            public void onResponse(String response) {
                                                                                                                try {
                                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                                    String success = jsonObject.getString("success");
                                                                                                                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                                                                    Double grandtotal = 0.00;
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

//                                                                                                                            grandtotal += (price * Integer.parseInt(quantity));
//
//                                                                                                                            Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));

                                                                                                                            final Item_All_Details item = new Item_All_Details(id,seller_id, main_category, sub_category,ad_detail, String.format("%.2f", price), division, district, image_item);
                                                                                                                            item.setQuantity(quantity);
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
                                                                                                RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                                                requestQueue.add(stringRequest);
                                                                                            } else {
                                                                                                Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                },
                                                                                new Response.ErrorListener() {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                        Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }) {
                                                                            @Override
                                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                                Map<String, String> params = new HashMap<>();
                                                                                params.put("id", item.getId());
                                                                                params.put("cart_id", item.getId());
                                                                                params.put("quantity", String.valueOf(number));
                                                                                return params;
                                                                            }
                                                                        };
                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                        requestQueue.add(stringRequest);
                                                                    }
//
                                                                }

                                                            });
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("ad_detail", ad_detail);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void DeleteOrder_Single() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                            } else {
                                Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeleteOrder_Single();
        Intent intent = new Intent(Cart.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
