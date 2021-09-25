package com.android.bidbatl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.Interface.CartItemInterface;
import com.android.bidbatl.Activity.PhotoBrowser;
import com.android.bidbatl.Model.Cart;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Constants;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;
    List<Cart.CartList> cartListArray;
    CartItemInterface cartItemInterface;
    int highlightedIndex ;
    List<Boolean>currentActiveItem = new ArrayList<>();

    public CartAdapter(Context context, List<Cart.CartList> cartList,CartItemInterface itemInterface,int highlightedIndex,List<Boolean> currentActiveItem) {
        this.context = context;
        this.cartListArray = cartList;
        this.cartItemInterface = itemInterface;
        this.highlightedIndex = highlightedIndex;
        this.currentActiveItem = currentActiveItem;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_selling_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.textProductName.setText(cartListArray.get(i).name);
//        viewHolder.textWeight.setText("   Unit: " + cartListArray.get(i).unit);
                viewHolder.textWeight.setText("/" + new Float(cartListArray.get(i).weight).toString().replaceAll("\\.?0*$", "")
                        +cartListArray.get(i).unit);
        viewHolder.textPrice.setText("₹" +new Float(cartListArray.get(i).mrp).toString().replaceAll("\\.?0*$", "")
        );
        double d = 0;
        Log.e("fff",cartListArray.get(i).price.replace(",",""));
        d = Double.parseDouble(cartListArray.get(i).price.replace(",",""));
        Float price = new Float(d);
        Float finalPrice = (price * (Integer.valueOf(cartListArray.get(i).cart_item_quantity)));
        String totalPrice = new Float(finalPrice).toString().replaceAll("\\.?0*$", "");

        viewHolder.textViewTotalItemPrice.setText("₹" + cartListArray.get(i).price);
        viewHolder.textCFC.setText(cartListArray.get(i).cfc_quantity);
        viewHolder.editTextQnty.setText(cartListArray.get(i).cart_item_quantity);
        viewHolder.textCategory.setText(cartListArray.get(i).category_name);
        viewHolder.buttonAdd.setText("₹" + totalPrice);
        viewHolder.textViewPriceCalculation.setText(Constants.rupee + cartListArray.get(i).price+"X" + cartListArray.get(i).cart_item_quantity + "=" + totalPrice);
        viewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemInterface.refreshProductList(i);

            }
        });
        viewHolder.addCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemInterface.didUpadetCartItem(cartListArray.get(i).product_id,viewHolder.editTextQnty.getText().toString(),i,cartListArray.get(i).price,cartListArray.get(i).cfc_stock);

            }
        });
        viewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemInterface.deleteCartItem(cartListArray.get(i).cart_id);
            }
        });

        if (cartListArray.get(i).product_image != null){
            Glide.with(context)
                    .load(Uri.parse(String.valueOf(cartListArray.get(i).product_image)))
                    .into(viewHolder.imageViewItem);
            viewHolder.imageViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PhotoBrowser.class);
                    intent.putExtra("galleryURL",cartListArray.get(i).product_image);
                    context.startActivity(intent);
                }
            });
        }
        else {viewHolder.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }
        viewHolder.textViewOrderingUnit.setText(cartListArray.get(i).ordering_unit);
        if (highlightedIndex == i){
            viewHolder.backgroundView.setBackgroundColor(context.getResources().getColor(R.color.hilightedColor));
        }
        else {
            viewHolder.backgroundView.setBackgroundColor(Color.WHITE);
        }
        if (currentActiveItem.get(i) == true){
            viewHolder.qantityLayout.setVisibility(View.VISIBLE);

        }
        else {
            viewHolder.qantityLayout.setVisibility(View.GONE);
        }

        if (cartListArray.get(i).status.equals("1")){
        viewHolder.textViewOutOfStock.setVisibility(View.GONE);
        }
        else {
            viewHolder.textViewOutOfStock.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cartListArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName,textWeight, textCategory,textPrice,textCFC,textViewTotalItemPrice,textViewPriceCalculation;
        Button buttonAdd;
        EditText editTextQnty;
        ImageView imageViewItem;
        ConstraintLayout qantityLayout;
        TextView addCartItem;
        TextView textViewOrderingUnit;
        ImageView deleteItem;
        ConstraintLayout backgroundView;
        TextView textViewOutOfStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.textViewitemTitel);
            textWeight = itemView.findViewById(R.id.textView_weight);
            textPrice = itemView.findViewById(R.id.textView_price);
            textCFC = itemView.findViewById(R.id.textView_cfc);
            textCategory = itemView.findViewById(R.id.textView_category);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            editTextQnty = itemView.findViewById(R.id.editText_qnty);
            addCartItem = itemView.findViewById(R.id.textView_addCartItem);
            deleteItem = itemView.findViewById(R.id.imageView_delete);
            deleteItem.setVisibility(View.VISIBLE);
            backgroundView = itemView.findViewById(R.id.backgroundView);
            textViewTotalItemPrice = itemView.findViewById(R.id.textview_mrp);
            textViewTotalItemPrice.setVisibility(View.GONE);
            textViewPriceCalculation = itemView.findViewById(R.id.textView_price_calculation);
            textViewPriceCalculation.setVisibility(View.VISIBLE);

            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            qantityLayout = itemView.findViewById(R.id.quantity);
            textViewOrderingUnit = itemView.findViewById(R.id.spinner);
            textViewOutOfStock = itemView.findViewById(R.id.textView18);

        }
    }
}

