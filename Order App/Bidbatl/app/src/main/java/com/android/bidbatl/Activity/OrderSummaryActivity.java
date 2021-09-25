package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidbatl.Model.Cart;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.LocationTracker;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderSummaryActivity extends AppCompatActivity implements View.OnClickListener {
TextView textViewShippingAddress,textViewBillingAdddress;
Button buttonPlaceHolder,buttonEditShippingAdd,buttonEditBillingAdd;
Button editShipping;
Button editBilling;
String billingId,billingAddress;
String shipingId,shipingAddress;
Cart.CartData cartData;

TextView textViewSubTotal;
TextView textViewDelCharges;
TextView textViewGST;
TextView textViewTotal;
TextView textViewItemsCount;
TextView tagShipAddress;
TextView tagBillAddress;

TextView textViewNameShip,textViewNameBill,textViewPhoneBill,textViewPhoneShip;
com.android.bidbatl.Model.Address.AddressList selectedAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        init();
        //getCurrentLatLong();
        initToolBar();
    }
    private void initToolBar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Order Summary");

    }
    private void init(){
        tagBillAddress = findViewById(R.id.textView_tag_bill);
        tagShipAddress = findViewById(R.id.textView_tag_ship);

        textViewBillingAdddress = findViewById(R.id.textView_add_billing);
        textViewNameBill = findViewById(R.id.textView_name_billing);
        textViewNameShip = findViewById(R.id.textView_name_shipping);
        textViewPhoneBill = findViewById(R.id.textView_phone_billing);
        textViewPhoneShip = findViewById(R.id.textView_phone_ship);
        textViewShippingAddress = findViewById(R.id.textView_add_ship);
        buttonPlaceHolder = findViewById(R.id.button_place_order);
        editShipping = findViewById(R.id.button_shipping);
        editBilling = findViewById(R.id.button_billing);
        editBilling.setOnClickListener(this);
        editShipping.setOnClickListener(this);

        textViewSubTotal = findViewById(R.id.textView_subtotal);
        textViewDelCharges = findViewById(R.id.textView_del_charge);
        textViewGST = findViewById(R.id.textView_gst);
        textViewTotal = findViewById(R.id.textView_grand_total);
        textViewItemsCount = findViewById(R.id.textView_item_count);
        cartData = (Cart.CartData) getIntent().getSerializableExtra("cart_value");
        if (cartData != null){
           textViewSubTotal.setText(Constants.rupee +cartData.subtotal);
            textViewDelCharges.setText(Constants.rupee+cartData.delivery_charges);
            //textViewGST.setText(cartData.subtotal);
            textViewTotal.setText(Constants.rupee + cartData.grandtotal);
            textViewItemsCount.setText("Price (" + cartData.count + ")");
        }

        List<com.android.bidbatl.Model.Address.AddressList> addressLists = (List<com.android.bidbatl.Model.Address.AddressList>) getIntent().getSerializableExtra("defaultAddress");
        for (com.android.bidbatl.Model.Address.AddressList a: addressLists) {
            if (TextUtils.isEmpty(a.type)){
                continue;
            }
            if (a.type.equalsIgnoreCase("billing")){
                textViewBillingAdddress.setText(a.address);
                billingId = a.id;
                textViewNameBill.setText(a.name);
                textViewPhoneBill.setText(a.phone);

            }

            else if (a.type.equalsIgnoreCase("shipping")){
                textViewShippingAddress.setText(a.address);
                shipingId = a.id;
                textViewNameShip.setText(a.name);
                textViewPhoneShip.setText(a.phone);


            }
        }
        if (addressLists.isEmpty()) {

            textViewShippingAddress.setVisibility(View.GONE);
            tagShipAddress.setVisibility(View.GONE);
            shipingId = "";
            textViewNameShip.setVisibility(View.GONE);
            textViewPhoneShip.setVisibility(View.GONE);
            editShipping.setText("Add Shipping Address");


            textViewBillingAdddress.setVisibility(View.GONE);
            tagBillAddress.setVisibility(View.GONE);
            billingId = "";
            textViewNameBill.setVisibility(View.GONE);
            textViewPhoneBill.setVisibility(View.GONE);
            editBilling.setText("Add Billing Address");

        }


        buttonPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billingId == null || shipingId == null){
                    Toast.makeText(getApplicationContext(),"Please select address",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(OrderSummaryActivity.this,PaymentOptionActivity.class);
                intent.putExtra("shipping_address_id",shipingId);
                intent.putExtra("billing_address_id",billingId);
                intent.putExtra("distance",getIntent().getStringExtra("distance"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void placeOrderAPI(){

    }

    @Override
    public void onClick(View v) {
        String addressType = "";
        if (v == editBilling){
           addressType = "billing";

        }
        else  if (v== editShipping){
           addressType = "shipping";

        }
        Intent intent = new Intent(this,SavedAddressActivity.class);
        intent.putExtra("addresstype",addressType);
        intent.putExtra("selectionType",1);
        startActivityForResult(intent, 1);//Takes intent and requestcode

//        startActivity(intent);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK) {
                selectedAddress = (com.android.bidbatl.Model.Address.AddressList) data.getSerializableExtra("SELECTED_ADD");
                Log.d("eeeee",selectedAddress.address);
                if (selectedAddress.type.equalsIgnoreCase("billing")){
                    textViewBillingAdddress.setText(selectedAddress.address);
                    billingId = selectedAddress.id;
                    textViewNameBill.setText(selectedAddress.name);
                    textViewPhoneBill.setText(selectedAddress.phone);

                    editBilling.setText("Change or Add Billing Address");
                    textViewBillingAdddress.setVisibility(View.VISIBLE);
                    tagBillAddress.setVisibility(View.VISIBLE);
                    textViewNameBill.setVisibility(View.VISIBLE);
                    textViewPhoneBill.setVisibility(View.VISIBLE);

                }
                else {
                    textViewShippingAddress.setText(selectedAddress.address);
                    shipingId = selectedAddress.id;
                    textViewNameShip.setText(selectedAddress.name);
                    textViewPhoneShip.setText(selectedAddress.phone);

                    textViewShippingAddress.setVisibility(View.VISIBLE);
                    tagShipAddress.setVisibility(View.VISIBLE);
                    textViewNameShip.setVisibility(View.VISIBLE);
                    textViewPhoneShip.setVisibility(View.VISIBLE);
                    editShipping.setText("Change or Add Shipping Address");
                }
            }
    }
}
