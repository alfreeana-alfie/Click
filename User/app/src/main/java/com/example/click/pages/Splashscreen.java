package com.example.click.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity {

    Animation topAnim, topAnim_02;
    TextView welcome_text, to_click_text;
    Timer timer;

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
