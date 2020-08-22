package com.example.click;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click.data.MySingleton;
import com.example.click.data.SessionManager;
import com.example.click.pages.Homepage;
import com.example.click.pages.Saved_Searches_Other;
import com.example.click.user.Edit_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buying_Other_Frag extends Fragment {
    String getId;
    SessionManager sessionManager;

    RelativeLayout Mylikes, Buying, MyRating,
            AccountSettings, HelpCentre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buying_frag, container, false);
        Declare(view);
        GotoPage();
        sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        return view;
    }

    private void GotoPage(){
        Mylikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent likes = new Intent(getContext(), Saved_Searches_Other.class);
                likes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(likes);
            }
        });

        Buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buy = new Intent(getContext(), Buying.class);
                buy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(buy);
            }
        });


        MyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent likes = new Intent(getContext(), MyRating.class);
                likes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(likes);
            }
        });

        AccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(getContext(), Edit_Profile.class);
                account.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(account);
            }
        });

        HelpCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://ketekmall.com/"));
                startActivity(intent1);
            }
        });
    }

    private void Declare(View view){

        Mylikes = view.findViewById(R.id.mylikes);
        Buying = view.findViewById(R.id.buying);
        MyRating = view.findViewById(R.id.myrating);
        AccountSettings = view.findViewById(R.id.accountsettings);
        HelpCentre = view.findViewById(R.id.helpcentre);
    }
}
