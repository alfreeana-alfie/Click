package com.ketekmall.ketekmall.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.data.UserDetails;
import com.ketekmall.ketekmall.pages.Homepage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class Login extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private static String URL_LOGIN = "https://ketekmall.com/ketekmall/login.php";
    private static String URL_REGISTER = "https://ketekmall.com/ketekmall/register.php";
    private final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");

    private String name_firebase, email_firebase;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private EditText email, password;
    private ProgressBar loading;
    private Button button_login, button_goto_register_page, button_goto_forgot_page;
    private SessionManager sessionManager;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        Declare(view);

        Firebase.setAndroidContext(view.getContext());
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
//                ChatLogin(v);
                SignIn(v);
            }
        });

        button_goto_register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment_register = new Register();
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
                final Fragment fragment_forgot_password = new Forgot_Password();
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

        callbackManager = CallbackManager.Factory.create();
        loginButton = v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Facebook_SignIn(loginResult);
            }

            @Override
            public void onCancel() {
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SignIn(final View view) {
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
                                        final String address_01 = object.getString("address_01").trim();
                                        final String address_02 = object.getString("address_02").trim();
                                        final String city = object.getString("division").trim();
                                        final String postcode = object.getString("postcode").trim();
                                        final String birthday = object.getString("birthday").trim();
                                        final String gender = object.getString("gender").trim();
                                        final String photo = object.getString("photo").trim();
                                        String id = object.getString("id").trim();

                                        sessionManager.createSession(name, email, phone_no, address_01, address_02, city, postcode, birthday, gender, id);

                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getContext(), Homepage.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("email", email);
                                                intent.putExtra("phone_no", phone_no);
                                                intent.putExtra("address_01", address_01);
                                                intent.putExtra("address_02", address_02);
                                                intent.putExtra("division", city);
                                                intent.putExtra("postcode", postcode);
                                                intent.putExtra("birthday", birthday);
                                                intent.putExtra("gender", gender);
                                                getActivity().startActivity(intent);
                                                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            }
                                        }, 100);
                                        Toast.makeText(getContext(), "SignIn Success", Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                        button_login.setVisibility(View.VISIBLE);

                                        //Firebase
                                        String url = "https://click-1595830894120.firebaseio.com/users.json";
                                        final String photo_url = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                if (s.equals("null")) {
                                                    Toast.makeText(getContext(), "user_actionbar not found", Toast.LENGTH_LONG).show();
                                                } else {
                                                    try {
                                                        JSONObject obj = new JSONObject(s);
                                                        if (!obj.has(name)) {
                                                            name_firebase = name;
                                                            email_firebase = email;

                                                            String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                    if (s.equals("null")) {
                                                                        reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                        if(photo.equals("null")){
                                                                            reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                        }else {
                                                                            reference.child(name_firebase).child("photo").setValue(photo);
                                                                        }

                                                                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                    } else {
                                                                        try {
                                                                            JSONObject obj = new JSONObject(s);

                                                                            if (!obj.has(name_firebase)) {
                                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                                if(photo.equals("null")){
                                                                                    reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                                }else {
                                                                                    reference.child(name_firebase).child("photo").setValue(photo);
                                                                                }
                                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            } else {
                                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                                if(photo.equals("null")){
                                                                                    reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                                }else {
                                                                                    reference.child(name_firebase).child("photo").setValue(photo);
                                                                                }
                                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }

                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    try {

                                                                        if (error instanceof TimeoutError) {
                                                                            //Time out error
                                                                            System.out.println("" + error);
                                                                        }else if(error instanceof NoConnectionError){
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
                                                                        }else{
                                                                            //Error
                                                                            System.out.println("" + error);
                                                                        }
                                                                        //End


                                                                    } catch (Exception e) {


                                                                    }
                                                                }
                                                            });

                                                            RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                                            rQueue.add(request);
                                                        } else if (obj.getJSONObject(name).getString("email").equals(email)) {
                                                            UserDetails.username = name;
                                                            UserDetails.email = email;

                                                            name_firebase = name;
                                                            email_firebase = email;

                                                            String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                    if (s.equals("null")) {
                                                                        reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                        if(photo.equals("null")){
                                                                            reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                        }
                                                                        reference.child(name_firebase).child("photo").setValue(photo);
                                                                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                    } else {
                                                                        try {
                                                                            JSONObject obj = new JSONObject(s);

                                                                            if (!obj.has(name_firebase)) {
                                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                                if(photo.equals("null")){
                                                                                    reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                                }else {
                                                                                    reference.child(name_firebase).child("photo").setValue(photo);
                                                                                }
                                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            } else {
                                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                                if(photo.equals("null")){
                                                                                    reference.child(name_firebase).child("photo").setValue(photo_url);
                                                                                }else {
                                                                                    reference.child(name_firebase).child("photo").setValue(photo);
                                                                                }
                                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }

                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    try {

                                                                        if (error instanceof TimeoutError ) {
                                                                            //Time out error
                                                                            System.out.println("" + error);
                                                                        }else if(error instanceof NoConnectionError){
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
                                                                        }else{
                                                                            //Error
                                                                            System.out.println("" + error);
                                                                        }
                                                                        //End


                                                                    } catch (Exception e) {


                                                                    }
                                                                }
                                                            });

                                                            RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                                            rQueue.add(request);
                                                        } else {
                                                            Toast.makeText(getContext(), "incorrect email", Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                try {

                                                    if (error instanceof TimeoutError ) {
                                                        //Time out error
                                                        System.out.println("" + error);
                                                    }else if(error instanceof NoConnectionError){
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
                                                    }else{
                                                        //Error
                                                        System.out.println("" + error);
                                                    }
                                                    //End


                                                } catch (Exception e) {


                                                }
                                            }
                                        });

                                        RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                        rQueue.add(request);
                                    }
                                } else {
                                    loading.setVisibility(View.GONE);
                                    button_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
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
                    try {

                        if (error instanceof TimeoutError ) {
                            //Time out error
                            System.out.println("" + error);
                        }else if(error instanceof NoConnectionError){
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
                        }else{
                            //Error
                            System.out.println("" + error);
                        }
                        //End


                    } catch (Exception e) {


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

    private void Facebook_SignIn(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    final String fbUserId = object.optString("id");
                    final String fbUserName = object.optString("name");
                    final String fbEmail = object.optString("email");
                    final String phone_no = "00000000000";

                    final String birthday = "";
                    final String gender = "Female";
                    final String strPassword = object.optString("name") + "Facebook";

                    final String photo = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";

                    name_firebase = fbUserName;
                    email_firebase = fbEmail;

                    String url = "https://click-1595830894120.firebaseio.com/users.json";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                            if (s.equals("null")) {
                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                reference.child(name_firebase).child("photo").setValue(photo);
                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(name_firebase)) {
                                        reference.child(name_firebase).child("email").setValue(email_firebase);
                                        reference.child(name_firebase).child("photo").setValue(photo);
                                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                    } else {
                                        reference.child(name_firebase).child("email").setValue(email_firebase);
                                        reference.child(name_firebase).child("photo").setValue(photo);
                                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(getContext());
                    rQueue.add(request);


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")) {
                                            String url = "https://click-1595830894120.firebaseio.com/users.json";
                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                    if (s.equals("null")) {
                                                        reference.child(name_firebase).child("email").setValue(email_firebase);
                                                        reference.child(name_firebase).child("photo").setValue(photo);
                                                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                    } else {
                                                        try {
                                                            JSONObject obj = new JSONObject(s);
                                                            if (!obj.has(name_firebase)) {
                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                reference.child(name_firebase).child("photo").setValue(photo);
                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                            } else {
                                                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                                                reference.child(name_firebase).child("photo").setValue(photo);
                                                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    try {

                                                        if (error instanceof TimeoutError ) {
                                                            //Time out error
                                                            System.out.println("" + error);
                                                        }else if(error instanceof NoConnectionError){
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
                                                        }else{
                                                            //Error
                                                            System.out.println("" + error);
                                                        }
                                                        //End


                                                    } catch (Exception e) {


                                                    }
                                                }
                                            });

                                            RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                            rQueue.add(request);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {

                                        if (error instanceof TimeoutError ) {
                                            //Time out error
                                            System.out.println("" + error);
                                        }else if(error instanceof NoConnectionError){
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
                                        }else{
                                            //Error
                                            System.out.println("" + error);
                                        }
                                        //End


                                    } catch (Exception e) {


                                    }
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", fbUserName);
                            params.put("email", fbEmail);
                            params.put("phone_no", phone_no);
                            params.put("password", strPassword);

                            params.put("birthday", birthday);
                            params.put("gender", gender);
                            params.put("photo", photo);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);

                    email.setText(fbEmail);
                    password.setText(strPassword);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button_login.performClick();
                        }
                    }, 20);

                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void Google_SignIn(GoogleSignInResult result) {
        final GoogleSignInAccount account = result.getSignInAccount();
        final String name = account.getDisplayName();
        final String email = account.getEmail();
        final String phone_no = "";
        final String birthday = "";
        final String gender = "Female";
        final String password = account.getFamilyName() + account.getGivenName();
        final String photo = String.valueOf(account.getPhotoUrl());
        final String photo_url = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

        name_firebase = name;
        email_firebase = email;

        String url = "https://click-1595830894120.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                if (s.equals("null")) {
                    reference.child(name_firebase).child("email").setValue(email_firebase);
                    if(photo.equals("null")){
                        reference.child(name_firebase).child("photo").setValue(photo_url);
                    }
                    reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(name_firebase)) {
                            reference.child(name_firebase).child("email").setValue(email_firebase);
                            if(photo.equals("null")){
                                reference.child(name_firebase).child("photo").setValue(photo_url);
                            }
                            reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                        } else {
                            reference.child(name_firebase).child("email").setValue(email_firebase);
                            if(photo.equals("null")){
                                reference.child(name_firebase).child("photo").setValue(photo_url);
                            }
                            reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                String url = "https://click-1595830894120.firebaseio.com/users.json";
                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                        if (s.equals("null")) {
                                            reference.child(name_firebase).child("email").setValue(email_firebase);
                                            if(photo.equals("null")){
                                                reference.child(name_firebase).child("photo").setValue(photo_url);
                                            }
                                            reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                        } else {
                                            try {
                                                JSONObject obj = new JSONObject(s);
                                                if (!obj.has(name_firebase)) {
                                                    reference.child(name_firebase).child("email").setValue(email_firebase);
                                                    if(photo.equals("null")){
                                                        reference.child(name_firebase).child("photo").setValue(photo_url);
                                                    }
                                                    reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                } else {
                                                    reference.child(name_firebase).child("email").setValue(email_firebase);
                                                    if(photo.equals("null")){
                                                        reference.child(name_firebase).child("photo").setValue(photo_url);
                                                    }
                                                    reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        try {

                                            if (error instanceof TimeoutError ) {
                                                //Time out error
                                                System.out.println("" + error);
                                            }else if(error instanceof NoConnectionError){
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
                                            }else{
                                                //Error
                                                System.out.println("" + error);
                                            }
                                            //End


                                        } catch (Exception e) {


                                        }
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                rQueue.add(request);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                            if (error instanceof TimeoutError ) {
                                //Time out error
                                System.out.println("" + error);
                            }else if(error instanceof NoConnectionError){
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
                            }else{
                                //Error
                                System.out.println("" + error);
                            }
                            //End


                        } catch (Exception e) {


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
            }, 100);

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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
}
