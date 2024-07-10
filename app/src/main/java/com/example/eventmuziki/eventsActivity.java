package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bidEventsAdapter;
import com.example.eventmuziki.Adapters.bookedEventsAdapter;
import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Adapters.eventSearchAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.eventModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class eventsActivity extends AppCompatActivity {

    ImageButton backArrow;
    FloatingActionButton addTask;
    RecyclerView recyclerView, eventRecyclerView, doneRecyclerView;
    ArrayList<eventModel> events, events2, events3;
    ArrayList<bookedEventsModel> booked;
    ArrayList<biddersEventModel> bidder;
    bidEventsAdapter bidAdapter;
    eventAdapter eventadapter;
    eventAdapter2 eventAdapters;
    bookedEventsAdapter bookedAdapter;
    FirebaseFirestore fStore;

    ImageButton searchEventsBtn, cancelBtn, searchBtn, allBids, allBooked;
    TextView title;
    LinearLayout searchTv, menu, searchLayout;
    ScrollView scrollView;
    EditText searchTxt;
    RecyclerView searchEventsRv, bidRecyclerView;
    eventSearchAdapter eventSearch;

    LinearLayout viewAll, bidEvents, viewBooked, allEvents, bookedEvents, viewBid;
    TextView allEventsTxt, allBidsTxt, allBookedTxt;
    View allEventsView, bookedEventsView, bidEventsView;

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
        bidRecyclerView = findViewById(R.id.bidRecyclerView);
        fStore = FirebaseFirestore.getInstance();

        searchEventsBtn = findViewById(R.id.searchEventsBtn);
        title = findViewById(R.id.titleTxt);
        searchTv = findViewById(R.id.search_layout);
        menu = findViewById(R.id.menu_layout);
        scrollView = findViewById(R.id.scrollView);
        searchLayout = findViewById(R.id.search_events_layout);
        cancelBtn = findViewById(R.id.cancel_search);
        searchTxt = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.search_icon);
        searchEventsRv = findViewById(R.id.searchEventsRv);

        viewAll = findViewById(R.id.allLayoutPg);
        viewBid = findViewById(R.id.bidEventsPg);
        viewBooked = findViewById(R.id.bookedEventsPg);

        allBids = findViewById(R.id.bidEventsBtn);
        allBooked = findViewById(R.id.bookedEventsBtn);

        bidEvents = findViewById(R.id.bidEventsLayout);
        bookedEvents = findViewById(R.id.bookedEventsLayout);
        allEvents = findViewById(R.id.popularEventsLayout);

        allEventsTxt = findViewById(R.id.allEventsTxt);
        allBidsTxt = findViewById(R.id.bidEventTxt);
        allBookedTxt = findViewById(R.id.bookEventTxt);

        allEventsView = findViewById(R.id.allEventsView);
        bookedEventsView = findViewById(R.id.bookedEventsView);
        bidEventsView = findViewById(R.id.bidEventsView);

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidEvents.setVisibility(View.GONE);
                bookedEvents.setVisibility(View.GONE);
                allEvents.setVisibility(View.VISIBLE);
                addTask.setVisibility(View.VISIBLE);

                allEventsTxt.setTextColor(getResources().getColor(R.color.orange));
                allBidsTxt.setTextColor(getResources().getColor(R.color.black));
                allBookedTxt.setTextColor(getResources().getColor(R.color.black));
                allEventsView.setBackgroundColor(getResources().getColor(R.color.orange));
                bookedEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                bidEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                // Fetch events from Firestore
                fetchEvents();

            }
        });
        viewBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidEvents.setVisibility(View.VISIBLE);
                bookedEvents.setVisibility(View.GONE);
                allEvents.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);

                allEventsTxt.setTextColor(getResources().getColor(R.color.black));
                allBidsTxt.setTextColor(getResources().getColor(R.color.orange));
                allBookedTxt.setTextColor(getResources().getColor(R.color.black));
                bidEventsView.setBackgroundColor(getResources().getColor(R.color.orange));
                bookedEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                allEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                // Fetch booked events from Firestore
                fetchBidEvents();

            }
        });
        viewBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidEvents.setVisibility(View.GONE);
                bookedEvents.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);

                allEventsTxt.setTextColor(getResources().getColor(R.color.black));
                allBidsTxt.setTextColor(getResources().getColor(R.color.black));
                allBookedTxt.setTextColor(getResources().getColor(R.color.orange));
                bookedEventsView.setBackgroundColor(getResources().getColor(R.color.orange));
                allEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                bidEventsView.setBackgroundColor(getResources().getColor(R.color.gray));
                // fetch booked events from Firestore
                fetchBookedEvents();
            }
        });

        events = new ArrayList<>();
        eventadapter = new eventAdapter(events);
        events2 = new ArrayList<>();
        eventAdapters = new eventAdapter2(events2);
        booked = new ArrayList<>();
        bookedAdapter = new bookedEventsAdapter(booked);
        bidder = new ArrayList<>();
        bidAdapter = new bidEventsAdapter(bidder);

        // Set up all Events RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventadapter);
        // Set up event RecyclerView
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventRecyclerView.setLayoutManager(layoutManager2);
        eventRecyclerView.setAdapter(eventAdapters);
        // Set up done RecyclerView
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        doneRecyclerView.setLayoutManager(layoutManager3);
        doneRecyclerView.setAdapter(bookedAdapter);
        // Set up bid RecyclerView
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bidRecyclerView.setLayoutManager(layoutManager4);
        bidRecyclerView.setAdapter(bidAdapter);

        // Check user access level
        checkUserAccessLevel();

        backArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        allBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), bidEvents.class));
            }
        });

        allBooked.setOnClickListener(new View.OnClickListener() {
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

        searchEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title.setText("Search Events");
                menu.setVisibility(View.GONE);
                searchTv.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                addTask.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.GONE);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("Events");
                menu.setVisibility(View.VISIBLE);
                searchTv.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                addTask.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchTxt.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length()<3) {
                    searchTxt.setError("Enter at least 3 characters");
                    return;
                }
                setUpRecyclerView(searchTerm);
            }
        });

    }

    private void setUpRecyclerView(String searchTerm) {
        Query query = FirebaseFirestore.getInstance().collection("Events")
                .whereGreaterThanOrEqualTo("eventName", searchTerm);
        FirestoreRecyclerOptions<eventModel> option = new FirestoreRecyclerOptions.Builder<eventModel>()
                .setQuery(query, eventModel.class).build();

        eventSearch = new eventSearchAdapter(option, getApplicationContext());
        searchEventsRv.setLayoutManager(new LinearLayoutManager(this));
        searchEventsRv.setAdapter(eventSearch);
        eventSearch.startListening();

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
                            Toast.makeText(eventsActivity.this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    private void fetchEventsS(String userId) {
        fStore.collection("Events")
                .whereEqualTo("creatorID", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            events2.add(event);
                        }
                        eventAdapters.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventsActivity.this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
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
                }).addOnFailureListener(e -> Toast.makeText(eventsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void fetchBidEvents() {

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
                        biddersEventModel event = documentSnapshot.toObject(biddersEventModel.class);
                        bidder.add(event);
                    }
                    bidAdapter.notifyDataSetChanged();
                });

        fStore.collection("BidEvents")
                .whereEqualTo("biddersId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        biddersEventModel event = documentSnapshot.toObject(biddersEventModel.class);
                        bidder.add(event);
                    }
                    bidAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(eventsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
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
                               fetchEventsS(userId);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                addTask.setVisibility(View.GONE);
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

}
