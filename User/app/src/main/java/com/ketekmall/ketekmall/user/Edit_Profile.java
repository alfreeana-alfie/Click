package com.ketekmall.ketekmall.user;

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
import android.view.View;
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
import com.ketekmall.ketekmall.data.Item_All_Details;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
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

public class Edit_Profile extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    private static String URL_EDIT = "https://ketekmall.com/ketekmall/edit_detail.php";
    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/profile_image/upload.php";
    private static String URL_READ_ORDER = "https://ketekmall.com/ketekmall/read_order_buyer_done_profile.php";
    public Uri filePath;
    public SessionManager sessionManager;
    public String getId;
    public BottomNavigationView bottomNav;
    private ArrayAdapter<CharSequence> adapter_gender;
    private DatePickerDialog datePickerDialog;
    private LinearLayout layout_gender_display, layout_gender, layout_income, layout_icno, layout_bankName, layout_BankAcc;
    private EditText name, email, phone_no, address_01, address_02, city, postcode, birthday, gender_display, icno, bank_name, bank_acc;
    private Button button_edit, button_accept;
    private TextView income;
    private ImageButton button_edit_photo;
    private Spinner gender;
    private Bitmap bitmap;
    private ImageView gender_img_spinner;
    private CircleImageView profile_image;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        Declare();
        getSession();
        getUserDetail();
        Buying_List();

        gender_display.setText(gender.getSelectedItem().toString());
        Button_Func();
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Declare() {
        name = findViewById(R.id.name_edit);
        email = findViewById(R.id.email_edit);
        phone_no = findViewById(R.id.phone_edit);
        button_edit = findViewById(R.id.button_edit);
        button_accept = findViewById(R.id.button_accept);
        button_edit_photo = findViewById(R.id.button_edit_photo);
        profile_image = findViewById(R.id.profile_image);
        gender = findViewById(R.id.gender_spinner);

        icno = findViewById(R.id.icno_edit);
        bank_name = findViewById(R.id.bank_name_edit);
        bank_acc = findViewById(R.id.bank_acc_edit);
        income = findViewById(R.id.income_text);

        layout_income = findViewById(R.id.layout_income);
        layout_bankName = findViewById(R.id.layout_bankName);
        layout_BankAcc = findViewById(R.id.layout_bankAcc);
        layout_icno = findViewById(R.id.layout_icno);

        address_01 = findViewById(R.id.address_edit01);
        address_02 = findViewById(R.id.address_edit02);
        city = findViewById(R.id.city_edit);
        postcode = findViewById(R.id.postcode_edit);

        birthday = findViewById(R.id.birthday_edit);
        gender_display = findViewById(R.id.textview_gender_display);
        gender_img_spinner = findViewById(R.id.gender_display_img_spinner);
        layout_gender_display = findViewById(R.id.layout_gender_display);
        layout_gender = findViewById(R.id.layout_gender);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Edit_Profile.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

//                    case R.id.nav_feed:
//                        Intent intent5 = new Intent(Edit_Profile.this, Feed_page.class);
//                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent5);
//                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Edit_Profile.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Edit_Profile.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });


        adapter_gender = ArrayAdapter.createFromResource(Edit_Profile.this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter_gender);

        birthday.setInputType(InputType.TYPE_NULL);
        profile_image.setBorderWidth(1);

        name.setFocusable(false);
        email.setFocusable(false);
        phone_no.setFocusable(false);

        address_01.setFocusable(false);
        address_02.setFocusable(false);
        city.setFocusable(false);
        postcode.setFocusable(false);

        birthday.setFocusable(false);
        gender_display.setFocusable(false);

        icno.setFocusable(false);
        bank_name.setFocusable(false);
        bank_acc.setFocusable(false);

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone_no.setFocusableInTouchMode(false);

        address_01.setFocusableInTouchMode(false);
        address_02.setFocusableInTouchMode(false);
        city.setFocusableInTouchMode(false);
        postcode.setFocusableInTouchMode(false);

        birthday.setFocusableInTouchMode(false);
        gender_display.setFocusableInTouchMode(false);

        icno.setFocusableInTouchMode(false);
        bank_name.setFocusableInTouchMode(false);
        bank_acc.setFocusableInTouchMode(false);

        layout_gender.setVisibility(View.GONE);
        button_accept.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Edit_Profile.this, Me_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void Edit_Func() {
        name.setFocusable(true);
        email.setFocusable(true);
        phone_no.setFocusable(true);

        address_01.setFocusable(true);
        address_02.setFocusable(true);
        city.setFocusable(true);
        postcode.setFocusable(true);

        birthday.setFocusable(true);

        icno.setFocusable(false);
        bank_name.setFocusable(true);
        bank_acc.setFocusable(true);

        name.setFocusableInTouchMode(true);
        email.setFocusableInTouchMode(true);
        phone_no.setFocusableInTouchMode(true);

        address_01.setFocusableInTouchMode(true);
        address_02.setFocusableInTouchMode(true);
        city.setFocusableInTouchMode(true);
        postcode.setFocusableInTouchMode(true);

        icno.setFocusableInTouchMode(false);
        bank_name.setFocusableInTouchMode(true);
        bank_acc.setFocusableInTouchMode(true);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(v.getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String Birthday = dayOfMonth + "/" + (month + 1) + "/" + year;
                        birthday.setText(Birthday);
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
        address_01.setFocusable(false);
        birthday.setFocusable(false);
        gender_display.setFocusable(false);
        icno.setFocusable(false);
        bank_name.setFocusable(false);
        bank_acc.setFocusable(false);

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone_no.setFocusableInTouchMode(false);
        address_01.setFocusableInTouchMode(false);
        birthday.setFocusableInTouchMode(false);
        gender_display.setFocusableInTouchMode(false);
        icno.setFocusableInTouchMode(false);
        bank_name.setFocusableInTouchMode(false);
        bank_acc.setFocusableInTouchMode(false);

        layout_gender_display.setVisibility(View.VISIBLE);
        button_edit.setVisibility(View.VISIBLE);
        button_accept.setVisibility(View.GONE);
        layout_gender.setVisibility(View.GONE);
        button_edit_photo.setVisibility(View.GONE);
        gender_display.setText(gender.getSelectedItem().toString());
    }

    private void Button_Func() {

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

            }
        });
        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accept_Func();
                SaveEditDetail();

            }
        });

    }

    // Getting User Details
    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(Edit_Profile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
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

                                        String strName = object.getString("name").trim();
                                        String strEmail = object.getString("email").trim();
                                        String strPhone_no = object.getString("phone_no").trim();
                                        String strAddress01 = object.getString("address_01").trim();
                                        String strAddress02 = object.getString("address_02").trim();
                                        String strCity = object.getString("division").trim();
                                        String strPostCode = object.getString("postcode").trim();
                                        String strBirthday = object.getString("birthday").trim();
                                        String strGender = object.getString("gender");
                                        String strPhoto = object.getString("photo");
                                        String strICNO = object.getString("ic_no").trim();
                                        String strBankName = object.getString("bank_name");
                                        String strBankAcc = object.getString("bank_acc");
                                        int strVerify = Integer.parseInt(object.getString("verification"));

                                        name.setText(strName);
                                        email.setText(strEmail);
                                        phone_no.setText(strPhone_no);
                                        address_01.setText(strAddress01);
                                        address_02.setText(strAddress02);
                                        city.setText(strCity);
                                        postcode.setText(strPostCode);
                                        birthday.setText(strBirthday);
                                        gender.setSelection(adapter_gender.getPosition(strGender));
                                        gender_display.setText(strGender);

                                        icno.setText(strICNO);
                                        bank_name.setText(strBankName);
                                        bank_acc.setText(strBankAcc);

                                        if (strVerify == 0) {
                                            layout_icno.setVisibility(View.GONE);
                                            layout_bankName.setVisibility(View.GONE);
                                            layout_BankAcc.setVisibility(View.GONE);
                                            layout_income.setVisibility(View.GONE);
                                        } else {
                                            layout_icno.setVisibility(View.VISIBLE);
                                            layout_bankName.setVisibility(View.VISIBLE);
                                            layout_BankAcc.setVisibility(View.VISIBLE);
                                            layout_income.setVisibility(View.VISIBLE);
                                        }

                                        gender.setVisibility(View.GONE);
                                        gender_img_spinner.setVisibility(View.GONE);
                                        Picasso.get().load(strPhoto).into(profile_image);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Edit_Profile.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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

        final String strAddress01 = this.address_01.getText().toString().trim();
        final String strAddress02 = this.address_02.getText().toString().trim();
        final String strCity = this.city.getText().toString().trim();
        final String strPostCode = this.postcode.getText().toString().trim();

        final String strBirthday = this.birthday.getText().toString().trim();
        final String strGender = this.gender.getSelectedItem().toString().trim();
        final String strAccNo = this.bank_acc.getText().toString();
        final String strBankName = this.bank_name.getText().toString();

        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(Edit_Profile.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
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
                                    Toast.makeText(Edit_Profile.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Edit_Profile.this, "Failed to read", Toast.LENGTH_SHORT).show();
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
                params.put("name", strName);
                params.put("email", strEmail);
                params.put("phone_no", str_Phone_no);
                params.put("address_01", strAddress01);
                params.put("address_02", strAddress02);
                params.put("division", strCity);
                params.put("postcode", strPostCode);
                params.put("birthday", strBirthday);
                params.put("gender", strGender);
                params.put("bank_acc", strAccNo);
                params.put("bank_name", strBankName);
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

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void UploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
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
                                    Toast.makeText(Edit_Profile.this, "Success!", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Edit_Profile.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(Edit_Profile.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
            UploadPicture(getId, getStringImage(bitmap));
        }

    }

    private void Buying_List() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_READ_ORDER,
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

                                        final String id = object.getString("id").trim();
                                        final String main_category = object.getString("main_category").trim();
                                        final String sub_category = object.getString("sub_category").trim();
                                        final String ad_detail = object.getString("ad_detail").trim();
                                        final double price = Double.parseDouble(object.getString("price").trim());
                                        final String division = object.getString("division");
                                        final String district = object.getString("district");
                                        final String image_item = object.getString("photo");
                                        final String seller_id = object.getString("seller_id");
                                        final String quantity = object.getString("quantity");

                                        grandtotal += (price * Integer.parseInt(quantity));
                                        income.setText("MYR" + String.format("%.2f", grandtotal));

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
                params.put("seller_id", getId);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(Edit_Profile.this);
        requestQueue1.add(stringRequest1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Edit_Profile.this, Me_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
