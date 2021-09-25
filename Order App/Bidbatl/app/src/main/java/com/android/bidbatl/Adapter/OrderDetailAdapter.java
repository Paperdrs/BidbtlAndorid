package com.android.bidbatl.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Model.OrderDetails;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    Context context;
    List<OrderDetails.OrderDetailList> orderDetailLists;

    public OrderDetailAdapter(Context context, List<OrderDetails.OrderDetailList> orderDetailLists) {
        this.context = context;
        this.orderDetailLists = orderDetailLists;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_detail_row,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.textProductName.setText(orderDetailLists.get(i).name);
        viewHolder.textWeight.setText("/" +new Float(orderDetailLists.get(i).weight).toString().replaceAll("\\.?0*$", "")
        +"gms");
        viewHolder.textPrice.setText(Constants.rupee+new Float(orderDetailLists.get(i).unit_price).toString().replaceAll("\\.?0*$", "")
        );
        viewHolder.textViewQty.setText(orderDetailLists.get(i).quantity);
        Float unit_price = 0f;
        Float total = 0f;
        Integer quantity = 0;

        try {
            unit_price = Float.parseFloat(orderDetailLists.get(i).unit_price);
            quantity = Integer.parseInt(orderDetailLists.get(i).quantity);
            total = unit_price*quantity;

        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        String totalAmount  = orderDetailLists.get(i).unit_price + "X"  + orderDetailLists.get(i).quantity + "= " + total;

        viewHolder.textViewTotalUnitPrice.setText(Constants.rupee+totalAmount);
        viewHolder.textViewOrderNo.setText(orderDetailLists.get(i).number);
        viewHolder.textViewOrderQty.setText("Quantity:" + orderDetailLists.get(i).quantity);
        if (orderDetailLists.get(i).product_image != null){
            Glide.with(context)
                    .load(Uri.parse(String.valueOf(orderDetailLists.get(i).product_image)))
                    .into(viewHolder.imageViewItem);
        }
        else {viewHolder.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }

    }

    @Override
    public int getItemCount() {
        return orderDetailLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName,textWeight,textPrice,textViewTotalUnitPrice,textViewQty,textViewOrderNo,textViewOrderQty;
        ImageView imageViewItem;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.textView_product_name);
            textWeight = itemView.findViewById(R.id.textView_weight);
            textPrice = itemView.findViewById(R.id.textView_price);
            textViewTotalUnitPrice = itemView.findViewById(R.id.textView_unit_amount);
            textViewQty = itemView.findViewById(R.id.textView_qty);
            textViewOrderNo = itemView.findViewById(R.id.textView_order_id);

            textViewOrderQty = itemView.findViewById(R.id.textView_order_qty);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);

        }
    }
}

