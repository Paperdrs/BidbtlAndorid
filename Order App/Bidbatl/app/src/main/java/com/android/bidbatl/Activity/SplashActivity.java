package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.android.bidbatl.Activity.product.MainActivity;
import com.android.bidbatl.R;
import com.preference.PowerPreference;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PowerPreference.getDefaultFile().set("motherPackId","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (PowerPreference.getDefaultFile().getString("user_id").isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
