package com.example.eventmuziki.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class musicianNameAdapter extends RecyclerView.Adapter<musicianNameAdapter.ViewHolder> {

    ArrayList<bookedEventsModel> bookedEvents;

    public musicianNameAdapter(ArrayList<bookedEventsModel> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.cardTv);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);

        }
    }


    @NonNull
    @Override
    public musicianNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booked_musician, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull musicianNameAdapter.ViewHolder holder, int position) {

        bookedEventsModel bookedEvent = bookedEvents.get(position);

        holder.name.setText(bookedEvent.getBiddersName());
        holder.category.setText(bookedEvent.getCategory());

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }


}
