package com.android.bidbatl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.bidbatl.Activity.product.MainActivity;
import com.android.bidbatl.R;

public class OrderStatusActivity extends AppCompatActivity {
Button buttonContinueShopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        buttonContinueShopping = findViewById(R.id.button_continue_shopping);
        buttonContinueShopping.setOnClickListener(continueShoppingAction);
        initToolBar();

    }
    private void initToolBar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = toolbar.findViewById(R.id.title);
        textViewTitle.setText("Order Confirmed");

    }
    private View.OnClickListener continueShoppingAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navigateToHome();

        }
    };

    private void navigateToHome() {
        Intent intent = new Intent(OrderStatusActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToHome();
    }
}
