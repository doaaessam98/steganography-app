package com.example.steganography;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.steganography.login.Login;

public class Splash extends AppCompatActivity {
    ImageView image;
    TextView text;
    Animation topAnim;
    Animation bottomAnim;
    Animation topRotet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image = findViewById(R.id.image_splash);
        text = findViewById(R.id.text_splash);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animatiom);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image.setAnimation(topAnim);

        text.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, Login.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, 2000);

    }
}
