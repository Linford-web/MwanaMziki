package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.categoryMenuAdapter;
import com.example.eventmuziki.Adapters.confirmAdapter;
import com.example.eventmuziki.Adapters.confirmEventAdapter;
import com.example.eventmuziki.Adapters.confirmedAdapter;
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.confirmPaymentModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class managePayment extends AppCompatActivity {

    ImageButton back, wallet;
    LinearLayout pendingBtn, confirmedBtn, pendingEvent, userBtn, productsBtn, advertsBtn, biddersLayout, serviceLayout,
            advertsLayout, corporate, musician, userBidBalance, userOrganizerBalance,
            confirmedUsersLayout, confirmedEventsLayout, emptyConfirmed, emptyConfirmed2,  emptyConfirmedUsers, emptyConfirmedEvents,
            confirmedLayout;
    TextView pendingBtnTxt, confirmedBtnTxt, userBalance, bidderBalance, eventsTxt, productTxt, advertTxt, eventName;
    RecyclerView pendingRv, confirmedUsersRv, confirmedEventsRv, pendingEventRv;
    ArrayList<ServicesDetails.bookedBiddersModel> bookedBidders, bookedEvents;
    confirmAdapter confirm;
    confirmEventAdapter adapter;
    ImageView poster, addAdvertisement, addService;
    ArrayList<confirmPaymentModel> confirmedModel, confirmedModel2;
    confirmedAdapter adapterConfirmed, adapterConfirmed2;


    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_payment);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back_arrow);
        wallet = findViewById(R.id.walletBtn);

        userBalance = findViewById(R.id.userBalance);
        bidderBalance = findViewById(R.id.bidderBalance);
        
        userBtn = findViewById(R.id.userBtn);
        productsBtn = findViewById(R.id.productsBtn);
        advertsBtn = findViewById(R.id.advertsBtn);
        eventsTxt = findViewById(R.id.eventsTxt);
        productTxt = findViewById(R.id.productTxt);
        advertTxt = findViewById(R.id.advertTxt);
        pendingBtn = findViewById(R.id.pendingBtn);
        confirmedBtn = findViewById(R.id.confirmBtn);
        pendingBtnTxt = findViewById(R.id.pendingBtnTxt);
        confirmedBtnTxt = findViewById(R.id.confirmedBtnTxt);
        pendingRv = findViewById(R.id.pendingRv);
        confirmedUsersRv = findViewById(R.id.confirmedUsersRv);
        pendingEvent = findViewById(R.id.Pending);
        pendingEventRv = findViewById(R.id.eventRv);
        userBalance = findViewById(R.id.userBalance);
        bidderBalance = findViewById(R.id.bidderBalance);
        biddersLayout = findViewById(R.id.biddersLayout);
        serviceLayout = findViewById(R.id.bookedServicesLayout);
        advertsLayout = findViewById(R.id.advertsLayout);
        corporate = findViewById(R.id.corporate);
        userBidBalance = findViewById(R.id.displayBiddersBalance);
        userOrganizerBalance = findViewById(R.id.displayCorporateBalance);
        poster = findViewById(R.id.Iconic);
        eventName = findViewById(R.id.eventName);
        emptyConfirmed = findViewById(R.id.emptyConfirmed);
        emptyConfirmedUsers = findViewById(R.id.emptyConfirmedUsers);
        emptyConfirmedEvents = findViewById(R.id.emptyConfirmedEvent);
        confirmedLayout = findViewById(R.id.confirmedLayout);
        confirmedEventsRv = findViewById(R.id.confirmedEventsRv);
        emptyConfirmed2 = findViewById(R.id.emptyConfirmed2);
        musician = findViewById(R.id.musician);
        addAdvertisement = findViewById(R.id.addAdverts);
        addService = findViewById(R.id.addServices);
        confirmedUsersLayout = findViewById(R.id.confirmedUsersLayout);
        confirmedEventsLayout = findViewById(R.id.confirmedEventsLayout);

        

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            finish();
        });
        wallet.setOnClickListener(v -> {
            finish();
        });

        checkUserAccessLevel();

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "Intent is Null", Toast.LENGTH_SHORT).show();
        }
        String eventId = intent.getStringExtra("eventId");
        String eventPoster = intent.getStringExtra("eventPoster");
        String name = intent.getStringExtra("eventName");
        String organizerName = intent.getStringExtra("organizerName");
        String bookedId = intent.getStringExtra("bookedId");
        String creatorId = intent.getStringExtra("creatorId");

        eventName.setText(name);

        if (eventPoster != null) {
            Glide.with(managePayment.this)
                    .load(eventPoster)
                    .centerCrop()
                    .placeholder(R.drawable.cover)
                    .error(R.drawable.cover)
                    .fallback(R.drawable.cover)
                    .into(poster);
        }

        bookedBidders = new ArrayList<>();
        confirm = new confirmAdapter(bookedBidders, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pendingRv.setLayoutManager(layoutManager);
        pendingRv.setAdapter(confirm);

        bookedEvents = new ArrayList<>();
        adapter = new confirmEventAdapter(bookedEvents, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pendingEventRv.setLayoutManager(layoutManager1);
        pendingEventRv.setAdapter(adapter);

        confirmedModel = new ArrayList<>();
        adapterConfirmed = new confirmedAdapter(confirmedModel, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        confirmedUsersRv.setLayoutManager(layoutManager2);
        confirmedUsersRv.setAdapter(adapterConfirmed);

        confirmedModel2 = new ArrayList<>();
        adapterConfirmed2 = new confirmedAdapter(confirmedModel2, this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        confirmedEventsRv.setLayoutManager(layoutManager3);
        confirmedEventsRv.setAdapter(adapterConfirmed2);

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        fStore.collection("BookedEvents")
                .whereEqualTo("creatorID", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            pendingRv.setVisibility(View.VISIBLE);
                            pendingEventRv.setVisibility(View.GONE);
                        }else {
                            pendingRv.setVisibility(View.GONE);
                            pendingEventRv.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(managePayment.this, "Error getting user", Toast.LENGTH_SHORT).show());

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingEvent.setVisibility(View.VISIBLE);
                confirmedLayout.setVisibility(View.GONE);
                pendingBtnTxt.setTextColor(getResources().getColor(R.color.orange));
                confirmedBtnTxt.setTextColor(getResources().getColor(R.color.black));
            }
        });

        confirmedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingEvent.setVisibility(View.GONE);
                confirmedLayout.setVisibility(View.VISIBLE);
                pendingBtnTxt.setTextColor(getResources().getColor(R.color.black));
                confirmedBtnTxt.setTextColor(getResources().getColor(R.color.orange));


            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productTxt.setTextColor(getResources().getColor(R.color.black));
                advertTxt.setTextColor(getResources().getColor(R.color.black));
                eventsTxt.setTextColor(getResources().getColor(R.color.orange));
                biddersLayout.setVisibility(View.VISIBLE);
                serviceLayout.setVisibility(View.GONE);
                advertsLayout.setVisibility(View.GONE);

            }
        });

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productTxt.setTextColor(getResources().getColor(R.color.orange));
                advertTxt.setTextColor(getResources().getColor(R.color.black));
                eventsTxt.setTextColor(getResources().getColor(R.color.black));
                biddersLayout.setVisibility(View.GONE);
                serviceLayout.setVisibility(View.VISIBLE);
                advertsLayout.setVisibility(View.GONE);

            }
        });

        advertsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productTxt.setTextColor(getResources().getColor(R.color.black));
                advertTxt.setTextColor(getResources().getColor(R.color.orange));
                eventsTxt.setTextColor(getResources().getColor(R.color.black));
                biddersLayout.setVisibility(View.GONE);
                serviceLayout.setVisibility(View.GONE);
                advertsLayout.setVisibility(View.VISIBLE);

            }
        });

        addAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(managePayment.this, addAdverts.class);
                startActivity(intent);
            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(managePayment.this, categoryOptions.class);
                startActivity(intent);
            }
        });

        // fetch booked users
        fetchBookedUsers(eventId);

        fetchBookedEvents(userId, eventId);

        fetchConfirmedUsers(userId, eventId);

        fetchConfirmedEvents(userId, eventId);

    }

    private void fetchConfirmedEvents(String userId, String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        // Reference to the "BookedEvents" collection
        CollectionReference bookedEventsRef = fStore.collection("BookedEvents");
        // Query all documents in "BookedEvents"
        bookedEventsRef.whereEqualTo("eventId", eventId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot bookedEventsSnapshot) {
                        // Loop through each document in "BookedEvents"
                        for (DocumentSnapshot bookedEventDoc : bookedEventsSnapshot.getDocuments()) {
                            String event = bookedEventDoc.getId();
                            fStore.collection("BookedEvents").document(event)
                                    .collection("PaymentConfirmation")
                                    .whereEqualTo("bidderId", userId)
                                    .whereEqualTo("eventId", eventId)
                                    .whereEqualTo("status", "Confirmed")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot paymentConfirmationSnapshot) {
                                            if (!paymentConfirmationSnapshot.isEmpty()) {
                                                Toast.makeText(managePayment.this, "Confirmed Events", Toast.LENGTH_SHORT).show();
                                                emptyConfirmed2.setVisibility(View.GONE);
                                                confirmedEventsRv.setVisibility(View.VISIBLE);

                                                confirmedModel.clear();
                                                for (DocumentSnapshot paymentConfirmationDoc : paymentConfirmationSnapshot.getDocuments()) {
                                                    confirmPaymentModel model = paymentConfirmationDoc.toObject(confirmPaymentModel.class);
                                                    confirmedModel.add(model);
                                                }
                                                adapterConfirmed.notifyDataSetChanged();

                                            }else {
                                                Toast.makeText(managePayment.this, "No Confirmed Events", Toast.LENGTH_SHORT).show();
                                                emptyConfirmed2.setVisibility(View.VISIBLE);
                                                confirmedEventsRv.setVisibility(View.GONE);
                                            }

                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchConfirmedUsers(String userId, String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        // Reference to the "BookedEvents" collection
        CollectionReference bookedEventsRef = fStore.collection("BookedEvents");
        // Query all documents in "BookedEvents"
        bookedEventsRef.whereEqualTo("eventId", eventId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot bookedEventsSnapshot) {
                // Loop through each document in "BookedEvents"
                for (DocumentSnapshot bookedEventDoc : bookedEventsSnapshot.getDocuments()) {
                    String event = bookedEventDoc.getId();
                    fStore.collection("BookedEvents")
                            .document(event)
                            .collection("PaymentConfirmation")
                            .whereEqualTo("creatorId", userId)
                            .whereEqualTo("eventId", eventId)
                            .whereEqualTo("status", "Confirmed")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot paymentConfirmationSnapshot) {
                                    if (!paymentConfirmationSnapshot.isEmpty()) {
                                        Toast.makeText(managePayment.this, "Confirmed Users", Toast.LENGTH_SHORT).show();
                                        emptyConfirmed.setVisibility(View.GONE);
                                        confirmedUsersRv.setVisibility(View.VISIBLE);

                                        confirmedModel2.clear();
                                        for (DocumentSnapshot paymentConfirmationDoc : paymentConfirmationSnapshot.getDocuments()) {
                                            confirmPaymentModel model = paymentConfirmationDoc.toObject(confirmPaymentModel.class);
                                            confirmedModel2.add(model);
                                        }
                                        adapterConfirmed2.notifyDataSetChanged();

                                    }else {
                                        Toast.makeText(managePayment.this, "No Confirmed Users", Toast.LENGTH_SHORT).show();
                                        emptyConfirmed.setVisibility(View.VISIBLE);
                                        confirmedUsersRv.setVisibility(View.GONE);
                                    }

                                }
                            }).addOnFailureListener(e -> Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBookedEvents(String userId, String eventId) {
        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String bookedEventId = documentSnapshot.getId();
                                // Now access the BookedBidders subcollection for each event
                                fStore.collection("BookedEvents")
                                        .document(bookedEventId)
                                        .collection("BookedBidders")
                                        .whereEqualTo("biddersId", userId)
                                        .whereEqualTo("eventId", eventId)
                                        .get()
                                        .addOnSuccessListener(biddersSnapshots -> {
                                            if (!biddersSnapshots.isEmpty()) {
                                                emptyConfirmedEvents.setVisibility(View.GONE);
                                                pendingEventRv.setVisibility(View.VISIBLE);

                                                double totalAmount1 = 0.0;
                                                bookedEvents.clear();
                                                for (DocumentSnapshot bidderSnapshot : biddersSnapshots) {
                                                    ServicesDetails.bookedBiddersModel model = bidderSnapshot.toObject(ServicesDetails.bookedBiddersModel.class);
                                                    bookedEvents.add(model);

                                                    // Calculate the total amount
                                                    String priceString = model.getBidAmount();
                                                    if (priceString != null && !priceString.isEmpty()) {
                                                        try {
                                                            double price = Double.parseDouble(priceString);
                                                            totalAmount1 += price;
                                                        } catch (NumberFormatException e) {
                                                            e.printStackTrace(); // Handle conversion error
                                                        }
                                                    }else {
                                                        totalAmount1 = 0.0;
                                                        Toast.makeText(managePayment.this, "Error getting Bid Amount", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                adapter.notifyDataSetChanged();
                                                bidderBalance.setText(String.format("%.2f", totalAmount1));
                                            } else {
                                                // No matching bidder found, show the empty message
                                                Log.d("TAG", "No matching bidder found for event: " + bookedEventId);
                                                emptyConfirmedEvents.setVisibility(View.VISIBLE);
                                                pendingEventRv.setVisibility(View.GONE);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }else {
                            Toast.makeText(managePayment.this, "No events found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkUserAccessLevel() {
        String userId = fAuth.getCurrentUser().getUid();
        fStore.collection("Users")
                .document(userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String usertype = documentSnapshot.getString("userType");
                            if (Objects.requireNonNull(usertype).equalsIgnoreCase("Musician")) {
                                corporate.setVisibility(View.GONE);
                                musician.setVisibility(View.VISIBLE);
                                userOrganizerBalance.setVisibility(View.GONE);
                                userBidBalance.setVisibility(View.VISIBLE);
                                confirmedEventsLayout.setVisibility(View.VISIBLE);
                                confirmedUsersLayout.setVisibility(View.GONE);

                            }
                            if (usertype.equalsIgnoreCase("Corporate")) {
                                corporate.setVisibility(View.VISIBLE);
                                musician.setVisibility(View.GONE);
                                userOrganizerBalance.setVisibility(View.VISIBLE);
                                userBidBalance.setVisibility(View.GONE);
                                confirmedUsersLayout.setVisibility(View.VISIBLE);
                                confirmedEventsLayout.setVisibility(View.GONE);

                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(managePayment.this, "Error getting user", Toast.LENGTH_SHORT).show());
    }

    private void fetchBookedUsers(String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String bookedEventId = documentSnapshot.getId();

                            fStore.collection("BookedEvents")
                                    .document(bookedEventId)
                                    .collection("BookedBidders")
                                    .get()
                                    .addOnSuccessListener(bidderSnapshots -> {
                                        if (!bidderSnapshots.isEmpty()) {
                                            emptyConfirmedUsers.setVisibility(View.GONE);
                                            pendingRv.setVisibility(View.VISIBLE);

                                            double totalAmount = 0.0;
                                            bookedBidders.clear();
                                            for (DocumentSnapshot bidderSnapshot : bidderSnapshots) {
                                                ServicesDetails.bookedBiddersModel model = bidderSnapshot.toObject(ServicesDetails.bookedBiddersModel.class);
                                                bookedBidders.add(model);

                                                // Calculate the total amount
                                                String priceString = model.getBidAmount();
                                                if (priceString != null && !priceString.isEmpty()) {
                                                    try {
                                                        double price = Double.parseDouble(priceString);
                                                        totalAmount += price;
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace(); // Handle conversion error
                                                    }
                                                }
                                            }
                                            confirm.notifyDataSetChanged();
                                            userBalance.setText(String.format("%.2f", totalAmount));


                                        } else {
                                            Log.d("BookedBidders", "No bidders found");
                                            emptyConfirmedUsers.setVisibility(View.VISIBLE);
                                            pendingRv.setVisibility(View.GONE);
                                        }
                                        
                                        
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Error", "Failed to retrieve BookedBidders subcollection: " + e.getMessage());
                                    });
                        }
                    } else {
                        Log.d("BookedEvents", "No booked events found for this eventId");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Error", "Failed to retrieve BookedEvents: " + e.getMessage());
                });
    }



}