package com.android.bidbatl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bidbatl.Activity.PhotoBrowser;
import com.android.bidbatl.Model.KYCDataProvider;
import com.android.bidbatl.R;
import com.bumptech.glide.Glide;
import com.preference.PowerPreference;

import java.util.List;

public class KycAdapter extends RecyclerView.Adapter<KycAdapter.ViewHolder> {

    Context context;
    List<KYCDataProvider.KYCList> addressLists;

    public KycAdapter(Context context, List<KYCDataProvider.KYCList> populardataProviders) {
        this.context = context;
        this.addressLists = populardataProviders;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kyc_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private String keepNumbersOnly(CharSequence s) {
        return s.toString().replaceAll("[^0-9]", " "); // Should of course be more robust
    }

    private String formatNumbersAsCode(CharSequence s) {
        int groupDigits = 0;
        String tmp = "";
        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                tmp += "-";
                groupDigits = 0;
            }
        }
        return tmp;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.textViewName.setText(addressLists.get(i).name);
        if (addressLists.get(i).type.equalsIgnoreCase("aadhaar")) {
            viewHolder.textViewKycNoHeading.setText("Aadhar Card Number");
            viewHolder.textViewKYCNO.setText(keepNumbersOnly(formatNumbersAsCode(addressLists.get(i).id_number.toUpperCase())));
        } else if (addressLists.get(i).type.equalsIgnoreCase("pan")) {
            viewHolder.textViewKYCNO.setText(addressLists.get(i).id_number.toUpperCase());
            viewHolder.textViewKycNoHeading.setText("Pan Card Number");

        } else {
            viewHolder.textViewKYCNO.setText(addressLists.get(i).id_number.toUpperCase());
            viewHolder.textViewKycNoHeading.setText("GST Number");
        }
        viewHolder.textKYCType.setText(addressLists.get(i).type.toUpperCase());
        if (addressLists.get(i).photo.length() == 0) {
            viewHolder.imageViewKYC.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.imageViewKYC.setVisibility(View.VISIBLE);
            Glide.with(context).load(addressLists.get(i).photo).placeholder(R.drawable.user_icon_placeholder).into(viewHolder.imageViewKYC);
            viewHolder.imageViewKYC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PhotoBrowser.class);
                    intent.putExtra("galleryURL",addressLists.get(i).photo);
                    context.startActivity(intent);
                }
            });
        }
        if (addressLists.get(i).status.equalsIgnoreCase("accepted")) {
            viewHolder.imageViewVerify.setImageDrawable(context.getResources().getDrawable(R.drawable.verified_green));
            viewHolder.textViewVerify.setTextColor(context.getResources().getColor(R.color.green_color));
            viewHolder.textViewVerify.setText("Verified");
        }
        else if (addressLists.get(i).status.equalsIgnoreCase("rejected")) {
            viewHolder.imageViewVerify.setImageDrawable(context.getResources().getDrawable(R.drawable.reject));
            viewHolder.textViewVerify.setTextColor(context.getResources().getColor(R.color.red_color));
            viewHolder.textViewVerify.setText("Rejected");
        }
        else {
            viewHolder.imageViewVerify.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted));
            viewHolder.textViewVerify.setTextColor(context.getResources().getColor(R.color.blue_color));
            viewHolder.textViewVerify.setText("Submitted");

        }

    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        Button editKYC;
        TextView textViewName, textViewKYCNO, textKYCType, textViewKycNoHeading, textViewVerify;
        ImageView imageViewKYC, imageViewVerify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewKYC = itemView.findViewById(R.id.imageView_user);
            textViewName = itemView.findViewById(R.id.textView_kyc_name);
            textViewKYCNO = itemView.findViewById(R.id.textView_kyc_num);
            textKYCType = itemView.findViewById(R.id.textView_kyc_type);
            textViewKycNoHeading = itemView.findViewById(R.id.textView_type_title);
            imageViewVerify = itemView.findViewById(R.id.imageView_verify);
            textViewVerify = itemView.findViewById(R.id.textView_verify);

        }
    }
}

