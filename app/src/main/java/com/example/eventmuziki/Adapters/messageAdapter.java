package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.messageModel;
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

public class messageAdapter extends FirestoreRecyclerAdapter<messageModel, messageAdapter.ViewHolder> {

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChat, rightChat;
        TextView leftMessage, rightMessage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.leftChatMessage);
            rightChat = itemView.findViewById(R.id.rightChatMessage);
            leftMessage = itemView.findViewById(R.id.leftChatText);
            rightMessage = itemView.findViewById(R.id.rightChatText);

        }
    }
    public messageAdapter(@NonNull FirestoreRecyclerOptions<messageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull messageModel model) {

        if (model.getSenderId().equals(FirebaseUtil.currentUserId())) {
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightMessage.setText(model.getMessage());
        }else {
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftMessage.setText(model.getMessage());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sendorreceive_row, parent, false);
        return new ViewHolder(view);
    }



}
