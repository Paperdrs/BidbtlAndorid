package com.android.bidbatl.Activity;

import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Adapter.CategoryAdapter;
import com.android.bidbatl.Model.Category;
import com.android.bidbatl.Model.MotherPackModel;
import com.android.bidbatl.Model.ProductListProvider;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.APIManager;
import com.android.bidbatl.Utility.Constants;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends BaseActivity {
RecyclerView recyclerViewategory;
CategoryAdapter categoryAdapter;
MotherPackModel category;

List<MotherPackModel.CategoryList> dataProvider = new ArrayList<>();
private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initTolbar();
    }
    private void initTolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView_title = toolbar.findViewById(R.id.title);
        ImageView imageViewBack = toolbar.findViewById(R.id.toolbar_back);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(backClick);
        textView_title.setText("Brands");
        initRecyclerView();
        initGSON();
        getMotherPack();
    }
    private void initGSON(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd");
        gson = gsonBuilder.create();
    }

    private void getMotherPack(){
        showDialog("");
        APIManager.getInstance().callAPI(Request.Method.GET, Constants.BASEURL + Constants.GET_MOTHER_PACK, new HashMap<String, String>(), this, new APIManager.APICallbackInterface() {
            @Override
            public void onSuccessFinished(String result) {
                dismissDialog();
                category = gson.fromJson(result, MotherPackModel.class);
                if (category.code == 200){
                    dataProvider = category.categoryLists;
                    Collections.sort(category.categoryLists, MotherPackModel.CategoryList.StuNameComparator);
                    if (dataProvider != null){
                        initRecyclerView();
                    }
                }

            }

            @Override
            public void onErrorFinished(String result) {
                dismissDialog();

            }
        });
    }
    private void initRecyclerView(){
        recyclerViewategory = findViewById(R.id.recyclervw_category);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),3);
        recyclerViewategory.setLayoutManager(manager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        recyclerViewategory.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        categoryAdapter = new CategoryAdapter(getApplicationContext(),dataProvider,CategoryActivity.this);
        recyclerViewategory.setAdapter(categoryAdapter);
    }



public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = space;
        }
    }
}
}