package com.ketekmall.ketekmall.pages.buyer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ketekmall.ketekmall.adapter.ChatSession_Adapter;
import com.ketekmall.ketekmall.data.ChatSession;
import com.ketekmall.ketekmall.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.ketekmall.ketekmall.pages.navigation_items.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat_Inbox extends AppCompatActivity {

    public static String URL_CREATECHAT = "https://ketekmall.com/ketekmall/createChat.php";
    public static String URL_GETCHAT = "https://ketekmall.com/ketekmall/getChat.php";
    public static String URL_GETCHATISREAD = "https://ketekmall.com/ketekmall/getChatIsRead.php";

    RecyclerView recyclerView;
    TextView noUsersText;
    List<ChatSession> usersArrayList;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;

    ChatSession_Adapter chat_adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_inbox);
        ToolbarSetting();
        getSession();
        BottomNavigationSettings();

        recyclerView = findViewById(R.id.usersList);
        usersArrayList = new ArrayList<ChatSession>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Chat_Inbox.this));
        noUsersText = findViewById(R.id.noUsersText);

        getChat();

    }

    public void getChat (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETCHAT,
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

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

//                                        String id = object.getString("id").trim();
                                        final String Name = object.getString("Name").trim();
                                        final String UserPhoto = object.getString("UserPhoto").trim();
                                        final String ChatWith = object.getString("ChatWith").trim();
                                        final String ChatWithID = object.getString("ChatWithID").trim();
                                        final String ChatWithPhoto = object.getString("ChatWithPhoto");
//                                        String Content = object.getString("Content").trim();
//                                        String CreatedDateTime = object.getString("CreatedDateTime");

                                        noUsersText.setVisibility(View.GONE);
                                        final ChatSession session = new ChatSession();
                                        session.setName(Name);
                                        session.setUserPhoto(UserPhoto);
                                        session.setUserID(getId);
                                        session.setChatWith(ChatWith);
                                        session.setChatWithID(ChatWithID);
                                        session.setChatWithPhoto(ChatWithPhoto);
//                                        session.setContent(Content);
//                                        session.setCreatedDateTime(CreatedDateTime);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETCHATISREAD,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if(response == null){
                                                            Log.e("onResponse", "Return NULL");
                                                        }else{
                                                            try {
                                                                final JSONObject jsonObject = new JSONObject(response);
                                                                String success = jsonObject.getString("success");
                                                                final JSONArray jsonArray1 = jsonObject.getJSONArray("read");

                                                                if (success.equals("1")) {
                                                                    session.setChatCount(String.valueOf(jsonArray1.length()));
                                                                    usersArrayList.add(session);

                                                                    chat_adapter = new ChatSession_Adapter(Chat_Inbox.this, usersArrayList);
                                                                    recyclerView.setAdapter(chat_adapter);
                                                                    chat_adapter.setOnItemClickListener(new ChatSession_Adapter.OnItemClickListener() {
                                                                        @Override
                                                                        public void onItemClick(int position) {
                                                                            ChatSession chatSession = usersArrayList.get(position);

                                                                            Intent chatIntent = new Intent(Chat_Inbox.this, Chat.class);
                                                                            chatIntent.putExtra("Name", chatSession.getName());
                                                                            chatIntent.putExtra("UserPhoto", chatSession.getUserPhoto());
                                                                            chatIntent.putExtra("ChatWith", chatSession.getChatWith());
                                                                            chatIntent.putExtra("ChatWithID", chatSession.getChatWithID());
                                                                            chatIntent.putExtra("ChatWithPhoto", chatSession.getChatWithPhoto());

                                                                            startActivity(chatIntent);
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(Chat_Inbox.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("UserID", getId);
                                                params.put("ChatWithID", ChatWithID);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);

                                    }

                                } else {
                                    Toast.makeText(Chat_Inbox.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                Intent intent = new Intent(Chat_Inbox.this, Me_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    public void BottomNavigationSettings(){
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Chat_Inbox.this, Me_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
    }
}
