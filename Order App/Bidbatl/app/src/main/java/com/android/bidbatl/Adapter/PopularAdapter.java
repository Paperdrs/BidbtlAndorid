package com.android.bidbatl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.Interface.UpdateCartItem;
import com.android.bidbatl.Model.Category;
import com.android.bidbatl.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder>{

    Context context;
    List<Category.CategoryList> categoryLists;
    UpdateCartItem itemInterface;
    int selected_position = 0;

    public PopularAdapter(Context context, List<Category.CategoryList> categoryLists, UpdateCartItem itemInterface) {
        this.context = context;
        this.categoryLists = categoryLists;
        this.itemInterface = itemInterface;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_brand_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.textCategoryName.setText(categoryLists.get(i).name);
        viewHolder.textCategoryName.setBackgroundResource(selected_position == i ? R.drawable.rounded_button_orange : R.drawable.rounded_button_orange_border);
        viewHolder.textCategoryName.setTextColor(selected_position == i ? context.getResources().getColor(R.color.background_color) : context.getResources().getColor(R.color.orangeColor));

        viewHolder.textCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selected_position = i;

                itemInterface.getProductBycategoryId(categoryLists.get(i).id);

            }
        });

//        Glide.with(context)
//                .load(Uri.parse(String.valueOf(productListProviders.get(i).getImage())))
//                .into(viewHolder.textCategoryName);
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCategoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName = itemView.findViewById(R.id.textView_category_name);
        }
    }
}

