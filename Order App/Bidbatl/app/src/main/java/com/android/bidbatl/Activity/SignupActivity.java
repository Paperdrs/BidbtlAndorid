package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.regex.Pattern;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
Button btnSignup;
ConstraintLayout layoutLogin;
TextInputEditText txtMobile;
TextInputEditText txtPassword;
TextInputEditText txtBusinessName;
TextInputEditText txtGST;
TextInputEditText txtEmail;
TextInputEditText txtName;
Login login;
private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        initGSON();
    }
    private void initViews(){
        btnSignup = findViewById(R.id.button_signup);
        layoutLogin = findViewById(R.id.layout_login);
        txtMobile = findViewById(R.id.textMobileNumber);
        txtPassword = findViewById(R.id.textPassword);
        txtBusinessName = findViewById(R.id.textBusinessName);
        txtEmail = findViewById(R.id.textEmail);
        txtName = findViewById(R.id.textUserName);
        txtGST = findViewById(R.id.textGST);

        btnSignup.setOnClickListener(this);
        layoutLogin.setOnClickListener(this);
    }
    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }

    private boolean validateUser(){
        if (txtMobile.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Mobile Number",Toast.LENGTH_LONG).show();
            return false;

        }
        else if (txtName.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_LONG).show();
            return  false;

        }
        else if (txtPassword.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_LONG).show();
            return  false;

        }
        else if (txtBusinessName.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Business Name",Toast.LENGTH_LONG).show();
            return  false;

        }
        else if (txtEmail.getText().toString().isEmpty()){
            Toast.makeText(this,"Please Enter Your Email",Toast.LENGTH_LONG).show();
            return  false;

        }
        else if (!isValidEmailId(txtEmail.getText().toString().trim())){
            Toast.makeText(this,"Please Enter Valid Email",Toast.LENGTH_LONG).show();
            return  false;

        }
        else {
            return true;
        }
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    private void callSignUpAPI(){
        showDialog("Loading...");
        HashMap params = new HashMap();
        params.put("phone",txtMobile.getText().toString());
        params.put("password",txtPassword.getText().toString());
        params.put("registered_name",txtBusinessName.getText().toString());
        params.put("email",txtEmail.getText().toString());
        params.put("gst_number",txtGST.getText().toString());
        params.put("name",txtName.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.SIGNUP, params, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                login = gson.fromJson(result,Login.class);
                if (login.code == 200) {
                    if (login.data != null) {
                        sendOtp();
//                    PowerPreference.getDefaultFile().set("token",login.data.token);
//                    PowerPreference.getDefaultFile().set("user_id",login.data.id);
//                    PowerPreference.getDefaultFile().set("name",login.data.name);
//                    PowerPreference.getDefaultFile().set("email",login.data.email);
//                    PowerPreference.getDefaultFile().set("businessname",login.data.registered_name);

                    }
                    else {
                        Toast.makeText(SignupActivity.this,login.message,Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(SignupActivity.this,login.message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });

    }
    private void sendOtp(){
        HashMap param = new HashMap();
        param.put("phone",txtMobile.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.SEND_OTP, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        JSONObject dataObject = jsonObject.getJSONObject("data");
//dataObject.getString("otp")+
                        Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("source","RR");
                        intent.putExtra("loginData",login);
                        intent.putExtra("phone",txtMobile.getText().toString());
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


    @Override
    public void onClick(View v) {
        if (v == btnSignup){
            if (validateUser()) {
                callSignUpAPI();

            }

        }
        else if (v == layoutLogin){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }

    }
}
