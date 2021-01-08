package com.ketekmall.ketekmall.pages.buyer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.ketekmall.ketekmall.adapter.ChatSession_Adapter;
import com.ketekmall.ketekmall.data.ChatSession;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.data.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.Me_Page;
import com.ketekmall.ketekmall.pages.Notification_Page;
import com.ketekmall.ketekmall.pages.navigation_items.Chat_Inbox_Homepage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private static String URL_ADD_CHAT = "https://ketekmall.com/ketekmall/add_chat.php";
    private static String URL_EDIT_CHAT = "https://ketekmall.com/ketekmall/edit_chat.php";
    private static String URL_NOTI = "https://ketekmall.com/ketekmall/onesignal_noti.php";
    private static String URL_GET_PLAYERID = "https://ketekmall.com/ketekmall/getPlayerID.php";

    public static String URL_CREATECHAT = "https://ketekmall.com/ketekmall/createChat.php";
    public static String URL_GETCHAT = "https://ketekmall.com/ketekmall/getChat.php";
    public static String URL_GETCHATSINGLE = "https://ketekmall.com/ketekmall/getChatSingle.php";
    public static String URL_UPDATECHAT = "https://ketekmall.com/ketekmall/updateChat.php";

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;

    BottomNavigationView bottomNav;
    List<ChatSession> chatDetailList;
    ChatSession_Adapter chatSession;
    String getId;
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        Declare();
        getSession();
        ToolbarSettings();
        BottomNavigationSettings();
        getChat();
        UpdateChat();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
                createChatWith();
            }
        });
    }

    public void Declare(){
        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        chatDetailList = new ArrayList<>();

    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ToolbarSettings(){

        final Intent chatIntent = getIntent();
        final String Name = chatIntent.getStringExtra("Name");
        final String UserPhoto = chatIntent.getStringExtra("UserPhoto");
        final String ChatWith = chatIntent.getStringExtra("ChatWith");
        final String ChatWithID = chatIntent.getStringExtra("ChatWithID");
        final String ChatWithPhoto = chatIntent.getStringExtra("ChatWithPhoto");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_chat);

        View view = getSupportActionBar().getCustomView();
        TextView chatname = view.findViewById(R.id.user_chatname);
        final CircleImageView circleImageView = view.findViewById(R.id.profile_image);

        chatname.setText(ChatWith);
        Picasso.get().load(ChatWithPhoto).into(circleImageView);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, Chat_Inbox.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void BottomNavigationSettings(){
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Chat.this, Homepage.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Chat.this, Notification_Page.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Chat.this, Me_Page.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
    }

    public void getChat(){
        final Intent chatIntent = getIntent();
        final String IntentChatWithID = chatIntent.getStringExtra("ChatWithID");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETCHATSINGLE,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

                                        String id = object.getString("id").trim();
                                        String Name = object.getString("Name").trim();
                                        String UserPhoto = object.getString("UserPhoto").trim();
                                        String ChatWith = object.getString("ChatWith").trim();
                                        String ChatWithID = object.getString("ChatWithID").trim();
                                        String ChatWithPhoto = object.getString("ChatWithPhoto");
                                        String Content = object.getString("Content").trim();
                                        String CreatedDateTime = object.getString("CreatedDateTime");
                                        String Type = object.getString("Type");

                                        addMessageBox(Content, Integer.parseInt(Type));
                                        addTimeBox(CreatedDateTime, Integer.parseInt(Type));
//                                        Toast.makeText(Chat02.this, ChatWithID, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Chat.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Chat.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("ChatWithID", IntentChatWithID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createChat(){
        final Intent chatIntent = getIntent();
        final String Name = chatIntent.getStringExtra("Name");
        final String UserPhoto = chatIntent.getStringExtra("UserPhoto");
        final String ChatWith = chatIntent.getStringExtra("ChatWith");
        final String ChatWithID = chatIntent.getStringExtra("ChatWithID");
        final String ChatWithPhoto = chatIntent.getStringExtra("ChatWithPhoto");
        final String MessageText = messageArea.getText().toString();

        DateFormat df = new SimpleDateFormat("HH:mm");
        final String CreatedDateTime = df.format(Calendar.getInstance().getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATECHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    addMessageBox(MessageText, 1);
                                    addTimeBox(CreatedDateTime, 1);
                                    GetPlayerData(ChatWithID, MessageText, getId);
                                    messageArea.setText("");
                                } else {
                                    Toast.makeText(Chat.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Chat.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("Name", Name);
                params.put("UserID", getId);
                params.put("UserPhoto", UserPhoto);
                params.put("ChatWith", ChatWith);
                params.put("ChatWithID", ChatWithID);
                params.put("ChatWithPhoto", ChatWithPhoto);
                params.put("Content", MessageText);
                params.put("IsRead", "true");
                params.put("Type", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createChatWith(){
        final Intent chatIntent = getIntent();
        final String Name = chatIntent.getStringExtra("Name");
        final String UserPhoto = chatIntent.getStringExtra("UserPhoto");
        final String ChatWith = chatIntent.getStringExtra("ChatWith");
        final String ChatWithID = chatIntent.getStringExtra("ChatWithID");
        final String ChatWithPhoto = chatIntent.getStringExtra("ChatWithPhoto");
        final String MessageText = messageArea.getText().toString();

        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        String CreatedDateTime = df.format(Calendar.getInstance().getTime());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATECHAT,
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

                                } else {
                                    Toast.makeText(Chat.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                params.put("Name", ChatWith);
                params.put("UserPhoto", ChatWithPhoto);
                params.put("UserID", ChatWithID);
                params.put("ChatWith", Name);
                params.put("ChatWithID", getId);
                params.put("ChatWithPhoto", UserPhoto);
                params.put("Content", MessageText);
                params.put("IsRead", "false");
                params.put("Type", "2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void UpdateChat(){
        final Intent chatIntent = getIntent();
        final String Name = chatIntent.getStringExtra("Name");
        final String UserPhoto = chatIntent.getStringExtra("UserPhoto");
        final String ChatWith = chatIntent.getStringExtra("ChatWith");
        final String ChatWithID = chatIntent.getStringExtra("ChatWithID");
        final String ChatWithPhoto = chatIntent.getStringExtra("ChatWithPhoto");
        final String MessageText = messageArea.getText().toString();

        DateFormat df = new SimpleDateFormat("HH:mm");
        final String CreatedDateTime = df.format(Calendar.getInstance().getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATECHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == null){
                            Log.e("onResponse", "Return NULL");
                        }else{
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                } else {
                                    Toast.makeText(Chat.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Chat.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("Name", Name);
                params.put("UserID", getId);
                params.put("UserPhoto", UserPhoto);
                params.put("ChatWithID", ChatWithID);
                params.put("IsRead", "true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void ChatData(final String user_chatWith, final String chat_key){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_CHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_chatwith", user_chatWith);
                params.put("chat_key", chat_key);
                params.put("is_read", "false");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Chat.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void GetPlayerData(final String SellerID, final String MessageText, final String CustomerID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PLAYERID,
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

                                    final String PlayerID = object.getString("PlayerID");

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PLAYERID,
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

                                                                String sPlayerID = object.getString("PlayerID");
                                                                String SName = object.getString("Name");
                                                                String UserID = object.getString("UserID");

                                                                OneSignalNoti(PlayerID, SName, MessageText);
                                                            }
                                                        } else {
                                                            Toast.makeText(Chat.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("UserID", CustomerID);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(stringRequest);
                                }
                            } else {
                                Toast.makeText(Chat.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Homepage.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", SellerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void OneSignalNoti(final String PlayerUserID, final String Name, final String MessageText){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NOTI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("POST", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {//Time out error
                                System.out.println("" + error);
                            } else if (error instanceof NoConnectionError) {
                                //net work error
                                System.out.println("" + error);
                            } else if (error instanceof AuthFailureError) {
                                //error
                                System.out.println("" + error);
                            } else if (error instanceof ServerError) {
                                //Error
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
                params.put("PlayerID", PlayerUserID);
                params.put("Name", Name);
                params.put("Words", Name +": "+ MessageText);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateChatData(final String user_chatWith){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_CHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put("user_chatwith", user_chatWith);
                params.put("is_read", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Chat.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        CircleImageView circleImageView = new CircleImageView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
//        textView.setLayoutParams(lp);

        if (type == 1) {
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(0, 0, 0, 10);
            lp1.gravity = Gravity.END;
            textView.setLayoutParams(lp1);

            textView.setBackgroundResource(R.mipmap.rounded_corner1);
            textView.setElevation(3);
            textView.setPadding(45, 25, 45, 25);
            textView.setTextSize(18);
            layout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);

        } else {
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(0, 0, 0, 10);
            lp2.gravity = Gravity.START;

            textView.setLayoutParams(lp2);
            textView.setBackgroundResource(R.mipmap.rounded_corner2);
            textView.setElevation(3);
            textView.setPadding(45, 25, 45, 25);
            textView.setTextSize(18);

            layout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);
        }

        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addTimeBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);

        if (type == 1) {
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(0, 0, 0, 10);
            lp1.gravity = Gravity.END;
            textView.setLayoutParams(lp1);
            textView.setElevation(0);
            textView.setPadding(15, 5, 15, 5);
            textView.setTextSize(12);
            layout.addView(textView);

        } else {
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(0, 0, 0, 10);
            lp2.gravity = Gravity.START;
            textView.setLayoutParams(lp2);
            textView.setElevation(0);
            textView.setPadding(15, 5, 15, 15);
            textView.setTextSize(12);
            layout.addView(textView);
        }
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Chat.this, Chat_Inbox.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
