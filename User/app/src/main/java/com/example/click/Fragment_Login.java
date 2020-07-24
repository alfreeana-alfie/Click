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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class Fragment_Login extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private static String URL_LOGIN = "https://annkalina53.000webhostapp.com/android_register_login/verify.php";
    private static String URL_REGISTER = "https://annkalina53.000webhostapp.com/android_register_login/register.php";
    private final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");
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
                fragmentTransaction.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
                fragmentTransaction.replace(R.id.framelayout, fragment_register);
                fragmentTransaction.commit();
            }
        });

        button_goto_forgot_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_forgot_password = new Fragment_Forgot_Password();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
                fragmentTransaction.replace(R.id.framelayout, fragment_forgot_password);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void Declare(View v) {
        email = v.findViewById(R.id.email_edit);
        password = v.findViewById(R.id.password_login);
        loading = v.findViewById(R.id.loading);
        button_login = v.findViewById(R.id.button_login);
        button_goto_register_page = v.findViewById(R.id.button_goto_register_page);
        button_goto_forgot_page = v.findViewById(R.id.button_goto_forgot_page);
        signInButton = v.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void Login(View view) {
        final String mEmail = this.email.getText().toString().trim();
        final String mPassword = this.password.getText().toString().trim();

        if (!PASSWORD_PATTERN.matcher(mPassword).matches()) {
            password.setError("Incorrect Password");
        } else if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
            loading.setVisibility(View.VISIBLE);
            button_login.setVisibility(View.GONE);

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

                                        final String name = object.getString("name").trim();
                                        final String email = object.getString("email").trim();
                                        final String phone_no = object.getString("phone_no").trim();
                                        final String address = object.getString("address").trim();
                                        final String birthday = object.getString("birthday").trim();
                                        final String gender = object.getString("gender").trim();
                                        String id = object.getString("id").trim();

                                        sessionManager.createSession(name, email, phone_no, address, birthday, gender, id);

                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getContext(), Activity_All_View.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("email", email);
                                                intent.putExtra("phone_no", phone_no);
                                                intent.putExtra("address", address);
                                                intent.putExtra("birthday", birthday);
                                                intent.putExtra("gender", gender);
                                                getActivity().startActivity(intent);
                                                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            }
                                        }, 100);
                                        Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                        button_login.setVisibility(View.VISIBLE);
                                    }
                                } else {
//                                    Toast.makeText(getContext(), "Login Failed! ", Toast.LENGTH_SHORT).show();

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
                    if (error.getMessage() == null) {
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        button_login.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        button_login.setVisibility(View.VISIBLE);
                    }
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

    private void Google_SignIn(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        final String name = account.getDisplayName();
        final String email = account.getEmail();
        final String phone_no = "";
        final String address = "";
        final String birthday = "";
        final String gender = "";
        final String password = account.getFamilyName() + account.getGivenName();

        final String photo = String.valueOf(account.getPhotoUrl());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Intent intent = new Intent(getContext(), Activity_All_View.class);
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
                        if (error.getMessage() == null) {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    private void handleSignInResult(GoogleSignInResult result) {
        int second = 1;

        if (result.isSuccess()) {
            Google_SignIn(result);
            GoogleSignInAccount account = result.getSignInAccount();
            email.setText(account.getEmail());
            password.setText(account.getFamilyName() + account.getGivenName());
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

}
