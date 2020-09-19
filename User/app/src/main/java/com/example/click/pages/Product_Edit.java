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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Product_Edit extends AppCompatActivity {

    private static String URL_UPLOAD = "https://ketekmall.com/ketekmall/edituser.php";
    private static String URL_IMG = "https://ketekmall.com/ketekmall/uploadimg02.php";

    ArrayAdapter<CharSequence> adapter_division, adapter_district, adapter_category, adapter_car,
            adapter_properties, adapter_electronic, adapter_home,
            adapter_leisure, adapter_business, adapter_jobs,
            adapter_travel, adapter_other;
    Uri filePath;
    String id, sub_category, district;
    private Bitmap bitmap;
    private TextView Main_Category_TextView, Sub_Category_TextView, Ad_Detail_TextView,
            Category_TextView, Location_TextView, Division_TextView, District_TextView, Delivery_Location;
    private EditText EditText_Price, EditText_Ad_Detail, edittext_brand, edittext_inner, edittext_stock, edittext_desc, Edittext_Order;
    private Button Button_AcceptCategory, Button_BackCategory,
            Button_AcceptAdDetail, Button_BackAdDetail, Button_BackEdit,
            Button_SavedEdit, Button_AcceptLocation, Button_BackLocation;
    private Spinner spinner_main_category, spinner_sub_category, spinner_division, spinner_district;
    private RelativeLayout category_page_layout, location_page_layout;
    private LinearLayout item_page_layout;
    private ImageView Upload_Photo;
    private ScrollView about_detail;
    private ProgressBar loading;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        Declare();

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        final String main_category = intent.getStringExtra("main_category");
        sub_category = intent.getStringExtra("sub_category");
        final String ad_detail = intent.getStringExtra("ad_detail");
        final String price = intent.getStringExtra("price");
        final String division = intent.getStringExtra("division");
        district = intent.getStringExtra("district");
        final String photo = intent.getStringExtra("photo");
        String Category_Text = main_category;
        String Location_Text = division + ", " + district;
        final String strMax_Order = intent.getStringExtra("max_order");

        final String brand = intent.getStringExtra("brand_material");
        final String inner = intent.getStringExtra("inner_material");
        final String stock = intent.getStringExtra("stock");
        final String desc = intent.getStringExtra("description");

        Edittext_Order.setText(strMax_Order);

        Category_TextView.setText(Category_Text);

        Delivery_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Product_Edit.this, Delivery_MainPage.class);
                intent1.putExtra("item_id", id);
                intent1.putExtra("ad_detail", ad_detail);
                intent1.putExtra("main_category", main_category);
                intent1.putExtra("sub_category", sub_category);
                intent1.putExtra("price", price);
                intent1.putExtra("division", division);
                intent1.putExtra("district", district);
                intent1.putExtra("photo", photo);
                intent1.putExtra("max_order", strMax_Order);

                intent1.putExtra("division", brand);
                intent1.putExtra("district", inner);
                intent1.putExtra("photo", stock);
                intent1.putExtra("max_order", desc);
                startActivity(intent1);
            }
        });

        if (main_category != null) {
            int main_catposition = adapter_category.getPosition(main_category);
            spinner_main_category.setSelection(main_catposition);

        }

        if (division != null) {
            int division_position = adapter_division.getPosition(division);
            spinner_division.setSelection(division_position);
        }

        Main_Category_TextView.setText(main_category);
        Sub_Category_TextView.setText(sub_category);
        Ad_Detail_TextView.setText(ad_detail);
        Location_TextView.setText(Location_Text);
        Division_TextView.setText(division);
        District_TextView.setText(district);
        EditText_Ad_Detail.setText(ad_detail);
        EditText_Price.setText(price);
        Picasso.get().load(photo).into(Upload_Photo);

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
        Delivery_Location = findViewById(R.id.enter_delivery_location);

        about_detail = findViewById(R.id.about_product);

        Edittext_Order = findViewById(R.id.enter_max_order);

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

        Upload_Photo = findViewById(R.id.upload_photo);
        loading = findViewById(R.id.loading);
        category_page_layout = findViewById(R.id.category_page_layout);
//        ad_detail_page_layout = findViewById(R.id.ad_detail_page_layout);
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
        Upload_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

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
                Intent intent = new Intent(Product_Edit.this, MyProducts.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        final String strMain_category = this.spinner_main_category.getSelectedItem().toString();
        final String strSub_category = this.Sub_Category_TextView.getText().toString().trim();
        final String strAd_Detail = this.EditText_Ad_Detail.getText().toString();
        final String strBrand = this.edittext_brand.getText().toString();
        final String strInner = this.edittext_inner.getText().toString();
        final String strStock = this.edittext_stock.getText().toString();
        final String strDesc = this.edittext_desc.getText().toString();

        final Double strPrice = Double.valueOf(this.EditText_Price.getText().toString().trim());
        final String strPrice_Text = String.format("%.2f", strPrice);
        final String strOrder = this.Edittext_Order.getText().toString();
        final String strDivision = this.Division_TextView.getText().toString().trim();
        final String strDistrict = this.District_TextView.getText().toString().trim();


        loading.setVisibility(View.VISIBLE);
        Button_SavedEdit.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                Button_SavedEdit.setVisibility(View.VISIBLE);
                                Toast.makeText(Product_Edit.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(Product_Edit.this, MyProducts.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent1);

                            } else {
                                loading.setVisibility(View.GONE);
                                Button_SavedEdit.setVisibility(View.VISIBLE);
                                Toast.makeText(Product_Edit.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            Button_SavedEdit.setVisibility(View.VISIBLE);
                            e.printStackTrace();
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


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("main_category", strMain_category);
                params.put("sub_category", strMain_category);
                params.put("ad_detail", strAd_Detail);
                params.put("brand_material", strBrand);
                params.put("inner_material", strInner);
                params.put("stock", strStock);
                params.put("description", strDesc);

                params.put("price", strPrice_Text);
                params.put("max_order", strOrder);
                params.put("division", strDivision);
                params.put("district", strDistrict);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void saveImage(final String photo) {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        final String strAd_Detail = this.EditText_Ad_Detail.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IMG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(Product_Edit.this, "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Product_Edit.this, "Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Product_Edit.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

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


                        }
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

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Upload_Photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveImage(getStringImage(bitmap));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Product_Edit.this, MyProducts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
