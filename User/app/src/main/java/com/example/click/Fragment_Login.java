package com.example.click;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Fragment_Login extends Fragment {

    private EditText email, password;
    private ProgressBar loading;
    private Button button_login, button_register_page;
    private SessionManager sessionManager;

    private static String URL_LOGIN = "http://192.168.1.15/android_register_login/login.php";
    final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });

        button_register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_register = new Fragment_Register();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment_register);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void Login(View view) {
        final String mEmail = this.email.getText().toString().trim();
        final String mPassword = this.password.getText().toString().trim();

        if (!PASSWORD_PATTERN.matcher(mPassword).matches()) {
            password.setError("Incorrect Password");
        } else if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
            loading.setVisibility(view.VISIBLE);
            button_login.setVisibility(view.GONE);

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
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String name = object.getString("name").trim();
                                        String email = object.getString("email").trim();
                                        String id = object.getString("id").trim();
                                        String phone_no = object.getString("phone_no").trim();

                                        sessionManager.createSession(name, email, id);

                                        Intent intent = new Intent(getContext(), Activity_Home.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("phone_no", phone_no);

                                        getActivity().startActivity(intent);

                                        Toast.makeText(getContext(),
                                                "Success Login " + name + email,
                                                Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                        button_login.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),
                                        "Incorrect Email or Password",
                                        Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                button_login.setVisibility(View.VISIBLE);

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),
                            "Failed Login " + error.toString(),
                            Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    button_login.setVisibility(View.VISIBLE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", mEmail);
                    params.put("password", mPassword);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);


        } else {
            email.setError("Fields cannot be empty!");
            password.setError("Fields cannot be empty!");
        }
    }

    private void Declare(View v) {
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        loading = v.findViewById(R.id.loading);
        button_login = v.findViewById(R.id.button_login);
        button_register_page = v.findViewById(R.id.button_register_page);
    }
}
