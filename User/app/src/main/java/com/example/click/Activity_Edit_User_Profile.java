package com.example.click;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Edit_User_Profile extends AppCompatActivity {

    private static String URL_READ = "https://annkalina53.000webhostapp.com/android_register_login/read_detail.php";
    private static String URL_EDIT = "https://annkalina53.000webhostapp.com/android_register_login/edit_detail.php";
    private static String URL_UPLOAD = "https://annkalina53.000webhostapp.com/android_register_login/upload.php";
    SessionManager sessionManager;
    String getId;
    Uri filePath;
    private ArrayAdapter<CharSequence> adapter_gender;
    private DatePickerDialog datePickerDialog;
    private LinearLayout layout_gender_display, layout_gender;
    private EditText name, email, phone_no, address, birthday, gender_display;
    private Button button_logout, button_edit, button_accept;
    private ImageButton button_edit_photo;
    private Spinner gender;
    private Bitmap bitmap;
    private ImageView gender_img, gender_img_spinner;
    private CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        Declare();
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        getUserDetail();
        gender_display.setText(gender.getSelectedItem().toString());

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

        button_edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Func();
                SaveEditDetail();
            }
        });
        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accept_Func();
            }
        });
    }

    private void Declare() {
        name = findViewById(R.id.name_edit);
        email = findViewById(R.id.email_edit);
        phone_no = findViewById(R.id.phone_edit);
        button_logout = findViewById(R.id.button_logout);
        button_edit = findViewById(R.id.button_edit);
        button_accept = findViewById(R.id.button_accept);
        button_edit_photo = findViewById(R.id.button_edit_photo);
        profile_image = findViewById(R.id.profile_image);
        gender = findViewById(R.id.gender_spinner);
        address = findViewById(R.id.address_edit);
        birthday = findViewById(R.id.birthday_edit);
        gender_display = findViewById(R.id.textview_gender_display);
        gender_img = findViewById(R.id.gender_display_img);
        gender_img_spinner = findViewById(R.id.gender_display_img_spinner);
        layout_gender_display = findViewById(R.id.layout_gender_display);
        layout_gender = findViewById(R.id.layout_gender);

        adapter_gender = ArrayAdapter.createFromResource(Activity_Edit_User_Profile.this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter_gender);

        birthday.setInputType(InputType.TYPE_NULL);
        profile_image.setBorderWidth(1);

        name.setFocusable(false);
        email.setFocusable(false);
        phone_no.setFocusable(false);
        address.setFocusable(false);
        birthday.setFocusable(false);
        gender_display.setFocusable(false);

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone_no.setFocusableInTouchMode(false);
        address.setFocusableInTouchMode(false);
        birthday.setFocusableInTouchMode(false);
        gender_display.setFocusableInTouchMode(false);

        layout_gender.setVisibility(View.GONE);
        button_accept.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_All_View.class));
            }
        });

    }

    private void Edit_Func() {
        name.setFocusable(true);
        email.setFocusable(true);
        phone_no.setFocusable(true);
        address.setFocusable(true);
        birthday.setFocusable(true);

        name.setFocusableInTouchMode(true);
        email.setFocusableInTouchMode(true);
        phone_no.setFocusableInTouchMode(true);
        address.setFocusableInTouchMode(true);
        birthday.setFocusableInTouchMode(true);
        gender.setFocusableInTouchMode(true);


        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        button_accept.setVisibility(View.VISIBLE);
        layout_gender.setVisibility(View.VISIBLE);
        gender.setVisibility(View.VISIBLE);
        gender_img_spinner.setVisibility(View.VISIBLE);
        button_edit_photo.setVisibility(View.VISIBLE);

        button_edit.setVisibility(View.GONE);
        layout_gender_display.setVisibility(View.GONE);
    }

    private void Accept_Func() {
        name.setFocusable(false);
        email.setFocusable(false);
        phone_no.setFocusable(false);
        address.setFocusable(false);
        birthday.setFocusable(false);
        gender_display.setFocusable(false);

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone_no.setFocusableInTouchMode(false);
        address.setFocusableInTouchMode(false);
        birthday.setFocusableInTouchMode(false);
        gender_display.setFocusableInTouchMode(false);

        layout_gender_display.setVisibility(View.VISIBLE);
        button_edit.setVisibility(View.VISIBLE);
        button_accept.setVisibility(View.GONE);
        layout_gender.setVisibility(View.GONE);
        button_edit_photo.setVisibility(View.GONE);
        gender_display.setText(gender.getSelectedItem().toString());
    }

    // Getting User Details
    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(Activity_Edit_User_Profile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");


                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strPhone_no = object.getString("phone_no").trim();
                                    String strAddress = object.getString("address").trim();
                                    String strBirthday = object.getString("birthday").trim();
                                    String strGender = object.getString("gender");
                                    String strPhoto = object.getString("photo");

                                    name.setText(strName);
                                    email.setText(strEmail);
                                    phone_no.setText(strPhone_no);
                                    address.setText(strAddress);
                                    birthday.setText(strBirthday);
                                    gender.setSelection(adapter_gender.getPosition(strGender));
                                    gender_display.setText(strGender);

                                    gender.setVisibility(View.GONE);
                                    gender_img_spinner.setVisibility(View.GONE);
                                    Picasso.get().load(strPhoto).into(profile_image);
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Activity_Edit_User_Profile.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Save User Details
    private void SaveEditDetail() {
        final String strName = this.name.getText().toString().trim();
        final String strEmail = this.email.getText().toString().trim();
        final String str_Phone_no = this.phone_no.getText().toString().trim();
        final String strAddress = this.address.getText().toString().trim();
        final String strBirthday = this.birthday.getText().toString().trim();
        final String strGender = this.gender.getSelectedItem().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(Activity_Edit_User_Profile.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                sessionManager.createSession(strName, strEmail, str_Phone_no, strAddress, strBirthday, strGender, id);
                                Toast.makeText(Activity_Edit_User_Profile.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Activity_Edit_User_Profile.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", strName);
                params.put("email", strEmail);
                params.put("phone_no", str_Phone_no);
                params.put("address", strAddress);
                params.put("birthday", strBirthday);
                params.put("gender", strGender);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void UploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                progressDialog.dismiss();
//                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Activity_Edit_User_Profile.this, "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), "Connection Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo", photo);
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UploadPicture(getId, getStringImage(bitmap));
    }
}
