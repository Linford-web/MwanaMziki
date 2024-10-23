package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.UserModel;
import com.example.eventmuziki.Models.UserModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchAdapter extends FirestoreRecyclerAdapter<UserModel, searchAdapter.ViewHolder> {

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name, phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileTv);
            name = itemView.findViewById(R.id.user_name);
            phone = itemView.findViewById(R.id.last_message);

        }
    }
    public searchAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull UserModel model) {

        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());

        String imageUrl = model.getProfilePicture();
        if (imageUrl !=null && !imageUrl.isEmpty()){
            Glide.with(holder.profile.getContext())
                    .load(model.getProfilePicture())
                    .placeholder(R.drawable.profile_icon)
                    .error(R.drawable.profile_icon)
                    .into(holder.profile);
        }else {
            holder.profile.setImageResource(R.drawable.profile_icon);
        }

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = model.getUserid();
        if (userId !=null && userId.equalsIgnoreCase(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())){
            holder.name.setText(String.format("%s (Me)", model.getName()));
            holder.name.setTextColor(context.getResources().getColor(R.color.dark_blue));
        }
        else {
            Log.d("TAG", "onBindViewHolder: "+model.getUserid());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatRoom.class);
                intent.putExtra("userId", model.getUserid());
                intent.putExtra("userName", model.getName());
                intent.putExtra("userImage", model.getProfilePicture());
                intent.putExtra("userPhone", model.getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_view, parent, false);
        return new ViewHolder(view);
    }

}
