package com.ketekmall.ketekmall.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ketekmall.ketekmall.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity {

    Animation topAnim, topAnim_02;
    TextView welcome_text, to_click_text;
    Timer timer;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        String s1 = sh.getString("lang", "");

        if(s1.equals("en")){
            String languageToLoad1 = "en"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.apply();
        }else{
            String languageToLoad1 = "ms"; // your language
            Locale locale1 = new Locale(languageToLoad1);
            Locale.setDefault(locale1);
            Configuration config1 = new Configuration();
            config1.locale = locale1;
            getBaseContext().getResources().updateConfiguration(config1,
                    getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences lang1 = getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor1 = lang1.edit();
            editor1.putString("lang", languageToLoad1);
            editor1.apply();


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Declare();
        Animate();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, 2500);
    }

    private void Declare() {
        welcome_text = findViewById(R.id.welcome_text);
        to_click_text = findViewById(R.id.to_click_text);
    }

    private void Animate() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        topAnim_02 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        topAnim_02.setStartOffset(1000);

        welcome_text.setAnimation(topAnim);
        to_click_text.setAnimation(topAnim_02);

    }
}
