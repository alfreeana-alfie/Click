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
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Fragment_View_Item_User extends Fragment implements Item_Adapter.OnItemClickListener {

    public static final String EXTRA_USERID = "userid";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_MAIN = "main_category";
    public static final String EXTRA_SUB = "sub_category";
    public static final String EXTRA_AD_DETAIL = "ad_detail";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_ITEM_LOCATION = "item_location";
    public static final String EXTRA_IMG_ITEM = "photo";

    private static String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/readuser.php";
    private static String URL_DELETE = "https://annkalina53.000webhostapp.com/android_register_login/delete_item.php";

    SessionManager sessionManager;
    String getId;
    GridView recyclerView;
    Item_Adapter adapter_item;
    List<Item_All_Details> itemList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_item, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());
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
        recyclerView = v.findViewById(R.id.gridView_item);

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
                                adapter_item = new Item_Adapter(getContext(), itemList);
                                adapter_item.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onEditClick(int position) {
                                        Intent detailIntent = new Intent(getContext(), Activity_Edit_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(EXTRA_USERID, getId);
                                        detailIntent.putExtra(EXTRA_ID, item.getId());
                                        detailIntent.putExtra(EXTRA_MAIN, item.getMain_category());
                                        detailIntent.putExtra(EXTRA_SUB, item.getSub_category());
                                        detailIntent.putExtra(EXTRA_AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(EXTRA_PRICE, item.getPrice());
                                        detailIntent.putExtra(EXTRA_ITEM_LOCATION, item.getItem_location());
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

                                                Intent detailIntent = new Intent(getContext(), Activity_Edit_Item.class);
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
                                                                        recyclerView.setAdapter(adapter_item);
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
                params.put("userid", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onEditClick(int position) {
//        Intent detailIntent = new Intent(getContext(), Activity_Edit_Item.class);
//        Item_All_Details item = itemList.get(position);
//
//        detailIntent.putExtra(EXTRA_USERID, getId);
//        detailIntent.putExtra(EXTRA_ID, item.getId());
//        detailIntent.putExtra(EXTRA_MAIN, item.getMain_category());
//        detailIntent.putExtra(EXTRA_SUB, item.getSub_category());
//        detailIntent.putExtra(EXTRA_AD_DETAIL, item.getAd_detail());
//        detailIntent.putExtra(EXTRA_PRICE, item.getPrice());
//        detailIntent.putExtra(EXTRA_ITEM_LOCATION, item.getItem_location());
//        detailIntent.putExtra(EXTRA_IMG_ITEM, item.getPhoto());
//
//        getActivity().startActivity(detailIntent);
    }

    @Override
    public void onDeleteClick(int position) {

    }

}
