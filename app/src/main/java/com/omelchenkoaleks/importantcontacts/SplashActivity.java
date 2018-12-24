package com.omelchenkoaleks.importantcontacts;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash;
    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash = findViewById(R.id.tv_splash);
        iv_splash = findViewById(R.id.iv_splash);

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.my_transition);
        tv_splash.startAnimation(myAnim);
        iv_splash.startAnimation(myAnim);

        final Intent intent = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
