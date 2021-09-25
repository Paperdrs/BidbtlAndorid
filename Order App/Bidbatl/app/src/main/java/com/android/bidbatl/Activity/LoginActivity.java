package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Model.Login;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    Button btnLogin;
    ConstraintLayout layoutSignup;
    TextView textViewForgotPassword;
    TextInputEditText txtMobileNumber;
    TextInputEditText txtPassword;
    private Gson gson;
Login loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        btnLogin = findViewById(R.id.button_login);
        layoutSignup = findViewById(R.id.layout_signup);
        btnLogin.setOnClickListener(this);
        layoutSignup.setOnClickListener(this);
        textViewForgotPassword = findViewById(R.id.textView_forgot_pass);
        textViewForgotPassword.setOnClickListener(this);
        txtMobileNumber = findViewById(R.id.textPhone);
        txtPassword = findViewById(R.id.textPassword);
        txtMobileNumber.setText(PowerPreference.getDefaultFile().getString("phone"));
        initGSON();

    }

    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void callLoginUserAPI(){
        showDialog("Logging...");
        HashMap param = new HashMap();
        param.put("phone",txtMobileNumber.getText().toString());
        param.put("password",txtPassword.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.LOGIN,param,this ,new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                Log.i("resp",result);
                dismissDialog();
                loginData = gson.fromJson(result, Login.class);
                if (loginData.code == 200) {

                    sendOtp();

                    //setSharedPreferencesData(loginData);
                } else {
                    Toast.makeText(LoginActivity.this,loginData.message,Toast.LENGTH_LONG).show();
                    //showToast(loginData.message);
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();

            }
        });
    }
    private void sendOtp(){
        HashMap param = new HashMap();
        param.put("phone",txtMobileNumber.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.SEND_OTP, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        Toast.makeText(getApplicationContext(),dataObject.getString("otp")+"OTP sent successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("loginData",loginData);
                        intent.putExtra("source","login");
                        intent.putExtra("phone",loginData.data.phone);
                        startActivity(intent);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {

            }
        });
    }
    private boolean validateUser(){
        if (txtMobileNumber.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Mobile Number",Toast.LENGTH_LONG).show();
            return false;

        }
        else if (txtPassword.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_LONG).show();
            return  false;

        }
        else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            if (validateUser()){
                callLoginUserAPI();
            }


        } else if (v == layoutSignup) {
            Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(intent);

        }
        else if (v == textViewForgotPassword) {
            Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
            startActivity(intent);

        }
    }
}
