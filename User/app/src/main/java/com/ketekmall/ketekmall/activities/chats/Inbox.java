package com.ketekmall.ketekmall.activities.chats;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.adapters.ChatListAdapter;
import com.ketekmall.ketekmall.configs.Setup;
import com.ketekmall.ketekmall.models.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.*;

import java.util.*;

import static com.ketekmall.ketekmall.configs.Constant.hideSoftKeyboard;
import static com.ketekmall.ketekmall.configs.Link.*;

public class Inbox extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noUsersText;
    List<ChatSession> usersArrayList;
    String getId;
    SessionManager sessionManager;
    BottomNavigationView bottomNav;

    LinearLayout parent;
    ChatListAdapter chat_adapter;
    Setup setup;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_inbox);
        ToolbarSetting();
        setup = new Setup(this);
        getId = setup.getUserId();

        BottomNavigationSettings();

        recyclerView = findViewById(R.id.usersList);
        usersArrayList = new ArrayList<ChatSession>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Inbox.this));
        noUsersText = findViewById(R.id.noUsersText);

        parent = findViewById(R.id.parent);
        setupUI(parent);

        getChat();

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Inbox.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void getChat (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CHAT,
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

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CHAT_IS_READ,
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

                                                                    chat_adapter = new ChatListAdapter(Inbox.this, usersArrayList);
                                                                    recyclerView.setAdapter(chat_adapter);
                                                                    chat_adapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
                                                                        @Override
                                                                        public void onItemClick(int position) {
                                                                            ChatSession chatSession = usersArrayList.get(position);

                                                                            Intent chatIntent = new Intent(Inbox.this, ChatList.class);
                                                                            chatIntent.putExtra("Name", chatSession.getName());
                                                                            chatIntent.putExtra("UserPhoto", chatSession.getUserPhoto());
                                                                            chatIntent.putExtra("ChatWith", chatSession.getChatWith());
                                                                            chatIntent.putExtra("ChatWithID", chatSession.getChatWithID());
                                                                            chatIntent.putExtra("ChatWithPhoto", chatSession.getChatWithPhoto());

                                                                            startActivity(chatIntent);
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(Inbox.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Inbox.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Inbox.this, Me.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

//    private void getSession() {
//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        getId = user.get(SessionManager.ID);
//    }

    public void BottomNavigationSettings(){
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(Inbox.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(Inbox.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(Inbox.this, Me.class);
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
        Intent intent = new Intent(Inbox.this, Me.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
    }
}
