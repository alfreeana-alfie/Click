package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.pages.Row_Add;
import com.example.click.user.Edit_Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Delivery extends AppCompatActivity {


    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/edit_delivery.php";

    ArrayAdapter<CharSequence> adapter_division, adapter_days;
    Spinner spinner_division, spinner_days;
    EditText price;

    Button Accept, Cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_edit);

        final Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        final String item_id = intent.getStringExtra("item_id");
        final String ad_detail = intent.getStringExtra("ad_detail");
        final String division = intent.getStringExtra("division");
        final String price_text = intent.getStringExtra("price");
        String days = intent.getStringExtra("days");

        price = findViewById(R.id.price);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_days = findViewById(R.id.spinner_day);
        Accept = findViewById(R.id.btn_accept);
        Cancel = findViewById(R.id.btn_cancel);

        adapter_division = ArrayAdapter.createFromResource(this, R.array.division, android.R.layout.simple_spinner_item);
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);

        adapter_days = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_days.setAdapter(adapter_days);

        price.setText(price_text);

        int division_position = adapter_division.getPosition(division);
        int days_position = adapter_division.getPosition(days);
        spinner_division.setSelection(division_position);
        spinner_days.setSelection(days_position);

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String division_edit = spinner_division.getSelectedItem().toString();
                final String price_edit = price.getText().toString();
                final String days_edit = spinner_days.getSelectedItem().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    if (success.equals("1")) {
                                        Toast.makeText(Edit_Delivery.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(Edit_Delivery.this, ActivityDelivery.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent1.putExtra("item_id", item_id);
                                        intent1.putExtra("ad_detail", ad_detail);
                                        startActivity(intent1);
                                    } else {
                                        Toast.makeText(Edit_Delivery.this, "Failed to Update", Toast.LENGTH_SHORT).show();
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
                        params.put("division", division_edit);
                        params.put("price", price_edit);
                        params.put("days", days_edit);
                        params.put("id", id);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Edit_Delivery.this);
                requestQueue.add(stringRequest);

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Edit_Delivery.this, ActivityDelivery.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
