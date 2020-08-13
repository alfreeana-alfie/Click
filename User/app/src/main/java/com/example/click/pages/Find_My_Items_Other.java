package com.example.click.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.adapter.FavouriteAdapter;
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

    private String getId;
    private GridView gridView;
    private Item_UserAdapter adapter_item;
    private List<Item_All_Details> itemList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin);
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.saved_actionbar);

        View view = getSupportActionBar().getCustomView();
        final EditText search_find = view.findViewById(R.id.search_find);
        final Button Button_Search = view.findViewById(R.id.btn_search);

        final ImageButton close_search = view.findViewById(R.id.btn_close);
        ImageButton back_button = view.findViewById(R.id.back_button);

        search_find.setHint("Search");
        search_find.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Button_Search.setVisibility(View.GONE);

                    close_search.setVisibility(View.GONE);
                    Button_Search.setVisibility(View.GONE);
                } else {
                    Button_Search.setVisibility(View.VISIBLE);

                    close_search.setVisibility(View.VISIBLE);
                }
            }
        });
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_find.setFocusable(false);
                search_find.setFocusable(true);
                search_find.setFocusableInTouchMode(true);
                search_find.getText().clear();
                no_result.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                itemList.clear();
                adapter_item = new FavouriteAdapter(itemList, Saved_Searches_Other.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                View_List();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Saved_Searches_Other.this, Homepage.class);
                startActivity(intent);
            }
        });

        Button_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_result.setVisibility(View.GONE);
                Button_Search.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                itemList.clear();
                adapter_item = new FavouriteAdapter(itemList, Saved_Searches_Other.this);
                adapter_item.notifyDataSetChanged();
                gridView.setAdapter(adapter_item);
                final String strAd_Detail = search_find.getText().toString();


                Search(strAd_Detail);

            }
        });
    }


    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Search(final String strAd_detail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
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
                                if (itemList.isEmpty()) {
                                    no_result.setVisibility(View.VISIBLE);
                                } else {
                                    no_result.setVisibility(View.GONE);
                                }
                                adapter_item = new FavouriteAdapter(itemList, Saved_Searches_Other.this);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new FavouriteAdapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(Saved_Searches_Other.this, Saved_Searches_View.class);
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
                                    public void onDeleteClick(final int position) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Saved_Searches_Other.this, R.style.MyDialogTheme);
                                        builder.setTitle("Are you sure?");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

//                                                Intent detailIntent = new Intent(getContext(), Edit_Item.class);
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
                                                                        Toast.makeText(Saved_Searches_Other.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(Saved_Searches_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                RequestQueue requestQueue = Volley.newRequestQueue(Saved_Searches_Other.this);
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
                            } else {
                                Toast.makeText(Saved_Searches_Other.this, "Login Failed! ", Toast.LENGTH_SHORT).show();
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
                params.put("ad_detail", strAd_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void Declare(View v) {
        itemList = new ArrayList<>();
        gridView = v.findViewById(R.id.gridView_item);

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

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_UserAdapter(getContext(), itemList);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_UserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onEditClick(int position) {
                                        Intent detailIntent = new Intent(getContext(), Edit_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(EXTRA_USERID, getId);
                                        detailIntent.putExtra(EXTRA_ID, item.getId());
                                        detailIntent.putExtra(EXTRA_MAIN, item.getMain_category());
                                        detailIntent.putExtra(EXTRA_SUB, item.getSub_category());
                                        detailIntent.putExtra(EXTRA_AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(EXTRA_PRICE, item.getPrice());
                                        detailIntent.putExtra(EXTRA_DIVISION, item.getDivision());
                                        detailIntent.putExtra(EXTRA_DISTRICT, item.getDistrict());
                                        detailIntent.putExtra(EXTRA_IMG_ITEM, item.getPhoto());

                                        getActivity().startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onDeleteClick(final int position) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                        builder.setTitle("Are you sure?");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent detailIntent = new Intent(getContext(), Edit_Item.class);
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
//                                                                        Toast.makeText(getContext(), "Login! ", Toast.LENGTH_SHORT).show();
//                                                                final String id = jsonObject.getString("id").trim();

                                                                    } else {
                                                                        Toast.makeText(getContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                                Toast.makeText(getContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() == null) {
//                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
