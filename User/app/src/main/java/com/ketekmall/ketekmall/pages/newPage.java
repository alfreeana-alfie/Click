package com.ketekmall.ketekmall.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.R;

import java.util.HashMap;
import java.util.Map;


public class newPage extends AppCompatActivity {

    private LinearLayout Home, Packing, Seller, Item_Details;
    private ScrollView Receiver, Pickup;
    private EditText AccountNo, SubscriptionCode, RequireToPickupText, RequireWebHookText, PickupLocationID, PickupLocationName,
            CallerName, CallerPhoneNo, ContactPerson, PhoneNo, PickupEmail, PickupAddress, PickupPostCode,
            PickupDistrict, PickupProvince, PickupCountry, PickupLocation, TotalQuantitytoPickup,
            ReadyAt, CloseAt, ReceiverID, ReceiverName, ReceiverFirstName,
            ReceiverLastName, ReceiverAddress, ReceiverAddress2, ReceiverDistrict, ReceiverProvince,
            ReceiverCity, ReceiverCountry, ReceiverPostCode, ReceiverEmailAddress, ReceiverPhone1,
            ReceiverPhone2, SellerOrderNo, SellerReferenceNo, OrderDate, ShipmentName,
            PostalCode, Currency, CountryCode, Comment, ConsignmentNoteNo,
            TotalWeight, Amount, ItemDescription, PackDescription, PackVol,
            PackLeng, PackWidth, PackHeight, TotalItem, PackDeliveryType;
    private CheckBox RequiretoPickup, RequireWebhook;
    private Spinner ItemType, PaymentType;
    private Button next_home, next_packing, next_item_details, next_receiver, next_seller, next_pickup,
    back_home, back_packing, back_item_details, back_receiver, back_seller, back_pickup;

    private ArrayAdapter<CharSequence> adapter_itemType, adapter_PaymentType;

    String HTTP_PreAcceptanceSingle = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/preacceptancessingle/v1";
    String serverKey_PreAcceptanceSingle = "M1djdzdrbTZod0pXOTZQdnFWVU5jWVpGNU9nUDVzb0M=";

    String HTTP_RoutingCode = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/routingcode/v1";
    String serverKey_RoutingCode = "UVREb1NFZkJqZEd6YXFRWUg2c3BPMTlRbDdTS1I4eEM=";

    String HTTP_GenerateConnote = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/generateconnote/v1";
    String serverKey_GenerateConnote = "S2cwNDRCbkl5OEt4OXF6WlFpQ0dHd1NSN1R2eVV5WDk=";

    String HTTP_GeneratePL9WConnote = "http://stagingsds.pos.com.my/apigateway/as2corporate/api/generatepl9wconnote/v1";
    String serverKey_GeneratePL9WConnote = "U2V1U05OcXdDVThCUnBqalNhdnhxZllQdjE5NDg1YUQ=";
    String contentType ="application/x-www-form-urlencoded";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poslaju_home);
        Declare();
        LayoutVisibility();
        PreAcceptanceSingle();
    }

    private void LayoutVisibility(){
        Home.setVisibility(View.VISIBLE);
        Pickup.setVisibility(View.GONE);
        Receiver.setVisibility(View.GONE);
        Seller.setVisibility(View.GONE);
        Item_Details.setVisibility(View.GONE);
        Packing.setVisibility(View.GONE);

        next_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.VISIBLE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        next_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.VISIBLE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        next_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.VISIBLE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        next_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.VISIBLE);
                Packing.setVisibility(View.GONE);
            }
        });

        next_item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.VISIBLE);
            }
        });

        next_packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreAcceptanceSingle();
            }
        });

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newPage.this, Homepage.class);
                startActivity(intent);
            }
        });

        back_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.VISIBLE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        back_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.VISIBLE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        back_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.VISIBLE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        back_item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.VISIBLE);
                Item_Details.setVisibility(View.GONE);
                Packing.setVisibility(View.GONE);
            }
        });

        back_packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Pickup.setVisibility(View.GONE);
                Receiver.setVisibility(View.GONE);
                Seller.setVisibility(View.GONE);
                Item_Details.setVisibility(View.VISIBLE);
                Packing.setVisibility(View.GONE);
            }
        });

        adapter_itemType = ArrayAdapter.createFromResource(newPage.this, R.array.itemType, android.R.layout.simple_spinner_item);
        adapter_itemType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ItemType.setAdapter(adapter_itemType);

        adapter_PaymentType = ArrayAdapter.createFromResource(newPage.this, R.array.paymentType, android.R.layout.simple_spinner_item);
        adapter_PaymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaymentType.setAdapter(adapter_PaymentType);

        RequiretoPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    RequireToPickupText.setText("TRUE");
                }else {
                    RequireToPickupText.setText("FALSE");
                }
            }
        });

        RequireWebhook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    RequireWebHookText.setText("TRUE");
                }else{
                    RequireWebHookText.setText("FALSE");
                }
            }
        });
    }

    private void Declare(){
        Home = findViewById(R.id.poslaju_home);
        Packing = findViewById(R.id.poslaju_packing);
        Seller = findViewById(R.id.polaju_seller);
        Item_Details = findViewById(R.id.poslaju_item_details);
        Receiver = findViewById(R.id.poslaju_receiver);
        Pickup = findViewById(R.id.poslaju_pickup);

        //Home
        SubscriptionCode = findViewById(R.id.subscriptionCode);
        RequiretoPickup = findViewById(R.id.requireToPickup); //CheckBox
        RequireToPickupText = findViewById(R.id.requireToPickupText);
        RequireWebhook = findViewById(R.id.requireWebhook); //CheckBox
        RequireWebHookText = findViewById(R.id.requireWebhookText);
        AccountNo = findViewById(R.id.accountNo);

        //Pickup
        PickupLocationID = findViewById(R.id.pickupLocationID);
        PickupLocationName = findViewById(R.id.pickupLocationName);
        CallerName = findViewById(R.id.callerName);
        CallerPhoneNo = findViewById(R.id.callerPhone);
        ContactPerson = findViewById(R.id.contactPerson);
        PhoneNo = findViewById(R.id.phoneNo);
        PickupEmail = findViewById(R.id.pickupEmail);
        PickupAddress = findViewById(R.id.pickupAddress);
        PickupPostCode = findViewById(R.id.pickupPostCode);
        PickupDistrict = findViewById(R.id.pickupDistrict);
        PickupProvince = findViewById(R.id.pickupProvince);
        PickupCountry = findViewById(R.id.pickupCountry);
        PickupLocation = findViewById(R.id.pickupLocation);
        TotalQuantitytoPickup = findViewById(R.id.totalQuantityToPickup);
        ReadyAt = findViewById(R.id.readyAt);
        CloseAt = findViewById(R.id.closeAt);

        //Receiver
        ReceiverID = findViewById(R.id.receiverID);
        ReceiverName = findViewById(R.id.receiverName);
        ReceiverFirstName = findViewById(R.id.receiverFirstName);
        ReceiverLastName = findViewById(R.id.receiverLastName);
        ReceiverAddress = findViewById(R.id.receiverAddress);
        ReceiverAddress2 = findViewById(R.id.receiverAddress2);
        ReceiverDistrict = findViewById(R.id.receiverDistrict);
        ReceiverProvince = findViewById(R.id.receiverProvince);
        ReceiverCity = findViewById(R.id.receiverCity);
        ReceiverCountry = findViewById(R.id.receiverCountry);
        ReceiverPostCode = findViewById(R.id.receiverPostCode);
        ReceiverEmailAddress = findViewById(R.id.receiverEmailAddress);
        ReceiverPhone1 = findViewById(R.id.receiverPhone1);
        ReceiverPhone2 = findViewById(R.id.receiverPhone2);

        //Seller
        SellerOrderNo = findViewById(R.id.sellerOrderNo);
        SellerReferenceNo = findViewById(R.id.sellerReferenceNo);
        OrderDate = findViewById(R.id.orderDate);
        ShipmentName = findViewById(R.id.shipmentName);
        PostalCode = findViewById(R.id.postalCode);
        Currency = findViewById(R.id.currency);
        CountryCode = findViewById(R.id.countryCode);
        Comment = findViewById(R.id.comment);

        //Item Details
        ConsignmentNoteNo = findViewById(R.id.consignmentNoteNumber);
        ItemType = findViewById(R.id.itemType); //Dropdown
        TotalWeight = findViewById(R.id.totalWeight);
        PaymentType = findViewById(R.id.paymentType); // DropDown
        Amount = findViewById(R.id.amount);
        ItemDescription = findViewById(R.id.itemDescription);

        //Packing
        PackDescription = findViewById(R.id.packDescription);
        PackVol = findViewById(R.id.packVol);
        PackLeng = findViewById(R.id.packLeng);
        PackWidth = findViewById(R.id.packWidth);
        PackHeight = findViewById(R.id.packHeight);
        TotalItem = findViewById(R.id.totalItem);
        PackDeliveryType = findViewById(R.id.packDeliveryType);

        next_home = findViewById(R.id.next_page_home);
        next_pickup = findViewById(R.id.next_page_pickup);
        next_receiver = findViewById(R.id.next_page_receiver);
        next_seller = findViewById(R.id.next_page_seller);
        next_packing = findViewById(R.id.next_page_packing);
        next_item_details = findViewById(R.id.next_page_item_details);

        back_home = findViewById(R.id.back_home);
        back_pickup = findViewById(R.id.back_pickup);
        back_receiver = findViewById(R.id.back_receiver);
        back_seller = findViewById(R.id.back_seller);
        back_packing = findViewById(R.id.back_packing);
        back_item_details = findViewById(R.id.back_item_details);

    }

    private void PreAcceptanceSingle(){
        //Home
        final String subscriptionCode = this.SubscriptionCode.getText().toString();
        final String requireToPickup = this.RequireToPickupText.getText().toString();
        final String requireWebhook = this.RequireWebHookText.getText().toString();
        final String accountNo = this.AccountNo.getText().toString();

        //Pickup
        final String pickupLocationID = this.PickupLocationID.getText().toString();
        final String pickupLocationName = this.PickupLocationName.getText().toString();
        final String callerName = this.CallerName.getText().toString();
        final String callerPhone = this.CallerPhoneNo.getText().toString();
        final String contactPerson = this.ContactPerson.getText().toString();
        final String phoneNo = this.PhoneNo.getText().toString();
        final String pickupEmail = this.PickupEmail.getText().toString();
        final String pickupAddress = this.PickupAddress.getText().toString();
        String pickupPostCode = this.PickupPostCode.getText().toString();
        final String pickupDistrict = this.PickupDistrict.getText().toString();
        final String pickupProvince = this.PickupProvince.getText().toString();
        final String pickupCountry = this.PickupCountry.getText().toString();
        final String pickupLocation = this.PickupLocation.getText().toString();
        final String totalQuantity = this.TotalQuantitytoPickup.getText().toString();
        final String readyAt = this.ReadyAt.getText().toString();
        final String closeAt = this.CloseAt.getText().toString();

        //Receiver
        final String receiverID = this.ReceiverID.getText().toString();
        final String receiverName = this.ReceiverName.getText().toString();
        final String receiverFirstName = this.ReceiverFirstName.getText().toString();
        final String receiverLastName = this.ReceiverLastName.getText().toString();
        final String receiverAddress = this.ReceiverAddress.getText().toString();
        final String receiverAddress2 = this.ReceiverAddress2.getText().toString();
        final String receiverDistrict = this.ReceiverDistrict.getText().toString();
        final String receiverProvince = this.ReceiverProvince.getText().toString();
        final String receiverCity = this.ReceiverCity.getText().toString();
        final String receiverCountry = this.ReceiverCountry.getText().toString();
        final String receiverPostCode = this.ReceiverPostCode.getText().toString();
        final String receiverEmailAddress = this.ReceiverEmailAddress.getText().toString();
        final String receiverPhone1 = this.ReceiverPhone1.getText().toString();
        final String receiverPhone2 = this.ReceiverPhone2.getText().toString();

        //Seller
        final String sellerOrderNo = this.SellerOrderNo.getText().toString();
        final String sellerReferenceNo = this.SellerReferenceNo.getText().toString();
        final String orderDate = this.OrderDate.getText().toString();
        final String shipmentName = this.ShipmentName.getText().toString();
        final String postalCode = this.PostalCode.getText().toString();
        final String currency = this.Currency.getText().toString();
        final String countryCode = this.CountryCode.getText().toString();
        final String comment = this.Comment.getText().toString();

        //Item Details
        final String consigmentNoteNo = this.ConsignmentNoteNo.getText().toString();
        final String itemType = this.ItemType.getSelectedItem().toString();
        final String totalWeight = this.TotalWeight.getText().toString();
        final String paymentType = this.PaymentType.getSelectedItem().toString();
        final String amount = this.Amount.getText().toString();
        final String itemDescription = this.ItemDescription.getText().toString();

        //Packing
        final String packDescription = this.PackDescription.getText().toString();
        final String packVol = this.PackVol.getText().toString();
        final String packLeng = this.PackLeng.getText().toString();
        final String packWidth = this.PackWidth.getText().toString();
        final String packHeight = this.PackHeight.getText().toString();
        final String totalItem = this.TotalItem.getText().toString();
        final String packDeliveryType = this.PackDeliveryType.getText().toString();

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
                params.put("X-User-Key", serverKey_PreAcceptanceSingle);
                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("subscriptionCode", subscriptionCode);
                params.put("requireToPickup", requireToPickup);
                params.put("requireWebHook", requireWebhook);
                params.put("accountNo", accountNo);
                params.put("callerName", callerName);
                params.put("callerPhone", callerPhone);
                params.put("pickupLocationID", pickupLocationID);
                params.put("pickupLocationName", pickupLocationName);
                params.put("contactPerson", contactPerson);
                params.put("phoneNo", phoneNo);
                params.put("pickupAddress", pickupAddress);
                params.put("ItemType", itemType);
                params.put("totalQuantityToPickup", totalQuantity);
                params.put("totalWeight", totalWeight);
                params.put("consignmentNoteNumber", consigmentNoteNo);
                params.put("PaymentType", paymentType);
                params.put("Amount", amount);
                params.put("readyToCollectAt", readyAt);
                params.put("closeAt", closeAt);
                params.put("receiverName", receiverName);
                params.put("receiverID", receiverID);
                params.put("receiverAddress", receiverAddress);
                params.put("receiverPostCode", receiverPostCode);
                params.put("receiverEmail", receiverEmailAddress);
                params.put("receiverPhone01", receiverPhone1);
                params.put("receiverPhone02", receiverPhone2);
                params.put("sellerReferenceNo", sellerReferenceNo);
                params.put("itemDescription", itemDescription);
                params.put("sellerOrderNo", sellerOrderNo);
                params.put("comments", comment);
                params.put("pickupDistrict", pickupDistrict);
                params.put("pickupProvince", pickupProvince);
                params.put("pickupEmail", pickupEmail);
                params.put("pickupCountry", pickupCountry);
                params.put("pickupLocation", pickupLocation);
                params.put("receiverFname", receiverFirstName);
                params.put("receiverLname", receiverLastName);
                params.put("receiverAddress2", receiverAddress2);
                params.put("receiverDistrict", receiverDistrict);
                params.put("receiverProvince", receiverProvince);
                params.put("receiverCity", receiverCity);
                params.put("receiverCountry", receiverCountry);
                params.put("packDesc", packDescription);
                params.put("packVol", packVol);
                params.put("packLeng", packLeng);
                params.put("postCode", postalCode);
                params.put("ConsignmentNoteNumber", consigmentNoteNo);
                params.put("packWidth", packWidth);
                params.put("packHeight", packHeight);
                params.put("packTotalitem", totalItem);
                params.put("orderDate", orderDate);
                params.put("packDeliveryType", packDeliveryType);
                params.put("ShipmentName", shipmentName);
                params.put("pickupProv", pickupProvince);
                params.put("deliveryProv", "");
                params.put("postalCode", postalCode);
                params.put("currency", currency);
                params.put("countryCode", countryCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void RoutingCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_RoutingCode,
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
                params.put("X-User-Key", serverKey_RoutingCode);
                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Origin", "96000");
                params.put("Destination", "93050");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GenerateConnote(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_GenerateConnote,
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
                params.put("X-User-Key", serverKey_GenerateConnote);
                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("numberOfItem", "");
                params.put("Prefix", "");

                params.put("ApplicationCode", "");
                params.put("Secretid", "");

                params.put("Orderid", "");
                params.put("username", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GeneratePL9WConnote(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_GeneratePL9WConnote,
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
                params.put("X-User-Key", serverKey_GeneratePL9WConnote);
                params.put("Content-Type", contentType);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNo", "");
                params.put("Secretid", "");
                params.put("Orderid", "");
                params.put("Username", "");
                params.put("ConnoteList", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
