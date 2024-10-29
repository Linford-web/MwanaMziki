package com.example.eventmuziki.Adapters;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

        TextView eventName, bidderName, bidAmount, delete;
        ImageView eventPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            bidderName = itemView.findViewById(R.id.musicianName);
            bidAmount = itemView.findViewById(R.id.bidAmount);
            eventPoster = itemView.findViewById(R.id.cardTv);
            delete = itemView.findViewById(R.id.delete);
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
        holder.bidderName.setText(bookedEvent.getBiddersName());

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
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.cover)
                                        .into(holder.eventPoster);
                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action here
                String bidId = bookedEvent.getBidId();
                FirebaseFirestore.getInstance().collection("BidEvents")
                        .document(bidId)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Show the popup dialog after deletion
                                showPopupDialog(holder.itemView);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }

    private void showPopupDialog(View view) {
        Dialog popupDialog = new Dialog(view.getContext());
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setCancelable(false);
        popupDialog.setContentView(R.layout.popup_layout);

        if (popupDialog.getWindow() != null) {
            popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        popupDialog.show();

        new Handler().postDelayed(() -> {
            if (popupDialog.isShowing()) {
                popupDialog.dismiss();
            }
        }, 3000);
    }

}
