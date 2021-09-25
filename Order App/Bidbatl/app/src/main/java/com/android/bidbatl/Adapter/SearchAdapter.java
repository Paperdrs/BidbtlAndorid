package com.android.bidbatl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bidbatl.Model.SearchDataProvider;
import com.android.bidbatl.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    Context context;
    List<SearchDataProvider> populardataProviders;

    public SearchAdapter(Context context, List<SearchDataProvider> populardataProviders) {
        this.context = context;
        this.populardataProviders = populardataProviders;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        Glide.with(context)
//                .load(Uri.parse(String.valueOf(productListProviders.get(i).getImage())))
//                .into(viewHolder.textCategoryName);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView imageViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.textView11);
        }
    }
}

