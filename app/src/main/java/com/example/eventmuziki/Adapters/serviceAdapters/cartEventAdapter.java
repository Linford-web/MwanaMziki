package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class cartEventAdapter extends RecyclerView.Adapter<cartEventAdapter.ViewHolder> {

    Context context;
    ArrayList<eventModel> events;
    cartEventAdapter.OnItemClickListener listener;

    public cartEventAdapter(ArrayList<eventModel> events, Context context) {
        this.events = events;
        this.context = context;
    }

    // add click listener to the view holder
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(cartEventAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{

        TextView eventName, eventDate, eventLocation;
        LinearLayout container;
        ImageView posterTv;
        ImageButton bookBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            container = itemView.findViewById(R.id.eventContainer);
            posterTv = itemView.findViewById(R.id.cardTv);
            bookBtn = itemView.findViewById(R.id.bookBtn);
        }
    }

    @NonNull
    @Override
    public cartEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_book, parent, false);
        return new cartEventAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull cartEventAdapter.ViewHolder holder, int position) {
        // Get the current event
        eventModel event = events.get(position);
        // Set the event details in the views
        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(event.getDate());
        holder.eventLocation.setText(event.getLocation());

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

                            if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
                                Glide.with(context)
                                        .load(eventPoster)
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.cover)
                                        .into(holder.posterTv);

                            }else {
                                Log.d("cartEventAdapter", "Context is null or activity is destroyed");
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        holder.bookBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    
}
