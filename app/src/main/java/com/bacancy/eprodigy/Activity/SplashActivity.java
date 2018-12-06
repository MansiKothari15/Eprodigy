package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.utils.Pref;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String verified = Pref.getValue(SplashActivity.this,"verified","");
                if(verified.equals("1")) {
                    Intent i = new Intent(SplashActivity.this, MessagingActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this, MobileRegistrationActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }
}
