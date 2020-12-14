package com.ketekmall.ketekmall.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.register_seller.TermsAndConditions;
import com.ketekmall.ketekmall.pages.seller.MyProducts;
import com.ketekmall.ketekmall.pages.seller.MySelling;
import com.ketekmall.ketekmall.pages.seller.ProductRating;
import com.ketekmall.ketekmall.pages.seller.Product_Add;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Selling extends Fragment {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";

    String getId;
    SessionManager sessionManager;
    Button btn_register;

    TextView textView12, textView13, textView14, textView15;

    RelativeLayout AddNewProduct, MyProduct, Selling, MyRating, MyIncome,
             BoostAd, SellerChecked, SellerUnchecked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_selling, container, false);
        Declare(view);
        GotoPage();
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        SellerCheck_Main(getId);
        return view;
    }

    private void Declare(View view){
        AddNewProduct = view.findViewById(R.id.AddNewProduct);
        MyProduct = view.findViewById(R.id.MyProduct);
        Selling = view.findViewById(R.id.MySelling);
        MyRating = view.findViewById(R.id.MyRating);
        MyIncome = view.findViewById(R.id.MyIncome);
        BoostAd = view.findViewById(R.id.BoostAds);

        SellerChecked = view.findViewById(R.id.sellerchecked);
        SellerUnchecked = view.findViewById(R.id.sellerunchecked);
        btn_register = view.findViewById(R.id.btn_register);

        textView12 = view.findViewById(R.id.textView12);
        textView13 = view.findViewById(R.id.textView13);
        textView14 = view.findViewById(R.id.textView14);
        textView15 = view.findViewById(R.id.textView15);
    }

    private void GotoPage(){
        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Product_Add.class);
                requireActivity().startActivity(intent);
            }});

        MyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProducts.class);
                requireActivity().startActivity(intent);
            }
        });

        Selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MySelling.class);
                requireActivity().startActivity(intent);
            }
        });

        MyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductRating.class);
                requireActivity().startActivity(intent);
            }
        });

        MyIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.ketekmall.ketekmall.pages.seller.MyIncome.class);
                requireActivity().startActivity(intent);
            }
        });

        BoostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.ketekmall.ketekmall.pages.seller.BoostAd.class);
                requireActivity().startActivity(intent);
            }
        });
    }

    private void SellerCheck_Main(final String user_id){
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
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int strVerify = Integer.parseInt(object.getString("verification"));
                                    if(strVerify == 0){

                                        SellerUnchecked.setVisibility(View.VISIBLE);
                                        SellerChecked.setVisibility(View.GONE);
                                        textView12.setVisibility(View.VISIBLE);
                                        textView13.setVisibility(View.VISIBLE);
                                        textView14.setVisibility(View.GONE);
                                        textView15.setVisibility(View.GONE);
                                        btn_register.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), TermsAndConditions.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }else if(strVerify == 2){
                                        SellerUnchecked.setVisibility(View.VISIBLE);
                                        SellerChecked.setVisibility(View.GONE);
                                        textView12.setVisibility(View.GONE);
                                        textView13.setVisibility(View.GONE);
                                        textView14.setVisibility(View.VISIBLE);
                                        textView15.setVisibility(View.VISIBLE);
                                        btn_register.setVisibility(View.GONE);
                                    }else{
                                        SellerUnchecked.setVisibility(View.GONE);
                                        SellerChecked.setVisibility(View.VISIBLE);
                                    }

                                }
                            } else {
                                Toast.makeText(getContext(), "Incorrect Information", Toast.LENGTH_SHORT).show();
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

                            }else if(error instanceof NoConnectionError){
                                //net work error

                            } else if (error instanceof AuthFailureError) {
                                //error

                            } else if (error instanceof ServerError) {
                                //Erroor
                            } else if (error instanceof NetworkError) {
                                //Error

                            } else if (error instanceof ParseError) {
                                //Error

                            }else{
                                //Error
                            }
                            //End


                        } catch (Exception e) {


                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}
