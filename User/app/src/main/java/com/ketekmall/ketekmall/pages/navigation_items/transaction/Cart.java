package com.ketekmall.ketekmall.pages.navigation_items.transaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.CartAdapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Cart extends AppCompatActivity {

    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_cart.php";
    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_CART_TEMP = "https://ketekmall.com/ketekmall/readcart_temp.php";
    private static String URL_READ_PRODUCTS = "https://ketekmall.com/ketekmall/read_products_two.php";
    private static String URL_READ_PRODUCTS_TWO = "https://ketekmall.com/ketekmall/readcart_single_two.php";
    private static String URL_READ_PRODUCTS_TWO_MINUS = "https://ketekmall.com/ketekmall/readcart_single_two_minus.php";
    private static String URL_ADD_CART_TEMP = "https://ketekmall.com/ketekmall/add_to_cart_temp_two.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_cart.php";
    private static String URL_DELETE_TEMP = "https://ketekmall.com/ketekmall/delete_cart_temp.php";
    private static String URL_DELETE_TEMP_USER = "https://ketekmall.com/ketekmall/delete_cart_temp_user.php";

    ArrayList<Item_All_Details> itemAllDetailsArrayList;
    ArrayList<Double> doubles = new ArrayList<>();

    RecyclerView recyclerView;
    CartAdapter _cart_adapter;
    Button Button_Checkout;
    TextView Grand_Total;

    String getId;
    SessionManager sessionManager;
    int number;

    BottomNavigationView bottomNav;

    ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Declare() {
        Grand_Total = findViewById(R.id.grandtotal);

        loading = findViewById(R.id.loading);


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

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Cart.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Cart.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });

        Button_Checkout = findViewById(R.id.btn_checkout);
        Button_Checkout.setVisibility(View.GONE);
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.shopping_cart));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder_Single();
                startActivity(new Intent(Cart.this, Homepage.class));
                finish();
            }
        });
        itemAllDetailsArrayList = new ArrayList<>();
    }

    private void View_Item() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    loading.setVisibility(View.GONE);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String id = object.getString("id").trim();
                                        final String seller_id = object.getString("seller_id").trim();
                                        final String main_category = object.getString("main_category").trim();
                                        final String sub_category = object.getString("sub_category").trim();
                                        final String ad_detail = object.getString("ad_detail").trim();
                                        final Double price = Double.valueOf(object.getString("price").trim());
                                        final String division = object.getString("division");
                                        final String postcode = object.getString("postcode");
                                        final String district = object.getString("district");
                                        final String image_item = object.getString("photo");
                                        final String quantity = object.getString("quantity");
                                        final String item_id = object.getString("item_id");
                                        final String weight = object.getString("weight");

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

                                                                    @SuppressLint("DefaultLocale")
                                                                    Item_All_Details item = new Item_All_Details(
                                                                            id,
                                                                            seller_id,
                                                                            main_category,
                                                                            sub_category,
                                                                            ad_detail,
                                                                            String.format("%.2f", price),
                                                                            division,
                                                                            district,
                                                                            image_item);
                                                                    item.setQuantity(quantity);
                                                                    item.setItem_id(item_id);
                                                                    item.setMax_order(max_order);
                                                                    item.setPostcode(postcode);
                                                                    item.setWeight(weight);
                                                                    itemAllDetailsArrayList.add(item);
                                                                }
                                                                _cart_adapter = new CartAdapter(Cart.this, itemAllDetailsArrayList);
                                                                recyclerView.setAdapter(_cart_adapter);
                                                                _cart_adapter.sortArrayHighest();
                                                                _cart_adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                                                                    @Override
                                                                    public void onDeleteClick(int position) {
                                                                        AlertDelete(position);
                                                                    }

                                                                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                                                                    @Override
                                                                    public void onDeleteCart_Temp(final int position) {
                                                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);

                                                                        DeleteCartTemp(item.getId());

                                                                        doubles.remove(Double.parseDouble(item.getPrice()) * Integer.parseInt(item.getQuantity()));

                                                                        Double fi = 0.00;
                                                                        for (int i = 0; i < doubles.size(); i++) {
                                                                            fi += doubles.get(i);
                                                                            Grand_Total.setText("MYR" + String.format("%.2f", fi));
                                                                        }
                                                                        if (doubles.size() == 0) {
                                                                            Grand_Total.setText("MYR0.00");
                                                                            Button_Checkout.setVisibility(View.GONE);
                                                                        }
                                                                    }

                                                                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                                                                    @Override
                                                                    public void onClick(int position) {
                                                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                        final double price = Double.parseDouble(item.getPrice());

                                                                        //Add to cart_temp
                                                                        AddCartTemp(item, price);

                                                                        Button_Checkout.setVisibility(View.VISIBLE);

                                                                        doubles.add(Double.parseDouble(item.getPrice()) * Integer.parseInt(item.getQuantity()));
                                                                        Log.d("QUAN", item.getQuantity());
                                                                        Double fi = 0.00;
                                                                        for (int i = 0; i < doubles.size(); i++) {
                                                                            fi += doubles.get(i);
                                                                            Grand_Total.setText("MYR" + String.format("%.2f", fi));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onAddClick(final int position) {
                                                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                        AddQuantity(item);
                                                                    }

                                                                    @Override
                                                                    public void onMinusClick(final int position) {
                                                                        final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                        if (item.getQuantity().equals("1")) {
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this, R.style.MyDialogTheme);
                                                                            builder.setTitle("Are you sure?");
                                                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    final Item_All_Details item = itemAllDetailsArrayList.get(position);
                                                                                    onClickAdd(position, item.getId());
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
                                                                            MinusQuantity(item);
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(Cart.this, "Failed", Toast.LENGTH_SHORT).show();
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
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
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
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void AddCartTemp(final Item_All_Details item, final double price) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_CART_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                final JSONObject Object = new JSONObject(response);
                                String success = Object.getString("success");
                                if (success.equals("1")) {
                                    Log.d("Message", "Return SUCCESS");
                                } else {
                                    Log.e("Message", "Return FAILED");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @SuppressLint("DefaultLocale")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", item.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void AlertDelete(final int position) {
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
                                if (response == null) {
                                    Log.e("onResponse", "Return NULL");
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")) {
                                            _cart_adapter.notifyDataSetChanged();
                                            itemAllDetailsArrayList.remove(position);
                                        } else {
                                            Toast.makeText(Cart.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
//                                        Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
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
//                number = 1;
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void DeleteCartTemp(final String CartID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Log.d("Message", "Return SUCCESS");
                                } else {
                                    Log.e("Message", "Return FAILED");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", CartID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void AddQuantity(final Item_All_Details item) {
//        Toast.makeText(Cart.this, item.getId(), Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCTS_TWO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                final JSONObject Object = new JSONObject(response);
                                String success = Object.getString("success");
                                JSONArray jsonArray = Object.getJSONArray("read");

                                if (success.equals("1")) {
                                    ReadCartTemp(item.getMax_order());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
//                                        final String max_order = object.getString("max_order");

//                                        if (number >= Integer.parseInt(max_order)) {
//                                            Toast.makeText(Cart.this, "Reached Maximum Order for " + item.getAd_detail() + ": " + max_order, Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(Cart.this, item.getItem_id(), Toast.LENGTH_SHORT).show();
//                                        }
                                    }
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Cart.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Cart.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("customer_id", getId);
                params.put("id", item.getId());
//                params.put("ad_detail", item.getAd_detail());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void onClickAdd(final int position, final String ItemID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    itemAllDetailsArrayList.remove(position);
                                    _cart_adapter.notifyItemRemoved(position);
                                } else {
                                    Log.e("Message", "Return FAILED");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Cart.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", ItemID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void ReadCartTemp(final String MaxOrder){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART_TEMP,
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
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String postcode = object.getString("postcode");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String seller_id = object.getString("seller_id");
                                    final String quantity = object.getString("quantity");
                                    final String weight = object.getString("weight");

                                    @SuppressLint("DefaultLocale")
                                    final Item_All_Details item = new Item_All_Details(
                                            id,
                                            seller_id,
                                            main_category,
                                            sub_category,
                                            ad_detail,
                                            String.format("%.2f", price),
                                            division,
                                            district,
                                            image_item);
                                    item.setQuantity(quantity);
                                    item.setMax_order(MaxOrder);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void MinusQuantity(final Item_All_Details item){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PRODUCTS_TWO_MINUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                final JSONObject Object = new JSONObject(response);
                                String success = Object.getString("success");
                                JSONArray jsonArray = Object.getJSONArray("read");

                                if (success.equals("1")) {
                                    ReadCartTemp2();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
//                                        final String max_order = object.getString("max_order");

//                                        if (number >= Integer.parseInt(max_order)) {
//                                            Toast.makeText(Cart.this, "Reached Maximum Order for " + item.getAd_detail() + ": " + max_order, Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(Cart.this, item.getItem_id(), Toast.LENGTH_SHORT).show();
//                                        }
                                    }
//                                                                Toast.makeText(Cart.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Cart.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(Cart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", item.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    private void ReadCartTemp2(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String id = object.getString("id").trim();
                                        final String main_category = object.getString("main_category").trim();
                                        final String sub_category = object.getString("sub_category").trim();
                                        final String ad_detail = object.getString("ad_detail").trim();
                                        final Double price = Double.valueOf(object.getString("price").trim());
                                        final String division = object.getString("division");
                                        final String postcode = object.getString("postcode");
                                        final String district = object.getString("district");
                                        final String image_item = object.getString("photo");
                                        final String seller_id = object.getString("seller_id");
                                        final String quantity = object.getString("quantity");
                                        final String weight = object.getString("weight");

                                        @SuppressLint("DefaultLocale") final Item_All_Details item = new Item_All_Details(
                                                id,
                                                seller_id,
                                                main_category,
                                                sub_category,
                                                ad_detail,
                                                String.format("%.2f", price),
                                                division,
                                                district,
                                                image_item);
                                        item.setQuantity(quantity);
                                        item.setPostcode(postcode);
                                        item.setWeight(weight);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
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
                                Log.d("Message", "Return SUCCESS");
                            } else {
                                Log.e("Message", "Return FAILED");
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
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
        startActivity(new Intent(Cart.this, Homepage.class));
        finish();
    }
}
