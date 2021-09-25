package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.R;

public class AddressActivity extends BaseActivity implements View.OnClickListener {
Button buttonShipping;
Button buttonBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initToobar();
    }
    private void initView(){
        buttonBilling = findViewById(R.id.button_billing);
        buttonShipping = findViewById(R.id.button_shipping);
        buttonBilling.setOnClickListener(this);
        buttonShipping.setOnClickListener(this);
    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Saved Address");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonBilling){
            Intent savedAddress = new Intent(getApplicationContext(),SavedAddressActivity.class);
            savedAddress.putExtra("addresstype","billing");
            startActivity(savedAddress);

        }
        else if (buttonShipping == v){
            Intent savedAddress = new Intent(getApplicationContext(),SavedAddressActivity.class);
            savedAddress.putExtra("addresstype","shipping");
            startActivity(savedAddress);

        }

    }
}
