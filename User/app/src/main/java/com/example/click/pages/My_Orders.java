package com.example.click.pages;

import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.adapter.OrderAdapter;
import com.example.click.data.Item_All_Details;
import com.example.click.data.MySingleton;
import com.example.click.data.SessionManager;

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

    private static String URL_READ_ORDER = "https://ketekmall.com/ketekmall/read_order.php";
    private static String URL_READ_APPROVAL = "https://ketekmall.com/ketekmall/read_approval.php";
    private static String URL_EDIT_RECEIPT= "https://ketekmall.com/ketekmall/edit_receipt.php";
    private static String URL_ACCEPT= "https://ketekmall.com/ketekmall/add_accept.php";
    private static String URL_REJECT= "https://ketekmall.com/ketekmall/add_reject.php";
    private static String URL_DELETE_APPROVAL= "https://ketekmall.com/ketekmall/delete_approval.php";
    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

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
                                        Accept(view);
                                    }

                                    @Override
                                    public void onRejectClick(int position) {
                                        Reject(view);
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

    private void Accept(final View view){
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

                                    final String id = object.getString("id").trim();
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String item_id = object.getString("item_id").trim();
                                    final String receipt_id = object.getString("receipt_id").trim();
                                    final String receipt_date = object.getString("receipt_date").trim();
                                    final String status = object.getString("status").trim();


                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ACCEPT,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString("success");

                                                        if (success.equals("1")) {
                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_EDIT_RECEIPT
                                                                    , new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                        String success = jsonObject.getString("success");
                                                                        if (success.equals("1")) {
                                                                            Toast.makeText(getContext(), "Success Receipt", Toast.LENGTH_SHORT).show();
                                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_DELETE_APPROVAL
                                                                                    , new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        String success = jsonObject.getString("success");
                                                                                        if (success.equals("1")) {
                                                                                            Toast.makeText(getContext(), "Success Delete", Toast.LENGTH_SHORT).show();
                                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
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

                                                                                                                        final String strName = object.getString("name").trim();

                                                                                                                        String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                                                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                                                                            @Override
                                                                                                                            public void onResponse(String s) {
                                                                                                                                try {
                                                                                                                                    JSONObject obj = new JSONObject(s);

                                                                                                                                    TOPIC = obj.getJSONObject(strName).get("token").toString();
                                                                                                                                    NOTIFICATION_TITLE = "KetekMall";
                                                                                                                                    NOTIFICATION_MESSAGE = "Hi, your order is being accepted!";

                                                                                                                                    JSONObject notification = new JSONObject();
                                                                                                                                    JSONObject notifcationBody = new JSONObject();
                                                                                                                                    try {
                                                                                                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                                                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                                                                                                        notification.put("to", TOPIC);
                                                                                                                                        notification.put("data", notifcationBody);
                                                                                                                                        sendNotification(notification);

                                                                                                                                        Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
                                                                                                                                    } catch (JSONException e) {
                                                                                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                                                                                    }
                                                                                                                                } catch (JSONException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }, new Response.ErrorListener() {
                                                                                                                            @Override
                                                                                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                                                                                System.out.println("" + volleyError);
                                                                                                                            }
                                                                                                                        });
                                                                                                                        RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                                                                                                        rQueue.add(request);


                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Toast.makeText(getContext(), "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                                                                                    params.put("id", customer_id);
                                                                                                    return params;
                                                                                                }
                                                                                            };
                                                                                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                                                                            requestQueue.add(stringRequest);
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
                                                                                    params.put("seller_id", seller_id);
                                                                                    params.put("receipt_id", receipt_id);
                                                                                    params.put("item_id", item_id);
                                                                                    params.put("customer_id", customer_id);
                                                                                    return params;
                                                                                }
                                                                            };
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                            requestQueue.add(stringRequest2);
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
                                                                    params.put("id", receipt_id);
                                                                    params.put("seller_id", seller_id);
                                                                    params.put("status", "Accept");
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                            requestQueue.add(stringRequest2);
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
                                            params.put("customer_id", customer_id);
                                            params.put("item_id", id);
                                            params.put("receipt_id", receipt_id);
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

    private void Reject(final View view){
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

                                    final String id = object.getString("id").trim();
                                    final String seller_id = object.getString("seller_id").trim();
                                    final String customer_id = object.getString("customer_id").trim();
                                    final String item_id = object.getString("item_id").trim();
                                    final String receipt_id = object.getString("receipt_id").trim();
                                    final String receipt_date = object.getString("receipt_date").trim();
                                    final String status = object.getString("status").trim();


                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_REJECT,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString("success");

                                                        if (success.equals("1")) {
                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_EDIT_RECEIPT
                                                                    , new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                        String success = jsonObject.getString("success");
                                                                        if (success.equals("1")) {
                                                                            Toast.makeText(getContext(), "Success Receipt", Toast.LENGTH_SHORT).show();
                                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_DELETE_APPROVAL
                                                                                    , new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        String success = jsonObject.getString("success");
                                                                                        if (success.equals("1")) {
                                                                                            Toast.makeText(getContext(), "Success Delete", Toast.LENGTH_SHORT).show();
                                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
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

                                                                                                                        final String strName = object.getString("name").trim();

                                                                                                                        String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                                                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                                                                            @Override
                                                                                                                            public void onResponse(String s) {
                                                                                                                                try {
                                                                                                                                    JSONObject obj = new JSONObject(s);

                                                                                                                                    TOPIC = obj.getJSONObject(strName).get("token").toString();
                                                                                                                                    NOTIFICATION_TITLE = "KetekMall";
                                                                                                                                    NOTIFICATION_MESSAGE = "Sorry, your order is being rejected!";

                                                                                                                                    JSONObject notification = new JSONObject();
                                                                                                                                    JSONObject notifcationBody = new JSONObject();
                                                                                                                                    try {
                                                                                                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                                                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                                                                                                        notification.put("to", TOPIC);
                                                                                                                                        notification.put("data", notifcationBody);
                                                                                                                                        sendNotification(notification);

                                                                                                                                        Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
                                                                                                                                    } catch (JSONException e) {
                                                                                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                                                                                    }
                                                                                                                                } catch (JSONException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }, new Response.ErrorListener() {
                                                                                                                            @Override
                                                                                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                                                                                System.out.println("" + volleyError);
                                                                                                                            }
                                                                                                                        });
                                                                                                                        RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                                                                                                        rQueue.add(request);


                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Toast.makeText(getContext(), "Incorrect Information", Toast.LENGTH_SHORT).show();
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
                                                                                                    params.put("id", customer_id);
                                                                                                    return params;
                                                                                                }
                                                                                            };
                                                                                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                                                                            requestQueue.add(stringRequest);
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
                                                                                    params.put("seller_id", seller_id);
                                                                                    params.put("receipt_id", receipt_id);
                                                                                    params.put("item_id", item_id);
                                                                                    params.put("customer_id", customer_id);
                                                                                    return params;
                                                                                }
                                                                            };
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                            requestQueue.add(stringRequest2);
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
                                                                    params.put("id", receipt_id);
                                                                    params.put("seller_id", seller_id);
                                                                    params.put("status", "Reject");
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                            requestQueue.add(stringRequest2);
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
                                            params.put("customer_id", customer_id);
                                            params.put("item_id", id);
                                            params.put("receipt_id", receipt_id);
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

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
