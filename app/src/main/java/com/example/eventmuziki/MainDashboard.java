package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Models.chatUserModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MainDashboard extends AppCompatActivity {

    ImageView chats, eventsBtn, adverts, clothes, home;
    ImageView profile, allAdverts, upcomingEvents;
    TextView userNameTv, emailTv;
    RecyclerView recyclerView;
    ArrayList<eventModel> events;
    eventAdapter2 eventadapter;
    FirebaseFirestore fStore;

    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dashboard);

        chats = findViewById(R.id.messageIcon);
        eventsBtn = findViewById(R.id.eventIcon);
        adverts = findViewById(R.id.advertIcon);
        clothes = findViewById(R.id.costumeIcon);
        home = findViewById(R.id.homeIcon);
        profile = findViewById(R.id.userProfileTv);
        userNameTv = findViewById(R.id.get_name);
        recyclerView = findViewById(R.id.eventsRecyclerView);
        allAdverts = findViewById(R.id.allAdvertsBtn);
        upcomingEvents = findViewById(R.id.allEventsBtn);
        emailTv = findViewById(R.id.emailTv);
        fStore = FirebaseFirestore.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");

        events = new ArrayList<>();
        eventadapter = new eventAdapter2(events);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventadapter);

        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null){
            // Fetch user details from FireStore "Users" collection
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(profile);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Toast.makeText(this, "Click Profile photo to upload Photo", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "No profile photo found");
                                }
                            } else {
                                Log.d("TAG", "No such document");
                                //Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // get user name and display it
        fetchUserName();
        // check user access level
        checkUserAccessLevel();

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboard.this, profileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String chatRoomId = UUID.randomUUID().toString();
                String userId = FirebaseAuth.getInstance().getUid();
                String email = emailTv.getText().toString();
                String name = userNameTv.getText().toString();

                chatUserModel chatRoom = new chatUserModel("NotSet", chatRoomId, userId, name, email);
                dbReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(chatRoom);

                Intent intent = new Intent(MainDashboard.this, chatActivity.class);
                intent.putExtra("roomId", chatRoomId);
                intent.putExtra("userId", userId);
                intent.putExtra("email", email);
                intent.putExtra("userName", name);
                startActivity(intent);

            }
        });

    }

    private void checkUserAccessLevel() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        //get user type
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if ("Corporate".equals(userType)) {
                                // Fetch events from Firestore
                                fetchEventSs(userId);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                // Fetch events from Firestore
                                fetchEvents();
                            } else {
                                // Handle other user types if needed
                                Log.d("TAG", "User is neither Corporate nor Musician");
                            }

                        }
                    } else {
                        Log.e("TaskListAdapter", "Error getting document", task.getException());
                    }
                });
    }

    private void fetchEventSs(String userId) {
        fStore.collection("Events")
                .whereEqualTo("creatorID", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            events.add(event);
                        }
                        eventadapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainDashboard.this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchEvents() {
        fStore.collection("Events")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            events.add(event);
                        }
                        eventadapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void fetchUserName() {
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String userName = document.getString("name");
                                    // Set the user name in the TextView
                                    userNameTv.setText(userName);
                                    emailTv.setText(document.getString("email"));

                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                                Toast.makeText(MainDashboard.this, "Error fetching user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}