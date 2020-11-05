package com.ketekmall.ketekmall.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.ketekmall.ketekmall.pages.MainActivity;
import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("deprecation")
public class Register extends AppCompatActivity {

    private static String URL_REGISTER = "https://ketekmall.com/ketekmall/register.php";
    private final int PICK_IMAGE_REQUEST = 22;
    private String name_firebase, email_firebase;
    private ImageView imageView;
    private EditText name, email, phone_no, password, confirm_password;
    private ProgressBar loading;
    private Button button_goto_login_page, button_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Declare();

        Firebase.setAndroidContext(Register.this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_firebase = name.getText().toString();
                email_firebase = email.getText().toString();
                SignUp(v);
            }
        });

        button_goto_login_page.setOnClickListener(new View.OnClickListener() {
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

    private void Declare() {
        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        phone_no = findViewById(R.id.phone_no_register);
        password = findViewById(R.id.password_register);
        confirm_password = findViewById(R.id.confirm_password_register);
        loading = findViewById(R.id.loading);
        button_register = findViewById(R.id.button_register);
        button_goto_login_page = findViewById(R.id.button_goto_login_page);
        imageView = findViewById(R.id.imageView);
    }

    private void SignUp(View view) {
        final String newName = this.name.getText().toString();
        final String strName = this.name.getText().toString();
        final String strEmail = this.email.getText().toString();
        final String strPhone_No = this.phone_no.getText().toString();
        final String strPassword = this.password.getText().toString();
        final String strConfirm_Password = this.confirm_password.getText().toString();
        final String strBirthday = "";
        final String strGender = "Female";
        final String strVerification ="0";

        final String strPhoto_URL = "https://ketekmall.com/ketekmall/profile_image/main_photo.png";

        final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");

        //Name
        if (strName.isEmpty()) {
            name.requestFocus();
            name.setError("Fields cannot be empty!");
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
            password.setError("At least 8 character lengths for email");
        }

        //Other
        if (!strConfirm_Password.equals(strPassword)) {
            confirm_password.requestFocus();
            confirm_password.setError("Confirm Password is different than Password");
        }

        if (Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()
                && Patterns.PHONE.matcher(strPhone_No).matches()
                && PASSWORD_PATTERN.matcher(strPassword).matches()
                && strConfirm_Password.equals(strPassword)) {

            loading.setVisibility(View.VISIBLE);
            button_register.setVisibility(View.GONE);

            //Firebase
            String url = "https://click-1595830894120.firebaseio.com/users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                    if (s.equals("null")) {
                        reference.child(name_firebase).child("email").setValue(email_firebase);
                        reference.child(name_firebase).child("photo").setValue(strPhoto_URL);
                        reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(name_firebase)) {
                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                reference.child(name_firebase).child("photo").setValue(strPhoto_URL);
                                reference.child(name_firebase).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                            } else {
                                reference.child(name_firebase).child("email").setValue(email_firebase);
                                reference.child(name_firebase).child("photo").setValue(strPhoto_URL);
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

            RequestQueue rQueue = Volley.newRequestQueue(view.getContext());
            rQueue.add(request);

            //MySQL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();

                            loading.setVisibility(View.GONE);
                            button_register.setVisibility(View.VISIBLE);

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

                            loading.setVisibility(View.GONE);
                            button_register.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "Email is already existed", Toast.LENGTH_SHORT).show();

                        loading.setVisibility(View.GONE);
                        button_register.setVisibility(View.VISIBLE);
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
                    params.put("name", strName);
                    params.put("email", strEmail);
                    params.put("phone_no", strPhone_No);
                    params.put("password", strPassword);
                    params.put("birthday", strBirthday);
                    params.put("gender", strGender);
                    params.put("photo", strPhoto_URL);
                    params.put("verification", strVerification);
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
                imageView.setImageBitmap(bitmap);
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
