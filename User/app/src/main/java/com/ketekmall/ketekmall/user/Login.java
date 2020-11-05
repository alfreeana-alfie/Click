package com.ketekmall.ketekmall.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.ketekmall.ketekmall.pages.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


@SuppressWarnings("deprecation")
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private static String URL_LOGIN = "https://ketekmall.com/ketekmall/login.php";
    private static String URL_REGISTER = "https://ketekmall.com/ketekmall/register.php";
    private final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");

    private String name_firebase, email_firebase;
    private CallbackManager callbackManager;
    private EditText email, password;
    private ProgressBar loading;
    private Button button_login, button_goto_register_page, button_goto_forgot_page;
    private SessionManager sessionManager;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private String getId;
    private RelativeLayout loading_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Declare();

        Firebase.setAndroidContext(Login.this);
        sessionManager = new SessionManager(Login.this);

        loading_layout = findViewById(R.id.loading_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

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
                SignIn();
            }
        });

        button_goto_register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
//                final Fragment fragment_register = new Register();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
//                fragmentTransaction.replace(R.id.framelayout, fragment_register);
//                fragmentTransaction.commit();
            }
        });

        button_goto_forgot_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Forgot_Password.class));
//                final Fragment fragment_forgot_password = new Forgot_Password();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
//                fragmentTransaction.replace(R.id.framelayout, fragment_forgot_password);
//                fragmentTransaction.commit();
            }
        });
    }

    private void Declare() {
        email = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_login);
        loading = findViewById(R.id.loading);
        button_login = findViewById(R.id.button_login);
        button_goto_register_page = findViewById(R.id.button_goto_register_page);
        button_goto_forgot_page = findViewById(R.id.button_goto_forgot_page);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
//        loginButton.setFragment(Login.this);

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
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SignIn() {
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
                            if(response != null){
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

                                            sessionManager.createSession(name, email,
                                                    phone_no, address_01,
                                                    address_02, city,
                                                    postcode, birthday,
                                                    gender, id);

                                            String newemail = email.substring(0, email.lastIndexOf("@"));

                                            Log.d("TAG: ", newemail);
                                            UserDetails.email = newemail;

                                            //Firebase
                                            String url = "https://click-1595830894120.firebaseio.com/users.json";
                                            final String photo_url = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    if(s != null){
                                                        if (s.equals("null")) {
                                                            Toast.makeText(Login.this, "actionbar_chat not found", Toast.LENGTH_LONG).show();
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
                                                                            if (s != null){
                                                                                final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                                if (s.equals("null")) {
                                                                                    reference.child(name_firebase)
                                                                                            .child("email")
                                                                                            .setValue(email_firebase);
                                                                                    if(photo.equals("null")){
                                                                                        reference.child(name_firebase)
                                                                                                .child("photo")
                                                                                                .setValue(photo_url);
                                                                                    }else {
                                                                                        reference.child(name_firebase)
                                                                                                .child("photo")
                                                                                                .setValue(photo);
                                                                                    }

                                                                                    reference.child(name_firebase)
                                                                                            .child("token")
                                                                                            .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                } else {
                                                                                    try {
                                                                                        JSONObject obj = new JSONObject(s);

                                                                                        if (!obj.has(name_firebase)) {
                                                                                            reference.child(name_firebase)
                                                                                                    .child("email")
                                                                                                    .setValue(email_firebase);
                                                                                            if(photo.equals("null")){
                                                                                                reference.child(name_firebase)
                                                                                                        .child("photo")
                                                                                                        .setValue(photo_url);
                                                                                            }else {
                                                                                                reference.child(name_firebase)
                                                                                                        .child("photo")
                                                                                                        .setValue(photo);
                                                                                            }
                                                                                            reference.child(name_firebase)
                                                                                                    .child("token")
                                                                                                    .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                        } else {
                                                                                            reference.child(name_firebase)
                                                                                                    .child("email")
                                                                                                    .setValue(email_firebase);
                                                                                            if(photo.equals("null")){
                                                                                                reference.child(name_firebase)
                                                                                                        .child("photo")
                                                                                                        .setValue(photo_url);
                                                                                            }else {
                                                                                                reference.child(name_firebase)
                                                                                                        .child("photo")
                                                                                                        .setValue(photo);
                                                                                            }
                                                                                            reference.child(name_firebase)
                                                                                                    .child("token")
                                                                                                    .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }else{
                                                                                Log.e("onResponse", "Return NULL");
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
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });

                                                                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                                                                    rQueue.add(request);

                                                                } else if (obj.getJSONObject(name).getString("email").equals(email)) {
                                                                    UserDetails.username = name;
                                                                    String newemail = email.substring(0, email.lastIndexOf("@"));
                                                                    UserDetails.email = newemail;

                                                                    name_firebase = name;
                                                                    email_firebase = email;

                                                                    String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                            if (s.equals("null")) {
                                                                                reference.child(name_firebase)
                                                                                        .child("email")
                                                                                        .setValue(email_firebase);
                                                                                if(photo.equals("null")){
                                                                                    reference.child(name_firebase)
                                                                                            .child("photo")
                                                                                            .setValue(photo_url);
                                                                                }
                                                                                reference.child(name_firebase)
                                                                                        .child("photo")
                                                                                        .setValue(photo);
                                                                                reference.child(name_firebase)
                                                                                        .child("token")
                                                                                        .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            } else {
                                                                                try {
                                                                                    JSONObject obj = new JSONObject(s);

                                                                                    if (!obj.has(name_firebase)) {
                                                                                        reference.child(name_firebase)
                                                                                                .child("email")
                                                                                                .setValue(email_firebase);
                                                                                        if(photo.equals("null")){
                                                                                            reference.child(name_firebase)
                                                                                                    .child("photo")
                                                                                                    .setValue(photo_url);
                                                                                        }else {
                                                                                            reference.child(name_firebase)
                                                                                                    .child("photo")
                                                                                                    .setValue(photo);
                                                                                        }
                                                                                        reference.child(name_firebase)
                                                                                                .child("token")
                                                                                                .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                    } else {
                                                                                        reference.child(name_firebase)
                                                                                                .child("email")
                                                                                                .setValue(email_firebase);
                                                                                        if(photo.equals("null")){
                                                                                            reference.child(name_firebase)
                                                                                                    .child("photo")
                                                                                                    .setValue(photo_url);
                                                                                        }else {
                                                                                            reference.child(name_firebase)
                                                                                                    .child("photo")
                                                                                                    .setValue(photo);
                                                                                        }
                                                                                        reference.child(name_firebase)
                                                                                                .child("token")
                                                                                                .setValue(FirebaseInstanceId.getInstance().getToken());
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
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    })  {
                                                                        @Override
                                                                        protected Map<String, String> getParams() {
                                                                            return getParams();
                                                                        }
                                                                    };
                                                                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                                                                    rQueue.add(request);
                                                                } else {
                                                                    Toast.makeText(Login.this, "incorrect email", Toast.LENGTH_LONG).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }else{
                                                        Log.e("onResponse", "Return NULL");
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
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            })  {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    return getParams();
                                                }
                                            };
                                            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                                            rQueue.add(request);

                                            loading_layout.setVisibility(View.VISIBLE);
                                            Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(Login.this, Homepage.class);
                                                    intent.putExtra("name", name);
                                                    intent.putExtra("email", email);
                                                    intent.putExtra("phone_no", phone_no);
                                                    intent.putExtra("address_01", address_01);
                                                    intent.putExtra("address_02", address_02);
                                                    intent.putExtra("division", city);
                                                    intent.putExtra("postcode", postcode);
                                                    intent.putExtra("birthday", birthday);
                                                    intent.putExtra("gender", gender);

                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                                }
                                            }, 3000);
                                            Toast.makeText(Login.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                                            loading.setVisibility(View.GONE);
                                            button_login.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        loading.setVisibility(View.GONE);
                                        button_login.setVisibility(View.VISIBLE);
                                        Toast.makeText(Login.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Login.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    button_login.setVisibility(View.VISIBLE);
                                }
                            }else{
                                Log.e("onResponse", "Return NULL");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", mEmail);
                    params.put("password", mPassword);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
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
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
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
                                reference.child(name_firebase).child("email")
                                        .setValue(email_firebase);
                                reference.child(name_firebase).child("photo")
                                        .setValue(photo);
                                reference.child(name_firebase).child("token")
                                        .setValue(FirebaseInstanceId.getInstance().getToken());
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(name_firebase)) {
                                        reference.child(name_firebase).child("email")
                                                .setValue(email_firebase);
                                        reference.child(name_firebase).child("photo")
                                                .setValue(photo);
                                        reference.child(name_firebase).child("token")
                                                .setValue(FirebaseInstanceId.getInstance().getToken());
                                    } else {
                                        reference.child(name_firebase).child("email")
                                                .setValue(email_firebase);
                                        reference.child(name_firebase).child("photo")
                                                .setValue(photo);
                                        reference.child(name_firebase).child("token")
                                                .setValue(FirebaseInstanceId.getInstance().getToken());
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

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
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
                                            });

                                            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
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
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Google_SignIn(GoogleSignInResult result) {
        final GoogleSignInAccount account = result.getSignInAccount();
        final String name = Objects.requireNonNull(account).getDisplayName();
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

        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
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
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Google_SignIn(result);
            GoogleSignInAccount account = result.getSignInAccount();
            email.setText(Objects.requireNonNull(account).getEmail());
            password.setText(account.getFamilyName() + account.getGivenName());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_login.performClick();
                }
            }, 100);

        } else {
            Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(Objects.requireNonNull(result));
        }
    }
}
