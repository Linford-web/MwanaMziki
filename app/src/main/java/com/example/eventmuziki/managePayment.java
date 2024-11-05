package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.advertPosterAdapter;
import com.example.eventmuziki.Adapters.confirmAdapter;
import com.example.eventmuziki.Adapters.confirmEventAdapter;
import com.example.eventmuziki.Adapters.confirmedAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cartAdapter;
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.Models.confirmPaymentModel;
import com.example.eventmuziki.Models.eventAdvertModel;
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
            confirmedUsersLayout, confirmedEventsLayout, emptyConfirmed, emptyConfirmed2,  emptyConfirmedUsers, emptyConfirmedEvents, emptyServices, emptyAdvert,
            confirmedLayout, organizerBalanceTv, bidderBalanceTv;
    TextView pendingBtnTxt, confirmedBtnTxt, userBalance, bidderBalance, eventsTxt, productTxt, advertTxt, eventName,servicesBalance, advertBalance;
    RecyclerView pendingRv, confirmedUsersRv, confirmedEventsRv, pendingEventRv, servicesRv, advertRv;
    ArrayList<ServicesDetails.bookedBiddersModel> bookedBidders, bookedEvents;
    confirmAdapter confirm;
    confirmEventAdapter adapter;
    ImageView poster, addAdvertisement, addService;
    ArrayList<confirmPaymentModel> userModel, eventModel;
    confirmedAdapter eventAdapter, userAdapter;
    ArrayList<ServicesDetails.cartModel> bookedList;
    cartAdapter cartServices;
    ArrayList<AdvertisementDetails.posterModel> advertList;
    advertPosterAdapter posterAdapter;

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
        servicesRv = findViewById(R.id.servicesRv);
        emptyServices = findViewById(R.id.emptyServices);
        emptyAdvert = findViewById(R.id.emptyAdverts);
        advertRv = findViewById(R.id.advertsRv);
        organizerBalanceTv = findViewById(R.id.displayOrganizerBalances);
        bidderBalanceTv = findViewById(R.id.displayBidderBalances);
        servicesBalance = findViewById(R.id.serviceBalance);
        advertBalance = findViewById(R.id.advertBalance);





        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            finish();
        });
        wallet.setOnClickListener(v -> {
            Intent intent = new Intent(managePayment.this, walletActivity.class);
            startActivity(intent);
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

        userModel = new ArrayList<>();
        eventAdapter = new confirmedAdapter(userModel, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        confirmedUsersRv.setLayoutManager(layoutManager2);
        confirmedUsersRv.setAdapter(eventAdapter);

        eventModel = new ArrayList<>();
        userAdapter = new confirmedAdapter(eventModel, this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        confirmedEventsRv.setLayoutManager(layoutManager3);
        confirmedEventsRv.setAdapter(userAdapter);

        bookedList = new ArrayList<>();
        cartServices = new cartAdapter(bookedList, this);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        servicesRv.setLayoutManager(layoutManager4);
        servicesRv.setAdapter(cartServices);

        advertList = new ArrayList<>();
        posterAdapter = new advertPosterAdapter(advertList, this);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        advertRv.setLayoutManager(layoutManager5);
        advertRv.setAdapter(posterAdapter);

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

                fetchConfirmedUsers(userId, eventId);

                fetchConfirmedEvents(userId, eventId);

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

        fetchBookedServices(eventId);

        fetchAdverts(eventId, userId);

    }

    private void fetchAdverts(String eventId, String userId) {
        fStore.collection("Advertisements")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                        if (!queryDocumentSnapshot.isEmpty()) {

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshot) {
                                documentSnapshot.getReference()
                                        .collection("EventAdvertisements")
                                        .whereEqualTo("eventId", eventId)
                                        .whereEqualTo("creatorId", userId)
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                               if (!queryDocumentSnapshots.isEmpty()) {
                                                   emptyAdvert.setVisibility(View.GONE);
                                                   advertRv.setVisibility(View.VISIBLE);
                                                   for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                       String advertId = document.getString("advertId");
                                                       fetchAdvertPlatforms(advertId);
                                                       Log.d("managePayment", "Success");
                                                   }
                                               }else{
                                                   Log.d("managePayment", "No adverts found");
                                                   emptyAdvert.setVisibility(View.VISIBLE);
                                                   advertRv.setVisibility(View.GONE);
                                               }
                                            }
                                        }).addOnFailureListener(e -> Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }else {
                            Log.d("TAG", "No adverts found");

                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchAdvertPlatforms(String advertId) {
        fStore.collection("Advertisements")
                .document(advertId).collection("AdvertPlatforms")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            emptyAdvert.setVisibility(View.GONE);
                            advertRv.setVisibility(View.VISIBLE);

                            double totalAmountss = 0.0;
                            advertList.clear();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                AdvertisementDetails.posterModel posterModel = document.toObject(AdvertisementDetails.posterModel.class);
                                advertList.add(posterModel);
                                // Toast.makeText(managePayment.this, "Success", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "Success");
                                // Calculate the total amount
                                String priceString = posterModel.getCost();
                                if (priceString != null && !priceString.isEmpty()) {
                                    try {
                                        double price = Double.parseDouble(priceString);
                                        totalAmountss += price;
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace(); // Handle conversion error
                                    }
                                }else {
                                    totalAmountss = 0.0;
                                    Toast.makeText(managePayment.this, "Error getting Bid Amount", Toast.LENGTH_SHORT).show();
                                }
                            }
                            posterAdapter.notifyDataSetChanged();
                            advertBalance.setText(String.format("%.2f", totalAmountss));

                        }else{
                            emptyAdvert.setVisibility(View.VISIBLE);
                            advertRv.setVisibility(View.GONE);
                            Log.d("TAG", "No adverts found");
                        }

                    }
                }).addOnFailureListener(e -> Toast.makeText(managePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchBookedServices(String eventId) {
        fStore.collection("Events")
                .document(eventId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Now query the BookedServices subcollection
                            fStore.collection("Events")
                                    .document(eventId)
                                    .collection("BookedServices")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                emptyServices.setVisibility(View.GONE);
                                                servicesRv.setVisibility(View.VISIBLE);
                                            } else {
                                                emptyServices.setVisibility(View.VISIBLE);
                                                servicesRv.setVisibility(View.GONE);
                                            }

                                            double totalAmounts = 0.0;
                                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                ServicesDetails.cartModel bookedService = document.toObject(ServicesDetails.cartModel.class);
                                                bookedList.add(bookedService);

                                                // Calculate the total amount
                                                String priceString = bookedService.getPrice();
                                                if (priceString != null && !priceString.isEmpty()) {
                                                    try {
                                                        double price = Double.parseDouble(priceString);
                                                        totalAmounts += price;
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace(); // Handle conversion error
                                                    }
                                                }else {
                                                    totalAmounts = 0.0;
                                                    Toast.makeText(managePayment.this, "Error getting Bid Amount", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            cartServices.notifyDataSetChanged();
                                            servicesBalance.setText(String.format("%.2f", totalAmounts));

                                        }
                                    })
                                    .addOnFailureListener(e -> Log.d("TAG", "Error fetching BookedServices: " + e.getMessage()));
                        } else {
                            Log.d("TAG", "Event not found");
                        }
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.getMessage()));
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
                                                Log.d("managePayment", "Success");
                                                emptyConfirmed2.setVisibility(View.GONE);
                                                confirmedEventsRv.setVisibility(View.VISIBLE);

                                                eventModel.clear();
                                                for (DocumentSnapshot paymentConfirmationDoc : paymentConfirmationSnapshot.getDocuments()) {
                                                    confirmPaymentModel model = paymentConfirmationDoc.toObject(confirmPaymentModel.class);
                                                    eventModel.add(model);
                                                }
                                                eventAdapter.notifyDataSetChanged();

                                            }else {
                                                Log.d("managePayment", "No Confirmed Events");
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
                                    // Check if there are any matching documents
                                    if (!paymentConfirmationSnapshot.isEmpty()) {
                                        emptyConfirmed.setVisibility(View.GONE);
                                        confirmedUsersRv.setVisibility(View.VISIBLE);

                                        userModel.clear();
                                        for (DocumentSnapshot paymentConfirmationDoc : paymentConfirmationSnapshot) {
                                            confirmPaymentModel model = paymentConfirmationDoc.toObject(confirmPaymentModel.class);
                                            userModel.add(model);
                                            Log.d("managePayment", "Success");
                                        }
                                        userAdapter.notifyDataSetChanged();

                                    }else {
                                        Log.d("managePayment", "No Confirmed Users");
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
                                organizerBalanceTv.setVisibility(View.GONE);
                                bidderBalanceTv.setVisibility(View.VISIBLE);
                                confirmedEventsLayout.setVisibility(View.VISIBLE);
                                confirmedUsersLayout.setVisibility(View.GONE);

                            }
                            if (usertype.equalsIgnoreCase("Corporate")) {
                                corporate.setVisibility(View.VISIBLE);
                                musician.setVisibility(View.GONE);
                                organizerBalanceTv.setVisibility(View.VISIBLE);
                                bidderBalanceTv.setVisibility(View.GONE);
                                confirmedUsersLayout.setVisibility(View.VISIBLE);
                                confirmedEventsLayout.setVisibility(View.GONE);

                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(managePayment.this, "Error getting user", Toast.LENGTH_SHORT).show());
    }




}