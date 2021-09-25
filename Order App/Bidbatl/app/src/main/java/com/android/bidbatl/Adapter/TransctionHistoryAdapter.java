package com.android.bidbatl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bidbatl.Model.MyTransctionHiistory;
import com.android.bidbatl.R;
import com.android.bidbatl.Utility.Constants;

import java.util.List;

public class TransctionHistoryAdapter extends RecyclerView.Adapter<TransctionHistoryAdapter.ViewHolder>{

    Context context;
    List<MyTransctionHiistory> transctionHistories;

    public TransctionHistoryAdapter(Context context, List<MyTransctionHiistory> populardataProviders) {
        this.context = context;
        this.transctionHistories = populardataProviders;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transction,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewDate.setText(transctionHistories.get(i).timestamp);
        viewHolder.textViewAmount.setText(transctionHistories.get(i).amount);
        viewHolder.textViewOrderNumber.setText(Constants.rupee+transctionHistories.get(i).orderNumber);
        viewHolder.textViewTransctionType.setText(transctionHistories.get(i).transaction_type);
    }

    @Override
    public int getItemCount() {
        return transctionHistories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTransctionType, textViewOrderNumber, textViewAmount, textViewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTransctionType = itemView.findViewById(R.id.textView_title_ts);
            textViewOrderNumber = itemView.findViewById(R.id.textView_sub_title_ts);
            textViewAmount = itemView.findViewById(R.id.textView_amount_ts);
            textViewDate = itemView.findViewById(R.id.textView_date_ts);
        }
    }
}

