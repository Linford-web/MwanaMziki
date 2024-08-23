package com.example.eventmuziki.Adapters.serviceAdapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceModels.ServicesDetails;

import java.util.ArrayList;

public class photoAdapter extends RecyclerView.Adapter<photoAdapter.ViewHolder> {

    ArrayList<ServicesDetails.photoModel> photoList;

    public photoAdapter(ArrayList<ServicesDetails.photoModel> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public photoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull photoAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
