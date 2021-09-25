package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
TextView textViewLogin;
Button btnSubmit;
TextInputEditText txtMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();
    }
    private void initUI(){
        textViewLogin = findViewById(R.id.textView_login);
        textViewLogin.setOnClickListener(this);
        btnSubmit  = findViewById(R.id.button_submit);
        txtMobile  = findViewById(R.id.textView_mob);
        btnSubmit.setOnClickListener(this);

    }
    private void sendOtp(){
        showDialog("");
        HashMap param = new HashMap();
        param.put("phone",txtMobile.getText().toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL+Constants.SEND_OTP, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200){
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        //dataObject.getString("otp")
                        Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("phone",txtMobile.getText().toString());
                        intent.putExtra("source","FP");
                        startActivity(intent);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == textViewLogin){
            finish();
        }
        else if (v == btnSubmit){
            sendOtp();

        }

    }
}
