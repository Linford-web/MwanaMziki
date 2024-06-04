package com.example.eventmuziki.Adapters;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.bookedEventModel;
import com.example.eventmuziki.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class biddersAdapter extends RecyclerView.Adapter<biddersAdapter.ViewHolder> {

    ArrayList<bookedEventModel> bidders;

    public biddersAdapter(ArrayList<bookedEventModel> bidders) {
        this.bidders = bidders;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView bidderImage;
        TextView bidAmount, bidderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bidderImage = itemView.findViewById(R.id.bidderImage);
            bidAmount = itemView.findViewById(R.id.bidAmount);
            bidderName = itemView.findViewById(R.id.bidderName);
        }
    }

    @NonNull
    @Override
    public biddersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bidders, parent, false);
        return new biddersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull biddersAdapter.ViewHolder holder, int position) {

        bookedEventModel bidder = bidders.get(position);

        holder.bidAmount.setText(bidder.getBidAmount());

        String bidderId = bidder.getBiddersId();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userid", bidderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            String name = document.getString("name");

                            holder.bidderName.setText(name);

                            // Retrieve profile photo URL from FireStore
                            String profileImageUrl = document.getString("profilePicture");

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                // Load profile photo into otherImageView using Glide or any other image loading library
                                Glide.with(holder.itemView.getContext())
                                        .load(profileImageUrl)
                                        .into(holder.bidderImage);

                            } else {
                                // Handle the case when no profile photo is available
                                Log.e("studentDetailsAdapter", "Profile Photo Not Uploaded");
                            }


                        } else {
                            Toast.makeText(holder.itemView.getContext(), "User document not found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), "Failed To fetch Students", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return bidders.size();
    }
}
