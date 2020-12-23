package com.ketekmall.ketekmall.pages.seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

public class Product_Edit extends AppCompatActivity {

    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/edituser.php";
    private static String URL_DELETE_PHOTO = "https://ketekmall.com/ketekmall/products_img/delete_photo.php";
    private static String URL_IMG = "https://ketekmall.com/ketekmall/products/uploadimg02.php";
    private static String URL_UPLOAD_EXTRA = "https://ketekmall.com/ketekmall/products_img/uploadimg03.php";
    private static String URL_READ_PHOTO = "https://ketekmall.com/ketekmall/products_img/read_photo.php";
    private static String URL_EDIT_PROD = "https://ketekmall.com/ketekmall/edit_product_detail.php";

    ArrayAdapter<CharSequence> adapter_division, adapter_district, adapter_category, adapter_car,
            adapter_properties, adapter_electronic, adapter_home,
            adapter_leisure, adapter_business, adapter_jobs,
            adapter_travel, adapter_other;
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
    private RelativeLayout category_page_layout, location_page_layout;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img1,upload_photo_img2,upload_photo_img3,upload_photo_img4,upload_photo_img5;
    private ImageView delete_2,delete_3,delete_4,delete_5;
    private ScrollView about_detail;
    private ProgressBar loading;
    BottomNavigationView bottomNav;


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
        String Location_Text = division + ", " + district;
        final String strMax_Order = intent.getStringExtra("max_order");
        final String strWeight = intent.getStringExtra("weight");

        final String brand = intent.getStringExtra("brand_material");
        final String inner = intent.getStringExtra("inner_material");
        final String stock = intent.getStringExtra("stock");
        final String desc = intent.getStringExtra("description");

        saveItemID();
        View_Photo();

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
            }
        });

        delete_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_3.setVisibility(View.GONE);
                deletePhoto("3");
            }
        });

        delete_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_4.setVisibility(View.GONE);
                deletePhoto("4");
            }
        });

        delete_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_photo_img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo_foreground));
                delete_5.setVisibility(View.GONE);
                deletePhoto("5");
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
//                Intent intent = new Intent(Product_Edit.this, MyProducts.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });

        Button_SavedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
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

    private void showResult(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                adapter_car = ArrayAdapter.createFromResource(this, R.array.vehicle_category, android.R.layout.simple_spinner_item);
                adapter_car.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_car);
                if (sub_category != null) {
                    int main_catposition = adapter_car.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }
                break;
            case 2:
                adapter_properties = ArrayAdapter.createFromResource(this, R.array.properties_category, android.R.layout.simple_spinner_item);
                adapter_properties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_properties);
                if (sub_category != null) {
                    int main_catposition = adapter_properties.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 3:
                adapter_electronic = ArrayAdapter.createFromResource(this, R.array.electronic_category, android.R.layout.simple_spinner_item);
                adapter_electronic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_electronic);
                if (sub_category != null) {
                    int main_catposition = adapter_electronic.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 4:
                adapter_home = ArrayAdapter.createFromResource(this, R.array.home_category, android.R.layout.simple_spinner_item);
                adapter_home.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_home);
                if (sub_category != null) {
                    int main_catposition = adapter_home.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 5:
                adapter_leisure = ArrayAdapter.createFromResource(this, R.array.leisure_category, android.R.layout.simple_spinner_item);
                adapter_leisure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_leisure);
                if (sub_category != null) {
                    int main_catposition = adapter_leisure.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 6:
                adapter_business = ArrayAdapter.createFromResource(this, R.array.business_category, android.R.layout.simple_spinner_item);
                adapter_business.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_business);
                if (sub_category != null) {
                    int main_catposition = adapter_business.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 7:
                adapter_jobs = ArrayAdapter.createFromResource(this, R.array.jobs_category, android.R.layout.simple_spinner_item);
                adapter_jobs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_jobs);
                if (sub_category != null) {
                    int main_catposition = adapter_jobs.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 8:
                adapter_travel = ArrayAdapter.createFromResource(this, R.array.travel_category, android.R.layout.simple_spinner_item);
                adapter_travel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_travel);
                if (sub_category != null) {
                    int main_catposition = adapter_travel.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }

                break;
            case 9:
                adapter_other = ArrayAdapter.createFromResource(this, R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
                if (sub_category != null) {
                    int main_catposition = adapter_other.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }
                break;

            case 10:
                adapter_other = ArrayAdapter.createFromResource(this, R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
                if (sub_category != null) {
                    int main_catposition = adapter_other.getPosition(sub_category);
                    spinner_sub_category.setSelection(main_catposition);

                }
                break;
        }
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
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
                                    Intent intent1 = new Intent(Product_Edit.this, MyProducts.class);
                                    startActivity(intent1);
                                    finish();
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

    private void View_Photo() {
        Intent intent = getIntent();
        final String ad_detail = intent.getStringExtra("ad_detail");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                final JSONArray jsonArray = jsonObject.getJSONArray("read");
                                String[] image = new String[jsonArray.length()];
                                if(jsonArray.length() == 0 || jsonArray.length() == 1){
                                    upload_photo_img3.setVisibility(VISIBLE);
                                    upload_photo_img4.setVisibility(VISIBLE);
                                    upload_photo_img5.setVisibility(VISIBLE);
                                    delete_2.setVisibility(View.GONE);
                                    delete_3.setVisibility(View.GONE);
                                    delete_4.setVisibility(View.GONE);
                                    delete_5.setVisibility(View.GONE);
                                }else {
                                    if (success.equals("1")) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String image_item = object.getString("filepath");
                                            image[i] = image_item;

                                            Log.d("PHOTO", image_item);
                                        }
                                        delete_2.setVisibility(View.GONE);
                                        delete_3.setVisibility(View.GONE);
                                        delete_4.setVisibility(View.GONE);
                                        delete_5.setVisibility(View.GONE);

                                        Log.d("PHOTO", String.valueOf(image.length));
                                        if(image.length == 2){
                                            Picasso.get().load(image[1]).into(upload_photo_img2);
                                            upload_photo_img3.setVisibility(VISIBLE);
                                            upload_photo_img4.setVisibility(VISIBLE);
                                            upload_photo_img5.setVisibility(VISIBLE);

                                            delete_2.setVisibility(View.VISIBLE);
                                            delete_3.setVisibility(View.GONE);
                                            delete_4.setVisibility(View.GONE);
                                            delete_5.setVisibility(View.GONE);
                                        } else if(image.length == 3) {
                                            Picasso.get().load(image[1]).into(upload_photo_img2);
                                            Picasso.get().load(image[2]).into(upload_photo_img3);
                                            upload_photo_img4.setVisibility(VISIBLE);
                                            upload_photo_img5.setVisibility(VISIBLE);

                                            delete_2.setVisibility(View.VISIBLE);
                                            delete_3.setVisibility(View.VISIBLE);
                                            delete_4.setVisibility(View.GONE);
                                            delete_5.setVisibility(View.GONE);
                                        } else if(image.length == 4) {
                                            Picasso.get().load(image[1]).into(upload_photo_img2);
                                            Picasso.get().load(image[2]).into(upload_photo_img3);
                                            Picasso.get().load(image[3]).into(upload_photo_img4);
                                            upload_photo_img5.setVisibility(VISIBLE);

                                            delete_2.setVisibility(View.VISIBLE);
                                            delete_3.setVisibility(View.VISIBLE);
                                            delete_4.setVisibility(View.VISIBLE);
                                            delete_5.setVisibility(View.GONE);
                                        } else if(image.length == 5) {
                                            Picasso.get().load(image[1]).into(upload_photo_img2);
                                            Picasso.get().load(image[2]).into(upload_photo_img3);
                                            Picasso.get().load(image[3]).into(upload_photo_img4);
                                            Picasso.get().load(image[4]).into(upload_photo_img5);
                                            delete_5.setVisibility(View.VISIBLE);
                                            delete_2.setVisibility(View.VISIBLE);
                                            delete_3.setVisibility(View.VISIBLE);
                                            delete_4.setVisibility(View.VISIBLE);
                                        }


                                    } else {
                                        Toast.makeText(Product_Edit.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                    }

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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", ad_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Product_Edit.this);
        requestQueue.add(stringRequest);
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

    private void saveImage2(final String photo) {
        Intent intent = getIntent();
        strID = intent.getStringExtra("id");
        final String strAd_Detail = this.EditText_Ad_Detail.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IMG,
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
//                                Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                            }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ad_detail", strAd_Detail);
                params.put("photo", photo);
                params.put("id", strID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveImage(final String number, final String photo) {
        Intent intent = getIntent();
        strID = intent.getStringExtra("id");
        final String strAd_Detail = this.EditText_Ad_Detail.getText().toString();

        final String Filename = strAd_Detail + number;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_EXTRA,
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
//                                Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                            }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", strID);
                params.put("ad_detail", strAd_Detail);
                params.put("filename", Filename);
                params.put("filepath", photo);
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
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                bitmap1 = Bitmap.createScaledBitmap(bitmap1, 300, 300, false);
                upload_photo_img1.setImageBitmap(bitmap1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage2(getStringImage(bitmap1));
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, 300, 300, false);
                upload_photo_img2.setImageBitmap(bitmap2);


                delete_2.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage("2", getStringImage(bitmap2));
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            try {

                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                bitmap3 = Bitmap.createScaledBitmap(bitmap3, 300, 300, false);
                upload_photo_img3.setImageBitmap(bitmap3);


                delete_3.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage("3", getStringImage(bitmap3));
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath4 = data.getData();
            try {

                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                bitmap4 = Bitmap.createScaledBitmap(bitmap4, 300, 300, false);
                upload_photo_img4.setImageBitmap(bitmap4);

                delete_4.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage("4", getStringImage(bitmap4));
        }
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath5 = data.getData();
            try {

                bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath5);
                bitmap5 = Bitmap.createScaledBitmap(bitmap5, 300, 300, false);
                upload_photo_img5.setImageBitmap(bitmap5);

                delete_5.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage("5", getStringImage(bitmap5));
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
        super.onBackPressed();
        startActivity(new Intent(Product_Edit.this, Me_Page.class));
        finish();
    }
}
