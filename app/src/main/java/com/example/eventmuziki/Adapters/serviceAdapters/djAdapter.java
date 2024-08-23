package com.example.eventmuziki.Adapters.serviceAdapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceModels.ServicesDetails;

import java.util.ArrayList;

public class djAdapter extends RecyclerView.Adapter<djAdapter.ViewHolder> {

    ArrayList<ServicesDetails.soundModel> soundList;

    public djAdapter(ArrayList<ServicesDetails.soundModel> soundList) {
        this.soundList = soundList;
    }

    @NonNull
    @Override
    public djAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull djAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
