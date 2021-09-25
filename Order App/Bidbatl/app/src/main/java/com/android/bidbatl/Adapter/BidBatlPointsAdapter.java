package com.android.bidbatl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bidbatl.Model.BidBatlPoints;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Utility;

import java.util.List;

public class BidBatlPointsAdapter extends RecyclerView.Adapter<BidBatlPointsAdapter.ViewHolder> {

    Context context;
    List<BidBatlPoints.PointList> bidBatlPoints;

    public BidBatlPointsAdapter(Context context, List<BidBatlPoints.PointList> populardataProviders) {
        this.context = context;
        this.bidBatlPoints = populardataProviders;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bidbatlpoints_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BidBatlPoints.PointList list = bidBatlPoints.get(i);

        if (list.type.equals("order")) {
            viewHolder.textViewType.setText("Type: Purchase");
        } else if (list.type.equals("register")) {
            viewHolder.textViewType.setText("Type: Sign Up");
        } else {
            viewHolder.textViewType.setText("Type: " + list.type);
        }
        if (list.total_point == null) {
            viewHolder.textViewBalancePoint.setText("BB Balance Point: N/A");

        } else {
//            int balance = Integer.parseInt(list.total_point) + Integer.parseInt(list.point);
            viewHolder.textViewBalancePoint.setText("BB Balance Point:" + list.total_point);

        }


        if (list.activity.equals("add")) {
            if (list.order_number == null) {
                viewHolder.textView_orderId.setText("Point added on: " + getActivityType(list.type));

            } else {
                viewHolder.textView_orderId.setText("Order# " + (list.order_number));

            }

            viewHolder.textView_coins.setText("+ " + (list.point));
            viewHolder.textView_coins.setTextColor(Color.GREEN);
            viewHolder.textView_date.setText("Credited on " + (Utility.formatDate(list.created_at)));
        } else {
            viewHolder.textView_orderId.setText("Redemption on Order# " + (list.order_number));

            viewHolder.textView_coins.setText("- " + (list.point));
            viewHolder.textView_coins.setTextColor(Color.RED);
            viewHolder.textView_date.setText("Debited on " + (Utility.formatDate(list.created_at)));
        }
    }

    String getActivityType(String type) {
        if (type.equals("kyc")) {
            return "KYC";
        } else if (type.equals("register")) {
            return "Sign Up";

        } else {
            return type;
        }

    }

    @Override
    public int getItemCount() {
        return bidBatlPoints.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_orderId, textView_date, textView_coins, textViewBalancePoint, textViewType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_orderId = itemView.findViewById(R.id.textView_orderId);
            textView_coins = itemView.findViewById(R.id.textView_bb_points);
            textView_date = itemView.findViewById(R.id.textView_credited_date);
            textViewBalancePoint = itemView.findViewById(R.id.textView_balance_point);
            textViewType = itemView.findViewById(R.id.textView_transc_type);
        }
    }

}

