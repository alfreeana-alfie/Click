package com.example.click.user;

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
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.pages.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Forgot_Password extends Fragment {

    private static String URL_LOGIN = "https://ketekmall.com/ketekmall/verify.php";
    private static String URL_SEND_EMAIL = "https://ketekmall.com/ketekmall/sendEmail_getPassword.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit.php";
    private EditText email, new_password, confirm_new_password;
    private Button button_enter_email, button_enter_new_password, button_back_pressed, button_back_password;
    private ProgressBar loading, loading_password;
    private LinearLayout password_linear_layout, email_linear_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password, container, false);
        Declare(view);

        Button_Func();

        return view;
    }

    private void Declare(View v) {
        email = v.findViewById(R.id.email_verify);
        button_enter_email = v.findViewById(R.id.button_enter_email);
        loading = v.findViewById(R.id.loading);
        loading_password = v.findViewById(R.id.loading_new_password);
        password_linear_layout = v.findViewById(R.id.password_linear_layout);
        email_linear_layout = v.findViewById(R.id.email_linear_layout);
        new_password = v.findViewById(R.id.edittext_new_password);
        confirm_new_password = v.findViewById(R.id.edittext_confirm_new_password);
        button_enter_new_password = v.findViewById(R.id.button_enter_new_password);
        button_back_pressed = v.findViewById(R.id.button_back);
        button_back_password = v.findViewById(R.id.button_back_password);
    }

    private void Button_Func() {
        button_enter_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify_Email(v);
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
            }
        }, 100);
    }

    private void Verify_Email(View view) {
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

                                        Toast.makeText(getContext(), "Email is Verified", Toast.LENGTH_SHORT).show();

                                        loading.setVisibility(View.GONE);
                                        button_enter_email.setVisibility(View.VISIBLE);

                                        password_linear_layout.setVisibility(View.GONE);

                                        sendEmail(mEmail);
                                        Intent intent1 = new Intent(getContext(), MainActivity.class);
                                        getActivity().startActivity(intent1);
                                        Toast.makeText(getContext(), "Please check your email inbox", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Incorrect Email", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    button_enter_email.setVisibility(View.VISIBLE);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to Retrieve the Email", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                button_enter_email.setVisibility(View.VISIBLE);

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Connection Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    button_enter_email.setVisibility(View.VISIBLE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", mEmail);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
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
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void Reset_Password(View view) {
        loading_password.setVisibility(View.VISIBLE);
        button_enter_new_password.setVisibility(View.GONE);
        final String mEmail = this.email.getText().toString().trim();
        final String mNew_Password = this.new_password.getText().toString().trim();
        final String mConfirm_new_password = this.confirm_new_password.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

                                    loading_password.setVisibility(View.GONE);
                                    button_enter_new_password.setVisibility(View.VISIBLE);

                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            getActivity().startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                        }
                                    }, 100);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "JSON Parsing Error : " + e.toString(), Toast.LENGTH_SHORT).show();

                                loading_password.setVisibility(View.GONE);
                                button_enter_new_password.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Connection Error " + error.toString(), Toast.LENGTH_SHORT).show();

                            loading_password.setVisibility(View.GONE);
                            button_enter_new_password.setVisibility(View.VISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
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
