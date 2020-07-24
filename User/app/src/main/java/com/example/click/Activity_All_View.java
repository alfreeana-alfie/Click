package com.example.click;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private static String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/readall.php";
    private static String URL_ADD = "https://annkalina53.000webhostapp.com/android_register_login/add_to_fav.php";
    private static String URL_ADD_CART = "https://annkalina53.000webhostapp.com/android_register_login/add_to_cart.php";

    List<Item_All_Details> itemList;
    Item_Adapter_All_View adapter_item;
    String getId;
    SessionManager sessionManager;

    private Button button_cars, button_sales;
    private TwoWayGridView gridView;
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

        button_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Category_Cars()).commit();
            }
        });

        View_Item();
    }

    private void Declare() {
        itemList = new ArrayList<>();
        gridView = findViewById(R.id.gridView_item);
        view = findViewById(R.id.support_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        profile_display = headerView.findViewById(R.id.profile_display);
        profile_display.setBorderWidth(1);
        name_display = headerView.findViewById(R.id.name_display);
        email_display = headerView.findViewById(R.id.email_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        button_cars = findViewById(R.id.button_cars);
        button_sales = findViewById(R.id.button_sales);
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
                                gridView.invalidateViews();
                                gridView.setAdapter(adapter_item);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Empty()).commit();
                Toast.makeText(this, "Homepage", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sell:
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Sell_Items()).commit();
                Toast.makeText(this, "Sell My Items", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find:
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_View_Item_User()).commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Saved_Searches()).commit();
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
                        new Fragment_Empty()).commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Edit_Profile()).commit();
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings is Clicked!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.add_to_cart:
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
