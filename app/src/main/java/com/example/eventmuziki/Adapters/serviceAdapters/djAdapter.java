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

public class djAdapter extends RecyclerView.Adapter<djAdapter.ViewHolder> {

    ArrayList<ServicesDetails.soundModel> soundList;
    Context context;

    public djAdapter(ArrayList<ServicesDetails.soundModel> soundList, Context context) {
        this.soundList = soundList;
        this.context = context;
    }

    @NonNull
    @Override
    public djAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_service, parent, false);
        return new djAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull djAdapter.ViewHolder holder, int position) {
        ServicesDetails.soundModel sound = soundList.get(position);
        // Set the sound details in the views
        holder.name.setText(sound.getName());
        holder.packageName.setText(sound.getArea());
        holder.status.setText(sound.getStatus());
        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", sound.getProductId());
                intent.putExtra("service", "Sound");
                intent.putExtra("creatorId", sound.getCreatorId());
                intent.putExtra("productsId", sound.getProductId());
                context.startActivity(intent);
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = sound.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.dj_icon)
                    .error(R.drawable.dj_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("eventAdapter", "Context is null or activity is destroyed");
        }
    }

    @Override
    public int getItemCount() {
        return soundList.size();
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
