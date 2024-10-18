package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class advertPosterAdapter extends RecyclerView.Adapter<advertPosterAdapter.ViewHolder>{

    ArrayList<AdvertisementDetails.posterModel> posterList;
    Context context;

    public advertPosterAdapter(ArrayList<AdvertisementDetails.posterModel> posterList, Context context) {
        this.posterList = posterList;
        this.context = context;
    }

    @NonNull
    @Override
    public advertPosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advert_places, parent, false);
        return new advertPosterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull advertPosterAdapter.ViewHolder holder, int position) {
        holder.posterCost.setText(posterList.get(position).getCost());
        holder.posterReach.setText(posterList.get(position).getReach());
        holder.posterPlace.setText(posterList.get(position).getPlace());
        holder.posterDescription.setText(posterList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView posterCost, posterReach, posterPlace, posterDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posterCost = itemView.findViewById(R.id.advertCost);
            posterReach = itemView.findViewById(R.id.posterReach);
            posterPlace = itemView.findViewById(R.id.posterPlace);
            posterDescription = itemView.findViewById(R.id.posterDescription);
        }
    }
}
