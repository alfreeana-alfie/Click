package com.example.click.pages;

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
import com.example.click.R;
import com.example.click.adapter.UserOrderAdapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.MySingleton;
import com.example.click.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_order_buyer.php";

    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer.php";
    private static String URL_RECEIPTS = "https://ketekmall.com/ketekmall/add_receipt.php";
    private static String URL_READ_RECEIPTS = "https://ketekmall.com/ketekmall/read_receipts.php";
    private static String URL_APPROVAL = "https://ketekmall.com/ketekmall/add_approval.php";

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    Button Button_Checkout;
    TextView Grand_Total, AddressUser;

    RecyclerView recyclerView;
    UserOrderAdapter userOrderAdapter;
    ArrayList<Item_All_Details> item_all_detailsList;

    String getId;
    SessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        Declare();
        getUserDetail();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
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

                                    String id = object.getString("id").trim();
                                    final String seller_id = object.getString("customer_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, String.format("%.2f", price), division, district, image_item);
                                    item_all_detailsList.add(item);
                                }
                                userOrderAdapter = new UserOrderAdapter(Checkout.this, item_all_detailsList);
                                recyclerView.setAdapter(userOrderAdapter);
                            }
                            userOrderAdapter.notifyDataSetChanged();
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

        Grand_Total = findViewById(R.id.grandtotal);
        AddressUser = findViewById(R.id.address);

        recyclerView = findViewById(R.id.item_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Checkout.this));
        recyclerView.setNestedScrollingEnabled(false);
        item_all_detailsList = new ArrayList<>();

        Button_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceipt();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder();
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
                                    String strCity = object.getString("division");
                                    String strPostCode = object.getString("postcode");

                                    String Address = strName + " | " + strPhone_no + "\n" + strAddress01 + " " + strAddress02 + "\n" + strPostCode + " " + strCity;

                                    AddressUser.setText(Address);
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
                                                            Toast.makeText(Checkout.this, "Success Receipt!", Toast.LENGTH_SHORT).show();
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
                                                                                                                Toast.makeText(Checkout.this, "Success Approved!", Toast.LENGTH_SHORT).show();
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

                                                                                                                                            Intent intent = new Intent(Checkout.this, After_Place_Order.class);
                                                                                                                                            startActivity(intent);

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
        DeleteOrder();
    }

    private void DeleteOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Intent intent = new Intent(Checkout.this, Cart.class);
                                startActivity(intent);
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
