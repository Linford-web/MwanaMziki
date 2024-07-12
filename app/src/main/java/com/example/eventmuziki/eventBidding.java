package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.bidEventsAdapter;
import com.example.eventmuziki.Adapters.biddersAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class eventBidding extends AppCompatActivity {

    TextView eventName, eventDate, eventTime, eventLocation, categoryNme, organizerName, eventDetails, amountTv, category, biddersName, categoryTv;
    EditText bidAmountTv;
    Button placeBid;
    ImageView eventPosterTv, backArrow;
    LinearLayout biddersView, allBiddersLayout, categoryLayout;
    FirebaseFirestore fStore;

    ArrayList<biddersEventModel> bidEvents, bidders;
    RecyclerView recyclerView, allBiddersRv;
    biddersAdapter bidAdapter, bidAdapter1;

    String eventId, categoryTxt, creatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_bidding);

        eventName = findViewById(R.id.eventNameTv);
        eventDate = findViewById(R.id.dateTv);
        eventTime = findViewById(R.id.timeTv);
        eventLocation = findViewById(R.id.locationTv);
        organizerName = findViewById(R.id.organizerNameTv);
        eventDetails = findViewById(R.id.eventDetailsTv);
        amountTv = findViewById(R.id.amountTv);
        bidAmountTv = findViewById(R.id.amountTxt);
        placeBid = findViewById(R.id.placeBidBtn);
        category = findViewById(R.id.categoryTv);
        eventPosterTv = findViewById(R.id.eventPoster);
        backArrow = findViewById(R.id.back_arrow);
        biddersName = findViewById(R.id.bidNameTv);
        biddersView = findViewById(R.id.biddersLayout);
        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.biddersRv);
        categoryTv = findViewById(R.id.category_bidderTv);
        categoryNme = findViewById(R.id.category_Tv);
        allBiddersLayout = findViewById(R.id.allBiddersLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        allBiddersRv = findViewById(R.id.allBiddersRv);

        bidEvents = new ArrayList<>();
        bidAdapter1 = new biddersAdapter(bidEvents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bidAdapter1);

        // Update RecyclerView with bidders from the selected event and category
        bidders = new ArrayList<>();
        bidAdapter = new biddersAdapter(bidders);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        allBiddersRv.setLayoutManager(layoutManager1);
        allBiddersRv.setAdapter(bidAdapter);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the event object from the intent
        eventModel event = getIntent().getParcelableExtra("eventModel");
        if (event != null) {
            // Set the event details in the views
            eventName.setText(event.getEventName());
            eventDate.setText(event.getDate());
            eventTime.setText(event.getTime());
            eventLocation.setText(event.getLocation());
            eventDetails.setText(event.getEventDetails());
            amountTv.setText(event.getAmount());
            category.setText(event.getCategory());
            organizerName.setText(event.getOrganizerName());
            // Get the event ID
            eventId = event.getEventId();
            categoryTxt = event.getCategory();
            creatorId = event.getCreatorID();

        }

        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the bid amount from the EditText
                confirmAndPlaceBid(eventId, creatorId);
            }
        });

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
                                Glide.with(eventBidding.this)
                                        .load(eventPoster)
                                        .centerCrop()
                                        .into(eventPosterTv);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventBidding.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // fetch bidders user name
        fetchUserName();
        // check user access level
        checkUserAccessLevel(eventId, categoryTxt);

    }

    private void checkUserAccessLevel(String events, String categories) {
        String userId = FirebaseAuth.getInstance().getUid();

        DocumentReference df = fStore.collection("Users").document(userId);

        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify User Access Level
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("Corporate") ){
                        biddersView.setVisibility(View.GONE);
                        allBiddersLayout.setVisibility(View.VISIBLE);
                        categoryLayout.setVisibility(View.GONE);
                        fetchBidders(events);
                    } if (userType.equals("Musician")) {
                        biddersView.setVisibility(View.VISIBLE);
                        categoryTv.setText(categories);
                        categoryNme.setText(categories);
                        allBiddersLayout.setVisibility(View.GONE);
                        categoryLayout.setVisibility(View.VISIBLE);
                        fetchCategoryBidders(eventId, categories);
                    } else {
                        // Handle other user types if needed
                        Log.d("TAG", "User is neither Corporate nor Musician");
                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Failed to get document: " + e.getMessage());
            }
        });

    }

    private void fetchCategoryBidders(String eventId, String category) {
        fStore.collection("BidEvents")
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bidders.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        biddersEventModel bidder = documentSnapshot.toObject(biddersEventModel.class);
                        bidders.add(bidder);
                    }
                    bidAdapter.notifyDataSetChanged();

                }).addOnFailureListener(e -> Toast.makeText(eventBidding.this, "Failed to fetch category bidders", Toast.LENGTH_SHORT).show());
    }

    private void fetchBidders(String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BidEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            bidEvents.clear();
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                biddersEventModel bidEvent = documentSnapshot.toObject(biddersEventModel.class);
                                bidEvents.add(bidEvent);
                            }
                            bidAdapter1.notifyDataSetChanged();
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventBidding.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void fetchUserName() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fStore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    biddersName.setText(name);
                } else {
                    Toast.makeText(eventBidding.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(eventBidding.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmAndPlaceBid(String eventId, String creatorId) {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String bidAmount = bidAmountTv.getText().toString().trim();
        String name = eventName.getText().toString().trim();
        String date = eventDate.getText().toString().trim();
        String time = eventTime.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String details = eventDetails.getText().toString().trim();
        String eventCategory = category.getText().toString().trim();
        String bidder = biddersName.getText().toString().trim();

        // Validate the bid amount
        if (bidAmount.isEmpty()) {
            bidAmountTv.setError("Please enter a bid amount");
        }
        else {
            // Add the bid to Firestore
            biddersEventModel bookedEvent = new biddersEventModel(name, details, date, time, location, bidAmount, eventId, creatorId, "", eventCategory,currentUserId, "", "");

            // Check if the current user has already placed a bid for this event
            fStore.collection("BidEvents")
                    .whereEqualTo("eventId", eventId)
                    .whereEqualTo("biddersId", currentUserId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            fStore.collection("BidEvents")
                                    .add(bookedEvent)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            String o_name = organizerName.getText().toString();
                                            documentReference.update("organizerName", o_name);
                                            documentReference.update("biddersName", bidder);
                                            documentReference.update("bidId", documentReference.getId());

                                            Toast.makeText(eventBidding.this, "Bid placed successfully", Toast.LENGTH_SHORT).show();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(eventBidding.this, bidEvents.class));
                                                    finish();
                                                }
                                            }, 2000);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(eventBidding.this, "Failed to place bid", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(eventBidding.this, "You have already made a bid on this", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(eventBidding.this, "Failed to check bid", Toast.LENGTH_SHORT).show();
                    });

        }


    }

}