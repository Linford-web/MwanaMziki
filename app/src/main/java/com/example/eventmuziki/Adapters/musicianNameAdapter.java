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
import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatActivity;
import com.example.eventmuziki.chatRoom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class musicianNameAdapter extends RecyclerView.Adapter<musicianNameAdapter.ViewHolder> {

    ArrayList<bookedEventsModel> bookedEvents;
    String user_name, user_email;

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
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");

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
        holder.category.setText(bookedEvent.getCategory());

        String bidderId = bookedEvent.getBiddersId();
        String categories = bookedEvent.getCategory();


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
                            String profileImageUrl = document.getString("profilePicture");

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                // Load profile photo into otherImageView using Glide or any other image loading library
                                Glide.with(holder.itemView.getContext())
                                        .load(profileImageUrl)
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.poster1)
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

        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("userid", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            if (document.exists()) {
                                user_name = document.getString("name");
                                user_email = document.getString("email");

                            }else {
                                Toast.makeText(holder.itemView.getContext(), "User documents not found", Toast.LENGTH_SHORT).show();
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


        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = bookedEvent.getBiddersId();
                String chatRoomId = UUID.randomUUID().toString();
                String name = bookedEvent.getBiddersName();
                String email = user_email;
                String eventID = bookedEvent.getEventId();

                if (userId == null || name == null || email == null) {
                    Toast.makeText(holder.itemView.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                chatUserModel bidderChatRoom = new chatUserModel("NotSet", eventID, chatRoomId, userId, name, email);
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                dbReference.child(userId).setValue(bidderChatRoom)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Start chatRoom activity with necessary data
                                Intent intent = new Intent(holder.itemView.getContext(), chatActivity.class);
                                intent.putExtra("roomId", chatRoomId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("email", email);
                                intent.putExtra("userName", name);
                                intent.putExtra("eventId", eventID);
                                intent.putExtra("category", categories);
                                holder.itemView.getContext().startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.itemView.getContext(), "Failed to Create Chat User", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }


}
