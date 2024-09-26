package com.example.eventmuziki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.advertAdapter;
import com.example.eventmuziki.Adapters.dashAdapter;
import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Adapters.eventSearchAdapter;
import com.example.eventmuziki.Models.advertisementModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceNameModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainDashboard extends AppCompatActivity {

    ImageView profile;
    ImageButton searchBtn, cancelSearch;
    EditText searchTxt;
    TextView userNameTv;
    RecyclerView menuRv, searchEventsRv, allEventsRv;
    dashAdapter adapter3;
    FirebaseFirestore fStore;
    LinearLayout addEventsTv;
    ScrollView mainScrollView;
    eventSearchAdapter eventSearch;
    ArrayList<eventModel> allEvents;
    eventAdapter2 adapter;

    String name, email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dashboard);

        profile = findViewById(R.id.userProfileTv);
        userNameTv = findViewById(R.id.get_name);
        fStore = FirebaseFirestore.getInstance();
        menuRv = findViewById(R.id.menuRv);
        userId = FirebaseAuth.getInstance().getUid();
        searchBtn = findViewById(R.id.search_icon);
        searchTxt = findViewById(R.id.searchInput);
        searchEventsRv = findViewById(R.id.searchEventsRv);
        mainScrollView = findViewById(R.id.mainScrollView);
        cancelSearch = findViewById(R.id.cancel_search_icon);
        allEventsRv = findViewById(R.id.allRecyclerView);
        addEventsTv = findViewById(R.id.addEventsTv);

        setUpRecyclerView();

        fetchEvents();


        allEvents = new ArrayList<>();
        adapter = new eventAdapter2(allEvents, getApplicationContext());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        allEventsRv.setLayoutManager(layoutManager3);
        allEventsRv.setAdapter(adapter);


        if (userId != null) {
            // Fetch user details from Firestore "Users" collection
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
                                // Retrieve profile photo URL from Firestore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo using Glide
                                    Glide.with(this).load(profileImageUrl).into(profile);
                                } else {
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

        searchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScrollView.setVisibility(View.GONE);
                searchEventsRv.setVisibility(View.VISIBLE);
                searchBtn.setVisibility(View.VISIBLE);
                menuRv.setVisibility(View.GONE);
                cancelSearch.setVisibility(View.VISIBLE);
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScrollView.setVisibility(View.VISIBLE);
                searchEventsRv.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                cancelSearch.setVisibility(View.GONE);
                menuRv.setVisibility(View.VISIBLE);
                searchTxt.setText("");
                adapter.notifyDataSetChanged();
                // clear the search recyclerview adapter
                if (eventSearch != null) {
                    eventSearch.stopListening();
                }
                searchEventsRv.setAdapter(null);
                searchEventsRv.setLayoutManager(null);
                searchEventsRv.removeAllViews();
                // hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);
                }

                // Clear focus from the EditText
                searchTxt.clearFocus();

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
                setUpRecyclerViewSearch(searchTerm);
            }
        });

    }

    private void setUpRecyclerView() {
        ArrayList<serviceNameModel> menuItems = new ArrayList<>();

        menuItems.add(new serviceNameModel("Events", R.drawable.event_icon, eventsActivity.class));
        menuItems.add(new serviceNameModel("Adverts", R.drawable.advertising, advertActivity.class));
        menuItems.add(new serviceNameModel("Services", R.drawable.service_icon, categoryOptions.class));
        menuItems.add(new serviceNameModel("Chats", R.drawable.chat_icon, chatActivity.class));
        menuItems.add(new serviceNameModel("Profile", R.drawable.profile_icon, profileActivity.class));

        adapter3 = new dashAdapter(MainDashboard.this, menuItems);
        menuRv.setLayoutManager(new GridLayoutManager(this, 5));
        menuRv.setAdapter(adapter3);
    }

    private void setUpRecyclerViewSearch(String searchTerm) {
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
                            allEventsRv.setVisibility(View.VISIBLE);
                        } else {
                            addEventsTv.setVisibility(View.VISIBLE);
                            allEventsRv.setVisibility(View.GONE);
                        }
                        allEvents.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            allEvents.add(event);
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

}