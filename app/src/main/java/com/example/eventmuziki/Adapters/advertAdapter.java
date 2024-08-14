package com.example.eventmuziki.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.advertisementModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.viewAdvert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class advertAdapter extends RecyclerView.Adapter<advertAdapter.ViewHolder> {

    ArrayList<advertisementModel> adverts;

    public advertAdapter(ArrayList<advertisementModel> adverts) {
        this.adverts = adverts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, eventDate, eventTime;
        ImageView advertPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            advertPoster = itemView.findViewById(R.id.posterTv);
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

        advertisementModel advert = adverts.get(position);
    holder.eventName.setText(advert.getEventName());
        holder.eventLocation.setText(advert.getEventLocation());
        holder.eventDate.setText(advert.getEventDate());
        holder.eventTime.setText(advert.getEventTime());

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
                                        .centerCrop()
                                        .into(holder.advertPoster);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                Intent intent = new Intent(v.getContext(), viewAdvert.class);
                intent.putExtra("eventName", advert.getEventName());
                intent.putExtra("eventLocation", advert.getEventLocation());
                intent.putExtra("eventDate", advert.getEventDate());
                intent.putExtra("eventTime", advert.getEventTime());
                intent.putExtra("organizerName", advert.getOrganizerName());
                intent.putExtra("advertPlans", advert.getAdvertPlans());
                intent.putExtra("eventId", advert.getEventId());
                intent.putExtra("eventPoster", advert.getPosterUrl());
                intent.putExtra("eventDetails", advert.getDetails());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

}
