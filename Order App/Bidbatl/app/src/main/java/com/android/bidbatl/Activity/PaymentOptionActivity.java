package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentOptionActivity extends BaseActivity {
Button buttonContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        buttonContinue = findViewById(R.id.button_continue);
        buttonContinue.setOnClickListener(continueAction);
        initToolBar();

    }
    private void initToolBar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Payment");

    }
    private View.OnClickListener continueAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           placeOrderAPI();

        }
    };
    private void placeOrderAPI(){
        showDialog("Placing Order...");
        HashMap param = new HashMap();
        param.put("shipping_address_id",getIntent().getStringExtra("shipping_address_id"));
        param.put("billing_address_id",getIntent().getStringExtra("billing_address_id"));
        param.put("distance",getIntent().getStringExtra("distance"));
        Log.e("prpr",param.toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.PLACE_ORDER, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200){
                        Intent intent = new Intent(PaymentOptionActivity.this,OrderStatusActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_LONG).show();
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

}
