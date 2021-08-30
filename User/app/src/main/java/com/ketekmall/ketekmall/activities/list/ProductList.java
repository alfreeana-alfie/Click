package com.ketekmall.ketekmall.activities.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.products.ViewProduct;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.adapters.ProductListAdapter;
import com.ketekmall.ketekmall.configs.Setup;
import com.ketekmall.ketekmall.models.Item_All_Details;
import com.ketekmall.ketekmall.models.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;

public class ProductList extends AppCompatActivity {
    private static String GET_CATEGORY = "";

    private static String ADD_TO_FAVOURITE = "";
    private static String ADD_TO_CART = "";

    private static String GET_SEARCH_CATEGORY = "";
    private static String GET_FILTER_DISTRICT = "";
    private static String GET_FILTER_DIVISION = "";
    private static String GET_SEARCH_FILTER_CATEGORY = "";
    private static String GET_SINGLE_CART_ITEM = "";

    SessionManager sessionManager;
    String getId;
    GridView gridView;
    ProductListAdapter adapter_item;
    List<Item_All_Details> itemList;

    RelativeLayout parent;
    RelativeLayout filter_layout, category_layout;
    TextView no_result;
    BottomNavigationView bottomNav;
    private Spinner spinner_division, spinner_district;
    private Button price_sortlowest, price_sorthighest, Button_Cancel, Button_Apply, Button_Filter;
    private ArrayAdapter<CharSequence> adapter_division, adapter_district;
    private ProgressBar loading;
    Setup setup;

//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        if(activity.getCurrentFocus() != null){
//            inputMethodManager.hideSoftInputFromWindow(
//                    activity.getCurrentFocus().getWindowToken(), 0);
//        }
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_listpage);

        checkLang();

        final Intent intent = getIntent();
        GET_CATEGORY = intent.getStringExtra("GET_CATEGORY");
        ADD_TO_FAVOURITE = intent.getStringExtra("ADD_TO_FAVOURITE");
        ADD_TO_CART = intent.getStringExtra("ADD_TO_CART");
        GET_SEARCH_CATEGORY = intent.getStringExtra("GET_SEARCH_CATEGORY");
        GET_FILTER_DISTRICT = intent.getStringExtra("GET_FILTER_DISTRICT");
        GET_FILTER_DIVISION = intent.getStringExtra("GET_FILTER_DIVISION");
        GET_SEARCH_FILTER_CATEGORY = intent.getStringExtra("GET_SEARCH_FILTER_CATEGORY");
        GET_SINGLE_CART_ITEM = intent.getStringExtra("GET_SINGLE_CART_ITEM");


        Declare();
        View_List();

        ToolbarSetting();
//        getSession();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ToolbarSetting() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_category);

        View view = getSupportActionBar().getCustomView();
        final EditText search_find = view.findViewById(R.id.search_find);
        final Button Button_Search = view.findViewById(R.id.btn_search);
        final Button Button_Filter = view.findViewById(R.id.btn_filter);
        final ImageButton close_search = view.findViewById(R.id.btn_close);
        ImageButton back_button = view.findViewById(R.id.back_button);

        Button_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_layout.setVisibility(View.VISIBLE);
                category_layout.setVisibility(View.GONE);
                no_result.setVisibility(View.GONE);
            }
        });

        search_find.setHint(getResources().getString(R.string.search));
        search_find.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Button_Search.setVisibility(View.GONE);
                    Button_Filter.setVisibility(View.GONE);
                    close_search.setVisibility(View.GONE);
                    Button_Search.setVisibility(View.GONE);
                } else {
                    Button_Search.setVisibility(View.VISIBLE);
                    Button_Filter.setVisibility(View.GONE);
                    close_search.setVisibility(View.VISIBLE);
                }
            }
        });
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_find.setFocusable(false);
                search_find.setFocusable(true);
                search_find.setFocusableInTouchMode(true);
                search_find.getText().clear();
                no_result.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                itemList.clear();
                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                View_List();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductList.this, Home.class);
                startActivity(intent);
            }
        });

        Button_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_result.setVisibility(View.GONE);
                Button_Search.setVisibility(View.GONE);
                Button_Filter.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                itemList.clear();
                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                final String strAd_Detail = search_find.getText().toString();
                final String strDivision = spinner_division.getSelectedItem().toString();

                if (!strAd_Detail.isEmpty() && !strDivision.equals(getResources().getString(R.string.All))) {
                    itemList.clear();
                    adapter_item = new ProductListAdapter(itemList, ProductList.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);
                    Filter_Search(strAd_Detail, strDivision);
                }
                if(!strAd_Detail.isEmpty() && strDivision.equals(getResources().getString(R.string.All))){
                    itemList.clear();
                    adapter_item = new ProductListAdapter(itemList, ProductList.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    Search(strAd_Detail);
                }


            }
        });
    }

    private void checkLang(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        String s1 = sh.getString("lang", "");

        if(s1.equals("en")){
            String languageToLoad1 = "en"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.apply();
        }else{
            String languageToLoad1 = "ms"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.apply();


        }
//        this.recreate();
    }

//    private void getSession() {
//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        getId = user.get(SessionManager.ID);
//    }

    private void Declare() {
        setup = new Setup(this);
        getId = setup.getUserId();

        loading = findViewById(R.id.loading);

        itemList = new ArrayList<>();
        gridView = findViewById(R.id.gridView_CarItem);
        filter_layout = findViewById(R.id.filter_layout);
        filter_layout.setVisibility(View.GONE);

        category_layout = findViewById(R.id.category_layout);
        category_layout.setVisibility(View.VISIBLE);

        Button_Cancel = findViewById(R.id.btn_cancel);
        Button_Apply = findViewById(R.id.btn_apply);
        Button_Filter = findViewById(R.id.btn_filter);
        Button_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_layout.setVisibility(View.VISIBLE);
                category_layout.setVisibility(View.GONE);
                no_result.setVisibility(View.GONE);
            }
        });

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(ProductList.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(ProductList.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(ProductList.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        no_result = findViewById(R.id.no_result);
        no_result.setVisibility(View.GONE);

        Button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_layout.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);
            }
        });

        Button_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);

                filter_layout.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);

                final String strDivision = spinner_division.getSelectedItem().toString();
                final String strDistrict = spinner_district.getSelectedItem().toString();

                if (strDistrict.equals(getResources().getString(R.string.All))) {
                    itemList.clear();
                    adapter_item = new ProductListAdapter(itemList, ProductList.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    Filter_Division(strDivision);
                }
                if (strDivision.equals(getResources().getString(R.string.All))) {
                    itemList.clear();
                    adapter_item = new ProductListAdapter(itemList, ProductList.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    View_List();
                }
                if(!strDivision.equals(getResources().getString(R.string.All)) && !strDistrict.equals(getResources().getString(R.string.All))){
                    itemList.clear();
                    adapter_item = new ProductListAdapter(itemList, ProductList.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    Filter_District(strDivision, strDistrict);
                }
            }
        });

        spinner_division = findViewById(R.id.spinner_division);
        spinner_district = findViewById(R.id.spinner_district);
        price_sortlowest = findViewById(R.id.price_sortlowest);
        price_sorthighest = findViewById(R.id.price_sorthighest);
        price_sorthighest.setVisibility(View.GONE);

        adapter_division = ArrayAdapter.createFromResource(ProductList.this, R.array.division, android.R.layout.simple_spinner_item);
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);

        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showResult(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        price_sortlowest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayLowest();
                price_sortlowest.setVisibility(View.GONE);
                price_sorthighest.setVisibility(View.VISIBLE);
            }
        });

        price_sorthighest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayHighest();
                price_sorthighest.setVisibility(View.GONE);
                price_sortlowest.setVisibility(View.VISIBLE);
            }
        });

        parent = findViewById(R.id.parent);
        setupUI(parent);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ProductList.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void View_Cart2(final Item_All_Details item) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SINGLE_CART_ITEM,
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
                                    final String strWeight = item.getWeight();

                                    if (getId.equals(strSeller_id)) {
                                        Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
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
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(ProductList.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                params.put("weight", strWeight);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                }

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString("item_id");
                                        Toast.makeText(ProductList.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

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

    private void Filter_Division(final String division) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_FILTER_DIVISION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loading.setVisibility(View.GONE);

                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Homepage.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("user_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();

                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();

                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String postcode = object.getString("postcode");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String image_item02 = object.getString("photo02");
                                    String image_item03 = object.getString("photo03");
                                    String image_item04 = object.getString("photo04");
                                    String image_item05 = object.getString("photo05");
                                    String rating = object.getString("rating");
                                    String weight = object.getString("weight");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ProductList.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

                                        detailIntent.putExtra("weight", item.getWeight());

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
                                        final String strWeight = item.getWeight();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, ADD_TO_FAVOURITE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {try {

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
                                                    params.put("postcode", strPostcode);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    params.put("weight", strWeight);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                Toast.makeText(ProductList.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("division", division);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
        requestQueue.add(stringRequest);
    }

    private void Filter_Search(final String ad_detail, final String division) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SEARCH_FILTER_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loading.setVisibility(View.GONE);

                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Homepage.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("user_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();

                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();

                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String postcode = object.getString("postcode");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String image_item02 = object.getString("photo02");
                                    String image_item03 = object.getString("photo03");
                                    String image_item04 = object.getString("photo04");
                                    String image_item05 = object.getString("photo05");
                                    String rating = object.getString("rating");
                                    String weight = object.getString("weight");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    item.setPhoto02(image_item02);
                                    item.setPhoto03(image_item03);
                                    item.setPhoto04(image_item04);
                                    item.setPhoto05(image_item05);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ProductList.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());

                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());
                                        detailIntent.putExtra("photo02", item.getPhoto02());
                                        detailIntent.putExtra("photo03", item.getPhoto03());
                                        detailIntent.putExtra("photo04", item.getPhoto04());
                                        detailIntent.putExtra("photo05", item.getPhoto05());
                                        detailIntent.putExtra("weight", item.getWeight());
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
                                        final String strWeight = item.getWeight();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, ADD_TO_FAVOURITE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
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


                                                            } }
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
                                                    params.put("postcode", strPostcode);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    params.put("weight", strWeight);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                Toast.makeText(ProductList.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put("ad_detail", ad_detail);
                params.put("division", division);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
        requestQueue.add(stringRequest);
    }

    private void Search(final String strAd_detail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SEARCH_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loading.setVisibility(View.GONE);

                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Homepage.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("user_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();

                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();

                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String postcode = object.getString("postcode");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String image_item02 = object.getString("photo02");
                                    String image_item03 = object.getString("photo03");
                                    String image_item04 = object.getString("photo04");
                                    String image_item05 = object.getString("photo05");
                                    String rating = object.getString("rating");
                                    String weight = object.getString("weight");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    item.setPhoto02(image_item02);
                                    item.setPhoto03(image_item03);
                                    item.setPhoto04(image_item04);
                                    item.setPhoto05(image_item05);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ProductList.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());
                                        detailIntent.putExtra("photo02", item.getPhoto02());
                                        detailIntent.putExtra("photo03", item.getPhoto03());
                                        detailIntent.putExtra("photo04", item.getPhoto04());
                                        detailIntent.putExtra("photo05", item.getPhoto05());
                                        detailIntent.putExtra("weight", item.getWeight());

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
                                        final String strWeight = item.getWeight();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, ADD_TO_FAVOURITE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
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


                                                            }   }
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
                                                    params.put("postcode", strPostcode);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    params.put("weight", strWeight);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                Toast.makeText(ProductList.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", strAd_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
        requestQueue.add(stringRequest);

    }

    private void Filter_District(final String strDivision, final String strDistrict) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_FILTER_DISTRICT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loading.setVisibility(View.GONE);

                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Homepage.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id               = object.getString("id").trim();
                                    String seller_id        = object.getString("user_id").trim();
                                    String main_category    = object.getString("main_category").trim();
                                    String sub_category     = object.getString("sub_category").trim();
                                    String ad_detail        = object.getString("ad_detail").trim();

                                    String brand            = object.getString("brand_material").trim();
                                    String inner            = object.getString("inner_material").trim();
                                    String stock            = object.getString("stock").trim();
                                    String desc             = object.getString("description").trim();

                                    String price            = object.getString("price").trim();
                                    String division         = object.getString("division");
                                    String postcode         = object.getString("postcode");
                                    String district         = object.getString("district");
                                    String image_item       = object.getString("photo");
                                    String image_item02     = object.getString("photo02");
                                    String image_item03     = object.getString("photo03");
                                    String image_item04     = object.getString("photo04");
                                    String image_item05     = object.getString("photo05");
                                    String rating           = object.getString("rating");
                                    String weight           = object.getString("weight");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    item.setPhoto02(image_item02);
                                    item.setPhoto03(image_item03);
                                    item.setPhoto04(image_item04);
                                    item.setPhoto05(image_item05);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ProductList.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());
                                        detailIntent.putExtra("photo02", item.getPhoto02());
                                        detailIntent.putExtra("photo03", item.getPhoto03());
                                        detailIntent.putExtra("photo04", item.getPhoto04());
                                        detailIntent.putExtra("photo05", item.getPhoto05());
                                        detailIntent.putExtra("weight", item.getWeight());

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
                                        final String strWeight = item.getWeight();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, ADD_TO_FAVOURITE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                    params.put("weight", strWeight);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                Toast.makeText(ProductList.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("division", strDivision);
                params.put("district", strDistrict);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
        requestQueue.add(stringRequest);
    }

    private void showResult(int position) {
        switch (position) {
            case 0:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.all, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 1:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.kuching, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 2:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.samarahan, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 3:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.serian, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 4:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.sri_aman, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 5:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.betong, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 6:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.sarikei, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 7:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.sibu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 8:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.mukah, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 9:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.bintulu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 10:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.kapit, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 11:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.miri, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 12:
                adapter_district = ArrayAdapter.createFromResource(this, R.array.limbang, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;
        }
    }

    private void View_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("user_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();

                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();

                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String district = object.getString("district");
                                    String postcode = object.getString("postcode");
                                    String image_item = object.getString("photo");
                                    String image_item02 = object.getString("photo02");
                                    String image_item03 = object.getString("photo03");
                                    String image_item04 = object.getString("photo04");
                                    String image_item05 = object.getString("photo05");
                                    String rating = object.getString("rating");
                                    String weight = object.getString("weight");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    item.setPhoto02(image_item02);
                                    item.setPhoto03(image_item03);
                                    item.setPhoto04(image_item04);
                                    item.setPhoto05(image_item05);
                                    itemList.add(item);

                                    Log.i("PHOTO CATEGORY", image_item02);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new ProductListAdapter(itemList, ProductList.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(ProductList.this, ViewProduct.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("user_id", item.getSeller_id());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());

                                        detailIntent.putExtra("brand_material", item.getBrand());
                                        detailIntent.putExtra("inner_material", item.getInner());
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());
                                        detailIntent.putExtra("photo02", item.getPhoto02());
                                        detailIntent.putExtra("photo03", item.getPhoto03());
                                        detailIntent.putExtra("photo04", item.getPhoto04());
                                        detailIntent.putExtra("photo05", item.getPhoto05());
                                        detailIntent.putExtra("weight", item.getWeight());
                                        Log.i("PHOTO GOTO VIEW", item.getPhoto02());
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
                                        final String strWeight = item.getWeight();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(ProductList.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, ADD_TO_FAVOURITE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(ProductList.this, R.string.added_to_like, Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ProductList.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                    params.put("weight", strWeight);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                Toast.makeText(ProductList.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductList.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductList.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
