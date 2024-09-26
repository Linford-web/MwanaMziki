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

    ArrayList<bookedEventsModel> bookedEvents;
    String bidderEmail, profileImageUrl, phone;

    public musicianNameAdapter(ArrayList<bookedEventsModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        CardView chat;
        TextView name, category;
        DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.cardTv);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            chat = itemView.findViewById(R.id.container);
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

        bookedEventsModel bookedEvent = bookedEvents.get(position);

        holder.name.setText(bookedEvent.getBiddersName());

        String bidderId = bookedEvent.getBiddersId();
        String bidId = bookedEvent.getBidId();

        // Retrieve profile photo URL,email and phone from FireStore for the bidder
        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userid", bidderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            // Retrieve profile photo URL from FireStore
                            profileImageUrl = document.getString("profilePicture");
                            bidderEmail = document.getString("email");
                            phone = document.getString("number");

                            holder.category.setText(bidderEmail);

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

                                fStore.collection("BookedEvents")
                                        .whereEqualTo("bidId", bidId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    holder.chat.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(holder.itemView.getContext(), chatRoom.class);
                                                            intent.putExtra("userId", bookedEvent.getBiddersId());
                                                            intent.putExtra("userName", bookedEvent.getBiddersName());
                                                            intent.putExtra("userEmail", bidderEmail);
                                                            intent.putExtra("userImage", profileImageUrl);
                                                            intent.putExtra("userPhone", phone);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            holder.itemView.getContext().startActivity(intent);

                                                        }
                                                    });
                                                }else {
                                                    Toast.makeText(holder.itemView.getContext(), "Failed To create chat room with bidder", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(holder.itemView.getContext(), "Failed To fetch Bid ID", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                Toast.makeText(holder.itemView.getContext(), "Musicians Cannot Start Chats", Toast.LENGTH_SHORT).show();
                                // Handle musician user type here
                                Intent intent = new Intent(holder.itemView.getContext(), chatRoom.class);
                                intent.putExtra("userId", bookedEvent.getCreatorID());
                                intent.putExtra("userName", bookedEvent.getOrganizerName());
                                intent.putExtra("userEmail", bidderEmail);
                                intent.putExtra("userImage", profileImageUrl);
                                intent.putExtra("userPhone", phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                holder.itemView.getContext().startActivity(intent);
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


