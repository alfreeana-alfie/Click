package com.ketekmall.ketekmall.auth.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.ketekmall.ketekmall.activities.policies.TermsAndConditions;
import com.ketekmall.ketekmall.configs.Setup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Constant.sID;
import static com.ketekmall.ketekmall.configs.Link.GET_PROFILE_DETAILS;

public class RegisterSellerMain extends AppCompatActivity {

    BottomNavigationView bnvMenu;
    Button btnGoToRegisterSeller;
    TextView tvBeforeYouCanStartSellingYourProducts, tvYouNeedToRegisterAsKetekMallSeller, tvYourAccountHasBeenBlocked, tvPleaseContactAdministratorForMoreDetails;
    String getId;
    Setup setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_seller_page);
        ToolbarSetting();

        setup = new Setup(this);
        getId = setup.getUserId();

        bnvMenu = findViewById(R.id.bottom_nav);
        bnvMenu.getMenu().getItem(0).setCheckable(false);
        bnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(RegisterSellerMain.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(RegisterSellerMain.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(RegisterSellerMain.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        btnGoToRegisterSeller = findViewById(R.id.btn_register);
        tvBeforeYouCanStartSellingYourProducts = findViewById(R.id.textView8);
        tvYouNeedToRegisterAsKetekMallSeller = findViewById(R.id.textView9);
        tvYourAccountHasBeenBlocked = findViewById(R.id.textView10);
        tvPleaseContactAdministratorForMoreDetails = findViewById(R.id.textView11);

        SellerCheck_Main(getId);

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(RegisterSellerMain.this);
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

    private void SellerCheck_Main(final String user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_DETAILS,
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
                                    int strVerify = Integer.parseInt(object.getString("verification"));
                                    if (strVerify == 2) {

                                        tvBeforeYouCanStartSellingYourProducts.setVisibility(View.GONE);
                                        tvYouNeedToRegisterAsKetekMallSeller.setVisibility(View.GONE);
                                        tvYourAccountHasBeenBlocked.setVisibility(View.VISIBLE);
                                        tvPleaseContactAdministratorForMoreDetails.setVisibility(View.VISIBLE);
                                        btnGoToRegisterSeller.setVisibility(View.GONE);
                                        btnGoToRegisterSeller.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegisterSellerMain.this, TermsAndConditions.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        tvBeforeYouCanStartSellingYourProducts.setVisibility(View.VISIBLE);
                                        tvYouNeedToRegisterAsKetekMallSeller.setVisibility(View.VISIBLE);
                                        tvYourAccountHasBeenBlocked.setVisibility(View.GONE);
                                        tvPleaseContactAdministratorForMoreDetails.setVisibility(View.GONE);
                                        btnGoToRegisterSeller.setVisibility(View.VISIBLE);
                                        btnGoToRegisterSeller.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegisterSellerMain.this, TermsAndConditions.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                            } else {
                                Toast.makeText(RegisterSellerMain.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError) {
                                //Time out error

                            } else if (error instanceof NoConnectionError) {
                                //net work error

                            } else if (error instanceof AuthFailureError) {
                                //error

                            } else if (error instanceof ServerError) {
                                //Erroor
                            } else if (error instanceof NetworkError) {
                                //Error

                            } else if (error instanceof ParseError) {
                                //Error

                            } else {
                                //Error
                            }
                            //End


                        } catch (Exception e) {


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(sID, user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterSellerMain.this);
        requestQueue.add(stringRequest);
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sell My Items");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterSellerMain.this, Home.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterSellerMain.this, Home.class));
    }
}
