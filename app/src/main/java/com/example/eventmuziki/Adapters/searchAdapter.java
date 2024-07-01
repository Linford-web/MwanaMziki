package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchAdapter extends FirestoreRecyclerAdapter<chatUserModel, searchAdapter.ViewHolder> {

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileTv);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);

        }
    }
    public searchAdapter(@NonNull FirestoreRecyclerOptions<chatUserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull chatUserModel model) {

        holder.name.setText(model.getName());
        holder.email.setText(model.getUserID());

        Glide.with(holder.profile.getContext())
                .load(model.getProfilePicture())
                .placeholder(R.drawable.cover)
                .error(R.drawable.poster1)
                .into(holder.profile);

/*
        if (model.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.name.setText(model.getName() + "(Me)");
            holder.name.setTextColor(context.getResources().getColor(R.color.dark_blue));
        }
        else {
            Toast.makeText(context, "Not Me", Toast.LENGTH_SHORT).show();
        }

 */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatRoom.class);
                intent.putExtra("userId", model.getUserID());
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
