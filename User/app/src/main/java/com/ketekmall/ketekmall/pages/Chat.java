package com.ketekmall.ketekmall.pages;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.Chat_Detail;
import com.ketekmall.ketekmall.data.MySingleton;
import com.ketekmall.ketekmall.data.UserDetails;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";

    private static String URL_ADD_CHAT = "https://ketekmall.com/ketekmall/add_chat.php";
    private static String URL_EDIT_CHAT = "https://ketekmall.com/ketekmall/edit_chat.php";


    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    BottomNavigationView bottomNav;
    List<Chat_Detail> chatDetailList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.user_actionbar);

        View view = getSupportActionBar().getCustomView();
        TextView chatname = view.findViewById(R.id.user_chatname);
        final CircleImageView circleImageView = view.findViewById(R.id.profile_image);

        chatname.setText(UserDetails.chatWith1);

        chatDetailList = new ArrayList<>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String url = "https://click-1595830894120.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    TOPIC = obj.getJSONObject(UserDetails.chatWith).get("token").toString();
                    Picasso.get().load(obj.getJSONObject(UserDetails.chatWith).get("photo").toString()).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(Chat.this);
        rQueue.add(request);

        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

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

        String newemail = UserDetails.email;
        if(newemail.contains("@")){
            newemail = UserDetails.email.substring(0, UserDetails.email.lastIndexOf("@"));
        }else {
            newemail = UserDetails.email;
        }
        
        Firebase.setAndroidContext(this);

        Log.d("message", newemail);
        final String ref1 = newemail + "_" + UserDetails.chatWith;
        final String ref2 = UserDetails.chatWith + "_" + newemail;

        UpdateChatData(ref1);

        reference1 = new Firebase("https://click-1595830894120.firebaseio.com/messages/" + newemail + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://click-1595830894120.firebaseio.com/messages/" + UserDetails.chatWith + "_" + newemail);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageArea.getText().toString();
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("time", currentTime);
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    String url = "https://click-1595830894120.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject obj = new JSONObject(s);

                                TOPIC = obj.getJSONObject(UserDetails.chatWith1).get("token").toString();
                                Log.d("CHAT", TOPIC);
                                NOTIFICATION_TITLE = "KetekMall";
                                NOTIFICATION_MESSAGE = UserDetails.username + ": " + messageText;

                                JSONObject notification = new JSONObject();
                                JSONObject notifcationBody = new JSONObject();
                                try {
                                    notifcationBody.put("title", NOTIFICATION_TITLE);
                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                    notification.put("to", TOPIC);
                                    notification.put("data", notifcationBody);
                                    sendNotification(notification);

                                    Log.d(TAG, "onCreate: " + NOTIFICATION_MESSAGE + NOTIFICATION_TITLE);
                                } catch (JSONException e) {
                                    Log.e(TAG, "onCreate: " + e.getMessage());
                                }
                                Picasso.get().load(obj.getJSONObject(UserDetails.chatWith).get("photo").toString()).into(circleImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Chat.this);
                    rQueue.add(request);

                    ChatData(ref1, messageText);
                    ChatData(ref2, messageText);
                }
            }
        });


        reference1.orderByChild("time").addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String time = map.get("time").toString();

                Chat_Detail chat_detail = new Chat_Detail(message, userName, time);
                chatDetailList.add(chat_detail);

                if (chat_detail.getUsera().equals(UserDetails.username)) {
                    addMessageBox(chat_detail.getMessages(), 1);
                    addTimeBox(chat_detail.getTimea(), 1);
                    messageArea.setText("");
                } else {
                    addMessageBox(chat_detail.getMessages(), 2);
                    addTimeBox(chat_detail.getTimea(), 2);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Chat.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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

            textView.setBackgroundResource(R.drawable.rounded_corner1);
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
            textView.setBackgroundResource(R.drawable.rounded_corner2);
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
        finish();
    }
}
