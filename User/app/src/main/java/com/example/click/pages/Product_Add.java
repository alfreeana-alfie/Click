package com.example.click.pages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.example.click.R;
import com.example.click.data.Item_All_Details_Other;
import com.example.click.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_Add extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/itemsave.php";
    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/products/uploadimg.php";

    SessionManager sessionManager;
    String getId;
    Uri filePath;
    private ArrayAdapter<CharSequence> adapter_division, adapter_district, adapter_category,
            adapter_car, adapter_properties, adapter_elctronic,
            adapter_home, adapter_leisure, adapter_business,
            adapter_jobs, adapter_travel, adapter_other;
    private Bitmap bitmap;
    private TextView enter_category, enter_ad_detail, enter_location, enter_setup;
    private EditText enter_price, edittext_ad_detail, edittext_brand, edittext_inner, edittext_stock, edittext_desc, edittext_order;
    private Button accept_item, accept_category, back_category, accept_ad_detail, back_ad_detail, accept_location, back_location, back_item;
    private Spinner spinner_main_category, spinner_sub_category, spinner_division, spinner_district;
    private RelativeLayout category_page_layout, location_page_layout;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img;
    private ProgressBar loading;
    private ScrollView about_detail;
    List<Item_All_Details_Other> itemList;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_item);
        Declare();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Product_Add.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Product_Add.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Product_Add.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        sessionManager = new SessionManager(Product_Add.this);
        sessionManager.checkLogin();

        getUserId();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

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
                about_detail.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        enter_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLocation();
            }
        });

        accept_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mLocation = spinner_division.getSelectedItem().toString() + ", " + spinner_district.getSelectedItem().toString();
                enter_location.setText(mLocation);
            }
        });

        back_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });


        accept_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath == null || edittext_ad_detail.getText().toString().isEmpty() || enter_price.getText().toString().isEmpty() || edittext_order.getText().toString().isEmpty()) {
                    Toast.makeText(Product_Add.this, "Incomplete information", Toast.LENGTH_LONG).show();
                } else {
                    saveEdit(getId, getStringImage(bitmap));
                }

            }
        });

        back_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(Product_Add.this, Me_Page.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);
            }
        });

        accept_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mCategory = spinner_main_category.getSelectedItem().toString();
                enter_category.setText(mCategory);
            }
        });

        accept_ad_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_detail.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mAd_Detail = edittext_ad_detail.getText().toString();
                enter_ad_detail.setText(mAd_Detail);
            }
        });
    }

    private void Declare() {
        itemList = new ArrayList<>();
        enter_category = findViewById(R.id.enter_main_category);
        enter_ad_detail = findViewById(R.id.enter_ad_detail);
        enter_location = findViewById(R.id.enter_location);
        enter_price = findViewById(R.id.enter_price);
        enter_setup = findViewById(R.id.enter_delivery_location);
        edittext_order = findViewById(R.id.enter_max_order);
        about_detail = findViewById(R.id.about_product);

        edittext_brand = findViewById(R.id.edittext_brand);
        edittext_inner = findViewById(R.id.edittext_inner);
        edittext_stock = findViewById(R.id.edittext_stock);
        edittext_desc = findViewById(R.id.edittext_desc);

        spinner_division = findViewById(R.id.spinner_division);
        spinner_district = findViewById(R.id.spinner_district);
        accept_location = findViewById(R.id.accept_location);
        back_location = findViewById(R.id.back_location);
        location_page_layout = findViewById(R.id.location_page_layout);

        accept_item = findViewById(R.id.accept_item);
        back_item = findViewById(R.id.back_item);
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
//        ad_detail_page_layout = findViewById(R.id.ad_detail_page_layout);
        item_page_layout = findViewById(R.id.item_page_layout);

        adapter_division = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_division, android.R.layout.simple_spinner_item);
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);
        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showLocationResult(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter_category = ArrayAdapter.createFromResource(Product_Add.this, R.array.main_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_main_category.setAdapter(adapter_category);

        spinner_main_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { showResult(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void gotoAdDetail() {
        about_detail.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void gotoLocation() {
        location_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void gotoCategory() {
        category_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void showResult(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                adapter_car = ArrayAdapter.createFromResource(Product_Add.this, R.array.vehicle_category, android.R.layout.simple_spinner_item);
                adapter_car.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_car);
                break;
            case 2:
                adapter_properties = ArrayAdapter.createFromResource(Product_Add.this, R.array.properties_category, android.R.layout.simple_spinner_item);
                adapter_properties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_properties);
                break;
            case 3:
                adapter_elctronic = ArrayAdapter.createFromResource(Product_Add.this, R.array.electronic_category, android.R.layout.simple_spinner_item);
                adapter_elctronic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_elctronic);
                break;
            case 4:
                adapter_home = ArrayAdapter.createFromResource(Product_Add.this, R.array.home_category, android.R.layout.simple_spinner_item);
                adapter_home.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_home);
                break;
            case 5:
                adapter_leisure = ArrayAdapter.createFromResource(Product_Add.this, R.array.leisure_category, android.R.layout.simple_spinner_item);
                adapter_leisure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_leisure);
                break;
            case 6:
                adapter_business = ArrayAdapter.createFromResource(Product_Add.this, R.array.business_category, android.R.layout.simple_spinner_item);
                adapter_business.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_business);
                break;
            case 7:
                adapter_jobs = ArrayAdapter.createFromResource(Product_Add.this, R.array.jobs_category, android.R.layout.simple_spinner_item);
                adapter_jobs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_jobs);
                break;
            case 8:
                adapter_travel = ArrayAdapter.createFromResource(Product_Add.this, R.array.travel_category, android.R.layout.simple_spinner_item);
                adapter_travel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_travel);
                break;
            case 9:
                adapter_other = ArrayAdapter.createFromResource(Product_Add.this, R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
                break;
            case 10:
                adapter_other = ArrayAdapter.createFromResource(Product_Add.this, R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
                break;
        }
    }

    private void showLocationResult(int position) {
        switch (position) {
            case 0:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_kuching, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 1:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_samarahan, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 2:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_serian, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 3:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_sri_aman, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 4:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_betong, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 5:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_sarikei, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 6:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_sibu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 7:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_mukah, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 8:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_bintulu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 9:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_kapit, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 10:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_miri, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 11:
                adapter_district = ArrayAdapter.createFromResource(Product_Add.this, R.array.new_limbang, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;
        }
    }

    private void getUserId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                }
                            } else {
                                Toast.makeText(Product_Add.this, "Failed to read", Toast.LENGTH_SHORT).show();
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
                            //End


                        } catch (Exception e) {


                        }
//                        Toast.makeText(Sell_Items_Other.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Product_Add.this);
        requestQueue.add(stringRequest);
    }

    private void saveEdit(final String id, final String photo) {
        final String strMain_category = this.spinner_main_category.getSelectedItem().toString().trim();
        final String strSub_category = this.spinner_sub_category.getSelectedItem().toString();
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final String strBrand = this.edittext_brand.getText().toString();
        final String strInner = this.edittext_inner.getText().toString();
        final String strStock = this.edittext_stock.getText().toString();
        final String strDesc = this.edittext_desc.getText().toString();

        final Double strPrice = Double.valueOf(this.enter_price.getText().toString().trim());
        final String strOrder = this.edittext_order.getText().toString();
        final String strDivision = this.spinner_division.getSelectedItem().toString().trim();
        final String strDistrict = this.spinner_district.getSelectedItem().toString().trim();

        if (strAd_Detail.isEmpty()) {
            Toast.makeText(Product_Add.this, "Incomplete info", Toast.LENGTH_SHORT).show();
        } else {
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

                                    Item_All_Details_Other item = new Item_All_Details_Other(getId, strMain_category, strSub_category, strAd_Detail, String.format("%.2f", strPrice), strDivision, strDistrict, photo);
                                    itemList.add(item);

                                    Intent intent = new Intent(Product_Add.this, MyProducts.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Product_Add.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    accept_item.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
                                Intent intent1 = new Intent(Product_Add.this, Product_Add.class);
                                startActivity(intent1);
                                Toast.makeText(Product_Add.this, "Please re-enter the item details again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            accept_item.setVisibility(View.VISIBLE);
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
                                //End


                            } catch (Exception e) {


                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", id);
                    params.put("main_category", strMain_category);
                    params.put("sub_category", strSub_category);
                    params.put("ad_detail", strAd_Detail);
                    params.put("brand_material", strBrand);
                    params.put("inner_material", strInner);
                    params.put("stock", strStock);
                    params.put("description", strDesc);
                    params.put("price", String.format("%.2f", strPrice));
                    params.put("max_order", strOrder);
                    params.put("division", strDivision);
                    params.put("district", strDistrict);
                    params.put("photo", photo);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Product_Add.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                upload_photo_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent4 = new Intent(Product_Add.this, Me_Page.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent4);
    }
}
