package com.example.eventmuziki;

import android.os.Bundle;
import android.os.Message;
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
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.Models.messageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class chatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    chatAdapter chatsAdapters;
    ArrayList<chatUserModel> messageList;
    DatabaseReference databaseReference;
    String userId, chatRoomId, name, email, eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // Retrieve the data passed from the adapter
        userId = getIntent().getStringExtra("userId");
        chatRoomId = getIntent().getStringExtra("roomId");
        name = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        eventID = getIntent().getStringExtra("eventId");


        if (userId == null || chatRoomId == null || name == null || email == null || eventID == null) {
            Toast.makeText(this, "Missing chat data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Initialize the chat room here
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String toolbarTitle = "Chat Rooms";
        getSupportActionBar().setTitle(toolbarTitle);

        chatsAdapters = new chatAdapter(this, new ArrayList<>());
        chatRecyclerView.setAdapter(chatsAdapters);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("eventID").equalTo(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing chat messages
                chatsAdapters.clear();
                String currentUserId = FirebaseAuth.getInstance().getUid();
                if (currentUserId == null) {
                    Toast.makeText(chatActivity.this, "User Not Logged In" + currentUserId, Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    chatUserModel chatUser = dataSnapshot.getValue(chatUserModel.class);
                    if (chatUser != null && chatUser.getUserId() != null && !chatUser.getUserId().equals(currentUserId)) {
                        if (eventID.equals(chatUser.getEventID())) {  // Filter by eventID
                            chatsAdapters.add(chatUser);
                        }
                    }
                }
                chatsAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}