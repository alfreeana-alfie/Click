package com.example.click;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_All_View extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Item_Adapter_All_View.OnItemClickListener {

    public static final String ID = "id";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String ITEM_LOCATION = "item_location";
    public static final String PHOTO = "photo";
    private static String URL_READ = "https://annkalina53.000webhostapp.com/android_register_login/read_detail.php";
    private static String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/category/readall.php";
    private static String URL_ADD = "https://annkalina53.000webhostapp.com/android_register_login/add_to_fav.php";
    private static String URL_ADD_CART = "https://annkalina53.000webhostapp.com/android_register_login/add_to_cart.php";

    List<Item_All_Details> itemList;
    Item_Adapter_All_View adapter_item;
    String getId;
    SessionManager sessionManager;

    SearchView searchView;
    private ScrollView scrollView;
    private Button Button_SellItem, Button_FindItem, button_cars, button_sales, button_camera,
            button_car_parts, button_business, button_computer, button_electronics, button_furniture,
            button_handcraft, button_home, button_men, button_mom, button_motorcycle,
            button_pets, button_rent, button_services,button_sport, button_travel,
            button_women, button_food,button_grocery, button_see_all;
    private GridView gridViewSearch;
    private CircleImageView profile_display;
    private TextView name_display, email_display;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Declare();

        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        getUserDetail();

        Category_Func();

        View_Item();
    }

    private void Declare() {
        itemList = new ArrayList<>();
        gridViewSearch = findViewById(R.id.gridView_itemSearch);
        view = findViewById(R.id.support_layout);
        scrollView = findViewById(R.id.grid_category);
        searchView = findViewById(R.id.search_find);
        gridViewSearch.setVisibility(View.GONE);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        profile_display = headerView.findViewById(R.id.profile_display);
        profile_display.setBorderWidth(1);
        name_display = headerView.findViewById(R.id.name_display);
        email_display = headerView.findViewById(R.id.email_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CLICK");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        button_see_all = findViewById(R.id.button_see);
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
//                                Toast.makeText(Activity_All_View.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("userid").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String item_location = object.getString("item_location");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id,seller_id, main_category, sub_category, ad_detail, price, item_location, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Adapter_All_View(itemList, Activity_All_View.this);
                                adapter_item.notifyDataSetChanged();
                                gridViewSearch.invalidateViews();
                                gridViewSearch.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter_All_View.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Activity_All_View.this, Activity_View_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(PRICE, item.getPrice());
                                        detailIntent.putExtra(ITEM_LOCATION, item.getItem_location());
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
                                        final String strItem_location = item.getItem_location();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if(success.equals("1")){
                                                                Toast.makeText(Activity_All_View.this, "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }catch (JSONException e){
                                                            e.printStackTrace();
                                                            Toast.makeText(Activity_All_View.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Activity_All_View.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("customer_id", getId);
                                                params.put("main_category", strMain_category);
                                                params.put("sub_category", strSub_category);
                                                params.put("ad_detail", strAd_Detail);
                                                params.put("price", String.format("%.2f", strPrice));
                                                params.put("item_location", strItem_location);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Activity_All_View.this);
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
                                        final String strItem_location = item.getItem_location();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if(success.equals("1")){
                                                                Toast.makeText(Activity_All_View.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }catch (JSONException e){
                                                            e.printStackTrace();
                                                            Toast.makeText(Activity_All_View.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Activity_All_View.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("customer_id", getId);
                                                params.put("main_category", strMain_category);
                                                params.put("sub_category", strSub_category);
                                                params.put("ad_detail", strAd_Detail);
                                                params.put("price", String.format("%.2f", strPrice));
                                                params.put("item_location", strItem_location);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Activity_All_View.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                });
                            } else {
                                Toast.makeText(Activity_All_View.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Activity_All_View.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if (error.getMessage() == null) {
//                            Toast.makeText(Activity_All_View.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Activity_All_View.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void Category_Func(){

        Button_SellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Sell_Items()).addToBackStack(null).commit();
            }
        });

        searchView.setVisibility(View.GONE);
        Button_FindItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                gridViewSearch.setVisibility(View.VISIBLE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter_item.getFilter().filter(newText);
                        return false;
                    }
                });
            }
        });

        button_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Business()).addToBackStack(null).commit();
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Camera()).addToBackStack(null).commit();
            }
        });

        button_car_parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Car_Accessories()).addToBackStack(null).commit();
            }
        });

        button_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Cars()).addToBackStack(null).commit();
            }
        });

        button_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Computer()).addToBackStack(null).commit();
            }
        });

        button_electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Electronics()).addToBackStack(null).commit();
            }
        });

        button_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Furniture()).addToBackStack(null).commit();
            }
        });

        button_handcraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Handcraft()).addToBackStack(null).commit();
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Home()).addToBackStack(null).commit();
            }
        });

        button_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Men()).addToBackStack(null).commit();
            }
        });

        button_mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Mom()).addToBackStack(null).commit();
            }
        });

        button_motorcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Motorcycles()).addToBackStack(null).commit();
            }
        });

        button_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Pets()).addToBackStack(null).commit();
            }
        });

        button_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Rent()).addToBackStack(null).commit();
            }
        });

        button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Sales()).addToBackStack(null).commit();
            }
        });

        button_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Services()).addToBackStack(null).commit();
            }
        });

        button_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Sports()).addToBackStack(null).commit();
            }
        });

        button_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Travel()).addToBackStack(null).commit();
            }
        });

        button_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Women()).addToBackStack(null).commit();
            }
        });

        button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Food()).addToBackStack(null).commit();
            }
        });

        button_grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Grocery()).addToBackStack(null).commit();
            }
        });

        button_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_See_All()).addToBackStack(null).commit();
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
                                    String strPhone_no = object.getString("phone_no").trim();
                                    String strAddress = object.getString("address").trim();
                                    String strBirthday = object.getString("birthday").trim();
                                    String strGender = object.getString("gender");
                                    String strPhoto = object.getString("photo");

                                    name_display.setText(strName);
                                    email_display.setText(strEmail);

                                    Picasso.get().load(strPhoto).into(profile_display);
                                }
                            } else {
                                Toast.makeText(Activity_All_View.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

//                            Toast.makeText(Activity_All_View.this, "JSON Parsing Eror: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Activity_All_View.this, "Connection Error", Toast.LENGTH_SHORT).show();
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
                        new Fragment_Empty()).commit();
                Toast.makeText(this, "Homepage", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sell:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Sell_Items()).addToBackStack(null).commit();
                Toast.makeText(this, "Sell My Items", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_View_Item_User()).addToBackStack(null).commit();
                Toast.makeText(this, "Find My Items", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_ads:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_chat_inbox:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_favourite_ads:
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                gridViewSearch.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Saved_Searches()).addToBackStack(null).commit();
                Toast.makeText(this, "My Favourite Ads", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_saved_searches:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_about_the_apps:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_log_out:
                sessionManager.logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                view.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Empty()).addToBackStack(null).commit();
                adapter_item.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                view.setVisibility(View.GONE);
                Intent intent1 = new Intent(Activity_All_View.this, Activity_Edit_User_Profile.class);
                startActivity(intent1);
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings is Clicked!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_cart:
                Intent intent = new Intent(Activity_All_View.this, Activity_Add_to_Cart.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            builder.setTitle("Do you want to exit the app?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sessionManager.logout();
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
