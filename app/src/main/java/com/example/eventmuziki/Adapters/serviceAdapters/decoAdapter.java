package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

public class decoAdapter extends RecyclerView.Adapter<decoAdapter.ViewHolder> {

    ArrayList<ServicesDetails.decorationModel> decorationList;
    Context context;

    public decoAdapter(ArrayList<ServicesDetails.decorationModel> decorationList, Context context) {
        this.decorationList = decorationList;
        this.context = context;
    }

    @NonNull
    @Override
    public decoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_added_service, parent, false);
        return new decoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull decoAdapter.ViewHolder holder, int position) {
        ServicesDetails.decorationModel decoration = decorationList.get(position);
        // Set the decoration details in the views
        holder.name.setText(decoration.getName());
        holder.packageName.setText(decoration.getPackage());
        holder.status.setText(decoration.getStatus());
        // Set the click listener for the book button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
            }
        });
        // Load the decoration poster using a library like Glide or Picasso
        String posterUrl = decoration.getImage();
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.error_icon)
                    .error(R.drawable.error_icon)
                    .into(holder.posterTv);
        }else {
            Log.d("eventAdapter", "Context is null or activity is destroyed");
        }
    }

    @Override
    public int getItemCount() {
        return decorationList.size();
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
