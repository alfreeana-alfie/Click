package com.example.click;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.click.data.SessionManager;

import java.util.HashMap;

public class MyIncome extends AppCompatActivity {
    TextView sold;

    String getId;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);
        Declare();
        getSession();
        getIncome();

    }

    private void getIncome() {

    }

    private void getSession() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }

    private void Declare() {
        sold = findViewById(R.id.sold_text);
    }
}
