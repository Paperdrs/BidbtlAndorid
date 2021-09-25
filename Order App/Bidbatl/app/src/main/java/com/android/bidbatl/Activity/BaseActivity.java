package com.android.bidbatl.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.bidbatl.R;
import com.bumptech.glide.Glide;

import java.util.Objects;
import java.util.Vector;

public class BaseActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    Dialog dialog;
    private Vector<Dialog> dialogs = new Vector<Dialog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public String formattedPrice(float price) {
        return new Float(price).toString().replaceAll("\\.?0*$", "");

    }

    public void showCircleDialog() {
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

    }
    public View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    public void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showCartDialog() {
        final Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.cart_success_popup);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.show();
        ImageView imageViewSuccess = mDialog.findViewById(R.id.imageView_success);
        Glide.with(this).asGif().load(R.drawable.success).into(imageViewSuccess);

        // Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        };
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                handler.removeCallbacks(runnable);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(mDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        handler.postDelayed(runnable, 2000);


    }

    public void dismisCircleDialog() {
        dialog.dismiss();
    }

    //    fun showDialog() {
//        dialog = Dialog(this)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.loading_dialog)
//        dialog.show()
//    }
//
//    fun dismissDialog() {
//        dialog.dismiss()
//    }
    public void showDialog(String text) {
        showCircleDialog();

//        int llpadding = 30;
//        LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.HORIZONTAL);
//        ll.setPadding(llpadding, llpadding, llpadding, llpadding);
//        ll.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
//        ll.setLayoutParams(layoutParams);
//        layoutParams = new LinearLayout.LayoutParams(100, 200);
//        layoutParams.setMargins(10, 0, 10, 0);
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setIndeterminate(true);
//        progressBar.setLayoutParams(layoutParams);
//        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(30, 0, 10, 0);
//        layoutParams.gravity = Gravity.CENTER_VERTICAL;
//        TextView tv = new TextView(this);
//        tv.setText(text);
//        tv.setTextColor(Color.parseColor("#000000"));
//        tv.setTextSize(16);
//        tv.setLayoutParams(layoutParams);
//        ll.addView(progressBar);
//        ll.addView(tv);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setView(ll);
//        alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
        dialogs.add(dialog);
//        if(!this.isFinishing())
//        {
//            alertDialog.show();
//        }


    }

    public void dismissDialog() {
        for (Dialog dialog : dialogs)
            if (dialog.isShowing()) dialog.dismiss();
    }
}
