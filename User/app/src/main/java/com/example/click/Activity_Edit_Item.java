package com.example.click;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.click.Fragment_View_Item_User.EXTRA_AD_DETAIL;
import static com.example.click.Fragment_View_Item_User.EXTRA_ID;
import static com.example.click.Fragment_View_Item_User.EXTRA_IMG_ITEM;
import static com.example.click.Fragment_View_Item_User.EXTRA_ITEM_LOCATION;
import static com.example.click.Fragment_View_Item_User.EXTRA_MAIN;
import static com.example.click.Fragment_View_Item_User.EXTRA_PRICE;
import static com.example.click.Fragment_View_Item_User.EXTRA_SUB;
import static com.example.click.Fragment_View_Item_User.EXTRA_USERID;

public class Activity_Edit_Item extends AppCompatActivity {

    private static String URL_UPLOAD = "https://annkalina53.000webhostapp.com/android_register_login/edituser.php";
    private static String URL_IMG = "https://annkalina53.000webhostapp.com/android_register_login/uploadimg02.php";
    ArrayAdapter<CharSequence> adapter_item_location, adapter_category, adapter_car, adapter_properties, adapter_elctronic, adapter_home, adapter_leisure, adapter_business, adapter_jobs, adapter_travel, adapter_other;

    Uri filePath;
    String id;
    private Bitmap bitmap;
    private TextView enter_main_category, enter_sub_category, enter_ad_detail, enter_category;
    private EditText enter_price, edittext_ad_detail;
    private Button accept_item, accept_category, back_category, accept_ad_detail, back_ad_detail, back_edit, edit_item;
    private Spinner spinner_main_category, spinner_sub_category, spinner_item_location;
    private RelativeLayout category_page_layout, ad_detail_page_layout;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img;
    private ProgressBar loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Declare();

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        final String userid = intent.getStringExtra(EXTRA_USERID);
        final String main_category = intent.getStringExtra(EXTRA_MAIN);
        final String sub_category = intent.getStringExtra(EXTRA_SUB);
        final String ad_detail = intent.getStringExtra(EXTRA_AD_DETAIL);
        final String price = intent.getStringExtra(EXTRA_PRICE);
        final String item_location = intent.getStringExtra(EXTRA_ITEM_LOCATION);

        final String photo = intent.getStringExtra(EXTRA_IMG_ITEM);

        enter_category.setText(main_category + ", " + sub_category);
        enter_main_category.setText(main_category);
        enter_sub_category.setText(sub_category);
        enter_ad_detail.setText(ad_detail);
        edittext_ad_detail.setText(ad_detail);
        enter_price.setText(price);

        int selectposition = adapter_item_location.getPosition(item_location);
        spinner_item_location.setSelection(selectposition);

//        Toast.makeText(Activity_Edit_Item.this, userid, Toast.LENGTH_SHORT).show();

        upload_photo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

            }
        });

        enter_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCategory();
            }
        });

        back_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        enter_ad_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAdDetail();
            }
        });

        back_ad_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_detail_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        Picasso.get().load(photo).into(upload_photo_img);

        back_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Edit_Item.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });

        accept_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mCategory = spinner_main_category.getSelectedItem().toString() + ", " + spinner_sub_category.getSelectedItem().toString();
                enter_main_category.setText(spinner_main_category.getSelectedItem().toString());
                enter_sub_category.setText(spinner_sub_category.getSelectedItem().toString());
                enter_category.setText(mCategory);
            }
        });

        accept_ad_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_detail_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mAd_Detail = edittext_ad_detail.getText().toString();
                enter_ad_detail.setText(mAd_Detail);
            }
        });
    }

    private void gotoAdDetail() {
        ad_detail_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void gotoCategory() {
        category_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

//    private void getUserId() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("read");
//
//                            if (success.equals("1")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                }
//                            } else {
//                                Toast.makeText(Activity_Edit_Item.this, "Incorrect Informations", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(Activity_Edit_Item.this, "Error!!" + e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Activity_Edit_Item.this, "Error!" + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", getId);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                upload_photo_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage(getStringImage(bitmap));
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    /*private void saveEditNo() {

        final String strMain_category = this.spinner_main_category.getSelectedItem().toString().trim();
        final String strSub_category = this.spinner_sub_category.getSelectedItem().toString().trim();
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final String strPrice = this.enter_price.getText().toString().trim();
        final String strItem_location = this.spinner_item_location.getSelectedItem().toString().trim();
        final String strUser_ID = id;

        loading.setVisibility(View.VISIBLE);
        accept_item.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
                                Toast.makeText(Activity_Edit_Item.this, "Success!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Activity_Edit_Item.this, "Registration Failed! ", Toast.LENGTH_SHORT).show();

                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            accept_item.setVisibility(View.VISIBLE);
                            Toast.makeText(Activity_Edit_Item.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        accept_item.setVisibility(View.VISIBLE);
                        Toast.makeText(Activity_Edit_Item.this, "Connection Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("user", strUser_ID);
                params.put("main_category", strMain_category);
                params.put("sub_category", strSub_category);
                params.put("ad_detail", strAd_Detail);
                params.put("price", strPrice);
                params.put("item_location", strItem_location);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

    private void saveImage(final String photo) {

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        final String strMain_category = this.enter_main_category.toString();
        final String strSub_category = this.spinner_sub_category.getSelectedItem().toString();
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final String strPrice = this.enter_price.getText().toString().trim();
        final String strItem_location = this.spinner_item_location.getSelectedItem().toString().trim();

        loading.setVisibility(View.VISIBLE);
        edit_item.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IMG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                edit_item.setVisibility(View.VISIBLE);
//                                    Toast.makeText(Activity_Edit_Item.this, "Success!", Toast.LENGTH_SHORT).show();

                            } else {
//                                    Toast.makeText(Activity_Edit_Item.this, "Registration Failed! ", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                edit_item.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            edit_item.setVisibility(View.VISIBLE);
                            Toast.makeText(Activity_Edit_Item.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        edit_item.setVisibility(View.VISIBLE);
                        Toast.makeText(Activity_Edit_Item.this, "Connection Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", strAd_Detail);
                params.put("photo", photo);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveEdit() {

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        final String strMain_category = this.enter_main_category.getText().toString().trim();
        final String strSub_category = this.enter_sub_category.getText().toString().trim();
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final String strPrice = this.enter_price.getText().toString().trim();
        final String strItem_location = this.spinner_item_location.getSelectedItem().toString().trim();

        loading.setVisibility(View.VISIBLE);
        edit_item.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    loading.setVisibility(View.GONE);
                                    edit_item.setVisibility(View.VISIBLE);

                                    Intent intent1 = new Intent(Activity_Edit_Item.this, Activity_Home.class);
                                    startActivity(intent1);

                                } else {
//                                    Toast.makeText(Activity_Edit_Item.this, "Registration Failed! ", Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                    edit_item.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                edit_item.setVisibility(View.VISIBLE);
                                Toast.makeText(Activity_Edit_Item.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            edit_item.setVisibility(View.VISIBLE);
                            Toast.makeText(Activity_Edit_Item.this, "Connection Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("main_category", strMain_category);
                    params.put("sub_category", strSub_category);
                    params.put("ad_detail", strAd_Detail);
                    params.put("price", strPrice);
                    params.put("item_location", strItem_location);
                    params.put("id", id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    private void showResult(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                adapter_car = ArrayAdapter.createFromResource(this, R.array.vehicle_category, android.R.layout.simple_spinner_item);
                adapter_car.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_car);
                break;
            case 2:
                adapter_properties = ArrayAdapter.createFromResource(this, R.array.properties_category, android.R.layout.simple_spinner_item);
                adapter_properties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_properties);
                break;
            case 3:
                adapter_elctronic = ArrayAdapter.createFromResource(this, R.array.electronic_category, android.R.layout.simple_spinner_item);
                adapter_elctronic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_elctronic);
                break;
            case 4:
                adapter_home = ArrayAdapter.createFromResource(this, R.array.home_category, android.R.layout.simple_spinner_item);
                adapter_home.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_home);
                break;
            case 5:
                adapter_leisure = ArrayAdapter.createFromResource(this, R.array.leisure_category, android.R.layout.simple_spinner_item);
                adapter_leisure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_leisure);
                break;
            case 6:
                adapter_business = ArrayAdapter.createFromResource(this, R.array.business_category, android.R.layout.simple_spinner_item);
                adapter_business.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_business);
                break;
            case 7:
                adapter_jobs = ArrayAdapter.createFromResource(this, R.array.jobs_category, android.R.layout.simple_spinner_item);
                adapter_jobs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_jobs);
                break;
            case 8:
                adapter_travel = ArrayAdapter.createFromResource(this, R.array.travel_category, android.R.layout.simple_spinner_item);
                adapter_travel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_travel);
                break;
            case 9:
                adapter_other = ArrayAdapter.createFromResource(this, R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
                break;
        }
    }
    
    private void Declare(){
        enter_main_category = findViewById(R.id.enter_main_category);
        enter_sub_category = findViewById(R.id.enter_sub_category);
        enter_category = findViewById(R.id.enter_category);
        enter_ad_detail = findViewById(R.id.enter_ad_detail);
        enter_price = findViewById(R.id.enter_price);
        spinner_item_location = findViewById(R.id.spinner_item_location);
        accept_item = findViewById(R.id.accept_item);
        edittext_ad_detail = findViewById(R.id.edittext_ad_detail);
        accept_ad_detail = findViewById(R.id.accept_ad_detail);
        back_ad_detail = findViewById(R.id.back_ad_detail);
        spinner_main_category = findViewById(R.id.spinner_main_category);
        spinner_sub_category = findViewById(R.id.spinner_sub_category);
        accept_category = findViewById(R.id.accept_category);
        back_category = findViewById(R.id.back_category);
        upload_photo_img = findViewById(R.id.upload_photo);
        loading = findViewById(R.id.loading);
        category_page_layout = findViewById(R.id.category_page_layout);
        ad_detail_page_layout = findViewById(R.id.ad_detail_page_layout);
        item_page_layout = findViewById(R.id.item_page_layout);
        back_edit = findViewById(R.id.back_edit);
        edit_item = findViewById(R.id.button_edit_item);

        adapter_item_location = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adapter_item_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_item_location.setAdapter(adapter_item_location);

        adapter_category = ArrayAdapter.createFromResource(this, R.array.main_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_main_category.setAdapter(adapter_category);

        spinner_main_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showResult(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
