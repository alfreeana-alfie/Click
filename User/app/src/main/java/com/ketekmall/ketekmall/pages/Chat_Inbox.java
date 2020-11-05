package com.ketekmall.ketekmall.pages;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ketekmall.ketekmall.adapter.Chat_Adapter;
import com.ketekmall.ketekmall.data.User;
import com.ketekmall.ketekmall.data.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat_Inbox extends AppCompatActivity {

    public static String URL = "https://click-1595830894120.firebaseio.com/users.json";
    public static String URL_MESSAGE = "https://click-1595830894120.firebaseio.com/messages.json";
    private static String URL_READ_CHAT = "https://ketekmall.com/ketekmall/read_chat.php";

    User user;
    RecyclerView recyclerView;
    TextView noUsersText;
    List<User> usersArrayList;
    Chat_Adapter user_adapter;
    int totalUsers = 0;
    BottomNavigationView bottomNav;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_inbox);
        ToolbarSetting();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Chat_Inbox.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Chat_Inbox.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Chat_Inbox.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        recyclerView = findViewById(R.id.usersList);
        usersArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Chat_Inbox.this));
        noUsersText = findViewById(R.id.noUsersText);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        doOnSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox.this);
        requestQueue.add(stringRequest);

        doON();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_chat_inbox);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Chat_Inbox.this, Homepage.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
    }


    private void doON(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object =new JSONObject(response);
                            Iterator i = object.keys();

                            while (i.hasNext()){
                                final String chat = i.next().toString();


                                String newemail = UserDetails.email;
                                if(newemail.contains("@")){
                                    newemail = UserDetails.email.substring(0, UserDetails.email.lastIndexOf("@"));
                                }else {
                                    newemail = UserDetails.email;
                                }
                                if(chat.contains(newemail + "_" )){
                                    final String finalNewemail = newemail;
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        final JSONObject object =new JSONObject(response);
                                                        Iterator i = object.keys();

                                                        while (i.hasNext()){
                                                            final String key = i.next().toString();
                                                            String newemail1 = object.getJSONObject(key).get("email").toString().substring(0, object.getJSONObject(key).get("email").toString().lastIndexOf("@"));

                                                            final String ref1 = finalNewemail + "_" + newemail1;

                                                            if(!key.equals(UserDetails.username)){
                                                                if (chat.equals(ref1)){
                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_CHAT,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        String success = jsonObject.getString("success");
                                                                                        final JSONArray jsonArray = jsonObject.getJSONArray("read");

                                                                                        if (success.equals("1")) {
                                                                                            user = new User(key, object.getJSONObject(key).get("photo").toString());
                                                                                            String newemail2 = object.getJSONObject(key).get("email").toString().substring(0, object.getJSONObject(key).get("email").toString().lastIndexOf("@"));
                                                                                            user.setChatwith(newemail2);
                                                                                            user.setCount(String.valueOf(jsonArray.length()));

                                                                                            usersArrayList.add(user);
                                                                                            user_adapter = new Chat_Adapter(Chat_Inbox.this, usersArrayList);

                                                                                            recyclerView.setAdapter(user_adapter);
                                                                                            user_adapter.setOnItemClickListener(new Chat_Adapter.OnItemClickListener() {
                                                                                                @Override
                                                                                                public void onItemClick(int position) {
                                                                                                    User user = usersArrayList.get(position);
                                                                                                    UserDetails.chatWith = user.getChatwith();
                                                                                                    UserDetails.chatWith1 = user.getUsername();
                                                                                                    startActivity(new Intent(Chat_Inbox.this, Chat.class));
                                                                                                }
                                                                                            });
                                                                                            Log.d("Message", user.getCount());
                                                                                        } else {
                                                                                            Log.e("Message", "Return FAILED");
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
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() {
                                                                            Map<String, String> params = new HashMap<>();
                                                                            params.put("user_chatwith", ref1);
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox.this);
                                                                    requestQueue.add(stringRequest);

                                                                }
                                                            }
                                                            totalUsers++;
                                                        }

                                                    }catch (JSONException e){
                                                        e.printStackTrace();
                                                    }

                                                    if (totalUsers <= 1) {
                                                        noUsersText.setVisibility(View.VISIBLE);
                                                        recyclerView.setVisibility(View.GONE);
                                                    } else {
                                                        noUsersText.setVisibility(View.GONE);
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        recyclerView.setAdapter(user_adapter);
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
                                                        } else if (error instanceof NoConnectionError) {
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
                                                        } else {
                                                            //Error
                                                            System.out.println("" + error);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                    RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        }catch (JSONException e){
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
                            } else if (error instanceof NoConnectionError) {
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
                            } else {
                                //Error
                                System.out.println("" + error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(Chat_Inbox.this, Homepage.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        finish();
    }
}
