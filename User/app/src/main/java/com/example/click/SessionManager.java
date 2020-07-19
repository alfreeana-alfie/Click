package com.example.click;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE_NO = "PHONE_NO";
    private static final String ADDRESS = "ADDRESS";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String GENDER = "GENDER";
    public static final String ID = "ID";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email,String phone_no, String address, String birthday, String gender, String id){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(PHONE_NO, phone_no);
        editor.putString(ADDRESS, address);
        editor.putString(BIRTHDAY, birthday);
        editor.putString(GENDER, gender);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggin()){
            Intent intent = new Intent(context, Activity_Main.class);
            context.startActivity(intent);
            ((Activity_Home)context).finish();
        }
    }

    public void checkLogin_Edit(){
        if(!this.isLoggin()){
            Intent intent = new Intent(context, Activity_Main.class);
            context.startActivity(intent);
            ((Activity_Edit_Item)context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE_NO, sharedPreferences.getString(PHONE_NO, null));
        user.put(ADDRESS, sharedPreferences.getString(ADDRESS, null));
        user.put(BIRTHDAY, sharedPreferences.getString(BIRTHDAY, null));
        user.put(GENDER, sharedPreferences.getString(GENDER, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, Activity_Main.class);
        context.startActivity(intent);
        ((Activity_Home)context).finish();
    }

    public void logout_screen(){
        editor.clear();
        editor.commit();
        ((Activity_Home)context).finish();
    }

}
