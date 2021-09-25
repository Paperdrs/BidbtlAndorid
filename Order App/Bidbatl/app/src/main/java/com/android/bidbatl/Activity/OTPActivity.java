package com.android.bidbatl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Activity.product.MainActivity;
import com.android.bidbatl.Model.Login;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OTPActivity extends BaseActivity implements View.OnClickListener {
Button btnVerify;
EditText otp1,otp2,otp3,otp4;
    String phone;
    TextView textViewResendOTP;
    TextView hintMessage;
    TextInputEditText editTextPassword,editTextVerifyPassword,editTextReVerifyMobile;
    TextInputLayout textInputLayoutPassword,textInputLayoutVerifyPassword,verifyLayout;
    Login loginData;
    int failedCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initView();

    }
    private void initView(){
        btnVerify = findViewById(R.id.button_verify);
        btnVerify.setOnClickListener(this);
        otp1 = findViewById(R.id.textOTP1);
        otp2 = findViewById(R.id.textOTP2);
        otp3 = findViewById(R.id.textOTP3);
        otp4 = findViewById(R.id.textOTP4);
        editTextReVerifyMobile = findViewById(R.id.inputEditTextReVerify);
        textViewResendOTP = findViewById(R.id.textView10);
        hintMessage = findViewById(R.id.textView3);

        textInputLayoutPassword = findViewById(R.id.textInputLayout6);
        textInputLayoutVerifyPassword = findViewById(R.id.textInputLayout8);
        verifyLayout = findViewById(R.id.textInputLayout2);

        textViewResendOTP.setOnClickListener(this);
        phone = getIntent().getStringExtra("phone");
        hintMessage.setText("Please type the verification code sent to " + phone);
        editTextPassword = findViewById(R.id.textView_password);
        editTextVerifyPassword = findViewById(R.id.textView_verify_password);
        if (getIntent().getStringExtra("source").equalsIgnoreCase("FP")){
            textInputLayoutPassword.setVisibility(View.VISIBLE);
            textInputLayoutVerifyPassword.setVisibility(View.VISIBLE);
        }
        moveCursor();
    }

    private void moveCursor(){
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1){
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1){
                    hideSoftKeyBoard();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean validateOTP(){
        if (otp1.getText().toString().isEmpty()||otp2.getText().toString().isEmpty()
                ||otp3.getText().toString().isEmpty()||otp4.getText().toString().isEmpty()){
            Toast.makeText(this,"Please enter OTP",Toast.LENGTH_LONG).show();
return  false;
        }
        else {
            return true;
        }
    }
    private void sendOtp(){
        showDialog("Please wait...");
        HashMap param = new HashMap();
        param.put("phone",phone);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.SEND_OTP, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });
    }
    private void clearOTP(){
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
    }
    private void changePassword(){
        HashMap param = new HashMap();
        param.put("phone",phone);
        param.put("password",editTextVerifyPassword.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.FORGOT_PASS, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent  =  new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
    private void verifyOTP(){
        showDialog("Verifying OTP");
        HashMap param = new HashMap();
        param.put("phone",phone);
        String otp = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();
        param.put("otp",otp);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.VERIFY_OTP, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        if (getIntent().getStringExtra("source").equalsIgnoreCase("FP")){
                           changePassword();
                        }
                        else {
                            Intent intent  =  new Intent(getApplicationContext(), MainActivity.class);
                            if (getIntent().getStringExtra("source").equalsIgnoreCase("login")){

                                Toast.makeText(getApplicationContext(),"You logged in successfully.",Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"You have signed up successfully.",Toast.LENGTH_LONG).show();

                            }
                            loginData = (Login) getIntent().getSerializableExtra("loginData");
                            PowerPreference.getDefaultFile().set("token",loginData.data.token);
                            PowerPreference.getDefaultFile().set("name",loginData.data.name);
                            PowerPreference.getDefaultFile().set("email",loginData.data.email);
                            PowerPreference.getDefaultFile().set("role",loginData.data.role);
                            PowerPreference.getDefaultFile().set("user_id",loginData.data.id);
                            PowerPreference.getDefaultFile().set("phone",loginData.data.phone);
                            PowerPreference.getDefaultFile().set("photo",loginData.data.photo);
                            PowerPreference.getDefaultFile().set("businessname",loginData.data.registered_name);
                            startActivity(intent);
                            finishAffinity();
                        }


                    }
                    else {
                        clearOTP();
                        failedCount = failedCount + 1;
                        verifyLayout.setVisibility(View.VISIBLE);
                        hintMessage.setText("Please Re-verify your mobile number");
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        if (v == btnVerify){
            if (failedCount > 0){
                if (editTextReVerifyMobile.getText().toString().equalsIgnoreCase(phone)){
                    hintMessage.setText("Please type the verification code sent to " + phone);
                    sendOtp();
                    failedCount = 0;
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter correct mobile number",Toast.LENGTH_LONG).show();
                }
                return;
            }
            if (validateOTP()){
                if (getIntent().getStringExtra("source").equalsIgnoreCase("FP")){
                    if (editTextPassword.getText().toString().trim().length() == 0){
                        Toast.makeText(getApplicationContext(),"Please enter new password",Toast.LENGTH_LONG).show();
                        return;

                    }
                    else if (editTextVerifyPassword.getText().toString().trim().length() == 0){
                        Toast.makeText(getApplicationContext(),"Please verify new password",Toast.LENGTH_LONG).show();
                        return;

                    }
                    else if (!editTextPassword.getText().toString().equalsIgnoreCase(editTextVerifyPassword.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Password mismatch",Toast.LENGTH_LONG).show();
                        return;

                    }
                    else {
                        verifyOTP();

                    }

                }
                else {
                    if (failedCount > 0){
                        if (editTextReVerifyMobile.getText().toString().equalsIgnoreCase(phone)){
                            hintMessage.setText("Please type the verification code sent to " + phone);
                            sendOtp();
                            failedCount = 0;
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter correct mobile number",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        verifyOTP();
                    }
                }
            }

        }
        else if (v == textViewResendOTP){
            sendOtp();
        }

    }
}
