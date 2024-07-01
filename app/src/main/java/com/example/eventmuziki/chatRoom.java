package com.example.eventmuziki;

import static java.security.Timestamp.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.chatAdapter;
import com.example.eventmuziki.Adapters.messageAdapter;
import com.example.eventmuziki.Adapters.searchAdapter;
import com.example.eventmuziki.Models.chatRoomModel;
import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.Models.messageModel;
import com.example.eventmuziki.utils.AndroidUtil;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatRoom extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    ImageButton sendBtn;
    ImageButton back;
    TextView otherName;
    CircleImageView profile;
    EditText messageText;
    LinearLayout writeAndSend;

    String chatRoomId;
    chatRoomModel chatRoom;
    chatUserModel otherUser;
    messageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_room);

        FirebaseApp.initializeApp(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        otherUser = AndroidUtil.getUserModelAsIntent(getIntent());

        chatRoomId = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserId(), otherUser.getUserID());

        // chatRoomId = UUID.randomUUID().toString();
        if (otherUser == null) {
            Toast.makeText(this, "Missing user details", Toast.LENGTH_SHORT).show();
            return;
        }

        sendBtn = findViewById(R.id.sendMessageBtn);
        writeAndSend = findViewById(R.id.messageContainer);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageText = findViewById(R.id.messageEditText);

        back = findViewById(R.id.back_arrow);
        otherName = findViewById(R.id.user_name);
        profile = findViewById(R.id.profileTv);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatRoom.this, chatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve the data passed from the adapter
        if (otherUser == null) {
            Toast.makeText(this, "Missing user details", Toast.LENGTH_SHORT).show();
            return;
        }

        otherName.setText(otherUser.getName());
        // Load profile image using Glide loading library
        Glide.with(this)
                .load(otherUser.getProfilePicture())
                .placeholder(R.drawable.profile_icon)
                .error(R.drawable.profile_icon)
                .into(profile);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString().trim();
                if (message.isEmpty())
                    return;
                sendMessageToUser(message);

            }
        });

        getOrCreateChatRoom();

        setRecyclerView();
    }

    private void setRecyclerView() {
        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<messageModel> options = new FirestoreRecyclerOptions.Builder<messageModel>()
                .setQuery(query, messageModel.class).build();

        adapter = new messageAdapter(options, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void sendMessageToUser(String message) {

        chatRoom.setLastMessageTime(Timestamp.now());
        chatRoom.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoom.setLastMessage(message);
        FirebaseUtil.getChatRoomReference(chatRoomId).set(chatRoom);

        messageModel messageModel = new messageModel(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatRoomMessageReference(chatRoomId).add(messageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageText.setText("");
                        }
                    }
                });

    }

    private void getOrCreateChatRoom() {
        FirebaseUtil.getChatRoomReference(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    chatRoom = task.getResult().toObject(chatRoomModel.class);
                    if (chatRoom == null) {
                        chatRoom = new chatRoomModel(chatRoomId, Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserID()), Timestamp.now(), "","");
                        FirebaseUtil.getChatRoomReference(chatRoomId).set(chatRoom);
                    } else {
                        Toast.makeText(chatRoom.this, "Chat room already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}

