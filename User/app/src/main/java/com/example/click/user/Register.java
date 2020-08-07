package com.example.click.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.page.MainActivity;
import com.example.click.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class Register extends Fragment {

    private static String URL_REGISTER = "https://annkalina53.000webhostapp.com/android_register_login/register.php";
    String name_firebase, email_firebase;
    private EditText name, email, phone_no, password, confirm_password;
    private ProgressBar loading;
    private Button button_goto_login_page, button_register;
    ImageView imageView;

    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register, container, false);
        Declare(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Firebase.setAndroidContext(view.getContext());

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_firebase = name.getText().toString();
                email_firebase = email.getText().toString();
                Register(v);

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                if(filePath != null){
                    final StorageReference storageReference1 = storageReference.child("images").child(String.valueOf(filePath));
                    storageReference1.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                    }
                                });
                            }
                        }
                    });

                }

                String url = "https://click-1595830894120.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                        if (s.equals("null")) {
                            reference.child(name_firebase).child("email").setValue(email_firebase);
                            Toast.makeText(getContext(), "registration successful", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(name_firebase)) {
                                    reference.child(name_firebase).child("email").setValue(email_firebase);
                                    Toast.makeText(getContext(), "registration successful", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "username already exists", Toast.LENGTH_LONG).show();
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

                RequestQueue rQueue = Volley.newRequestQueue(v.getContext());
                rQueue.add(request);

            }
        });

        button_goto_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
                    }
                }, 100);

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        imageView = v.findViewById(R.id.imageView);
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
            password.setError("At least 8 character lengths for email");
        }

/*
        //Confirm Password
        if (strConfirm_Password.isEmpty()) {
            confirm_password.requestFocus();
            confirm_password.setError("Fields cannot be empty!");
        } else if (!PASSWORD_PATTERN.matcher(strPassword).matches()) {
            confirm_password.requestFocus();
            confirm_password.setError("At least 8 character lengths for email");
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

            //MySQL
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
                                    Intent intent = new Intent(getContext(), MainActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
