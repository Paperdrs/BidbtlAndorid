package com.android.bidbatl.Activity;

import android.app.SearchManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.android.bidbatl.Adapter.SearchAdapter;
import com.android.bidbatl.Model.SearchDataProvider;
import com.android.bidbatl.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
RecyclerView recyclerView;
SearchAdapter searchAdapter;
    List<SearchDataProvider> dataproviders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initRecyclerView();
    }
    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclervw_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        searchAdapter = new SearchAdapter(getApplicationContext(),dataproviders);
        recyclerView.setAdapter(searchAdapter);
        handleSearch();


    }
    private void handleSearch() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            searchAdapter = new SearchAdapter(getApplicationContext(),dataproviders);
            recyclerView.setAdapter(searchAdapter);

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String selectedSuggestionRowId =  intent.getDataString();
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item
            Toast.makeText(this, "selected search suggestion "+selectedSuggestionRowId,
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch();
    }
}
