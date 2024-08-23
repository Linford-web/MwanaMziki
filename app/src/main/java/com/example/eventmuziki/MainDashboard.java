package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.eventmuziki.Adapters.eventAdapter2;
import com.example.eventmuziki.Models.advertisementModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceNameModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainDashboard extends AppCompatActivity {

    Button adverts;
    ImageView profile;
    TextView userNameTv;
    RecyclerView eventRv, advertsRv, menuRv;
    ArrayList<eventModel> events;
    ArrayList<advertisementModel> advert;
    eventAdapter2 adapter;
    advertAdapter adapter2;
    dashAdapter adapter3;
    FirebaseFirestore fStore;
    BottomNavigationView bottomNavigationView;

    String name, email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dashboard);

        adverts = findViewById(R.id.addAdvertBtn);
        profile = findViewById(R.id.userProfileTv);
        userNameTv = findViewById(R.id.get_name);
        eventRv = findViewById(R.id.eventsRecyclerView);
        fStore = FirebaseFirestore.getInstance();
        advertsRv = findViewById(R.id.advertRv);
        menuRv = findViewById(R.id.menuRv);
        userId = FirebaseAuth.getInstance().getUid();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (view, insets) -> {
            // Remove the bottom padding
            view.setPadding(0, 0, 0,0);
            return insets;
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    return true;
                } else if (itemId == R.id.services) {
                    startActivity(new Intent(getApplicationContext(), categoryOptions.class));
                    return true;
                }else {
                    // Handle other menu items if needed
                }
                return false;
            }
        });

        // Fetch events from Firestore
        fetchEventSs(userId);
        fetchAdverts(userId);
        // Initialize Event RecyclerView
        events = new ArrayList<>();
        adapter = new eventAdapter2(events, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventRv.setLayoutManager(layoutManager);
        eventRv.setAdapter(adapter);

        // Initialize Advertisement RecyclerView
        advert = new ArrayList<>();
        adapter2 = new advertAdapter(advert);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        advertsRv.setLayoutManager(layoutManager2);
        advertsRv.setAdapter(adapter2);

        if (userId != null){
            // Fetch user details from FireStore "Users" collection
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
                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(profile);
                                } else {
                                    // Handle the case when no profile photo is available
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

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        ArrayList<serviceNameModel> menuItems = new ArrayList<>();

        menuItems.add(new serviceNameModel("Events", R.drawable.event_icon, eventsActivity.class));
        menuItems.add(new serviceNameModel("Adverts", R.drawable.advertising, advertActivity.class));
        menuItems.add(new serviceNameModel("Chats", R.drawable.chat_icon, chatActivity.class));
        menuItems.add(new serviceNameModel("Services", R.drawable.service_icon, categoryOptions.class));

        adapter3 = new dashAdapter(MainDashboard.this, menuItems);
        menuRv.setLayoutManager(new GridLayoutManager(this, 4));
        menuRv.setAdapter(adapter3);
    }

    private void fetchAdverts(String userId) {
        fStore.collection("Advertisements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        advertisementModel advertTv = documentSnapshot.toObject(advertisementModel.class);
                        advert.add(advertTv);
                    }
                    adapter2.notifyDataSetChanged();
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchEventSs(String userId) {
        fStore.collection("Events")
                .whereEqualTo("creatorID", userId)
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
                        Toast.makeText(MainDashboard.this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}