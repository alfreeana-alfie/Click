package com.example.click.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.example.click.R;
import com.example.click.adapter.Item_UserAdapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Find_My_Items_Other extends AppCompatActivity {

    public static final String EXTRA_USERID = "user_id";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_MAIN = "main_category";
    public static final String EXTRA_SUB = "sub_category";
    public static final String EXTRA_AD_DETAIL = "ad_detail";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_DIVISION = "division";
    public static final String EXTRA_DISTRICT = "district";
    public static final String EXTRA_IMG_ITEM = "photo";


    private static String URL_VIEW = "https://ketekmall.com/ketekmall/readuser.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_item.php";
    SessionManager sessionManager;
    TextView no_result;
    private String getId;
    private GridView gridView;
    private Item_UserAdapter adapter_item;
    private List<Item_All_Details> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_my_items_other);
        Declare();
        getSession();
        ToolbarSetting();
        View_List();
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
                Intent intent = new Intent(Find_My_Items_Other.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
        gridView = findViewById(R.id.gridView_item);
        no_result = findViewById(R.id.no_result);
    }

    private void View_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(getContext(), "Login! ", Toast.LENGTH_SHORT).show();
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
                                    String max_order = object.getString("max_order");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    item.setMax_order(max_order);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_UserAdapter(itemList, Find_My_Items_Other.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_UserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onEditClick(int position) {
                                        Intent detailIntent = new Intent(Find_My_Items_Other.this, Edit_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra("user_id", getId);
                                        detailIntent.putExtra("id", item.getId());
                                        detailIntent.putExtra("main_category", item.getMain_category());
                                        detailIntent.putExtra("sub_category", item.getSub_category());
                                        detailIntent.putExtra("ad_detail", item.getAd_detail());
                                        detailIntent.putExtra("price", item.getPrice());
                                        detailIntent.putExtra("division", item.getDivision());
                                        detailIntent.putExtra("district", item.getDistrict());
                                        detailIntent.putExtra("photo", item.getPhoto());
                                        detailIntent.putExtra("max_order", item.getMax_order());

                                        detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onDeleteClick(final int position) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Find_My_Items_Other.this, R.style.MyDialogTheme);
                                        builder.setTitle("Are you sure?");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent detailIntent = new Intent(Find_My_Items_Other.this, Edit_Item.class);
                                                final Item_All_Details item = itemList.get(position);

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    String success = jsonObject.getString("success");

                                                                    if (success.equals("1")) {
                                                                        itemList.remove(position);
                                                                        adapter_item.notifyDataSetChanged();
                                                                        gridView.setAdapter(adapter_item);

                                                                    } else {
                                                                        Toast.makeText(Find_My_Items_Other.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(Find_My_Items_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                        params.put("id", item.getId());
                                                        return params;
                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(Find_My_Items_Other.this);
                                                requestQueue.add(stringRequest);
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
                                });
                                adapter_item.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Find_My_Items_Other.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() == null) {
                        } else {
                            Toast.makeText(Find_My_Items_Other.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Find_My_Items_Other.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent4 = new Intent(Find_My_Items_Other.this, Homepage.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent4);
    }

}
