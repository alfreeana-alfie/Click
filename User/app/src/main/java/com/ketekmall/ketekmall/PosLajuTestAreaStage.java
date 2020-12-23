package com.ketekmall.ketekmall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PosLajuTestAreaStage extends AppCompatActivity {
    // Variables
    TextView Status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poslaju_home);
        Status = findViewById(R.id.status);

        // Routing Code = KCU-SB-SBW
        // ConnoteNo = ER000249760MY
        PreAcceptanceSingle();

//        PoslajuDomesticbyPostcode();
//        RoutingCode();
//        GenConnote();
    }

    // PreAcceptance Single
    String contentType ="application/x-www-form-urlencoded";
    String HTTP_PreAcceptanceSingle = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
    String serverKey_PreAcceptanceSingle = "M1djdzdrbTZod0pXOTZQdnFWVU5jWVpGNU9nUDVzb0M=";
    private void PreAcceptanceSingle(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_PreAcceptanceSingle,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsonObjectRequest", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_PreAcceptanceSingle);
                return params;
            }

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("subscriptionCode", "admin@ketekmall.com");
                params.put("requireToPickup", "FALSE");
                params.put("requireWebHook", "FALSE");
                params.put("accountNo", "4799110862895245");
                params.put("callerName", "Alfreeana Alfie");
                params.put("callerPhone", "0138940023");
                params.put("pickupLocationID", "M34123998");
                params.put("pickupLocationName", "Sibu");
                params.put("contactPerson", "0138940023");
                params.put("phoneNo", "0138940023");
                params.put("pickupAddress", "LOT245, NO.3G, LORONG SIBU JAYA 6");
                params.put("ItemType", "2");
                params.put("totalQuantityToPickup", "2");
                params.put("totalWeight", "1.01");
                params.put("PaymentType", "2");
                params.put("Amount", "10.00");
                params.put("readyToCollectAt", "12.00PM");
                params.put("closeAt", "6.00PM");
                params.put("receiverName", "Kalina Ann");
                params.put("receiverID", "");
                params.put("receiverAddress", "LO24, NO.3, LORONG SIBU JAYA 6");
                params.put("receiverPostCode", "96000");
                params.put("receiverEmail", "");
                params.put("receiverPhone01", "0138940023");
                params.put("receiverPhone02", "0189232002");
                params.put("sellerReferenceNo", "");
                params.put("itemDescription", "");
                params.put("sellerOrderNo", "");
                params.put("comments", "Fragile");
                params.put("pickupDistrict", "SIBU JAYA");
                params.put("pickupProvince", "SIBU");
                params.put("pickupEmail", "annkalina53@gmail.com");
                params.put("pickupCountry", "MY");
                params.put("pickupLocation", "");
                params.put("receiverFname", "Kalina");
                params.put("receiverLname", "Ann");
                params.put("receiverAddress2", "");
                params.put("receiverDistrict", "Lorong Sibu Jaya 6");
                params.put("receiverProvince", "Sibu Jaya");
                params.put("receiverCity", "Sibu");
                params.put("receiverCountry", "MY");
                params.put("packDesc", "");
                params.put("packVol", "");
                params.put("packLeng", "");
                params.put("postCode", "");
                params.put("ConsignmentNoteNumber", "ER000249760MY");
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String HTTP_PoslajuDomesticbyPostcode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/poslajudomesticbypostcode/v1";
    String serverKey_PoslajuDomesticbyPostcode = "a1g2cmM2VmowNm00N1lZekFmTGR0MldpRHhKaFRHSks=";
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
                        Log.i("PostcodeObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("PostcodeObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("PostcodeObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
//                        Toast.makeText(PosLajuTestAreaStage.this, "Request error", Toast.LENGTH_LONG).show();
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

    String HTTP_RoutingCode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/routingcode/v1";
    String serverKey_RoutingCode = "UVREb1NFZkJqZEd6YXFRWUg2c3BPMTlRbDdTS1I4eEM=";
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
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RoutingCodeObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("RoutingCodeObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("RoutingCodeObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
//                        Toast.makeText(PosLajuTestAreaStage.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("STAGINGERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("X-User-Key", serverKey_RoutingCode);
//                params.put("Content-Type", "application/xml");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PosLajuTestAreaStage.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0));
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    String HTTP_GenerateConnote = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/generateconnote/v1";
    String serverKey_GenerateConnote = "S2cwNDRCbkl5OEt4OXF6WlFpQ0dHd1NSN1R2eVV5WDk=";
    public void GenConnote(){
        String numberOfItem = "1";
        String Prefix = "ERC";
        String ApplicationCode = "HNM";
        String Secretid = "HM@$343";
        String Orderid = "454";
        String username = "HMNNadhir";

        String API = HTTP_GenerateConnote +
                "?numberOfItem=" + numberOfItem +
                "&Prefix=" + Prefix +
                "&ApplicationCode=" + ApplicationCode +
                "&Secretid=" + Secretid +
                "&Orderid=" + Orderid +
                "&username=" + username;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("jsonObjectRequest", response);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GenConnoteObjectRequest", "Status Code " + error.networkResponse.statusCode);
                Log.i("GenConnoteObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("GenConnoteObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                Log.i("GenConnoteObjectRequest", error.toString());

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
}
