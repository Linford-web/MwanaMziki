package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class serviceDetailAdapter extends RecyclerView.Adapter<serviceDetailAdapter.ViewHolder> {

    ArrayList<ServicesDetails.serviceDetail> serviceDetailList;
    Context context;

    public serviceDetailAdapter(ArrayList<ServicesDetails.serviceDetail> serviceDetailList, Context context) {
        this.serviceDetailList = serviceDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public serviceDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_musician, parent, false);
        return new serviceDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull serviceDetailAdapter.ViewHolder holder, int position) {
        ServicesDetails.serviceDetail serviceDetail = serviceDetailList.get(position);
        holder.category.setText(serviceDetail.getCategory());

        FirebaseFirestore.getInstance().collection("Users")
                .document(serviceDetail.getCreatorId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        holder.userName.setText(name);
                        String imageUrl = documentSnapshot.getString("profilePicture");
                        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
                            Glide.with(context)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.error_icon)
                                    .error(R.drawable.error_icon)
                                    .into(holder.userServiceImage);

                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return serviceDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userServiceImage;
        TextView userName, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userServiceImage = itemView.findViewById(R.id.cardTv);
            userName = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);

        }
    }
}
