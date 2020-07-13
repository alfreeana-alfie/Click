package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Fragment_Login extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private static String URL_LOGIN = "http://192.168.1.15/android_register_login/verify.php";
    private static String URL_REGISTER = "http://192.168.1.15/android_register_login/register.php";
    private final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private EditText email, password;
    private ProgressBar loading;
    private Button button_login, button_goto_register_page, button_goto_forgot_page;
    private SessionManager sessionManager;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });

        button_goto_register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_register = new Fragment_Register();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment_register);
                fragmentTransaction.commit();
            }
        });

        button_goto_forgot_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_verify_email = new Fragment_Forgot_Password();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment_verify_email);
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
                                        String phone_no = object.getString("phone_no").trim();
                                        String address = object.getString("address").trim();
                                        String birthday = object.getString("birthday").trim();
                                        String gender = object.getString("gender").trim();
                                        String id = object.getString("id").trim();


                                        sessionManager.createSession(name, email, phone_no, address, birthday, gender, id);

                                        Intent intent = new Intent(getContext(), Activity_Home.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("phone_no", phone_no);
                                        intent.putExtra("address", address);
                                        intent.putExtra("birthday", birthday);
                                        intent.putExtra("gender", gender);

                                        getActivity().startActivity(intent);

//                                        Toast.makeText(getContext(), "Success Login " + name + email, Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                        button_login.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Login Failed! ", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    button_login.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                                loading.setVisibility(View.GONE);
                                button_login.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Connection Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

/*    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        email.setText(account.getEmail());
        password.setText(account.getEmail());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button_login.performClick();
            }
        }, 1*2000);
    }
*/

    private void handleSignInResult(GoogleSignInResult result) {
        int second = 1;

        if (result.isSuccess()) {
            Google_SignIn(result);
            GoogleSignInAccount account = result.getSignInAccount();
            email.setText(account.getEmail());
            password.setText(account.getEmail());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_login.performClick();
                }
            }, second * 100);
        } else {
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void Google_SignIn(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        final String name = account.getDisplayName();
        final String email = account.getEmail();
        final String phone_no = "";
        final String address = "";
        final String birthday = "";
        final String gender = "";
        final String password = account.getDisplayName();

        final String photo = String.valueOf(account.getPhotoUrl());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Intent intent = new Intent(getContext(), Activity_Home.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone_no", phone_no);
                params.put("password", password);
                params.put("address", address);
                params.put("birthday", birthday);
                params.put("gender", gender);
                params.put("photo", photo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void Declare(View v) {
        email = v.findViewById(R.id.email_edit);
        password = v.findViewById(R.id.password_login);
        loading = v.findViewById(R.id.loading);
        button_login = v.findViewById(R.id.button_login);
        button_goto_register_page = v.findViewById(R.id.button_goto_register_page);
        button_goto_forgot_page = v.findViewById(R.id.button_goto_forgot_page);
        signInButton = v.findViewById(R.id.sign_in_button);
    }
}
