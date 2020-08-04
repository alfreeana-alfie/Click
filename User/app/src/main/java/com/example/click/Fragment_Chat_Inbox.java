package com.example.click;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.adapter.User_Adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Fragment_Chat_Inbox extends Fragment {

    public static String URL = "https://click-1595830894120.firebaseio.com/users.json";
    User user;
    private RecyclerView recyclerView;
    private TextView noUsersText;
    private List<User> usersArrayList;
    private User_Adapter user_adapter;
    private int totalUsers = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist, container, false);
        recyclerView = view.findViewById(R.id.usersList);
        usersArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noUsersText = view.findViewById(R.id.noUsersText);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doOnSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        return view;
    }

    private void doOnSuccess(final String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key;

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {
                    Toast.makeText(getContext(), obj.getJSONObject(key).get("photo").toString(), Toast.LENGTH_SHORT).show();
                    user = new User(key, obj.getJSONObject(key).get("photo").toString());
                    usersArrayList.add(user);
                    user_adapter = new User_Adapter(getContext(), usersArrayList);
                }
                totalUsers++;
//                Toast.makeText(getContext(),user.getUsername(), Toast.LENGTH_SHORT).show();
            }
            recyclerView.setAdapter(user_adapter);
            user_adapter.setOnItemClickListener(new User_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    User user = usersArrayList.get(position);
                    UserDetails.chatWith = user.getUsername();
                    startActivity(new Intent(getContext(), Chat.class));
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
}
