package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.ActivityDelivery;
import com.example.click.Delivery;
import com.example.click.R;
import com.example.click.data.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Row_Add extends AppCompatActivity implements View.OnClickListener {

    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/add_delivery_partone.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_delivery.php";

    LinearLayout layout;
    Button buttonAdd, buttonSubmit;

    ArrayAdapter<CharSequence> adapter_division, adapter_days;
    Spinner spinner_division, spinner_days;
    EditText editText;
    ImageView imageView;
    ArrayList<Delivery> cricketersList = new ArrayList<>();

    SessionManager sessionManager;
    String getId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_mainactivity);

        sessionManager = new SessionManager(Row_Add.this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        layout = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Back");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityDelivery.class));
            }
        });

        addView();
    }

    private void addView() {
        final View view = getLayoutInflater().inflate(R.layout.row_add, null, false);

        editText = view.findViewById(R.id.price);
        imageView = view.findViewById(R.id.btn_close);
        spinner_division = view.findViewById(R.id.spinner_division);
        spinner_days = view.findViewById(R.id.spinner_day);

        adapter_division = ArrayAdapter.createFromResource(view.getContext(), R.array.division, android.R.layout.simple_spinner_item);
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);

        adapter_days = ArrayAdapter.createFromResource(view.getContext(), R.array.days, android.R.layout.simple_spinner_item);
        adapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_days.setAdapter(adapter_days);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
            }
        });
        layout.addView(view);
    }

    private void removeView(View view) {
        layout.removeView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                addView();
                break;

            case R.id.button_submit:
                if (checkIfValidAndRead03()) {
                    Intent intent = new Intent(Row_Add.this, ActivityDelivery.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean checkIfValidAndRead03() {
        cricketersList.clear();
        boolean result = true;

        for (int i = 0; i < layout.getChildCount(); i++) {

            View view = layout.getChildAt(i);

            editText = view.findViewById(R.id.price);
            imageView = view.findViewById(R.id.btn_close);
            spinner_division = view.findViewById(R.id.spinner_division);
            spinner_days = view.findViewById(R.id.spinner_day);

            final String strDivision = spinner_division.getSelectedItem().toString();
            final String strPrice = editText.getText().toString().trim();
            final String strDays = spinner_days.getSelectedItem().toString() + " Days";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
//                                    Toast.makeText(Row_Add.this, "success", Toast.LENGTH_SHORT).show();
//                                    delivery = new Delivery(strDivision, strPrice, strDays);
                                } else {
                                    Toast.makeText(Row_Add.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Row_Add.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Row_Add.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", getId);
                    params.put("division", strDivision);
                    params.put("price", strPrice);
                    params.put("days", strDays);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Row_Add.this);
            requestQueue.add(stringRequest);
        }
        return result;
    }
}
