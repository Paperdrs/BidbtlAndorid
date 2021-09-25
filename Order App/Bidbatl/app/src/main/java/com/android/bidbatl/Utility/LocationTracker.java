package com.android.bidbatl.Utility;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class LocationTracker {
    private Activity activity;
    public boolean isGps;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Location loc;
    private double wayLatitude, wayLongitude;
    CoordinateSender coordinateSender;

    public LocationTracker(Activity activity, CoordinateSender coordinateSender) {
        this.activity = activity;
        this.coordinateSender = coordinateSender;
        fusedLocationProviderClient = new FusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        new GpsUtils(activity).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGps = isGPSEnable;
            }
        });

        setUpLocationCallBack();
    }


    @SuppressLint("MissingPermission")
    private void setUpLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        coordinateSender.getCoordinates(wayLatitude, wayLongitude);
                        if (fusedLocationProviderClient != null) {
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        if (!isGps) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (isGps) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        loc = location;
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        coordinateSender.getCoordinates(wayLatitude, wayLongitude);
                    } else {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            }).addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "Can't fetch current loaction.", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    public interface CoordinateSender {
        void getCoordinates(double lat, double longi);
    }


}
