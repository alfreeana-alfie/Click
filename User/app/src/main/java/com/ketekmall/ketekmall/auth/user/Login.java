package com.ketekmall.ketekmall.auth.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
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
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.models.SessionManager;
import com.ketekmall.ketekmall.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.ketekmall.ketekmall.configs.Constant.PASSWORD_PATTERN;
import static com.ketekmall.ketekmall.configs.Constant.RC_SIGN_IN;
import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Constant.sADDRESS_01;
import static com.ketekmall.ketekmall.configs.Constant.sADDRESS_02;
import static com.ketekmall.ketekmall.configs.Constant.sBIRTHDAY;
import static com.ketekmall.ketekmall.configs.Constant.sDIVISION;
import static com.ketekmall.ketekmall.configs.Constant.sEMAIL;
import static com.ketekmall.ketekmall.configs.Constant.sGENDER;
import static com.ketekmall.ketekmall.configs.Constant.sID;
import static com.ketekmall.ketekmall.configs.Constant.sNAME;
import static com.ketekmall.ketekmall.configs.Constant.sNULL;
import static com.ketekmall.ketekmall.configs.Constant.sPASSWORD;
import static com.ketekmall.ketekmall.configs.Constant.sPHONE_NO;
import static com.ketekmall.ketekmall.configs.Constant.sPHOTO;
import static com.ketekmall.ketekmall.configs.Constant.sPOSTCODE;
import static com.ketekmall.ketekmall.configs.Constant.sTOKEN;
import static com.ketekmall.ketekmall.configs.Link.FIREBASE_USER;
import static com.ketekmall.ketekmall.configs.Link.IMAGE_DEFAULT;
import static com.ketekmall.ketekmall.configs.Link.LOGIN;
import static com.ketekmall.ketekmall.configs.Link.REGISTER;


@SuppressWarnings("deprecation")
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String firebaseName, firebaseEmail;
    private CallbackManager callbackManager;
    private EditText etEmail, etPassword;
    private ProgressBar pbLoading;
    private Button btnLogin;
    private SessionManager sessionManager;
    private GoogleApiClient googleApiClient;
    private RelativeLayout rlLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Declare();

        Firebase.setAndroidContext(Login.this);

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
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

    private void Declare() {
        etEmail = findViewById(R.id.email_edit);
        etPassword = findViewById(R.id.password_login);
        pbLoading = findViewById(R.id.loading);
        btnLogin = findViewById(R.id.button_login);
        Button btnGoToRegister = findViewById(R.id.button_goto_register_page);
        Button btnGoToForgotPassword = findViewById(R.id.button_goto_forgot_page);
        rlLoading = findViewById(R.id.loading_layout);
        sessionManager = new SessionManager(Login.this);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(sEMAIL);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        btnGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Forgot_Password.class));
            }
        });
    }

    private void SignIn() {
        final String mEmail = this.etEmail.getText().toString().trim();
        final String mPassword = this.etPassword.getText().toString().trim();

        if (!PASSWORD_PATTERN.matcher(mPassword).matches()) {
            etPassword.setError("Incorrect Password");
        } else if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
            pbLoading.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                                    if (success.equals("1")) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            final String strName = object.getString(sNAME).trim();
                                            final String strEmail = object.getString(sEMAIL).trim();
                                            final String strPhoneNo = object.getString(sPHONE_NO).trim();
                                            final String strAddress01 = object.getString(sADDRESS_01).trim();
                                            final String strAddress02 = object.getString(sADDRESS_02).trim();
                                            final String strDivision = object.getString(sDIVISION).trim();
                                            final String strPostcode = object.getString(sPOSTCODE).trim();
                                            final String strBirthday = object.getString(sBIRTHDAY).trim();
                                            final String strGender = object.getString(sGENDER).trim();
                                            final String strPhoto = object.getString(sPHOTO).trim();
                                            String strId = object.getString(sID).trim();

                                            sessionManager.createSession(strName, strEmail,
                                                    strPhoneNo, strAddress01,
                                                    strAddress02, strDivision,
                                                    strPostcode, strBirthday,
                                                    strGender, strId);

                                            UserDetails.email = strEmail.substring(0, strEmail.lastIndexOf("@"));

                                            //Firebase
                                            String url = "https://click-1595830894120.firebaseio.com/users.json";
                                            final String photo_url = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

                                            StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    if (s != null) {
                                                        if (s.equals(sNULL)) {
                                                            Toast.makeText(Login.this, "actionbar_chat not found", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            try {
                                                                JSONObject obj = new JSONObject(s);
                                                                if (!obj.has(strName)) {
                                                                    firebaseName = strName;
                                                                    firebaseEmail = strEmail;

                                                                    String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            if (s != null) {
                                                                                final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                                if (s.equals(sNULL)) {
                                                                                    REFERENCE.child(firebaseName)
                                                                                            .child(sEMAIL)
                                                                                            .setValue(firebaseEmail);
                                                                                    if (strPhoto.equals(sNULL)) {
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sPHOTO)
                                                                                                .setValue(IMAGE_DEFAULT);
                                                                                    } else {
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sPHOTO)
                                                                                                .setValue(strPhoto);
                                                                                    }

                                                                                    REFERENCE.child(firebaseName)
                                                                                            .child(sTOKEN)
                                                                                            .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                } else {
                                                                                    try {
                                                                                        JSONObject obj = new JSONObject(s);

                                                                                        if (!obj.has(firebaseName)) {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sEMAIL)
                                                                                                    .setValue(firebaseEmail);
                                                                                            if (strPhoto.equals(sNULL)) {
                                                                                                REFERENCE.child(firebaseName)
                                                                                                        .child(sPHOTO)
                                                                                                        .setValue(IMAGE_DEFAULT);
                                                                                            } else {
                                                                                                REFERENCE.child(firebaseName)
                                                                                                        .child(sPHOTO)
                                                                                                        .setValue(strPhoto);
                                                                                            }
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sTOKEN)
                                                                                                    .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                        } else {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sEMAIL)
                                                                                                    .setValue(firebaseEmail);
                                                                                            if (strPhoto.equals(sNULL)) {
                                                                                                REFERENCE.child(firebaseName)
                                                                                                        .child(sPHOTO)
                                                                                                        .setValue(IMAGE_DEFAULT);
                                                                                            } else {
                                                                                                REFERENCE.child(firebaseName)
                                                                                                        .child(sPHOTO)
                                                                                                        .setValue(strPhoto);
                                                                                            }
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sTOKEN)
                                                                                                    .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            } else {
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

                                                                } else if (obj.getJSONObject(strName).getString(sEMAIL).equals(strEmail)) {
                                                                    UserDetails.username = strName;
                                                                    UserDetails.email = strEmail.substring(0, strEmail.lastIndexOf("@"));

                                                                    firebaseName = strName;
                                                                    firebaseEmail = strEmail;

                                                                    StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                            if (s.equals(sNULL)) {
                                                                                REFERENCE.child(firebaseName)
                                                                                        .child(sEMAIL)
                                                                                        .setValue(firebaseEmail);
                                                                                if (strPhoto.equals(sNULL)) {
                                                                                    REFERENCE.child(firebaseName)
                                                                                            .child(sPHOTO)
                                                                                            .setValue(IMAGE_DEFAULT);
                                                                                }
                                                                                REFERENCE.child(firebaseName)
                                                                                        .child(sPHOTO)
                                                                                        .setValue(strPhoto);
                                                                                REFERENCE.child(firebaseName)
                                                                                        .child(sTOKEN)
                                                                                        .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                            } else {
                                                                                try {
                                                                                    JSONObject obj = new JSONObject(s);

                                                                                    if (!obj.has(firebaseName)) {
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sEMAIL)
                                                                                                .setValue(firebaseEmail);
                                                                                        if (strPhoto.equals(sNULL)) {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sPHOTO)
                                                                                                    .setValue(IMAGE_DEFAULT);
                                                                                        } else {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sPHOTO)
                                                                                                    .setValue(strPhoto);
                                                                                        }
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sTOKEN)
                                                                                                .setValue(FirebaseInstanceId.getInstance().getToken());
                                                                                    } else {
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sEMAIL)
                                                                                                .setValue(firebaseEmail);
                                                                                        if (strPhoto.equals(sNULL)) {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sPHOTO)
                                                                                                    .setValue(IMAGE_DEFAULT);
                                                                                        } else {
                                                                                            REFERENCE.child(firebaseName)
                                                                                                    .child(sPHOTO)
                                                                                                    .setValue(strPhoto);
                                                                                        }
                                                                                        REFERENCE.child(firebaseName)
                                                                                                .child(sTOKEN)
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
                                                    } else {
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
                                                    return getParams();
                                                }
                                            };
                                            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                                            rQueue.add(request);

                                            rlLoading.setVisibility(View.VISIBLE);
                                            Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(Login.this, Home.class);
                                                    intent.putExtra(sNAME, strName);
                                                    intent.putExtra(sEMAIL, strEmail);
                                                    intent.putExtra(sPHONE_NO, strPhoneNo);
                                                    intent.putExtra(sADDRESS_01, strAddress01);
                                                    intent.putExtra(sADDRESS_02, strAddress02);
                                                    intent.putExtra(sDIVISION, strDivision);
                                                    intent.putExtra(sPOSTCODE, strPostcode);
                                                    intent.putExtra(sBIRTHDAY, strBirthday);
                                                    intent.putExtra(sGENDER, strGender);

                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                                }
                                            }, 3000);
                                            Toast.makeText(Login.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                                            pbLoading.setVisibility(View.GONE);
                                            btnLogin.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        pbLoading.setVisibility(View.GONE);
                                        btnLogin.setVisibility(View.VISIBLE);
                                        Toast.makeText(Login.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Login.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                                    pbLoading.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.VISIBLE);
                                }
                            } else {
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
                    params.put(sEMAIL, mEmail);
                    params.put(sPASSWORD, mPassword);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(stringRequest);
        } else {
            etEmail.setError("Fields cannot be empty!");
            etPassword.setError("Fields cannot be empty!");
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
                    final String fbEmail = object.optString(sEMAIL);
                    final String phone_no = "00000000000";

                    final String birthday = "";
                    final String gender = "Female";
                    final String strPassword = object.optString("name") + "Facebook";

                    final String photo = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";

                    firebaseName = fbUserName;
                    firebaseEmail = fbEmail;

                    StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");


                            if (s.equals(sNULL)) {
                                REFERENCE.child(firebaseName).child(sEMAIL)
                                        .setValue(firebaseEmail);
                                REFERENCE.child(firebaseName).child(sPHOTO)
                                        .setValue(photo);
                                REFERENCE.child(firebaseName).child(sTOKEN)
                                        .setValue(FirebaseInstanceId.getInstance().getToken());
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(firebaseName)) {
                                        REFERENCE.child(firebaseName).child(sEMAIL)
                                                .setValue(firebaseEmail);
                                        REFERENCE.child(firebaseName).child(sPHOTO)
                                                .setValue(photo);
                                        REFERENCE.child(firebaseName).child(sTOKEN)
                                                .setValue(FirebaseInstanceId.getInstance().getToken());
                                    } else {
                                        REFERENCE.child(firebaseName).child(sEMAIL)
                                                .setValue(firebaseEmail);
                                        REFERENCE.child(firebaseName).child(sPHOTO)
                                                .setValue(photo);
                                        REFERENCE.child(firebaseName).child(sTOKEN)
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

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")) {
                                            StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                    if (s.equals(sNULL)) {
                                                        REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                                        REFERENCE.child(firebaseName).child(sPHOTO).setValue(photo);
                                                        REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                                                    } else {
                                                        try {
                                                            JSONObject obj = new JSONObject(s);
                                                            if (!obj.has(firebaseName)) {
                                                                REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(photo);
                                                                REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                                                            } else {
                                                                REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(photo);
                                                                REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
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
                            params.put(sNAME, fbUserName);
                            params.put(sEMAIL, fbEmail);
                            params.put(sPHONE_NO, phone_no);
                            params.put(sPASSWORD, strPassword);

                            params.put(sBIRTHDAY, birthday);
                            params.put(sGENDER, gender);
                            params.put(sPHOTO, photo);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    requestQueue.add(stringRequest);

                    etEmail.setText(fbEmail);
                    etPassword.setText(strPassword);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLogin.performClick();
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

        firebaseName = name;
        firebaseEmail = email;

        StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                if (s.equals(sNULL)) {
                    REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                    if (photo.equals(sNULL)) {
                        REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                    }
                    REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(firebaseName)) {
                            REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                            if (photo.equals(sNULL)) {
                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                            }
                            REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                        } else {
                            REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                            if (photo.equals(sNULL)) {
                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                            }
                            REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                        if (s.equals(sNULL)) {
                                            REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                            if (photo.equals(sNULL)) {
                                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                                            }
                                            REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                                        } else {
                                            try {
                                                JSONObject obj = new JSONObject(s);
                                                if (!obj.has(firebaseName)) {
                                                    REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                                    if (photo.equals(sNULL)) {
                                                        REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                                                    }
                                                    REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                                                } else {
                                                    REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                                    if (photo.equals(sNULL)) {
                                                        REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                                                    }
                                                    REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
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
                params.put(sNAME, name);
                params.put(sEMAIL, email);
                params.put(sPHONE_NO, phone_no);
                params.put(sPASSWORD, password);
                params.put(sBIRTHDAY, birthday);
                params.put(sGENDER, gender);
                params.put(sPHOTO, photo);
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
            etEmail.setText(Objects.requireNonNull(account).getEmail());
            etPassword.setText(account.getFamilyName() + account.getGivenName());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnLogin.performClick();
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
