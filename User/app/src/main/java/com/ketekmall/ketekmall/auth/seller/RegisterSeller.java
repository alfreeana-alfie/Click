package com.ketekmall.ketekmall.auth.seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.activities.products.AddNewProduct;
import com.ketekmall.ketekmall.configs.Setup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Constant.sBANK_ACCOUNT;
import static com.ketekmall.ketekmall.configs.Constant.sBANK_NAME;
import static com.ketekmall.ketekmall.configs.Constant.sIC_NO;
import static com.ketekmall.ketekmall.configs.Constant.sID;
import static com.ketekmall.ketekmall.configs.Constant.sVERIFICATION;
import static com.ketekmall.ketekmall.configs.Link.UPDATE_SELLER_PROFILE_DETAILS;

public class RegisterSeller extends AppCompatActivity {

    EditText etIcNo, etBankName, etBankAcc;
    Button btnAccept, btnCancel;

    String getId;
    BottomNavigationView bnvMenu;
    ProgressBar pbLoading;
    Setup setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_seller);

        Declare();


        ToolbarSetting();

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(RegisterSeller.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void EditDetail(final String user_id) {
        pbLoading.setVisibility(View.VISIBLE);
        btnAccept.setVisibility(View.GONE);
        final String strICNO = etIcNo.getText().toString();
        final String strBankName = etBankName.getText().toString();
        final String strBankAcc = etBankAcc.getText().toString();
        final String strVerify = "1";

        final ProgressDialog progressDialog = new ProgressDialog(RegisterSeller.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_SELLER_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                pbLoading.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterSeller.this, R.string.success_saved, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterSeller.this, AddNewProduct.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                pbLoading.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.VISIBLE);
                                Toast.makeText(RegisterSeller.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            pbLoading.setVisibility(View.GONE);
                            btnAccept.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbLoading.setVisibility(View.GONE);
                        btnAccept.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        try {

                            if (error instanceof TimeoutError) {
                                //Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
                                //net work error
                                System.out.println("" + error);
                            } else if (error instanceof AuthFailureError) {
                                //error
                                System.out.println("" + error);
                            } else if (error instanceof ServerError) {
                                //Erroor
                                System.out.println("" + error);
                            } else if (error instanceof NetworkError) {
                                //Error
                                System.out.println("" + error);
                            } else if (error instanceof ParseError) {
                                //Error
                                System.out.println("" + error);
                            } else {
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


                        }
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(sIC_NO, strICNO);
                params.put(sBANK_NAME, strBankName);
                params.put(sBANK_ACCOUNT, strBankAcc);
                params.put(sVERIFICATION, strVerify);
                params.put(sID, user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Declare() {
        setup = new Setup(this);
        getId = setup.getUserId();

        etIcNo = findViewById(R.id.ic_no_edit);
        etBankName = findViewById(R.id.bank_name_edit);
        etBankAcc = findViewById(R.id.bank_acc_edit);
        pbLoading = findViewById(R.id.loading);

        btnAccept = findViewById(R.id.btn_accept);
        btnCancel = findViewById(R.id.btn_cancel);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDetail(getId);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSeller.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        bnvMenu = findViewById(R.id.bottom_nav);
        bnvMenu.getMenu().getItem(0).setCheckable(false);
        bnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(RegisterSeller.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(RegisterSeller.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(RegisterSeller.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.register_seller));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
