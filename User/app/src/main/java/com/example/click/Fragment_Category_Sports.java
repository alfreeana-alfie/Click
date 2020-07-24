package com.example.click;

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

public class Fragment_Category_Sports extends Fragment {

    public static final String ID = "id";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String ITEM_LOCATION = "item_location";
    public static final String PHOTO = "photo";
    private static String URL_VIEW = "https://annkalina53.000webhostapp.com/android_register_login/category/read_category_sports.php";

    SessionManager sessionManager;
    String getId;
    GridView recyclerView;
    Item_Adapter_All_View adapter_item;
    List<Item_All_Details> itemList;

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
        recyclerView = v.findViewById(R.id.gridView_CarItem);
    }

    private void View_Item(View view) {
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
                                recyclerView.invalidateViews();
                                recyclerView.setAdapter(adapter_item);

                            } else {
                                Toast.makeText(getContext(), "Failed! ", Toast.LENGTH_SHORT).show();
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
