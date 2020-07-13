package com.example.click;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class Activity_Home_Backup extends AppCompatActivity{

    private EditText name, email, phone_no, address, birthday, gender_display;
    private Button button_logout, button_upload_photo;
    private Menu action;
    private Spinner gender;
    private Bitmap bitmap;
    private ImageView gender_img, gender_img_spinner;
    private CircleImageView profile_image;

    ArrayAdapter<CharSequence> adapter;
    DatePickerDialog datePickerDialog;
    SessionManager sessionManager;
    String getId;

    private static String URL_READ = "http://192.168.1.15/android_register_login/read_detail.php";
    private static String URL_EDIT = "http://192.168.1.15/android_register_login/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.1.15/android_register_login/profile_image/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Declare();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(Activity_Home_Backup.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

        button_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
    }

    // Getting User Details
    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                                    gender_display.setText(strGender);

                                    gender.setVisibility(View.GONE);
                                    gender_img_spinner.setVisibility(View.GONE);
                                    Picasso.get().load(strPhoto).into(profile_image);
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Activity_Home_Backup.this, "Incorrect Informations", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Activity_Home_Backup.this, "Error!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Home_Backup.this, "Error!!", Toast.LENGTH_SHORT).show();
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

        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                                Toast.makeText(Activity_Home_Backup.this, "Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(strName, strEmail,str_Phone_no, strAddress, strBirthday, strGender, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Activity_Home_Backup.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Home_Backup.this, "Error!" + error.toString(), Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:

                name.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                phone_no.setFocusableInTouchMode(true);
                address.setFocusableInTouchMode(true);
                birthday.setFocusableInTouchMode(true);
                gender.setFocusableInTouchMode(true);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                gender_display.setVisibility(View.GONE);
                gender_img.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);
                gender_img_spinner.setVisibility(View.VISIBLE);
                return true;

            case R.id.menu_save:
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

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

                gender_display.setVisibility(View.VISIBLE);
                gender_img.setVisibility(View.VISIBLE);
                gender.setVisibility(View.GONE);
                gender_img_spinner.setVisibility(View.GONE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
                                Toast.makeText(Activity_Home_Backup.this, "Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Activity_Home_Backup.this, "Failed!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Home_Backup.this, "Failed!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadPicture(getId, getStringImage(bitmap));
        }
    }

    private void Declare() {
        name = findViewById(R.id.name_edit);
        email = findViewById(R.id.email_edit);
        phone_no = findViewById(R.id.phone_edit);
        button_logout = findViewById(R.id.button_logout);
        button_upload_photo = findViewById(R.id.button_upload_photo);
        profile_image = findViewById(R.id.profile_image);
        gender = findViewById(R.id.gender_spinner);
        address = findViewById(R.id.address_edit);
        birthday = findViewById(R.id.birthday_edit);
        gender_display = findViewById(R.id.textview_gender_display);
        gender_img = findViewById(R.id.gender_display_img);
        gender_img_spinner = findViewById(R.id.gender_display_img_spinner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        birthday.setInputType(InputType.TYPE_NULL);
        profile_image.setBorderWidth(1);

        name.setFocusable(false);
        email.setFocusable(false);
        phone_no.setFocusable(false);
        address.setFocusable(false);
        birthday.setFocusable(false);
        gender.setFocusable(false);

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone_no.setFocusableInTouchMode(false);
        address.setFocusableInTouchMode(false);
        birthday.setFocusableInTouchMode(false);
        gender.setFocusableInTouchMode(false);
    }
}
