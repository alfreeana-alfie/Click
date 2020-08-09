package com.example.click;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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

public class My_Orders extends Fragment {

    public static final String ID = "id";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String ITEM_LOCATION = "district";
    public static final String PHOTO = "photo";

    private static String URL_VIEW = "https://ketekmall.com/ketekmall/readfav.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_fav.php";
    private static String URL_READ_ORDER = "https://ketekmall.com/ketekmall/read_order.php";
    private static String URL_READ_ITEM = "https://ketekmall.com/ketekmall/read_item.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_READ_APPROVAL = "https://ketekmall.com/ketekmall/read_approval.php";
    private static String URL_APPROVAL= "https://ketekmall.com/ketekmall/edit_approval.php";


    GridView gridView;
    OrderAdapter adapter_item;
    List<Item_All_Details> itemList;
    String getId;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        Approval_List(view);
        return view;
    }

    private void Declare(View v) {
        itemList = new ArrayList<>();
        gridView = v.findViewById(R.id.gridView_item);
//        ScrollView scrollView = v.findViewById(R.id.grid_category);
//
//        scrollView.setVisibility(View.GONE);
    }

    private void Approval_List(final View view){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_ORDER,
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

                                    final String id = object.getString("id").trim();
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String main_category = object.getString("main_category").trim();
                                    final String sub_category = object.getString("sub_category").trim();
                                    final String ad_detail = object.getString("ad_detail").trim();
                                    final Double price = Double.valueOf(object.getString("price").trim());
                                    final String division = object.getString("division");
                                    final String district = object.getString("district");
                                    final String image_item = object.getString("photo");
                                    final String item_id = object.getString("item_id").trim();


                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, String.format("%.2f", price), division, district, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new OrderAdapter(getContext(), itemList);
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                                    @Override
                                    public void onAcceptClick(int position) {
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_READ_APPROVAL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("success");
                                                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                            if(success.equals("1")){
                                                                for(int i = 0; i < jsonArray.length(); i++){
                                                                    JSONObject object = jsonArray.getJSONObject(i);

                                                                    String id = object.getString("id").trim();
                                                                    String seller_id = object.getString("seller_id").trim();
                                                                    String customer_id = object.getString("customer_id").trim();
                                                                    final String item_id = object.getString("item_id").trim();
                                                                    String receipt_id = object.getString("receipt_id").trim();
                                                                    String receipt_date = object.getString("receipt_date").trim();
                                                                    String status = object.getString("status").trim();

                                                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_APPROVAL,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        String success = jsonObject.getString("success");

                                                                                        if (success.equals("1")) {
                                                                                            Toast.makeText(getContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
                                                                                        } else {
                                                                                            Toast.makeText(getContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {

                                                                        }
                                                                    }){
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String, String> params = new HashMap<>();
                                                                            params.put("seller_id", getId);
                                                                            params.put("status", "Accept");
                                                                            params.put("item_id", item_id);
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                    requestQueue.add(stringRequest1);
                                                                }
                                                            }

                                                        }catch (JSONException e){

                                                        }


                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("seller_id", getId);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest1);
                                    }

                                    @Override
                                    public void onRejectClick(int position) {
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_READ_APPROVAL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("seller_id", getId);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest1);
                                    }
                                });
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
                        Toast.makeText(getContext(), "JSON Parsing Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

}
