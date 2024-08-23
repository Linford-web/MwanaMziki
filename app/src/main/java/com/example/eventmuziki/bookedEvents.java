package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bookedEventsAdapter;
import com.example.eventmuziki.Adapters.bookedSearchAdapter;
import com.example.eventmuziki.Adapters.eventSearchAdapter;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.eventModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class bookedEvents extends AppCompatActivity {

    ImageView back;
    RecyclerView bookedRv, bookedSearchRv;

    ArrayList<bookedEventsModel> booked;
    bookedEventsAdapter bookedAdapter;

    LinearLayout searchBookedEventsLayout, bookedEventsLayout;
    EditText searchTxt;
    ImageButton searchBtn, cancelBtn, search;

    bookedSearchAdapter eventSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booked_events);

        back = findViewById(R.id.back_arrow);
        bookedRv = findViewById(R.id.bookedEventsRv);
        searchBookedEventsLayout = findViewById(R.id.searchBookedEventsLayout);
        bookedEventsLayout = findViewById(R.id.bookedEventsLayout);
        searchTxt = findViewById(R.id.searchInput);
        search = findViewById(R.id.search_icon);
        cancelBtn = findViewById(R.id.cancelBtn);
        searchBtn = findViewById(R.id.searchEventsBtn);


        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(bookedEvents.this, eventsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        searchBtn.setOnClickListener(v -> {
            bookedEventsLayout.setVisibility(View.GONE);
            searchBookedEventsLayout.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
            findViewById(R.id.back_arrow).setVisibility(View.GONE);
            // set title to search
            getSupportActionBar().setTitle("Bid Events");
        });
        cancelBtn.setOnClickListener(v -> {
            bookedEventsLayout.setVisibility(View.VISIBLE);
            searchBookedEventsLayout.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
            // set title to events
            getSupportActionBar().setTitle("Booked Events");
            findViewById(R.id.back_arrow).setVisibility(View.VISIBLE);
            searchTxt.setText("");
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchTerm = searchTxt.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                    searchTxt.setError("Enter at least 3 characters");
                    return;
                }
                setUpSearchView(searchTerm);
            }
        });

        booked = new ArrayList<>();
        bookedAdapter = new bookedEventsAdapter(booked);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookedRv.setLayoutManager(layoutManager);
        bookedRv.setAdapter(bookedAdapter);

        // fetch booked events
        fetchBookedEvents();

    }

    private void setUpSearchView(String searchTerm) {
        Query query = FirebaseFirestore.getInstance().collection("BookedEvents")
                .whereGreaterThanOrEqualTo("eventName", searchTerm);
        FirestoreRecyclerOptions<bookedEventsModel> option = new FirestoreRecyclerOptions.Builder<bookedEventsModel>()
                .setQuery(query, bookedEventsModel.class).build();

        eventSearch = new bookedSearchAdapter(option, getApplicationContext());
        bookedSearchRv.setLayoutManager(new LinearLayoutManager(this));
        bookedSearchRv.setAdapter(eventSearch);
        eventSearch.startListening();
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
                        if (!events.contains(event.getEventId())) {
                            booked.add(event);
                            events.add(event.getEventId());
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