package com.example.eventmuziki.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class bidEventsAdapter extends RecyclerView.Adapter<bidEventsAdapter.ViewHolder> {

    ArrayList<biddersEventModel> bookedEvents;

    public bidEventsAdapter(ArrayList<biddersEventModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, bidderName, bidAmount;
        ImageView eventPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            bidderName = itemView.findViewById(R.id.musicianName);
            bidAmount = itemView.findViewById(R.id.bidAmount);
            eventPoster = itemView.findViewById(R.id.cardTv);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bid_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bidEventsAdapter.ViewHolder holder, int position) {

        biddersEventModel bookedEvent = bookedEvents.get(position);

        holder.eventName.setText(bookedEvent.getEventName());
        holder.bidAmount.setText(bookedEvent.getBidAmount());

        String bidderId = bookedEvent.getCreatorID();

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

        String eventId = bookedEvent.getEventId();

        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String eventPoster = documentSnapshot.getString("eventPoster");

                            if (eventPoster != null && !eventPoster.isEmpty()) {
                                Glide.with(holder.itemView.getContext())
                                        .load(eventPoster)
                                        .into(holder.eventPoster);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }
}
