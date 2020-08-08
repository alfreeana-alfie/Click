package com.example.click;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";

    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_CHECKOUT = "https://ketekmall.com/ketekmall/add_to_checkout.php";
    private static String URL_RECEIPTS = "https://ketekmall.com/ketekmall/add_receipt.php";
    private static String URL_READ_RECEIPTS = "https://ketekmall.com/ketekmall/read_receipts.php";
    private static String URL_APPROVAL = "https://ketekmall.com/ketekmall/add_approval.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_cart.php";

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    ArrayList<Item_All_Details> itemAllDetailsArrayList;

    RecyclerView recyclerView;
    CartAdapter _cart_adapter;
    Button Button_Checkout;
    TextView Grand_Total;

    String getId;
    SessionManager sessionManager;

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

        Button_Checkout = findViewById(R.id.btn_checkout);
        Button_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                                            final String seller_id = object.getString("customer_id").trim();
                                            final String main_category = object.getString("main_category").trim();
                                            final String sub_category = object.getString("sub_category").trim();
                                            final String ad_detail = object.getString("ad_detail").trim();
                                            final Double price = Double.valueOf(object.getString("price").trim());
                                            final String division = object.getString("division");
                                            final String district = object.getString("district");
                                            final String image_item = object.getString("photo");

                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECKOUT,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                final JSONObject Object = new JSONObject(response);
                                                                String success = Object.getString("success");

                                                                if (success.equals("1")) {
                                                                    addReceipt();
                                                                    Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();

                                                                } else {
                                                                    Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }catch (JSONException e){
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
                                                    }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("seller_id", seller_id);
                                                    params.put("customer_id", getId);
                                                    params.put("ad_detail", ad_detail);
                                                    params.put("main_category", main_category);
                                                    params.put("sub_category", sub_category);
                                                    params.put("price", String.format("%.2f", price));
                                                    params.put("division", division);
                                                    params.put("district", district);
                                                    params.put("photo", image_item);
                                                    params.put("item_id", id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                                            requestQueue.add(stringRequest);
                                        }
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
                                Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", getId);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
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
                startActivity(new Intent(getApplicationContext(), Homepage.class));
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

                                    String id = object.getString("id").trim();
                                    final String seller_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");

                                    Double grandtotal = 0.00;
                                    grandtotal += price;

                                    Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, String.format("%.2f", price), division, district, image_item);
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
                                    }

                                });

                            }
                            _cart_adapter.notifyDataSetChanged();
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

    private void addReceipt(){
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
                                    final String seller_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECEIPTS,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        final JSONObject Object = new JSONObject(response);
                                                        String success = Object.getString("success");

                                                        if (success.equals("1")) {
                                                            Toast.makeText(Cart.this, "Success Receipt!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }catch (JSONException e){
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
                                            }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("customer_id", getId);
                                            params.put("seller_id", seller_id);
                                            params.put("quantity", "1");
                                            params.put("grand_total", String.format("%.2f", price));
                                            params.put("status", "Pending");
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                    requestQueue.add(stringRequest);

                                    addApproval();
                                }
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
                        Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void addApproval(){
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
                                    final String receipt_date = object.getString("date");

                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_CART,
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
                                                                final String seller_id = object.getString("customer_id").trim();

                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_APPROVAL,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    final JSONObject Object = new JSONObject(response);
                                                                                    String success = Object.getString("success");

                                                                                    if (success.equals("1")) {
                                                                                        Toast.makeText(Cart.this, "Success Approved!", Toast.LENGTH_SHORT).show();
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
                                                                                                                    RequestQueue rQueue = Volley.newRequestQueue(Cart.this);
                                                                                                                    rQueue.add(request);


                                                                                                                }
                                                                                                            } else {
                                                                                                                Toast.makeText(Cart.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                                        requestQueue.add(stringRequest);
                                                                                    } else {
                                                                                        Toast.makeText(Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                }catch (JSONException e){
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
                                                                        }){
                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                                        Map<String, String> params = new HashMap<>();
                                                                        params.put("seller_id", seller_id);
                                                                        params.put("customer_id", getId);
                                                                        params.put("item_id", id);
                                                                        params.put("receipt_id", receipt_id);
                                                                        params.put("receipt_date", receipt_date);
                                                                        params.put("status", "Pending");
                                                                        return params;
                                                                    }
                                                                };
                                                                RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                                                requestQueue.add(stringRequest);
                                                            }
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
                                                    Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();

                                                }
                                            }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("customer_id", getId);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                    requestQueue.add(stringRequest1);
                                }
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
                        Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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

//    private void getUserDetail() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("read");
//
//
//                            if (success.equals("1")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    String strName = object.getString("name").trim();
//                                    String strEmail = object.getString("email").trim();
//                                    String strPhoto = object.getString("photo");
//
//                                    String url = "https://click-1595830894120.firebaseio.com/users.json";
//
//                                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String s) {
//                                            try {
//                                                JSONObject obj = new JSONObject(s);
//
//                                                TOPIC = obj.getJSONObject(UserDetails.chatWith).get("token").toString();
//                                                NOTIFICATION_TITLE = UserDetails.username;
//                                                NOTIFICATION_MESSAGE = "You have new order";
//
//                                                JSONObject notification = new JSONObject();
//                                                JSONObject notifcationBody = new JSONObject();
//                                                try {
//                                                    notifcationBody.put("title", NOTIFICATION_TITLE);
//                                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
//
//                                                    notification.put("to", TOPIC);
//                                                    notification.put("data", notifcationBody);
//                                                    sendNotification(notification);
//
//                                                    Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
//                                                } catch (JSONException e) {
//                                                    Log.e(TAG, "onCreate: " + e.getMessage());
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError volleyError) {
//                                            System.out.println("" + volleyError);
//                                        }
//                                    });
//                                    RequestQueue rQueue = Volley.newRequestQueue(Cart.this);
//                                    rQueue.add(request);
//
//
//                                }
//                            } else {
//                                Toast.makeText(Cart.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", seller_id);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

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
                        Toast.makeText(Cart.this, "Request error", Toast.LENGTH_LONG).show();
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
        getSupportFragmentManager().getBackStackEntryCount();
    }
}