package com.ketekmall.ketekmall.auth.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.ketekmall.ketekmall.configs.Constant.PASSWORD_PATTERN;
import static com.ketekmall.ketekmall.configs.Constant.PICK_IMAGE_REQUEST;
import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Constant.sBIRTHDAY;
import static com.ketekmall.ketekmall.configs.Constant.sEMAIL;
import static com.ketekmall.ketekmall.configs.Constant.sGENDER;
import static com.ketekmall.ketekmall.configs.Constant.sNAME;
import static com.ketekmall.ketekmall.configs.Constant.sNULL;
import static com.ketekmall.ketekmall.configs.Constant.sPHONE_NO;
import static com.ketekmall.ketekmall.configs.Constant.sPHOTO;
import static com.ketekmall.ketekmall.configs.Constant.sTOKEN;
import static com.ketekmall.ketekmall.configs.Constant.sVERIFICATION;
import static com.ketekmall.ketekmall.configs.Link.FIREBASE_USER;
import static com.ketekmall.ketekmall.configs.Link.IMAGE_DEFAULT;
import static com.ketekmall.ketekmall.configs.Link.REGISTER;

@SuppressWarnings("deprecation")
public class Register extends AppCompatActivity {

    private String firebaseName, firebaseEmail;
    private ImageView ivUserImage;
    private EditText etName, etEmail, etPhoneNo, etPassword, etConfirmPassword;
    private ProgressBar pbLoading;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Firebase.setAndroidContext(Register.this);

        Declare();

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Register.this);
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
        etName = findViewById(R.id.name_register);
        etEmail = findViewById(R.id.email_register);
        etPhoneNo = findViewById(R.id.phone_no_register);
        etPassword = findViewById(R.id.password_register);
        etConfirmPassword = findViewById(R.id.confirm_password_register);
        pbLoading = findViewById(R.id.loading);
        btnRegister = findViewById(R.id.button_register);
        ivUserImage = findViewById(R.id.imageView);

        ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseName = etName.getText().toString();
                firebaseEmail = etEmail.getText().toString();
                SignUp(v);
            }
        });

        Button btn_goToLogin = findViewById(R.id.button_goto_login_page);
        btn_goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
                    }
                }, 100);

            }
        });
    }

    private void SignUp(View view) {
        final String strName = this.etName.getText().toString();
        final String strEmail = this.etEmail.getText().toString();
        final String strPhone_No = this.etPhoneNo.getText().toString();
        final String strPassword = this.etPassword.getText().toString();
        final String strConfirm_Password = this.etConfirmPassword.getText().toString();
        final String strBirthday = "";
        final String strGender = "Female";
        final String strVerification = "0";

        //Name
        if (strName.isEmpty()) {
            etName.requestFocus();
            etName.setError("Fields cannot be empty!");
        }

        //Email
        if (strEmail.isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError("Fields cannot be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            etEmail.requestFocus();
            etEmail.setError("Please enter a valid email address");
        }

        //Phone NO.
        if (strPhone_No.isEmpty()) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError("Fields cannot be empty!");
        } else if (!Patterns.PHONE.matcher(strPhone_No).matches()) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError("Enter only Numerical Letter");
        }

        //Password
        if (strPassword.isEmpty()) {
            etPassword.requestFocus();
            etPassword.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(strPassword).matches()) {
            etPassword.requestFocus();
            etPassword.setError("At least 8 character lengths for email");
        }

        //Other
        if (!strConfirm_Password.equals(strPassword)) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Confirm Password is different than Password");
        }

        if (Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()
                && Patterns.PHONE.matcher(strPhone_No).matches()
                && PASSWORD_PATTERN.matcher(strPassword).matches()
                && strConfirm_Password.equals(strPassword)) {

            pbLoading.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);

            //Firebase
            StringRequest request = new StringRequest(Request.Method.GET, FIREBASE_USER, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
//                    Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");
                    final Firebase REFERENCE = new Firebase("https://click-1595830894120.firebaseio.com/users");

                    if (s.equals(sNULL)) {
                        REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                        REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                        REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(firebaseName)) {
                                REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
                                REFERENCE.child(firebaseName).child(sTOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
                            } else {
                                REFERENCE.child(firebaseName).child(sEMAIL).setValue(firebaseEmail);
                                REFERENCE.child(firebaseName).child(sPHOTO).setValue(IMAGE_DEFAULT);
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

            RequestQueue rQueue = Volley.newRequestQueue(view.getContext());
            rQueue.add(request);

            //MySQL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();

                            pbLoading.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            }, 100);
                        } else {
                            Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();

                            pbLoading.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "Email is already existed", Toast.LENGTH_SHORT).show();

                        pbLoading.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
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
                    params.put(sNAME, strName);
                    params.put(sEMAIL, strEmail);
                    params.put(sPHONE_NO, strPhone_No);
                    params.put(sPHONE_NO, strPassword);
                    params.put(sBIRTHDAY, strBirthday);
                    params.put(sGENDER, strGender);
                    params.put(sPHOTO, IMAGE_DEFAULT);
                    params.put(sVERIFICATION, strVerification);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && Objects.requireNonNull(data).getData() != null) {

            // Get the Uri of data
            Uri filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivUserImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
