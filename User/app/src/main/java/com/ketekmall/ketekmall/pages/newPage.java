package com.ketekmall.ketekmall.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.HolderAPI;
import com.ketekmall.ketekmall.Post;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class newPage extends AppCompatActivity {
    // Header
    String contentType ="application/x-www-form-urlencoded";

    // Variables
    TextView Status;

    String ConsigmentNoteNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poslaju_home);
        Declare();
//        RoutingCode();
        GenConnote2();
    }

    private void Declare(){
        Status = findViewById(R.id.status);
    }

//    String HTTP_PreAcceptanceSingle = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
//    String serverKey_PreAcceptanceSingle = "M1djdzdrbTZod0pXOTZQdnFWVU5jWVpGNU9nUDVzb0M=";
//    private void PreAcceptanceSingle(){
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_PreAcceptanceSingle,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("jsonObjectRequest", response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
//                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
//                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
//                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
//                        Log.i("STAGINGERROR", error.toString());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("X-User-Key", serverKey_PreAcceptanceSingle);
//                params.put("Content-Type", contentType);
//                return params;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=UTF-8";
//            }
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                return super.parseNetworkResponse(response);
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("subscriptionCode", subscriptionCode);
//                params.put("requireToPickup", requireToPickup);
//                params.put("requireWebHook", requireWebhook);
//                params.put("accountNo", accountNo);
//                params.put("callerName", callerName);
//                params.put("callerPhone", callerPhone);
//                params.put("pickupLocationID", pickupLocationID);
//                params.put("pickupLocationName", pickupLocationName);
//                params.put("contactPerson", contactPerson);
//                params.put("phoneNo", phoneNo);
//                params.put("pickupAddress", pickupAddress);
//                params.put("ItemType", itemType);
//                params.put("totalQuantityToPickup", totalQuantity);
//                params.put("totalWeight", totalWeight);
//                params.put("consignmentNoteNumber", consigmentNoteNo);
//                params.put("PaymentType", paymentType);
//                params.put("Amount", amount);
//                params.put("readyToCollectAt", readyAt);
//                params.put("closeAt", closeAt);
//                params.put("receiverName", receiverName);
//                params.put("receiverID", receiverID);
//                params.put("receiverAddress", receiverAddress);
//                params.put("receiverPostCode", receiverPostCode);
//                params.put("receiverEmail", receiverEmailAddress);
//                params.put("receiverPhone01", receiverPhone1);
//                params.put("receiverPhone02", receiverPhone2);
//                params.put("sellerReferenceNo", sellerReferenceNo);
//                params.put("itemDescription", itemDescription);
//                params.put("sellerOrderNo", sellerOrderNo);
//                params.put("comments", comment);
//                params.put("pickupDistrict", pickupDistrict);
//                params.put("pickupProvince", pickupProvince);
//                params.put("pickupEmail", pickupEmail);
//                params.put("pickupCountry", pickupCountry);
//                params.put("pickupLocation", pickupLocation);
//                params.put("receiverFname", receiverFirstName);
//                params.put("receiverLname", receiverLastName);
//                params.put("receiverAddress2", receiverAddress2);
//                params.put("receiverDistrict", receiverDistrict);
//                params.put("receiverProvince", receiverProvince);
//                params.put("receiverCity", receiverCity);
//                params.put("receiverCountry", receiverCountry);
//                params.put("packDesc", packDescription);
//                params.put("packVol", packVol);
//                params.put("packLeng", packLeng);
//                params.put("postCode", postalCode);
//                params.put("ConsignmentNoteNumber", consigmentNoteNo);
//                params.put("packWidth", packWidth);
//                params.put("packHeight", packHeight);
//                params.put("packTotalitem", totalItem);
//                params.put("orderDate", orderDate);
//                params.put("packDeliveryType", packDeliveryType);
//                params.put("ShipmentName", shipmentName);
//                params.put("pickupProv", pickupProvince);
//                params.put("deliveryProv", "");
//                params.put("postalCode", postalCode);
//                params.put("currency", currency);
//                params.put("countryCode", countryCode);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    String HTTP_PoslajuDomesticbyPostcode = "https://apis.pos.com.my/apigateway/as2corporate/api/poslajubypostcodedomestic/v1";
    private void PoslajuDomesticbyPostcode(){
        String postcodeFrom = "96000";
        String postcodeTo = "93400";
        String Weight = "0.5";

        String API = HTTP_PoslajuDomesticbyPostcode + "?postcodeFrom=" + postcodeFrom + "&postcodeTo=" + postcodeTo + "&Weight=" + Weight;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                        try{
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String totalAmount       = jsonobject.getString("totalAmount");
                                Log.i("jsonObjectRequest", totalAmount);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.i("jsonObjectRequest", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PoslajuDomesticbyPostcode);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_RoutingCode = "https://apis.pos.com.my/apigateway/as01/api/routingcode/v1";
    String serverKey_PoslajuDomesticbyPostcode = "N1hHVHJFRW95cjRkQ0NyR3dialdrZUF4NGxaNm9Na1U=";
    String HTTP_RoutingCodeStage = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/routingcode/v1";

    String serverKey_RoutingCode = "aWFGekJBMXUyRFFmTmNxUEpmcXhwR0hXYnY5cWdCTmE=";
    String serverKey_RoutingCodeStage = "UVREb1NFZkJqZEd6YXFRWUg2c3BPMTlRbDdTS1I4eEM=";
    private void RoutingCode(){
        String API = HTTP_RoutingCode + "?Origin=93050&Destination=96000";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                                JSONObject jsonObject = new JSONObject(response);
                                String RoutingCode       = jsonObject.getString("RoutingCode");

                                Log.i("ObjectRequest", RoutingCode);
//                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.i("jsonObjectRequest", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_RoutingCode);
                params.put("Content-Type", "application/form-data");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    String HTTP_GenerateConnote = "https://apis.pos.com.my/apigateway/as01/api/genconnote/v1";
    String serverKey_GenerateConnote = "MmpkbDI0MFpuTVpuZDRXb3J0VUk4M25ZTkY1a2NqSFU=";
    public void GenConnote(){
        String numberOfItem = "1";
        String Prefix = "ER";
        String ApplicationCode = "hnm";
        String Secretid = "hnm@321";
        String Orderid = "4545";
        String username = "HMNNadhir";

        String API = HTTP_GenerateConnote
                + "?numberOfItem=1&Prefix=ER&ApplicationCode=hnm&Secretid=hnm@321&Orderid=99123&username=HMNNadhir";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("jsonObjectRequest", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                Log.i("jsonObjectRequest", error.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-User-Key", serverKey_GenerateConnote);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GenConnote2(){
        String API = "https://apis.pos.com.my/apigateway/as01/api/genconnote/v1" +
                "?numberOfItem=1" +
                "&Prefix=ER" +
                "&ApplicationCode=hnm" +
                "&Secretid=hnm@321" +
                "&Orderid='0032MNQFDA'" +
                "&username=HMNNadhir";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ObjectRequest", response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String ConnoteNo       = jsonObject.getString("ConnoteNo");
                            String StatusCode      = jsonObject.getString("StatusCode");
                            String Message         = jsonObject.getString("Message");

                            Log.i("ObjectRequest", StatusCode);
                            Log.i("ObjectRequest", ConnoteNo);
                            Log.i("ObjectRequest", Message);
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.i("jsonObjectRequest", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
//                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", "MmpkbDI0MFpuTVpuZDRXb3J0VUk4M25ZTkY1a2NqSFU=");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
