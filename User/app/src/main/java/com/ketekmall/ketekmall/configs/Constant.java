package com.ketekmall.ketekmall.configs;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

public class Constant {
    public static String host = "https://ketekmall.com/ketekmall/";
    public static final String ONESIGNAL_APP_ID = "6236bfc3-df4d-4f44-82d6-754332044779";

    public static final int RC_SIGN_IN = 1;
    public static Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");

    public static int PICK_IMAGE_REQUEST = 22;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }
}
