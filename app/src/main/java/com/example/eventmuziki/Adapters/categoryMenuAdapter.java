package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.menuModel;
import com.example.eventmuziki.Models.serviceNameModel;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class categoryMenuAdapter extends RecyclerView.Adapter<categoryMenuAdapter.ViewHolder> {

    Context context;
    ArrayList<menuModel> menuList;
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

    public categoryMenuAdapter(Context context, ArrayList<menuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_menu, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int viewType) {
        menuModel menuItem = menuList.get(viewType);
        holder.serviceName.setText(menuItem.getTitle());
        holder.serviceIcon.setImageResource(menuItem.getImageId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here
                if (listener != null) {
                    listener.onItemClick(menuItem.getTitle());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}