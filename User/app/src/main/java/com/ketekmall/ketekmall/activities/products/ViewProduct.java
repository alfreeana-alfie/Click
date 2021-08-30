package com.ketekmall.ketekmall.activities.products;

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
import com.ketekmall.ketekmall.activities.chats.ChatRoom;
import com.ketekmall.ketekmall.activities.list.SellerShopList;
import com.ketekmall.ketekmall.adapters.HomePageListAdapter;
import com.ketekmall.ketekmall.adapters.PageAdapter;
import com.ketekmall.ketekmall.configs.Setup;
import com.ketekmall.ketekmall.models.Item_All_Details;
import com.ketekmall.ketekmall.models.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
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

import static com.ketekmall.ketekmall.configs.Link.*;
import static com.ketekmall.ketekmall.configs.Constant.*;

public class ViewProduct extends AppCompatActivity {

    String id, userid, ad_detail, division, district, strMain_category, strSub_category, strPrice, photo, getId,
            brand, inner, stock, desc, postcode, weight;
    String photo02, photo03, photo04, photo05;
    HomePageListAdapter adapter_item;
    List<Item_All_Details> itemList, itemList2;
    SessionManager sessionManager;
    RelativeLayout review11;
    BottomNavigationView bottomNav;
    Float ratingfull, ratingfull2;
    private ImageView img_item, seller_image, image20, image21;
    private TextView ad_detail_item, price_item, sold_text, detail_info,
            seller_name, seller_location, view_all, customer_name1, customer_name2,
            btn_view_all_review, review1, review2, no_review, Page_Text, ratingText;
    private Button add_to_cart_btn, btn_view_seller;
    private ImageButton btn_chat, btn_chat_wsp;
    private TwoWayGridView gridView_item;
    private RatingBar ratingBar, ratingBar20, ratingBar21;

    ViewPager viewPager;
    PageAdapter pageAdapter;

    List<String>  photoList = new ArrayList<String>();
    Setup setup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product);

        ratingfull = 0.0F;
        ratingfull2 = 0.0F;
        ratingText = findViewById(R.id.rating);

        setup = new Setup(this);
        getId = setup.getUserId();

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
        btn_view_all_review.setVisibility(View.INVISIBLE);

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
                        Intent intent4 = new Intent(ViewProduct.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(ViewProduct.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(ViewProduct.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        final Intent intent = getIntent();
        id = intent.getStringExtra(sID);
        userid = intent.getStringExtra(sUSER_ID);
        strMain_category = intent.getStringExtra(sMAIN_CATEGORY);
        strSub_category = intent.getStringExtra(sSUB_CATEGORY);
        ad_detail = intent.getStringExtra(sAD_DETAIL);

        brand = intent.getStringExtra(sBRAND_MAT);
        inner = intent.getStringExtra(sINNER_MAT);
        stock = intent.getStringExtra(sSTOCK);
        desc = intent.getStringExtra(sDESCRIPTION);

        strPrice = intent.getStringExtra(sPRICE);
        division = intent.getStringExtra(sDIVISION);
        postcode = intent.getStringExtra(sPOSTCODE);
        district = intent.getStringExtra(sDISTRICT);
        photo = intent.getStringExtra(sPHOTO);
        photo02 = intent.getStringExtra(sPHOTO02);
        photo03 = intent.getStringExtra(sPHOTO03);
        photo04 = intent.getStringExtra(sPHOTO04);
        photo05 = intent.getStringExtra(sPHOTO05);
        weight = intent.getStringExtra(sWEIGHT);

        // List of photos
        photoList.add(photo);
        if(!photo02.equals(sNULL)){
            photoList.add(photo02);
        }
        if(!photo03.equals(sNULL)){
            photoList.add(photo03);
        }
        if(!photo04.equals(sNULL)){
            photoList.add(photo04);
        }
        if(!photo05.equals(sNULL)){
            photoList.add(photo05);
        }

        String Price_Text = "RM" + strPrice;

        ad_detail_item.setText(ad_detail);
        price_item.setText(Price_Text);
//        Picasso.get().load(photo).into(img_item);

        //Review
        Read_Review(id);

        Picasso.get().load(IMAGE_DEFAULT).into(image20);
        Picasso.get().load(IMAGE_DEFAULT).into(image21);

        getUserDetail();
        View_Item();
        getSold();
        ViewPhoto();
//        View_Photo();

        detail_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewProduct.this, ViewMoreDetails.class);

                final Intent intent4 = getIntent();
                String id1 = intent4.getStringExtra(sID);
                String stock = intent4.getStringExtra(sSTOCK);
                String brand = intent4.getStringExtra(sBRAND_MAT);
                String inner = intent4.getStringExtra(sINNER_MAT);
                String desc = intent4.getStringExtra(sDESCRIPTION);
                String division = intent4.getStringExtra(sDIVISION);
                String district = intent4.getStringExtra(sDISTRICT);

                String userid1 = intent4.getStringExtra(sUSER_ID);
                String strMain_category1 = intent4.getStringExtra(sMAIN_CATEGORY);
                String strSub_category1 = intent4.getStringExtra(sSUB_CATEGORY);
                String ad_detail1 = intent4.getStringExtra(sAD_DETAIL);
                String strPrice1 = intent4.getStringExtra(sPRICE);
                String division1 = intent4.getStringExtra(sDIVISION);
                String district1 = intent4.getStringExtra(sDISTRICT);
                String photo1 = intent4.getStringExtra(sPHOTO);
                String photo2 = intent4.getStringExtra(sPHOTO02);
                String photo3 = intent4.getStringExtra(sPHOTO03);
                String photo4 = intent4.getStringExtra(sPHOTO04);
                String photo5 = intent4.getStringExtra(sPHOTO05);
                String item_id = intent4.getStringExtra(sITEM_ID);
                String postcode = intent4.getStringExtra(sPOSTCODE);

                intent1.putExtra(sITEM_ID, item_id);
                intent1.putExtra(sID, id1);
                intent1.putExtra(sUSER_ID, userid1);
                intent1.putExtra(sMAIN_CATEGORY, strMain_category1);
                intent1.putExtra(sSUB_CATEGORY, strSub_category1);
                intent1.putExtra(sAD_DETAIL, ad_detail1);
                intent1.putExtra(sPRICE, strPrice1);
                intent1.putExtra(sDIVISION, division1);
                intent1.putExtra(sPHOTO, photo1);
                intent1.putExtra(sPHOTO02, photo2);
                intent1.putExtra(sPHOTO03, photo3);
                intent1.putExtra(sPHOTO04, photo4);
                intent1.putExtra(sPHOTO05, photo5);
                intent1.putExtra(sPOSTCODE, postcode);
                intent1.putExtra(sPHOTO, photo1);


                intent1.putExtra(sID, id1);
                intent1.putExtra(sSTOCK, stock);
                intent1.putExtra(sBRAND_MAT, brand);
                intent1.putExtra(sINNER_MAT, inner);
                intent1.putExtra(sDESCRIPTION, desc);
                intent1.putExtra(sDIVISION, division);
                intent1.putExtra(sDISTRICT, district);

                startActivity(intent1);
            }
        });

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getId.equals(userid)) {
                    Toast.makeText(ViewProduct.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                } else {
                    View_Cart();
                }
            }
        });

        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewProduct.this, SellerShopList.class);
                startActivity(intent1);
            }
        });

        ToolbarSetting();


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

    private void ViewPhoto(){
        String[] image = new String[photoList.size()];

        for (int i = 0; i < photoList.size(); i++) {
            image[i] = photoList.get(i);

            pageAdapter = new PageAdapter(ViewProduct.this, image);
            Log.i("PHOTO VIEW", image[i]);
        }
        viewPager.setAdapter(pageAdapter);

        Page_Text.setText("1/" + photoList.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int NEWposition = position + 1;
                if(position == 0){
                    Page_Text.setText(sONE + "/" + photoList.size());
                }else{
                    Page_Text.setText(NEWposition + "/" + photoList.size());
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void View_Cart() {
        final Intent intent = getIntent();
        id = intent.getStringExtra(sID);
        userid = intent.getStringExtra(sUSER_ID);
        strMain_category = intent.getStringExtra(sMAIN_CATEGORY);
        strSub_category = intent.getStringExtra(sSUB_CATEGORY);
        ad_detail = intent.getStringExtra(sAD_DETAIL);
        postcode = intent.getStringExtra(sPOSTCODE);

        brand = intent.getStringExtra(sBRAND_MAT);
        inner = intent.getStringExtra(sINNER_MAT);
        stock = intent.getStringExtra(sSTOCK);
        desc = intent.getStringExtra(sDESCRIPTION);

        strPrice = intent.getStringExtra(sPRICE);
        division = intent.getStringExtra(sDIVISION);
        district = intent.getStringExtra(sDISTRICT);
        photo = intent.getStringExtra(sPHOTO);
        weight = intent.getStringExtra(sWEIGHT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SINGLE_CART_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString(sSUCCESS);
                                JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                                if(jsonArray.length() == 0){
                                    Add_Cart(strMain_category, strSub_category, ad_detail, strPrice,
                                            division,postcode, district, photo, userid, id, weight);
                                }
                                if (success.equals(sONE)) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString(sITEM_ID);

                                        if(!item_id.isEmpty() && item_id.equals(id)){
                                            Toast.makeText(ViewProduct.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                        }else {
                                            Add_Cart(strMain_category, strSub_category, ad_detail, strPrice,
                                                    division,postcode, district, photo, userid, id, weight);
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
                params.put(sCUSTOMER_ID, getId);
                params.put(sITEM_ID, id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void View_Cart2(final Item_All_Details item) {
        final Intent intent = getIntent();
        id = intent.getStringExtra(sID);
        userid = intent.getStringExtra(sUSER_ID);
        strMain_category = intent.getStringExtra(sMAIN_CATEGORY);
        strSub_category = intent.getStringExtra(sSUB_CATEGORY);
        ad_detail = intent.getStringExtra(sAD_DETAIL);
        postcode = intent.getStringExtra(sPOSTCODE);

        brand = intent.getStringExtra(sBRAND_MAT);
        inner = intent.getStringExtra(sINNER_MAT);
        stock = intent.getStringExtra(sSTOCK);
        desc = intent.getStringExtra(sDESCRIPTION);

        strPrice = intent.getStringExtra(sPRICE);
        division = intent.getStringExtra(sDIVISION);
        district = intent.getStringExtra(sDISTRICT);
        photo = intent.getStringExtra(sPHOTO);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SINGLE_CART_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString(sSUCCESS);
                                JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                                if(jsonArray.length() == 0){
                                    final String strItem_Id = item.getId();
                                    final String strSeller_id = item.getSeller_id();
                                    final String strMain_category = item.getMain_category();
                                    final String strSub_category = item.getMain_category();
                                    final String strAd_Detail = item.getAd_detail();
                                    final Double strPrice = Double.valueOf(item.getPrice());
                                    final String strDivision = item.getDivision();
                                    final String strPostcode = item.getPostcode();
                                    final String strDistrict = item.getDistrict();
                                    final String strPhoto = item.getPhoto();
                                    final String strWeight = item.getWeight();

                                    Log.i("PRODUCT", strWeight);

                                    if (getId.equals(strSeller_id)) {
                                        Toast.makeText(ViewProduct.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                    } else {
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, ADD_TO_CART,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response == null) {
                                                            Log.e("onResponse", "Return NULL");
                                                        } else {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString(sSUCCESS);

                                                                if (success.equals(sONE)) {
                                                                    Toast.makeText(ViewProduct.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(ViewProduct.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ViewProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                params.put(sCUSTOMER_ID, getId);
                                                params.put(sMAIN_CATEGORY, strMain_category);
                                                params.put(sSUB_CATEGORY, strSub_category);
                                                params.put(sAD_DETAIL, strAd_Detail);
                                                params.put(sPRICE, String.format(s2DP, strPrice));
                                                params.put(sDIVISION, strDivision);
                                                params.put(sPOSTCODE, strPostcode);
                                                params.put(sDISTRICT, strDistrict);
                                                params.put(sPHOTO, strPhoto);
                                                params.put(sSELLER_ID, strSeller_id);
                                                params.put(sITEM_ID, strItem_Id);
                                                params.put(sWEIGHT, strWeight);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                }

                                if (success.equals(sONE)) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString(sITEM_ID);
                                        Toast.makeText(ViewProduct.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

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
                params.put(sCUSTOMER_ID, getId);
                params.put(sITEM_ID, item.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Add_Cart(final String Main_category, final String Sub_category,
                          final String Ad_detail, final String Price, final String Division, final String strPostcode,
                          final String District, final String Photo,
                          final String Userid, final String Id, final String Weight){
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, ADD_TO_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String success = jsonObject1.getString(sSUCCESS);

                            if (success.equals(sONE)) {
                                Toast.makeText(ViewProduct.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ViewProduct.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(sCUSTOMER_ID, getId);
                params.put(sMAIN_CATEGORY, Main_category);
                params.put(sSUB_CATEGORY, Sub_category);
                params.put(sAD_DETAIL, Ad_detail);
                params.put(sPRICE, Price);
                params.put(sDIVISION, Division);
                params.put(sPOSTCODE, strPostcode);
                params.put(sDISTRICT, District);
                params.put(sPHOTO, Photo);
                params.put(sSELLER_ID, Userid);
                params.put(sITEM_ID, Id);
                params.put(sWEIGHT, Weight);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest2);
    }

    public void getUserDetailsCustomer(final String SellerName, final String SellerID, final String SellerPhoto){
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

                                    final String strID = object.getString(sID);
                                    final String strName = object.getString(sNAME).trim();
                                    final String strEmail = object.getString(sEMAIL);
                                    final String strPhoto = object.getString(sPHOTO);
                                    final String mobile_num = object.getString(sPHONE_NO);
                                    final String strDivision = object.getString(sDIVISION).trim();

                                    btn_chat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent chatIntent = new Intent(ViewProduct.this, ChatRoom.class);
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
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sID, getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

                                    final String strID = object.getString(sID);
                                    final String strName = object.getString(sNAME).trim();
                                    final String strEmail = object.getString(sEMAIL);
                                    final String strPhoto = object.getString(sPHOTO);
                                    final String mobile_num = object.getString(sPHONE_NO);
                                    final String strDivision = object.getString(sDIVISION).trim();

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
                                                Toast.makeText(ViewProduct.this, "Not Installed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    view_all.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(ViewProduct.this, SellerShopList.class);
                                            intent1.putExtra(sID, userid);
                                            intent1.putExtra(sSELLER_NAME, strName);
                                            intent1.putExtra(sSELLER_PHOTO, strPhoto);
                                            intent1.putExtra(sSELLER_LOCATION, strDivision);
                                            intent1.putExtra(sSELLER_PHONE, mobile_num);
                                            startActivity(intent1);
                                        }
                                    });

                                    btn_view_seller.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(ViewProduct.this, SellerShopList.class);
                                            intent1.putExtra(sID, userid);
                                            intent1.putExtra(sSELLER_NAME, strName);
                                            intent1.putExtra(sSELLER_EMAIL, strEmail);
                                            intent1.putExtra(sSELLER_PHOTO, strPhoto);
                                            intent1.putExtra(sSELLER_LOCATION, strDivision);
                                            intent1.putExtra(sSELLER_PHONE, mobile_num);
                                            startActivity(intent1);
                                        }
                                    });

                                   getUserDetailsCustomer(strName, strID, strPhoto);

                                }
                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sID, userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getUserDetail_ReviewOne(final String username) {
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
                                    String strEmail = object.getString(sEMAIL).trim();
                                    final String strPhoto = object.getString(sPHOTO);
                                    final String mobile_num = object.getString(sPHONE_NO);
                                    final String strDivision = object.getString(sDIVISION).trim();

                                    review1.setVisibility(VISIBLE);
                                    review11.setVisibility(VISIBLE);
                                    no_review.setVisibility(GONE);

                                    btn_view_all_review.setVisibility(VISIBLE);
                                    customer_name1.setText(strName);
                                    customer_name2.setText(strName);
                                }
                            } else {
                                review1.setVisibility(GONE);
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sID, username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void View_Item() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SELLER_PRODUCT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            final JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                            if (success.equals(sONE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString(sID).trim();
                                    String seller_id = object.getString(sUSER_ID).trim();
                                    String main_category = object.getString(sMAIN_CATEGORY).trim();
                                    String sub_category = object.getString(sSUB_CATEGORY).trim();
                                    String ad_detail = object.getString(sAD_DETAIL).trim();
                                    String price = object.getString(sPRICE).trim();
                                    String division = object.getString(sDIVISION);
                                    String postcode = object.getString(sPOSTCODE);
                                    String district = object.getString(sDISTRICT);
                                    String image_item = object.getString(sPHOTO);
                                    String image_item2 = object.getString(sPHOTO02);
                                    String image_item3 = object.getString(sPHOTO03);
                                    String image_item4 = object.getString(sPHOTO04);
                                    String image_item5 = object.getString(sPHOTO05);
                                    String rating = object.getString(sRATING);
                                    String brand = object.getString(sBRAND_MAT).trim();
                                    String inner = object.getString(sINNER_MAT).trim();
                                    String stock = object.getString(sSTOCK).trim();
                                    String desc = object.getString(sDESCRIPTION).trim();
                                    String weight = object.getString(sWEIGHT).trim();

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setRating(rating);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    item.setPhoto02(image_item2);
                                    item.setPhoto03(image_item3);
                                    item.setPhoto04(image_item4);
                                    item.setPhoto05(image_item5);
                                    itemList.add(item);
                                }
                                adapter_item = new HomePageListAdapter(itemList);
                                adapter_item.notifyDataSetChanged();
                                gridView_item.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new HomePageListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ViewProduct.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(sITEM_ID, item.getItem_id());
                                        detailIntent.putExtra(sID, item.getId());
                                        detailIntent.putExtra(sUSER_ID, item.getSeller_id());
                                        detailIntent.putExtra(sMAIN_CATEGORY, item.getMain_category());
                                        detailIntent.putExtra(sSUB_CATEGORY, item.getSub_category());
                                        detailIntent.putExtra(sAD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(sPRICE, item.getPrice());
                                        detailIntent.putExtra(sDIVISION, item.getDivision());
                                        detailIntent.putExtra(sPOSTCODE, item.getPostcode());
                                        detailIntent.putExtra(sDISTRICT, item.getDistrict());
                                        detailIntent.putExtra(sPHOTO, item.getPhoto());
                                        detailIntent.putExtra(sPHOTO02, item.getPhoto02());
                                        detailIntent.putExtra(sPHOTO03, item.getPhoto03());
                                        detailIntent.putExtra(sPHOTO04, item.getPhoto04());
                                        detailIntent.putExtra(sPHOTO05, item.getPhoto05());

                                        detailIntent.putExtra(sBRAND_MAT, item.getBrand());
                                        detailIntent.putExtra(sINNER_MAT, item.getInner());
                                        detailIntent.putExtra(sSTOCK, item.getStock());
                                        detailIntent.putExtra(sDESCRIPTION, item.getDescription());

                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        View_Cart2(item);
                                    }
                                });

                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sUSER_ID, userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        requestQueue.add(stringRequest);
    }

    private void getSold() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PRODUCT_SOLD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            final JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                            if (success.equals(sONE)) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString(sID).trim();
                                    String seller_id = object.getString(sSELLER_ID).trim();
                                    String main_category = object.getString(sMAIN_CATEGORY).trim();
                                    String sub_category = object.getString(sSUB_CATEGORY).trim();
                                    String ad_detail = object.getString(sAD_DETAIL).trim();
                                    String price = object.getString(sPRICE).trim();
                                    String division = object.getString(sDIVISION);
                                    String postcode = object.getString(sPOSTCODE);
                                    String district = object.getString(sDISTRICT);
                                    String image_item = object.getString(sPHOTO);

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setPostcode(postcode);
                                    itemList2.add(item);
                                }
                                EditSold(id, String.valueOf(itemList2.size()));
                                String sold = String.valueOf(itemList2.size());
                                sold_text.setText(sold);

                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sSELLER_ID, userid);
                params.put(sAD_DETAIL, ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        requestQueue.add(stringRequest);
    }

    private void Read_Review(final String item_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_REVIEW_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);
                            final JSONArray jsonArray = jsonObject.getJSONArray(sREAD);

                            if (success.equals(sONE)) {
                                if(jsonArray.length() == 0){
                                    ratingBar.setVisibility(GONE);
                                    ratingText.setText("No Rating Yet");
                                }else{
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String id = object.getString(sID).trim();
                                        String seller_id = object.getString(sSELLER_ID).trim();
                                        String customer_id = object.getString(sCUSTOMER_ID).trim();
                                        String customer_name = object.getString(sCUSTOMER_NAME).trim();
                                        final String item_id = object.getString(sITEM_ID).trim();
                                        String review = object.getString(sREVIEW).trim();
                                        String rating = object.getString(sRATING);

                                        ratingBar20.setRating(Float.parseFloat(rating));
                                        ratingBar21.setRating(Float.parseFloat(rating));

                                        ratingfull += Float.parseFloat(rating) / jsonArray.length();
                                        ratingfull2 += Float.parseFloat(rating) / jsonArray.length();

                                        ratingBar.setRating(ratingfull);
                                        ratingText.setText(String.format(s1DP, ratingfull));

                                        review1.setText(review);
                                        review2.setText(review);

                                        getUserDetail_ReviewOne(customer_id);

                                        btn_view_all_review.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent1 = new Intent(ViewProduct.this, ViewProductReview.class);

                                                final Intent intent4 = getIntent();
                                                String id1 = intent4.getStringExtra(sID);
                                                String stock = intent4.getStringExtra(sSTOCK);
                                                String brand = intent4.getStringExtra(sBRAND_MAT);
                                                String inner = intent4.getStringExtra(sINNER_MAT);
                                                String desc = intent4.getStringExtra(sDESCRIPTION);
                                                String division = intent4.getStringExtra(sDIVISION);
                                                String postcode = intent4.getStringExtra(sPOSTCODE);
                                                String district = intent4.getStringExtra(sDISTRICT);

                                                String userid1 = intent4.getStringExtra(sUSER_ID);
                                                String strMain_category1 = intent4.getStringExtra(sMAIN_CATEGORY);
                                                String strSub_category1 = intent4.getStringExtra(sSUB_CATEGORY);
                                                String ad_detail1 = intent4.getStringExtra(sAD_DETAIL);
                                                String strPrice1 = intent4.getStringExtra(sPRICE);
                                                String division1 = intent4.getStringExtra(sDIVISION);
                                                String district1 = intent4.getStringExtra(sDISTRICT);
                                                String photo1 = intent4.getStringExtra(sPHOTO);
                                                String photo2 = intent4.getStringExtra(sPHOTO02);
                                                String photo3 = intent4.getStringExtra(sPHOTO03);
                                                String photo4 = intent4.getStringExtra(sPHOTO04);
                                                String photo5 = intent4.getStringExtra(sPHOTO05);

                                                intent1.putExtra(sITEM_ID, item_id);
                                                intent1.putExtra(sID, id1);
                                                intent1.putExtra(sUSER_ID, userid1);
                                                intent1.putExtra(sMAIN_CATEGORY, strMain_category1);
                                                intent1.putExtra(sSUB_CATEGORY, strSub_category1);
                                                intent1.putExtra(sAD_DETAIL, ad_detail1);
                                                intent1.putExtra(sPRICE, strPrice1);
                                                intent1.putExtra(sDIVISION, division1);
                                                intent1.putExtra(sPOSTCODE, postcode);
                                                intent1.putExtra(sDISTRICT, district1);
                                                intent1.putExtra(sPHOTO, photo1);
                                                intent1.putExtra(sPHOTO02, photo2);
                                                intent1.putExtra(sPHOTO03, photo3);
                                                intent1.putExtra(sPHOTO04, photo4);
                                                intent1.putExtra(sPHOTO05, photo5);
                                                
                                                intent1.putExtra(sID, id1);
                                                intent1.putExtra(sSTOCK, stock);
                                                intent1.putExtra(sBRAND_MAT, brand);
                                                intent1.putExtra(sINNER_MAT, inner);
                                                intent1.putExtra(sDESCRIPTION, desc);
                                                intent1.putExtra(sDIVISION, division);
                                                intent1.putExtra(sDISTRICT, district);

                                                startActivity(intent1);
                                            }
                                        });

                                    }
                                    EditRating(id, String.valueOf(ratingfull2));
                                }

                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sITEM_ID, item_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        requestQueue.add(stringRequest);
    }

    private void EditRating(final String item_id, final String rating) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);

                            if (success.equals(sONE)) {
//                                Toast.makeText(View_Item_Single.this, "Login! ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put(sID, item_id);
                params.put(sRATING, rating);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        requestQueue.add(stringRequest);
    }

    private void EditSold(final String item_id, final String sold) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PRODUCT_SOLD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString(sSUCCESS);

                            if (success.equals(sONE)) {
//                                Toast.makeText(View_Item_Single.this, "Login! ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                //Error
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sID, item_id);
                params.put(sSOLD, sold);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProduct.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
