package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.R;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {
    ImageView imageViewCart;
    Button kycBtn,loginSecBtn,transHistoryBtn,chooseLangBtn,bbtPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initToobar();
    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("My Account");
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);
        initView();

    }
    private void initView(){
        kycBtn = findViewById(R.id.button_kyc);
        loginSecBtn = findViewById(R.id.button_log_sec);
        transHistoryBtn = findViewById(R.id.button_trans_history);
       // chooseLangBtn = findViewById(R.id.button_choose_lang);
        bbtPoint = findViewById(R.id.button_btl_point);
        bbtPoint.setOnClickListener(this);
        kycBtn.setOnClickListener(this);
        loginSecBtn.setOnClickListener(this);
        transHistoryBtn.setOnClickListener(this);
       // chooseLangBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == loginSecBtn){
            intent = new Intent(MyAccountActivity.this,UserDetailActivity.class);

        }
        else  if (v == bbtPoint){
            intent = new Intent(MyAccountActivity.this,BidbatlPointsActivity.class);

        }
        else if (v == kycBtn){
            intent = new Intent(MyAccountActivity.this,KYCActivity.class);

        }
        else  if (v == transHistoryBtn){
            intent = new Intent(MyAccountActivity.this,TransctionHistoryActivity.class);

        }

        startActivity(intent);

    }
}
