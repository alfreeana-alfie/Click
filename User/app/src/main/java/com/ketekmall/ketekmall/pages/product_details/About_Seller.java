package com.ketekmall.ketekmall.pages.product_details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.ketekmall.ketekmall.adapter.Item_ByCategory_Adapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.data.UserDetails;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.ketekmall.ketekmall.pages.buyer.Chat;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class About_Seller extends AppCompatActivity {

    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readall_seller.php";
    private static String URL_READ_SELLER = "https://ketekmall.com/ketekmall/read_order_done_seller_shop.php";
    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_READ_CART2 = "https://ketekmall.com/ketekmall/readcart_single.php";

    SessionManager sessionManager;
    String getId;
    ImageView sellerPhoto;
    TextView sellerName, sellerLocation, item_text, sold_text;

    Item_ByCategory_Adapter adapter_item;
    List<Item_All_Details> itemList, itemList2;
    BottomNavigationView bottomNav;
    ImageButton btn_chat_wsp, btn_chat;
    private GridView gridView_item;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_seller);

        itemList = new ArrayList<>();
        itemList2 = new ArrayList<>();
        sellerPhoto = findViewById(R.id.image);
        sellerName = findViewById(R.id.seller_name);
        sellerLocation = findViewById(R.id.seller_location);
        gridView_item = findViewById(R.id.gridView_CarItem);
        item_text = findViewById(R.id.item_text);
        sold_text = findViewById(R.id.sold_text);
        btn_chat_wsp = findViewById(R.id.btn_chat_wsp);
        btn_chat = findViewById(R.id.btn_chat);

        Intent intent = getIntent();
        String seller_id = intent.getStringExtra("id");
        final String seller_name = intent.getStringExtra("seller_name");
        final String seller_email = intent.getStringExtra("seller_email");
        String seller_location = intent.getStringExtra("seller_location");
        String seller_photo = intent.getStringExtra("seller_photo");
        final String seller_phone = intent.getStringExtra("seller_phone");

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(About_Seller.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(About_Seller.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(About_Seller.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        btn_chat_wsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean installed = appInstalledOrNot();
                if (installed) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+6" + seller_phone));
                    startActivity(intent1);
                } else {
                    Toast.makeText(About_Seller.this, "Not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserDetails.chatWith = Objects.requireNonNull(seller_email).substring(0, seller_email.lastIndexOf("@"));
                UserDetails.chatWith1 = seller_name;
                Log.d("TAG", Objects.requireNonNull(UserDetails.chatWith1));
                Intent intent = new Intent(About_Seller.this, Chat.class);
                startActivity(intent);
            }
        });

        sellerName.setText(seller_name);
        sellerLocation.setText(seller_location);
        Picasso.get().load(seller_photo).into(sellerPhoto);

        getSession();
        View_Item(seller_id);
        getSold(seller_id);

        ToolbarSetting();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_seller);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean appInstalledOrNot() {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void View_Cart2(final Item_All_Details item) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART2,
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

                                if(jsonArray.length() == 0){
                                    final String strItem_Id = item.getId();
                                    final String strSeller_id = item.getSeller_id();
                                    final String strMain_category = item.getMain_category();
                                    final String strSub_category = item.getSub_category();
                                    final String strAd_Detail = item.getAd_detail();
                                    final Double strPrice = Double.valueOf(item.getPrice());
                                    final String strDivision = item.getDivision();
                                    final String strPostcode = item.getPostcode();
                                    final String strDistrict = item.getDistrict();
                                    final String strPhoto = item.getPhoto();

                                    if (getId.equals(strSeller_id)) {
                                        Toast.makeText(About_Seller.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                    } else {
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response == null) {
                                                            Log.e("onResponse", "Return NULL");
                                                        } else {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(About_Seller.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(About_Seller.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(About_Seller.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                            //End


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }) {
                                            @SuppressLint("DefaultLocale")
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("customer_id", getId);
                                                params.put("main_category", strMain_category);
                                                params.put("sub_category", strSub_category);
                                                params.put("ad_detail", strAd_Detail);
                                                params.put("price", String.format("%.2f", strPrice));
                                                params.put("division", strDivision);
                                                params.put("postcode", strPostcode);
                                                params.put("district", strDistrict);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                params.put("item_id", strItem_Id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(About_Seller.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                }

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString("item_id");

                                        Toast.makeText(About_Seller.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

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
                params.put("item_id", item.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void View_Item(final String seller_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                final JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String id = object.getString("id").trim();
                                        String seller_id = object.getString("user_id").trim();
                                        String main_category = object.getString("main_category").trim();
                                        String sub_category = object.getString("sub_category").trim();
                                        String ad_detail = object.getString("ad_detail").trim();
                                        String price = object.getString("price").trim();
                                        String division = object.getString("division");
                                        String postcode = object.getString("postcode");
                                        String district = object.getString("district");
                                        String image_item = object.getString("photo");
                                        String rating = object.getString("rating");
                                        String brand = object.getString("brand_material").trim();
                                        String inner = object.getString("inner_material").trim();
                                        String stock = object.getString("stock").trim();
                                        String desc = object.getString("description").trim();

                                        Item_All_Details item = new Item_All_Details(
                                                id,
                                                seller_id,
                                                main_category,
                                                sub_category,
                                                ad_detail,
                                                price,
                                                division,
                                                district,
                                                image_item);
                                        item.setRating(rating);
                                        item.setBrand(brand);
                                        item.setInner(inner);
                                        item.setStock(stock);
                                        item.setDescription(desc);
                                        item.setPostcode(postcode);
                                        itemList.add(item);
                                    }
                                    String product = String.valueOf(itemList.size());
                                    item_text.setText(product);
                                    adapter_item = new Item_ByCategory_Adapter(itemList, About_Seller.this);
                                    gridView_item.setAdapter(adapter_item);
                                    adapter_item.setOnItemClickListener(new Item_ByCategory_Adapter.OnItemClickListener() {
                                        @Override
                                        public void onViewClick(int position) {
                                            Intent detailIntent = new Intent(About_Seller.this, View_Product.class);
                                            Item_All_Details item = itemList.get(position);

                                            detailIntent.putExtra("item_id", item.getItem_id());
                                            detailIntent.putExtra("id", item.getId());
                                            detailIntent.putExtra("user_id", item.getSeller_id());
                                            detailIntent.putExtra("main_category", item.getMain_category());
                                            detailIntent.putExtra("sub_category", item.getSub_category());
                                            detailIntent.putExtra("ad_detail", item.getAd_detail());
                                            detailIntent.putExtra("price", item.getPrice());
                                            detailIntent.putExtra("division", item.getDivision());
                                            detailIntent.putExtra("district", item.getDistrict());
                                            detailIntent.putExtra("photo", item.getPhoto());

                                            detailIntent.putExtra("brand_material", item.getBrand());
                                            detailIntent.putExtra("inner_material", item.getInner());
                                            detailIntent.putExtra("postcode", item.getPostcode());
                                            detailIntent.putExtra("stock", item.getStock());
                                            detailIntent.putExtra("description", item.getDescription());

                                            startActivity(detailIntent);
                                        }

                                        @Override
                                        public void onAddtoFavClick(int position) {
                                            Item_All_Details item = itemList.get(position);

                                            final String strItem_Id = item.getId();
                                            final String strSeller_id = item.getSeller_id();
                                            final String strMain_category = item.getMain_category();
                                            final String strSub_category = item.getSub_category();
                                            final String strAd_Detail = item.getAd_detail();
                                            final Double strPrice = Double.valueOf(item.getPrice());
                                            final String strDivision = item.getDivision();
                                            final String strPostcode = item.getPostcode();
                                            final String strDistrict = item.getDistrict();
                                            final String strPhoto = item.getPhoto();

                                            if (getId.equals(item.getSeller_id())) {
                                                Toast.makeText(About_Seller.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                            } else {
                                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                if (response == null) {
                                                                    Log.e("onResponse", "Return NULL");
                                                                } else {
                                                                    try {
                                                                        JSONObject jsonObject1 = new JSONObject(response);
                                                                        String success = jsonObject1.getString("success");

                                                                        if (success.equals("1")) {
                                                                            Toast.makeText(About_Seller.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(About_Seller.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                        Toast.makeText(About_Seller.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                        params.put("customer_id", getId);
                                                        params.put("main_category", strMain_category);
                                                        params.put("sub_category", strSub_category);
                                                        params.put("ad_detail", strAd_Detail);
                                                        params.put("price", String.format("%.2f", strPrice));
                                                        params.put("division", strDivision);
                                                        params.put("postcode", strPostcode);
                                                        params.put("district", strDistrict);
                                                        params.put("photo", strPhoto);
                                                        params.put("seller_id", strSeller_id);
                                                        params.put("item_id", strItem_Id);
                                                        return params;
                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(About_Seller.this);
                                                requestQueue.add(stringRequest1);
                                            }
                                        }

                                        @Override
                                        public void onAddtoCartClick(int position) {
                                            Item_All_Details item = itemList.get(position);

                                            View_Cart2(item);
                                        }
                                    });

                                } else {
                                    Toast.makeText(About_Seller.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            //End


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", seller_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(About_Seller.this);
        requestQueue.add(stringRequest);
    }

    private void getSold(final String seller_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_SELLER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                final JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String id = object.getString("id").trim();
                                        String seller_id = object.getString("seller_id").trim();
                                        String main_category = object.getString("main_category").trim();
                                        String sub_category = object.getString("sub_category").trim();
                                        String ad_detail = object.getString("ad_detail").trim();
                                        String price = object.getString("price").trim();
                                        String division = object.getString("division");
                                        String postcode = object.getString("postcode");
                                        String district = object.getString("district");
                                        String image_item = object.getString("photo");

                                        Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                        itemList2.add(item);
                                    }
                                    String sold = String.valueOf(itemList2.size());
                                    sold_text.setText(sold);

                                } else {
                                    Toast.makeText(About_Seller.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put("seller_id", seller_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(About_Seller.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
