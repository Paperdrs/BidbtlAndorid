package com.android.bidbatl.Utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utility {
    private static final Utility ourInstance = new Utility();
    private static DecimalFormat df2 = new DecimalFormat("#");

    public static synchronized Utility getInstance() {
        return ourInstance;
    }

    double sourceLat;
    double soourceLongt;

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSoourceLongt() {
        return soourceLongt;
    }

    public void setSoourceLongt(double soourceLongt) {
        this.soourceLongt = soourceLongt;
    }

    private Utility() {
    }

    public static  boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public static String formatDate(String datei){
        // Note, MM is months, not mm
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Date date = null;
        try {
            date = inputFormat.parse(datei);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date);
        return  outputText;

    }

    public  String distance(double lat2, double lon2) {
        Location location = new Location("");
        location.setLatitude(Utility.getInstance().getSourceLat());
        location.setLongitude(Utility.getInstance().getSoourceLongt());

        Location location1 = new Location("");
        location1.setLongitude(lon2);
        location1.setLatitude(lat2);

//        double theta = Utility.getInstance().soourceLongt - lon2;
//        double dist = Math.sin(deg2rad(Utility.getInstance().sourceLat))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(Utility.getInstance().sourceLat))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
        return df2.format((location.distanceTo(location1)/1000.0));
    }

    private  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private  double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void getLatLongFromAddress(Context context) {
        Geocoder geoCoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geoCoder.getFromLocationName("Block B, Sector 11, Faridabad, Haryana 121006", 5);
            if (addresses.size() >= 0) {
                Utility.getInstance().setSoourceLongt((addresses.get(0).getLongitude()));
                Utility.getInstance().setSourceLat((addresses.get(0).getLatitude()));
                Log.d("Latitude is:", "" + (addresses.get(0).getLatitude()));
                Log.d("Longitude is:", "" + (addresses.get(0).getLongitude()));
            }
        } catch (Exception e) {

        }
    }
}