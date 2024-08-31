package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.menuModel;
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
    ArrayList<bookedEventsModel> booked;
    ArrayList<biddersEventModel> bidder;
    bidEventsAdapter bidAdapter;
    eventAdapter adapter, adapter1;
    eventAdapter2 eventAdapters;
    bookedEventsAdapter bookedAdapter;
    categoryAdapter categoryAdpt;
    FirebaseFirestore fStore;

    ArrayList<menuModel> menuList;
    categoryMenuAdapter menuAdapter;

    ImageButton backArrow,searchEventsBtn, cancelBtn, searchBtn, cancelCategory;
    TextView categoryTxt, allEventsTxt, allBidsTxt, allBookedTxt, allBids, allBooked, userCategoryTv;
    LinearLayout menu, searchLayout, viewAll, bidEvents, viewBooked, allEvents, bookedEvents, viewBid, categoryLayout, menuCategoryLayout, userCategoryLayout;
    ScrollView scrollView;
    EditText searchTxt;
    RecyclerView searchEventsRv, bidRecyclerView, recyclerView, eventRecyclerView, doneRecyclerView, categoryRecyclerView, categoryMenuRv, userCategoryRv;
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
        // Fetch events from Firestore
        fetchEventsS(userId);
        // Fetch events from Firestore
        fetchEvents();

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
                checkUserAccessLevel();
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bidEvents.setVisibility(View.GONE);
                bookedEvents.setVisibility(View.GONE);
                allEvents.setVisibility(View.VISIBLE);
                cancelCategory.setVisibility(View.GONE);
                addTask.setVisibility(View.VISIBLE);
                menuCategoryLayout.setVisibility(View.VISIBLE);
                searchEventsBtn.setVisibility(View.VISIBLE);
                categoryLayout.setVisibility(View.GONE);
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

                allEventsTxt.setTextColor(getResources().getColor(R.color.black));
                allBidsTxt.setTextColor(getResources().getColor(R.color.black));
                allBookedTxt.setTextColor(getResources().getColor(R.color.orange));
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
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Church")) {
                    fetchCategoryEvents("Church");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Wedding")) {
                    fetchCategoryEvents("Wedding");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Music")) {
                    fetchCategoryEvents("Music");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Graduation")) {
                    fetchCategoryEvents("Graduation");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Photography")) {
                    fetchCategoryEvents("Photography");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Other")) {
                    fetchCategoryEvents("Other");
                    cancelCategory.setVisibility(View.VISIBLE);
                    allEvents.setVisibility(View.GONE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryTxt.setText(itemName);
                    userCategoryLayout.setVisibility(View.GONE);
                }

            }
        });

    }

    private void fetchEventsBasedOnCategory(String category) {
        if (category == null || category.isEmpty()) {
            Log.e("fetchEventsBasedOnCategory", "User category is null or empty");
            return;
        } else {
            Log.d("fetchEventsBasedOnCategory", "User category: " + category);

        }
        CollectionReference eventsRef = fStore.collection("Events");

        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String eventId = documentSnapshot.getId();

                fStore.collection("Events")
                        .document(eventId)
                        .collection("EventServices")
                        .whereEqualTo("service", category)
                        .get()
                        .addOnSuccessListener(serviceSnapshot -> {
                            if (!serviceSnapshot.isEmpty()) {
                                eventModel event = documentSnapshot.toObject(eventModel.class);
                                events4.add(event);
                            }
                            adapter1.notifyDataSetChanged();
                        });
            }
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
                    events3.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        eventModel event = documentSnapshot.toObject(eventModel.class);
                        events3.add(event);
                    }
                    categoryAdpt.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(eventsActivity.this, "Failed to fetch category events", Toast.LENGTH_SHORT).show());

        if (category.equals("All")) {

        }


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

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if ("Corporate".equals(userType)) {
                               userCategoryLayout.setVisibility(View.GONE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                userCategoryLayout.setVisibility(View.VISIBLE);
                                String userCategory = document.getString("category");
                                userCategoryTv.setText(userCategory);
                                // Fetch events based on user category
                                fetchEventsBasedOnCategory(userCategory);

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
