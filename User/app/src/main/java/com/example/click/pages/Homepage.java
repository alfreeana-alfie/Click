package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.Goto_Register_Page;
import com.example.click.R;
import com.example.click.View_Item_Single;
import com.example.click.adapter.CartAdapter;
import com.example.click.adapter.Item_Single_Adapter;
import com.example.click.adapter.PageAdapter;
import com.example.click.category.Agriculture;
import com.example.click.category.Cake;
import com.example.click.category.Fashion;
import com.example.click.category.Handcraft;
import com.example.click.category.Health;
import com.example.click.category.Home;
import com.example.click.category.Pepper;
import com.example.click.category.Personal;
import com.example.click.category.Processed;
import com.example.click.category.Retail;
import com.example.click.category.Sevice;
import com.example.click.category.View_All;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;
import com.example.click.user.Edit_Profile;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private static String URL_READALL = "https://ketekmall.com/ketekmall/category/readall.php";
    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_READ_PROMOTION = "https://ketekmall.com/ketekmall/read_promotion.php";

    List<Item_All_Details> itemList;

    CartAdapter _cart_adapter;
    RecyclerView recyclerView;
    ArrayList<Item_All_Details> itemAllDetailsArrayList;

    String getId;
    SessionManager sessionManager;

    TextView textCartItemCount;
    int mCartItemCount;
    private TwoWayGridView gridView_HardSelling, gridView_TopSelling;

    private ScrollView scrollView;
    private Button Button_SellItem, Button_FindItem, button_retail, button_processed,
            button_handcraft, button_cake, button_agriculture, button_service, button_health,
            button_home, button_personal, button_pepper, button_fashion;

    private CircleImageView profile_display, profile_image;
    private TextView name_display, email_display, button_view_all, username, verify, verify1;
    private DrawerLayout drawer;
    private View view;
    Item_Single_Adapter adapter_item;
    ViewPager viewPager;
    Timer timer;

    BottomNavigationView bottomNav;
    private long backPressedTime;
    private Toast backToast;
    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };
    String[] image = new String[3];
    PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        Declare();

        viewPager = findViewById(R.id.view_pager);

        final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (viewPager.getCurrentItem() == adapter.getCount() - 1) { //adapter is your custom ViewPager's adapter
                    viewPager.setCurrentItem(0);
                }
                else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        getSession();

        getUserDetail();

        Category_Func();

        Cart_Item();

        View_HardSelling();

        View_TopSelling();

        View_Photo();

        SellerCheck_Main(getId);
    }

    private void getSession() {
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

    }

    private void Declare() {
        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.cart_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Homepage.this));
        itemAllDetailsArrayList = new ArrayList<>();

        gridView_HardSelling = findViewById(R.id.gridView_HardSelling);
        gridView_TopSelling = findViewById(R.id.gridView_TopSelling);
        _cart_adapter = new CartAdapter(this, itemList);
        view = findViewById(R.id.support_layout);
        scrollView = findViewById(R.id.grid_category);
        Button_SellItem = findViewById(R.id.button_sellItem);
        Button_FindItem = findViewById(R.id.button_FindItem);

        button_cake = findViewById(R.id.button_cake);
        button_processed = findViewById(R.id.button_process_food);
        button_handcraft = findViewById(R.id.button_handcraft);
        button_retail = findViewById(R.id.button_retail);
        button_agriculture = findViewById(R.id.button_agri);
        button_service = findViewById(R.id.button_service);
        button_health = findViewById(R.id.button_health);
        button_home = findViewById(R.id.button_homes);
        button_fashion = findViewById(R.id.button_fashion);
        button_personal = findViewById(R.id.button_personal);
        button_pepper = findViewById(R.id.button_pepper);
        button_view_all = findViewById(R.id.button_see);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        verify = findViewById(R.id.verify);
        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Homepage.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Homepage.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        profile_display = headerView.findViewById(R.id.profile_display);
        profile_display.setBorderWidth(1);
        name_display = headerView.findViewById(R.id.name_display);
        email_display = headerView.findViewById(R.id.email_display);
        verify1 = headerView.findViewById(R.id.verify1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
                SellerCheck(getId);
            }
        });

        Button_FindItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, View_All.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Cake.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_processed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Processed.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_handcraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Handcraft.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Agriculture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Sevice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Health.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Fashion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Personal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_pepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, Pepper.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intent = new Intent(Homepage.this, View_All.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

    private void SellerCheck(final String user_id){
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

                                    int strVerify = Integer.valueOf(object.getString("verification"));
                                    if(strVerify == 0){
                                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                                        Menu nav_Menu = navigationView.getMenu();
                                        nav_Menu.findItem(R.id.nav_sell).setVisible(false);
                                        nav_Menu.findItem(R.id.nav_find).setVisible(false);

                                        Intent intent1 = new Intent(Homepage.this, Goto_Register_Page.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
                                    }else{
                                        Intent intent1 = new Intent(Homepage.this, Sell_Items_Other.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
                                    }

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
                params.put("id", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SellerCheck_Main(final String user_id){
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
                                    int strVerify = Integer.valueOf(object.getString("verification"));
                                    if(strVerify == 0){
                                        NavigationView navigationView = findViewById(R.id.nav_view);
                                        Menu nav_Menu = navigationView.getMenu();
                                        nav_Menu.findItem(R.id.nav_sell).setVisible(false);
                                        nav_Menu.findItem(R.id.nav_find).setVisible(false);
                                        verify.setText("Buyer");
                                        verify1.setText("Buyer");
                                    }else{
                                        NavigationView navigationView = findViewById(R.id.nav_view);
                                        Menu nav_Menu = navigationView.getMenu();
                                        nav_Menu.findItem(R.id.nav_sell).setVisible(true);
                                        nav_Menu.findItem(R.id.nav_find).setVisible(true);
                                        verify.setText("Seller");
                                        verify1.setText("Seller");
                                    }

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
                params.put("id", user_id);
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
                Intent intent4 = new Intent(Homepage.this, Homepage.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);
                Toast.makeText(this, "Homepage", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sell:

                SellerCheck(getId);
                Toast.makeText(this, "Sell My Items", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find:
                Intent intent1 = new Intent(Homepage.this, Find_My_Items_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                Toast.makeText(this, "Find My Items", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_ads:
                Intent intent3 = new Intent(Homepage.this, Main_Order_Other.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);
                Toast.makeText(this, "My Orders", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_chat_inbox:
                Intent intent5 = new Intent(Homepage.this, Chat_Inbox_Other.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent5);
                Toast.makeText(this, "My Chat Inbox", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_favourite_ads:
                Intent intent = new Intent(Homepage.this, Saved_Searches_Other.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(this, "My Favourite Ads", Toast.LENGTH_SHORT).show();
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

            case R.id.menu_search:
                Intent intent2 = new Intent(Homepage.this, View_All.class);
                startActivity(intent2);
                break;

            case R.id.menu_chat:
                Intent intent3 = new Intent(Homepage.this, Chat_Inbox_Other.class);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void View_HardSelling() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READALL,
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

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Single_Adapter(itemList, Homepage.this);
                                adapter_item.notifyDataSetChanged();
                                gridView_HardSelling.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Single_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Item_Single.class);
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

                                        startActivity(detailIntent);
                                    }
                                });

                            } else {
                                Toast.makeText(Homepage.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    private void View_TopSelling() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READALL,
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

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Single_Adapter(itemList, Homepage.this);
                                adapter_item.notifyDataSetChanged();
                                gridView_TopSelling.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Single_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Item_Single.class);
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

                                        startActivity(detailIntent);
                                    }
                                });

                            } else {
                                Toast.makeText(Homepage.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    private void View_Photo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PROMOTION,
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
                                    String image_item = object.getString("photo");

                                    image[i] = image_item;

                                    adapter = new PageAdapter(Homepage.this, image);
                                }
                                viewPager.setAdapter(adapter);

                            } else {
                                Toast.makeText(Homepage.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            timer.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        timer.cancel();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
