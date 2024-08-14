package com.example.eventmuziki.Adapters.serviceAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.carModel;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class carAdapter extends RecyclerView.Adapter<carAdapter.ViewHolder> {
    ArrayList<carModel> carList;

    public carAdapter(ArrayList<carModel> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public carAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car, viewGroup, false);
        return new carAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull carAdapter.ViewHolder viewHolder, int i) {
        carModel service = carList.get(i);
        viewHolder.carModel.setText(service.getCar_model());
        viewHolder.carSeats.setText(service.getSeats());
        viewHolder.carStatus.setText(service.getStatus());
        viewHolder.carCategory.setText(service.getCar_type());
        // load image using Glide or Picasso
        Glide.with(viewHolder.itemView.getContext())
                .load(R.drawable.car_icon)
                .into(viewHolder.carImage);

    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView carImage;
        TextView carModel, carSeats, carStatus, carCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carImage = itemView.findViewById(R.id.item_icon);
            carModel = itemView.findViewById(R.id.item_model);
            carSeats = itemView.findViewById(R.id.item_seats);
            carStatus = itemView.findViewById(R.id.item_status);
            carCategory = itemView.findViewById(R.id.item_category);

        }
    }
}
