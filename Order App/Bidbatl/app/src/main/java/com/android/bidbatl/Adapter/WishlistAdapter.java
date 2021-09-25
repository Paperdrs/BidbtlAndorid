package com.android.bidbatl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.bidbatl.Model.Wishlist;
import com.android.bidbatl.R;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{

    Context context;
    List<Wishlist> wishlistList;

    public WishlistAdapter(Context context, List<Wishlist> wishlists) {
        this.context = context;
        this.wishlistList = wishlists;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_wishlist,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.qantityLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewitemTitel,textViewWeight,textViewDelivery;
        Button buttonAdd;
        ImageView imageViewItem;
        ConstraintLayout qantityLayout;
        Spinner spinnerUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewitemTitel = itemView.findViewById(R.id.textViewitemTitel);
            textViewWeight = itemView.findViewById(R.id.textViewWeight);
            textViewDelivery = itemView.findViewById(R.id.textViewDelivery);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            qantityLayout = itemView.findViewById(R.id.quantity);
            spinnerUnit = itemView.findViewById(R.id.spinner);
            List<String> list = new ArrayList<String>();
            list.add("Unit");
            list.add("Ltr");
            list.add("Gms");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.spiner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerUnit.setAdapter(dataAdapter);


        }
    }
}

