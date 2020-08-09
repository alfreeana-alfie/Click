package com.example.click;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Sell_Items extends Fragment {

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
    private TextView enter_category, enter_ad_detail, enter_location;
    private EditText enter_price, edittext_ad_detail;
    private Button accept_item, accept_category, back_category, accept_ad_detail, back_ad_detail, accept_location, back_location, back_item;
    private Spinner spinner_main_category, spinner_sub_category, spinner_division, spinner_district;
    private RelativeLayout category_page_layout, ad_detail_page_layout, location_page_layout;
    private LinearLayout item_page_layout;
    private ImageView upload_photo_img;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_item, container, false);
        Declare(view);

        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        getUserId(view);

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
                ad_detail_page_layout.setVisibility(View.GONE);
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
                if (filePath == null) {
                    Toast.makeText(getContext(), "Please enter image of product", Toast.LENGTH_LONG).show();
                } else {
                    saveEdit(getId, getStringImage(bitmap));
                }

            }
        });

        back_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Homepage.class));
            }
        });

        accept_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_page_layout.setVisibility(View.GONE);
                item_page_layout.setVisibility(View.VISIBLE);

                final String mCategory = spinner_main_category.getSelectedItem().toString() + ", " + spinner_sub_category.getSelectedItem().toString();
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


        return view;
    }

    private void Declare(View v) {
        enter_category = v.findViewById(R.id.enter_main_category);
        enter_ad_detail = v.findViewById(R.id.enter_ad_detail);
        enter_location = v.findViewById(R.id.enter_location);
        enter_price = v.findViewById(R.id.enter_price);

        spinner_division = v.findViewById(R.id.spinner_division);
        spinner_district = v.findViewById(R.id.spinner_district);
        accept_location = v.findViewById(R.id.accept_location);
        back_location = v.findViewById(R.id.back_location);
        location_page_layout = v.findViewById(R.id.location_page_layout);

        accept_item = v.findViewById(R.id.accept_item);
        back_item = v.findViewById(R.id.back_item);
        edittext_ad_detail = v.findViewById(R.id.edittext_ad_detail);
        accept_ad_detail = v.findViewById(R.id.accept_ad_detail);
        back_ad_detail = v.findViewById(R.id.back_ad_detail);
        spinner_main_category = v.findViewById(R.id.spinner_main_category);
        spinner_sub_category = v.findViewById(R.id.spinner_sub_category);
        accept_category = v.findViewById(R.id.accept_category);
        back_category = v.findViewById(R.id.back_category);
        upload_photo_img = v.findViewById(R.id.upload_photo);
        loading = v.findViewById(R.id.loading);

        category_page_layout = v.findViewById(R.id.category_page_layout);
        ad_detail_page_layout = v.findViewById(R.id.ad_detail_page_layout);
        item_page_layout = v.findViewById(R.id.item_page_layout);

        adapter_division = ArrayAdapter.createFromResource(v.getContext(), R.array.division, android.R.layout.simple_spinner_item);
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

        adapter_category = ArrayAdapter.createFromResource(getContext(), R.array.main_category, android.R.layout.simple_spinner_item);
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

    private void gotoAdDetail() {
        ad_detail_page_layout.setVisibility(View.VISIBLE);
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
                adapter_car = ArrayAdapter.createFromResource(getContext(), R.array.vehicle_category, android.R.layout.simple_spinner_item);
                adapter_car.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_car);
//                Toast.makeText(getContext(), "Car", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                adapter_properties = ArrayAdapter.createFromResource(getContext(), R.array.properties_category, android.R.layout.simple_spinner_item);
                adapter_properties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_properties);
//                Toast.makeText(getContext(), "Properties", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                adapter_elctronic = ArrayAdapter.createFromResource(getContext(), R.array.electronic_category, android.R.layout.simple_spinner_item);
                adapter_elctronic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_elctronic);
//                Toast.makeText(getContext(), "Electronics", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                adapter_home = ArrayAdapter.createFromResource(getContext(), R.array.home_category, android.R.layout.simple_spinner_item);
                adapter_home.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_home);
//                Toast.makeText(getContext(), "Home and Personal Items", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                adapter_leisure = ArrayAdapter.createFromResource(getContext(), R.array.leisure_category, android.R.layout.simple_spinner_item);
                adapter_leisure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_leisure);
//                Toast.makeText(getContext(), "Leisure/Sport/Hobbies", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                adapter_business = ArrayAdapter.createFromResource(getContext(), R.array.business_category, android.R.layout.simple_spinner_item);
                adapter_business.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_business);
//                Toast.makeText(getContext(), "Business to Business", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                adapter_jobs = ArrayAdapter.createFromResource(getContext(), R.array.jobs_category, android.R.layout.simple_spinner_item);
                adapter_jobs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_jobs);
//                Toast.makeText(getContext(), "Jobs and Services", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                adapter_travel = ArrayAdapter.createFromResource(getContext(), R.array.travel_category, android.R.layout.simple_spinner_item);
                adapter_travel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_travel);
//                Toast.makeText(getContext(), "Travel", Toast.LENGTH_SHORT).show();
                break;
            case 9:
                adapter_other = ArrayAdapter.createFromResource(getContext(), R.array.other_category, android.R.layout.simple_spinner_item);
                adapter_other.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sub_category.setAdapter(adapter_other);
//                Toast.makeText(getContext(), "Other", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showLocationResult(int position) {
        switch (position) {
            case 0:
                break;

            case 1:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.kuching, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 2:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.samarahan, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 3:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.serian, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 4:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sri_aman, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 5:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.betong, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 6:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sarikei, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 7:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sibu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 8:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.mukah, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 9:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.bintulu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 10:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.kapit, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 11:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.miri, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

            case 12:
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.limbang, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                break;

        }
    }

    private void getUserId(View view) {
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
                                Toast.makeText(getContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

    private void saveEdit(final String id, final String photo) {

        final String strMain_category = this.spinner_main_category.getSelectedItem().toString().trim();
        final String strSub_category = this.spinner_sub_category.getSelectedItem().toString();
        final String strAd_Detail = this.edittext_ad_detail.getText().toString();
        final Double strPrice = Double.valueOf(this.enter_price.getText().toString().trim());
        final String strDivision = this.spinner_division.getSelectedItem().toString().trim();
        final String strDistrict = this.spinner_district.getSelectedItem().toString().trim();

        if (strAd_Detail.isEmpty()) {
            Toast.makeText(getContext(), "Incomplete info", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                                            new Find_My_Items()).commit();
                                } else {
                                    Toast.makeText(getContext(), "Failed to Save Product", Toast.LENGTH_SHORT).show();

                                    loading.setVisibility(View.GONE);
                                    accept_item.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
//                                    Toast.makeText(getContext(), "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() == null) {
//                                    Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                accept_item.setVisibility(View.VISIBLE);
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
                    params.put("price", String.format("%.2f", strPrice));
                    params.put("division", strDivision);
                    params.put("district", strDistrict);
                    params.put("photo", photo);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                upload_photo_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
