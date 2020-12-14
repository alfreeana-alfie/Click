package com.ketekmall.ketekmall.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import com.ketekmall.ketekmall.pages.Homepage;
import com.ketekmall.ketekmall.pages.MainActivity;

public class SessionManager {

    public static final String ID = "ID";
    private static final String LOGIN = "IS_LOGIN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE_NO = "PHONE_NO";
    private static final String ADDRESS01 = "ADDRESS01";
    private static final String ADDRESS02 = "ADDRESS02";
    private static final String CITY = "DIVISION";
    private static final String POSTCODE = "POSTCODE";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String GENDER = "GENDER";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String phone_no, String address01, String address02, String city, String postcode, String birthday, String gender, String id) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(PHONE_NO, phone_no);
        editor.putString(ADDRESS01, address01);
        editor.putString(ADDRESS02, address02);
        editor.putString(CITY, city);
        editor.putString(POSTCODE, postcode);
        editor.putString(BIRTHDAY, birthday);
        editor.putString(GENDER, gender);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent intent = new Intent(context, Homepage.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE_NO, sharedPreferences.getString(PHONE_NO, null));
        user.put(ADDRESS01, sharedPreferences.getString(ADDRESS01, null));
        user.put(ADDRESS02, sharedPreferences.getString(ADDRESS02, null));
        user.put(CITY, sharedPreferences.getString(CITY, null));
        user.put(POSTCODE, sharedPreferences.getString(POSTCODE, null));
        user.put(BIRTHDAY, sharedPreferences.getString(BIRTHDAY, null));
        user.put(GENDER, sharedPreferences.getString(GENDER, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Homepage) context).finish();
    }

}
