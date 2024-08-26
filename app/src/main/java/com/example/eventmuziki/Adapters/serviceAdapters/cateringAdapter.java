package com.example.eventmuziki.Adapters.serviceAdapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceModels.ServicesDetails;

import java.util.ArrayList;

public class cateringAdapter extends RecyclerView.Adapter<cateringAdapter.ViewHolder> {

    ArrayList<ServicesDetails.cateringModel> cateringList;

    public cateringAdapter(ArrayList<ServicesDetails.cateringModel> cateringList) {
        this.cateringList = cateringList;
    }

    @NonNull
    @Override
    public cateringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull cateringAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cateringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
