package com.example.eventmuziki.Adapters.serviceAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class carAdapter extends RecyclerView.Adapter<carAdapter.ViewHolder> {
    ArrayList<ServicesDetails.carModel> carList;
    Context context;

    public carAdapter(ArrayList<ServicesDetails.carModel> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull
    @Override
    public carAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car, viewGroup, false);
        return new carAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull carAdapter.ViewHolder viewHolder, int init) {
        ServicesDetails.carModel service = carList.get(init);
        viewHolder.carModel.setText(service.getCar_model());
        viewHolder.carSeats.setText(service.getSeats());
        viewHolder.carStatus.setText(service.getStatus());
        viewHolder.carCategory.setText(service.getCar_type());

        String imageUrl = service.getCarImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            viewHolder.carImage.setImageResource(R.drawable.car_icon);
            return;
        }
        // load image using Glide or Picasso
        if (context != null && context instanceof Activity && !((Activity) context).isDestroyed()) {
            Glide.with(viewHolder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.error_icon)
                    .error(R.drawable.error_icon)
                    .into(viewHolder.carImage);
        }

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
