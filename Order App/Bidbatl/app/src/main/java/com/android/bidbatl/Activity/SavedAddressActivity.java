package com.android.bidbatl.Activity;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Interface.AddressInterface;
import com.android.bidbatl.Adapter.AddressAdapter;
import com.android.bidbatl.Model.Address;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SavedAddressActivity extends BaseActivity implements View.OnClickListener, AddressInterface {
    ImageView imageViewCart;
    RecyclerView recyclerViewAddress;
    AddressAdapter addressAdapter;
    List<Address.AddressList> addressList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);
        initToobar();
        initGSON();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    private void initRecyclerView(){
        recyclerViewAddress = findViewById(R.id.recyclervw_address);
        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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
        textViewTitle.setText("My Address");
        if (getIntent().getStringExtra("addresstype").equalsIgnoreCase("shipping")){
            textViewTitle.setText("Shipping Address");
        }
        else {
            textViewTitle.setText("Billing Address");
        }
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        initRecyclerView();
        initUI();

    }
    private void getAddress(){
        showDialog("Loading...");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_ADDRESS+getIntent().getStringExtra("addresstype"), new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                Address address = gson.fromJson(result,Address.class);
                if (address.addressLists != null){
                    addressList = address.addressLists;
                    addressAdapter = new AddressAdapter(SavedAddressActivity.this,addressList,SavedAddressActivity.this,getIntent());
                    recyclerViewAddress.setAdapter(addressAdapter);
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });
    }
    private void initUI(){
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton){
            Intent intent = new Intent(getApplicationContext(),AddNewAddressActivity.class);
            intent.putExtra("addresstype",getIntent().getStringExtra("addresstype"));
            startActivity(intent);
        }

    }

    @Override
    public void deleteAddress(String id) {
        showDialog("Deleting");
        HashMap param = new HashMap();
        param.put("address_id",id);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.DELETE_ADDRESS, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200){
                        Toast.makeText(getApplicationContext(),"Address Deleted.",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
                getAddress();

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void setDefault(String id,String type) {
        showDialog("Setting Default...");
        HashMap param = new HashMap();
        param.put("id",id);
        param.put("type",type);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.SET_DEFAULT, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                getAddress();


            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });


    }

    @Override
    public void getSelectedAddress(Address.AddressList addressList) {

    }
}
