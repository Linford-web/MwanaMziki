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

public class makeUpAdapter extends RecyclerView.Adapter<makeUpAdapter.ViewHolder> {

    ArrayList<ServicesDetails.makeUpModel> makeUpList;
    Context context;

    public makeUpAdapter(ArrayList<ServicesDetails.makeUpModel> makeUpList, Context context) {
        this.makeUpList = makeUpList;
        this.context = context;
    }

    @NonNull
    @Override
    public makeUpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_service, parent, false);
        return new makeUpAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull makeUpAdapter.ViewHolder holder, int position) {

        ServicesDetails.makeUpModel beauty = makeUpList.get(position);

        holder.name.setText(beauty.getName());
        holder.packageName.setText(beauty.getMakeUpPackage());
        holder.status.setText(beauty.getStatus());
        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(context, viewServices.class);
                intent.putExtra("product", beauty.getProductId());
                intent.putExtra("service", "MakeUp");
                intent.putExtra("creatorId", beauty.getCreatorId());
                intent.putExtra("productsId", beauty.getProductId());
                intent.putExtra("image", beauty.getImage());
                context.startActivity(intent);
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = beauty.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.makeup_icon)
                    .error(R.drawable.makeup_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("makeupAdapter", "Context is null or activity is destroyed");
        }
    }

    @Override
    public int getItemCount() {
        return makeUpList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
