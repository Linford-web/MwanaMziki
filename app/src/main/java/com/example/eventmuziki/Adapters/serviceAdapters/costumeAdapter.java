package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class costumeAdapter extends RecyclerView.Adapter<costumeAdapter.ViewHolder> {
    ArrayList<ServicesDetails.costumeModel> costumeList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, status;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.item_icon);
            productName = itemView.findViewById(R.id.item_name);
            productPrice = itemView.findViewById(R.id.item_amount);
            status = itemView.findViewById(R.id.item_status);
        }
    }
    public costumeAdapter(ArrayList<ServicesDetails.costumeModel> costumeList, Context context) {
        this.costumeList = costumeList;
        this.context = context;
    }

    @NonNull
    @Override
    public costumeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_costume, viewGroup, false);
        return new costumeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull costumeAdapter.ViewHolder holder, int position) {
        ServicesDetails.costumeModel service = costumeList.get(position);
        holder.productName.setText(service.getCostumeName());
        holder.productPrice.setText(service.getMaterial());
        holder.status.setText(service.getStatus());

       String imageUrl = service.getImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.productImage.setImageResource(R.drawable.tshirt);
        }
        // load image using Glide
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.error_icon)
                    .error(R.drawable.error_icon)
                    .into(holder.productImage);
        }

    }

    @Override
    public int getItemCount() {
        return costumeList.size();
    }
}
