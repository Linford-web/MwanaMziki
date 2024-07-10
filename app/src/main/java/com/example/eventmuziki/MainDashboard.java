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
import com.example.eventmuziki.Adapters.advertAdapter;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Models.advertisementModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainDashboard extends AppCompatActivity {

    ImageView chats, eventsBtn, adverts, clothes, home;
    ImageView profile, allAdverts, upcomingEvents;
    TextView userNameTv;
    RecyclerView eventRv, advertsRv;
    ArrayList<eventModel> events;
    ArrayList<advertisementModel> advert;
    eventAdapter2 adapter;
    advertAdapter adapter2;
    FirebaseFirestore fStore;

    String name, email;

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
        eventRv = findViewById(R.id.eventsRecyclerView);
        allAdverts = findViewById(R.id.allAdvertsBtn);
        upcomingEvents = findViewById(R.id.allEventsBtn);
        fStore = FirebaseFirestore.getInstance();
        advertsRv = findViewById(R.id.advertRv);
        // bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Initialize Event RecyclerView
        events = new ArrayList<>();
        adapter = new eventAdapter2(events);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventRv.setLayoutManager(layoutManager);
        eventRv.setAdapter(adapter);

        // Initialize Advertisement RecyclerView
        advert = new ArrayList<>();
        adapter2 = new advertAdapter(advert);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        advertsRv.setLayoutManager(layoutManager2);
        advertsRv.setAdapter(adapter2);

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
                                name = document.getString("name");
                                email = document.getString("email");
                                // Set the user name in the TextView
                                userNameTv.setText(name);
                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(profile);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Log.d("TAG", "No profile photo found");
                                }
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // check user access level
        checkUserAccessLevel();

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), eventsActivity.class));

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
                Intent intent = new Intent(getApplicationContext(), chatActivity.class);
                startActivity(intent);

            }
        });
        adverts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), advertActivity.class);
                startActivity(intent);
            }
        });
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), costumeActivity.class);
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
                                fetchAdverts(userId);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                // Fetch events from Firestore
                                fetchEvents();
                                fetchAllAdverts();
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

    private void fetchAllAdverts() {
        fStore.collection("Advertisements")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        advertisementModel advertTv = documentSnapshot.toObject(advertisementModel.class);
                        advert.add(advertTv);
                    }
                    adapter2.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(MainDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchAdverts(String userId) {
        fStore.collection("Advertisements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        advertisementModel advertTv = documentSnapshot.toObject(advertisementModel.class);
                        advert.add(advertTv);
                    }
                    adapter2.notifyDataSetChanged();
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        adapter.notifyDataSetChanged();
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
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}