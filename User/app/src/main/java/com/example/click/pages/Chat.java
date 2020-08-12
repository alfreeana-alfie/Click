package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.data.MySingleton;
import com.example.click.data.UserDetails;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1e9WIaM:APA91bGoWyt9jVnxE08PH2SzgIqh2VgOOolPPBy_uGVkrNV7q8E-1ecG3staHzI73jDzygIisGIRG2XbxzBBQBVRf-rU-qSNb8Fu0Lwo3JDlQtmNrsIvGSec5V3ANVFyR3jcGhgEduH7";
    final private String contentType = "application/json";
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, reference1_other, reference2_other;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.user_actionbar);

        View view = getSupportActionBar().getCustomView();
        TextView chatname = view.findViewById(R.id.user_chatname);
        final CircleImageView circleImageView = view.findViewById(R.id.profile_image);

        chatname.setText(UserDetails.chatWith);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Homepage.class));
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

        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://click-1595830894120.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference1_other = new Firebase("https://click-1595830894120.firebaseio.com/users/" + UserDetails.username);
        reference2 = new Firebase("https://click-1595830894120.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        reference2_other = new Firebase("https://click-1595830894120.firebaseio.com/users/" + UserDetails.chatWith);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageArea.getText().toString();
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("time", currentTime);
                    map.put("message", messageText);
                    map.put("user_actionbar", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    String url = "https://click-1595830894120.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject obj = new JSONObject(s);

                                TOPIC = obj.getJSONObject(UserDetails.chatWith).get("token").toString();
                                NOTIFICATION_TITLE = UserDetails.username;
                                NOTIFICATION_MESSAGE = messageText;

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

                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user_actionbar").toString();
                String time = map.get("time").toString();

                if (userName.equals(UserDetails.username)) {
                    addMessageBox(message, 1);
                    addTimeBox(time, 1);
                    messageArea.setText("");
                } else {
                    addMessageBox(message, 2);
                    addTimeBox(time, 2);
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

//            LinearLayout lp3 = new LinearLayout(Chat.this);
//            lp3.setOrientation(LinearLayout.HORIZONTAL);
//            lp3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            textView.setLayoutParams(lp2);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            textView.setElevation(3);
            textView.setPadding(45, 25, 45, 25);
            textView.setTextSize(18);


//            circleImageView.setLayoutParams(new LinearLayout.LayoutParams(170, 170));
//            circleImageView.setImageURI(Uri.parse(UserDetails.photo));
//            circleImageView.setElevation(3);
//            circleImageView.setPadding(0, 0, 0 ,0);
//
//            lp3.addView(circleImageView);
//            lp3.addView(textView);

            layout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);
        }

        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void addTimeBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
//        textView.setLayoutParams(lp);

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
//        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().getBackStackEntryCount();
    }
}
