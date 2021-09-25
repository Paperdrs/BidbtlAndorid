package com.android.bidbatl.Activity.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.bidbatl.Activity.AddNewAddressActivity;
import com.android.bidbatl.Activity.AddressActivity;
import com.android.bidbatl.Activity.BaseActivity;
import com.android.bidbatl.Activity.BidbatlPointsActivity;
import com.android.bidbatl.Activity.CategoryActivity;
import com.android.bidbatl.Activity.LoginActivity;
import com.android.bidbatl.Activity.MyAccountActivity;
import com.android.bidbatl.Activity.MyOrderesActivity;
import com.android.bidbatl.Activity.NotificationActivity;
import com.android.bidbatl.Model.Profile;
import com.android.bidbatl.Utility.CountDrawable;
import com.android.bidbatl.Utility.CustomTypefaceSpan;
import com.android.bidbatl.Utility.LocationTracker;
import com.android.bidbatl.Utility.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Interface.UpdateCartItem;
import com.android.bidbatl.Adapter.PopularAdapter;
import com.android.bidbatl.Adapter.TopSellingAdapter;
import com.android.bidbatl.Model.Category;
import com.android.bidbatl.Model.ProductListProvider;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.preference.PowerPreference;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, UpdateCartItem {
    TopSellingAdapter topSellingAdapter;

    RecyclerView subCategoryRecyclerview, productListRecyclerview;
    ConstraintLayout categoryLayout;
    ImageView imageViewCart;
    Toolbar toolbar;
    ProductListProvider productListProvider;
    Category category;
    private Gson gson;
    String selectedCategoryID = "0";
    List<Boolean> currentActiveItem = new ArrayList<>();
    int cuurrentIndex = -1;
    TextView textViewCartCount;
    EditText searchText;
    RelativeLayout emptyLayout;
    CircleImageView circleImageView;
   // TextView textViewPoints;
    MenuItem bidbatalPointItem;
    TextView textViewUserName;
    int exitCount = 0;
    String distance = "";
    MutableLiveData<String> distanceString;
    PopularAdapter popularAdapter;
    String motherPackId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();


        searchText = findViewById(R.id.editText);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().toString().length() == 0){
                        selectedCategoryID = "0";
                        PowerPreference.getDefaultFile().set("motherPackId","");
                        getAllProductList(selectedCategoryID,true);
                    }
                    else {


                    }
                    return true;
                }
                return false;
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // if (charSequence.length() > 2) {
                if (charSequence.length() == 0){
                    selectedCategoryID = "0";
                    PowerPreference.getDefaultFile().set("motherPackId","");
                    getAllProductList(selectedCategoryID,true);
                }
                else {
                    PowerPreference.getDefaultFile().set("motherPackId","");
                    searchProduct(searchText.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Utility.getInstance().getLatLongFromAddress(this);

        initUI();
        setUpDrawerMenu();
        initRecyclerView();
        initGSON();
//        getCurrentLatLong();

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
                    Toast.makeText(MainActivity.this,"Your Distance from current Location to Block B, Sector 11, Faridabad, Haryana 121006 " +
                            "in KM->" + distance,Toast.LENGTH_LONG).show();
                    getAllProductList(selectedCategoryID,true);

                }
            }
        });
        locationTracker.getLocation();
    }

    private void initToolBar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        imageViewCart.setVisibility(View.VISIBLE);
        textViewCartCount = toolbar.findViewById(R.id.textView_cart_count);
        FrameLayout frameLayout = toolbar.findViewById(R.id.cart_holder);
        frameLayout.setVisibility(View.VISIBLE);
        FrameLayout frameLayoutNoti = toolbar.findViewById(R.id.frameLayout_noti);
        frameLayoutNoti.setVisibility(View.GONE);
        frameLayoutNoti.setOnClickListener(notificationAction);
    }

    private void initRecyclerView() {
        productListRecyclerview = findViewById(R.id.top_selling_recyclerview);
        subCategoryRecyclerview = findViewById(R.id.popular_brand_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productListRecyclerview.setLayoutManager(linearLayoutManager);
        productListRecyclerview.setHasFixedSize(true);
        subCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        subCategoryRecyclerview.setHasFixedSize(true);

    }
    public View.OnClickListener notificationAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//            finish();
        }
    };

    private void setUpDrawerMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        textViewUserName = header.findViewById(R.id.textView_menu_user_name);
        textViewUserName.setText(PowerPreference.getDefaultFile().getString("name"));
//        textViewPoints = header.findViewById(R.id.textView_points);
//        textViewPoints.setText(PowerPreference.getDefaultFile().getString("points"));

        TextView textViewUserPhone =  header.findViewById(R.id.textView_menu_user_phone);
        textViewUserPhone.setText(PowerPreference.getDefaultFile().getString("phone"));
        circleImageView = header.findViewById(R.id.imageView_user);
        setfullwidth(navigationView);

        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        bidbatalPointItem = menu.findItem(R.id.bidbatl_point);
        bidbatalPointItem.setTitle("Bidbatl Point " + PowerPreference.getDefaultFile().getString("points"));

        getUserDetail();
    }
    private void setfullwidth(NavigationView navigationView){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        int width = (int) (displayMetrics.widthPixels * 0.8);
        params.width = width;
        navigationView.setLayoutParams(params);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    private void showImageBrowser(){

    }
    private void getProductByMotherPackId(String id){
        showDialog("");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_MOTHER_PACK_BY_ID + distance+"/" + id, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                motherPackId = "";

                productListProvider = gson.fromJson(result, ProductListProvider.class);
                if (productListProvider.code == 200) {
                    if (productListProvider.productListProviders != null) {
                        for (int i = 0; i < productListProvider.productListProviders.size(); i++) {
                            if (cuurrentIndex == i) {
                                currentActiveItem.add(true);
                            } else {
                                currentActiveItem.add(false);
                            }
                        }
                        if (productListProvider.productListProviders.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                        }
//
                        topSellingAdapter = new TopSellingAdapter(getApplicationContext(), productListProvider.productListProviders, MainActivity.this, currentActiveItem, cuurrentIndex);
                        productListRecyclerview.setAdapter(topSellingAdapter);
                        Log.i("count", productListProvider.productListProviders.size() + "");

                    } else {
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, productListProvider.message, Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        motherPackId = PowerPreference.getDefaultFile().getString("motherPackId");
        textViewUserName.setText(PowerPreference.getDefaultFile().getString("name"));

        Glide.with(this).load(PowerPreference.getDefaultFile().getString("photo")).placeholder(R.drawable.user_icon_placeholder).into(circleImageView);
        getCategoryList();
        selectedCategoryID = "0";
        if (motherPackId.length()>0){
            getProductByMotherPackId(motherPackId);
        }
        else {
            getUserDetail();
//            if (distance.length() == 0){
//                getUserDetail();
//            }
//            else {
//                getAllProductList(selectedCategoryID,true);
//            }
        }

    }

    private void initGSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }

    private void initUI() {
        categoryLayout = findViewById(R.id.constraintLayout_category);
        categoryLayout.setOnClickListener(this);
        emptyLayout = findViewById(R.id.empty_layout);
        imageViewCart.setOnClickListener(this);
        emptyLayout.setVisibility(View.GONE);
    }

    private void getCategoryList() {
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_ALL_CATEGORY, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                category = gson.fromJson(result, Category.class);
                List<Category.CategoryList>lists = new ArrayList<>();
                if (category.code == 200) {
                    if (category.categoryLists != null) {
                        Log.i("count", category.categoryLists.size() + "");
                        lists.add(new Category.CategoryList("0","All"));
                        for (Category.CategoryList category : category.categoryLists){
                            if (!category.parent_id.equals("0")){
                                lists.add(category);
                            }


                        }
                        Collections.sort(lists, Category.CategoryList.StuNameComparator);
//                        category.categoryLists.add(0,new Category.CategoryList("0","All"));
                        popularAdapter = new PopularAdapter(getApplicationContext(), lists, MainActivity.this);
                        subCategoryRecyclerview.setAdapter(popularAdapter);
                    }

                }

            }

            @Override
            public void onErrorFinished(String result) {


            }
        });
    }

    private void searchProduct(String item) {
        //showDialog("Loading...");
        HashMap param = new HashMap();
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.SEARCH+"/"+distance+"/"+item, param, this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
               // dismissDialog();
                productListProvider = gson.fromJson(result, ProductListProvider.class);
                if (productListProvider.code == 200) {
                    if (productListProvider.productListProviders != null) {
                        for (int i = 0; i < productListProvider.productListProviders.size(); i++) {
                            if (cuurrentIndex == i) {
                                currentActiveItem.add(true);
                            } else {
                                currentActiveItem.add(false);
                            }
                        }
                        if (productListProvider.productListProviders.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                        }
//                        if (productListProvider.metaData.cart_count != null){
//                            textViewCartCount.setText(productListProvider.metaData.cart_count);
//                        }
                        topSellingAdapter = new TopSellingAdapter(getApplicationContext(), productListProvider.productListProviders, MainActivity.this, currentActiveItem, cuurrentIndex);
                        productListRecyclerview.setAdapter(topSellingAdapter);
                        Log.i("count", productListProvider.productListProviders.size() + "");

                    } else {
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, productListProvider.message, Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onErrorFinished(String result) {
               //dismissDialog();
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getProductList(String categoryID,final Boolean shouldShowLoader) {
        if (shouldShowLoader) {
            showDialog("Loading...");
        }

        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_PRODUCT_BY_CAT_ID + selectedCategoryID +"/"+ distance, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                if (shouldShowLoader) {
                    dismissDialog();
                }
                productListProvider = gson.fromJson(result, ProductListProvider.class);
                if (productListProvider.code == 200) {
                    if (productListProvider.productListProviders != null) {
                        for (int i = 0; i < productListProvider.productListProviders.size(); i++) {

                            if (cuurrentIndex == i) {
                                currentActiveItem.add(true);
                            } else {
                                currentActiveItem.add(false);
                            }
                        }
                        if (productListProvider.metaData.cart_count != null) {
                            if (productListProvider.metaData.cart_count.equalsIgnoreCase("0")) {
                                textViewCartCount.setText("");
                                textViewCartCount.setVisibility(View.GONE);

                            } else {
                                textViewCartCount.setText(productListProvider.metaData.cart_count);
                                textViewCartCount.setVisibility(View.VISIBLE);

                            }
                        }
                        if (productListProvider.productListProviders.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                        }
                        topSellingAdapter = new TopSellingAdapter(getApplicationContext(), productListProvider.productListProviders, MainActivity.this, currentActiveItem, cuurrentIndex);
                        productListRecyclerview.setAdapter(topSellingAdapter);

                                Log.i("count", productListProvider.productListProviders.size() + "");

                    } else {
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, productListProvider.message, Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onErrorFinished(String result) {
                if (shouldShowLoader) {
                    dismissDialog();
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getAllProductList(String categoryID, final Boolean shouldShowLoader) {
        if (shouldShowLoader) {
            showDialog("Loading...");
        }
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_PRODUCT + selectedCategoryID +"/"+ distance, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                if (shouldShowLoader){
                    dismissDialog();
                }
                productListProvider = gson.fromJson(result, ProductListProvider.class);
                if (productListProvider.code == 200) {
                    if (productListProvider.productListProviders != null) {
                        for (int i = 0; i < productListProvider.productListProviders.size(); i++) {
                            if (cuurrentIndex == i) {
                                currentActiveItem.add(true);
                            } else {
                                currentActiveItem.add(false);
                            }
                        }
                        if (productListProvider.metaData.cart_count != null) {
                            if (productListProvider.metaData.cart_count.equalsIgnoreCase("0")) {
                                textViewCartCount.setText("");
                                textViewCartCount.setVisibility(View.GONE);

                            } else {
                                textViewCartCount.setText(productListProvider.metaData.cart_count);
                                textViewCartCount.setVisibility(View.VISIBLE);

                            }
                        }
                        if (productListProvider.productListProviders.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                        }
                        topSellingAdapter = new TopSellingAdapter(getApplicationContext(), productListProvider.productListProviders, MainActivity.this, currentActiveItem, cuurrentIndex);
                        productListRecyclerview.setAdapter(topSellingAdapter);
                        Log.i("count", productListProvider.productListProviders.size() + "");
                        topSellingAdapter.notifyItemChanged(cuurrentIndex);

                    } else {
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, productListProvider.message, Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onErrorFinished(String result) {
                if (shouldShowLoader) {
                    dismissDialog();
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitCount = exitCount + 1;
            if (exitCount == 1) {
                Toast.makeText(getApplicationContext(), "Press Back again to exit from the app", Toast.LENGTH_LONG).show();
            }
            if (exitCount >1){
                finishAffinity();
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_order) {
            Intent myOrder = new Intent(getApplicationContext(), MyOrderesActivity.class);
            startActivity(myOrder);
            // Handle the camera action
        } else if (id == R.id.my_account) {
            Intent myAccount = new Intent(getApplicationContext(), MyAccountActivity.class);
            startActivity(myAccount);

        } else if (id == R.id.saved_address) {
            Intent savedAddress = new Intent(getApplicationContext(), AddressActivity.class);
            startActivity(savedAddress);

        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PowerPreference.clearAllData();
                            Intent wishList = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(wishList);
                            finishAffinity();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        else if (id == R.id.bidbatl_point) {
            Intent wishList = new Intent(getApplicationContext(), BidbatlPointsActivity.class);
            startActivity(wishList);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == imageViewCart) {
            Intent intentCart = new Intent(getApplicationContext(), MyCartActivity.class);
            startActivity(intentCart);
            //super.onSearchRequested();

        } else if (v == categoryLayout) {
            Intent intentCart = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intentCart);
        }

    }

    @Override
    public void didUpdateCartItemFromMainActivity(ProductListProvider.ProductListData productListData, String qnty, int index) {
        hideSoftKeyBoard();
        Log.e("ww",Integer.parseInt(qnty) + "" + Integer.parseInt(productListData.cfc_stock));
        if (Integer.parseInt(qnty) >  Integer.parseInt(productListData.cfc_stock)){
            Toast.makeText(MainActivity.this, "Product quantity exceeded. You can select max " + productListData.cfc_stock, Toast.LENGTH_LONG).show();
        return;
        }
        cuurrentIndex = index;
        double d = 0;
        try {
            d = (Double) NumberFormat.getNumberInstance().parse(productListData.price);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Float mPrice = new Float(d);
        //showDialog("Adding Item In cart...");
        HashMap params = new HashMap();
        params.put("product_id", productListData.id);
        params.put("quantity", qnty);
        params.put("price",mPrice+"");
        Log.i("CartP", params.toString());

        APIManager.getInstance().callAPI(Request.Method.POST, Constants.BASEURL + Constants.ADD_UPDATE_CART_ITEM, params, getApplicationContext(), new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                //  dismissDialog();
                Toast.makeText(MainActivity.this, "Added To Cart", Toast.LENGTH_LONG).show();
                showCartDialog();
                getProductBycategoryId(selectedCategoryID);

            }

            @Override
            public void onErrorFinished(String result) {
                // dismissDialog();

            }
        });

    }
    private void getUserDetail() {
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_USER, new HashMap<String, String>(), getApplicationContext(), new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                Profile profile = gson.fromJson(result, Profile.class);
                if (profile.code == 200) {
                    Profile.UserProfile userProfile = profile.userProfile;
                    PowerPreference.getDefaultFile().set("name",userProfile.name);
                    PowerPreference.getDefaultFile().set("photo", userProfile.photo);
                    if (TextUtils.isEmpty(profile.userProfile.latitude)){
                        Intent intent = new Intent(MainActivity.this,AddNewAddressActivity.class);
                        startActivity(intent);
                    }
                    else {
                        distance = Utility.getInstance().distance(Double.parseDouble(profile.userProfile.latitude),Double.parseDouble(profile.userProfile.logitude));
                        Toast.makeText(MainActivity.this,"Your Distance from current Location to Block B, Sector 11, Faridabad, Haryana 121006 " +
                                "in KM->" + distance,Toast.LENGTH_LONG).show();
                        getAllProductList(selectedCategoryID,true);
                    }
                    //textViewPoints.setText(profile.userProfile.point);
                    bidbatalPointItem.setTitle("Bidbatl Point " + profile.userProfile.point);
                    PowerPreference.getDefaultFile().set("points",profile.userProfile.point);
                }

            }

            @Override
            public void onErrorFinished(String result) {

            }
        });
    }

    @Override
    public void getProductBycategoryId(String id) {
        PowerPreference.getDefaultFile().set("motherPackId","");
        if (!id.equalsIgnoreCase(selectedCategoryID)) {
            cuurrentIndex = -1;

        }
        popularAdapter.notifyDataSetChanged();

        selectedCategoryID = id;

        if (selectedCategoryID.equalsIgnoreCase("0")) {
            getAllProductList(selectedCategoryID,false);
            return;
        }

        selectedCategoryID = id;
        getProductList(id,false);

    }

    @Override
    public void refreshProductList(int index) {
        if (currentActiveItem.indexOf(true) >= 0) {
            currentActiveItem.set(currentActiveItem.indexOf(true), false);
        }
        currentActiveItem.set(index, true);
//       topSellingAdapter.notifyItemChanged(index);
        topSellingAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, "9", menu);
        return true;
    }

    public void setCount(Context context, String count, Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.ic_group);
//        menuItem.setIcon(buildCounterDrawable(count,  R.drawable.ic_menu_gallery));
//        MenuItem menuItem = defaultMenu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

}
