package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.messageModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class messageAdapter extends FirestoreRecyclerAdapter<messageModel, messageAdapter.ViewHolder> {

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChat, rightChat;
        TextView leftMessage, rightMessage, leftTime, rightTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.leftChatMessage);
            rightChat = itemView.findViewById(R.id.rightChatMessage);
            leftMessage = itemView.findViewById(R.id.leftChatText);
            rightMessage = itemView.findViewById(R.id.rightChatText);
            leftTime = itemView.findViewById(R.id.leftChatTime);
            rightTime = itemView.findViewById(R.id.rightChatTime);

        }
    }
    public messageAdapter(@NonNull FirestoreRecyclerOptions<messageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull messageModel model) {

        if (model.getSenderId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightMessage.setText(model.getMessage());
            holder.rightTime.setText(FirebaseUtil.timeStampToString(model.getTimestamp()));
        }else {
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftMessage.setText(model.getMessage());
            holder.leftTime.setText(FirebaseUtil.timeStampToString(model.getTimestamp()));
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send_or_receive_row, parent, false);
        return new ViewHolder(view);
    }



}
