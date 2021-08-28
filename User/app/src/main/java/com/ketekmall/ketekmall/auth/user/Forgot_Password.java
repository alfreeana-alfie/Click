package com.ketekmall.ketekmall.auth.user;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.firebase.client.Firebase;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.MainActivity;

import org.json.*;

import java.util.*;

import static com.ketekmall.ketekmall.configs.Link.*;

public class Forgot_Password extends AppCompatActivity {

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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_RESET_PASSWORD,
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PASSWORD,
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
