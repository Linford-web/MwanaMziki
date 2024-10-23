package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.viewServices;

import java.util.ArrayList;

public class cateringAdapter extends RecyclerView.Adapter<cateringAdapter.ViewHolder> {

    ArrayList<ServicesDetails.cateringModel> cateringList;
    Context context;

    public cateringAdapter(ArrayList<ServicesDetails.cateringModel> cateringList, Context context) {
        this.cateringList = cateringList;
        this.context = context;
    }

    @NonNull
    @Override
    public cateringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_catering, null);
        return new cateringAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cateringAdapter.ViewHolder holder, int position) {
        ServicesDetails.cateringModel catering = cateringList.get(position);
        holder.cuisine.setText(catering.getCuisine());
        holder.packageName.setText(catering.getPackaged());
        holder.service.setText(catering.getService());
        holder.status.setText(catering.getStatus());
        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", catering.getProductId());
                intent.putExtra("service", "Catering");
                intent.putExtra("creatorId", catering.getCreatorId());
                intent.putExtra("productsId", catering.getProductId());
                intent.putExtra("image", catering.getImage());
                context.startActivity(intent);
            }
        });
        String posterUrl = catering.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.fastfood_icon)
                    .error(R.drawable.fastfood_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("eventAdapter", "Context is null or activity is destroyed");
            holder.posterTv.setImageResource(R.drawable.fastfood_icon);
        }

    }

    @Override
    public int getItemCount() {
        return cateringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterTv;
        TextView cuisine, packageName ,status, service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posterTv = itemView.findViewById(R.id.item_icon);
            cuisine = itemView.findViewById(R.id.item_cuisine);
            packageName = itemView.findViewById(R.id.packages);
            status = itemView.findViewById(R.id.item_status);
            service = itemView.findViewById(R.id.item_service);

        }
    }
}
