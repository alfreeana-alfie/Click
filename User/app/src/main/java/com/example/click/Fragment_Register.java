package com.example.click;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.regex.Pattern;

public class Fragment_Register extends Fragment {

    private EditText name_edittext, email_edittext, phone_no_edittext, password_edittext, confirm_password_edittext;
    private ProgressBar loading;
    private Button button_login_page, button_register;
    private static String URL_REGISTER = "https://annkalina53.000webhostapp.com/android_register_login/register.php";

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

        button_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_login = new Fragment_Login();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment_login);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void Register(View view) {

        final String name = this.name_edittext.getText().toString().trim();
        final String email = this.email_edittext.getText().toString().trim();
        final String phone_no = this.phone_no_edittext.getText().toString().trim();
        final String password = this.password_edittext.getText().toString().trim();
        final String confirm_password = this.confirm_password_edittext.getText().toString().trim();

        final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");

        //Name
        if (name.isEmpty()) {
            name_edittext.requestFocus();
            name_edittext.setError("Fields cannot be empty!");
        } else if (!name.matches("[a-zA-Z ]+")) {
            name_edittext.requestFocus();
            name_edittext.setError("Enter only Alphabetical Letter");
        }

        //Email
        if (email.isEmpty()) {
            email_edittext.requestFocus();
            email_edittext.setError("Fields cannot be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edittext.requestFocus();
            email_edittext.setError("Please enter a valid email address");
        }

        //Phone NO.
        if (phone_no.isEmpty()) {
            phone_no_edittext.requestFocus();
            phone_no_edittext.setError("Fields cannot be empty!");
        } else if (!Patterns.PHONE.matcher(phone_no).matches()) {
            phone_no_edittext.requestFocus();
            phone_no_edittext.setError("Enter only Numerical Letter");
        }

        //Password
        if (password.isEmpty()) {
            password_edittext.requestFocus();
            password_edittext.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            password_edittext.requestFocus();
            password_edittext.setError("At least 4 lengths and at least 1 Capital Letter, 1 number, and 1 symbol for password");
        }

        //Confirm Password
        if (confirm_password.isEmpty()) {
            confirm_password_edittext.requestFocus();
            confirm_password_edittext.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            confirm_password_edittext.requestFocus();
            confirm_password_edittext.setError("At least 4 lengths and at least 1 Capital Letter, 1 number, and 1 symbol for password");
        }

        //Other
        if (!confirm_password.equals(password)) {
            confirm_password_edittext.requestFocus();
            confirm_password_edittext.setError("Confirm Password is different than Password");
        }

        if (name.matches("[a-zA-Z ]+")
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && Patterns.PHONE.matcher(phone_no).matches()
                && PASSWORD_PATTERN.matcher(password).matches()
                && confirm_password.equals(password)) {

            loading.setVisibility(view.VISIBLE);
            button_register.setVisibility(view.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(getContext(), "Register Success", Toast.LENGTH_SHORT).show();
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
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Register Error" + error.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    button_register.setVisibility(View.VISIBLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phone_no", phone_no);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
    }

    private void Declare(View v) {
        name_edittext = v.findViewById(R.id.name_edittext);
        email_edittext = v.findViewById(R.id.email_edittext);
        phone_no_edittext = v.findViewById(R.id.phone_no_edittext);
        password_edittext = v.findViewById(R.id.password_edittext);
        confirm_password_edittext = v.findViewById(R.id.confirm_password_edittext);
        loading = v.findViewById(R.id.loading);
        button_register = v.findViewById(R.id.button_register);
        button_login_page = v.findViewById(R.id.button_login_page);
    }
}
