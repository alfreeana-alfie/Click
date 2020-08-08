package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String URL_READ = "https://ketekmall.com/ketekmall/read_detail.php";
    String getId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        final Fragment fragment_login = new Login();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment_login);
        fragmentTransaction.commit();

        getUserDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        disconnectFromFacebook();
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    private void getUserDetail() {
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

                                    final String name = object.getString("name").trim();
                                    final String email = object.getString("email").trim();
                                    final String phone_no = object.getString("phone_no").trim();
                                    final String address = object.getString("address").trim();
                                    final String birthday = object.getString("birthday").trim();
                                    final String gender = object.getString("gender").trim();
                                    final String photo = object.getString("photo").trim();
                                    String id = object.getString("id").trim();

                                    sessionManager.createSession(name, email, phone_no, address, birthday, gender, id);

                                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("phone_no", phone_no);
                                    intent.putExtra("address", address);
                                    intent.putExtra("birthday", birthday);
                                    intent.putExtra("gender", gender);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                                    //Firebase
                                    String url = "https://click-1595830894120.firebaseio.com/users.json";

                                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            if (s.equals("null")) {
                                                Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                            } else {
                                                try {
                                                    JSONObject obj = new JSONObject(s);
                                                    if (!obj.has(name)) {

                                                        String url = "https://click-1595830894120.firebaseio.com/users.json";

                                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                final Firebase reference = new Firebase("https://click-1595830894120.firebaseio.com/users");

                                                                if (s.equals("null")) {
                                                                    reference.child(name).child("email").setValue(email);
                                                                    reference.child(name).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                } else {
                                                                    try {
                                                                        JSONObject obj = new JSONObject(s);

                                                                        if (!obj.has(name)) {
                                                                            reference.child(name).child("email").setValue(email);
                                                                            reference.child(name).child("photo").setValue(photo);
                                                                            reference.child(name).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                        } else {
                                                                            reference.child(name).child("email").setValue(email);
                                                                            reference.child(name).child("photo").setValue(photo);
                                                                            reference.child(name).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }

                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                System.out.println("" + volleyError);
                                                            }
                                                        });

                                                        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                                                        rQueue.add(request);
                                                    } else if (obj.getJSONObject(name).getString("email").equals(email)) {
                                                        UserDetails.username = name;
                                                        UserDetails.email = email;
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "incorrect email", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            System.out.println("" + volleyError);
                                        }
                                    });

                                    RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                                    rQueue.add(request);


                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
