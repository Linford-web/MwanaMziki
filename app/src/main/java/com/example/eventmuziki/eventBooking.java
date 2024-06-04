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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.biddersAdapter;
import com.example.eventmuziki.Models.bookedEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class eventBooking extends AppCompatActivity {

    TextView eventName, eventDate, eventTime, eventLocation, organizerName, eventDetails, amountTv, category, id, id2, biddersName;
    EditText bidAmountTv;
    Button placeBid;
    ImageView eventPosterTv, backArrow;
    LinearLayout biddersView;
    FirebaseFirestore fStore;

    ArrayList<bookedEventModel> bidEvents;
    RecyclerView recyclerView;
    biddersAdapter bidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_booking);

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
        id = findViewById(R.id.creatorIdTV);
        id2 = findViewById(R.id.eventIdTv);
        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.biddersRv);


        bidEvents = new ArrayList<>();
        bidAdapter = new biddersAdapter(bidEvents);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bidAdapter);


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
            id.setText(event.getCreatorID());
            id2.setText(event.getEventId());

        }


        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the bid amount from the EditText
                confirmAndPlaceBid();
            }
        });

        String eventId = event.getEventId();
        if (event != null) {

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
                                    Glide.with(eventBooking.this)
                                            .load(eventPoster)
                                            .into(eventPosterTv);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(eventBooking.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this, "No event found", Toast.LENGTH_SHORT).show();
        }

        // fetch bidders user name
        fetchUserName();
        // Fetch bidders from Firestore
        fetchBidders();
        // check user access level
        checkUserAccessLevel();

    }

    private void checkUserAccessLevel() {
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
                    } if (userType.equals("Musician")) {
                        biddersView.setVisibility(View.VISIBLE);
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

    private void fetchBidders() {

        fStore.collection("BidEvents")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            bookedEventModel bookedEvent = documentSnapshot.toObject(bookedEventModel.class);
                            bidEvents.add(bookedEvent);
                        }
                        bidAdapter.notifyDataSetChanged();
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
                    Toast.makeText(eventBooking.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(eventBooking.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmAndPlaceBid() {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        String bidAmount = bidAmountTv.getText().toString().trim();
        String eventId = id2.getText().toString().trim();
        String name = eventName.getText().toString().trim();
        String date = eventDate.getText().toString().trim();
        String time = eventTime.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String details = eventDetails.getText().toString().trim();
        String eventCategory = category.getText().toString().trim();
        String creatorId = id.getText().toString().trim();
        String bidder = biddersName.getText().toString().trim();

        // Validate the bid amount
        if (bidAmount.isEmpty()) {
            bidAmountTv.setError("Please enter a bid amount");
        }
        else {
            // Add the bid to Firestore
            bookedEventModel bookedEvent = new bookedEventModel(name, details, date, time, location, bidAmount, eventId, creatorId, "", eventCategory,"");

            fStore.collection("BidEvents")
                    .add(bookedEvent)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String o_name = organizerName.getText().toString();
                            documentReference.update("organizerName", o_name);
                            documentReference.update("biddersName", bidder);
                            documentReference.update("biddersId", userId);

                            documentReference.update("bidId", documentReference.getId());
                            Toast.makeText(eventBooking.this, "Bid placed successfully", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(eventBooking.this, bookedEvents.class));
                                    finish();
                                }
                            }, 2000);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(eventBooking.this, "Failed to place bid", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }

}