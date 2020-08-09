package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Item_Adapter.OnItemClickListener {

    public static final String ID = "id";
    public static final String USERID = "userid";
    public static final String MAIN_CATE = "main_category";
    public static final String SUB_CATE = "sub_category";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String DISTRICT = "district";
    public static final String DIVISION = "division";
    public static final String PHOTO = "photo";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_VIEW = "https://ketekmall.com/ketekmall/category/readall.php";
    private static String URL_ADD = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";

    List<Item_All_Details> itemList;
    Item_Adapter adapter_item;


    CartAdapter _cart_adapter;
    RecyclerView recyclerView;
    ArrayList<Item_All_Details> itemAllDetailsArrayList;


    String getId;
    SessionManager sessionManager;
    TextView textCartItemCount;
    int mCartItemCount;

    private SearchView searchView;
    private ScrollView scrollView;
    private Button Button_SellItem, Button_FindItem, button_cars, button_sales, button_camera,
            button_car_parts, button_business, button_computer, button_electronics, button_furniture,
            button_handcraft, button_home, button_men, button_mom, button_motorcycle,
            button_pets, button_rent, button_services, button_sport, button_travel,
            button_women, button_food, button_grocery;
    private GridView gridViewSearch;
    private CircleImageView profile_display, profile_image;
    private TextView name_display, email_display, button_view_all, username;
    private DrawerLayout drawer;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        Declare();

        _cart_adapter = new CartAdapter(this, itemList);
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        getUserDetail();

        Category_Func();

        View_Item();
        Cart_Item();
    }

    private void Declare() {
        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.cart_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Homepage.this));
        itemAllDetailsArrayList = new ArrayList<>();

        gridViewSearch = findViewById(R.id.gridView_itemSearch);
        view = findViewById(R.id.support_layout);
        scrollView = findViewById(R.id.grid_category);
        searchView = findViewById(R.id.search_find);
        Button_SellItem = findViewById(R.id.button_sellItem);
        Button_FindItem = findViewById(R.id.button_FindItem);
        button_business = findViewById(R.id.button_business);
        button_camera = findViewById(R.id.button_camera);
        button_car_parts = findViewById(R.id.button_car_parts);
        button_cars = findViewById(R.id.button_cars);
        button_computer = findViewById(R.id.button_computer);
        button_electronics = findViewById(R.id.button_elect);
        button_furniture = findViewById(R.id.button_furniture);
        button_handcraft = findViewById(R.id.button_handcraft);
        button_home = findViewById(R.id.button_home);
        button_men = findViewById(R.id.button_men);
        button_mom = findViewById(R.id.button_mom);
        button_motorcycle = findViewById(R.id.button_motors);
        button_pets = findViewById(R.id.button_pets);
        button_rent = findViewById(R.id.button_rent);
        button_sales = findViewById(R.id.button_sales);
        button_services = findViewById(R.id.button_services);
        button_sport = findViewById(R.id.button_sports);
        button_travel = findViewById(R.id.button_travel);
        button_women = findViewById(R.id.button_women);
        button_food = findViewById(R.id.button_food);
        button_grocery = findViewById(R.id.button_grocery);
        button_view_all = findViewById(R.id.button_see);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        gridViewSearch.setVisibility(View.GONE);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        profile_display = headerView.findViewById(R.id.profile_display);
        profile_display.setBorderWidth(1);
        name_display = headerView.findViewById(R.id.name_display);
        email_display = headerView.findViewById(R.id.email_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void View_Item() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
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
                                    String seller_id = object.getString("userid").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList.add(item);

                                }
                                adapter_item = new Item_Adapter(itemList, Homepage.this);
                                adapter_item.notifyDataSetChanged();
                                gridViewSearch.invalidateViews();
                                gridViewSearch.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(USERID, item.getSeller_id());
                                        detailIntent.putExtra(MAIN_CATE, item.getMain_category());
                                        detailIntent.putExtra(SUB_CATE, item.getSub_category());
                                        detailIntent.putExtra(AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(PRICE, item.getPrice());
                                        detailIntent.putExtra(DIVISION, item.getDivision());
                                        detailIntent.putExtra(DISTRICT, item.getDistrict());
                                        detailIntent.putExtra(PHOTO, item.getPhoto());

                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoFavClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strDivision = item.getDivision();
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if (success.equals("1")) {
                                                                Toast.makeText(Homepage.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(Homepage.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Homepage.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
                                        requestQueue.add(stringRequest1);
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strDivision = item.getDivision();
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if (success.equals("1")) {
                                                                Toast.makeText(Homepage.this, "Add To Cart", Toast.LENGTH_SHORT).show();
                                                                setupBadge();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(Homepage.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Homepage.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                });
                            } else {
                                Toast.makeText(Homepage.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Homepage.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if (error.getMessage() == null) {
//                            Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Homepage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Cart_Item() {
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
                                    String seller_id = object.getString("customer_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemAllDetailsArrayList.add(item);
                                    mCartItemCount = itemAllDetailsArrayList.size();

                                }
                                setupBadge();
                                _cart_adapter.notifyDataSetChanged();
                                _cart_adapter = new CartAdapter(Homepage.this, itemAllDetailsArrayList);
                                recyclerView.setAdapter(_cart_adapter);
                                _cart_adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                                    @Override
                                    public void onDeleteClick(final int position) {
                                    }

                                });

                            }
                            _cart_adapter.notifyDataSetChanged();
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
                params.put("customer_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Category_Func() {

        Button_SellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Sell_Items()).addToBackStack(null).commit();
            }
        });

        searchView.setVisibility(View.GONE);
        Button_FindItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new All()).addToBackStack(null).commit();
            }
        });

        button_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Business()).addToBackStack(null).commit();
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Camera()).addToBackStack(null).commit();
            }
        });

        button_car_parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Car_Accessories()).addToBackStack(null).commit();
            }
        });

        button_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Cars()).addToBackStack(null).commit();
            }
        });

        button_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Computer()).addToBackStack(null).commit();
            }
        });

        button_electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Electronics()).addToBackStack(null).commit();
            }
        });

        button_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Furniture()).addToBackStack(null).commit();
            }
        });

        button_handcraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Handcraft()).addToBackStack(null).commit();
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Home_Appliances()).addToBackStack(null).commit();
            }
        });

        button_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Men()).addToBackStack(null).commit();
            }
        });

        button_mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Mom()).addToBackStack(null).commit();
            }
        });

        button_motorcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Motorcycles()).addToBackStack(null).commit();
            }
        });

        button_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Pets()).addToBackStack(null).commit();
            }
        });

        button_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new House_Rent()).addToBackStack(null).commit();
            }
        });

        button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new House_Sales()).addToBackStack(null).commit();
            }
        });

        button_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Services()).addToBackStack(null).commit();
            }
        });

        button_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Sports()).addToBackStack(null).commit();
            }
        });

        button_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Travel()).addToBackStack(null).commit();
            }
        });

        button_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Women()).addToBackStack(null).commit();
            }
        });

        button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Food()).addToBackStack(null).commit();
            }
        });

        button_grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Grocery()).addToBackStack(null).commit();
            }
        });

        button_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new All()).addToBackStack(null).commit();
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
                                    String strEmail = object.getString("email").trim();
                                    String strPhoto = object.getString("photo");

                                    name_display.setText(strName);
                                    email_display.setText(strEmail);

                                    Picasso.get().load(strPhoto).into(profile_display);

                                    username.setText(strName);
                                    Picasso.get().load(strPhoto).into(profile_image);
                                }
                            } else {
                                Toast.makeText(Homepage.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                view.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Empty()).commit();
                Toast.makeText(this, "Homepage", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sell:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Sell_Items()).addToBackStack(null).commit();
                Toast.makeText(this, "Sell My Items", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Find_My_Items()).addToBackStack(null).commit();
                Toast.makeText(this, "Find My Items", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_ads:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new My_Orders()).addToBackStack(null).commit();
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_chat_inbox:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Chat_Inbox()).addToBackStack(null).commit();
                Toast.makeText(this, "My Chat Inbox", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_favourite_ads:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Saved_Searches()).addToBackStack(null).commit();
                Toast.makeText(this, "My Favourite Ads", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_saved_searches:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_about_the_apps:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_log_out:
                disconnectFromFacebook();
                sessionManager.logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(itemAllDetailsArrayList.size()));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                view.setVisibility(View.GONE);
                Intent intent1 = new Intent(Homepage.this, Edit_Profile.class);
                startActivity(intent1);
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings is Clicked!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_cart:
                Intent intent = new Intent(Homepage.this, Cart.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            sessionManager.logout();
        } else {
            getSupportFragmentManager().popBackStack();
            view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onViewClick(int position) {

    }

    @Override
    public void onAddtoFavClick(int position) {

    }

    @Override
    public void onAddtoCartClick(int position) {

    }

}
