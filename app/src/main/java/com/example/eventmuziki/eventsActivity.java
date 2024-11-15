package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bidEventsAdapter;
import com.example.eventmuziki.Adapters.bookedEventsAdapter;
import com.example.eventmuziki.Adapters.categoryAdapter;
import com.example.eventmuziki.Adapters.categoryMenuAdapter;
import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Adapters.eventSearchAdapter;
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.menuModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class eventsActivity extends AppCompatActivity {

    String userId;
    FloatingActionButton addTask;
    ArrayList<eventModel> events, events2, events3, events4;
    ArrayList<bookedEventsModel> booked, booked1;
    ArrayList<biddersEventModel> bidder;
    bidEventsAdapter bidAdapter;
    eventAdapter adapter, adapter1;
    eventAdapter2 eventAdapters;
    bookedEventsAdapter bookedAdapter, bookedAdapter1;

    categoryAdapter categoryAdpt;
    FirebaseFirestore fStore;

    ArrayList<menuModel> menuList;
    categoryMenuAdapter menuAdapter;

    ImageView addEvents;
    ImageButton backArrow,searchEventsBtn, cancelBtn, searchBtn, cancelCategory;
    TextView categoryTxt, allEventsTxt, allBidsTxt, allBookedTxt, allBids, allBooked, userCategoryTv;
    LinearLayout emptyRv, addEventsTv, menu, searchLayout, viewAll, bidEvents,
            viewBooked, allEvents, allEventsLayout, bookedEvents, viewBid, categoryLayout,
            menuCategoryLayout, userCategoryLayout, displayEmpty, emptyBooked, emptyBookTv, bookedCorporate, bookedMusician;
    NestedScrollView scrollView;
    EditText searchTxt;
    RecyclerView searchEventsRv, bidRecyclerView, recyclerView, eventRecyclerView, doneRecyclerView,
            categoryRecyclerView, categoryMenuRv, userCategoryRv, myBookedRv;
    eventSearchAdapter eventSearch;

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
        myBookedRv = findViewById(R.id.bookedSvpRv);

        searchEventsBtn = findViewById(R.id.searchEventsBtn);
        menu = findViewById(R.id.menu_layout);
        scrollView = findViewById(R.id.scrollView);
        searchLayout = findViewById(R.id.search_events_layout);
        cancelBtn = findViewById(R.id.cancelBtn);
        searchTxt = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.search_icon);
        searchEventsRv = findViewById(R.id.searchEventsRv);
        categoryTxt = findViewById(R.id.categoryTxt);
        categoryLayout = findViewById(R.id.categoryLayout);
        cancelCategory = findViewById(R.id.cancelCategory);

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
        menuCategoryLayout = findViewById(R.id.menuCategoryLayout);
        userCategoryRv = findViewById(R.id.userCategoryRv);
        userCategoryTv = findViewById(R.id.userCategoryTv);
        userCategoryLayout = findViewById(R.id.userCategoryEvents);
        allEventsLayout = findViewById(R.id.allEventsLayout);
        emptyRv = findViewById(R.id.displayEmptyRv);
        addEvents = findViewById(R.id.addEventsBtn);
        addEventsTv = findViewById(R.id.addEventsTv);
        displayEmpty = findViewById(R.id.displayEmptyRvLayout);
        emptyBooked = findViewById(R.id.emptyBookedTv);
        emptyBookTv = findViewById(R.id.emptyBookTv);
        bookedCorporate = findViewById(R.id.bookedCorporate);
        bookedMusician = findViewById(R.id.bookedMusician);

        categoryMenuRv = findViewById(R.id.categoriesRv);
        //get user type
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        // Check user access level
        checkUserAccessLevel();

        events4 = new ArrayList<>();
        adapter1 = new eventAdapter(events4, this);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        userCategoryRv.setLayoutManager(layoutManager4);
        userCategoryRv.setAdapter(adapter1);

        setUpCategoryMenu();
        menuAdapter = new categoryMenuAdapter(this, menuList);
        categoryMenuRv.setLayoutManager(new GridLayoutManager(this, 4));
        categoryMenuRv.setAdapter(menuAdapter);

        // Set up all Events RecyclerView
        events = new ArrayList<>();
        adapter = new eventAdapter(events, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Set up event RecyclerView
        events2 = new ArrayList<>();
        eventAdapters = new eventAdapter2(events2, this);
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
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bidRecyclerView.setLayoutManager(layoutManager6);
        bidRecyclerView.setAdapter(bidAdapter);

        // Update RecyclerView with events from the selected category
        events3 = new ArrayList<>();
        categoryAdpt = new categoryAdapter(events3);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager5);
        categoryRecyclerView.setAdapter(categoryAdpt);

        booked1 = new ArrayList<>();
        bookedAdapter1 = new bookedEventsAdapter(booked1);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myBookedRv.setLayoutManager(layoutManager7);
        myBookedRv.setAdapter(bookedAdapter1);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.back_arrow).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.searchEventsBtn).setOnClickListener(v -> {
            searchEventsBtn.setVisibility(View.GONE);
            menu.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            addTask.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.GONE);
            bidEvents.setVisibility(View.GONE);
            // set title to search
            getSupportActionBar().setTitle("Search Events");
        });
        findViewById(R.id.cancelBtn).setOnClickListener(v -> {
            menu.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            addTask.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
            searchEventsBtn.setVisibility(View.VISIBLE);
            // set title to events
            getSupportActionBar().setTitle("Events");
            searchTxt.setText("");
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        });

        cancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLayout.setVisibility(View.GONE);
                cancelCategory.setVisibility(View.GONE);
                allEvents.setVisibility(View.VISIBLE);
                allEventsLayout.setVisibility(View.VISIBLE);

            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bidEvents.setVisibility(View.GONE);
                bookedEvents.setVisibility(View.GONE);
                cancelCategory.setVisibility(View.GONE);
                addTask.setVisibility(View.VISIBLE);
                menuCategoryLayout.setVisibility(View.VISIBLE);
                searchEventsBtn.setVisibility(View.VISIBLE);
                categoryLayout.setVisibility(View.GONE);
                allEventsLayout.setVisibility(View.VISIBLE);
                checkUserAccessLevel();

                allEventsTxt.setTextColor(getResources().getColor(R.color.orange));
                allBidsTxt.setTextColor(getResources().getColor(R.color.black));
                allBookedTxt.setTextColor(getResources().getColor(R.color.black));

            }
        });

        viewBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidEvents.setVisibility(View.VISIBLE);
                bookedEvents.setVisibility(View.GONE);
                allEvents.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.GONE);
                menuCategoryLayout.setVisibility(View.GONE);
                searchEventsBtn.setVisibility(View.GONE);
                userCategoryLayout.setVisibility(View.GONE);
                allEventsLayout.setVisibility(View.GONE);

                allEventsTxt.setTextColor(getResources().getColor(R.color.black));
                allBidsTxt.setTextColor(getResources().getColor(R.color.orange));
                allBookedTxt.setTextColor(getResources().getColor(R.color.black));
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
                categoryLayout.setVisibility(View.GONE);
                menuCategoryLayout.setVisibility(View.GONE);
                searchEventsBtn.setVisibility(View.GONE);
                userCategoryLayout.setVisibility(View.GONE);
                allEventsLayout.setVisibility(View.GONE);

                allEventsTxt.setTextColor(getResources().getColor(R.color.black));
                allBidsTxt.setTextColor(getResources().getColor(R.color.black));
                allBookedTxt.setTextColor(getResources().getColor(R.color.orange));

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

        menuAdapter.setOnItemClickListener(new categoryMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                if (itemName.equals("Sports")) {
                    fetchCategoryEvents("Sports");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Church")) {
                    fetchCategoryEvents("Church");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Wedding")) {
                    fetchCategoryEvents("Wedding");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Music")) {
                    fetchCategoryEvents("Music");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Graduation")) {
                    fetchCategoryEvents("Graduation");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Photography")) {
                    fetchCategoryEvents("Photography");
                    clearDisplay(itemName);
                }
                if (itemName.equals("Other")) {
                    fetchCategoryEvents("Other");
                    clearDisplay(itemName);
                }

            }
        });

        addEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addEvents.class));
            }
        });

        // Fetch events from Firestore
        fetchEventsS(userId);
        // Fetch events from Firestore
        fetchEvents();
        // fetch booked events from Firestore
        fetchBookedEvents();

        fetchBookedUsers(userId);

    }

    private void fetchBookedUsers(String userId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userIds = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // First, get all documents from the BookedEvents collection
        fStore.collection("BookedEvents")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Loop through each event in the BookedEvents collection
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String bookedEventId = document.getId();
                            // Now access the BookedBidders subcollection for each event
                            fStore.collection("BookedEvents")
                                    .document(bookedEventId)
                                    .collection("BookedBidders")
                                    .whereEqualTo("biddersId", userIds)
                                    .get()
                                    .addOnSuccessListener(biddersSnapshots -> {
                                        if (!biddersSnapshots.isEmpty()) {
                                            Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();
                                            myBookedRv.setVisibility(View.VISIBLE);
                                            emptyBookTv.setVisibility(View.GONE);

                                            // Convert each bidder document to your model class
                                            booked1.clear();
                                            for (DocumentSnapshot bidderDocument : biddersSnapshots) {
                                                // Convert each document to your model class
                                                bookedEventsModel bidder = bidderDocument.toObject(bookedEventsModel.class);
                                                booked1.add(bidder);
                                            }
                                            bookedAdapter1.notifyDataSetChanged();
                                        } else {
                                            // No matching bidder found, show the empty message
                                            Log.d("TAG", "No matching bidder found for event: " + bookedEventId);
                                            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                                            myBookedRv.setVisibility(View.GONE);
                                            emptyBookTv.setVisibility(View.VISIBLE);

                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error checking BookedBidders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // No events found, show the empty message
                        Log.d("TAG", "No events found in BookedEvents collection");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error for querying the BookedEvents collection
                    Toast.makeText(this, "Failed to fetch booked events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private void setUpCategoryMenu() {
        menuList = new ArrayList<>();
        menuList.add(new menuModel(R.drawable.sport_icon, "Sports"));
        menuList.add(new menuModel(R.drawable.church_icon, "Church"));
        menuList.add(new menuModel(R.drawable.flower_icon, "Wedding"));
        menuList.add(new menuModel(R.drawable.music_icon, "Music"));
        menuList.add(new menuModel(R.drawable.grad_icon, "Graduation"));
        menuList.add(new menuModel(R.drawable.camera_icon, "Photography"));
        menuList.add(new menuModel(R.drawable.other_icon, "Other"));
    }

    private void fetchCategoryEvents(String category) {

        fStore.collection("Events")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        categoryRecyclerView.setVisibility(View.VISIBLE);
                        displayEmpty.setVisibility(View.GONE);

                        events3.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            events3.add(event);
                        }
                        categoryAdpt.notifyDataSetChanged();

                    } else {
                        categoryRecyclerView.setVisibility(View.GONE);
                        displayEmpty.setVisibility(View.VISIBLE);
                    }

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
                            if (!queryDocumentSnapshots.isEmpty()) {
                                addEventsTv.setVisibility(View.GONE);
                                eventRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                addEventsTv.setVisibility(View.VISIBLE);
                                eventRecyclerView.setVisibility(View.GONE);
                            }
                            events.clear();
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
                        if (!queryDocumentSnapshots.isEmpty()) {
                            addEventsTv.setVisibility(View.GONE);
                            eventRecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            addEventsTv.setVisibility(View.VISIBLE);
                            eventRecyclerView.setVisibility(View.GONE);
                        }
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fStore.collection("BookedEvents")
                .whereEqualTo("creatorID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        emptyBooked.setVisibility(View.GONE);
                        doneRecyclerView.setVisibility(View.VISIBLE);
                        booked.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                            booked.add(event);
                            Log.d("TAG", "Event: " + event.getEventName());
                        }
                        bookedAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("TAG", "No events found");
                        emptyBooked.setVisibility(View.VISIBLE);
                        doneRecyclerView.setVisibility(View.GONE);
                    }
                });

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

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("Users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if ("Corporate".equalsIgnoreCase(userType)) {
                               userCategoryLayout.setVisibility(View.GONE);
                               addTask.setVisibility(View.VISIBLE);
                               allEvents.setVisibility(View.VISIBLE);
                               bookedCorporate.setVisibility(View.VISIBLE);
                               bookedMusician.setVisibility(View.GONE);
                            }
                            if ("Musician".equalsIgnoreCase(userType)) {
                                userCategoryLayout.setVisibility(View.VISIBLE);
                                addTask.setVisibility(View.GONE);
                                allEvents.setVisibility(View.GONE);
                                bookedCorporate.setVisibility(View.GONE);
                                bookedMusician.setVisibility(View.VISIBLE);
                                // Fetch events based on user category
                                String userCategory = document.getString("category");
                                if (userCategory != null) {
                                    userCategoryTv.setText(userCategory);
                                    fetchEventsBasedOnCategory(userCategory);
                                }else {
                                    Log.d("TAG", "Service provider category is null");
                                    // Toast.makeText(eventsActivity.this, "Service provider category is null", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Handle other user types if needed
                                Log.d("TAG", "User is neither Corporate nor Musician");
                                return;
                            }

                        }
                    } else {
                        Log.e("TaskListAdapter", "Error getting document", task.getException());
                    }
                });
        }

    private void fetchEventsBasedOnCategory(String category) {
        if (category == null || category.isEmpty()) {
            Log.e("fetchEventsBasedOnCategory", "User category is null or empty");
            return;
        }
        Log.d("fetchEventsBasedOnCategory", "Fetching events for category: " + category);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

// Fetch all documents in the Events collection
        fStore.collection("Events")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot eventDocument : queryDocumentSnapshots) {
                                String eventId = eventDocument.getId(); // Get the event ID

                                // Access the EventServices subcollection for this event
                                fStore.collection("Events")
                                        .document(eventId).collection("EventServices")
                                        .whereEqualTo("service", category) // Match the category
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot serviceSnapshots) {

                                                if (!serviceSnapshots.isEmpty()) {
                                                    eventModel event = eventDocument.toObject(eventModel.class);
                                                    events4.add(event);

                                                    adapter1.notifyDataSetChanged();
                                                } else {
                                                    Log.d("TAG", "No matching service found for event: " + eventId);
                                                }


                                            }
                                        }).addOnFailureListener(e -> Log.d("TAG", "Error getting EventServices: ", e));
                            }
                        } else {
                            Log.d("TAG", "No events found");
                        }
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "Error getting events: ", e));
    }

    private void clearDisplay(String itemName) {
        cancelCategory.setVisibility(View.VISIBLE);
        allEvents.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.VISIBLE);
        categoryTxt.setText(itemName);
        userCategoryLayout.setVisibility(View.GONE);
        allEventsLayout.setVisibility(View.GONE);
    }

}
