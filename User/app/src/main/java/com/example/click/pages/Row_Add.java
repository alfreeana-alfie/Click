package com.example.click.pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.ActivityDelivery;
import com.example.click.Delivery;
import com.example.click.Feed_page;
import com.example.click.Noti_Page;
import com.example.click.R;
import com.example.click.data.SessionManager;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Row_Add extends AppCompatActivity implements View.OnClickListener {

    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/add_delivery_partone.php";
    private static String URL_EDIT_DEL_STATUS = "https://ketekmall.com/ketekmall/edit_delivery_status.php";
    private static String URL_DELETE = "https://ketekmall.com/ketekmall/delete_delivery.php";

    LinearLayout layout;
    Button buttonAdd, buttonSubmit;

    ArrayAdapter<CharSequence> adapter_division, adapter_days;
    Spinner spinner_division, spinner_days;
    EditText editText, edit_day;
    ImageView imageView;
    ArrayList<Delivery> cricketersList = new ArrayList<>();
    ProgressBar loading;

    SessionManager sessionManager;
    String getId;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_mainactivity);

        sessionManager = new SessionManager(Row_Add.this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        loading = findViewById(R.id.loading);

        layout = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Row_Add.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(Row_Add.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Row_Add.this, Noti_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Row_Add.this, Edit_Profile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.back));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                final String item_id = intent.getStringExtra("item_id");
                final String ad_detail = intent.getStringExtra("ad_detail");

                Intent intent1 = new Intent(Row_Add.this, ActivityDelivery.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.putExtra("item_id", item_id);
                intent1.putExtra("ad_detail", ad_detail);
                startActivity(intent1);
            }
        });

        addView();
    }

    private void addView() {
        final View view = getLayoutInflater().inflate(R.layout.row_add, null, false);

        editText = view.findViewById(R.id.price);
        edit_day = view.findViewById(R.id.days);
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
                if (checkIfValidAndRead()) {
                    Intent intent = getIntent();
                    final String item_id = intent.getStringExtra("item_id");
                    final String ad_detail = intent.getStringExtra("ad_detail");

                    Intent intent1 = new Intent(Row_Add.this, ActivityDelivery.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.putExtra("item_id", item_id);
                    intent1.putExtra("ad_detail", ad_detail);
                    startActivity(intent1);
                }
                break;
        }
    }

    private boolean checkIfValidAndRead() {
        cricketersList.clear();
        boolean result = true;

        for (int i = 0; i < layout.getChildCount(); i++) {
            loading.setVisibility(View.VISIBLE);
            buttonSubmit.setVisibility(View.GONE);

            View view = layout.getChildAt(i);

            Intent intent = getIntent();
            final String item_id = intent.getStringExtra("item_id");
            final String ad_detail = intent.getStringExtra("ad_detail");

            editText = view.findViewById(R.id.price);
            edit_day = view.findViewById(R.id.days);
            imageView = view.findViewById(R.id.btn_close);
            spinner_division = view.findViewById(R.id.spinner_division);
            spinner_days = view.findViewById(R.id.spinner_day);

            final String strDivision = spinner_division.getSelectedItem().toString();
            final String strPrice = editText.getText().toString().trim();
            final String strDaysText= edit_day.getText().toString().trim();
            final String strDays = spinner_days.getSelectedItem().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    loading.setVisibility(View.GONE);
                                    buttonSubmit.setVisibility(View.VISIBLE);
                                    onEditStatus(item_id);
//                                    Toast.makeText(Row_Add.this, "success", Toast.LENGTH_SHORT).show();
//                                    delivery = new Delivery(strDivision, strPrice, strDays);
                                } else {
                                    loading.setVisibility(View.GONE);
                                    buttonSubmit.setVisibility(View.VISIBLE);
                                    Toast.makeText(Row_Add.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                loading.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                                Toast.makeText(Row_Add.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            buttonSubmit.setVisibility(View.VISIBLE);
                            Toast.makeText(Row_Add.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", getId);
                    params.put("division", strDivision);
                    params.put("price", strPrice);
                    params.put("item_id", item_id);
                    params.put("days", strDaysText);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Row_Add.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
        return result;
    }

    private void onEditStatus(final String item_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_DEL_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
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
                params.put("id", item_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Row_Add.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        final String item_id = intent.getStringExtra("item_id");
        final String ad_detail = intent.getStringExtra("ad_detail");

        Intent intent1 = new Intent(Row_Add.this, ActivityDelivery.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("item_id", item_id);
        intent1.putExtra("ad_detail", ad_detail);
        startActivity(intent1);
    }
}
