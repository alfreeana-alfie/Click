package com.ketekmall.ketekmall.configs;

import android.content.Context;

import com.ketekmall.ketekmall.models.SessionManager;

import java.util.HashMap;

public class Setup {
    Context context;

    SessionManager sessionManager;
    String getId;

    public Setup(Context context){
        this.context = context;
    }

    public String getUserId(){
        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        return getId;
    }

}
