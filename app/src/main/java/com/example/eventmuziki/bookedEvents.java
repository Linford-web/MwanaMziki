package com.example.eventmuziki;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bookedEventAdapter;
import com.example.eventmuziki.Models.bookedEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class bookedEvents extends AppCompatActivity {

    ImageView backArrow;
    RecyclerView bookedEventRv;
    ArrayList<bookedEventModel> booked;
    bookedEventAdapter bookedAdapter;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booked_events);

        backArrow = findViewById(R.id.back_arrow);
        bookedEventRv = findViewById(R.id.bookedEventsRv);
        fStore = FirebaseFirestore.getInstance();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        booked = new ArrayList<>();
        bookedAdapter = new bookedEventAdapter(booked);

        // Set up RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookedEventRv.setLayoutManager(layoutManager);
        bookedEventRv.setAdapter(bookedAdapter);

        // Fetch booked events from Firestore
        fetchBookedEvents();

    }

    private void fetchBookedEvents() {

        String userId = FirebaseAuth.getInstance().getUid();

        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        fStore.collection("BidEvents")
                .whereEqualTo("creatorID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventModel event = documentSnapshot.toObject(bookedEventModel.class);
                        booked.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

        fStore.collection("BidEvents")
                .whereEqualTo("biddersId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventModel event = documentSnapshot.toObject(bookedEventModel.class);
                        if (!booked.contains(event)) {
                            booked.add(event);
                        }
                    }
                    bookedAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(bookedEvents.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}