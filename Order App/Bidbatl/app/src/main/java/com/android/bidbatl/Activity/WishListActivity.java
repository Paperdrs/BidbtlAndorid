package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Adapter.WishlistAdapter;
import com.android.bidbatl.Model.Wishlist;
import com.android.bidbatl.R;

import java.util.List;

public class WishListActivity extends AppCompatActivity {
    ImageView imageViewCart;
    RecyclerView recyclerViewWishlist;
    WishlistAdapter wishlistAdapter;
    List<Wishlist> wishlists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        initToobar();
    }
    private void initToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Wishlist");
        imageViewCart = toolbar.findViewById(R.id.imageView_cart);
        initRecyclerView();
    }
    private void initRecyclerView(){
        recyclerViewWishlist = findViewById(R.id.recyclervw_wishlist);
        recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        wishlistAdapter = new WishlistAdapter(getApplicationContext(),wishlists);
        recyclerViewWishlist.setAdapter(wishlistAdapter);
    }
}
