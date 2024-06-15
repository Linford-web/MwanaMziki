package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bookedEventsAdapter;
import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class EventsActivity extends AppCompatActivity {

    ImageView backArrow;
    FloatingActionButton addTask;
    RecyclerView recyclerView, eventRecyclerView, doneRecyclerView;
    ArrayList<eventModel> events, events2;
    ArrayList<bookedEventsModel> booked;
    eventAdapter eventadapter;
    eventAdapter2 eventAdapters;
    bookedEventsAdapter bookedAdapter;
    FirebaseFirestore fStore;
    CardView bidEvents, viewBooked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);

        backArrow = findViewById(R.id.back_arrow);
        addTask = findViewById(R.id.add_taskFBtn);
        recyclerView = findViewById(R.id.allRecyclerView);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        doneRecyclerView = findViewById(R.id.doneRecyclerView);
        bidEvents = findViewById(R.id.bidEvents);
        viewBooked = findViewById(R.id.bookedEvents);
        fStore = FirebaseFirestore.getInstance();

        events = new ArrayList<>();
        eventadapter = new eventAdapter(events);
        events2 = new ArrayList<>();
        eventAdapters = new eventAdapter2(events2);
        booked = new ArrayList<>();
        bookedAdapter = new bookedEventsAdapter(booked);

        // Set up RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventadapter);

        // Set up RecyclerView
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventRecyclerView.setLayoutManager(layoutManager2);
        eventRecyclerView.setAdapter(eventAdapters);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        doneRecyclerView.setLayoutManager(layoutManager3);
        doneRecyclerView.setAdapter(bookedAdapter);

        // Fetch events from Firestore
        fetchEvents();
        // Check user access level
        checkUserAccessLevel();
        // fetch booked events from Firestore
        fetchBookedEvents();

        backArrow.setOnClickListener(v -> {
            finish();
        });

        bidEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), bidEvents.class));
            }
        });

        viewBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), bookedEvents.class));
            }
        });

        // Add Task Button
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addEvents.class));
            }
        });

    }

    private void fetchBookedEvents() {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        fStore.collection("BookedEvents")
                .whereEqualTo("creatorID", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        if (!booked.contains(event)) {
                            booked.add(event);
                        }
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

        fStore.collection("BookedEvents")
                .whereEqualTo("biddersId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        if (!booked.contains(event)) {
                            booked.add(event);
                        }
                    }
                    bookedAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(EventsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

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
                                events2.add(event);

                            }
                            eventadapter.notifyDataSetChanged();
                            eventAdapters.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventsActivity.this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
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
                               addTask.setVisibility(View.VISIBLE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                addTask.setVisibility(View.GONE);
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

}
