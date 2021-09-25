package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Activity.product.MainActivity;
import com.android.bidbatl.Adapter.BidBatlPointsAdapter;
import com.android.bidbatl.Adapter.TopSellingAdapter;
import com.android.bidbatl.Model.BidBatlPoints;
import com.android.bidbatl.Model.ProductListProvider;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BidbatlPointsActivity extends BaseActivity {
    RecyclerView recyclerView;
    BidBatlPointsAdapter adapter;
    BidBatlPoints bidBatlPointsList;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidbatl_points);
        initGSON();
        initToolbar();
        initViews();
    }
    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Bidbatl Points");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }
    private void initViews(){
        recyclerView = findViewById(R.id.recyclervw_bbt_point);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new BidBatlPointsAdapter(getApplicationContext(),new ArrayList<BidBatlPoints.PointList>());
        recyclerView.setAdapter(adapter);
        getBidbatlPoints();


    }
    private void getBidbatlPoints(){
        showCircleDialog();
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.BB_POINTS, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                Log.e("reee",result);
                dismisCircleDialog();
             BidBatlPoints   bidBatlPointsList = gson.fromJson(result, BidBatlPoints.class);
                if (bidBatlPointsList.code == 200) {
                    if (bidBatlPointsList.pointList != null) {
                        adapter = new BidBatlPointsAdapter(getApplicationContext(), bidBatlPointsList.pointList);
                        recyclerView.setAdapter(adapter);
                        Log.i("count", bidBatlPointsList.pointList.size() + "");

                    } else {
//                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(BidbatlPointsActivity.this, bidBatlPointsList.message, Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onErrorFinished(String result) {
                    dismisCircleDialog();
                Toast.makeText(BidbatlPointsActivity.this, result, Toast.LENGTH_LONG).show();

            }
        });

    }
}
