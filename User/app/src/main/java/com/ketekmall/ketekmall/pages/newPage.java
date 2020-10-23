package com.ketekmall.ketekmall.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.adapter.BoostAdapter;
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.MySingleton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static java.lang.Boolean.FALSE;


public class newPage extends AppCompatActivity {

    String API = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
    String serverKey = "M1djdzdrbTZod0pXOTZQdnFWVU5jWVpGNU9nUDVzb0M=";
    String contentType ="application/x-www-form-urlencoded";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONArray array = new JSONArray();
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notification.put("subscriptionCode", "admin@ketekmall.com");
            notifcationBody.put("requireToPickup", "FALSE");
//            notifcationBody.put("requireWebHook", "TRUE");
//            notifcationBody.put("accountNo", "8800001234");
//            notifcationBody.put("callerName", "Seller A");
//            notifcationBody.put("callerPhone", "0388889999");
//            notifcationBody.put("pickupLocationID", "M00012");
//            notifcationBody.put("pickupLocationName", "Baby Boo Sdn Bhd");
//            notifcationBody.put("contactPerson", "En Halim");
//            notifcationBody.put("phoneNo", "0123699717");
//            notifcationBody.put("pickupAddress", "11A, 3N/USJ9,SUBANG JAYA,SELANGOR");
//            notifcationBody.put("ItemType", "0");
//            notifcationBody.put("totalQuantityToPickup", "1");
//            notifcationBody.put("totalWeight", "2.01");
//            notifcationBody.put("consignmentNoteNumber", "EM744780967MY");
//            notifcationBody.put("PaymentType", "2");
//            notifcationBody.put("Amount", "20.50");
//            notifcationBody.put("readyToCollectAt", "12:00 PM");
//            notifcationBody.put("closeAt", "06:00 PM");
//            notifcationBody.put("receiverName", "Ahmad Suhaili");
//            notifcationBody.put("receiverID", "");
//            notifcationBody.put("receiverAddress", "AT-1-8 Taman Tun Sardon");
//            notifcationBody.put("receiverPostCode", "11700");
//            notifcationBody.put("receiverEmail", "");
//            notifcationBody.put("receiverPhone01", "0123966717");
//            notifcationBody.put("receiverPhone02", "0388886666");
//            notifcationBody.put("sellerReferenceNo", "");
//            notifcationBody.put("itemDescription", "");
//            notifcationBody.put("sellerOrderNo", "");
//            notifcationBody.put("comments", "Perlu van, barang besar");
//            notifcationBody.put("pickupDistrict", "SUBANG JAYA");
//            notifcationBody.put("pickupProvince", "Selangor");
//            notifcationBody.put("pickupEmail", "abc@gmail.com.my");
//            notifcationBody.put("pickupCountry", "MY");
//            notifcationBody.put("pickupLocation", "");
//            notifcationBody.put("receiverFname", "Ahmad Suhaili");
//            notifcationBody.put("receiverLname", "Mohamad");
//            notifcationBody.put("receiverAddress2", "");
//            notifcationBody.put("receiverDistrict", "Gelugor-11700");
//            notifcationBody.put("receiverProvince", "Penang");
//            notifcationBody.put("receiverCity", "Gelugor");
//            notifcationBody.put("receiverCountry", "MY");
//            notifcationBody.put("packDesc", "Book|Bag");
//            notifcationBody.put("packVol", "");
//            notifcationBody.put("packLeng", "");
//            notifcationBody.put("postCode", "56000");
//            notifcationBody.put("ConsignmentNoteNumber", "EM744780967MY");
//            notifcationBody.put("packWidth", "");
//            notifcationBody.put("packHeight", "");
//            notifcationBody.put("packTotalitem", "");
//            notifcationBody.put("orderDate", "");
//            notifcationBody.put("packDeliveryType", "");
//            notifcationBody.put("ShipmentName", "PosLaju");
//            notifcationBody.put("pickupProv", "");
//            notifcationBody.put("deliveryProv", "");
//
//            notifcationBody.put("postalCode", "");
//            notifcationBody.put("currency", "MYR");
//            notifcationBody.put("countryCode", "MY");
//            notifcationBody.put("pickupDate", "2019-07-11");

        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }

//        array.put(notifcationBody);
//        Testing4(array);
//        sendData(notifcationBody);
        Testing3();
    }

    private void sendData(JSONObject jsonObject){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(API, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());

                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);

                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey);
//                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("requireToPickup", "FALSE");
//                params.put("Content-Type", contentType);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
    }

    private void Testing2(){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(API);
        post.addHeader(contentType,serverKey);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("requireToPickup", "FALSE"));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Testing3(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());

                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);

                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());

                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);

                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // take the statusCode here.
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("subscriptionCode", "admin@ketekmall.com");
                params.put("requireToPickup", "FALSE");
            params.put("requireWebHook", "TRUE");
            params.put("accountNo", "8800001234");
            params.put("callerName", "Seller A");
            params.put("callerPhone", "0388889999");
            params.put("pickupLocationID", "M00012");
            params.put("pickupLocationName", "Baby Boo Sdn Bhd");
            params.put("contactPerson", "En Halim");
            params.put("phoneNo", "0123699717");
            params.put("pickupAddress", "11A, 3N/USJ9,SUBANG JAYA,SELANGOR");
            params.put("ItemType", "0");
            params.put("totalQuantityToPickup", "1");
            params.put("totalWeight", "2.01");
            params.put("consignmentNoteNumber", "EM744780967MY");
            params.put("PaymentType", "2");
            params.put("Amount", "20.50");
            params.put("readyToCollectAt", "12:00 PM");
            params.put("closeAt", "06:00 PM");
            params.put("receiverName", "Ahmad Suhaili");
            params.put("receiverID", "");
            params.put("receiverAddress", "AT-1-8 Taman Tun Sardon");
            params.put("receiverPostCode", "11700");
            params.put("receiverEmail", "");
            params.put("receiverPhone01", "0123966717");
            params.put("receiverPhone02", "0388886666");
            params.put("sellerReferenceNo", "");
            params.put("itemDescription", "");
            params.put("sellerOrderNo", "");
            params.put("comments", "Perlu van, barang besar");
            params.put("pickupDistrict", "SUBANG JAYA");
            params.put("pickupProvince", "Selangor");
            params.put("pickupEmail", "abc@gmail.com.my");
            params.put("pickupCountry", "MY");
            params.put("pickupLocation", "");
            params.put("receiverFname", "Ahmad Suhaili");
            params.put("receiverLname", "Mohamad");
            params.put("receiverAddress2", "");
            params.put("receiverDistrict", "Gelugor-11700");
            params.put("receiverProvince", "Penang");
            params.put("receiverCity", "Gelugor");
            params.put("receiverCountry", "MY");
            params.put("packDesc", "Book|Bag");
            params.put("packVol", "");
            params.put("packLeng", "");
            params.put("postCode", "56000");
            params.put("ConsignmentNoteNumber", "EM744780967MY");
            params.put("packWidth", "");
            params.put("packHeight", "");
            params.put("packTotalitem", "");
            params.put("orderDate", "");
            params.put("packDeliveryType", "");
            params.put("ShipmentName", "PosLaju");
            params.put("pickupProv", "");
            params.put("deliveryProv", "");

            params.put("postalCode", "");
            params.put("currency", "MYR");
            params.put("countryCode", "MY");
            params.put("pickupDate", "2019-07-11");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Testing4(JSONArray jsonArray){
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, API, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Get Final response
                        Log.i("jsonObjectRequest","success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(newPage.this, "Request error", Toast.LENGTH_LONG).show();
                Log.i("jsonObjectRequest", error.toString());

                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);

                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
            //Important part to convert response to JSON Array Again
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                String responseString;
                JSONArray array = new JSONArray();
                if (response != null) {

                    try {
                        responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj = new JSONObject(responseString);
                        (array).put(obj);
                    } catch (Exception ex) {

                    }
                }
                //return array;
                return Response.success(array, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request_json);
    }
}
