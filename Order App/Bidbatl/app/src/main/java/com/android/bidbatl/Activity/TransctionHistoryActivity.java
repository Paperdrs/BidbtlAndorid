package com.android.bidbatl.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bidbatl.Adapter.TransctionHistoryAdapter;
import com.android.bidbatl.Model.MyTransctionHiistory;
import com.android.bidbatl.Model.TransctionHistory;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransctionHistoryActivity extends BaseActivity {
    RecyclerView recyclerView;
    TransctionHistoryAdapter adapter;
    TransctionHistory transctionHistory;
    List<TransctionHistory.MyTransctions>transctionHistoryList = new ArrayList<>();

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transction_history);
        initToobar();
        initGSON();
        initRecyclerView(new ArrayList<MyTransctionHiistory>());
        getTransctionHistory();

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
        textViewTitle.setText("My Transctions");
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);

    }
    private void initRecyclerView(List<MyTransctionHiistory> hiistory){
        recyclerView = findViewById(R.id.recyclervw_trans_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TransctionHistoryAdapter(getApplicationContext(),hiistory);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        if (0 == recyclerView.getItemDecorationCount()) {
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
            recyclerView.addItemDecoration(dividerItemDecoration);

        }
        recyclerView.setAdapter(adapter);
    }

    private void getTransctionHistory(){
        showDialog("");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_TRANSCTION_HISTORY, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                Log.e("trans",result);
                transctionHistory = gson.fromJson(result,TransctionHistory.class);
                ArrayList<MyTransctionHiistory> myTransctions = new ArrayList<>();
                if (transctionHistory.code == 200){
                    transctionHistoryList = transctionHistory.transctionsList;
                    if (transctionHistoryList != null){
                        for (TransctionHistory.MyTransctions  transctions : transctionHistoryList){

                            if (transctions.order != null){
                                MyTransctionHiistory history = new MyTransctionHiistory();
                                history.timestamp = transctions.order.timestamp;
                                history.invoice_no = transctions.order.invoice_no;
                                history.transaction_type = transctions.order.transaction_type;
                                history.amount = transctions.order.amount;
                                history.orderNumber = transctions.number;
                                myTransctions.add(history);
                            }
                            if (transctions.paidPoints != null){
                                MyTransctionHiistory history = new MyTransctionHiistory();
                                history.timestamp = transctions.paidPoints.timestamp;
                                history.invoice_no = transctions.paidPoints.invoice_no;
                                history.transaction_type = transctions.paidPoints.transaction_type;
                                history.amount = "Redeem "   + transctions.paidPoints.amount  + " Bidbatl Points";
                                history.orderNumber = transctions.number;
                                myTransctions.add(history);
                            }
                            if (transctions.payment != null && transctions.payment.timestamp != null){

                                MyTransctionHiistory history = new MyTransctionHiistory();
                                history.timestamp = transctions.payment.timestamp;
                                history.invoice_no = transctions.payment.invoice_no;
                                history.transaction_type = transctions.payment.transaction_type;
                                history.amount = transctions.payment.amount;
                                history.orderNumber = transctions.number;
                                myTransctions.add(history);
                            }
                            if (transctions.cashbackPoints != null && transctions.cashbackPoints.timestamp != null){
                                MyTransctionHiistory history = new MyTransctionHiistory();
                                history.timestamp = transctions.cashbackPoints.timestamp;
                                history.invoice_no = transctions.cashbackPoints.invoice_no;
                                history.transaction_type = transctions.cashbackPoints.transaction_type;
                                history.amount = "Credit " + transctions.cashbackPoints.amount + " Bidbatl Points";
                                history.orderNumber = transctions.number;
                                myTransctions.add(history);
                            }

                        }
                        initRecyclerView(myTransctions);
                    }
                }

            }

            @Override
            public void onErrorFinished(String result) {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                dismissDialog();

            }
        });
    }

}
