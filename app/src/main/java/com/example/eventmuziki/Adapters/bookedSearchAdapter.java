package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.eventmuziki.eventBidding;
import com.example.eventmuziki.viewBookedEvents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class bookedSearchAdapter extends FirestoreRecyclerAdapter<bookedEventsModel, bookedSearchAdapter.ViewHolder> {

    Context context;

    public bookedSearchAdapter(FirestoreRecyclerOptions<bookedEventsModel> option, Context context) {
        super(option);
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, date;
        ImageView posterTv;
        CardView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            date = itemView.findViewById(R.id.eventDate);
            posterTv = itemView.findViewById(R.id.bigCardTv);
            view = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull bookedEventsModel model) {

        holder.eventName.setText(model.getEventName());
        holder.date.setText(model.getDate());

        String eventId = model.getEventId();

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewBookedEvents.class);
                intent.putExtra("eventModel", model);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_view3, parent, false);
        return new ViewHolder(view);
    }
}

