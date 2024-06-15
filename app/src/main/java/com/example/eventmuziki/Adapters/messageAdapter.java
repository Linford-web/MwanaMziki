package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.messageModel;
import com.example.eventmuziki.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;


    Context context;
    ArrayList<messageModel> messages;

    public messageAdapter(Context context, ArrayList<messageModel> messages) {
        this.context = context;
        this.messages = new ArrayList<>();
    }

    public messageAdapter() {
    }

    public void add(messageModel message) {
        messages.add(message);
        notifyDataSetChanged();

    }
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSentMessage, textViewReceivedMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSentMessage = itemView.findViewById(R.id.textViewSendMessage);
            textViewReceivedMessage = itemView.findViewById(R.id.textViewReceivedMessage);

        }
    }

    @NonNull
    @Override
    public messageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_row_sent, parent, false);
            return new ViewHolder(view);
        }
        else  {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_row_received, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.ViewHolder holder, int position) {

        messageModel message = messages.get(position);
        if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.textViewSentMessage.setText(message.getMessage());
        }
        else {
            holder.textViewReceivedMessage.setText(message.getMessage());

        }

    }

    @Override
    public int getItemViewType(int position) {
        messageModel message = messages.get(position);
        if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_SENDER;
        }
        else {
            return VIEW_TYPE_RECEIVER;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public ArrayList<messageModel> getMessages() {
        return messages;
    }

}
