package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class Fragment_Register extends Fragment {

    private static String URL_REGISTER = "https://annkalina53.000webhostapp.com/android_register_login/register.php";

    private EditText name, email, phone_no, password, confirm_password;
    private ProgressBar loading;
    private Button button_goto_login_page, button_register;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        Declare(view);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register(v);

            }
        });

        button_goto_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), Activity_Main.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
                    }
                }, 100);

            }
        });
        return view;
    }

    private void Declare(View v) {
        name = v.findViewById(R.id.name_register);
        email = v.findViewById(R.id.email_register);
        phone_no = v.findViewById(R.id.phone_no_register);
        password = v.findViewById(R.id.password_register);
        confirm_password = v.findViewById(R.id.confirm_password_register);
        loading = v.findViewById(R.id.loading);
        button_register = v.findViewById(R.id.button_register);
        button_goto_login_page = v.findViewById(R.id.button_goto_login_page);
    }

    private void Register(View view) {
        final String strName = this.name.getText().toString().trim();
        final String strEmail = this.email.getText().toString().trim();
        final String strPhone_No = this.phone_no.getText().toString().trim();
        final String strPassword = this.password.getText().toString().trim();
        final String strConfirm_Password = this.confirm_password.getText().toString().trim();
        final String strAddress = "";
        final String strBirthday = "";
        final String strGender = "Female";
        final String strPhoto_URL = "https://annkalina53.000webhostapp.com/android_register_login/profile_image/main_photo.png";

        final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");

        //Name
        if (strName.isEmpty()) {
            name.requestFocus();
            name.setError("Fields cannot be empty!");
        } else if (!strName.matches("[a-zA-Z ]+")) {
            name.requestFocus();
            name.setError("Enter only Alphabetical Letter");
        }

        //Email
        if (strEmail.isEmpty()) {
            email.requestFocus();
            email.setError("Fields cannot be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.requestFocus();
            email.setError("Please enter a valid email address");
        }

        //Phone NO.
        if (strPhone_No.isEmpty()) {
            phone_no.requestFocus();
            phone_no.setError("Fields cannot be empty!");
        } else if (!Patterns.PHONE.matcher(strPhone_No).matches()) {
            phone_no.requestFocus();
            phone_no.setError("Enter only Numerical Letter");
        }

        //Password
        if (strPassword.isEmpty()) {
            password.requestFocus();
            password.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(strPassword).matches()) {
            password.requestFocus();
            password.setError("At least 8 character lengths for password");
        }

/*
        //Confirm Password
        if (strConfirm_Password.isEmpty()) {
            confirm_password.requestFocus();
            confirm_password.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(strPassword).matches()) {
            confirm_password.requestFocus();
            confirm_password.setError("At least 8 character lengths for password");
        }
*/

        //Other
        if (!strConfirm_Password.equals(strPassword)) {
            confirm_password.requestFocus();
            confirm_password.setError("Confirm Password is different than Password");
        }

        if (strName.matches("[a-zA-Z]+")
                && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()
                && Patterns.PHONE.matcher(strPhone_No).matches()
                && PASSWORD_PATTERN.matcher(strPassword).matches()
                && strConfirm_Password.equals(strPassword)) {

            loading.setVisibility(View.VISIBLE);
            button_register.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                            loading.setVisibility(View.GONE);
                            button_register.setVisibility(View.VISIBLE);

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(), Activity_Main.class);
                                    getActivity().startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            }, 100);
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                            loading.setVisibility(View.GONE);
                            button_register.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Email is already existed", Toast.LENGTH_SHORT).show();

                        loading.setVisibility(View.GONE);
                        button_register.setVisibility(View.VISIBLE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() == null) {
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        button_register.setVisibility(View.VISIBLE);
                    } else {
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        button_register.setVisibility(View.VISIBLE);
                    }
                    error.printStackTrace();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", strName);
                    params.put("email", strEmail);
                    params.put("phone_no", strPhone_No);
                    params.put("password", strPassword);
                    params.put("address", strAddress);
                    params.put("birthday", strBirthday);
                    params.put("gender", strGender);
                    params.put("photo", strPhoto_URL);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
    }

}
