package com.ketekmall.ketekmall.pages.seller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Product_Add_Data;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Product_Add extends AppCompatActivity {

    private static String URL_READ = "http://hawkingnight.com/ketekmall/itemsave.php";
    private static String URL_DELETE_PHOTO = "http://hawkingnight.com/ketekmall/products_img/delete_photo.php";

    private static String URL_ADD_TEMP = "http://hawkingnight.com/ketekmall/products/add_temp.php";
    private static String URL_DELETE_TEMP = "http://hawkingnight.com/ketekmall/products/delete_temp.php";
    private static String URL_DELETE_DB_TEMP = "http://hawkingnight.com/ketekmall/products/delete_db_temp.php";
    private static String URL_ADD = "http://hawkingnight.com/ketekmall/products/add.php";

    SessionManager sessionManager;
    String getId;
    Uri filePath1,filePath2,filePath3,filePath4,filePath5;
    private ArrayAdapter<CharSequence> adapter_division, adapter_district, adapter_category;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;
    private TextView enter_category, enter_ad_detail, enter_location, enter_setup;
    private EditText enter_price, edittext_ad_detail, edittext_brand, edittext_inner, edittext_stock, edittext_desc, edittext_order, edittext_postcode, edittext_weight;
    private Button accept_item, accept_category, back_category, accept_ad_detail, back_ad_detail, accept_location, back_location, back_item;
    private Spinner spinner_main_category, spinner_sub_category, spinner_division, spinner_district;
    private RelativeLayout category_page_layout, location_page_layout;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img1,upload_photo_img2,upload_photo_img3,upload_photo_img4,upload_photo_img5;
    private ImageView delete_2,delete_3,delete_4,delete_5;
    private ProgressBar loading;
    private ScrollView about_detail;
    List<Product_Add_Data> itemList;
    BottomNavigationView bottomNav;
    RelativeLayout parent;

    List<String> photoTempId = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
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

        upload_photo_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile1();
            }
        });

        upload_photo_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile2();
            }
        });

        upload_photo_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile3();
            }
        });

        upload_photo_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile4();
            }
        });

        upload_photo_img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile5();
            }
        });

        delete_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black_foreground));
                delete_2.setVisibility(View.GONE);
                deletePhoto("2");
                deleteTemp(1);
            }
        });

        delete_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black_foreground));
                delete_3.setVisibility(View.GONE);
                deletePhoto("3");
                deleteTemp(2);
            }
        });

        delete_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black_foreground));
                delete_4.setVisibility(View.GONE);
                deletePhoto("4");
                deleteTemp(3);
            }
        });

        delete_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black_foreground));
                delete_5.setVisibility(View.GONE);
                deletePhoto("5");
                deleteTemp(4);
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
                if (filePath1 == null || edittext_ad_detail.getText().toString().isEmpty() || enter_price.getText().toString().isEmpty() || edittext_order.getText().toString().isEmpty()) {
                    Toast.makeText(Product_Add.this, "Incomplete information", Toast.LENGTH_LONG).show();
                } else {
//                    saveEdit(getId, getStringImage(bitmap1));
                    add(getId);

                    for(int i = 0; i<photoTempId.size(); i++){
                        deleteDbTemp(i);
                    }

                    new Timer().schedule(
                            new TimerTask(){

                                @Override
                                public void run(){
                                    Intent intent = new Intent(Product_Add.this, MyProducts.class);
                                    startActivity(intent);
                                }

                            }, 3000);


                }

            }
        });

        back_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< photoTempId.size(); i++){
                    deleteAllTemp(i);
                    Log.i("ARRAY", String.valueOf(i));
                }
                new Timer().schedule(
                        new TimerTask(){

                            @Override
                            public void run(){
                                Intent intent4 = new Intent(Product_Add.this, Me_Page.class);
                                startActivity(intent4);
                                finish();
                            }

                        }, 2000);
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
        parent = findViewById(R.id.parent);
        setupUI(parent);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Product_Add.this);
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    private void Declare() {
        itemList = new ArrayList<>();
        enter_category = findViewById(R.id.enter_main_category);
        enter_ad_detail = findViewById(R.id.enter_ad_detail);
        enter_location = findViewById(R.id.enter_location);
        enter_price = findViewById(R.id.enter_price);
        enter_setup = findViewById(R.id.enter_delivery_location);
        edittext_postcode = findViewById(R.id.enter_postcode);
        edittext_order = findViewById(R.id.enter_max_order);
        about_detail = findViewById(R.id.about_product);
        edittext_weight = findViewById(R.id.enter_weight);

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
        loading = findViewById(R.id.loading);

        category_page_layout = findViewById(R.id.category_page_layout);
        item_page_layout = findViewById(R.id.item_page_layout);

        upload_photo_img1 = findViewById(R.id.upload_photo1);
        upload_photo_img2 = findViewById(R.id.upload_photo2);
        upload_photo_img3 = findViewById(R.id.upload_photo3);
        upload_photo_img4 = findViewById(R.id.upload_photo4);
        upload_photo_img5 = findViewById(R.id.upload_photo5);
        delete_2 = findViewById(R.id.delete_2);
        delete_3 = findViewById(R.id.delete_3);
        delete_4 = findViewById(R.id.delete_4);
        delete_5 = findViewById(R.id.delete_5);

        delete_2.setVisibility(View.GONE);
        delete_3.setVisibility(View.GONE);
        delete_4.setVisibility(View.GONE);
        delete_5.setVisibility(View.GONE);

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
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

    private void chooseFile1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void chooseFile2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    private void chooseFile3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
    }

    private void chooseFile4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);
    }

    private void chooseFile5() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
    }

    private void deletePhoto(final String number){
        Intent intent = getIntent();
        final String ad_detail = intent.getStringExtra("ad_detail");

        final String Filename = ad_detail + number;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
//                                Toast.makeText(Product_Add.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Add.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                            e.printStackTrace();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filename", Filename);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            if(photoTempId.isEmpty()){
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth(), bitmap1.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap1.getWidth() > bitmap1.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
                        upload_photo_img1.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img1.setImageBitmap(bitmap1);
                    }
                    addTemp(getStringImage(bitmap1),0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                if(deleteTemp(0)){
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                        bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth(), bitmap1.getHeight(), true);
                        Matrix matrix = new Matrix();

                        if(bitmap1.getWidth() > bitmap1.getHeight()) {
                            matrix.postRotate(360);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
                            upload_photo_img1.setImageBitmap(rotatedBitmap);
                        }else{
                            upload_photo_img1.setImageBitmap(bitmap1);
                        }
                        addTemp(getStringImage(bitmap1),0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            if(photoTempId.isEmpty()){
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap2.getWidth() > bitmap2.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                        upload_photo_img2.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img2.setImageBitmap(bitmap2);
                    }
                    addTemp(getStringImage(bitmap2),1);
                    delete_2.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(photoTempId.size() == 1){
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap2.getWidth() > bitmap2.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                        upload_photo_img2.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img2.setImageBitmap(bitmap2);
                    }
                    addTemp(getStringImage(bitmap2),1);
                    delete_2.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                if(deleteTemp(1)){
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                        bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
                        Matrix matrix = new Matrix();

                        if(bitmap2.getWidth() > bitmap2.getHeight()) {
                            matrix.postRotate(360);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                            upload_photo_img2.setImageBitmap(rotatedBitmap);
                        }else{
                            upload_photo_img2.setImageBitmap(bitmap2);
                        }
                        addTemp(getStringImage(bitmap2),1);
                        delete_2.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            if(photoTempId.isEmpty()){
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    bitmap3 = Bitmap.createScaledBitmap(bitmap3, bitmap3.getWidth(), bitmap3.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap3.getWidth() > bitmap3.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
                        upload_photo_img3.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img3.setImageBitmap(bitmap3);
                    }
                    addTemp(getStringImage(bitmap3),2);
                    delete_3.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(photoTempId.size() == 2){
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    bitmap3 = Bitmap.createScaledBitmap(bitmap3, bitmap3.getWidth(), bitmap3.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap3.getWidth() > bitmap3.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
                        upload_photo_img3.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img3.setImageBitmap(bitmap3);
                    }
                    addTemp(getStringImage(bitmap3),2);
                    delete_3.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                if(deleteTemp(2)){
                    try {
                        bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                        bitmap3 = Bitmap.createScaledBitmap(bitmap3, bitmap3.getWidth(), bitmap3.getHeight(), true);
                        Matrix matrix = new Matrix();

                        if(bitmap3.getWidth() > bitmap3.getHeight()) {
                            matrix.postRotate(360);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
                            upload_photo_img3.setImageBitmap(rotatedBitmap);
                        }else{
                            upload_photo_img3.setImageBitmap(bitmap3);
                        }
                        addTemp(getStringImage(bitmap3),2);
                        delete_3.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath4 = data.getData();
            if(photoTempId.isEmpty()){
                try {
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                    bitmap4 = Bitmap.createScaledBitmap(bitmap4, bitmap4.getWidth(), bitmap4.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap4.getWidth() > bitmap4.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
                        upload_photo_img4.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img4.setImageBitmap(bitmap4);
                    }
                    addTemp(getStringImage(bitmap4),3);
                    delete_4.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (photoTempId.size() == 3){
                try {
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                    bitmap4 = Bitmap.createScaledBitmap(bitmap4, bitmap4.getWidth(), bitmap4.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap4.getWidth() > bitmap4.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
                        upload_photo_img4.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img4.setImageBitmap(bitmap4);
                    }
                    addTemp(getStringImage(bitmap4),3);
                    delete_4.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                if(deleteTemp(3)){
                    try {
                        bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                        bitmap4 = Bitmap.createScaledBitmap(bitmap4, bitmap4.getWidth(), bitmap4.getHeight(), true);
                        Matrix matrix = new Matrix();

                        if(bitmap4.getWidth() > bitmap4.getHeight()) {
                            matrix.postRotate(360);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
                            upload_photo_img4.setImageBitmap(rotatedBitmap);
                        }else{
                            upload_photo_img4.setImageBitmap(bitmap4);
                        }
                        addTemp(getStringImage(bitmap4),3);
                        delete_4.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath5 = data.getData();
            if(photoTempId.isEmpty()){
                try {
                    bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath5);
                    bitmap5 = Bitmap.createScaledBitmap(bitmap5, bitmap5.getWidth(), bitmap5.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap5.getWidth() > bitmap5.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true);
                        upload_photo_img5.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img5.setImageBitmap(bitmap5);
                    }
                    addTemp(getStringImage(bitmap5),4);
                    delete_5.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(photoTempId.size() == 4){
                try {
                    bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath5);
                    bitmap5 = Bitmap.createScaledBitmap(bitmap5, bitmap5.getWidth(), bitmap5.getHeight(), true);
                    Matrix matrix = new Matrix();

                    if(bitmap5.getWidth() > bitmap5.getHeight()) {
                        matrix.postRotate(360);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true);
                        upload_photo_img5.setImageBitmap(rotatedBitmap);
                    }else{
                        upload_photo_img5.setImageBitmap(bitmap5);
                    }
                    addTemp(getStringImage(bitmap5),4);
                    delete_5.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                if(deleteTemp(4)){
                    try {
                        bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath5);
                        bitmap5 = Bitmap.createScaledBitmap(bitmap5, bitmap5.getWidth(), bitmap5.getHeight(), true);
                        Matrix matrix = new Matrix();

                        if(bitmap5.getWidth() > bitmap5.getHeight()) {
                            matrix.postRotate(360);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true);
                            upload_photo_img5.setImageBitmap(rotatedBitmap);
                        }else{
                            upload_photo_img5.setImageBitmap(bitmap5);
                        }
                        addTemp(getStringImage(bitmap5),4);
                        delete_5.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        for(int i = 0; i<photoTempId.size(); i++){
            deleteAllTemp(i);
            Log.i("ARRAY", String.valueOf(i));
        }
        super.onBackPressed();
        finish();
    }

    // New Function - 21st August 2021
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String random() {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(20);
        for(int i = 0; i< 20; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void addTemp(final String filename, int count) {
        final String filename_temp = random();
        photoTempId.add(count, filename_temp);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Add.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Add.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filename_temp", filename_temp);
                params.put("photo", filename);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Log.i("ARRAY", photoTempId.toString());
        Log.i("ARRAY", String.valueOf(photoTempId.size()));
    }

    private boolean deleteTemp(int count){
        final String photoId = photoTempId.get(count);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Add.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Add.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                            e.printStackTrace();


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", photoId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        photoTempId.remove(count);
        return true;
    }

    private void deleteAllTemp(int count){
        final String photoId = photoTempId.get(count);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Add.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Add.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                            e.printStackTrace();


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", photoId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void deleteDbTemp(int count){
        final String photoId = photoTempId.get(count);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_DB_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Add.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Add.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                            e.printStackTrace();


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", photoId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void add(final String id){
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final String strMain_category = this.spinner_main_category.getSelectedItem().toString().trim();
        final String strBrand = this.edittext_brand.getText().toString();
        final String strStock = this.edittext_stock.getText().toString();
        final String strDesc = this.edittext_desc.getText().toString();

        final Double strPrice = Double.valueOf(this.enter_price.getText().toString().trim());
        final String strOrder = this.edittext_order.getText().toString();
        final String strDivision = this.spinner_division.getSelectedItem().toString().trim();
        final String strPostcode = this.edittext_postcode.getText().toString();
        final String strDistrict = this.spinner_district.getSelectedItem().toString().trim();
        final String strWeight = this.edittext_weight.getText().toString();

        // Photos
        if (strAd_Detail.isEmpty()) {
            Toast.makeText(Product_Add.this, "Incomplete info", Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            accept_item.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    loading.setVisibility(View.GONE);
                                    accept_item.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(Product_Add.this, R.string.failed, Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    accept_item.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
                                Intent intent1 = new Intent(Product_Add.this, Product_Add.class);
                                startActivity(intent1);
                                Toast.makeText(Product_Add.this, R.string.please_re_enter_the_item_again, Toast.LENGTH_SHORT).show();
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
                                e.printStackTrace();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", id);
                    params.put("main_category", strMain_category);
                    params.put("sub_category", strMain_category);
                    params.put("ad_detail", strAd_Detail);
                    params.put("brand_material", strBrand);
                    params.put("inner_material", strBrand);
                    params.put("stock", strStock);
                    params.put("description", strDesc);
                    params.put("price", String.format("%.2f", strPrice));
                    params.put("max_order", strOrder);
                    params.put("division", strDivision);
                    params.put("postcode", strPostcode);
                    params.put("district", strDistrict);
                    params.put("photo", photoTempId.get(0));
                    for(int i = 1; i< photoTempId.size(); i++){
                        int num = i+1;

                        params.put("photo0" + num, photoTempId.get(i));
                    }
                    params.put("weight", strWeight);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Product_Add.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }
}