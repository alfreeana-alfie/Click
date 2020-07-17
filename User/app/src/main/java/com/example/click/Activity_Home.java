package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Item> itemList;
    GridView gridView;
    Adapter_Item_Grid_Search adapter_item;
    private String URL_VIEW = "http://192.168.1.15/android_register_login/readall.php";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Declare();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                Toast.makeText(Activity_Home.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String item_location = object.getString("item_location");
                                    String image_item = object.getString("photo");

                                    Item item = new Item(id, ad_detail, price, item_location, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Adapter_Item_Grid_Search(itemList, Activity_Home.this);
                                gridView.setAdapter(adapter_item);

                            } else {
                                Toast.makeText(Activity_Home.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Activity_Home.this, "Connection Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_Home.this, "Connection Error" + error.toString(), Toast.LENGTH_SHORT).show();

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
                adapter_item.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_profile:
                view.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new Fragment_Home()).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Declare() {
        itemList = new ArrayList<>();
        gridView = findViewById(R.id.gridView_item);
        view = findViewById(R.id.support_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}
