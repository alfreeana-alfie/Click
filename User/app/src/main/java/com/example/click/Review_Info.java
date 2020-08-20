package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class Review_Info extends AppCompatActivity {

    private static String URL_READ_REVIEW = "https://ketekmall.com/ketekmall/read_review.php";

    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_info);

        ToolbarSetting();

        Intent intent1 = getIntent();
        String item_id = intent1.getStringExtra("item_id");

        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Review_Info.this));

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Review_Info.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_feed:
                        Intent intent5 = new Intent(Review_Info.this, Feed_page.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent5);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Review_Info.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Review_Info.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
        Read_Review(item_id);

    }

    private void ToolbarSetting(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Review");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review_Info.this, View_Item_Single.class);

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
                String item_id = intent4.getStringExtra("item_id");

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

                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    private void Read_Review(final String item_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_REVIEW,
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
                                    String customer_id = object.getString("customer_id").trim();
                                    String customer_name = object.getString("customer_name").trim();
                                    final String item_id = object.getString("item_id").trim();
                                    String review = object.getString("review").trim();
                                    Float rating = Float.valueOf(object.getString("rating").trim());

                                    Review review1 = new Review(customer_name, review, rating);
                                    reviewList.add(review1);
                                }
                                reviewAdapter = new ReviewAdapter(Review_Info.this, reviewList);
                                recyclerView.setAdapter(reviewAdapter);

                            } else {
                                Toast.makeText(Review_Info.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("item_id", item_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Review_Info.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(Review_Info.this, View_Item_Single.class);

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
        String item_id = intent4.getStringExtra("item_id");

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

        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
    }
}
