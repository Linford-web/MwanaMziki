package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.eventmuziki.Adapters.categoryAdapter;
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

    FloatingActionButton addTask;
    ArrayList<eventModel> events, events2, events3;
    ArrayList<bookedEventsModel> booked;
    ArrayList<biddersEventModel> bidder;
    bidEventsAdapter bidAdapter;
    eventAdapter adapter;
    eventAdapter2 eventAdapters;
    bookedEventsAdapter bookedAdapter;
    categoryAdapter categoryAdpt;
    FirebaseFirestore fStore;

    ImageButton backArrow,searchEventsBtn, cancelBtn, searchBtn, allBids, allBooked, cancelCategory;
    TextView title, categoryTxt, allEventsTxt, allBidsTxt, allBookedTxt;
    LinearLayout searchTv, menu, searchLayout, viewAll, bidEvents, viewBooked, allEvents, bookedEvents, viewBid, categoryLayout;
    ScrollView scrollView;
    EditText searchTxt;
    RecyclerView searchEventsRv, bidRecyclerView, recyclerView, eventRecyclerView, doneRecyclerView, categoryRecyclerView;
    eventSearchAdapter eventSearch;
    View allEventsView, bookedEventsView, bidEventsView;

    LinearLayout sportsCategory, churchCategory, weddingCategory, musicCategory, graduationCategory, photographyCategory, otherCategory, categoryLayoutOptions;
    TextView sportsTxt, churchTxt, weddingTxt, musicTxt, graduationTxt, photographyTxt, otherTxt;
    ImageView sportsIcon, churchIcon, weddingIcon, musicIcon, graduationIcon, photographyIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);

        fStore = FirebaseFirestore.getInstance();
        backArrow = findViewById(R.id.back_arrow);
        addTask = findViewById(R.id.add_taskFBtn);
        recyclerView = findViewById(R.id.allRecyclerView);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        doneRecyclerView = findViewById(R.id.doneRecyclerView);
        bidRecyclerView = findViewById(R.id.bidRecyclerView);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

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
        categoryTxt = findViewById(R.id.categoryTxt);
        categoryLayout = findViewById(R.id.categoryLayout);
        cancelCategory = findViewById(R.id.cancelCategory);
        categoryLayoutOptions = findViewById(R.id.categoryLayoutOptions);

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

        sportsCategory = findViewById(R.id.sportsCategory);
        churchCategory = findViewById(R.id.churchCategory);
        weddingCategory = findViewById(R.id.weddingCategory);
        musicCategory = findViewById(R.id.musicCategory);
        graduationCategory = findViewById(R.id.graduationCategory);
        photographyCategory = findViewById(R.id.photographyCategory);
        otherCategory = findViewById(R.id.otherCategory);

        sportsTxt = findViewById(R.id.sportsTxt);
        churchTxt = findViewById(R.id.churchTxt);
        weddingTxt = findViewById(R.id.weddingTxt);
        musicTxt = findViewById(R.id.musicTxt);
        graduationTxt = findViewById(R.id.graduationTxt);
        photographyTxt = findViewById(R.id.photographyTxt);
        otherTxt = findViewById(R.id.otherTxt);

        sportsIcon = findViewById(R.id.sportsIcon);
        churchIcon = findViewById(R.id.churchIcon);
        weddingIcon = findViewById(R.id.weddingIcon);
        musicIcon = findViewById(R.id.musicIcon);
        graduationIcon = findViewById(R.id.graduationIcon);
        photographyIcon = findViewById(R.id.photographyIcon);


        sportsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Sports");
                sportsTxt.setTextColor(getResources().getColor(R.color.orange));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.orange));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        churchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Church");
                churchTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.orange));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        weddingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Wedding");
                weddingTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.orange));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        musicCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Music");
                musicTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.orange));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        graduationCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Graduation");
                graduationTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.orange));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        photographyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Photography");
                photographyTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.orange));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        otherCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCategoryEvents("Other");
                otherTxt.setTextColor(getResources().getColor(R.color.orange));
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                cancelCategory.setVisibility(View.VISIBLE);
                allEvents.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        cancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLayout.setVisibility(View.GONE);
                cancelCategory.setVisibility(View.GONE);
                allEvents.setVisibility(View.VISIBLE);
                sportsTxt.setTextColor(getResources().getColor(R.color.black));
                churchTxt.setTextColor(getResources().getColor(R.color.black));
                weddingTxt.setTextColor(getResources().getColor(R.color.black));
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                graduationTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));
                sportsIcon.setColorFilter(getResources().getColor(R.color.black));
                churchIcon.setColorFilter(getResources().getColor(R.color.black));
                weddingIcon.setColorFilter(getResources().getColor(R.color.black));
                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                graduationIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                otherTxt.setTextColor(getResources().getColor(R.color.black));

            }
        });



        // Set up all Events RecyclerView
        events = new ArrayList<>();
        adapter = new eventAdapter(events);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Set up event RecyclerView
        events2 = new ArrayList<>();
        eventAdapters = new eventAdapter2(events2);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventRecyclerView.setLayoutManager(layoutManager2);
        eventRecyclerView.setAdapter(eventAdapters);

        // Set up done RecyclerView
        booked = new ArrayList<>();
        bookedAdapter = new bookedEventsAdapter(booked);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        doneRecyclerView.setLayoutManager(layoutManager3);
        doneRecyclerView.setAdapter(bookedAdapter);

        // Set up bid RecyclerView
        bidder = new ArrayList<>();
        bidAdapter = new bidEventsAdapter(bidder);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bidRecyclerView.setLayoutManager(layoutManager4);
        bidRecyclerView.setAdapter(bidAdapter);

        // Update RecyclerView with events from the selected category
        events3 = new ArrayList<>();
        categoryAdpt = new categoryAdapter(events3);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager5);
        categoryRecyclerView.setAdapter(categoryAdpt);

        // Check user access level
        checkUserAccessLevel();
        // Fetch events from Firestore
        fetchEvents();

        backArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidEvents.setVisibility(View.GONE);
                bookedEvents.setVisibility(View.GONE);
                allEvents.setVisibility(View.VISIBLE);
                categoryLayoutOptions.setVisibility(View.VISIBLE);

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
                categoryLayoutOptions.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.GONE);

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
                categoryLayoutOptions.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.GONE);

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

    private void fetchCategoryEvents(String category) {
        fStore.collection("Events")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    events3.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        eventModel event = documentSnapshot.toObject(eventModel.class);
                        events3.add(event);
                    }
                    categoryAdpt.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(eventsActivity.this, "Failed to fetch category events", Toast.LENGTH_SHORT).show());
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
                            adapter.notifyDataSetChanged();
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
    private void fetchSEvents(){
        fStore.collection("Events")
                .limit(6)
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

        fStore.collection("BidEvents")
                .whereEqualTo("creatorID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bidder.clear();
                    HashSet<String> bidEvents = new HashSet<>();

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        biddersEventModel event = documentSnapshot.toObject(biddersEventModel.class);
                        if (!bidEvents.contains(event.getEventId())) {
                            bidder.add(event);
                            bidEvents.add(event.getEventId());
                        }
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
                                fetchSEvents();
                                String category = document.getString("category");
                                categoryTxt.setText(category);
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
