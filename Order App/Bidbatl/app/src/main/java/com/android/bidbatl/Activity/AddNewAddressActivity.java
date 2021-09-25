package com.android.bidbatl.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.bidbatl.Utility.ConnectionDetector;
import com.android.bidbatl.Utility.LocationTracker;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener {
    ConstraintLayout useCurrentLoclayout;
    TextInputEditText txtName;
    TextInputEditText txtMobile, txtHouseNo, txtPincode, txtLandmark, txtCity, txtState;
    Button btnSubmitAddress;
    private Gson gson;
    CheckBox checkBoxDefault,checkBoxSameAddress;
    List<Address> addresses;
    String latt = "", longt = "";
    Boolean isEditingAddres = false;
    String addressType;
    int isAddressSame = 0;
    Boolean isUpdated = false;

    com.android.bidbatl.Model.Address.AddressList addressData;

    private boolean mLocationPermissionGranted;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        initToobar();
        initGSON();
        initViews();

        addressData = (com.android.bidbatl.Model.Address.AddressList) getIntent().getSerializableExtra("editAddress");
        if (addressData != null) {
            isEditingAddres = true;
            fillvalues();
            checkBoxDefault.setVisibility(View.GONE);
            checkBoxSameAddress.setVisibility(View.GONE);
        }
        else {
            addressType = getIntent().getStringExtra("addresstype");
            if (addressType != null){
                if (addressType.equalsIgnoreCase("billing")) {
                    checkBoxSameAddress.setText("Same as Shipping Address");
                } else {
                    checkBoxSameAddress.setText("Same as Billing Address");
                }
            }
            else {
                addressType = "shipping";
            }

        }
        getCurrentLatLong();
    }

    private void fillvalues() {
        txtHouseNo.setText(addressData.address);
        txtPincode.setText(addressData.zip_code);
        txtCity.setText(addressData.city);
        txtState.setText(addressData.state);
        txtLandmark.setText(addressData.area);
         txtName.setText(addressData.name);
        txtMobile.setText(addressData.phone);

    }

    private void useCurrentLocation() {

        if (addresses != null) {
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            txtHouseNo.setText(address.getAddressLine(0));
            txtPincode.setText(address.getPostalCode());
            txtCity.setText(address.getLocality());
            txtState.setText(address.getAdminArea());
        }
    }
    }

    private void initGSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Add New Address");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }

    private void getCurrentLatLong() {
        LocationTracker locationTracker = new LocationTracker(AddNewAddressActivity.this, new LocationTracker.CoordinateSender() {
            @Override
            public void getCoordinates(double lat, double longi) {
                if (lat != 0.0 && longi != 0.0) {
                    latt = lat + "";
                    longt = longi + "";
                    Log.i("LATT", lat + "");
                    //26.8467° N, 80.9462° E
                    Geocoder geocoder = new Geocoder(AddNewAddressActivity.this, Locale.ENGLISH);
                    try {
                        addresses = geocoder.getFromLocation(lat, longi, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        locationTracker.getLocation();
    }

    private void initViews() {
        useCurrentLoclayout = findViewById(R.id.useCurrentLoc);
        useCurrentLoclayout.setOnClickListener(this);
        txtName = findViewById(R.id.name);
        txtMobile = findViewById(R.id.textMobileNumber);
        txtHouseNo = findViewById(R.id.textHouseNo);
        txtPincode = findViewById(R.id.textPincode);
        txtLandmark = findViewById(R.id.textLandmark);
        txtCity = findViewById(R.id.textCity);
        txtState = findViewById(R.id.textState);
        txtName = findViewById(R.id.name);
        btnSubmitAddress = findViewById(R.id.buttonSubmitAddress);
        checkBoxDefault = findViewById(R.id.checkbox_default);
        checkBoxSameAddress = findViewById(R.id.checkbox_same_address);
        btnSubmitAddress.setOnClickListener(this);


    }

    private boolean validateAddress() {
        if (txtName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtMobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtHouseNo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtPincode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Area Pincode", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtCity.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your City", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtState.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your State", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void addAddress() {
        if (validateAddress()) {
            String apiName = "";
            showDialog("Loading...");
            int defaultType = checkBoxDefault.isChecked() ? 1 : 0;
            HashMap paaram = new HashMap();

            paaram.put("latitude", latt);
            paaram.put("logitude", longt);
            paaram.put("address", txtHouseNo.getText().toString());
            paaram.put("area", "");
            paaram.put("state", txtState.getText().toString());
            paaram.put("city", txtCity.getText().toString());
            paaram.put("zip_code", txtPincode.getText().toString());

            paaram.put("name",txtName.getText().toString());
            paaram.put("phone",txtMobile.getText().toString());
            if (isEditingAddres){
                paaram.put("address_id",addressData.id);
                apiName = "updateAddress";

            }
            else {
                paaram.put("type", addressType);
                paaram.put("is_default", defaultType);
                paaram.put("is_deleted", "0");
                apiName = Constants.ADD_ADDRESS;
            }
            Log.d("add", paaram.toString());

            APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + apiName, paaram, this, new APIManager.APICallbackInterface() {
                @Override
                public void onSuccessFinished(String result) {

                    if (isEditingAddres){
                        finish();
                        Toast.makeText(getApplicationContext(), "Address Updated Successfully", Toast.LENGTH_LONG).show();
                    dismissDialog();

                    }
                    else {
                            if (addressType.equalsIgnoreCase("billing")) {
                                addressType = "shipping";
                                addAddress();
                            }

                         else {
                            finish();
                            Toast.makeText(getApplicationContext(), "Address Saved Successfully", Toast.LENGTH_LONG).show();
                        }
                        dismissDialog();
                    }


                }

                @Override
                public void onErrorFinished(String result) {
                    dismissDialog();

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmitAddress) {
            addAddress();

        } else if (v == useCurrentLoclayout) {
            useCurrentLocation();

        }

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getCurrentLatLong();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getCurrentLatLong();
                } else {
                    Toast.makeText(this, "Location Permission Denied.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                return;
            }

            case Constants.LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLatLong();
                } else {
                    Toast.makeText(this, "Location Permission Denied.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                return;
            }

        }
    }
}
