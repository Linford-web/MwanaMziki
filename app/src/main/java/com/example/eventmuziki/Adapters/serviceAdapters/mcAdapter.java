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

public class mcAdapter extends RecyclerView.Adapter<mcAdapter.ViewHolder> {

    ArrayList<ServicesDetails.mcModel> mcList;
    Context context;

    public mcAdapter(ArrayList<ServicesDetails.mcModel> mcList, Context context) {
        this.mcList = mcList;
        this.context = context;
    }

    @NonNull
    @Override
    public mcAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_service, parent, false);
        return new mcAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mcAdapter.ViewHolder holder, int position) {

        ServicesDetails.mcModel mc = mcList.get(position);

        holder.name.setText(mc.getSpecialization());
        holder.packageName.setText(mc.getEventRole());
        holder.status.setText(mc.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", mc.getProductId());
                intent.putExtra("service", "Emcee");
                intent.putExtra("creatorId", mc.getCreatorId());
                intent.putExtra("productsId", mc.getProductId());
                intent.putExtra("image", mc.getImage());
                context.startActivity(intent);
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = mc.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.mc_icon)
                    .error(R.drawable.mc_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("mcAdapter", "Context is null or activity is destroyed");
        }

    }

    @Override
    public int getItemCount() {
        return mcList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterTv;
        TextView name, packageName ,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posterTv = itemView.findViewById(R.id.poster_Tv);
            name = itemView.findViewById(R.id.event_Name);
            packageName = itemView.findViewById(R.id.item_package);
            status = itemView.findViewById(R.id.item_status);

        }
    }
}
