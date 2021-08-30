package com.ketekmall.ketekmall.activities.users;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.configs.Setup;
import com.ketekmall.ketekmall.models.Item_All_Details;
import com.ketekmall.ketekmall.models.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Link.EDIT_PROFILE_DETAILS;
import static com.ketekmall.ketekmall.configs.Link.GET_INCOME;
import static com.ketekmall.ketekmall.configs.Link.GET_PROFILE_DETAILS;
import static com.ketekmall.ketekmall.configs.Link.PROFILE_IMAGE_UPLOAD;

import static com.ketekmall.ketekmall.configs.Constant.*;

public class UserProfile extends AppCompatActivity {

    Uri filePath;
    SessionManager sessionManager;
    Setup setup;
    private String getId;
    private ArrayAdapter<CharSequence> adapterGender;
    private DatePickerDialog dpdBirthday;
    private LinearLayout llGenderDisplay, llGender, llIncome, llIcNo, llBankName, llBankAcc;
    private EditText etName, etEmail, etPhoneNo, etAddress01, etAddress02, etDivision, etPostcode,
            etBirthday, etGenderDisplay, etIcNo, etBankName, etBankAcc;
    private Button btnEditProfile, btnSavedProfile;
    private TextView tvIncome;
    private ImageButton btnEditImage;
    private Spinner spinGender;
    private Bitmap bitmapUserImage;
    private ImageView ivGender;
    private CircleImageView civUserImage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        Declare();

        getUserDetail();
        Buying_List();

        setupUI(findViewById(R.id.parent));
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(UserProfile.this);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Declare() {
        setup = new Setup(this);
        getId = setup.getUserId();

        etName = findViewById(R.id.name_edit);
        etEmail = findViewById(R.id.email_edit);
        etPhoneNo = findViewById(R.id.phone_edit);
        btnEditProfile = findViewById(R.id.button_edit);
        btnSavedProfile = findViewById(R.id.button_accept);
        btnEditImage = findViewById(R.id.button_edit_photo);
        civUserImage = findViewById(R.id.profile_image);
        spinGender = findViewById(R.id.gender_spinner);

        etIcNo = findViewById(R.id.icno_edit);
        etBankName = findViewById(R.id.bank_name_edit);
        etBankAcc = findViewById(R.id.bank_acc_edit);
        tvIncome = findViewById(R.id.income_text);

        llIncome = findViewById(R.id.layout_income);
        llBankName = findViewById(R.id.layout_bankName);
        llBankAcc = findViewById(R.id.layout_bankAcc);
        llIcNo = findViewById(R.id.layout_icno);

        etAddress01 = findViewById(R.id.address_edit01);
        etAddress02 = findViewById(R.id.address_edit02);
        etDivision = findViewById(R.id.city_edit);
        etPostcode = findViewById(R.id.postcode_edit);

        etBirthday = findViewById(R.id.birthday_edit);
        etGenderDisplay = findViewById(R.id.textview_gender_display);
        ivGender = findViewById(R.id.gender_display_img_spinner);
        llGenderDisplay = findViewById(R.id.layout_gender_display);
        llGender = findViewById(R.id.layout_gender);

        etGenderDisplay.setText(spinGender.getSelectedItem().toString());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(UserProfile.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(UserProfile.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(UserProfile.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });


        adapterGender = ArrayAdapter.createFromResource(UserProfile.this, R.array.gender, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(adapterGender);

        etBirthday.setInputType(InputType.TYPE_NULL);
        civUserImage.setBorderWidth(1);

        etName.setFocusable(false);
        etEmail.setFocusable(false);
        etPhoneNo.setFocusable(false);

        etAddress01.setFocusable(false);
        etAddress02.setFocusable(false);
        etDivision.setFocusable(false);
        etPostcode.setFocusable(false);

        etBirthday.setFocusable(false);
        etGenderDisplay.setFocusable(false);

        etIcNo.setFocusable(false);
        etBankName.setFocusable(false);
        etBankAcc.setFocusable(false);

        etName.setFocusableInTouchMode(false);
        etEmail.setFocusableInTouchMode(false);
        etPhoneNo.setFocusableInTouchMode(false);

        etAddress01.setFocusableInTouchMode(false);
        etAddress02.setFocusableInTouchMode(false);
        etDivision.setFocusableInTouchMode(false);
        etPostcode.setFocusableInTouchMode(false);

        etBirthday.setFocusableInTouchMode(false);
        etGenderDisplay.setFocusableInTouchMode(false);

        etIcNo.setFocusableInTouchMode(false);
        etBankName.setFocusableInTouchMode(false);
        etBankAcc.setFocusableInTouchMode(false);

        llGender.setVisibility(View.GONE);
        btnSavedProfile.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfile.this, Me.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Func();

            }
        });
        btnSavedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accept_Func();
                SaveEditDetail();

            }
        });
    }

    private void Edit_Func() {
        etName.setFocusable(true);
        etEmail.setFocusable(true);
        etPhoneNo.setFocusable(true);

        etAddress01.setFocusable(true);
        etAddress02.setFocusable(true);
        etDivision.setFocusable(true);
        etPostcode.setFocusable(true);

        etBirthday.setFocusable(true);

        etIcNo.setFocusable(false);
        etBankName.setFocusable(true);
        etBankAcc.setFocusable(true);

        etName.setFocusableInTouchMode(true);
        etEmail.setFocusableInTouchMode(true);
        etPhoneNo.setFocusableInTouchMode(true);

        etAddress01.setFocusableInTouchMode(true);
        etAddress02.setFocusableInTouchMode(true);
        etDivision.setFocusableInTouchMode(true);
        etPostcode.setFocusableInTouchMode(true);

        etIcNo.setFocusableInTouchMode(false);
        etBankName.setFocusableInTouchMode(true);
        etBankAcc.setFocusableInTouchMode(true);

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                dpdBirthday = new DatePickerDialog(v.getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String Birthday = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etBirthday.setText(Birthday);
                    }
                }, year, month, day);
                dpdBirthday.show();
            }
        });

        btnSavedProfile.setVisibility(View.VISIBLE);
        llGender.setVisibility(View.VISIBLE);
        spinGender.setVisibility(View.VISIBLE);
        ivGender.setVisibility(View.VISIBLE);
        btnEditImage.setVisibility(View.VISIBLE);

        btnEditProfile.setVisibility(View.GONE);
        llGenderDisplay.setVisibility(View.GONE);
    }

    private void Accept_Func() {
        etName.setFocusable(false);
        etEmail.setFocusable(false);
        etPhoneNo.setFocusable(false);
        etAddress01.setFocusable(false);
        etBirthday.setFocusable(false);
        etGenderDisplay.setFocusable(false);
        etIcNo.setFocusable(false);
        etBankName.setFocusable(false);
        etBankAcc.setFocusable(false);

        etName.setFocusableInTouchMode(false);
        etEmail.setFocusableInTouchMode(false);
        etPhoneNo.setFocusableInTouchMode(false);
        etAddress01.setFocusableInTouchMode(false);
        etBirthday.setFocusableInTouchMode(false);
        etGenderDisplay.setFocusableInTouchMode(false);
        etIcNo.setFocusableInTouchMode(false);
        etBankName.setFocusableInTouchMode(false);
        etBankAcc.setFocusableInTouchMode(false);

        llGenderDisplay.setVisibility(View.VISIBLE);
        btnEditProfile.setVisibility(View.VISIBLE);
        btnSavedProfile.setVisibility(View.GONE);
        llGender.setVisibility(View.GONE);
        btnEditImage.setVisibility(View.GONE);
        etGenderDisplay.setText(spinGender.getSelectedItem().toString());
    }

    // Getting User Details
    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String strName = object.getString(sNAME).trim();
                                        String strEmail = object.getString(sEMAIL).trim();
                                        String strPhone_no = object.getString(sPHONE_NO).trim();
                                        String strAddress01 = object.getString(sADDRESS_01).trim();
                                        String strAddress02 = object.getString(sADDRESS_02).trim();
                                        String strCity = object.getString(sDIVISION).trim();
                                        String strPostCode = object.getString(sPOSTCODE).trim();
                                        String strBirthday = object.getString(sBIRTHDAY).trim();
                                        String strGender = object.getString(sGENDER);
                                        String strPhoto = object.getString(sPHOTO);
                                        String strICNO = object.getString(sIC_NO).trim();
                                        String strBankName = object.getString(sBANK_NAME);
                                        String strBankAcc = object.getString(sBANK_ACCOUNT);
                                        int strVerify = Integer.parseInt(object.getString(sVERIFICATION));

                                        etName.setText(strName);
                                        etEmail.setText(strEmail);
                                        etPhoneNo.setText(strPhone_no);
                                        etAddress01.setText(strAddress01);
                                        etAddress02.setText(strAddress02);
                                        etDivision.setText(strCity);
                                        etPostcode.setText(strPostCode);
                                        etBirthday.setText(strBirthday);
                                        spinGender.setSelection(adapterGender.getPosition(strGender));
                                        etGenderDisplay.setText(strGender);

                                        etIcNo.setText(strICNO);
                                        etBankName.setText(strBankName);
                                        etBankAcc.setText(strBankAcc);

                                        if (strVerify == 0) {
                                            llIcNo.setVisibility(View.GONE);
                                            llBankName.setVisibility(View.GONE);
                                            llBankAcc.setVisibility(View.GONE);
                                            llIncome.setVisibility(View.GONE);
                                        } else {
                                            llIcNo.setVisibility(View.VISIBLE);
                                            llBankName.setVisibility(View.VISIBLE);
                                            llBankAcc.setVisibility(View.VISIBLE);
                                            llIncome.setVisibility(View.VISIBLE);
                                        }

                                        spinGender.setVisibility(View.GONE);
                                        ivGender.setVisibility(View.GONE);
                                        Picasso.get().load(strPhoto).into(civUserImage);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UserProfile.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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
                            //End


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sID, getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Save User Details
    private void SaveEditDetail() {
        final String strName = this.etName.getText().toString().trim();
        final String strEmail = this.etEmail.getText().toString().trim();
        final String str_Phone_no = this.etPhoneNo.getText().toString().trim();

        final String strAddress01 = this.etAddress01.getText().toString().trim();
        final String strAddress02 = this.etAddress02.getText().toString().trim();
        final String strCity = this.etDivision.getText().toString().trim();
        final String strPostCode = this.etPostcode.getText().toString().trim();

        final String strBirthday = this.etBirthday.getText().toString().trim();
        final String strGender = this.spinGender.getSelectedItem().toString().trim();
        final String strAccNo = this.etBankAcc.getText().toString();
        final String strBankName = this.etBankName.getText().toString();

        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    sessionManager.createSession(strName, strEmail, str_Phone_no, strAddress01, strAddress02, strCity, strPostCode, strBirthday, strGender, id);
                                    Toast.makeText(UserProfile.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserProfile.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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
                            //End


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
                params.put(sPHONE_NO, str_Phone_no);
                params.put(sADDRESS_01, strAddress01);
                params.put(sADDRESS_02, strAddress02);
                params.put(sDIVISION, strCity);
                params.put(sPOSTCODE, strPostCode);
                params.put(sBIRTHDAY, strBirthday);
                params.put(sGENDER, strGender);
                params.put(sBANK_ACCOUNT, strAccNo);
                params.put(sBANK_NAME, strBankName);
                params.put(sID, id);
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

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void UploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_IMAGE_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UserProfile.this, "Success!", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UserProfile.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(UserProfile.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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
                            //End


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sPHOTO, photo);
                params.put(sID, id);
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
                bitmapUserImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                civUserImage.setImageBitmap(bitmapUserImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(getId, getStringImage(bitmapUserImage));
        }

    }

    private void Buying_List() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, GET_INCOME,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Log.e("onResponse", "Return NULL");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("read");

                                double grandtotal = 0.00;
                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        final String id = object.getString(sID).trim();
                                        final String main_category = object.getString(sMAIN_CATEGORY).trim();
                                        final String sub_category = object.getString(sSUB_CATEGORY).trim();
                                        final String ad_detail = object.getString(sAD_DETAIL).trim();
                                        final double price = Double.parseDouble(object.getString(sPRICE).trim());
                                        final String division = object.getString(sDIVISION);
                                        final String district = object.getString(sDISTRICT);
                                        final String image_item = object.getString(sPHOTO);
                                        final String seller_id = object.getString(sSELLER_ID);
                                        final String quantity = object.getString(sQUANTITY);

                                        grandtotal += (price * Integer.parseInt(quantity));
                                        tvIncome.setText("MYR" + String.format("%.2f", grandtotal));

                                        final Item_All_Details item = new Item_All_Details(id,
                                                seller_id,
                                                main_category,
                                                sub_category,
                                                ad_detail,
                                                String.format("%.2f", price),
                                                division,
                                                district,
                                                image_item);
                                        item.setQuantity(quantity);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                            Toast.makeText(Cart.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                    //End


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(sSELLER_ID, getId);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(UserProfile.this);
        requestQueue1.add(stringRequest1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, Me.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
