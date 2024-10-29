package com.example.eventmuziki.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.cartActivity;
import com.example.eventmuziki.managePayment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmAdapter extends RecyclerView.Adapter<confirmAdapter.ViewHolder> {

    ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents;
    Context context;

    public confirmAdapter(ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    @NonNull
    @Override
    public confirmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_bidder, parent, false);
        context = parent.getContext();
        return new confirmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmAdapter.ViewHolder holder, int position) {

        ServicesDetails.bookedBiddersModel bookedBidders = bookedEvents.get(position);

        holder.amount.setText(bookedBidders.getBidAmount());
        holder.name.setText(bookedBidders.getBiddersName());

        String profileImageUrl = bookedBidders.getProfile();

        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            // Load profile photo into otherImageView using Glide or any other image loading library
            Glide.with(holder.itemView.getContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile_image)
                    .error(R.drawable.profile_image)
                    .into(holder.profile);

        } else {
            // Handle the case when no profile photo is available
            Log.e("musicianNameAdapter", "Profile Photo Not Uploaded");
        }

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name, amount;
        CheckBox check;
        CardView container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.cardTv);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            check = itemView.findViewById(R.id.checkBox);
            container = itemView.findViewById(R.id.container);

        }
    }
}
