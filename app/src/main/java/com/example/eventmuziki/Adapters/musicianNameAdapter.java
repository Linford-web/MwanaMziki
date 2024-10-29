package com.example.eventmuziki.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class musicianNameAdapter extends RecyclerView.Adapter<musicianNameAdapter.ViewHolder> {

    ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents;

    public musicianNameAdapter(ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile, rating, message;
        CardView chat;
        TextView name, category;
        DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.cardTv);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            chat = itemView.findViewById(R.id.container);
            // rating = itemView.findViewById(R.id.ratingBtn);
            message = itemView.findViewById(R.id.messageBtn);
            databaseReference = FirebaseDatabase.getInstance().getReference("ChatRooms");

        }
    }

    @NonNull
    @Override
    public musicianNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booked_musician, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull musicianNameAdapter.ViewHolder holder, int position) {

        ServicesDetails.bookedBiddersModel bookedBidders = bookedEvents.get(position);

        holder.name.setText(bookedBidders.getBiddersName());
        holder.category.setText(bookedBidders.getBiddersEmail());

        String bidderId = bookedBidders.getBiddersId();
        String bidderEmail = bookedBidders.getBiddersEmail();
        String phone = bookedBidders.getBiddersPhone();

        String profileImageUrl = bookedBidders.getProfile();

        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            // Load profile photo into otherImageView using Glide or any other image loading library
            Glide.with(holder.itemView.getContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile_icon)
                    .error(R.drawable.profile_icon)
                    .into(holder.profile);

        } else {
            // Handle the case when no profile photo is available
            Log.e("musicianNameAdapter", "Profile Photo Not Uploaded");
        }


        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if ("Corporate".equals(userType)) {
                                holder.message.setVisibility(View.VISIBLE);

                                holder.message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(holder.itemView.getContext(), chatRoom.class);
                                        intent.putExtra("userId", bidderId);
                                        intent.putExtra("userName", bookedBidders.getBiddersName());
                                        intent.putExtra("userEmail", bidderEmail);
                                        intent.putExtra("userImage", profileImageUrl);
                                        intent.putExtra("userPhone", phone);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        holder.itemView.getContext().startActivity(intent);

                                    }
                                });

                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                holder.message.setVisibility(View.GONE);
                            } else {
                                // Handle other user types if needed
                                Log.d("TAG", "User is neither Corporate nor Musician");
                            }

                        }
                    } else {
                        Log.e("musicianNameAdapter", "Error getting document", task.getException());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }


}


