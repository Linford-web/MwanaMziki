package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.confirmPaymentModel;
import com.example.eventmuziki.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmedAdapter extends RecyclerView.Adapter<confirmedAdapter.ViewHolder> {

    ArrayList<confirmPaymentModel> confirmPaymentModels;
    Context context;

    public confirmedAdapter(ArrayList<confirmPaymentModel> confirmPaymentModels, Context context) {
        this.confirmPaymentModels = confirmPaymentModels;
        this.context = context;
    }

    @NonNull
    @Override
    public confirmedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_confirmed, parent, false);
        return new confirmedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmedAdapter.ViewHolder holder, int position) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        confirmPaymentModel model = confirmPaymentModels.get(position);

        holder.amount.setText(model.getAmount());
        holder.status.setText(model.getStatus());

        String bidderId = model.getBidderId();
        String creatorId = model.getCreatorId();

        loadEventPoster(fStore, holder, model.getEventId());
        loadProfileImage(fStore, bidderId, holder.profile);

        loadUserName(fStore, model.getBidderId(), holder.bidderNames);
        loadUserName(fStore, model.getCreatorId(), holder.creatorNames);

        loadAndAdjustUIByUserType(fStore, userId, holder);

    }

    private void loadProfileImage(FirebaseFirestore fStore, String bidderId, CircleImageView profile) {
        fStore.collection("Users").document(bidderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImage = documentSnapshot.getString("profilePicture");

                        if (profileImage != null && !profileImage.isEmpty()) {
                            Glide.with(profile.getContext())
                                    .load(profileImage)
                                    .placeholder(R.drawable.profile_image)
                                    .error(R.drawable.profile_image)
                                    .into(profile);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("confirmedAdapter", "Error getting profile image: " + e.getMessage());
                    }
                });
    }

    private void loadEventPoster(FirebaseFirestore fStore, ViewHolder holder, String eventId) {
        fStore.collection("Events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String poster = documentSnapshot.getString("eventPoster");
                        if (poster != null && !poster.isEmpty()) {
                            Glide.with(holder.itemView.getContext())
                                    .load(poster)
                                    .placeholder(R.drawable.cover)
                                    .error(R.drawable.cover)
                                    .into(holder.poster);
                        } else {
                            Log.e("confirmEventAdapter", "Poster Not Uploaded");
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadUserName(FirebaseFirestore fStore, String userId, TextView userNameTextView) {
        fStore.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        userNameTextView.setText(name);
                    }
                }).addOnFailureListener(e -> Toast.makeText(userNameTextView.getContext(), "Error getting user name", Toast.LENGTH_SHORT).show());
    }


    private void loadAndAdjustUIByUserType(FirebaseFirestore fStore, String userId, ViewHolder holder) {
        fStore.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userType = documentSnapshot.getString("userType");
                        if ("Musician".equalsIgnoreCase(userType)) {
                            holder.creatorNames.setVisibility(View.VISIBLE);
                            holder.bidderNames.setVisibility(View.GONE);
                            holder.posterTv.setVisibility(View.VISIBLE);
                            holder.profile.setVisibility(View.GONE);
                        } else if ("Corporate".equalsIgnoreCase(userType)) {
                            holder.creatorNames.setVisibility(View.GONE);
                            holder.bidderNames.setVisibility(View.VISIBLE);
                            holder.posterTv.setVisibility(View.GONE);
                            holder.profile.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Error getting user type", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return confirmPaymentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView posterTv;
        ImageView poster;
        CircleImageView profile;
        TextView creatorNames, bidderNames, amount;
        CheckBox checkBox;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posterTv = itemView.findViewById(R.id.poster);
            poster = itemView.findViewById(R.id.posterTv);
            profile = itemView.findViewById(R.id.cardTv);
            creatorNames = itemView.findViewById(R.id.creatorNames);
            bidderNames = itemView.findViewById(R.id.bidderNames);
            amount = itemView.findViewById(R.id.amount);
            checkBox = itemView.findViewById(R.id.checkBox);
            status = itemView.findViewById(R.id.status);

        }
    }

}
