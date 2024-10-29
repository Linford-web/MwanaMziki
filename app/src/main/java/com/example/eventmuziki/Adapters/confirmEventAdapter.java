package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.managePayment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class confirmEventAdapter extends RecyclerView.Adapter<confirmEventAdapter.ViewHolder> {

    ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents;
    Context context;

    public confirmEventAdapter(ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }


    @NonNull
    @Override
    public confirmEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_event, parent, false);
        return new confirmEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmEventAdapter.ViewHolder holder, int position) {

        ServicesDetails.bookedBiddersModel bookedBidders = bookedEvents.get(position);

        holder.amount.setText(bookedBidders.getBidAmount());
        holder.name.setText(bookedBidders.getOrganizerName());

        String eventId = bookedBidders.getEventId();

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
                                Glide.with(context)
                                        .load(eventPoster)
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.cover)
                                        .into(holder.poster);
                            }else {
                                Log.d("eventAdapter", "Context is null or activity is destroyed");
                                holder.poster.setImageResource(R.drawable.cover);
                            }

                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());


        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name, amount;
        CheckBox check;
        CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.posterTv);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            check = itemView.findViewById(R.id.checkBox);
            container = itemView.findViewById(R.id.container);
        }
    }
}
