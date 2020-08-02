package com.example.click;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, reference1_other, reference2_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(UserDetails.chatWith);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_All_View.class));
            }
        });


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
                String messageText = messageArea.getText().toString();
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("time", currentTime);
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
//                    reference1_other.child("chatWith").setValue(UserDetails.chatWith);
                    reference2.push().setValue(map);
//                    reference2_other.child("chatWith").setValue(UserDetails.username);

                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
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
            textView.setPadding(45, 25, 45 ,25);
            textView.setTextSize(16);
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
            textView.setPadding(45, 25, 45 ,25);
            textView.setTextSize(16);

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
            textView.setPadding(15, 5, 15 ,5);
            textView.setTextSize(12);
            layout.addView(textView);

        } else {
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(0, 0, 0, 10);
            lp2.gravity = Gravity.START;
            textView.setLayoutParams(lp2);
            textView.setElevation(0);
            textView.setPadding(15, 5, 15 ,15);
            textView.setTextSize(12);
            layout.addView(textView);
        }
//        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}