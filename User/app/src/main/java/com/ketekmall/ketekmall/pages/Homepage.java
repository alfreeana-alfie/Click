package com.ketekmall.ketekmall.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.adapter.CartAdapter;
import com.ketekmall.ketekmall.adapter.Item_Adapter_Main;
import com.ketekmall.ketekmall.adapter.PageAdapter;
import com.ketekmall.ketekmall.category.Agriculture;
import com.ketekmall.ketekmall.category.Cake;
import com.ketekmall.ketekmall.category.Fashion;
import com.ketekmall.ketekmall.category.Handicraft;
import com.ketekmall.ketekmall.category.Health;
import com.ketekmall.ketekmall.category.Home;
import com.ketekmall.ketekmall.category.Pepper;
import com.ketekmall.ketekmall.category.Processed;
import com.ketekmall.ketekmall.category.Retail;
import com.ketekmall.ketekmall.category.Service;
import com.ketekmall.ketekmall.category.View_All;
import com.ketekmall.ketekmall.category.View_All_Hot;
import com.ketekmall.ketekmall.category.View_All_Shock;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.data.UserDetails;
import com.ketekmall.ketekmall.user.Edit_Profile;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity {

    public static final String ID = "id";

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_READALL = "https://ketekmall.com/ketekmall/category/readall.php";
    private static String URL_READALL_SHOCK = "https://ketekmall.com/ketekmall/category/readall_shocking.php";
    private static String URL_READALL_HOT = "https://ketekmall.com/ketekmall/category/readall_sold.php";
    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_READ_PROMOTION = "https://ketekmall.com/ketekmall/read_promotion.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";

    private static String URL_READ_CHAT = "https://ketekmall.com/ketekmall/read_chat.php";

    List<Item_All_Details> itemList, itemList2;

    CartAdapter _cart_adapter;
    RecyclerView recyclerView;
    ArrayList<Item_All_Details> itemAllDetailsArrayList;

    String getId;
    SessionManager sessionManager;

    TextView textCartItemCount, textChatItemCount;
    int mCartItemCount,mChatItemCount;
    private TwoWayGridView gridView_HardSelling, gridView_TopSelling;

    private Button Button_SellItem, Button_FindItem, button_retail, button_processed,
            button_handcraft, button_cake, button_agriculture, button_service, button_health,
            button_home, button_pepper, button_fashion;

    private CircleImageView profile_display, profile_image;
    private TextView name_display;
    private TextView email_display;
    private TextView button_view_all;
    private TextView username;
    private TextView verify;
    private TextView verify1;
    private TextView button_view_top;

    Item_Adapter_Main adapter_item, adapter_item2;
    ViewPager viewPager;
    Timer timer;
    RelativeLayout hot_layout, top_layout;

    ImageButton btn_next, btn_back;
    String lang;

    ProgressBar loading_hot, loading_shock;

    BottomNavigationView bottomNav;
    private long backPressedTime;
    private Toast backToast;
    String[] image = new String[3];
    PageAdapter adapter;
    RequestQueue queue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        queue = Volley.newRequestQueue(this);

        getSession();

        Declare();

        viewPager = findViewById(R.id.view_pager);

        getSession();

        getUserDetail();

        Category_Func();

        new Timer().schedule(
                new TimerTask(){

                    @Override
                    public void run(){
                        Cart_Item();
                    }

                }, 1000);

        View_HardSelling();

        View_TopSelling();

        View_Photo();

        SellerCheck_Main(getId);
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Declare() {
        itemList = new ArrayList<>();
        itemList2 = new ArrayList<>();

        loading_hot = findViewById(R.id.loading_hot);
        loading_shock = findViewById(R.id.loading_shock);

        recyclerView = findViewById(R.id.cart_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Homepage.this));
        itemAllDetailsArrayList = new ArrayList<>();

        gridView_HardSelling = findViewById(R.id.gridView_HardSelling);
        gridView_TopSelling = findViewById(R.id.gridView_TopSelling);
        _cart_adapter = new CartAdapter(this, itemList);

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
        button_pepper = findViewById(R.id.button_pepper);
        button_view_all = findViewById(R.id.button_see);
        TextView button_view_hard = findViewById(R.id.button_view_hard);
        button_view_top = findViewById(R.id.button_view_top);
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);

        hot_layout = findViewById(R.id.hot_layout);
        top_layout = findViewById(R.id.top_layout);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                if(current<viewPager.getChildCount()){
                    viewPager.setCurrentItem(current);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() - 1;
                if(current<viewPager.getChildCount()){
                    viewPager.setCurrentItem(current);
                }
            }
        });

        button_view_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, View_All_Hot.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button_view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, View_All_Shock.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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
                        Intent intent6 = new Intent(Homepage.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Homepage.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
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

                SellerCheck(getId);
//                Intent intent = new Intent(Homepage.this, newPage.class);
//                startActivity(intent);
            }
        });

        Button_FindItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Homepage.this, View_All.class);
                startActivity(intent);
            }
        });

        button_cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Cake.class);
                startActivity(intent);
            }
        });

        button_processed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Processed.class);
                startActivity(intent);
            }
        });

        button_handcraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Handicraft.class);
                startActivity(intent);
            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Retail.class);
                startActivity(intent);
            }
        });

        button_agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Agriculture.class);
                startActivity(intent);
            }
        });

        button_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Service.class);
                startActivity(intent);
            }
        });

        button_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Health.class);
                startActivity(intent);
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Home.class);
                startActivity(intent);
            }
        });

        button_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Fashion.class);
                startActivity(intent);
            }
        });

        button_pepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Pepper.class);
                startActivity(intent);
            }
        });

        button_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, View_All.class);
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
                                    String strPhoto = object.getString("photo");

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
                                //Error
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

                                    int strVerify = Integer.parseInt(object.getString("verification"));
                                    if(strVerify == 0){
                                        Intent intent1 = new Intent(Homepage.this, Register_Seller_MainPage.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
                                    }else{
                                        Intent intent1 = new Intent(Homepage.this, Product_Add.class);
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
                                //Error
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
                                    int strVerify = Integer.parseInt(object.getString("verification"));
                                    if(strVerify == 0){
                                        verify.setText(getResources().getString(R.string.buyer));
                                    }else{
                                        verify.setText(getResources().getString(R.string.seller));
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
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
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
                params.put("id", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        final MenuItem menuItem1 = menu.findItem(R.id.menu_chat);

        View actionView = menuItem.getActionView();
        View actionView1 = menuItem1.getActionView();

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        textChatItemCount = actionView1.findViewById(R.id.chat_badge);

        setupBadge();

        MessageCount();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        actionView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem1);
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
                Intent intent1 = new Intent(Homepage.this, Edit_Profile.class);
                startActivity(intent1);
                break;
            case R.id.setting:
                disconnectFromFacebook();
                sessionManager.logout();
                break;

            case R.id.contact_us:
                Intent intent2 = new Intent(Homepage.this, About_KetekMall.class);
                startActivity(intent2);
                break;

            case R.id.malaylang:
                String languageToLoad = "ms"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences lang = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = lang.edit();
                editor.putString("lang", languageToLoad);
                editor.apply();

                this.recreate();
                break;

            case R.id.englang:
                String languageToLoad1 = "en"; // your language
                Locale locale1 = new Locale(languageToLoad1);
                Locale.setDefault(locale1);
                Configuration config1 = new Configuration();
                config1.locale = locale1;
                getBaseContext().getResources().updateConfiguration(config1,
                        getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences lang1 = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor1 = lang1.edit();
                editor1.putString("lang", languageToLoad1);
                editor1.apply();

                this.recreate();
                break;

            case R.id.menu_cart:
                Intent intent = new Intent(Homepage.this, Cart.class);
                startActivity(intent);
                break;

            case R.id.menu_chat:
                Intent intent3 = new Intent(Homepage.this, Chat_Inbox_Homepage.class);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void View_HardSelling() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READALL_HOT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                if(jsonArray.length() == 0){
                                    hot_layout.setVisibility(View.GONE);
                                }
                                loading_hot.setVisibility(View.GONE);
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
                                    String sold = object.getString("sold");

                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();
                                    String rating = object.getString("rating");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setSold(sold);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Adapter_Main(itemList, Homepage.this);
                                adapter_item.sortArrayHighest();
                                adapter_item.notifyDataSetChanged();
                                gridView_HardSelling.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter_Main.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Product.class);
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
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        startActivity(detailIntent);

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
                                            Toast.makeText(Homepage.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
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
                                                                        Toast.makeText(Homepage.this, "Add To Cart", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(Homepage.this, "Failed Adding To Favourite", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(Homepage.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
                                            requestQueue.add(stringRequest2);
                                        }
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
                        Toast.makeText(Homepage.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
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
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    private void View_TopSelling() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READALL_SHOCK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                if(jsonArray.length() == 0){
                                    top_layout.setVisibility(View.GONE);
                                }
                                loading_shock.setVisibility(View.GONE);
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
                                    String brand = object.getString("brand_material").trim();
                                    String inner = object.getString("inner_material").trim();
                                    String stock = object.getString("stock").trim();
                                    String desc = object.getString("description").trim();
                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    itemList2.add(item);
                                }
                                adapter_item2 = new Item_Adapter_Main(itemList2, Homepage.this);
                                adapter_item2.notifyDataSetChanged();
                                gridView_TopSelling.setAdapter(adapter_item2);
                                adapter_item2.setOnItemClickListener(new Item_Adapter_Main.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Product.class);
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
                                        detailIntent.putExtra("stock", item.getStock());
                                        detailIntent.putExtra("description", item.getDescription());

                                        startActivity(detailIntent);
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
                                            Toast.makeText(Homepage.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
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
                                                                        Toast.makeText(Homepage.this, "Add To Cart", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(Homepage.this, "Failed Adding To Favourite", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(Homepage.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
                                            requestQueue.add(stringRequest2);
                                        }
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
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
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
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
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
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    private void MessageCount(){
        final String ref1 = UserDetails.email + "_";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                Log.d("Message", String.valueOf(jsonArray.length()));
                                mChatItemCount = jsonArray.length();
                                setupBadgeChat(jsonArray.length());
                            } else {
                                Log.e("Message", "Return FAILED");
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
                params.put("user_chatwith", ref1);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
        requestQueue.add(stringRequest);
    }

    private void setupBadgeChat(final int count) {

            if (count == 0) {
                if (textChatItemCount.getVisibility() != View.GONE) {
                    textChatItemCount.setVisibility(View.GONE);
                }
            } else {
                textChatItemCount.setText(String.valueOf(count));
                if (textChatItemCount.getVisibility() != View.VISIBLE) {
                    textChatItemCount.setVisibility(View.VISIBLE);
                }
            }


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
