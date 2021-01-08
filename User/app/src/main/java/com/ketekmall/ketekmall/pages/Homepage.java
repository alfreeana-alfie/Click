package com.ketekmall.ketekmall.pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
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
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.navigation_items.About_KetekMall;
import com.ketekmall.ketekmall.pages.navigation_items.transaction.Cart;
import com.ketekmall.ketekmall.pages.navigation_items.Chat_Inbox_Homepage;
import com.ketekmall.ketekmall.pages.product_details.View_Product;
import com.ketekmall.ketekmall.pages.seller.Product_Add;
import com.ketekmall.ketekmall.user.Edit_Profile;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.onesignal.OneSignal;
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
    private static final String ONESIGNAL_APP_ID = "6236bfc3-df4d-4f44-82d6-754332044779";

    private static String URL_READ_USER_DETAIL = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_CART = "https://ketekmall.com/ketekmall/readcart.php";
    private static String URL_READ_PROMOTION = "https://ketekmall.com/ketekmall/read_promo.php";

    private static String URL_ADD_PLAYERID = "https://ketekmall.com/ketekmall/add_playerID.php";

    private static String URL_READ_CHAT = "https://ketekmall.com/ketekmall/read_chat.php";

    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readcart_single.php";

    private static String URL_GETCHATISREADALL = "https://ketekmall.com/ketekmall/getChatIsReadAll.php";

    String URL_READ_CATEGORY_MAIN = "https://ketekmall.com/ketekmall/category/";
    String URL_READ_CATEGORY_SEARCH_MAIN = "https://ketekmall.com/ketekmall/search/";
    String URL_READ_CATEGORY_FILTER_DISTRICT_MAIN = "https://ketekmall.com/ketekmall/filter_district/";
    String URL_READ_CATEGORY_FILTER_DIVISION_MAIN = "https://ketekmall.com/ketekmall/filter_division/";
    String URL_READ_CATEGORY_FILTER_SEARCH_MAIN = "https://ketekmall.com/ketekmall/filter_search_division/";


    String[] CATEGORY_LIST = {
            "read_cake.php",
            "read_process.php",
            "read_handicraft.php",
            "read_retail.php",
            "read_agri.php",
            "read_service.php",
            "read_health.php",
            "read_home.php",
            "read_fashion.php",
            "read_pepper.php",
            "readall.php",
            "readall_sold.php",
            "readall_shocking.php",
            "readall.php",
            "read_pickup.php"};

    List<Item_All_Details> itemList;
    List<Item_All_Details> itemList2;

    CartAdapter _cart_adapter;
    RecyclerView recyclerView;
    ArrayList<Item_All_Details> itemAllDetailsArrayList;

    String getId;
    SessionManager sessionManager;

    TextView textCartItemCount, textChatItemCount;
    int mCartItemCount,mChatItemCount;
    private TwoWayGridView gridView_HardSelling, gridView_TopSelling;

    private Button Button_SellItem,
            Button_FindItem,
            button_processed,
            button_handcraft,
            button_health,
            button_home,
            button_pepper,
            button_fashion,
            button_pickup;

    private CircleImageView profile_image;
    private TextView button_view_all;
    private TextView username;
    private TextView verify;

    Item_Adapter_Main adapter_item, adapter_item2;
    ViewPager viewPager;
    RelativeLayout hot_layout, top_layout;

    ImageButton btn_next, btn_back;

    ProgressBar loading_hot, loading_shock;

    BottomNavigationView bottomNav;
    private long backPressedTime;
    private Toast backToast;
    String[] image = new String[3];
    PageAdapter adapter;
    RequestQueue queue;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String s1 = sh.getString("lang", "");

        if(s1.equals("en")){
            String languageToLoad1 = "en"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.commit();

//            Toast.makeText(Homepage.this, "en", Toast.LENGTH_SHORT).show();
        }else{
            String languageToLoad1 = "ms"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;

            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.commit();

//            Toast.makeText(Homepage.this, "ms", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        queue = Volley.newRequestQueue(this);

        checkLang();

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


        button_processed = findViewById(R.id.button_process_food);
        button_handcraft = findViewById(R.id.button_handcraft);
        /*
        button_cake = findViewById(R.id.button_cake);
        button_retail = findViewById(R.id.button_retail);
        button_agriculture = findViewById(R.id.button_agri);
        button_service = findViewById(R.id.button_service);
        */
        button_health = findViewById(R.id.button_health);
        button_home = findViewById(R.id.button_homes);
        button_fashion = findViewById(R.id.button_fashion);
        button_pepper = findViewById(R.id.button_pepper);
        button_view_all = findViewById(R.id.button_see);
        button_pickup = findViewById(R.id.button_pickup);

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
                        intent6.putExtra("URL_READ", URL_READ_CATEGORY_MAIN + CATEGORY_LIST[12]);
                        intent6.putExtra("URL_ADD_FAV", URL_ADD_FAV);
                        intent6.putExtra("URL_ADD_CART", URL_ADD_CART);
                        intent6.putExtra("URL_SEARCH", URL_READ_CATEGORY_SEARCH_MAIN + CATEGORY_LIST[12]);
                        intent6.putExtra("URL_FILTER_DISTRICT", URL_READ_CATEGORY_FILTER_DISTRICT_MAIN + CATEGORY_LIST[12]);
                        intent6.putExtra("URL_FILTER_DIVISION", URL_READ_CATEGORY_FILTER_DIVISION_MAIN + CATEGORY_LIST[12]);
                        intent6.putExtra("URL_FILTER_SEARCH", URL_READ_CATEGORY_FILTER_SEARCH_MAIN + CATEGORY_LIST[12]);
                        intent6.putExtra("URL_READ_CART", URL_READ_CART);
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

    private void GotoCategory(int number){
        Intent intent = new Intent(Homepage.this, View_Category.class);
        intent.putExtra("URL_READ", URL_READ_CATEGORY_MAIN + CATEGORY_LIST[number]);
        intent.putExtra("URL_ADD_FAV", URL_ADD_FAV);
        intent.putExtra("URL_ADD_CART", URL_ADD_CART);
        intent.putExtra("URL_SEARCH", URL_READ_CATEGORY_SEARCH_MAIN + CATEGORY_LIST[number]);
        intent.putExtra("URL_FILTER_DISTRICT", URL_READ_CATEGORY_FILTER_DISTRICT_MAIN + CATEGORY_LIST[number]);
        intent.putExtra("URL_FILTER_DIVISION", URL_READ_CATEGORY_FILTER_DIVISION_MAIN + CATEGORY_LIST[number]);
        intent.putExtra("URL_FILTER_SEARCH", URL_READ_CATEGORY_FILTER_SEARCH_MAIN + CATEGORY_LIST[number]);
        intent.putExtra("URL_READ_CART", URL_READ_CART);
        startActivity(intent);
    }

    private void Category_Func() {
        TextView button_view_hard = findViewById(R.id.button_view_hard);
        TextView button_view_top = findViewById(R.id.button_view_top);

        Button_SellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SellerCheck(getId);
                Intent intent = new Intent(Homepage.this, Product_Add.class);
                startActivity(intent);
            }
        });

        Button_FindItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GotoCategory(13);
            }
        });

/*
        button_cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(0);
            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(3);
            }
        });

        button_agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(4);
            }
        });

        button_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GotoCategory(5);
            }
        });
*/

        button_processed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(1);
            }
        });

        button_handcraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(2);
            }
        });



        button_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(6);
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(7);
            }
        });

        button_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(8);
            }
        });

        button_pepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(9);
            }
        });

        button_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(14);
            }
        });

        button_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(10);
            }
        });

        button_view_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(11);
            }
        });

        button_view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCategory(12);
            }
        });

    }

    private void getUserDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_USER_DETAIL,
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


                                    InsertNotificationData(strName);
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

    private void InsertNotificationData(final String Name){
        final String PlayerID = OneSignal.getDeviceState().getUserId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_PLAYERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("POST", response);
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
                params.put("PlayerID", PlayerID);
                params.put("Name", Name);
                params.put("UserID", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SellerCheck_Main(final String user_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_USER_DETAIL,
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
                SharedPreferences lang = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = lang.edit();
                editor.putString("lang", languageToLoad);
                editor.commit();

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
                SharedPreferences lang1 = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor1 = lang1.edit();
                editor1.putString("lang", languageToLoad1);
                editor1.commit();

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CATEGORY_MAIN + CATEGORY_LIST[11],
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
                                    String weight = object.getString("weight");
                                    String postcode = object.getString("postcode");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setSold(sold);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Adapter_Main(itemList);
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
                                        detailIntent.putExtra("weight", item.getWeight());
                                        detailIntent.putExtra("postcode", item.getPostcode());

                                        startActivity(detailIntent);

                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);
                                        View_Cart2(item);
                                    }
                                });

                            } else {
                                Toast.makeText(Homepage.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Homepage.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CATEGORY_MAIN + CATEGORY_LIST[12],
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
                                    String weight = object.getString("weight");
                                    String postcode = object.getString("postcode");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setBrand(brand);
                                    item.setInner(inner);
                                    item.setStock(stock);
                                    item.setDescription(desc);
                                    item.setRating(rating);
                                    item.setPostcode(postcode);
                                    item.setWeight(weight);
                                    itemList2.add(item);
                                }
                                adapter_item2 = new Item_Adapter_Main(itemList2);
                                adapter_item2.notifyDataSetChanged();
                                gridView_TopSelling.setAdapter(adapter_item2);
                                adapter_item2.setOnItemClickListener(new Item_Adapter_Main.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Homepage.this, View_Product.class);
                                        Item_All_Details item = itemList2.get(position);

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
                                        detailIntent.putExtra("weight", item.getWeight());
                                        detailIntent.putExtra("postcode", item.getPostcode());
                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList2.get(position);
                                        View_Cart2(item);
                                    }
                                });

                            } else {
                                Toast.makeText(Homepage.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Homepage.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETCHATISREADALL,
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
                params.put("UserID", getId);
                params.put("IsRead", "false");
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

    private void View_Cart2(final Item_All_Details item) {
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
                                    final String strWeight = item.getWeight();

                                    if (getId.equals(strSeller_id)) {
                                        Toast.makeText(Homepage.this, R.string.cannot_add_your_own_item, Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(Homepage.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(Homepage.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
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
                                                params.put("postcode", strPostcode);
                                                params.put("district", strDistrict);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                params.put("item_id", strItem_Id);
                                                params.put("weight", strWeight);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(Homepage.this);
                                        requestQueue.add(stringRequest2);
                                    }
                                }

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String item_id = object.getString("item_id");
                                        Toast.makeText(Homepage.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.press_back_again_to_exit, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
