package com.example.eventmuziki;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class viewBookedEvents extends AppCompatActivity {
    ImageView profileTv, back;
    TextView eventName, eventLocation, eventDate, eventTime, organizerName;
    Button advertise;

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
        advertise = findViewById(R.id.advertiseBtn);

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

            String eventId = bookedEvent.getEventId();
            // fetch booked Users
            fetchBookedUsers(eventId);

            FirebaseFirestore.getInstance()
                    .collection("Events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                String eventPoster = documentSnapshot.getString("eventPoster");

                                if (eventPoster != null && !eventPoster.isEmpty()) {
                                    Glide.with(viewBookedEvents.this)
                                            .load(eventPoster)
                                            .placeholder(R.drawable.cover)
                                            .error(R.drawable.poster1)
                                            .into(profileTv);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(viewBookedEvents.this, "Failed To get Event Poster", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        bookedEvents = new ArrayList<>();
        bookedAdapter = new musicianNameAdapter(bookedEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookedAdapter);

        // check user access level
        checkUserAccessLevel();

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
                                advertise.setVisibility(View.VISIBLE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                advertise.setVisibility(View.GONE);
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

    private void fetchBookedUsers(String eventId) {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        bookedEvents.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

    }
}