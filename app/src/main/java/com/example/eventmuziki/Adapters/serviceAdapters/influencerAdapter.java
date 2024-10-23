package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.eventmuziki.viewServices;

import java.util.ArrayList;

public class influencerAdapter extends RecyclerView.Adapter<influencerAdapter.influencerViewHolder>{

    ArrayList<ServicesDetails.influencerModel> influencerList;
    Context context;

    public influencerAdapter(ArrayList<ServicesDetails.influencerModel> influencerList, Context context) {
        this.influencerList = influencerList;
        this.context = context;
    }

    @NonNull
    @Override
    public influencerAdapter.influencerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_service, parent, false);
        return new influencerAdapter.influencerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull influencerAdapter.influencerViewHolder holder, int position) {
        ServicesDetails.influencerModel content = influencerList.get(position);
        holder.name.setText(content.getHandle());
        holder.packageName.setText(content.getPlatform());
        holder.status.setText(content.getStatus());

        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", content.getProductId());
                intent.putExtra("service", "Influencer");
                intent.putExtra("creatorId", content.getCreatorId());
                intent.putExtra("productsId", content.getProductId());
                intent.putExtra("image", content.getImage());
                context.startActivity(intent);
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = content.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.content_icon)
                    .error(R.drawable.content_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("eventAdapter", "Context is null or activity is destroyed");
        }
    }

    @Override
    public int getItemCount() {
        return influencerList.size();
    }

    public class influencerViewHolder extends RecyclerView.ViewHolder {
        ImageView posterTv;
        TextView name, packageName ,status;

        public influencerViewHolder(@NonNull View itemView) {
            super(itemView);
            posterTv = itemView.findViewById(R.id.poster_Tv);
            name = itemView.findViewById(R.id.event_Name);
            packageName = itemView.findViewById(R.id.item_package);
            status = itemView.findViewById(R.id.item_status);

        }
    }
}
