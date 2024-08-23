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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.bidEventsAdapter;
import com.example.eventmuziki.Adapters.bidSearchAdapter;
import com.example.eventmuziki.Adapters.eventSearchAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class bidEvents extends AppCompatActivity {

    RecyclerView bookedEventRv, bidSearchRv;
    ArrayList<biddersEventModel> booked;
    bidEventsAdapter bookedAdapter;
    FirebaseFirestore fStore;

    LinearLayout searchBidEventsLayout, bidEventsLayout;
    EditText searchTxt;
    ImageButton searchBtn, cancelBtn, search;

    // create an adapter class for bid events search
    bidSearchAdapter eventSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bid_events);


        bookedEventRv = findViewById(R.id.bookedEventsRv);
        fStore = FirebaseFirestore.getInstance();
        searchBidEventsLayout = findViewById(R.id.searchBidEventsLayout);
        bidEventsLayout = findViewById(R.id.bidEventsLayout);
        searchTxt = findViewById(R.id.searchInput);
        search = findViewById(R.id.search_icon);
        cancelBtn = findViewById(R.id.cancelBtn);
        searchBtn = findViewById(R.id.searchEventsBtn);
        bidSearchRv = findViewById(R.id.bidSearchRv);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.back_arrow).setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(bidEvents.this, eventsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        searchBtn.setOnClickListener(v -> {
            bidEventsLayout.setVisibility(View.GONE);
            searchBidEventsLayout.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
            findViewById(R.id.back_arrow).setVisibility(View.GONE);
            // set title to search
            getSupportActionBar().setTitle("Bid Events");
        });
        cancelBtn.setOnClickListener(v -> {
            bidEventsLayout.setVisibility(View.VISIBLE);
            searchBidEventsLayout.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
            // set title to events
            getSupportActionBar().setTitle("Bid Events");
            findViewById(R.id.back_arrow).setVisibility(View.VISIBLE);
            searchTxt.setText("");
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        });

        booked = new ArrayList<>();
        bookedAdapter = new bidEventsAdapter(booked);

        // Set up RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookedEventRv.setLayoutManager(layoutManager);
        bookedEventRv.setAdapter(bookedAdapter);

        // Fetch booked events from Firestore
        fetchBidEvents();

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

    }

    private void setUpSearchView(String searchTerm) {
        Query query = FirebaseFirestore.getInstance().collection("BidEvents")
                .whereGreaterThanOrEqualTo("eventName", searchTerm);
        FirestoreRecyclerOptions<biddersEventModel> option = new FirestoreRecyclerOptions.Builder<biddersEventModel>()
                .setQuery(query, biddersEventModel.class).build();

        eventSearch = new bidSearchAdapter(option, getApplicationContext());
        bidSearchRv.setLayoutManager(new LinearLayoutManager(this));
        bidSearchRv.setAdapter(eventSearch);
        eventSearch.startListening();
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
                        booked.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

        fStore.collection("BidEvents")
                .whereEqualTo("biddersId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        biddersEventModel event = documentSnapshot.toObject(biddersEventModel.class);
                        booked.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(bidEvents.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}