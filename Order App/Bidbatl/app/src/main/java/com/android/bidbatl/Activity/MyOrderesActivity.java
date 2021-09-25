package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Adapter.MyOrderAdapter;
import com.android.bidbatl.Model.MyOrder;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrderesActivity extends BaseActivity {
    ImageView imageViewCart;
    MyOrderAdapter myOrderAdapter;
    RecyclerView recyclerViewMyOrder;
    List<MyOrder.MyOrderList> myOrderList = new ArrayList<>();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orderes);
        initToobar();
        initGSON();
    }
    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("My Orders");
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);
        initRecyclerView();

    }
    private void initRecyclerView(){
        recyclerViewMyOrder = findViewById(R.id.recyclervw_my_order);
        recyclerViewMyOrder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMyOrder.getContext(), DividerItemDecoration.VERTICAL);
        if (0 == recyclerViewMyOrder.getItemDecorationCount()) {
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
            recyclerViewMyOrder.addItemDecoration(dividerItemDecoration);

        }
        getMyOrderList();

    }
    private void getMyOrderList(){
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL+Constants.GET_ORDER, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                MyOrder myOrder = gson.fromJson(result,MyOrder.class);

                myOrderList = myOrder.myOrderLists;
                if (myOrderList != null) {
                    myOrderAdapter = new MyOrderAdapter(getApplicationContext(), myOrderList);
                    recyclerViewMyOrder.setAdapter(myOrderAdapter);
                }


            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();


            }
        });
    }
}
