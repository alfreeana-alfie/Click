package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.R;
import com.example.click.adapter.UserAdapter;
import com.example.click.data.User;
import com.example.click.data.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chat_Inbox_Other extends AppCompatActivity {

    public static String URL = "https://click-1595830894120.firebaseio.com/users.json";
    public static String URL_MESSAGE = "https://click-1595830894120.firebaseio.com/messages.json";
    User user;
    RecyclerView recyclerView;
    TextView noUsersText;
    List<User> usersArrayList;
    UserAdapter user_adapter;
    int totalUsers = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        ToolbarSetting();

        recyclerView = findViewById(R.id.usersList);
        usersArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Chat_Inbox_Other.this));
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
        RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox_Other.this);
        requestQueue.add(stringRequest);

        doON();
    }

    private void ToolbarSetting() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.userlist_actionbar);

        View view = getSupportActionBar().getCustomView();
        ImageButton back_button = view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat_Inbox_Other.this, Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void doOnSuccess(final String s) {
        try {
            final JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {

                    user = new User(key, obj.getJSONObject(key).get("photo").toString());
                    usersArrayList.add(user);
                    user_adapter = new UserAdapter(Chat_Inbox_Other.this, usersArrayList);
                }
                totalUsers++;
            }
            recyclerView.setAdapter(user_adapter);
            user_adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    User user = usersArrayList.get(position);
                    UserDetails.chatWith = user.getUsername();
                    startActivity(new Intent(Chat_Inbox_Other.this, Chat.class));
                }
            });

        } catch (JSONException e) {
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

                                if(chat.contains(UserDetails.username + "_")){
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject object =new JSONObject(response);
                                                        Iterator i = object.keys();

                                                        while (i.hasNext()){
                                                            String key = i.next().toString();

                                                            if(!key.equals(UserDetails.username)){
                                                                if (chat.contains(key)){
                                                                    user = new User(key, object.getJSONObject(key).get("photo").toString());
                                                                    usersArrayList.add(user);
                                                                    user_adapter = new UserAdapter(Chat_Inbox_Other.this, usersArrayList);
                                                                }
                                                            }
                                                            totalUsers++;
                                                        }
                                                        recyclerView.setAdapter(user_adapter);
                                                        user_adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(int position) {
                                                                User user = usersArrayList.get(position);
                                                                UserDetails.chatWith = user.getUsername();
                                                                startActivity(new Intent(Chat_Inbox_Other.this, Chat.class));
                                                            }
                                                        });
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

                                                }
                                            });
                                    RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox_Other.this);
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

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(Chat_Inbox_Other.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Chat_Inbox_Other.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
