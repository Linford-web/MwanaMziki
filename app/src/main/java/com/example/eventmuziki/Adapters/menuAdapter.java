package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.serviceNameModel;
import com.example.eventmuziki.Models.walletModels.walletModel;
import com.example.eventmuziki.R;

import java.util.ArrayList;

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder> {

    ArrayList<walletModel.walletMenu> menuList;
    Context context;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String itemName);
    }

    public menuAdapter(Context context, ArrayList<walletModel.walletMenu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_wallet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        walletModel.walletMenu item = menuList.get(position);
        holder.icon.setImageResource(item.getServiceIcon());
        holder.name.setText(item.getServiceName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(item.getServiceName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.menu_icon);
            name = itemView.findViewById(R.id.menu_name);
        }
    }
}
