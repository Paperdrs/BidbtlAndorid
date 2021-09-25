package com.android.bidbatl.Activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
//import android.support.design.widget.TextInputEditText;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;

import com.android.bidbatl.Utility.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Model.Profile;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.FileAPIManager;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.util.TypedValue.TYPE_NULL;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener {
    private Gson gson;
    TextView inputEditTextName;
    TextView inputEditTextEmail;
    TextView inputEditTextPhone;
    TextView inputEditTextBusinessName;
    TextView defaultAddress;
    private final int CAMERA_PIC_REQUEST_INVOICE = 101;
    CircleImageView imageViewUser;
    Button buttonEditProfile;
    Profile.UserProfile profile;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initToolBar();
        initViews();
        initGSON();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("My Profile");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }

    private void initViews() {
        inputEditTextName = findViewById(R.id.textView_profile_name);
        inputEditTextEmail = findViewById(R.id.textView_profile_email);
        inputEditTextPhone = findViewById(R.id.textView_profile_phone);
        inputEditTextBusinessName = findViewById(R.id.textView_profile_bus_name);
        defaultAddress = findViewById(R.id.textView_profile_address);
        buttonEditProfile = findViewById(R.id.button_edit_profile);
        buttonEditProfile.setOnClickListener(this);
        imageViewUser = findViewById(R.id.imageView_user);
        Glide.with(this).load(PowerPreference.getDefaultFile().getString("photo")).placeholder(R.drawable.user_icon_placeholder).into(imageViewUser);

    }

    private void initGSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void setUserData(Profile.UserProfile profile) {
        this.profile = profile;
        inputEditTextName.setText(profile.name);
        inputEditTextPhone.setText(profile.phone);
        inputEditTextBusinessName.setText(profile.registered_name);
        inputEditTextEmail.setText(profile.email);
        defaultAddress.setText(profile.address);
        PowerPreference.getDefaultFile().set("name",profile.name);
        PowerPreference.getDefaultFile().set("photo", profile.photo);
        if (profile.photo != null) {
            url = profile.photo;
            Glide.with(this)
                    .load(Uri.parse(String.valueOf(url))).placeholder(R.drawable.user_icon_placeholder)
                    .into(imageViewUser);
        }

    }

    private void getUserDetail() {
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_USER, new HashMap<String, String>(), getApplicationContext(), new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                Profile profile = gson.fromJson(result, Profile.class);
                if (profile.code == 200) {
                    setUserData(profile.userProfile);
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEditProfile) {
            Intent intent = new Intent(UserDetailActivity.this, EditProfileActivity.class);
            intent.putExtra("profile", profile);
            startActivity(intent);
        }
    }

}
