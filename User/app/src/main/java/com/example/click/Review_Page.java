package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.click.data.SessionManager;
import com.example.click.pages.Main_Order_Other;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Review_Page extends AppCompatActivity {

    private static String URL_REVIEW = "https://ketekmall.com/ketekmall/add_review.php";

    EditText edit_review;
    Button btn_submit, btn_cancel;
    String getId;
    SessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);

        getSession();

        Intent intent = getIntent();
        final String strSeller_ID = intent.getStringExtra("seller_id");
        String strCustomer_ID = intent.getStringExtra("customer_id");
        final String strItem_ID = intent.getStringExtra("item_id");

        edit_review = findViewById(R.id.editText_review);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewList(strSeller_ID, getId, strItem_ID);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review_Page.this, Main_Order_Other.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
    }

    private void ViewList(final String strSeller_ID, final String strCustomer_ID, final String strItem_ID) {
        final String reviewtext = edit_review.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Review_Page.this, "Saved", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Review_Page.this, Main_Order_Other.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Review_Page.this, "Failed to Save Product", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                                    Toast.makeText(Sell_Items_Other.this, "JSON Parsing Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", strSeller_ID);
                params.put("customer_id", strCustomer_ID);
                params.put("item_id", strItem_ID);
                params.put("review", reviewtext);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Review_Page.this);
        requestQueue.add(stringRequest);
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }
}
