package com.example.click;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.click.adapter.Item_Single_Adapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;
import com.example.click.data.UserDetails;
import com.example.click.pages.Chat;
import com.example.click.pages.Homepage;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View_Item_Single extends AppCompatActivity {

    private static String URL_READ_CART = "https://ketekmall.com/ketekmall/readall_seller.php";
    private static String URL_READ_SELLER = "https://ketekmall.com/ketekmall/read_order_done_seller.php";

    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_READ_DELIVERY = "https://ketekmall.com/ketekmall/read_delivery_single.php";


    String id, userid, ad_detail, division, district, strMain_category, strSub_category, strPrice, photo
            , getId, brand, inner, stock, desc;
    Item_Single_Adapter adapter_item;
    List<Item_All_Details> itemList, itemList2;
    SessionManager sessionManager;
    private ImageView img_item, seller_image;
    private TextView ad_detail_item, price_item, sold_text, shipping_info, detail_info,
            seller_name, seller_location, view_all;
    private Button btn_chat, add_to_cart_btn;
    private ImageButton btn_chat_wsp;
    private TwoWayGridView gridView_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item_other);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        itemList = new ArrayList<>();
        itemList2 = new ArrayList<>();
        img_item = findViewById(R.id.img_item);
        ad_detail_item = findViewById(R.id.ad_details_item);
        price_item = findViewById(R.id.price_item);
        sold_text = findViewById(R.id.sold_text);
        shipping_info = findViewById(R.id.shipping_info);
        detail_info = findViewById(R.id.detail_info);

        seller_name = findViewById(R.id.seller_name);
        seller_image = findViewById(R.id.seller_image);
        seller_location = findViewById(R.id.seller_location);
        view_all = findViewById(R.id.view_all);
        btn_chat = findViewById(R.id.btn_chat);
        gridView_item = findViewById(R.id.gridView_item);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
        btn_chat_wsp = findViewById(R.id.btn_chat_wsp);

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
        district = intent.getStringExtra("district");
        photo = intent.getStringExtra("photo");

        String Price_Text = "MYR" + strPrice;

        ad_detail_item.setText(ad_detail);
        price_item.setText(Price_Text);
        Picasso.get().load(photo).into(img_item);

        getUserDetail();
        View_Item();
        getSold();

        detail_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(View_Item_Single.this, Detail_Info.class);

                final Intent intent4 = getIntent();
                String id1 = intent4.getStringExtra("id");
                String stock = intent4.getStringExtra("stock");
                String brand = intent4.getStringExtra("brand_material");
                String inner = intent4.getStringExtra("inner_material");
                String desc = intent4.getStringExtra("description");
                String division = intent4.getStringExtra("division");
                String district = intent4.getStringExtra("district");

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
                    Toast.makeText(View_Item_Single.this, "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(response);
                                        String success = jsonObject1.getString("success");

                                        if (success.equals("1")) {
                                            Toast.makeText(View_Item_Single.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(View_Item_Single.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(View_Item_Single.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("customer_id", getId);
                            params.put("main_category", strMain_category);
                            params.put("sub_category", strSub_category);
                            params.put("ad_detail", ad_detail);
                            params.put("price", strPrice);
                            params.put("division", division);
                            params.put("district", district);
                            params.put("photo", photo);
                            params.put("seller_id", userid);
                            params.put("item_id", id);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(View_Item_Single.this);
                    requestQueue.add(stringRequest2);
                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }
        });

        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(View_Item_Single.this, Seller_Shop.class);
                startActivity(intent1);
            }
        });

        ToolbarSetting();

        shipping_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_DELIVERY,
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

                                            final String delivery_id = object.getString("id").trim();
                                            final String user_id = object.getString("user_id").trim();
                                            final String division = object.getString("division").trim();
                                            final Double price = Double.valueOf(object.getString("price").trim());
                                            final String days = object.getString("days");
                                            final String item_id = object.getString("item_id");

                                            Delivery delivery = new Delivery(division, String.format("%.2f", price), days, item_id);

                                            Intent intent1 = new Intent(View_Item_Single.this, Shipping_Info.class);

                                            final Intent intent4 = getIntent();
                                            String id1 = intent4.getStringExtra("id");
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
                                            intent1.putExtra("district", district1);
                                            intent1.putExtra("photo", photo1);

                                            startActivity(intent1);
                                        }

                                    } else {
                                        Toast.makeText(View_Item_Single.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(View_Item_Single.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(View_Item_Single.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("item_id", id);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(View_Item_Single.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.find_actionbar);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Item_Single.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void AddCart() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String success = jsonObject1.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(View_Item_Single.this, "Add To Cart", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(View_Item_Single.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(View_Item_Single.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", getId);
                params.put("main_category", strMain_category);
                params.put("sub_category", strSub_category);
                params.put("ad_detail", ad_detail);
                params.put("price", strPrice);
                params.put("division", division);
                params.put("district", district);
                params.put("photo", photo);
                params.put("seller_id", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Item_Single.this);
        requestQueue.add(stringRequest2);
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

                                    final String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
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
                                            if(installed){
                                                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                                                intent1.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+6"+ mobile_num));
                                                startActivity(intent1);
                                            }else {
                                                Toast.makeText(View_Item_Single.this, "Not Installed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    view_all.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(View_Item_Single.this, Seller_Shop.class);
                                            intent1.putExtra("id", userid);
                                            intent1.putExtra("seller_name", strName);
                                            intent1.putExtra("seller_photo", strPhoto);
                                            intent1.putExtra("seller_location", strDivision);
                                            startActivity(intent1);
                                        }
                                    });

                                    btn_chat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserDetails.chatWith = strName;
                                            Intent intent = new Intent(View_Item_Single.this, Chat.class);
                                            startActivity(intent);
                                        }
                                    });

                                }
                            } else {
                                Toast.makeText(View_Item_Single.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

    private void View_Item() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CART,
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
                                adapter_item = new Item_Single_Adapter(itemList, View_Item_Single.this);
                                adapter_item.notifyDataSetChanged();
                                gridView_item.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Single_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(View_Item_Single.this, View_Item_Single.class);
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
                                Toast.makeText(View_Item_Single.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("user_id", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Item_Single.this);
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
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList2.add(item);
                                }
                                String sold = String.valueOf(itemList2.size());
                                sold_text.setText(sold);

                            } else {
                                Toast.makeText(View_Item_Single.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("seller_id", userid);
                params.put("ad_detail", ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View_Item_Single.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(View_Item_Single.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
