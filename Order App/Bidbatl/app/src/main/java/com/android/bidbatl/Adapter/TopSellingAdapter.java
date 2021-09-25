package com.android.bidbatl.Adapter;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.Interface.UpdateCartItem;
import com.android.bidbatl.Activity.PhotoBrowser;
import com.android.bidbatl.Model.ProductListProvider;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class TopSellingAdapter extends RecyclerView.Adapter<TopSellingAdapter.ViewHolder> {


    Context context;
    List<ProductListProvider.ProductListData> productListArray;
    UpdateCartItem updateCartItemInterface;
    List<Boolean> currentActiveItem = new ArrayList<>();
    int highlightedIndex;

    public TopSellingAdapter(Context context, List<ProductListProvider.ProductListData> productListArray, UpdateCartItem updateCartItem, List<Boolean> currentActiveItem, int selectedIndex) {
        this.context = context;
        this.productListArray = productListArray;
        this.updateCartItemInterface = updateCartItem;
        this.currentActiveItem = currentActiveItem;
        this.highlightedIndex = selectedIndex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_selling_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final int mPosition = viewHolder.getAdapterPosition();
        viewHolder.textProductName.setText(productListArray.get(mPosition).name);
//        viewHolder.textWeight.setText("  Unit: " + productListArray.get(mPosition).unit);
        viewHolder.textWeight.setText("/" + new Float(productListArray.get(mPosition).weight).toString().replaceAll("\\.?0*$", "")
                + productListArray.get(mPosition).unit);
        viewHolder.textPrice.setText("₹" + new Float(productListArray.get(mPosition).mrp).toString().replaceAll("\\.?0*$", "")
        );
        if (productListArray.get(i).cfc_stock.equalsIgnoreCase("0")) {
            viewHolder.textCFC.setText("");
        } else {
            viewHolder.textCFC.setText(productListArray.get(mPosition).cfc_stock);

        }

        viewHolder.textViewMrp.setText("₹" + productListArray.get(mPosition).price);
        viewHolder.buttonAdd.setText("₹" + productListArray.get(mPosition).price);


        viewHolder.textCFC.setText(productListArray.get(mPosition).cfc_quantity);
        if (productListArray.get(mPosition).product_cart_count == null || productListArray.get(mPosition).product_cart_count.equalsIgnoreCase("null")) {
            viewHolder.editTextQnty.setText("");
            viewHolder.textViewCartQnty.setText("");
            viewHolder.backgroundView.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.textViewCartQnty.setText("In cart: " +productListArray.get(mPosition).product_cart_count);
            viewHolder.editTextQnty.setText(productListArray.get(mPosition).product_cart_count);
            viewHolder.backgroundView.setBackgroundColor(context.getResources().getColor(R.color.hilightedColor));
        }
        //viewHolder.editTextQnty.setText(productListArray.get(i).cfc_stock);
        viewHolder.textCategory.setText(productListArray.get(i).category_name);
        viewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCartItemInterface.refreshProductList(i);

            }
        });
        viewHolder.addCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCartItemInterface.didUpdateCartItemFromMainActivity(productListArray.get(mPosition), viewHolder.editTextQnty.getText().toString(), mPosition);
            }
        });
        if (productListArray.get(mPosition).photo != null) {
            Glide.with(context)
                    .load(Uri.parse(String.valueOf(Constants.IMAGE_BASE_URL + productListArray.get(mPosition).photo)))
                    .into(viewHolder.imageViewItem);
            viewHolder.imageViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PhotoBrowser.class);
                    intent.putExtra("galleryURL",Constants.IMAGE_BASE_URL + productListArray.get(mPosition).photo);
                    context.startActivity(intent);
                }
            });
        } else {
            viewHolder.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }

        viewHolder.textViewOrderingUnit.setText(productListArray.get(mPosition).ordering_unit);

//        if (highlightedIndex == mPosition) {
//            viewHolder.backgroundView.setBackgroundColor(context.getResources().getColor(R.color.hilightedColor));
//        } else {
//            viewHolder.backgroundView.setBackgroundColor(Color.WHITE);
//        }
        if (currentActiveItem.get(i) == true) {
            viewHolder.qantityLayout.setVisibility(View.VISIBLE);

        } else {
            viewHolder.qantityLayout.setVisibility(View.GONE);
        }


    }

    private void showDialog(View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.quantity_dialog);
        TextView dialogButton = (TextView) dialog.findViewById(R.id.textView_submit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return productListArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textWeight, textCategory, textPrice, textCFC, addCartItem, textViewMrp,textViewPriceCalculation;
        Button buttonAdd;
        EditText editTextQnty;
        ImageView imageViewItem;
        ConstraintLayout qantityLayout;
        TextView textViewOrderingUnit,textViewCartQnty;
        ConstraintLayout backgroundView;

        List<String> list = new ArrayList<String>();

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
            textViewCartQnty = itemView.findViewById(R.id.textView_cart_qnty);
            textViewCartQnty.setVisibility(View.VISIBLE);

            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            qantityLayout = itemView.findViewById(R.id.quantity);
            textViewOrderingUnit = itemView.findViewById(R.id.spinner);
            backgroundView = itemView.findViewById(R.id.backgroundView);
            textViewPriceCalculation = itemView.findViewById(R.id.textView_price_calculation);
            list.add("Unit");
            list.add("Ltr");
            list.add("Gms");
            textViewMrp = itemView.findViewById(R.id.textview_mrp);
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
//                    R.layout.spiner_item, list);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            textViewOrderingUnit.setAdapter(dataAdapter);
        }
    }
}
