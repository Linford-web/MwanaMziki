package com.example.eventmuziki;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.example.eventmuziki.Adapters.chatAdapter;
import com.example.eventmuziki.Adapters.messageAdapter;
import com.example.eventmuziki.Models.messageModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class chatRoom extends AppCompatActivity {

    DatabaseReference dbReferenceSender, dbReferenceReceiver, databaseReference;
    messageAdapter messageAdapters;
    RecyclerView messageRecyclerView;
    String senderId, receiverId, senderName, receiverName, senderRoom, receiverRoom;
    ImageView back, sendBtn;
    EditText messageText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_room);

        back = findViewById(R.id.back_arrow);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String toolbarTitle = "Chat Room";
        getSupportActionBar().setTitle(toolbarTitle);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        receiverId = getIntent().getStringExtra("userId");
        receiverName = getIntent().getStringExtra("userName");

        getSupportActionBar().setTitle(receiverName);
        if (receiverId != null) {
            senderRoom = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + receiverId;
            receiverRoom = receiverId + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        }
        sendBtn = findViewById(R.id.sendMessageBtn);
        messageAdapters = new messageAdapter(this, new ArrayList<>());

        messageRecyclerView = findViewById(R.id.chatRecyclerView);
        messageText = findViewById(R.id.messageEditText);

        messageRecyclerView.setAdapter(messageAdapters);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        dbReferenceSender = FirebaseDatabase.getInstance().getReference("Chats").child(senderId).child(senderRoom);
        dbReferenceReceiver = FirebaseDatabase.getInstance().getReference("Chats").child(receiverId).child(receiverRoom);

        dbReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<messageModel> messages = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messageModel model = dataSnapshot.getValue(messageModel.class);
                    messages.add(model);
                }
                messageAdapters.clear();
                for (messageModel model : messages){
                    messageAdapters.add(model);
                }
                messageAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString();
                if (message.trim().length()>0){
                    SendMessage(message);
                }else {
                    Toast.makeText(chatRoom.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void SendMessage(String message) {

        String messageId = UUID.randomUUID().toString();
        messageModel model = new messageModel(message, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), "", messageId, System.currentTimeMillis());
        messageAdapters.add(model);

        dbReferenceSender.child(messageId).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(chatRoom.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });

        dbReferenceReceiver.child(messageId).setValue(model);
        messageRecyclerView.scrollToPosition(messageAdapters.getItemCount() - 1);
        messageText.setText("");


    }


}