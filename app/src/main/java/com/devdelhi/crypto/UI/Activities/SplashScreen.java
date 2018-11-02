package com.devdelhi.crypto.UI.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.devdelhi.crypto.R;

public class SplashScreen extends AppCompatActivity {

    TextView appNameTV;
    TextView devdelhiTV;
    TextView whiteFadeBackground;
    ImageView appicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appNameTV = findViewById(R.id.app_name_tv);
        devdelhiTV = findViewById(R.id.devdelhi_name_tv);
        whiteFadeBackground = findViewById(R.id.white_fade_background);
        appicon = findViewById(R.id.app_logo);

        Typeface news_heading_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/news_heading_font.ttf");
        Typeface semibold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/semibold.ttf");
        appNameTV.setTypeface(news_heading_font);
        devdelhiTV.setTypeface(semibold);

        appicon.setAlpha(0f);
        appNameTV.setAlpha(0f);
        devdelhiTV.setAlpha(0f);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                appicon.animate().alpha(1f).setDuration(500);
            }
        }, 0);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                appNameTV.animate().alpha(1f).setDuration(500);
            }
        }, 500);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                devdelhiTV.animate().alpha(1f).setDuration(500);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 1500);

    }
}
