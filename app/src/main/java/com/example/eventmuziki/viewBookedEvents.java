package com.example.eventmuziki;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class viewBookedEvents extends AppCompatActivity {
    ImageView profileTv, back;
    TextView eventName, eventLocation, eventDate, eventTime, organizerName;

    ArrayList<bookedEventsModel> bookedEvents;
    musicianNameAdapter bookedAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_booked_events);

        profileTv = findViewById(R.id.poster);
        back = findViewById(R.id.back_arrow);
        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        organizerName = findViewById(R.id.organizerNameTv);
        recyclerView = findViewById(R.id.bookedMusiciansRv);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookedEventsModel bookedEvent = getIntent().getParcelableExtra("bookedEventsModel");
        if (bookedEvent !=null){
            eventName.setText(bookedEvent.getEventName());
            eventLocation.setText(bookedEvent.getLocation());
            eventDate.setText(bookedEvent.getDate());
            eventTime.setText(bookedEvent.getTime());
            organizerName.setText(bookedEvent.getOrganizerName());
        }

        bookedEvents = new ArrayList<>();
        bookedAdapter = new musicianNameAdapter(bookedEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookedAdapter);

        // fetch booked Users
        fetchBookedUsers();


    }

    private void fetchBookedUsers() {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
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
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        bookedEvents.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

        fStore.collection("BidEvents")
                .whereEqualTo("biddersId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        if (!bookedEvents.contains(event)) {
                            bookedEvents.add(event);
                        }
                    }
                    bookedAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(viewBookedEvents.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}