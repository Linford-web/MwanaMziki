package com.example.eventmuziki;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.example.eventmuziki.Adapters.serviceAdapters.cartAdapter;
import com.example.eventmuziki.Adapters.serviceNameAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class eventBidding extends AppCompatActivity {

    TextView eventName, eventDate, eventTime, eventLocation, categoryNme, organizerName, eventDetails, amountTv, category, biddersName, categoryTv, viewAll;
    EditText bidAmountTv;
    Button placeBid, deleteEventBtn;
    ImageView eventPosterTv, backArrow;
    LinearLayout biddersView, categoryLayout, myEmptyRv;
    FirebaseFirestore fStore;

    ArrayList<biddersEventModel> bidEvents, categoryService;
    RecyclerView recyclerView, requiredServicesRv, allRecyclerview;
    biddersAdapter bidAdapter, categoryAdapter;

    Dialog popupDialog;
    public String eventId, creatorId, userType, bidderType;

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
        allRecyclerview = findViewById(R.id.allRv);
        categoryLayout = findViewById(R.id.categoryLayout);
        requiredServicesRv = findViewById(R.id.serviceRequiredRv);
        myEmptyRv = findViewById(R.id.emptyRv);
        deleteEventBtn = findViewById(R.id.deleteEvent);

        bidEvents = new ArrayList<>();
        bidAdapter = new biddersAdapter(bidEvents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        allRecyclerview.setLayoutManager(layoutManager);
        allRecyclerview.setAdapter(bidAdapter);

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

                String selectedService = service.getServiceName();
                categoryTv.setText(selectedService);

                // List of services that should trigger the same actions
                List<String> validServices = Arrays.asList(
                        "Decorations", "Car Rental", "Photographer", "Catering",
                        "Sound", "Costumes", "Influencers", "Sponsors",
                        "MakeUp", "Music"
                );

                if (validServices.contains(selectedService)) {
                    fetchCategoryBidders(eventId, selectedService);
                    recyclerView.setVisibility(View.VISIBLE);
                    allRecyclerview.setVisibility(View.GONE);
                } else {
                    Log.d("TAG", "onItemClick: No service selected");
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

        FirebaseFirestore.getInstance().collection("Events")
                .whereEqualTo("creatorID", FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            deleteEventBtn.setVisibility(View.VISIBLE);
                        }else {
                            deleteEventBtn.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventBidding.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

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
                recyclerView.setVisibility(View.GONE);
                allRecyclerview.setVisibility(View.VISIBLE);
                categoryTv.setText("All");
                myEmptyRv.setVisibility(View.GONE);
            }
        });

        fetchBidders(eventId);
        // fetch bidders user name
        fetchUserName();
        // check user access level
        checkUserAccessLevel(categoryTv.getText().toString());

        checkUserServiceForEvent(eventId);



    }

    private void deleteEvent() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // Reference to the EventServices subcollection
        fStore.collection("Events")
                .document(eventId)
                .collection("EventServices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if the subcollection has documents
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Loop through each document and delete them
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String serviceId = document.getId();
                            fStore.collection("Events")
                                    .document(eventId)
                                    .collection("EventServices")
                                    .document(serviceId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Successfully deleted a service document
                                        Toast.makeText(eventBidding.this, "Event service deleted successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure for deleting a service document
                                        Toast.makeText(eventBidding.this, "Failed to delete event service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // No documents in the subcollection
                        Log.d("TAG", "No documents in the subcollection");
                    }

                    // After deleting all documents in EventServices, delete the main event document
                    fStore.collection("Events")
                            .document(eventId)
                            .delete()
                            .addOnSuccessListener(unused -> {
                                showPopupDialog();
                                new Handler().postDelayed(this::finish, 3000);
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure of deleting the event document
                                Toast.makeText(eventBidding.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle failure of retrieving the EventServices subcollection
                    Toast.makeText(eventBidding.this, "Failed to retrieve event services: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUserServiceForEvent(String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Step 1: Fetch the services provided by the current user
        fStore.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(userSnapshot -> {
                    if (userSnapshot.exists()) {
                        // Assuming the user has a 'services' field which is a list or array of services they provide
                        String userServices = userSnapshot.getString("category");

                        // Step 2: Fetch the event's services
                        fStore.collection("Events").document(eventId)
                                .collection("EventServices")
                                .whereEqualTo("service", userServices)
                                .get()
                                .addOnSuccessListener(serviceSnapshots -> {
                                    if (!serviceSnapshots.isEmpty()) {
                                        biddersView.setVisibility(View.VISIBLE);
                                    } else {
                                        biddersView.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("TAG", "Failed to fetch event services: " + e.getMessage());
                                });

                    } else {
                        Log.d("TAG", "User does not exist in Firestore");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("TAG", "Failed to fetch user services: " + e.getMessage());
                });
    }

    private void fetchRequiredServices(String eventId) {

        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .collection("EventServices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        myEmptyRv.setVisibility(View.GONE);
                        requiredServicesRv.setVisibility(View.VISIBLE);
                    } else {
                        myEmptyRv.setVisibility(View.VISIBLE);
                        requiredServicesRv.setVisibility(View.GONE);
                    }
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
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(eventBidding.this, "Failed to fetch services", Toast.LENGTH_SHORT).show();
                });
    }

    private int getIconForService(String serviceName) {
        if (serviceName.equals("Music")) {
            return R.drawable.music_icon;
        }
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
        if (serviceName.equals("MakeUp")) {
            return R.drawable.makeup_icon;
        }
        return R.drawable.service_icon;

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
                        allRecyclerview.setVisibility(View.VISIBLE);
                    } if (userType.equalsIgnoreCase("Musician")) {
                        categoryNme.setText(categories);
                        viewAll.setVisibility(View.GONE);
                        allRecyclerview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

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
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        myEmptyRv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        myEmptyRv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    categoryService.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                        biddersEventModel bidder = documentSnapshot.toObject(biddersEventModel.class);
                        categoryService.add(bidder);
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(eventBidding.this, "Failed to fetch category bidders", Toast.LENGTH_SHORT).show();
                });
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
                    bidderType = documentSnapshot.getString("userType");
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
        String bidder = biddersName.getText().toString().trim();

        // Validate the bid amount
        if (bidAmount.isEmpty()) {
            bidAmountTv.setError("Please enter a bid amount");
        }
        else {
            // Add the bid to Firestore
            biddersEventModel bookedEvent = new biddersEventModel(name, details, date, time, location, bidAmount, eventId, creatorId, "", bidderType, currentUserId, "", "");

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

                                            showPopupDialog();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(eventBidding.this, bidEvents.class));
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    if (popupDialog != null) popupDialog.dismiss();
                                                }
                                            }, 3000);

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

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(eventBidding.this);
            popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popupDialog.setCancelable(false);
            popupDialog.setContentView(R.layout.popup_layout);

            if (popupDialog.getWindow() != null) {
                popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        popupDialog.show();

        new Handler().postDelayed(() -> {
            if (popupDialog != null && popupDialog.isShowing() && !isFinishing() && !isDestroyed()) {
                popupDialog.dismiss();
            }
        }, 3000);
    }

}