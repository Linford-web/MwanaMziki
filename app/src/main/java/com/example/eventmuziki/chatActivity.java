package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.chatAdapter;
import com.example.eventmuziki.Models.chatRoomModel;
import com.example.eventmuziki.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.Query;

public class chatActivity extends AppCompatActivity {

    ImageButton searchBtn;
    ImageView back;
    RecyclerView recyclerView;
    chatAdapter adapter;

    String userId, name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatRecyclerView);
        searchBtn = findViewById(R.id.searchBtn);
        back = findViewById(R.id.back_arrow);
        FirebaseApp.initializeApp(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chatActivity.this, searchUsersActivity.class));
            }
        });

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        Query query = FirebaseUtil.allChatRoomCollections()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<chatRoomModel> options = new FirestoreRecyclerOptions.Builder<chatRoomModel>()
                .setQuery(query, chatRoomModel.class).build();

        adapter = new chatAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}