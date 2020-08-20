package com.example.click.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.example.click.Feed_page;
import com.example.click.Noti_Page;
import com.example.click.R;
import com.example.click.View_Item_Single;
import com.example.click.adapter.Item_Adapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;
import com.example.click.pages.Find_My_Items_Other;
import com.example.click.pages.Homepage;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agriculture extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/category/read_agri.php";

    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";

    private static String URL_SEARCH = "https://ketekmall.com/ketekmall/search/read_agri.php";
    private static String URL_FILTER_DISTRICT = "https://ketekmall.com/ketekmall/filter_district/read_agri.php";
    private static String URL_FILTER_DIVISION = "https://ketekmall.com/ketekmall/filter_division/read_agri.php";
    private static String URL_FILTER_SEARCH = "https://ketekmall.com/ketekmall/filter_search_division/read_agri.php";

    SessionManager sessionManager;
    String getId;
    GridView gridView;
    Item_Adapter adapter_item;
    List<Item_All_Details> itemList;

    RelativeLayout filter_layout, category_layout;
    TextView no_result;
    private Spinner spinner_division, spinner_district;
    private Button price_sortlowest, price_sorthighest, Button_Cancel, Button_Apply, Button_Filter;
    private ArrayAdapter<CharSequence> adapter_division, adapter_district;

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_car);
        Declare();
        View_List();

        ToolbarSetting();
        getSession();
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.category_actionbar);

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

        search_find.setHint("Search");
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
                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                View_List();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agriculture.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                final String strAd_Detail = search_find.getText().toString();
                final String strDivision = spinner_division.getSelectedItem().toString();

                if (!strAd_Detail.isEmpty() && !strDivision.equals("All")) {
                    itemList.clear();
                    adapter_item = new Item_Adapter(itemList, Agriculture.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);
                    Filter_Search(strAd_Detail, strDivision);
                }
                if(!strAd_Detail.isEmpty() && strDivision.equals("All")){
                    itemList.clear();
                    adapter_item = new Item_Adapter(itemList, Agriculture.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    Search(strAd_Detail);
                }


            }
        });
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Declare() {
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
                        Intent intent4 = new Intent(Agriculture.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_feed:
                        Intent intent5 = new Intent(Agriculture.this, Feed_page.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent5);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Agriculture.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Agriculture.this, Edit_Profile.class);
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
                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);

                filter_layout.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);

                final String strDivision = spinner_division.getSelectedItem().toString();
                final String strDistrict = spinner_district.getSelectedItem().toString();

                if (strDistrict.equals("All")) {
                    itemList.clear();
                    adapter_item = new Item_Adapter(itemList, Agriculture.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    Filter_Division(strDivision);
                }
                if (strDivision.equals("All")) {
                    itemList.clear();
                    adapter_item = new Item_Adapter(itemList, Agriculture.this);
                    adapter_item.notifyDataSetChanged();
                    gridView.setAdapter(adapter_item);

                    View_List();
                }
                if(!strDivision.equals("All") && !strDistrict.equals("All")){
                    itemList.clear();
                    adapter_item = new Item_Adapter(itemList, Agriculture.this);
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

        adapter_division = ArrayAdapter.createFromResource(Agriculture.this, R.array.division, android.R.layout.simple_spinner_item);
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
    }

    private void Filter_Division(final String division) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILTER_DIVISION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Agriculture.this, View_Item_Single.class);
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
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Agriculture.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("division", division);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
        requestQueue.add(stringRequest);
    }

    private void Filter_Search(final String ad_detail, final String division) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILTER_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Agriculture.this, View_Item_Single.class);
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
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Agriculture.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("ad_detail", ad_detail);
                params.put("division", division);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
        requestQueue.add(stringRequest);
    }

    private void Search(final String strAd_detail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Agriculture.this, View_Item_Single.class);
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
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Agriculture.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", strAd_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
        requestQueue.add(stringRequest);

    }

    private void Filter_District(final String strDivision, final String strDistrict) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILTER_DISTRICT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Agriculture.this, View_Item_Single.class);
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
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Agriculture.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("division", strDivision);
                params.put("district", strDistrict);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new Item_Adapter(itemList, Agriculture.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Agriculture.this, View_Item_Single.class);
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
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());

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
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if (getId.equals(item.getSeller_id())) {
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
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
                                            Toast.makeText(Agriculture.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(Agriculture.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Agriculture.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Agriculture.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
                                            requestQueue.add(stringRequest2);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Agriculture.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Agriculture.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Agriculture.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
