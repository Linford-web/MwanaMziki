package com.example.eventmuziki.Adapters.serviceAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.costumeModel;
import com.example.eventmuziki.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class costumeAdapter extends RecyclerView.Adapter<costumeAdapter.ViewHolder> {
    ArrayList<costumeModel> costumeList;

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
    public costumeAdapter(ArrayList<costumeModel> costumeList) {
        this.costumeList = costumeList;
    }

    @NonNull
    @Override
    public costumeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_costume, viewGroup, false);
        return new costumeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull costumeAdapter.ViewHolder holder, int position) {
        costumeModel service = costumeList.get(position);
        holder.productName.setText(service.getProductName());
        holder.productPrice.setText(service.getProductPrice());
        holder.status.setText(service.getStatus());

        FirebaseFirestore.getInstance().collection("Services")
                .document()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {

                                Glide.with(holder.itemView.getContext())
                                        .load(service.getImages().get(0))
                                        .into(holder.productImage);

                            }else {
                                Toast.makeText(holder.itemView.getContext(), "No images uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            // Handle failure
                            e.printStackTrace();
                        });

    }

    @Override
    public int getItemCount() {
        return costumeList.size();
    }
}
