package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class Fragment_Category_Furniture extends Fragment {

    public static final String ID = "id";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String ITEM_LOCATION = "item_location";
    public static final String PHOTO = "photo";
    private static String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/category/read_category_furniture.php";
    private static String URL_ADD = "https://annkalina53.000webhostapp.com/android_register_login/add_to_fav.php";
    private static String URL_ADD_CART = "https://annkalina53.000webhostapp.com/android_register_login/add_to_cart.php";

    SessionManager sessionManager;
    String getId;
    GridView gridView;
    Item_Adapter_All_View adapter_item;
    List<Item_All_Details> itemList;

    SearchView searchView;
    Spinner spinner_location;
    ImageButton but_loc;
    Button price_sortlowest, price_sorthighest;
    ArrayAdapter<CharSequence> adapter_location;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_view, container, false);
        Declare(view);
        View_Item(view);
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        return view;
    }

    private void Declare(View v) {
        itemList = new ArrayList<>();
        gridView = v.findViewById(R.id.gridView_CarItem);
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

        spinner_location = v.findViewById(R.id.spinner_location);
        but_loc = v.findViewById(R.id.but_loc);
        price_sortlowest = v.findViewById(R.id.price_sortlowest);
        price_sorthighest = v.findViewById(R.id.price_sorthighest);
        price_sorthighest.setVisibility(View.GONE);

        adapter_location = ArrayAdapter.createFromResource(getContext(), R.array.item_location2, android.R.layout.simple_spinner_item);
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adapter_location);

        price_sortlowest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayLowest();
                price_sortlowest.setVisibility(View.GONE);
                price_sorthighest.setVisibility(View.VISIBLE);
            }
        });

        price_sorthighest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayHighest();
                price_sorthighest.setVisibility(View.GONE);
                price_sortlowest.setVisibility(View.VISIBLE);
            }
        });

        but_loc.setVisibility(View.GONE);
        but_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.getFilter().filter(null);
                spinner_location.setSelection(0);
                but_loc.setVisibility(View.GONE);
            }
        });

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    but_loc.setVisibility(View.VISIBLE);
                    adapter_item.getFilter().filter(spinner_location.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter_item.getFilter().filter(null);
            }
        });


    }

    private void View_Item(final View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Activity_All_View.this, "Login! ", Toast.LENGTH_SHORT).show();
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
                                adapter_item = new Item_Adapter_All_View(itemList, getContext());
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter_All_View.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(getContext(), Activity_View_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(PRICE, item.getPrice());
                                        detailIntent.putExtra(ITEM_LOCATION, item.getItem_location());
                                        detailIntent.putExtra(PHOTO, item.getPhoto());

                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoFavClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strItem_location = item.getItem_location();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if(success.equals("1")){
                                                                Toast.makeText(getContext(), "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }catch (JSONException e){
                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("customer_id", getId);
                                                params.put("main_category", strMain_category);
                                                params.put("sub_category", strSub_category);
                                                params.put("ad_detail", strAd_Detail);
                                                params.put("price", String.format("%.2f", strPrice));
                                                params.put("item_location", strItem_location);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest1);
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strItem_location = item.getItem_location();
                                        final String strPhoto = item.getPhoto();

                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response);
                                                            String success = jsonObject1.getString("success");

                                                            if(success.equals("1")){
                                                                Toast.makeText(getContext(), "Add To Cart", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }catch (JSONException e){
                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("customer_id", getId);
                                                params.put("main_category", strMain_category);
                                                params.put("sub_category", strSub_category);
                                                params.put("ad_detail", strAd_Detail);
                                                params.put("price", String.format("%.2f", strPrice));
                                                params.put("item_location", strItem_location);
                                                params.put("photo", strPhoto);
                                                params.put("seller_id", strSeller_id);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest2);
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Activity_All_View.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if (error.getMessage() == null) {
//                            Toast.makeText(Activity_All_View.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Activity_All_View.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

}
