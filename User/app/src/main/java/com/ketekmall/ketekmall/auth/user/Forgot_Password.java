package com.ketekmall.ketekmall.auth.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.firebase.client.Firebase;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Constant.sEMAIL;
import static com.ketekmall.ketekmall.configs.Constant.sPASSWORD;
import static com.ketekmall.ketekmall.configs.Link.EDIT_PASSWORD;
import static com.ketekmall.ketekmall.configs.Link.EMAIL_RESET_PASSWORD;
import static com.ketekmall.ketekmall.configs.Link.VERIFY;

public class Forgot_Password extends AppCompatActivity {

    private EditText etEmail, etNewPassword, etConfirmNewPassword;
    private Button btnEnterEmail;
    private Button btnEnterNewPassword;
    private ProgressBar pbLoading, pbLoadingNewPassword;
    private LinearLayout llPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Firebase.setAndroidContext(Forgot_Password.this);
        Declare();

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Forgot_Password.this);
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

    private void Declare() {
        etEmail = findViewById(R.id.email_verify);
        btnEnterEmail = findViewById(R.id.button_enter_email);
        pbLoading = findViewById(R.id.loading);
        pbLoadingNewPassword = findViewById(R.id.loading_new_password);
        llPassword = findViewById(R.id.password_linear_layout);
        etNewPassword = findViewById(R.id.edittext_new_password);
        etConfirmNewPassword = findViewById(R.id.edittext_confirm_new_password);
        btnEnterNewPassword = findViewById(R.id.button_enter_new_password);
        Button btn_back = findViewById(R.id.button_back);
        Button btn_backPassword = findViewById(R.id.button_back_password);

        btnEnterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify_Email();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_Activity_Main();
            }
        });

        btnEnterNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Password(v);
            }
        });

        btn_backPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_Activity_Main();
            }
        });
    }

    private void Goto_Activity_Main() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Forgot_Password.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
            }
        }, 100);
    }

    private void Verify_Email() {
        final String mEmail = this.etEmail.getText().toString().trim();

        if (!mEmail.isEmpty()) {
            pbLoading.setVisibility(View.VISIBLE);
            btnEnterEmail.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, VERIFY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("login");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        Toast.makeText(Forgot_Password.this, "Email is Verified", Toast.LENGTH_SHORT).show();

                                        pbLoading.setVisibility(View.GONE);
                                        btnEnterEmail.setVisibility(View.VISIBLE);

                                        llPassword.setVisibility(View.GONE);

                                        sendEmail(mEmail);
                                    }
                                } else {
                                    Toast.makeText(Forgot_Password.this, "Incorrect Email", Toast.LENGTH_SHORT).show();

                                    pbLoading.setVisibility(View.GONE);
                                    btnEnterEmail.setVisibility(View.VISIBLE);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Forgot_Password.this, "Failed to Retrieve the Email", Toast.LENGTH_SHORT).show();
                                pbLoading.setVisibility(View.GONE);
                                btnEnterEmail.setVisibility(View.VISIBLE);

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pbLoading.setVisibility(View.GONE);
                    btnEnterEmail.setVisibility(View.VISIBLE);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(sEMAIL, mEmail);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Forgot_Password.this);
            requestQueue.add(stringRequest);


        } else {
            etEmail.setError("Fields cannot be empty!");
        }
    }

    private void sendEmail(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_RESET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(Forgot_Password.this, "Please check your email inbox", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Forgot_Password.this, "FAILED", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Forgot_Password.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(Forgot_Password.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sEMAIL, email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Forgot_Password.this);
        requestQueue.add(stringRequest);
    }


    private void Reset_Password(View view) {
        pbLoadingNewPassword.setVisibility(View.VISIBLE);
        btnEnterNewPassword.setVisibility(View.GONE);
        final String mEmail = this.etEmail.getText().toString().trim();
        final String mNew_Password = this.etNewPassword.getText().toString().trim();
        final String mConfirm_new_password = this.etConfirmNewPassword.getText().toString().trim();

        final ProgressDialog pd_Saving = new ProgressDialog(Forgot_Password.this);
        pd_Saving.setMessage("Saving...");
        pd_Saving.show();

        if (!mConfirm_new_password.equals(mNew_Password)) {
            etConfirmNewPassword.requestFocus();
            etConfirmNewPassword.setError("Incorrect Confirm Password");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PASSWORD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd_Saving.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(Forgot_Password.this, "Success!", Toast.LENGTH_SHORT).show();

                                    pbLoadingNewPassword.setVisibility(View.GONE);
                                    btnEnterNewPassword.setVisibility(View.VISIBLE);

                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Forgot_Password.this, MainActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                        }
                                    }, 100);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pd_Saving.dismiss();
                                Toast.makeText(Forgot_Password.this, "JSON Parsing Error : " + e.toString(), Toast.LENGTH_SHORT).show();

                                pbLoadingNewPassword.setVisibility(View.GONE);
                                btnEnterNewPassword.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd_Saving.dismiss();

                            pbLoadingNewPassword.setVisibility(View.GONE);
                            btnEnterNewPassword.setVisibility(View.VISIBLE);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(sEMAIL, mEmail);
                    params.put(sPASSWORD, mNew_Password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
    }
}
