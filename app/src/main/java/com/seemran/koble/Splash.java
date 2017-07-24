package com.seemran.koble;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    //global vars go here

    private int SPLASH_LENGTH = 3000; //This is the time in milliseconds for splash screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeStatusBarColor();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, Signin.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_LENGTH); // This comes from above.
        // new Handler().postDelayed(runnable, length);
        // That is the format


    }

    private void changeStatusBarColor() { // htis is a function (private)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // this checks the version of android user running. before material we couldn't change the color of status bar
            Window window = getWindow(); // if lollipop and above, that is material then take the status bar and set its color to transparent
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
    }
}


