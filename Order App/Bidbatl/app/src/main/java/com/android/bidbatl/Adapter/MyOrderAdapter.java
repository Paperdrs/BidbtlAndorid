package com.android.bidbatl.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Activity.OrderDetailActivity;
import com.android.bidbatl.Model.MyOrder;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Utility;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    Context context;
    List<MyOrder.MyOrderList> myOrderList;

    public MyOrderAdapter(Context context, List<MyOrder.MyOrderList> myOrders) {
        this.context = context;
        this.myOrderList = myOrders;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_row,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.textViewPaymentStatus.setText(capitalize(myOrderList.get(i).payment_status));
        viewHolder.textViewDate.setText(Utility.formatDate(myOrderList.get(i).created_at));
        viewHolder.textViewOrderNo.setText(myOrderList.get(i).orderNumber);
        if (myOrderList.get(i).payment_status.equals("accepted") || myOrderList.get(i).status.equals("delivered")) {
            viewHolder.textViewStatus.setText("Delivered");
        }
        else {
            viewHolder.textViewStatus.setText(capitalize(myOrderList.get(i).status));

        }
        viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("orderID",myOrderList.get(i).id);
                intent.putExtra("status",myOrderList.get(i).status);
                intent.putExtra("invoice_url",myOrderList.get(i).invoice_url);
                intent.putExtra("payment_status",myOrderList.get(i).payment_status);
                context.startActivity(intent);
            }
        });

//        Glide.with(context)
//                .load(Uri.parse(String.valueOf(productListProviders.get(i).getImage())))
//                .into(viewHolder.textCategoryName);
    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPaymentStatus,textViewDate,textViewOrderNo,textViewStatus;
        ConstraintLayout rowLayout;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPaymentStatus = itemView.findViewById(R.id.textView_payment_status);
            textViewDate = itemView.findViewById(R.id.textView_date);
            textViewOrderNo = itemView.findViewById(R.id.textView_order_number);
            textViewStatus = itemView.findViewById(R.id.textView_status);
            rowLayout = itemView.findViewById(R.id.row_layout);
            imageView = itemView.findViewById(R.id.imageView12);
            imageView.setVisibility(View.GONE);
        }
    }
}

