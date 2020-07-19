package com.example.click;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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

public class Fragment_View_Item_User extends Fragment implements Adapter_Item_Grid.OnItemClickListener{
    public static final String EXTRA_USERID= "userid";
    public static final String EXTRA_ID= "id";
    public static final String EXTRA_MAIN = "main_category";
    public static final String EXTRA_SUB = "sub_category";
    public static final String EXTRA_AD_DETAIL = "ad_detail";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_ITEM_LOCATION = "item_location";
    public static final String EXTRA_IMG_ITEM = "photo";


    SessionManager sessionManager;
    String getId;
    GridView recyclerView;
    Adapter_Item_Grid adapter_item;
    List<ItemFull> itemList;

    private String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/readuser.php";
    private String URL_EDIT = "https://annkalina53.000webhostapp.com/android_register_login/edititem.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_item, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

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
                                Toast.makeText(getContext(), "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String item_location = object.getString("item_location");
                                    String image_item = object.getString("photo");

                                    ItemFull item = new ItemFull(id,main_category, sub_category, ad_detail, price, item_location, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Adapter_Item_Grid(getContext(), itemList);
                                recyclerView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Adapter_Item_Grid.OnItemClickListener() {
                                    @Override
                                    public void onEditClick(int position) {
                                        Intent detailIntent = new Intent(getContext(), Activity_Edit_Item.class);
                                        ItemFull item = itemList.get(position);

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
                                });
                            } else {
                                Toast.makeText(getContext(), "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Connection Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Connection Error" + error.toString(), Toast.LENGTH_SHORT).show();

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
        Intent detailIntent = new Intent(getContext(), Activity_Edit_Item.class);
        ItemFull item = itemList.get(position);

        detailIntent.putExtra(EXTRA_USERID, getId);
        detailIntent.putExtra(EXTRA_MAIN, item.getMain_category());
        detailIntent.putExtra(EXTRA_SUB, item.getSub_category());
        detailIntent.putExtra(EXTRA_AD_DETAIL, item.getAd_detail());
        detailIntent.putExtra(EXTRA_PRICE, item.getPrice());
        detailIntent.putExtra(EXTRA_ITEM_LOCATION, item.getItem_location());
        detailIntent.putExtra(EXTRA_IMG_ITEM, item.getPhoto());

        getActivity().startActivity(detailIntent);
    }
}
