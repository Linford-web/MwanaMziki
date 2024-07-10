package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bookedEventsAdapter;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class bookedEvents extends AppCompatActivity {

    ImageView back;
    RecyclerView bookedRv;

    ArrayList<bookedEventsModel> booked;
    bookedEventsAdapter bookedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booked_events);

        back = findViewById(R.id.back_arrow);
        bookedRv = findViewById(R.id.bookedEventsRv);

        back.setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(bookedEvents.this, MainDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        booked = new ArrayList<>();
        bookedAdapter = new bookedEventsAdapter(booked);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookedRv.setLayoutManager(layoutManager);
        bookedRv.setAdapter(bookedAdapter);

        // fetch booked events
        fetchBookedEvents();

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
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    booked.clear();
                    HashSet<String> events = new HashSet<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        if (userId != null){
                            if (!events.contains(event.getEventId())) {
                                booked.add(event);
                                events.add(event.getEventId());
                            }
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
                        booked.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(bookedEvents.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}