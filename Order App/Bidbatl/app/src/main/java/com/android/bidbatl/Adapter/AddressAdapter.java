package com.android.bidbatl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.Interface.AddressInterface;
import com.android.bidbatl.Activity.AddNewAddressActivity;
import com.android.bidbatl.Model.Address;
import com.android.bidbatl.R;

import java.io.Serializable;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    Context context;
    List<Address.AddressList> addressLists;
    AddressInterface addressInterface;
    Intent intent;

    public AddressAdapter(Context context, List<Address.AddressList> populardataProviders,AddressInterface addressInterface,Intent intent) {
        this.context = context;
        this.addressLists = populardataProviders;
        this.addressInterface = addressInterface;
        this.intent = intent;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.textViewName.setText(addressLists.get(i).name);
        viewHolder.textViewMob.setText(addressLists.get(i).phone);
        viewHolder.textViewAddress.setText(addressLists.get(i).address+" "+addressLists.get(i).area+" "+addressLists.get(i).state
        +"-" + addressLists.get(i).zip_code+" "+addressLists.get(i).city);
        if (addressLists.get(i).is_default == 1){
           viewHolder.textViewDefault.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.textViewDefault.setVisibility(View.INVISIBLE);
        }

//        Glide.with(context)
//                .load(Uri.parse(String.valueOf(productListProviders.get(i).getImage())))
//                .into(viewHolder.textCategoryName);

        viewHolder.imageViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                popup.getMenuInflater().inflate(R.menu.option_menu,popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id==R.id.set_default){
                            addressInterface.setDefault(addressLists.get(i).id,addressLists.get(i).type);

                        }else if(id==R.id.edit){
                            Intent intent = new Intent(context, AddNewAddressActivity.class);
                            intent.putExtra("editAddress", (Serializable) addressLists.get(i));
                            context.startActivity(intent);

                        }
                        else if(id==R.id.delete){
                            addressInterface.deleteAddress(addressLists.get(i).id);
                        }
                        return true;
                    }
                });
            }
        });
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intent.getIntExtra("selectionType",0) == 0){
                    addressInterface.getSelectedAddress(addressLists.get(i));

                }
                else {
                    intent.putExtra("SELECTED_ADD", addressLists.get(i));


                    ((Activity) context).setResult(((Activity) context).RESULT_OK,
                            intent);
                    ((Activity) context).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageViewOption;
        TextView textViewName,textViewAddress,textViewMob,textViewDefault;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOption = itemView.findViewById(R.id.imageView_option);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewAddress = itemView.findViewById(R.id.textView_address);
            textViewMob = itemView.findViewById(R.id.textView_mob);
            textViewDefault = itemView.findViewById(R.id.textView_default);
            constraintLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}

