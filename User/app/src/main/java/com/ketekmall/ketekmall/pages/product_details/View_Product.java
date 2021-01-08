package com.ketekmall.ketekmall.pages.product_details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.Item_Adapter_Main;
import com.ketekmall.ketekmall.adapter.PageAdapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.ketekmall.ketekmall.pages.navigation_items.Chat;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class View_Product extends AppCompatActivity {

    private static String URL_READALL_SELLER = "https://ketekmall.com/ketekmall/readall_seller.php";
    private static String URL_READ_SELLER = "https://ketekmall.com/ketekmall/read_order_done_seller.php";

    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_READ_REVIEW = "https://ketekmall.com/ketekmall/read_review.php";
    private static String URL_EDIT_RATING = "https://ketekmall.com/ketekmall/edit_detail_rating.php";
    private static String URL_EDIT_SOLD = "https://ketekmall.com/ketekmall/edit_detail_sold.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single.php";
    private static String URL_READ_PHOTO = "https://ketekmall.com/ketekmall/products_img/read_photo.php";
    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readcart_single.php";

    String image_default = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";


    String id, userid, ad_detail, division, district, strMain_category, strSub_category, strPrice, photo, getId, brand, inner, stock, desc, postcode;
    Item_Adapter_Main adapter_item;
    List<Item_All_Details> itemList, itemList2;
    SessionManager sessionManager;
    RelativeLayout review11;
    BottomNavigationView bottomNav;
    Float ratingfull, ratingfull2;
    private ImageView img_item, seller_image, image20, image21;
    private TextView ad_detail_item, price_item, sold_text, detail_info,
            seller_name, seller_location, view_all, customer_name1, customer_name2,
            btn_view_all_review, review1, review2, no_review, Page_Text;
    private Button add_to_cart_btn, btn_view_seller;
    private ImageButton btn_chat, btn_chat_wsp;
    private TwoWayGridView gridView_item;
    private RatingBar ratingBar, ratingBar20, ratingBar21;

    ViewPager viewPager;
    PageAdapter pageAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product);

        ratingfull = 0.0F;
        ratingfull2 = 0.0F;

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        viewPager = findViewById(R.id.view_pager);


        itemList = new ArrayList<>();
        itemList2 = new ArrayList<>();
        img_item = findViewById(R.id.img_item);
        ad_detail_item = findViewById(R.id.ad_details_item);
        price_item = findViewById(R.id.price_item);
        sold_text = findViewById(R.id.sold_text);
        detail_info = findViewById(R.id.detail_info);
        ratingBar = findViewById(R.id.ratingBar);
        Page_Text = findViewById(R.id.page_text);

        image20 = findViewById(R.id.image20);
        image21 = findViewById(R.id.image21);

        customer_name1 = findViewById(R.id.customer_name1);
        customer_name2 = findViewById(R.id.customer_name2);

        review1 = findViewById(R.id.review1);
        review2 = findViewById(R.id.review2);
        review11 = findViewById(R.id.review11);
        no_review = findViewById(R.id.no_review);
        no_review.setVisibility(VISIBLE);
        review11.setVisibility(GONE);
        review1.setVisibility(GONE);

        ratingBar20 = findViewById(R.id.ratingBar20);
        ratingBar21 = findViewById(R.id.ratingBar21);

        btn_view_all_review = findViewById(R.id.view_all_review);

        seller_name = findViewById(R.id.seller_name);
        seller_image = findViewById(R.id.image);
        seller_location = findViewById(R.id.seller_location);
        view_all = findViewById(R.id.view_all);
        btn_chat = findViewById(R.id.btn_chat);
        gridView_item = findViewById(R.id.gridView_item);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
        btn_chat_wsp = findViewById(R.id.btn_chat_wsp);
        btn_view_seller = findViewById(R.id.btn_view_seller);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(View_Product.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(View_Product.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(View_Product.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        userid = intent.getStringExtra("user_id");
        strMain_category = intent.getStringExtra("main_category");
        strSub_category = intent.getStringExtra("sub_category");
        ad_detail = intent.getStringExtra("ad_detail");

        brand = intent.getStringExtra("brand_material");
        inner = intent.getStringExtra("inner_material");
        stock = intent.getStringExtra("stock");
        desc = intent.getStringExtra("description");

        strPrice = intent.getStringExtra("price");
        division = intent.getStringExtra("division");
        postcode = intent.getStringExtra("postcode");
        district = intent.getStringExtra("district");
        photo = intent.getStringExtra("photo");

        String Price_Text = "MYR" + strPrice;

        ad_detail_item.setText(ad_detail);
        price_item.setText(Price_Text);
        Picasso.get().load(photo).into(img_item);

        //Review
        Read_Review(id);

        Picasso.get().load(image_default).into(image20);
        Picasso.get().load(image_default).into(image21);

        getUserDetail();
        View_Item();
        getSold();

        detail_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(View_Product.this, More_Details.class);

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
                String postcode = intent4.getStringExtra("postcode");

                intent1.putExtra("item_id", item_id);
                intent1.putExtra("id", id1);
                intent1.putExtra("user_id", userid1);
                intent1.putExtra("main_category", strMain_category1);
                intent1.putExtra("sub_category", strSub_category1);
                intent1.putExtra("ad_detail", ad_detail1);
                intent1.putExtra("price", strPrice1);
                intent1.putExtra("division", division1);
                intent1.putExtra("photo", photo1);
                intent1.putExtra("postcode", postcode);
                intent1.putExtra("photo", photo1);


                intent1.putExtra("id", id1);
                intent1.putExtra("stock", stock);
                intent1.putExtra("brand_material", brand);
                intent1.putExtra("inner_material", inner);
                intent1.putExtra("description", desc);
                intent1.putExtra("division", division);
                intent1.putExtra("district", district);

                startActivity(intent1);
            }
        });

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getId.equals(userid)) {
                    Toast.makeText(View_Product.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                } else {
                    View_Cart();
                }
            }
        });

        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(View_Product.this, About_Seller.class);
                startActivity(intent1);
            }
        });

        ToolbarSetting();

        View_Photo();
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_view);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void View_Photo() {
        final Intent intent = getIntent();
        ad_detail = intent.getStringExtra("ad_detail");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");
                            String[] image = new String[jsonArray.length()];
                            if(jsonArray.length() == 0 || jsonArray.length() == 1){
                                viewPager.setVisibility(GONE);
                                Page_Text.setVisibility(GONE);
                                img_item.setVisibility(VISIBLE);
                            }else{
                                viewPager.setVisibility(VISIBLE);
                                Page_Text.setVisibility(VISIBLE);
                                img_item.setVisibility(GONE);
                            }
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String image_item = object.getString("filepath");

                                    image[i] = image_item;

                                    pageAdapter = new PageAdapter(View_Product.this, image);


                                }
                                viewPager.setAdapter(pageAdapter);

                                Page_Text.setText("1/" + jsonArray.length());

                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        int NEWposition = position + 1;
                                        if(position == 0){
                                            Page_Text.setText("1/5");
                                        }else{
                                            Page_Text.setText(NEWposition + "/" + jsonArray.length());
                                        }

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });

                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    private void View_Cart() {
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        userid = intent.getStringExtra("user_id");
        strMain_category = intent.getStringExtra("main_category");
        strSub_category = intent.getStringExtra("sub_category");
        ad_detail = intent.getStringExtra("ad_detail");
        postcode = intent.getStringExtra("postcode");

        brand = intent.getStringExtra("brand_material");
        inner = intent.getStringExtra("inner_material");
        stock = intent.getStringExtra("stock");
        desc = intent.getStringExtra("description");

        strPrice = intent.getStringExtra("price");
        division = intent.getStringExtra("division");
        district = intent.getStringExtra("district");
        photo = intent.getStringExtra("photo");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART,
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
                                    Add_Cart(strMain_category, strSub_category, ad_detail, strPrice,
                                            division,postcode, district, photo, userid, id);
                                }
                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString("item_id");

                                        if(!item_id.isEmpty() && item_id.equals(id)){
                                            Toast.makeText(View_Product.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                        }else {
                                            Add_Cart(strMain_category, strSub_category, ad_detail, strPrice,
                                                    division,postcode, district, photo, userid, id);
                                        }
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
                params.put("item_id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void View_Cart2(final Item_All_Details item) {
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        userid = intent.getStringExtra("user_id");
        strMain_category = intent.getStringExtra("main_category");
        strSub_category = intent.getStringExtra("sub_category");
        ad_detail = intent.getStringExtra("ad_detail");
        postcode = intent.getStringExtra("postcode");

        brand = intent.getStringExtra("brand_material");
        inner = intent.getStringExtra("inner_material");
        stock = intent.getStringExtra("stock");
        desc = intent.getStringExtra("description");

        strPrice = intent.getStringExtra("price");
        division = intent.getStringExtra("division");
        district = intent.getStringExtra("district");
        photo = intent.getStringExtra("photo");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART,
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
                                        Toast.makeText(View_Product.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(View_Product.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(View_Product.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(View_Product.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                }

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString("item_id");

                                        if(!item_id.isEmpty() && item_id.equals(id)){
                                            Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                        }
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
                params.put("item_id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Add_Cart(final String Main_category, final String Sub_category,
                          final String Ad_detail, final String Price, final String Division, final String strPostcode,
                          final String District, final String Photo,
                          final String Userid, final String Id){
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String success = jsonObject1.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(View_Product.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(View_Product.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(View_Product.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                params.put("main_category", Main_category);
                params.put("sub_category", Sub_category);
                params.put("ad_detail", Ad_detail);
                params.put("price", Price);
                params.put("division", Division);
                params.put("postcode", strPostcode);
                params.put("district", District);
                params.put("photo", Photo);
                params.put("seller_id", Userid);
                params.put("item_id", Id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest2);
    }

    public void getUserDetailsCustomer(final String SellerName, final String SellerID, final String SellerPhoto){
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

                                    final String strID = object.getString("id");
                                    final String strName = object.getString("name").trim();
                                    final String strEmail = object.getString("email");
                                    final String strPhoto = object.getString("photo");
                                    final String mobile_num = object.getString("phone_no");
                                    final String strDivision = object.getString("division").trim();

                                    btn_chat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent chatIntent = new Intent(View_Product.this, Chat.class);
                                            chatIntent.putExtra("Name", strName);
                                            chatIntent.putExtra("UserPhoto", strPhoto);
                                            chatIntent.putExtra("ChatWith", SellerName);
                                            chatIntent.putExtra("ChatWithID", SellerID);
                                            chatIntent.putExtra("ChatWithPhoto", SellerPhoto);
//                                            String newemail1 = strEmail.substring(0, strEmail.lastIndexOf("@"));
//                                            UserDetails.chatWith = newemail1;
//                                            UserDetails.chatWith1 = strName;
//                                            Intent intent = new Intent(View_Product.this, Chat.class);
                                            startActivity(chatIntent);
                                        }
                                    });

                                }
                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Homepage.this, "JSON Parsing Eror: " + e.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

                                    final String strID = object.getString("id");
                                    final String strName = object.getString("name").trim();
                                    final String strEmail = object.getString("email");
                                    final String strPhoto = object.getString("photo");
                                    final String mobile_num = object.getString("phone_no");
                                    final String strDivision = object.getString("division").trim();

                                    Picasso.get().load(strPhoto).into(seller_image);
                                    seller_name.setText(strName);
                                    seller_location.setText(strDivision);

                                    btn_chat_wsp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            boolean installed = appInstalledOrNot("com.whatsapp");
                                            if (installed) {
                                                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                                                intent1.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+6" + mobile_num));
                                                startActivity(intent1);
                                            } else {
                                                Toast.makeText(View_Product.this, "Not Installed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    view_all.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(View_Product.this, About_Seller.class);
                                            intent1.putExtra("id", userid);
                                            intent1.putExtra("seller_name", strName);
                                            intent1.putExtra("seller_photo", strPhoto);
                                            intent1.putExtra("seller_location", strDivision);
                                            intent1.putExtra("seller_phone", mobile_num);
                                            startActivity(intent1);
                                        }
                                    });

                                    btn_view_seller.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(View_Product.this, About_Seller.class);
                                            intent1.putExtra("id", userid);
                                            intent1.putExtra("seller_name", strName);
                                            intent1.putExtra("seller_email", strEmail);
                                            intent1.putExtra("seller_photo", strPhoto);
                                            intent1.putExtra("seller_location", strDivision);
                                            intent1.putExtra("seller_phone", mobile_num);
                                            startActivity(intent1);
                                        }
                                    });

                                   getUserDetailsCustomer(strName, strID, strPhoto);

                                }
                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Homepage.this, "JSON Parsing Eror: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getUserDetail_ReviewOne(final String username) {
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
                                    String strEmail = object.getString("email").trim();
                                    final String strPhoto = object.getString("photo");
                                    final String mobile_num = object.getString("phone_no");
                                    final String strDivision = object.getString("division").trim();

                                    review1.setVisibility(VISIBLE);
                                    review11.setVisibility(VISIBLE);
                                    no_review.setVisibility(GONE);
                                    customer_name1.setText(strName);
                                    customer_name2.setText(strName);
                                }
                            } else {
                                review1.setVisibility(GONE);
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void View_Item() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READALL_SELLER,
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
                                    String postcode = object.getString("postcode");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");
                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setRating(rating);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setPostcode(postcode);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Adapter_Main(itemList);
                                adapter_item.notifyDataSetChanged();
                                gridView_item.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter_Main.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(View_Product.this, View_Product.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("item_id", item.getItem_id());
                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());
                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        View_Cart2(item);
                                    }
                                });

                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    private void getSold() {
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
                                    String postcode = object.getString("postcode");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setPostcode(postcode);
                                    itemList2.add(item);
                                }
                                EditSold(id, String.valueOf(itemList2.size()));
                                String sold = String.valueOf(itemList2.size());
                                sold_text.setText(sold);

                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", userid);
                params.put("ad_detail", ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    private void Read_Review(final String item_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_REVIEW,
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
                                    String customer_id = object.getString("customer_id").trim();
                                    String customer_name = object.getString("customer_name").trim();
                                    final String item_id = object.getString("item_id").trim();
                                    String review = object.getString("review").trim();
                                    String rating = object.getString("rating").trim();

                                    ratingBar20.setRating(Float.parseFloat(rating));
                                    ratingBar21.setRating(Float.parseFloat(rating));

                                    ratingfull += Float.parseFloat(rating) / jsonArray.length();
                                    ratingfull2 += Float.parseFloat(rating) / jsonArray.length();

                                    ratingBar.setRating(ratingfull);

                                    review1.setText(review);
                                    review2.setText(review);

                                    getUserDetail_ReviewOne(customer_id);

                                    btn_view_all_review.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(View_Product.this, Review_Info.class);

                                            final Intent intent4 = getIntent();
                                            String id1 = intent4.getStringExtra("id");
                                            String stock = intent4.getStringExtra("stock");
                                            String brand = intent4.getStringExtra("brand_material");
                                            String inner = intent4.getStringExtra("inner_material");
                                            String desc = intent4.getStringExtra("description");
                                            String division = intent4.getStringExtra("division");
                                            String postcode = intent4.getStringExtra("postcode");
                                            String district = intent4.getStringExtra("district");

                                            String userid1 = intent4.getStringExtra("user_id");
                                            String strMain_category1 = intent4.getStringExtra("main_category");
                                            String strSub_category1 = intent4.getStringExtra("sub_category");
                                            String ad_detail1 = intent4.getStringExtra("ad_detail");
                                            String strPrice1 = intent4.getStringExtra("price");
                                            String division1 = intent4.getStringExtra("division");
                                            String district1 = intent4.getStringExtra("district");
                                            String photo1 = intent4.getStringExtra("photo");

                                            intent1.putExtra("item_id", item_id);
                                            intent1.putExtra("id", id1);
                                            intent1.putExtra("user_id", userid1);
                                            intent1.putExtra("main_category", strMain_category1);
                                            intent1.putExtra("sub_category", strSub_category1);
                                            intent1.putExtra("ad_detail", ad_detail1);
                                            intent1.putExtra("price", strPrice1);
                                            intent1.putExtra("division", division1);
                                            intent1.putExtra("postcode", postcode);
                                            intent1.putExtra("district", district1);
                                            intent1.putExtra("photo", photo1);


                                            intent1.putExtra("id", id1);
                                            intent1.putExtra("stock", stock);
                                            intent1.putExtra("brand_material", brand);
                                            intent1.putExtra("inner_material", inner);
                                            intent1.putExtra("description", desc);
                                            intent1.putExtra("division", division);
                                            intent1.putExtra("district", district);

                                            startActivity(intent1);
                                        }
                                    });

                                }

                                EditRating(id, String.valueOf(ratingfull2));
                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", item_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    private void EditRating(final String item_id, final String rating) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                Toast.makeText(View_Item_Single.this, "Login! ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", item_id);
                params.put("rating", rating);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    private void EditSold(final String item_id, final String sold) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_SOLD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                Toast.makeText(View_Item_Single.this, "Login! ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(View_Product.this, R.string.failed, Toast.LENGTH_SHORT).show();
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

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", item_id);
                params.put("sold", sold);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Product.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
