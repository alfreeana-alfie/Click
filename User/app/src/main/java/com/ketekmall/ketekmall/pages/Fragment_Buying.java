package com.ketekmall.ketekmall.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.SessionManager;
import com.ketekmall.ketekmall.pages.buyer.Chat_Inbox;
import com.ketekmall.ketekmall.pages.buyer.MyBuying;
import com.ketekmall.ketekmall.pages.buyer.MyLikes;
import com.ketekmall.ketekmall.user.Edit_Profile;

import java.util.HashMap;

public class Fragment_Buying extends Fragment {
    String getId;
    SessionManager sessionManager;

    RelativeLayout Mylikes, Buying, MyRating,
            AccountSettings, HelpCentre, ChatInbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mybuying, container, false);
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
                Intent likes = new Intent(getContext(), MyLikes.class);
                startActivity(likes);
            }
        });

        Buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buy = new Intent(getContext(), MyBuying.class);
                startActivity(buy);
            }
        });


        MyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent likes = new Intent(getContext(), com.ketekmall.ketekmall.pages.buyer.MyRating.class);
                startActivity(likes);
            }
        });

        AccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(getContext(), Edit_Profile.class);
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

        ChatInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(getContext(), Chat_Inbox.class);
                startActivity(account);
            }
        });
    }

    private void Declare(View view){

        Mylikes = view.findViewById(R.id.mylikes);
        Buying = view.findViewById(R.id.buying);
        MyRating = view.findViewById(R.id.myrating);
        AccountSettings = view.findViewById(R.id.accountsettings);
        HelpCentre = view.findViewById(R.id.helpcentre);
        ChatInbox = view.findViewById(R.id.Chat);
    }
}
