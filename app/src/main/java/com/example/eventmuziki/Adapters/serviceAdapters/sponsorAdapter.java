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

public class sponsorAdapter extends RecyclerView.Adapter<sponsorAdapter.ViewHolder> {

    ArrayList<ServicesDetails.sponsorModel> sponsorList;
    Context context;

    public sponsorAdapter(ArrayList<ServicesDetails.sponsorModel> sponsorList, Context context) {
        this.sponsorList = sponsorList;
        this.context = context;
    }

    @NonNull
    @Override
    public sponsorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_service, parent, false);
        return new sponsorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sponsorAdapter.ViewHolder holder, int position) {

        ServicesDetails.sponsorModel sponsor = sponsorList.get(position);
        holder.name.setText(sponsor.getName());
        holder.packageName.setText(sponsor.getType());
        holder.status.setText(sponsor.getStatus());

        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", sponsor.getProductId());
                intent.putExtra("service", "Sponsors");
                intent.putExtra("creatorId", sponsor.getCreatorId());
                intent.putExtra("productsId", sponsor.getProductId());
                context.startActivity(intent);
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = sponsor.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.sponsorship_icon)
                    .error(R.drawable.sponsorship_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("eventAdapter", "Context is null or activity is destroyed");
        }

    }

    @Override
    public int getItemCount() {
        return sponsorList.size();
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
