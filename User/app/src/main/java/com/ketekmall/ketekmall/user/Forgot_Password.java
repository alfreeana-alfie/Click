package com.ketekmall.ketekmall.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.ketekmall.ketekmall.pages.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Forgot_Password extends AppCompatActivity {

    private static String URL_LOGIN = "https://ketekmall.com/ketekmall/verify.php";
    private static String URL_SEND_EMAIL = "https://ketekmall.com/ketekmall/sendEmail_getPassword.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit.php";
    private EditText email, new_password, confirm_new_password;
    private Button button_enter_email, button_enter_new_password, button_back_pressed, button_back_password;
    private ProgressBar loading, loading_password;
    private LinearLayout password_linear_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Firebase.setAndroidContext(Forgot_Password.this);
        Declare();

        Button_Func();
    }

    private void Declare() {
        email = findViewById(R.id.email_verify);
        button_enter_email = findViewById(R.id.button_enter_email);
        loading = findViewById(R.id.loading);
        loading_password = findViewById(R.id.loading_new_password);
        password_linear_layout = findViewById(R.id.password_linear_layout);
        new_password = findViewById(R.id.edittext_new_password);
        confirm_new_password = findViewById(R.id.edittext_confirm_new_password);
        button_enter_new_password = findViewById(R.id.button_enter_new_password);
        button_back_pressed = findViewById(R.id.button_back);
        button_back_password = findViewById(R.id.button_back_password);
    }

    private void Button_Func() {
        button_enter_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify_Email();
            }
        });

        button_back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_Activity_Main();
            }
        });

        button_enter_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Password(v);
            }
        });

        button_back_password.setOnClickListener(new View.OnClickListener() {
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
        final String mEmail = this.email.getText().toString().trim();

        if (!mEmail.isEmpty()) {
            loading.setVisibility(View.VISIBLE);
            button_enter_email.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
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

                                        loading.setVisibility(View.GONE);
                                        button_enter_email.setVisibility(View.VISIBLE);

                                        password_linear_layout.setVisibility(View.GONE);

                                        sendEmail(mEmail);
//                                        Intent intent1 = new Intent(Forgot_Password.this, MainActivity.class);
//                                        startActivity(intent1);
//                                        Toast.makeText(Forgot_Password.this, "Please check your email inbox", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Forgot_Password.this, "Incorrect Email", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    button_enter_email.setVisibility(View.VISIBLE);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Forgot_Password.this, "Failed to Retrieve the Email", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                button_enter_email.setVisibility(View.VISIBLE);

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getContext(), "Connection Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    button_enter_email.setVisibility(View.VISIBLE);
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
                    params.put("email", mEmail);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Forgot_Password.this);
            requestQueue.add(stringRequest);


        } else {
            email.setError("Fields cannot be empty!");
        }
    }

    private void sendEmail(final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(Forgot_Password.this, "Please check your email inbox", Toast.LENGTH_SHORT).show();
                            }else{
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
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Forgot_Password.this);
        requestQueue.add(stringRequest);
    }


    private void Reset_Password(View view) {
        loading_password.setVisibility(View.VISIBLE);
        button_enter_new_password.setVisibility(View.GONE);
        final String mEmail = this.email.getText().toString().trim();
        final String mNew_Password = this.new_password.getText().toString().trim();
        final String mConfirm_new_password = this.confirm_new_password.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(Forgot_Password.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        if (!mConfirm_new_password.equals(mNew_Password)) {
            confirm_new_password.requestFocus();
            confirm_new_password.setError("Incorrect Confirm Password");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(Forgot_Password.this, "Success!", Toast.LENGTH_SHORT).show();

                                    loading_password.setVisibility(View.GONE);
                                    button_enter_new_password.setVisibility(View.VISIBLE);

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
                                progressDialog.dismiss();
                                Toast.makeText(Forgot_Password.this, "JSON Parsing Error : " + e.toString(), Toast.LENGTH_SHORT).show();

                                loading_password.setVisibility(View.GONE);
                                button_enter_new_password.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "Connection Error " + error.toString(), Toast.LENGTH_SHORT).show();

                            loading_password.setVisibility(View.GONE);
                            button_enter_new_password.setVisibility(View.VISIBLE);
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
                    params.put("email", mEmail);
                    params.put("password", mNew_Password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
    }
}
