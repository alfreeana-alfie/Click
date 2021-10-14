package com.ketekmall.ketekmall.pages.seller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import com.ketekmall.ketekmall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class Product_Edit extends AppCompatActivity {

    private static String UPDATE = "https://ketekmall.com/ketekmall/products/update.php";

    private static String URL_DELETE_PHOTO = "https://ketekmall.com/ketekmall/products_img/delete_photo.php";
    private static String URL_IMG = "https://ketekmall.com/ketekmall/products/uploadimg02.php";
    private static String URL_UPLOAD_EXTRA = "https://ketekmall.com/ketekmall/products_img/uploadimg03.php";
    private static String URL_READ_PHOTO = "https://ketekmall.com/ketekmall/products_img/read_photo.php";
    private static String URL_EDIT_PROD = "https://ketekmall.com/ketekmall/edit_product_detail.php";

    private static String URL_ADD_TEMP = "https://ketekmall.com/ketekmall/products/add_temp.php";
    private static String URL_DELETE_TEMP = "https://ketekmall.com/ketekmall/products/delete_temp.php";
    private static String URL_DELETE_DB_TEMP = "https://ketekmall.com/ketekmall/products/delete_db_temp.php";
    
    ArrayAdapter<CharSequence> adapter_division, adapter_district, adapter_category;
    Uri filePath1,filePath2,filePath3,filePath4,filePath5;
    String strID, sub_category, district;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;
    private TextView Main_Category_TextView, Sub_Category_TextView, Ad_Detail_TextView,
            Category_TextView, Location_TextView, Division_TextView, District_TextView;
    private EditText EditText_Price, EditText_Ad_Detail, edittext_brand, edittext_inner, edittext_stock, edittext_desc, Edittext_Order, Edittext_Postcode, editText_weight;
    private Button Button_AcceptCategory, Button_BackCategory,
            Button_AcceptAdDetail, Button_BackAdDetail, Button_BackEdit,
            Button_SavedEdit, Button_AcceptLocation, Button_BackLocation;
    private Spinner spinner_main_category, spinner_sub_category, spinner_division, spinner_district;
    private RelativeLayout category_page_layout, location_page_layout, parent;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img1,upload_photo_img2,upload_photo_img3,upload_photo_img4,upload_photo_img5;
    private ImageView delete_2,delete_3,delete_4,delete_5;
    private ScrollView about_detail;
    private ProgressBar loading;
    BottomNavigationView bottomNav;
    
    List<String> photoUrlLink = new ArrayList<String>();
    List<String> photoTempId = new ArrayList<String>();
    String photo02, photo03, photo04, photo05;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);
        Declare();

        final Intent intent = getIntent();

        strID = intent.getStringExtra("id");
        final String main_category = intent.getStringExtra("main_category");
        sub_category = intent.getStringExtra("sub_category");
        final String ad_detail = intent.getStringExtra("ad_detail");
        final String price = intent.getStringExtra("price");
        final String division = intent.getStringExtra("division");
        final String postcode = intent.getStringExtra("postcode");
        district = intent.getStringExtra("district");
        final String photo = intent.getStringExtra("photo");
        photo02 = intent.getStringExtra("photo02");
        photo03 = intent.getStringExtra("photo03");
        photo04 = intent.getStringExtra("photo04");
        photo05 = intent.getStringExtra("photo05");
        String Location_Text = division + ", " + district;
        final String strMax_Order = intent.getStringExtra("max_order");
        final String strWeight = intent.getStringExtra("weight");

        final String brand = intent.getStringExtra("brand_material");
        final String inner = intent.getStringExtra("inner_material");
        final String stock = intent.getStringExtra("stock");
        final String desc = intent.getStringExtra("description");

        // List of photos
        if(!photo.equals("null") && !photo.contains("https://ketekmall.com/ketekmall/products/.jpg")){
            photoUrlLink.add(photo);
            final String[] getPhotoId = photo.split("https://ketekmall\\.com/ketekmall/products/");
            final String[] noExtension = getPhotoId[1].split("\\.");
            photoTempId.add(noExtension[0]);
        }
        if(!photo02.equals("null") && !photo02.contains("https://ketekmall.com/ketekmall/products/.jpg")){
            photoUrlLink.add(photo02);
            final String[] getPhotoId = photo02.split("https://ketekmall\\.com/ketekmall/products/");
            final String[] noExtension = getPhotoId[1].split("\\.");
            photoTempId.add(noExtension[0]);
        }
        if(!photo03.equals("null") && !photo03.contains("https://ketekmall.com/ketekmall/products/.jpg")){
            photoUrlLink.add(photo03);
            final String[] getPhotoId = photo03.split("https://ketekmall\\.com/ketekmall/products/");
            final String[] noExtension = getPhotoId[1].split("\\.");
            photoTempId.add(noExtension[0]);
        }
        if(!photo04.equals("null") && !photo04.contains("https://ketekmall.com/ketekmall/products/.jpg")){
            photoUrlLink.add(photo04);
            final String[] getPhotoId = photo04.split("https://ketekmall\\.com/ketekmall/products/");
            final String[] noExtension = getPhotoId[1].split("\\.");
            photoTempId.add(noExtension[0]);
        }
        if(!photo05.equals("null") && !photo05.contains("https://ketekmall.com/ketekmall/products/.jpg")){
            photoUrlLink.add(photo05);
            final String[] getPhotoId = photo05.split("https://ketekmall\\.com/ketekmall/products/");
            final String[] noExtension = getPhotoId[1].split("\\.");
            photoTempId.add(noExtension[0]);
        }

        Log.i("PHOTO LIST", String.valueOf(photoUrlLink.size()));
        Log.i("PHOTO", photoUrlLink.toString());
        Log.i("PHOTO", photoTempId.toString());

        saveItemID();
        ViewPhoto();

        Edittext_Order.setText(strMax_Order);

        Category_TextView.setText(main_category);

        if (main_category != null) {
            int main_catposition = adapter_category.getPosition(main_category);
            spinner_main_category.setSelection(main_catposition);

        }

        if (division != null) {
            int division_position = adapter_division.getPosition(division);
            spinner_division.setSelection(division_position);
        }

        Edittext_Postcode.setText(postcode);
        Main_Category_TextView.setText(main_category);
        Sub_Category_TextView.setText(sub_category);
        Ad_Detail_TextView.setText(ad_detail);
        Location_TextView.setText(Location_Text);
        Division_TextView.setText(division);
        District_TextView.setText(district);
        EditText_Ad_Detail.setText(ad_detail);
        EditText_Price.setText(price);
        editText_weight.setText(strWeight);
        Picasso.get().load(photo).into(upload_photo_img1);

        edittext_brand.setText(brand);
        edittext_inner.setText(inner);
        edittext_stock.setText(stock);
        edittext_desc.setText(desc);

        Button_Func();
    }

    private void Declare() {
        parent = findViewById(R.id.parent);
        Main_Category_TextView = findViewById(R.id.enter_main_category);
        Sub_Category_TextView = findViewById(R.id.enter_sub_category);
        Category_TextView = findViewById(R.id.enter_category);
        Ad_Detail_TextView = findViewById(R.id.enter_ad_detail);
        Location_TextView = findViewById(R.id.enter_location);
        Division_TextView = findViewById(R.id.division_TextView);
        District_TextView = findViewById(R.id.district_TextView);
        editText_weight = findViewById(R.id.enter_weight);

        about_detail = findViewById(R.id.about_product);

        Edittext_Order = findViewById(R.id.enter_max_order);
        Edittext_Postcode = findViewById(R.id.enter_postcode);

        edittext_brand = findViewById(R.id.edittext_brand);
        edittext_inner = findViewById(R.id.edittext_inner);
        edittext_stock = findViewById(R.id.edittext_stock);
        edittext_desc = findViewById(R.id.edittext_desc);

        EditText_Price = findViewById(R.id.enter_price);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_district = findViewById(R.id.spinner_district);

        EditText_Ad_Detail = findViewById(R.id.edittext_ad_detail);
        Button_AcceptAdDetail = findViewById(R.id.accept_ad_detail);
        Button_BackAdDetail = findViewById(R.id.back_ad_detail);

        spinner_main_category = findViewById(R.id.spinner_main_category);
        spinner_sub_category = findViewById(R.id.spinner_sub_category);

        Button_AcceptCategory = findViewById(R.id.accept_category);
        Button_BackCategory = findViewById(R.id.back_category);

        Button_AcceptLocation = findViewById(R.id.accept_location);
        Button_BackLocation = findViewById(R.id.back_location);

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

        loading = findViewById(R.id.loading);
        category_page_layout = findViewById(R.id.category_page_layout);
        location_page_layout = findViewById(R.id.location_page_layout);
        item_page_layout = findViewById(R.id.item_page_layout);

        Button_BackEdit = findViewById(R.id.back_edit);
        Button_SavedEdit = findViewById(R.id.button_edit_item);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Product_Edit.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Product_Edit.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Product_Edit.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        adapter_division = ArrayAdapter.createFromResource(this, R.array.new_division, android.R.layout.simple_spinner_item);
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

        adapter_category = ArrayAdapter.createFromResource(this, R.array.main_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_main_category.setAdapter(adapter_category);

//        setupUI(parent);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Product_Edit.this);
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

    private void Button_Func() {
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
                upload_photo_img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_2.setVisibility(View.GONE);
                deletePhoto("2");
                deleteTemp(1);
            }
        });

        delete_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_3.setVisibility(View.GONE);
                deletePhoto("3");
                deleteTemp(2);
            }
        });

        delete_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_4.setVisibility(View.GONE);
                deletePhoto("4");
                deleteTemp(3);
            }
        });

        delete_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_5.setVisibility(View.GONE);
                deletePhoto("5");
                deleteTemp(4);
            }
        });

        Category_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCategory();
            }
        });

        Button_BackCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        Ad_Detail_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAdDetail();
            }
        });

        Button_BackAdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_detail.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        Button_BackLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);
            }
        });

        Location_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLocation();
            }
        });

        Button_BackEdit.setOnClickListener(new View.OnClickListener() {
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
                                Intent intent = new Intent(Product_Edit.this, MyProducts.class);
                                startActivity(intent);
                                finish();
                            }

                        }, 2000);
            }
        });

        Button_SavedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();

                for(int i = 0; i< photoTempId.size(); i++){
                    deleteDbTemp(i);
                }

                new Timer().schedule(
                        new TimerTask(){

                            @Override
                            public void run(){
                                Intent intent1 = new Intent(Product_Edit.this, MyProducts.class);
                                startActivity(intent1);
                                finish();
                            }

                        }, 3000);
            }
        });

        Button_AcceptCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mCategory = spinner_main_category.getSelectedItem().toString();
                Category_TextView.setText(mCategory);
            }
        });

        Button_AcceptLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mLocation = spinner_division.getSelectedItem().toString() + ", " + spinner_district.getSelectedItem().toString();
                Division_TextView.setText(spinner_division.getSelectedItem().toString());
                District_TextView.setText(spinner_district.getSelectedItem().toString());
                Location_TextView.setText(mLocation);
            }
        });


        Button_AcceptAdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_detail.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mAd_Detail = EditText_Ad_Detail.getText().toString();
                Ad_Detail_TextView.setText(mAd_Detail);
            }
        });
    }

    private void gotoLocation() {
        location_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void gotoAdDetail() {
        about_detail.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void gotoCategory() {
        category_page_layout.setVisibility(View.VISIBLE);
        item_page_layout.setVisibility(View.GONE);
    }

    private void showLocationResult(int position) {
        switch (position) {
            case 0:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_kuching, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 1:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_samarahan, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 2:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_serian, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 3:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_sri_aman, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 4:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_betong, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 5:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_sarikei, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 6:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_sibu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 7:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_mukah, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 8:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_bintulu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 9:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_kapit, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 10:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_miri, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 11:
                adapter_district = ArrayAdapter.createFromResource(Product_Edit.this, R.array.new_limbang, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;
        }
    }

    private void saveEdit() {
        final Intent intent = getIntent();
        strID = intent.getStringExtra("id");
        final String strMain_category = this.spinner_main_category.getSelectedItem().toString();
        final String strAd_Detail = this.EditText_Ad_Detail.getText().toString();
        final String strBrand = this.edittext_brand.getText().toString();
        final String strInner = this.edittext_inner.getText().toString();
        final String strStock = this.edittext_stock.getText().toString();
        final String strDesc = this.edittext_desc.getText().toString();
        final String strWeight = this.editText_weight.getText().toString();

        final Double strPrice = Double.valueOf(this.EditText_Price.getText().toString().trim());
        @SuppressLint("DefaultLocale")
        final String strPrice_Text = String.format("%.2f", strPrice);
        final String strOrder = this.Edittext_Order.getText().toString();
        final String strDivision = this.Division_TextView.getText().toString().trim();
        final String strPostcode = this.Edittext_Postcode.getText().toString().trim();
        final String strDistrict = this.District_TextView.getText().toString().trim();


        loading.setVisibility(View.VISIBLE);
        Button_SavedEdit.setVisibility(View.GONE);

        Button_BackEdit.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    loading.setVisibility(View.GONE);
                                    Button_SavedEdit.setVisibility(View.VISIBLE);
                                    Button_BackEdit.setVisibility(VISIBLE);
                                    Toast.makeText(Product_Edit.this, R.string.success_update, Toast.LENGTH_SHORT).show();
                                } else {
                                    loading.setVisibility(View.GONE);
                                    Button_SavedEdit.setVisibility(View.VISIBLE);
                                    Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                loading.setVisibility(View.GONE);
                                Button_SavedEdit.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        Button_SavedEdit.setVisibility(View.VISIBLE);
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("main_category", strMain_category);
                params.put("sub_category", strMain_category);
                params.put("ad_detail", strAd_Detail);
                params.put("brand_material", strBrand);
                params.put("inner_material", strBrand);
                params.put("stock", strStock);
                params.put("description", strDesc);

                params.put("price", strPrice_Text);
                params.put("max_order", strOrder);
                params.put("division", strDivision);
                params.put("postcode", strPostcode);
                params.put("district", strDistrict);
                params.put("id", strID);
                params.put("item_id", strID);
                params.put("photo", photoTempId.get(0));
                for(int i = 1; i< photoTempId.size(); i++){
                    int num = i+1;
                    Log.i("PHOTO", String.valueOf(num));
                    params.put("photo0" + num, photoTempId.get(i));
                }
                params.put("weight", strWeight);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void saveItemID() {
        Intent intent = getIntent();
        strID = intent.getStringExtra("id");
        final String ad_detail = intent.getStringExtra("ad_detail");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Log.d("Message", "Return SUCCESS");
                                } else {
                                    Log.e("Message", "Return FAILED");
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        Button_SavedEdit.setVisibility(View.VISIBLE);
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
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", ad_detail);
                params.put("item_id", strID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void ViewPhoto(){
        delete_2.setVisibility(View.GONE);
        delete_3.setVisibility(View.GONE);
        delete_4.setVisibility(View.GONE);
        delete_5.setVisibility(View.GONE);

        Log.d("PHOTO", String.valueOf(photoUrlLink.size()));
        if(photoUrlLink.size() == 2){
            Picasso.get().load(photoUrlLink.get(1)).into(upload_photo_img2);
            upload_photo_img3.setVisibility(VISIBLE);
            upload_photo_img4.setVisibility(VISIBLE);
            upload_photo_img5.setVisibility(VISIBLE);

            delete_2.setVisibility(View.VISIBLE);
            delete_3.setVisibility(View.GONE);
            delete_4.setVisibility(View.GONE);
            delete_5.setVisibility(View.GONE);
        } else if(photoUrlLink.size() == 3) {
            Picasso.get().load(photoUrlLink.get(1)).into(upload_photo_img2);
            Picasso.get().load(photoUrlLink.get(2)).into(upload_photo_img3);
            upload_photo_img4.setVisibility(VISIBLE);
            upload_photo_img5.setVisibility(VISIBLE);

            delete_2.setVisibility(View.VISIBLE);
            delete_3.setVisibility(View.VISIBLE);
            delete_4.setVisibility(View.GONE);
            delete_5.setVisibility(View.GONE);
        } else if(photoUrlLink.size() == 4) {
            Picasso.get().load(photoUrlLink.get(1)).into(upload_photo_img2);
            Picasso.get().load(photoUrlLink.get(2)).into(upload_photo_img3);
            Picasso.get().load(photoUrlLink.get(3)).into(upload_photo_img4);
            upload_photo_img5.setVisibility(VISIBLE);

            delete_2.setVisibility(View.VISIBLE);
            delete_3.setVisibility(View.VISIBLE);
            delete_4.setVisibility(View.VISIBLE);
            delete_5.setVisibility(View.GONE);
        } else if(photoUrlLink.size() == 5) {
            Picasso.get().load(photoUrlLink.get(1)).into(upload_photo_img2);
            Picasso.get().load(photoUrlLink.get(2)).into(upload_photo_img3);
            Picasso.get().load(photoUrlLink.get(3)).into(upload_photo_img4);
            Picasso.get().load(photoUrlLink.get(4)).into(upload_photo_img5);
            delete_5.setVisibility(View.VISIBLE);
            delete_2.setVisibility(View.VISIBLE);
            delete_3.setVisibility(View.VISIBLE);
            delete_4.setVisibility(View.VISIBLE);
        }
    }

    private void deletePhoto(final String number){
        Intent intent = getIntent();
        final String ad_detail = intent.getStringExtra("ad_detail");

        final String Filename = ad_detail + number;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Log.d("Message", "Return SUCCESS");
                                } else {
                                    Log.e("Message", "Return FAILED");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Log.e("onResponse", "Return NULL");
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
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

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
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

                    InputStream inputStream = getContentResolver().openInputStream(filePath1);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img1.setImageBitmap(bitmap1);

                    addTemp(getStringImage(bitmap1),0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                if(deleteTemp(0)){
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                        bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth(), bitmap1.getHeight(), true);

                        InputStream inputStream = getContentResolver().openInputStream(filePath1);

                        try {
                            ExifInterface exif = new ExifInterface(inputStream);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload_photo_img1.setImageBitmap(bitmap1);

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

                    InputStream inputStream = getContentResolver().openInputStream(filePath2);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img2.setImageBitmap(bitmap2);

                    addTemp(getStringImage(bitmap2),1);
                    delete_2.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(photoTempId.size() == 1){
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);

                    InputStream inputStream = getContentResolver().openInputStream(filePath2);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img2.setImageBitmap(bitmap2);

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

                        InputStream inputStream = getContentResolver().openInputStream(filePath2);

                        try {
                            ExifInterface exif = new ExifInterface(inputStream);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload_photo_img2.setImageBitmap(bitmap2);

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

                    InputStream inputStream = getContentResolver().openInputStream(filePath3);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img3.setImageBitmap(bitmap3);

                    addTemp(getStringImage(bitmap3),2);
                    delete_3.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(photoTempId.size() == 2){
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    bitmap3 = Bitmap.createScaledBitmap(bitmap3, bitmap3.getWidth(), bitmap3.getHeight(), true);

                    InputStream inputStream = getContentResolver().openInputStream(filePath3);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img3.setImageBitmap(bitmap3);

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

                        InputStream inputStream = getContentResolver().openInputStream(filePath3);

                        try {
                            ExifInterface exif = new ExifInterface(inputStream);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload_photo_img3.setImageBitmap(bitmap3);

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

                    InputStream inputStream = getContentResolver().openInputStream(filePath4);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img4.setImageBitmap(bitmap4);

                    addTemp(getStringImage(bitmap4),3);
                    delete_4.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (photoTempId.size() == 3){
                try {
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                    bitmap4 = Bitmap.createScaledBitmap(bitmap4, bitmap4.getWidth(), bitmap4.getHeight(), true);

                    InputStream inputStream = getContentResolver().openInputStream(filePath4);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img4.setImageBitmap(bitmap4);

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

                        InputStream inputStream = getContentResolver().openInputStream(filePath4);

                        try {
                            ExifInterface exif = new ExifInterface(inputStream);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload_photo_img4.setImageBitmap(bitmap4);

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

                    InputStream inputStream = getContentResolver().openInputStream(filePath5);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap5 = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img5.setImageBitmap(bitmap5);

                    addTemp(getStringImage(bitmap5),4);
                    delete_5.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(photoTempId.size() == 4){
                try {
                    bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath5);
                    bitmap5 = Bitmap.createScaledBitmap(bitmap5, bitmap5.getWidth(), bitmap5.getHeight(), true);

                    InputStream inputStream = getContentResolver().openInputStream(filePath5);

                    try {
                        ExifInterface exif = new ExifInterface(inputStream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap5 = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    upload_photo_img5.setImageBitmap(bitmap5);

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

                        InputStream inputStream = getContentResolver().openInputStream(filePath5);

                        try {
                            ExifInterface exif = new ExifInterface(inputStream);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap5 = Bitmap.createBitmap(bitmap5, 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload_photo_img5.setImageBitmap(bitmap5);

                        addTemp(getStringImage(bitmap5),4);
                        delete_5.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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

    @Override
    public void onBackPressed() {
        for(int i = 0; i< photoTempId.size(); i++){
            deleteAllTemp(i);
            Log.i("ARRAY", String.valueOf(i));
        }

        super.onBackPressed();
        startActivity(new Intent(Product_Edit.this, Me_Page.class));
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
//                                Toast.makeText(Product_Edit.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
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

        Log.i("PHOTO", photoTempId.toString());
        Log.i("PHOTO", String.valueOf(photoTempId.size()));
    }

    private boolean deleteTemp(int count){
        final String photoId = photoUrlLink.get(count);
        Log.i("IMAGE", photoId);

        final String[] getPhotoId = photoId.split("https://ketekmall\\.com/ketekmall/products/");
        final String[] noExtension = getPhotoId[1].split("\\.");
        Log.i("IMAGE", getPhotoId[1]);
        Log.i("IMAGE", noExtension[0]);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Edit.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("id", noExtension[0]);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

//        photoUrlLink.set(count, "null");
        photoTempId.set(count, "null");
        Log.i("PHOTO", photoUrlLink.toString());
        Log.i("PHOTO", photoTempId.toString());
        return true;
    }

    private void deleteAllTemp(int count){
        final String photoId = photoUrlLink.get(count);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Log.i("IMAGE", "SUCCESS");
//                                Toast.makeText(Product_Edit.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
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
//                                Toast.makeText(Product_Edit.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("IMAGE", "ERROR");
                                Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
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
}
