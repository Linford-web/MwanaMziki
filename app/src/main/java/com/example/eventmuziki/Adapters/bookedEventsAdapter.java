package com.example.eventmuziki.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.eventBidding;
import com.example.eventmuziki.viewBookedEvents;

import java.util.ArrayList;

public class bookedEventsAdapter extends RecyclerView.Adapter<bookedEventsAdapter.ViewHolder> {

    ArrayList<bookedEventsModel> bookedEvents;

    public bookedEventsAdapter(ArrayList<bookedEventsModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, location, date, time;
        ImageView posterTv;
        CardView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            location = itemView.findViewById(R.id.eventLocation);
            date = itemView.findViewById(R.id.eventDate);
            time = itemView.findViewById(R.id.eventTime);
            posterTv = itemView.findViewById(R.id.bigCardTv);
            view = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public bookedEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booked_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookedEventsAdapter.ViewHolder holder, int position) {

        bookedEventsModel bookedEvent = bookedEvents.get(position);

        holder.eventName.setText(bookedEvent.getEventName());
        holder.location.setText(bookedEvent.getLocation());
        holder.date.setText(bookedEvent.getDate());
        holder.time.setText(bookedEvent.getTime());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(v.getContext(), viewBookedEvents.class);
                intent.putExtra("bookedEventsModel", bookedEvent);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }
}
