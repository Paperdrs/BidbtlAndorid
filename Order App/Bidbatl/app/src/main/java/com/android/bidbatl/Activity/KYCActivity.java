package com.android.bidbatl.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Adapter.KycAdapter;
import com.android.bidbatl.Model.KYCDataProvider;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KYCActivity extends BaseActivity implements View.OnClickListener {
private Gson gson;
CardView cardViewAadharcard;
CardView cardViewpanCard;
CardView cardViewGST;
String kycType;
RecyclerView recyclerViewKYC;
List<KYCDataProvider.KYCList>kycLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);

        initViews();
        initToolbar();
        initGSON();

    }
    private void initViews(){
        cardViewAadharcard = findViewById(R.id.cardView_aadhar_card);
        cardViewAadharcard.setOnClickListener(this);
        cardViewpanCard = findViewById(R.id.cardView_pan_card);
        cardViewGST = findViewById(R.id.cardView_gst);
        cardViewGST.setOnClickListener(this);
        cardViewpanCard.setOnClickListener(this);
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("My Kyc");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getKYCDocument();
    }

    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void initRecyclerView(){
        recyclerViewKYC = findViewById(R.id.recyclerView_kyc);
        recyclerViewKYC.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        KycAdapter adapter = new KycAdapter(this,kycLists);
        recyclerViewKYC.setAdapter(adapter);
    }

    private void getKYCDocument(){
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_KYC, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                KYCDataProvider kycDataProvider = gson.fromJson(result,KYCDataProvider.class);
                if (kycDataProvider.kycLists != null) {
                    kycLists = kycDataProvider.kycLists;
                    for (KYCDataProvider.KYCList item :kycLists){
                        if (item.type.equals("aadhaar") ){
                            Log.e("ssas","assas");
                            if (item.status.toLowerCase().equals("accepted")){
                            cardViewAadharcard.setOnClickListener(null);
                            cardViewAadharcard.setVisibility(View.GONE);
                            }

                        }
                        if (item.type.equals("pan")){
                            if (item.status.toLowerCase().equals("accepted")){
                                cardViewpanCard.setOnClickListener(null);
                                cardViewpanCard.setVisibility(View.GONE);
                            }
                        }

                        if (item.type.equals("gst")){
                            if (item.status.toLowerCase().equals("accepted")){
                                cardViewGST.setOnClickListener(null);
                                cardViewGST.setVisibility(View.GONE);
                            }
                        }
                    }

                    initRecyclerView();
                }
                else {
                }


                dismissDialog();

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(KYCActivity.this,result,Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == cardViewAadharcard){
            kycType = "aadhaar";

        }
        else if (v == cardViewpanCard){
            kycType = "pan";

        }
        else if (v == cardViewGST){
            kycType = "gst";

        }
        Intent intent = new Intent(this,KYCDetailActivity.class);
        intent.putExtra("kyc",kycType);
        startActivity(intent);

    }
}
