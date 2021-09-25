package com.android.bidbatl.Activity.product;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Interface.CartItemInterface;
import com.android.bidbatl.Activity.AddNewAddressActivity;
import com.android.bidbatl.Activity.BaseActivity;
import com.android.bidbatl.Activity.OrderSummaryActivity;
import com.android.bidbatl.Adapter.CartAdapter;
import com.android.bidbatl.Model.Address;
import com.android.bidbatl.Model.Cart;
import com.android.bidbatl.Model.Profile;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.bidbatl.Utility.LocationTracker;
import com.android.bidbatl.Utility.Utility;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.preference.PowerPreference;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCartActivity extends BaseActivity implements CartItemInterface, View.OnClickListener {
    ImageView imageViewCart;
    RecyclerView recyclerViewCart;
    CartAdapter cartAdapter;
    List<Cart.CartList> cartList = new ArrayList<>();
    private Gson gson;
    CardView bottomHeader;
    Button checkOut;
    TextView textViewCount;
    TextView textViewSubtotal;
    TextView textViewDelCharges;
    TextView textViewGrandTotal;
    TextView textViewGST;
    List<Address.AddressList>addressLists = new ArrayList<>();
    List<Boolean> currentActiveItem = new ArrayList<>();
    int cuurrentIndex = -1;
    RelativeLayout emptylayout;
    Cart.CartData cartData;
    private String distance = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        bottomHeader = findViewById(R.id.constraintLayout_bottom);
        checkOut =  findViewById(R.id.button_continue);
        textViewCount = findViewById(R.id.textView_item_count);
        textViewGrandTotal = findViewById(R.id.textView_grand_total);
        textViewSubtotal = findViewById(R.id.textView_subtotal);
        textViewDelCharges = findViewById(R.id.textView_del_charge);
        textViewGST = findViewById(R.id.textView_gst);
        bottomHeader.setVisibility(View.GONE);
        checkOut.setOnClickListener(this);
        emptylayout = findViewById(R.id.empty_layout);
        emptylayout.setVisibility(View.GONE);
        initToobar();
        initGSON();
        Log.d("Dist", "" + Utility.getInstance().distance(28.6981224,77.1600089));
    }
    private void getCurrentLatLong() {
        LocationTracker locationTracker = new LocationTracker(this, new LocationTracker.CoordinateSender() {
            @Override
            public void getCoordinates(double lat, double longi) {
                if (lat != 0.0 && longi != 0.0) {
                    Log.e("lat",lat+"");
                    Log.e("longi",longi+"");
                    //28.5503596,77.2969282
                    Log.e("Dist", "" + Utility.getInstance().distance(lat,longi));
                    distance = Utility.getInstance().distance(lat,longi);
                    Toast.makeText(MyCartActivity.this,"Your Distance from current Location to Block B, Sector 11, Faridabad, Haryana 121006 " +
                            "in KM->" + distance,Toast.LENGTH_LONG).show();
                    getCartItem(true);

                }
            }
        });
        locationTracker.getLocation();
    }
    private void getUserDetail() {
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_USER, new HashMap<String, String>(), getApplicationContext(), new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                Profile profile = gson.fromJson(result, Profile.class);
                if (profile.code == 200) {
                    if (TextUtils.isEmpty(profile.userProfile.latitude)){
                        Intent intent = new Intent(MyCartActivity.this, AddNewAddressActivity.class);
                        startActivity(intent);
                    }
                    else {
                        distance = Utility.getInstance().distance(Double.parseDouble(profile.userProfile.latitude),Double.parseDouble(profile.userProfile.logitude));
                        Toast.makeText(MyCartActivity.this,"Your Distance from current Location to Block B, Sector 11, Faridabad, Haryana 121006 " +
                                "in KM->" + distance,Toast.LENGTH_LONG).show();
                        getCartItem(true);
                    }
                }

            }

            @Override
            public void onErrorFinished(String result) {

            }
        });
    }
    private void initRecyclerView(){
        recyclerViewCart = findViewById(R.id.recyclervw_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
        textViewTitle.setText("My Cart");
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        imageViewCart.setVisibility(View.GONE);
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);
        initRecyclerView();
        getUserDetail();
//        getCurrentLatLong();

    }
    private void deleteItemFromCart(String id){
        //showDialog("Deleting...");
        HashMap param = new HashMap();
        param.put("id",id);
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.DELETE_CART_ITEM, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                getCartItem(false);

            }

            @Override
            public void onErrorFinished(String result) {
                //dismissDialog();

            }
        });
    }
    private void getCartItem(final Boolean shouldShowLoader){
        if (shouldShowLoader) {
            showDialog("Loading...");
        }
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL+Constants.GET_CART_ITEM+"/"+distance, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                if (shouldShowLoader) {
                    dismissDialog();
                }
               Cart cart = gson.fromJson(result, Cart.class);
               if (cart.data.cartLists != null) {
                   cartList = cart.data.cartLists;



                   for (int i = 0; i<cartList.size();i++){
                       if (cuurrentIndex == i){
                           currentActiveItem.add(true);
                       }
                       else {
                           currentActiveItem.add(false);
                       }
                   }
                   if (cart.data.addressLists != null){
                       addressLists = cart.data.addressLists;
                       cartData = cart.data;
                   }
//                   Log.d("adddd",cart.data.addressLists.size() + "");
                   cartAdapter = new CartAdapter(getApplicationContext(), cartList, MyCartActivity.this,cuurrentIndex,currentActiveItem);
                   recyclerViewCart.setAdapter(cartAdapter);
                   textViewCount.setText("Transction Amount (" + cart.data.count+")");
                   textViewDelCharges.setText(Constants.rupee+cart.data.delivery_charges);
                   textViewSubtotal.setText(Constants.rupee+cart.data.subtotal);
                   textViewGrandTotal.setText(Constants.rupee+cart.data.grandtotal);
                   textViewGST.setText(Constants.rupee+cart.data.gst);
                   bottomHeader.setVisibility(View.VISIBLE);
                   if (cartList.size() == 0){
                       bottomHeader.setVisibility(View.GONE);
                       recyclerViewCart.setVisibility(View.GONE);
                       emptylayout.setVisibility(View.VISIBLE);
                   }
                   else {
                       emptylayout.setVisibility(View.GONE);
                   }
               }
               else {
                   bottomHeader.setVisibility(View.GONE);
                   recyclerViewCart.setVisibility(View.GONE);
               }

            }

            @Override
            public void onErrorFinished(String result) {
                if (shouldShowLoader) {
                    dismissDialog();
                }
            }
        });
    }


    @Override
    public void didUpadetCartItem(String productid, String qty,int index,String price,String stock) {
        hideSoftKeyBoard();
        cuurrentIndex = index;

        if (Integer.parseInt(qty) >  Integer.parseInt(stock)){
            Toast.makeText(MyCartActivity.this, "Product quantity exceeded. You can select max " + stock, Toast.LENGTH_LONG).show();
            return;
        }

        double d = 0;
        try {
            d = (Double) NumberFormat.getNumberInstance().parse(price);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Float mPrice = new Float(d);
        HashMap param = new HashMap();
        param.put("product_id",productid);
        param.put("quantity",qty);
        param.put("price",mPrice+"");
        Log.i("CartP",param.toString());
        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.ADD_UPDATE_CART_ITEM, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                showCartDialog();
                getCartItem(false);
            }

            @Override
            public void onErrorFinished(String result) {
                Toast.makeText(MyCartActivity.this,result,Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void deleteCartItem(String id) {
        deleteItemFromCart(id);

    }

    @Override
    public void refreshProductList(int index) {
        if (currentActiveItem.indexOf(true) >=0) {
            currentActiveItem.set(currentActiveItem.indexOf(true), false);
        }
        currentActiveItem.set(index,true);
//       topSellingAdapter.notifyItemChanged(index);
        cartAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (v == checkOut){
            Intent intent = new Intent(this, OrderSummaryActivity.class);
            intent.putExtra("defaultAddress", (Serializable) addressLists);
            intent.putExtra("cart_value",cartData);
            intent.putExtra("distance",distance);

            startActivity(intent);

        }

    }
}
