package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.chatRoomModel;
import com.example.eventmuziki.Models.UserModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatAdapter extends FirestoreRecyclerAdapter<chatRoomModel, chatAdapter.ViewHolder> {

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name, lastMessage, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileTv);
            name = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            time = itemView.findViewById(R.id.lastTimeTxt);

        }
    }
    public chatAdapter(@NonNull FirestoreRecyclerOptions<chatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull chatRoomModel model) {

        FirebaseUtil.getOtherUserFromChats(model.getUserIds())
                .get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        UserModel otherUser = task.getResult().toObject(UserModel.class);
                        holder.name.setText(otherUser.getName());
                        if (lastMessageSentByMe)
                            holder.lastMessage.setText("You: " + model.getLastMessage());
                        else
                            holder.lastMessage.setText(model.getLastMessage());
                        holder.time.setText(FirebaseUtil.timeStampToString(model.getLastMessageTime()));

                        Glide.with(holder.profile.getContext())
                                .load(otherUser.getProfilePicture())
                                .placeholder(R.drawable.profile_icon)
                                .error(R.drawable.profile_icon)
                                .into(holder.profile);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, chatRoom.class);
                                intent.putExtra("userId", otherUser.getUserID());
                                intent.putExtra("userName", otherUser.getName());
                                intent.putExtra("userEmail", otherUser.getEmail());
                                intent.putExtra("userImage", otherUser.getProfilePicture());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }
                        });

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
