package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceNameModel;
import com.example.eventmuziki.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class serviceNameAdapter extends  RecyclerView.Adapter<serviceNameAdapter.ViewHolder>{

    Context context;
    private ArrayList<serviceNameModel> serviceNameList;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(serviceNameModel service);
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

    public serviceNameAdapter( Context context, ArrayList<serviceNameModel> serviceList) {
        this.context = context;
        this.serviceNameList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dash, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int viewType) {
        serviceNameModel serviceName = serviceNameList.get(viewType);
        holder.serviceName.setText(serviceName.getServiceName());
        holder.serviceIcon.setImageResource(serviceName.getServiceIcon());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(serviceName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceNameList.size();
    }


}
