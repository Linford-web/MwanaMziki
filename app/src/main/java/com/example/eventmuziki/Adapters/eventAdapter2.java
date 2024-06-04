package com.example.eventmuziki.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.eventBidding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class eventAdapter2 extends RecyclerView.Adapter<eventAdapter2.ViewHolder2> {

    ArrayList<eventModel> events;

    public eventAdapter2(ArrayList<eventModel> events) {
        this.events = events;
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView eventName, eventDate, eventTime, eventLocation;
        LinearLayout container;
        ImageView posterTv;


        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            container = itemView.findViewById(R.id.container2);
            posterTv = itemView.findViewById(R.id.bigCardTv);


        }
    }

    @NonNull
    @Override
    public eventAdapter2.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_view2, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull eventAdapter2.ViewHolder2 holder, int position) {

        // Get the current event
        eventModel event = events.get(position);
        // Set the event details in the views
        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(event.getDate());
        holder.eventTime.setText(event.getTime());
        holder.eventLocation.setText(event.getLocation());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(v.getContext(), eventBidding.class);
                intent.putExtra("eventModel", event);
                v.getContext().startActivity(intent);
            }
        });

        String eventId = event.getEventId();

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
                                        .into(holder.posterTv);
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
        return events.size();
    }
}
