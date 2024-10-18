package com.example.eventmuziki.Adapters;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventAdvertModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.viewAdvert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class advertAdapter extends RecyclerView.Adapter<advertAdapter.ViewHolder> {

    ArrayList<eventAdvertModel> adverts;

    public advertAdapter(ArrayList<eventAdvertModel> adverts) {
        this.adverts = adverts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, eventDate, eventTime, eventOrganizer, eventDetails;
        ImageView advertPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            advertPoster = itemView.findViewById(R.id.posterTv);
            eventOrganizer = itemView.findViewById(R.id.eventOrganizer);
            eventDetails = itemView.findViewById(R.id.eventDetails);
        }
    }

    @NonNull
    @Override
    public advertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_advertisement, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull advertAdapter.ViewHolder holder, int position) {

        eventAdvertModel advert = adverts.get(position);
        holder.eventName.setText(advert.getEventName());
        holder.eventLocation.setText(advert.getEventLocation());
        holder.eventDate.setText(advert.getEventDate());
        holder.eventTime.setText(advert.getEventTime());
        holder.eventOrganizer.setText(advert.getOrganizerName());
        holder.eventDetails.setText(advert.getDetails());

        // Load advert poster image using Glide or any other image loading library
        String eventId = advert.getEventId();

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
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.cover)
                                        .into(holder.advertPoster);
                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

}
