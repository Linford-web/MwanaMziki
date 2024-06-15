package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder> {

    Context context;
    ArrayList<chatUserModel> messages;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, event;
        ImageView profile;
        CardView chatRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            event = itemView.findViewById(R.id.user_email);
            profile = itemView.findViewById(R.id.profileTv);
            chatRoom = itemView.findViewById(R.id.container);
        }
    }


    public chatAdapter(Context context, ArrayList<chatUserModel> messages) {
        this.context = context;
        this.messages = new ArrayList<>();
    }

    public ArrayList<chatUserModel> getMessages() {
        return messages;
    }

    public void  add(chatUserModel message) {
        messages.add(message);

    }
    public  void clear() {
        messages.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public chatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatAdapter.ViewHolder holder, int position) {

        chatUserModel message = messages.get(position);

        holder.name.setText(message.getUserName());
        holder.event.setText(message.getEmail());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handle click event here
                Intent intent = new Intent(context, chatRoom.class);
                intent.putExtra("userId", message.getUserId());
                intent.putExtra("name", message.getUserName());
                intent.putExtra("email", message.getEmail());
                intent.putExtra("roomId", message.getChatRoomId());
                intent.putExtra("eventId", message.getEventID());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
