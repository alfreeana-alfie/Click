package com.example.click;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Forgot_Password extends Fragment {

    private EditText email, new_password, confirm_new_password;
    private Button button_enter_email, button_enter_new_password, button_back_pressed;
    private ProgressBar loading, loading_password;
    private LinearLayout password_linear_layout, email_linear_layout;

    private static String URL_LOGIN = "http://192.168.1.15/android_register_login/verify.php";
    private static String URL_EDIT = "http://192.168.1.15/android_register_login/edit.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        Declare(view);

        button_enter_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify_Email(v);
            }
        });

        button_back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_Main.class);
                getActivity().startActivity(intent);
            }
        });

        button_enter_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Password(v);
            }
        });
        return view;
    }

    private void Verify_Email(View view) {
        final String mEmail = this.email.getText().toString().trim();

        if (!mEmail.isEmpty()) {
            loading.setVisibility(view.VISIBLE);
            button_enter_email.setVisibility(view.GONE);

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

                                        password_linear_layout.setVisibility(View.VISIBLE);
                                        email_linear_layout.setVisibility(View.GONE);
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

    private void Reset_Password(View view) {
        loading_password.setVisibility(View.VISIBLE);
        button_enter_new_password.setVisibility(View.GONE);
        final String mEmail = this.email.getText().toString().trim();
        final String mNew_Password = this.new_password.getText().toString().trim();
        final String mConfirm_new_password = this.confirm_new_password.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        if(!mConfirm_new_password.equals(mNew_Password)){
            confirm_new_password.requestFocus();
            confirm_new_password.setError("Incorrect Confirm Password");
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

                                    loading_password.setVisibility(View.GONE);
                                    button_enter_new_password.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(getContext(), Activity_Main.class);
                                    getActivity().startActivity(intent);
                                }
                            } catch (JSONException e) {
                                //e.printStackTrace();
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
    }
}
