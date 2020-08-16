package com.example.click.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.adapter.CartAdapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Other_Cart extends AppCompatActivity {

    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_cart.php";

    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";

    private static String URL_CHECKOUT = "https://ketekmall.com/ketekmall/add_to_checkout.php";

    private static String URL_ADD_CART_TEMP = "https://ketekmall.com/ketekmall/add_to_cart_temp.php";

    private static String URL_READ_PRODUCTS = "https://ketekmall.com/ketekmall/read_products_two.php";

    private static String URL_READ_RECEIPTS = "https://ketekmall.com/ketekmall/read_receipts.php";
    private static String URL_APPROVAL = "https://ketekmall.com/ketekmall/add_approval.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_cart.php";
    private static String URL_DELETE_ORDER = "https://ketekmall.com/ketekmall/delete_order.php";
    private static String URL_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer.php";
    private static String URL_EDIT_ORDER = "https://ketekmall.com/ketekmall/edit_to_checkout.php";

    ArrayList<Item_All_Details> itemAllDetailsArrayList;

    RecyclerView recyclerView;
    CartAdapter _cart_adapter;
    Button Button_Checkout;
    TextView Grand_Total;

    String getId;
    SessionManager sessionManager;
    int number;

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
                                            final String customer_id = object.getString("customer_id").trim();
                                            final String main_category = object.getString("main_category").trim();
                                            final String sub_category = object.getString("sub_category").trim();
                                            final String ad_detail = object.getString("ad_detail").trim();
                                            final Double price = Double.valueOf(object.getString("price").trim());
                                            final String division = object.getString("division");
                                            final String district = object.getString("district");
                                            final String image_item = object.getString("photo");
                                            final String seller_id = object.getString("seller_id").trim();
                                            final String item_id = object.getString("item_id").trim();
                                            final String quantity = object.getString("quantity").trim();

//                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ORDER,
//                                                    new Response.Listener<String>() {
//                                                        @Override
//                                                        public void onResponse(String response) {
//                                                            try {
//                                                                JSONObject jsonObject = new JSONObject(response);
//                                                                String success = jsonObject.getString("success");
//                                                                JSONArray jsonArray = jsonObject.getJSONArray("read");
//
//                                                                if (success.equals("1")) {
//                                                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                                                        JSONObject object = jsonArray.getJSONObject(i);
//
//                                                                        final String order_date = object.getString("order_date");
//
//                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_ORDER,
//                                                                                new Response.Listener<String>() {
//                                                                                    @Override
//                                                                                    public void onResponse(String response) {
//                                                                                        try {
//                                                                                            JSONObject jsonObject = new JSONObject(response);
//                                                                                            String success = jsonObject.getString("success");
//
//                                                                                            if (success.equals("1")) {
//                                                                                                Toast.makeText(Cart.this, order_date, Toast.LENGTH_SHORT).show();
//                                                                                            }
//                                                                                        } catch (JSONException e) {
//                                                                                            e.printStackTrace();
//                                                                                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
//                                                                                        }
//                                                                                    }
//                                                                                },
//                                                                                new Response.ErrorListener() {
//                                                                                    @Override
//                                                                                    public void onErrorResponse(VolleyError error) {
//                                                                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                                                                                    }
//                                                                                }) {
//                                                                            @Override
//                                                                            protected Map<String, String> getParams() throws AuthFailureError {
//                                                                                Map<String, String> params = new HashMap<>();
//                                                                                params.put("order_date", order_date);
//                                                                                params.put("item_id", item_id);
//                                                                                return params;
//                                                                            }
//                                                                        };
//                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
//                                                                        requestQueue.add(stringRequest);
//                                                                    }
//                                                                }
//                                                            } catch (JSONException e) {
//                                                                e.printStackTrace();
//                                                                Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    },
//                                                    new Response.ErrorListener() {
//                                                        @Override
//                                                        public void onErrorResponse(VolleyError error) {
//                                                            Toast.makeText(Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }) {
//                                                @Override
//                                                protected Map<String, String> getParams() throws AuthFailureError {
//                                                    Map<String, String> params = new HashMap<>();
//                                                    params.put("customer_id", getId);
//                                                    return params;
//                                                }
//                                            };
//                                            RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
//                                            requestQueue.add(stringRequest);

                                            Intent intent = new Intent(Other_Cart.this, Checkout.class);
                                            startActivity(intent);

                                            //Add to order
                                            /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECKOUT,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                final JSONObject Object = new JSONObject(response);
                                                                String success = Object.getString("success");

                                                                if (success.equals("1")) {
                                                                    Intent intent = new Intent(Cart.this, Checkout.class);
                                                                    startActivity(intent);
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
                                                    params.put("seller_id", seller_id);
                                                    params.put("customer_id", getId);
                                                    params.put("ad_detail", ad_detail);
                                                    params.put("main_category", main_category);
                                                    params.put("sub_category", sub_category);
                                                    params.put("price", String.format("%.2f", price));
                                                    params.put("division", division);
                                                    params.put("district", district);
                                                    params.put("photo", image_item);
                                                    params.put("item_id", item_id);
                                                    params.put("quantity", quantity);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                                            requestQueue.add(stringRequest);*/
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(Other_Cart.this));

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
                                    String quantity = object.getString("quantity");

//                                    Double grandtotal = 0.00;
//                                    grandtotal += (price * Integer.parseInt(quantity));
//
//                                    Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, String.format("%.2f", price), division, district, image_item);
                                    item.setQuantity(quantity);
                                    number = Integer.parseInt(item.getQuantity());
                                    itemAllDetailsArrayList.add(item);
                                }
                                _cart_adapter = new CartAdapter(Other_Cart.this, itemAllDetailsArrayList);
                                recyclerView.setAdapter(_cart_adapter);
                                _cart_adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                                    @Override
                                    public void onDeleteClick(final int position) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Other_Cart.this, R.style.MyDialogTheme);
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
                                                                    Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
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

                                    @Override
                                    public void onDeleteCart_Temp(final int position) {
                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_ORDER,
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
                                                            Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                params.put("customer_id", getId);
                                                params.put("ad_detail", item.getAd_detail());
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
                                        requestQueue.add(stringRequest);
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
                                                                Toast.makeText(Other_Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Other_Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                                                params.put("item_id", item.getId());
                                                params.put("quantity", item.getQuantity());
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
                                        requestQueue.add(stringRequest);


                                    }

                                    @Override
                                    public void onAddClick(int position) {
                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                        final Double price = Double.valueOf(item.getPrice());
                                        final int final_num = number++;
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
                                                                    String max_order = object.getString("max_order");

                                                                    if (final_num > Integer.parseInt(max_order)) {
                                                                        Toast.makeText(Other_Cart.this, "You have reach limit for this item", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                                                                new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        try {
                                                                                            final JSONObject Object = new JSONObject(response);
                                                                                            String success = Object.getString("success");

                                                                                            if (success.equals("1")) {

                                                                                                Double grandtotal = 0.00;
                                                                                                grandtotal += (price * final_num);

                                                                                                Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));
//                                                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                                                            } else {
                                                                                                Toast.makeText(Other_Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                            Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                },
                                                                                new Response.ErrorListener() {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                        Toast.makeText(Other_Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }) {
                                                                            @Override
                                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                                Map<String, String> params = new HashMap<>();
                                                                                params.put("id", item.getId());
                                                                                params.put("quantity", String.valueOf(final_num));
                                                                                return params;
                                                                            }
                                                                        };
                                                                        RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
                                                                        requestQueue.add(stringRequest);
                                                                    }
                                                                }
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(Other_Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Other_Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("ad_detail", item.getAd_detail());
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
                                        requestQueue.add(stringRequest);


                                    }

                                    @Override
                                    public void onMinusClick(final int position) {
                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                        final Double price = Double.valueOf(item.getPrice());

                                        final int final_num = number--;
                                        if (final_num == 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Other_Cart.this, R.style.MyDialogTheme);
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
                                                                        Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                    RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
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

                                                                    Double grandtotal = 0.00;
                                                                    grandtotal -= (price * final_num);

                                                                    Grand_Total.setText("MYR" + String.format("%.2f", grandtotal));
                                                                    Toast.makeText(Other_Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(Other_Cart.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Other_Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Other_Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("id", item.getId());
                                                    params.put("quantity", String.valueOf(final_num));
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(Other_Cart.this);
                                            requestQueue.add(stringRequest);
                                        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Other_Cart.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
