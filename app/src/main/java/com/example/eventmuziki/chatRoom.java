package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.messageAdapter;
import com.example.eventmuziki.Models.chatRoomModel;
import com.example.eventmuziki.Models.UserModel;
import com.example.eventmuziki.Models.messageModel;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatRoom extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    ImageButton sendBtn;
    ImageButton back;
    TextView otherName, phone;
    CircleImageView profile;
    EditText messageText;

    String chatRoomId, currentId;
    chatRoomModel chatModel;

    messageAdapter adapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_room);

        sendBtn = findViewById(R.id.sendMessageBtn);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageText = findViewById(R.id.messageEditText);
        back = findViewById(R.id.back_arrow);
        otherName = findViewById(R.id.user_name);
        profile = findViewById(R.id.profileTv);
        phone = findViewById(R.id.user_phone);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(chatRoom.this, chatActivity.class);
            startActivity(intent);
            finish();
        });

        currentId = fAuth.getCurrentUser().getUid();

        FirebaseApp.initializeApp(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String id = intent.getStringExtra("userId");
        String name = intent.getStringExtra("userName");
        String image = intent.getStringExtra("userImage");
        String phones = intent.getStringExtra("userPhone");

        if (id != null) {
            chatRoomId = createChatRoomId(currentId, id);
        }else {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
        }

        // Retrieve the data passed from the adapter
        otherName.setText(name);
        phone.setText(phones);
        if (image != null && !image.isEmpty()){
            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.profile_image)
                    .error(R.drawable.profile_image)
                    .into(profile);
        }
        else {
            profile.setImageResource(R.drawable.profile_image);
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(chatRoom.this, chatRoomId, Toast.LENGTH_SHORT).show();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString().trim();
                if (message.isEmpty())
                    return;
                sendMessageToUser(message, id);

            }
        });

        //getOrCreateChatRoom(chatRoomId, currentId, id);

        setMessageRecyclerView();
    }

    private String createChatRoomId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    private void setMessageRecyclerView() {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId).collection("Messages");
        Query query = ref.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<messageModel> options = new FirestoreRecyclerOptions.Builder<messageModel>()
                .setQuery(query, messageModel.class).build();

        adapter = new messageAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(manager);
        chatRecyclerView.setAdapter(adapter);
        adapter.startListening();
        // Scroll to the bottom of the chat when a new message is added
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void sendMessageToUser(String message, String id) {

        chatRoomModel chatModel = new chatRoomModel();

        chatModel.setChatRoomId(chatRoomId);
        chatModel.setUserIds(Arrays.asList(currentId, id));
        chatModel.setLastMessageTime(Timestamp.now());
        chatModel.setLastMessageSenderId(currentId);
        chatModel.setLastMessage(message);

        fStore.collection("ChatRooms").document(chatRoomId).set(chatModel);

        messageModel messageModel = new messageModel(message, currentId, Timestamp.now());
        fStore.collection("ChatRooms").document(chatRoomId)
                .collection("Messages").add(messageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageText.setText("");
                            Toast.makeText(chatRoom.this, "Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(chatRoom.this, "Message not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(chatRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show());


    }

    private void getOrCreateChatRoom(String chatRoomId, String currentId, String id) {

    FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId)
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        chatModel = documentSnapshot.toObject(chatRoomModel.class);
                        if (chatModel == null) {
                            chatModel = new chatRoomModel(chatRoomId, Arrays.asList(currentId, id), Timestamp.now(), currentId, messageText.getText().toString().trim());
                            fStore.collection("ChatRooms").document(chatRoomId).set(chatModel);
                        }
                    }
                    else {
                        Log.d("ChatRoom", "Chat room not found");
                    }
                }else {
                    Toast.makeText(chatRoom.this, "Chat room not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

