package com.example.click;

import android.app.AlertDialog;
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
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View_Item_User extends Fragment implements Item_UserAdapter.OnItemClickListener {

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
    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_item_user, container, false);
        Declare(view);
        SessionManager sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.setting, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search");
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void Declare(View v) {
        itemList = new ArrayList<>();
        gridView = v.findViewById(R.id.gridView_item);
        searchView = v.findViewById(R.id.search_find);
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

    @Override
    public void onEditClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {

    }

}
