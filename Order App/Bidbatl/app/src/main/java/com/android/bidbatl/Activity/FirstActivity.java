package com.android.bidbatl.Activity;

import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.bidbatl.R;
import com.android.bidbatl.Utility.ConnectionDetector;
import com.android.bidbatl.Utility.LocationTracker;
import com.android.bidbatl.Utility.Permission;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
TextView textViewSignIn;
TextView textViewSignUp;

    private boolean mLocationPermissionGranted;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    ConnectionDetector cd = new ConnectionDetector(FirstActivity.this);
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    LocationTracker locationTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initViews();
        getLocationPermission();

    }
    void initViews(){
        textViewSignIn = findViewById(R.id.textView_signin);
        textViewSignUp = findViewById(R.id.textView_signup);
        textViewSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this, new String[]{FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == textViewSignIn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (v == textViewSignUp){
            Intent intent = new Intent(this,SignupActivity.class);
            startActivity(intent);

        }

    }
}
