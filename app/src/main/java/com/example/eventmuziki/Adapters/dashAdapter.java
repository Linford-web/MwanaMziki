package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceNameModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.addServices;
import com.example.eventmuziki.categoryOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class dashAdapter extends RecyclerView.Adapter<dashAdapter.ViewHolder> {

    Context context;
    private ArrayList<serviceNameModel> dashList;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String itemName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceIcon;
        TextView serviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceIcon = itemView.findViewById(R.id.service_icon);
            serviceName = itemView.findViewById(R.id.service_name);
        }
    }

    public dashAdapter( Context context, ArrayList<serviceNameModel> serviceList) {
        this.context = context;
        this.dashList = serviceList;
    }

    @NonNull
    @Override
    public dashAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dash, viewGroup, false);
        return new dashAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull dashAdapter.ViewHolder holder, int viewType) {
        serviceNameModel serviceName = dashList.get(viewType);
        holder.serviceName.setText(serviceName.getServiceName());
        holder.serviceIcon.setImageResource(serviceName.getServiceIcon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (serviceName.getActivityClass() != null) {
                   Intent intent = new Intent(context, serviceName.getActivityClass());
                   context.startActivity(intent);
               }
                else {
                    Intent intent = new Intent(context, serviceName.getActivityClass());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashList.size();
    }

}
