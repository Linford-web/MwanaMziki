package com.example.eventmuziki.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.viewAdvert;

import java.util.ArrayList;

public class advertAdapter2 extends RecyclerView.Adapter<advertAdapter2.ViewHolder> {

    ArrayList<AdvertisementDetails.advertModel> advertList;
    Context context;

    public advertAdapter2(ArrayList<AdvertisementDetails.advertModel> advertList, Context context) {
        this.advertList = advertList;
        this.context = context;
    }

    @NonNull
    @Override
    public advertAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adverts, parent, false);
        return new advertAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull advertAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AdvertisementDetails.advertModel model = advertList.get(position);

        holder.advertTitle.setText(model.getTitle());
        holder.advertDuration.setText(model.getDuration());
        holder.advertDate.setText(model.getDate());
        holder.advertTime.setText(model.getTime());
        holder.advertDetails.setText(model.getDetails());

        // Set other views as needed
        String imageUrl = advertList.get(position).getImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.bigCardTv.setImageResource(R.drawable.cover);
        }
        // Load the image using a library like Glide or Picasso
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.cover)
                .error(R.drawable.cover)
                .into(holder.bigCardTv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here
                Intent intent = new Intent(context, viewAdvert.class);
                intent.putExtra("title", advertList.get(position).getTitle());
                intent.putExtra("duration", advertList.get(position).getDuration());
                intent.putExtra("date", advertList.get(position).getDate());
                intent.putExtra("time", advertList.get(position).getTime());
                intent.putExtra("details", advertList.get(position).getDetails());
                intent.putExtra("image", advertList.get(position).getImage());
                intent.putExtra("advertId", advertList.get(position).getAdvertId());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bigCardTv;
        TextView advertTitle, advertDuration, advertDate,advertTime, advertDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bigCardTv = itemView.findViewById(R.id.bigCardTv);
            advertTitle = itemView.findViewById(R.id.advertTitle);
            advertDuration = itemView.findViewById(R.id.advertDuration);
            advertDate = itemView.findViewById(R.id.advertDate);
            advertTime = itemView.findViewById(R.id.advertTime);
            advertDetails = itemView.findViewById(R.id.advertDetails);

        }
    }
}
