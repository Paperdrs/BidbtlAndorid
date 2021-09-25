package com.android.bidbatl.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.Interface.MotherPackEvent;
import com.android.bidbatl.Activity.CategoryActivity;
import com.android.bidbatl.Model.Category;
import com.android.bidbatl.Model.MotherPackModel;
import com.android.bidbatl.R;
import com.bumptech.glide.Glide;
import com.preference.PowerPreference;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    Context context;
    Activity activity;
    List<MotherPackModel.CategoryList> populardataProviders;

    public CategoryAdapter(Context context, List<MotherPackModel.CategoryList> populardataProviders,Activity activity) {
        this.context = context;
        this.populardataProviders = populardataProviders;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.textView_category.setText(populardataProviders.get(i).name);
        if (PowerPreference.getDefaultFile().getString("motherPackId").equals(populardataProviders.get(i).id)){
            viewHolder.layoutBg.setBackgroundResource(R.drawable.kyc_border);

        }
        else {
            viewHolder.layoutBg.setBackgroundResource(R.drawable.white_bg);

        }
        Glide.with(context).load(populardataProviders.get(i).image_url).placeholder(context.getResources().getDrawable(R.drawable.neuronature)).into(viewHolder.imageViewcategory);
        viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerPreference.getDefaultFile().set("motherPackId",populardataProviders.get(i).id);
                activity.finish();

            }
        });


//        Glide.with(context)
//                .load(Uri.parse(String.valueOf(productListProviders.get(i).getImage())))
//                .into(viewHolder.textCategoryName);
    }
    private int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return populardataProviders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_category;
        CardView rowLayout;
        ImageView imageViewcategory;
        ConstraintLayout layoutBg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           textView_category = itemView.findViewById(R.id.textView_category);
           rowLayout = itemView.findViewById(R.id.row_layout);
            imageViewcategory = itemView.findViewById(R.id.imageView_category);
            layoutBg = itemView.findViewById(R.id.layout_bg);
        }
    }
}

