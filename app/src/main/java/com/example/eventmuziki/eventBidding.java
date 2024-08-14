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
import com.example.eventmuziki.Adapters.biddersAdapter;
import com.example.eventmuziki.Adapters.serviceNameAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceNameModel;
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

    TextView eventName, eventDate, eventTime, eventLocation, categoryNme, organizerName, eventDetails, amountTv, category, biddersName, categoryTv, viewAll;
    EditText bidAmountTv;
    Button placeBid;
    ImageView eventPosterTv, backArrow;
    LinearLayout biddersView, categoryLayout;
    FirebaseFirestore fStore;

    ArrayList<biddersEventModel> bidEvents, categoryService;
    RecyclerView recyclerView, requiredServicesRv;
    biddersAdapter bidAdapter, categoryAdapter;

    public String eventId, creatorId, userType;

    ArrayList<serviceNameModel> requiredServices;
    serviceNameAdapter nameAdapter;

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
        viewAll = findViewById(R.id.view_all);

        fStore = FirebaseFirestore.getInstance();

        categoryTv = findViewById(R.id.category_bidderTv);
        categoryNme = findViewById(R.id.category_Tv);
        recyclerView = findViewById(R.id.biddersRv);
        categoryLayout = findViewById(R.id.categoryLayout);
        requiredServicesRv = findViewById(R.id.serviceRequiredRv);

        bidEvents = new ArrayList<>();
        bidAdapter = new biddersAdapter(bidEvents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bidAdapter);

        requiredServices = new ArrayList<>();
        nameAdapter = new serviceNameAdapter(this,requiredServices);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        requiredServicesRv.setLayoutManager(layoutManager1);
        requiredServicesRv.setAdapter(nameAdapter);

        categoryService = new ArrayList<>();
        categoryAdapter = new biddersAdapter(categoryService);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(categoryAdapter);

        nameAdapter.setOnItemClickListener(new serviceNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(serviceNameModel service) {
                categoryTv.setText(service.getServiceName());
                if (service.getServiceName().equals("Decorations")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Car Rental")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Photographer")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Catering")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Sound")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Costumes")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Influencers")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else if (service.getServiceName().equals("Sponsors")) {
                    fetchCategoryBidders(eventId, service.getServiceName());
                } else {
                    fetchBidders(eventId);
                }


            }
        });



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
            category.setText(event.getCategory());
            eventDetails.setText(event.getEventDetails());
            amountTv.setText(event.getAmount());
            organizerName.setText(event.getOrganizerName());
            // Get the event ID
            eventId = event.getEventId();
            creatorId = event.getCreatorID();

        }

        fetchRequiredServices(Objects.requireNonNull(event).getEventId());

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

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBidders(eventId);
            }
        });

        // fetch bidders user name
        fetchUserName();
        // check user access level
        checkUserAccessLevel(categoryTv.getText().toString());

    }

    private void fetchRequiredServices(String eventId) {

        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .collection("EventServices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        requiredServices.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            // Extract service details
                            String serviceName = documentSnapshot.getString("service");
                            if (serviceName != null) {
                                int serviceIcon = getIconForService(serviceName);
                                serviceNameModel service = new serviceNameModel(serviceName, serviceIcon, null);
                                requiredServices.add(service);
                            }else {
                                Toast.makeText(eventBidding.this, "Services Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        nameAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(eventBidding.this, "No services found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(eventBidding.this, "Failed to fetch services", Toast.LENGTH_SHORT).show();
                });
    }

    private int getIconForService(String serviceName) {
        if (serviceName.equals("Car Rental")) {
            return R.drawable.car_icon;
        }
        if (serviceName.equals("Photographer")) {
            return R.drawable.camera_icon;
        }
        if (serviceName.equals("Catering")) {
            return R.drawable.fastfood_icon;
        }
        if (serviceName.equals("Costumes")) {
            return R.drawable.costume_icon;
        }
        if (serviceName.equals("Sound")) {
            return R.drawable.dj_icon;
        }
        if (serviceName.equals("Decorations")) {
            return R.drawable.deco_icon;
        }
        if (serviceName.equals("Influencers")) {
            return R.drawable.content_icon;
        }
        if (serviceName.equals("Sponsors")) {
            return R.drawable.sponsorship_icon;
        }
        return R.drawable.money_icon;

    }

    private void checkUserAccessLevel(String categories) {
        String userId = FirebaseAuth.getInstance().getUid();

        DocumentReference df = fStore.collection("Users").document(userId);

        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify User Access Level
                userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equalsIgnoreCase("Corporate")){
                        biddersView.setVisibility(View.GONE);
                        viewAll.setVisibility(View.VISIBLE);
                    } if (userType.equalsIgnoreCase("Musician")) {
                        categoryNme.setText(categories);
                        viewAll.setVisibility(View.GONE);

                        FirebaseFirestore.getInstance().collection("BidEvents")
                                .whereEqualTo("eventId", eventId)
                                .whereEqualTo("biddersId", FirebaseAuth.getInstance().getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                                // User has already placed a bid for this event
                                                biddersView.setVisibility(View.GONE);
                                            }
                                            else {
                                                // User has not placed a bid for this event
                                                biddersView.setVisibility(View.VISIBLE);
                                            }
                                        }else {
                                            Toast.makeText(eventBidding.this, "Failed to check bid", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

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

    private void fetchCategoryBidders(String eventId, String service) {
        fStore.collection("BidEvents")
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("userCategory", service)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryService.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        biddersEventModel bidder = documentSnapshot.toObject(biddersEventModel.class);
                        categoryService.add(bidder);
                    }
                    categoryAdapter.notifyDataSetChanged();

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
                            bidAdapter.notifyDataSetChanged();
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
        // String userCategory = categoryTv.getText().toString().trim();
        String bidder = biddersName.getText().toString().trim();

        // Validate the bid amount
        if (bidAmount.isEmpty()) {
            bidAmountTv.setError("Please enter a bid amount");
        }
        else {
            // Add the bid to Firestore
            biddersEventModel bookedEvent = new biddersEventModel(name, details, date, time, location, bidAmount, eventId, creatorId, "", userType,currentUserId, "", "");

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