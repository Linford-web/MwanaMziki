package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import com.example.eventmuziki.Adapters.confirmAdapter;
import com.example.eventmuziki.Adapters.confirmEventAdapter;
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class managePayment extends AppCompatActivity {

    ImageButton back, wallet;
    LinearLayout pendingBtn, confirmedBtn, pendingEvent;
    TextView pendingBtnTxt, confirmedBtnTxt, userBalance, bidderBalance;
    RecyclerView pendingRv, confirmedRv, pendingEventRv;
    ArrayList<ServicesDetails.bookedBiddersModel> bookedBidders, bookedEvents;
    confirmAdapter confirm;
    confirmEventAdapter adapter;


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
        pendingBtn = findViewById(R.id.pendingBtn);
        confirmedBtn = findViewById(R.id.confirmBtn);
        pendingBtnTxt = findViewById(R.id.pendingBtnTxt);
        confirmedBtnTxt = findViewById(R.id.confirmedBtnTxt);
        pendingRv = findViewById(R.id.pendingRv);
        confirmedRv = findViewById(R.id.confirmedRv);
        pendingEvent = findViewById(R.id.pendings);
        pendingEventRv = findViewById(R.id.eventRv);
        userBalance = findViewById(R.id.userBalance);
        bidderBalance = findViewById(R.id.bidderBalance);


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
        String eventName = intent.getStringExtra("eventName");
        String organizerName = intent.getStringExtra("organizerName");
        String bookedId = intent.getStringExtra("bookedId");
        String creatorId = intent.getStringExtra("creatorId");

        bookedBidders = new ArrayList<>();
        confirm = new confirmAdapter(bookedBidders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pendingRv.setLayoutManager(layoutManager);
        pendingRv.setAdapter(confirm);

        bookedEvents = new ArrayList<>();
        adapter = new confirmEventAdapter(bookedEvents);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pendingEventRv.setLayoutManager(layoutManager1);
        pendingEventRv.setAdapter(adapter);

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
                confirmedRv.setVisibility(View.GONE);
                pendingBtnTxt.setTextColor(getResources().getColor(R.color.orange));
                confirmedBtnTxt.setTextColor(getResources().getColor(R.color.black));
            }
        });
        confirmedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingEvent.setVisibility(View.GONE);
                confirmedRv.setVisibility(View.VISIBLE);
                pendingBtnTxt.setTextColor(getResources().getColor(R.color.black));
                confirmedBtnTxt.setTextColor(getResources().getColor(R.color.orange));
            }
        });

        // fetch booked users
        fetchBookedUsers(eventId);

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
                            if (usertype.equalsIgnoreCase("Musician")) {
                                pendingRv.setVisibility(View.GONE);
                                pendingEventRv.setVisibility(View.VISIBLE);
                                bidderBalance.setVisibility(View.VISIBLE);
                                userBalance.setVisibility(View.GONE);
                            }
                            if (usertype.equalsIgnoreCase("Corporate")) {
                                pendingRv.setVisibility(View.VISIBLE);
                                pendingEventRv.setVisibility(View.GONE);
                                userBalance.setVisibility(View.VISIBLE);
                                bidderBalance.setVisibility(View.GONE);
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


                                            bookedEvents.clear();
                                            for (DocumentSnapshot bidderSnapshot : queryDocumentSnapshots) {
                                                ServicesDetails.bookedBiddersModel model = bidderSnapshot.toObject(ServicesDetails.bookedBiddersModel.class);
                                                bookedEvents.add(model);

                                                // Calculate the total amount
                                                String priceString = model.getBidAmount();
                                                if (priceString != null && !priceString.isEmpty()) {
                                                    try {
                                                        double price = Double.parseDouble(priceString);
                                                        totalAmount += price;
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace(); // Handle conversion error
                                                    }
                                                }else {
                                                    totalAmount = 0.0;
                                                    Toast.makeText(managePayment.this, "Error getting user", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                            bidderBalance.setText(String.format("%.2f", totalAmount));

                                        } else {
                                            Log.d("BookedBidders", "No bidders found");
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