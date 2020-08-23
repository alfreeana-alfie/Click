package com.example.click;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.adapter.Item_Adapter;
import com.example.click.category.Agriculture;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;
import com.example.click.data.UserDetails;
import com.example.click.pages.Chat;
import com.example.click.pages.Find_My_Items_Other;
import com.example.click.pages.Homepage;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seller_Shop extends AppCompatActivity {

    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readall_seller.php";
    private static String URL_READ_SELLER = "https://ketekmall.com/ketekmall/read_order_done_seller_shop.php";
    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";

    SessionManager sessionManager;
    String getId;
    ImageView sellerPhoto;
    TextView sellerName, sellerLocation, item_text, sold_text;

    Item_Adapter adapter_item;
    List<Item_All_Details> itemList, itemList2;
    private GridView gridView_item;
    BottomNavigationView bottomNav;
    ImageButton btn_chat_wsp, btn_chat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_seller);

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
                        Intent intent4 = new Intent(Seller_Shop.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(Seller_Shop.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Seller_Shop.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Seller_Shop.this, Profile_Page.class);
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

                boolean installed = appInstalledOrNot("com.whatsapp");
                if(installed){
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+6"+ seller_phone));
                    startActivity(intent1);
                }else {
                    Toast.makeText(Seller_Shop.this, "Not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newemail1 = seller_email.substring(0, seller_email.lastIndexOf("@"));
                UserDetails.chatWith = newemail1;
                UserDetails.chatWith1 = seller_name;
                Intent intent = new Intent(Seller_Shop.this, Chat.class);
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

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.seller_actionbar);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
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


    private void View_Item(final String seller_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                String product = String.valueOf(itemList.size());
                                item_text.setText(product);
                                adapter_item = new Item_Adapter(itemList, Seller_Shop.this);
                                gridView_item.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent intent1 = new Intent(Seller_Shop.this, View_Item_Single.class);

                                        final Intent intent4 = getIntent();
                                        String id1 = intent4.getStringExtra("id");
                                        String stock = intent4.getStringExtra("stock");
                                        String brand = intent4.getStringExtra("brand_material");
                                        String inner = intent4.getStringExtra("inner_material");
                                        String desc = intent4.getStringExtra("description");
                                        String division = intent4.getStringExtra("division");
                                        String district = intent4.getStringExtra("district");

                                        String userid1 = intent4.getStringExtra("user_id");
                                        String strMain_category1 = intent4.getStringExtra("main_category");
                                        String strSub_category1 = intent4.getStringExtra("sub_category");
                                        String ad_detail1 = intent4.getStringExtra("ad_detail");
                                        String strPrice1 = intent4.getStringExtra("price");
                                        String division1 = intent4.getStringExtra("division");
                                        String district1 = intent4.getStringExtra("district");
                                        String photo1 = intent4.getStringExtra("photo");
                                        String item_id = intent4.getStringExtra("item_id");

                                        intent1.putExtra("item_id", item_id);
                                        intent1.putExtra("id", id1);
                                        intent1.putExtra("user_id", userid1);
                                        intent1.putExtra("main_category", strMain_category1);
                                        intent1.putExtra("sub_category", strSub_category1);
                                        intent1.putExtra("ad_detail", ad_detail1);
                                        intent1.putExtra("price", strPrice1);
                                        intent1.putExtra("division", division1);
                                        intent1.putExtra("district", district1);
                                        intent1.putExtra("photo", photo1);

                                        intent1.putExtra("id", id1);
                                        intent1.putExtra("stock", stock);
                                        intent1.putExtra("brand_material", brand);
                                        intent1.putExtra("inner_material", inner);
                                        intent1.putExtra("description", desc);
                                        intent1.putExtra("division", division);
                                        intent1.putExtra("district", district);

                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Seller_Shop.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Seller_Shop.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Seller_Shop.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Seller_Shop.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("customer_id", getId);
                                                    params.put("main_category", strMain_category);
                                                    params.put("sub_category", strSub_category);
                                                    params.put("ad_detail", strAd_Detail);
                                                    params.put("price", String.format("%.2f", strPrice));
                                                    params.put("division", strDivision);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(Seller_Shop.this);
                                            requestQueue.add(stringRequest1);
                                        }
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strItem_Id = item.getId();
                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strDivision = item.getDivision();
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(strSeller_id)) {
                                            Toast.makeText(Seller_Shop.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Seller_Shop.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Seller_Shop.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Seller_Shop.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("customer_id", getId);
                                                    params.put("main_category", strMain_category);
                                                    params.put("sub_category", strSub_category);
                                                    params.put("ad_detail", strAd_Detail);
                                                    params.put("price", String.format("%.2f", strPrice));
                                                    params.put("division", strDivision);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(Seller_Shop.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(Seller_Shop.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("user_id", seller_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Seller_Shop.this);
        requestQueue.add(stringRequest);
    }

    private void getSold(final String seller_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_SELLER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList2.add(item);
                                }
                                String sold = String.valueOf(itemList2.size());
//                                sold_text.setText(sold);

                            } else {
                                Toast.makeText(Seller_Shop.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("seller_id", seller_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Seller_Shop.this);
        requestQueue.add(stringRequest);
    }

}
