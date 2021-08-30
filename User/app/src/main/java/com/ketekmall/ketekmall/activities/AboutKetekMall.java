package com.ketekmall.ketekmall.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.activities.main.Home;
import com.ketekmall.ketekmall.activities.main.Me;
import com.ketekmall.ketekmall.activities.main.Notification;
import com.ketekmall.ketekmall.activities.policies.AppVersion;
import com.ketekmall.ketekmall.activities.policies.Contact_Us;
import com.ketekmall.ketekmall.activities.policies.DeliveryPolicy;
import com.ketekmall.ketekmall.activities.policies.ReturnRefundPolicy;
import com.ketekmall.ketekmall.activities.policies.TermsAndConditionsOnly;
import com.ketekmall.ketekmall.configs.Setup;

import java.util.Objects;

public class AboutKetekMall extends AppCompatActivity {

    LinearLayout llReturnRefundPolicy, llDeliveryPolicy, llContactUs, llTermsConditions, llAppVersion;

    String getId;
    BottomNavigationView bottomNav;
    Setup setup;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_ketekmall);
        ToolbarSettings();
        Declare();
    }

    private void Declare() {
        setup = new Setup(this);
        getId = setup.getUserId();

        llReturnRefundPolicy = findViewById(R.id.return_refund_layout);
        llDeliveryPolicy = findViewById(R.id.delivery_policy_layout);
        llContactUs = findViewById(R.id.contact_us_layout);
        llTermsConditions = findViewById(R.id.terms_and_conditions_layout);
        llAppVersion = findViewById(R.id.version_layout);

        llReturnRefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutKetekMall.this, ReturnRefundPolicy.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        llDeliveryPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutKetekMall.this, DeliveryPolicy.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutKetekMall.this, Contact_Us.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        llTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutKetekMall.this, TermsAndConditionsOnly.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        llAppVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutKetekMall.this, AppVersion.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent4 = new Intent(AboutKetekMall.this, Home.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        break;

                    case R.id.nav_noti:
                        Intent intent6 = new Intent(AboutKetekMall.this, Notification.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent6);
                        break;

                    case R.id.nav_edit_profile:
                        Intent intent1 = new Intent(AboutKetekMall.this, Me.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ToolbarSettings() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.about_ketekMall));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
