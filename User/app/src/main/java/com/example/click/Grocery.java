package com.example.click;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.View_Item;
import com.example.click.R;
import com.example.click.Item_Adapter;
import com.example.click.Item_All_Details;
import com.example.click.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grocery extends Fragment {

    public static final String ID = "id";
    public static final String USERID = "user_id";
    public static final String MAIN_CATE = "main_category";
    public static final String SUB_CATE = "sub_category";
    public static final String AD_DETAIL = "ad_detail";
    public static final String PRICE = "price";
    public static final String DISTRICT = "district";
    public static final String DIVISION = "division";
    public static final String PHOTO = "photo";
    private static String URL_READ = "https://ketekmall.com/ketekmall/category/read_category_grocery.php";
    private static String URL_ADD_FAV = "https://ketekmall.com/ketekmall/add_to_fav.php";
    private static String URL_ADD_CART = "https://ketekmall.com/ketekmall/add_to_cart.php";

    SessionManager sessionManager;
    String getId;
    GridView gridView;
    Item_Adapter adapter_item;
    List<Item_All_Details> itemList;

    SearchView searchView;
    private Spinner spinner_division, spinner_district;
    private ImageButton but_division, but_district;
    private Button price_sortlowest, price_sorthighest;
    private ArrayAdapter<CharSequence> adapter_division, adapter_district;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_view, container, false);
        Declare(view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(spinner_division.getSelectedItem().toString().equals("All")){
                    adapter_item.getFilter().filter(newText);
                }else if(spinner_district.getSelectedItem().toString().equals("All")){
                    adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + newText);
                } else if (!spinner_division.getSelectedItem().toString().equals("All") && !spinner_district.getSelectedItem().toString().equals("All")){
                    adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString() + newText);
                } else{
                    adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + newText + spinner_district.getSelectedItem().toString() );

                }
                return false;
            }
        });
        View_Item(view);
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        return view;
    }

    private void Declare(View v) {
        itemList = new ArrayList<>();
        gridView = v.findViewById(R.id.gridView_CarItem);
        searchView = v.findViewById(R.id.search_find);


        spinner_division = v.findViewById(R.id.spinner_division);
        spinner_district = v.findViewById(R.id.spinner_district);
        but_division = v.findViewById(R.id.but_division);
        but_district = v.findViewById(R.id.but_district);
        price_sortlowest = v.findViewById(R.id.price_sortlowest);
        price_sorthighest = v.findViewById(R.id.price_sorthighest);
        price_sorthighest.setVisibility(View.GONE);

        adapter_division = ArrayAdapter.createFromResource(v.getContext(), R.array.division, android.R.layout.simple_spinner_item);
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);

        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                showResult(position);
                if (position != 0) {
                    but_division.setVisibility(View.VISIBLE);
                    adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        price_sortlowest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayLowest();
                price_sortlowest.setVisibility(View.GONE);
                price_sorthighest.setVisibility(View.VISIBLE);
            }
        });

        price_sorthighest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.sortArrayHighest();
                price_sorthighest.setVisibility(View.GONE);
                price_sortlowest.setVisibility(View.VISIBLE);
            }
        });

        but_division.setVisibility(View.GONE);
        but_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.getFilter().filter(null);
                spinner_division.setSelection(0);
                but_division.setVisibility(View.GONE);
                but_district.setVisibility(View.GONE);
                spinner_district.setSelection(0);
            }
        });

        but_district.setVisibility(View.GONE);
        but_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString());
                spinner_district.setSelection(0);
                but_district.setVisibility(View.GONE);
            }
        });
    }

    private void showResult(int position){
        switch (position){
            case 0:
                spinner_district.setVisibility(View.GONE);
                break;

            case 1:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.kuching, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 2:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.samarahan, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });
                break;

            case 3:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.serian, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 4:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sri_aman, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 5:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.betong, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 6:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sarikei, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 7:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.sibu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 8:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.mukah, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 9:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.bintulu, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 10:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.kapit, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 11:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.miri, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });

                break;

            case 12:
                spinner_district.setVisibility(View.VISIBLE);
                adapter_district = ArrayAdapter.createFromResource(getContext(), R.array.limbang, android.R.layout.simple_spinner_item);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        if (position != 0) {
                            but_district.setVisibility(View.VISIBLE);
                            adapter_item.getFilter().filter(spinner_division.getSelectedItem().toString() + spinner_district.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        adapter_item.getFilter().filter(null);
                    }
                });
                break;
        }
    }

    private void View_Item(final View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
//                                Toast.makeText(Homepage.this, "Login! ", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String seller_id = object.getString("user_id").trim();
                                    String main_category = object.getString("main_category").trim();
                                    String sub_category = object.getString("sub_category").trim();
                                    String ad_detail = object.getString("ad_detail").trim();
                                    String price = object.getString("price").trim();
                                    String division = object.getString("division");
                                    String district = object.getString("district");
                                    String image_item = object.getString("photo");

                                    Item_All_Details item = new Item_All_Details(id, seller_id, main_category, sub_category, ad_detail, price, division, district, image_item);
                                    itemList.add(item);
                                }
                                adapter_item = new Item_Adapter(itemList, getContext());
                                adapter_item.notifyDataSetChanged();
                                gridView.setAdapter(adapter_item);
                                adapter_item.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
                                    @Override
                                    public void onViewClick(int position) {
                                        Intent detailIntent = new Intent(getContext(), View_Item.class);
                                        Item_All_Details item = itemList.get(position);

                                        detailIntent.putExtra(USERID, item.getSeller_id());
                                        detailIntent.putExtra(MAIN_CATE, item.getMain_category());
                                        detailIntent.putExtra(SUB_CATE, item.getSub_category());
                                        detailIntent.putExtra(AD_DETAIL, item.getAd_detail());
                                        detailIntent.putExtra(PRICE, item.getPrice());
                                        detailIntent.putExtra(DIVISION, item.getDivision());
                                        detailIntent.putExtra(DISTRICT, item.getDistrict());
                                        detailIntent.putExtra(PHOTO, item.getPhoto());

                                        startActivity(detailIntent);
                                    }

                                    @Override
                                    public void onAddtoFavClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strItem_Id = item.getId();
                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strDivision = item.getDivision();
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if(getId.equals(item.getSeller_id())){
                                            Toast.makeText(getContext(), "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        }else{
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(getContext(), "Add To Favourite", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("customer_id", getId);
                                                    params.put("main_category", strMain_category);
                                                    params.put("sub_category", strSub_category);
                                                    params.put("ad_detail", strAd_Detail);
                                                    params.put("price", String.format("%.2f", strPrice));
                                                    params.put("division", strDivision);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                            requestQueue.add(stringRequest1);
                                        }
                                    }

                                    @Override
                                    public void onAddtoCartClick(int position) {
                                        Item_All_Details item = itemList.get(position);

                                        final String strItem_Id = item.getId();
                                        final String strSeller_id = item.getSeller_id();
                                        final String strMain_category = item.getMain_category();
                                        final String strSub_category = item.getSub_category();
                                        final String strAd_Detail = item.getAd_detail();
                                        final Double strPrice = Double.valueOf(item.getPrice());
                                        final String strDivision = item.getDivision();
                                        final String strDistrict = item.getDistrict();
                                        final String strPhoto = item.getPhoto();

                                        if(getId.equals(strSeller_id)){
                                            Toast.makeText(getContext(), "Sorry, Cannot add your own item", Toast.LENGTH_SHORT).show();
                                        }else{
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_ADD_CART,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                String success = jsonObject1.getString("success");

                                                                if (success.equals("1")) {
                                                                    Toast.makeText(getContext(), "Add To Cart", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("customer_id", getId);
                                                    params.put("main_category", strMain_category);
                                                    params.put("sub_category", strSub_category);
                                                    params.put("ad_detail", strAd_Detail);
                                                    params.put("price", String.format("%.2f", strPrice));
                                                    params.put("division", strDivision);
                                                    params.put("district", strDistrict);
                                                    params.put("photo", strPhoto);
                                                    params.put("seller_id", strSeller_id);
                                                    params.put("item_id", strItem_Id);
                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                            requestQueue.add(stringRequest2);
                                        }

//                                        new Handler().post(new Runnable() {
//                                            @Override
//                                            public void run()
//                                            {
//                                                Intent intent = getActivity().getIntent();
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
//                                                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                getActivity().overridePendingTransition(0, 0);
//
//                                                getActivity().overridePendingTransition(0, 0);
//                                                startActivity(intent);
//                                            }
//                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Login Failed! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }
}
