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
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Adapters.namesAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cartAdapter;
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.Models.eventAdvertModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class viewBookedEvents extends AppCompatActivity {
    ImageView profileTv, addServices;
    ImageButton back, cancel, confirmBtn;
    TextView eventName, eventLocation, eventDate, eventTime, organizerName, eventDetails, advertisementDate, advertisementTime;
    Button advertise, completeAdvertisement, delete;
    EditText details, title, contact, duration;
    TextView cost1, reach1, description1, cost2, reach2, description2, cost3, reach3, description3, cost4, reach4, description4;
    ArrayList<ServicesDetails.bookedBiddersModel> bookedBidders;
    musicianNameAdapter bookedAdapter;
    ArrayList<ServicesDetails.cartModel> bookedList;
    cartAdapter adapter;

    RecyclerView recyclerView, bookedRv;
    LinearLayout bookedLyt, namesLyt, detailsLyt, emptyRv, bookedServices;
    CheckBox checkBoxBillboard, checkBoxMatatu, checkBoxScreens, checkBoxSocials;
    String eventId, eventPoster, bookedId;
    boolean valid = true;
    String documentId;
    Dialog popupDialog;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_booked_events);

        profileTv = findViewById(R.id.poster);
        back = findViewById(R.id.back_arrow);
        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        organizerName = findViewById(R.id.organizerNameTv);
        recyclerView = findViewById(R.id.bookedMusiciansRv);
        advertise = findViewById(R.id.advertiseBtn);
        bookedLyt = findViewById(R.id.bookedMusiciansLayout);
        namesLyt = findViewById(R.id.namesLayout);
        cancel = findViewById(R.id.close_icon);

        checkBoxBillboard = findViewById(R.id.checkBox1);
        checkBoxMatatu = findViewById(R.id.checkBox2);
        checkBoxScreens = findViewById(R.id.checkBox3);
        checkBoxSocials = findViewById(R.id.checkBox4);
        completeAdvertisement = findViewById(R.id.advertiseNow);
        eventDetails = findViewById(R.id.eventDetail);
        detailsLyt = findViewById(R.id.event_details);

        title = findViewById(R.id.title);
        contact = findViewById(R.id.contact);
        duration = findViewById(R.id.duration);
        details = findViewById(R.id.details);
        advertisementDate = findViewById(R.id.advertisementDate);
        advertisementTime = findViewById(R.id.advertisementTime);

        cost1 = findViewById(R.id.cost1);
        reach1 = findViewById(R.id.reach1);
        description1 = findViewById(R.id.content1);
        cost2 = findViewById(R.id.cost2);
        reach2 = findViewById(R.id.reach2);
        description2 = findViewById(R.id.content2);
        cost3 = findViewById(R.id.cost3);
        reach3 = findViewById(R.id.reach3);
        description3 = findViewById(R.id.content3);
        cost4 = findViewById(R.id.cost4);
        reach4 = findViewById(R.id.reach4);
        description4 = findViewById(R.id.content4);
        delete = findViewById(R.id.deleteEvent);
        confirmBtn = findViewById(R.id.confirmEvent);
        bookedRv = findViewById(R.id.bookedRv);
        emptyRv = findViewById(R.id.displayEmptyRv);
        bookedServices = findViewById(R.id.bookedServicesLayout);
        addServices = findViewById(R.id.addServices);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            finish();
        });

        bookedEventsModel bookedEvent = getIntent().getParcelableExtra("bookedEventsModel");
        if (bookedEvent !=null){
            eventName.setText(bookedEvent.getEventName());
            eventLocation.setText(bookedEvent.getLocation());
            eventDate.setText(bookedEvent.getDate());
            eventTime.setText(bookedEvent.getTime());
            organizerName.setText(bookedEvent.getOrganizerName());

            eventId = bookedEvent.getEventId();
            bookedId = bookedEvent.getBookedId();

            // fetch booked Users
            fetchBookedUsers(eventId);

            FirebaseFirestore.getInstance()
                    .collection("Events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                String details = documentSnapshot.getString("eventDetails");
                                eventDetails.setText(details);
                                eventPoster = documentSnapshot.getString("eventPoster");

                                if (eventPoster != null && !eventPoster.isEmpty()) {
                                    Glide.with(viewBookedEvents.this)
                                            .load(eventPoster)
                                            .placeholder(R.drawable.cover)
                                            .error(R.drawable.cover)
                                            .into(profileTv);
                                }else {
                                    profileTv.setImageResource(R.drawable.cover);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(viewBookedEvents.this, "Failed To get Event Poster", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        bookedList = new ArrayList<>();
        adapter = new cartAdapter(bookedList, this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookedRv.setLayoutManager(layoutManager3);
        bookedRv.setAdapter(adapter);

        bookedBidders = new ArrayList<>();
        bookedAdapter = new musicianNameAdapter(bookedBidders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookedAdapter);

        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewBookedEvents.this, categoryOptions.class);
                startActivity(intent);
            }
        });

        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookedLyt.setVisibility(View.GONE);
                namesLyt.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.GONE);
                completeAdvertisement.setVisibility(View.VISIBLE);
                advertise.setVisibility(View.GONE);
                detailsLyt.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookedLyt.setVisibility(View.VISIBLE);
                namesLyt.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
                completeAdvertisement.setVisibility(View.GONE);
                advertise.setVisibility(View.VISIBLE);
                detailsLyt.setVisibility(View.VISIBLE);
                eventDetails.setText("");
                title.setText("");
                contact.setText("");
                duration.setText("");
                details.setText("");
                checkBoxBillboard.setChecked(false);
                checkBoxMatatu.setChecked(false);
                checkBoxScreens.setChecked(false);
                checkBoxSocials.setChecked(false);
            }
        });

        completeAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAdvertisements();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("BookedEvents")
                        .document(bookedId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                showPopupDialog();
                                new Handler().postDelayed(viewBookedEvents.this::finish, 3000);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(viewBookedEvents.this, "Failed To Delete Advertisement", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewBookedEvents.this, managePayment.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventPoster", eventPoster);
                intent.putExtra("eventName", eventName.getText().toString());
                intent.putExtra("organizerName", bookedEvent.getOrganizerName());
                intent.putExtra("bookedId", bookedId);
                intent.putExtra("creatorId", bookedEvent.getCreatorID());
                startActivity(intent);
            }
        });
        // check user access level
        checkUserAccessLevel();
        // Call the method to display date and time
        displayCurrentDateTime();
        // Check Event Date
        checkEventDate(eventId);
        // fetch booked services
        fetchBookedServices(eventId);

    }

    private void checkEventDate(String eventId) {
        FirebaseFirestore.getInstance().collection("Events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the event date from the document
                        String eventDateStr = documentSnapshot.getString("date");

                        // Parse the event date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd : MM : yyyy", Locale.getDefault());
                        try {
                            Date eventDate = dateFormat.parse(eventDateStr);
                            Date currentDate = new Date(); // Get the current date

                            // Check if the event date is before the current date
                            if (eventDate != null && eventDate.before(currentDate)) {
                                //Toast.makeText(this, "Event passed", Toast.LENGTH_SHORT).show();
                                //confirmBtn.setVisibility(View.VISIBLE);
                            } else {
                                //Toast.makeText(this, "Event is upcoming", Toast.LENGTH_SHORT).show();
                                //confirmBtn.setVisibility(View.GONE);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Invalid date format in event data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to retrieve event date", Toast.LENGTH_SHORT).show());
    }

    private void addAdvertisements() {

        checkField(title);
        checkField(contact);
        checkField(details);
        checkField(duration);

        String titleText = title.getText().toString();
        String contactText = contact.getText().toString();
        String detailsText = details.getText().toString();
        String durationText = duration.getText().toString();
        String time = advertisementTime.getText().toString();
        String date = advertisementDate.getText().toString();

        AdvertisementDetails.advertModel model = new AdvertisementDetails.advertModel(titleText, contactText, date, time, detailsText, durationText, eventPoster, "", Objects.requireNonNull(fAuth.getCurrentUser()).getUid());

        // Reference to the Advertisements collection
        fStore.collection("Advertisements")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            // Iterate through each document in the Advertisements collection
                            for (QueryDocumentSnapshot advertisementDoc : queryDocumentSnapshots) {
                                // Query the Events subcollection for the eventId
                                advertisementDoc.getReference()
                                        .collection("EventAdvertisements")
                                        .whereEqualTo("eventId", eventId)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot eventSnapshots) {
                                                if (eventSnapshots != null && !eventSnapshots.isEmpty()) {
                                                    // Event found, handle as needed
                                                    Toast.makeText(viewBookedEvents.this, "Advertisement already exists for this event", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // If no matching advertisement exists, proceed to add the advertisement
                                                    fStore.collection("Advertisements").add(model)
                                                            .addOnSuccessListener(documentReference -> {
                                                                // Get the new advertisement document ID
                                                                documentId = documentReference.getId();
                                                                // Update the advertId field with the documentId
                                                                documentReference.update("advertId", documentId);

                                                                // Add Advertisement subcollection (if necessary)
                                                                addAdvertSubCollection(documentReference, documentId);

                                                                // Add Event Advertisement subcollection
                                                                AddEventSubCollection(eventId);

                                                                Log.d("Success", "Added Advertisement successfully");
                                                                Toast.makeText(viewBookedEvents.this, "Advertisement Added Successfully", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Handle failure for adding advertisement
                                                                Toast.makeText(viewBookedEvents.this, "Error Adding Advertisement final", Toast.LENGTH_SHORT).show();
                                                                Log.e("Error", "Error Adding Advertisement: " + e.getMessage());
                                                            });
                                                }
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure querying the Events subcollection
                                            Toast.makeText(viewBookedEvents.this, "Error Fetching Event Advertisements", Toast.LENGTH_SHORT).show();
                                            Log.e("Error", "Error Fetching Event Advertisements: " + e.getMessage());
                                        });
                            }
                        } else {
                            // No documents in Advertisements collection
                            Toast.makeText(viewBookedEvents.this, "No Advertisements found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure fetching Advertisements
                    Toast.makeText(viewBookedEvents.this, "Error Fetching Advertisements", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Error Fetching Advertisements: " + e.getMessage());
                });
    }

    private void AddEventSubCollection(String eventId) {
        // Reference to the Advertisement collection
        CollectionReference advertisementRef = fStore.collection("Advertisements");

        // Query the Advertisement collection for all documents
        advertisementRef.get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                // Loop through each document in the Advertisement collection
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    // Get the ID of the current document in the Advertisement collection
                    String advertisementId = documentSnapshot.getId();

                    // Reference to the AdvertisementEvent subcollection of the current document
                    CollectionReference advertisementEventRef = advertisementRef
                            .document(advertisementId)
                            .collection("EventAdvertisements");

                    // Query the subcollection for documents that match the passed eventId
                    advertisementEventRef
                            .whereEqualTo("eventId", eventId)
                            .get()
                            .addOnSuccessListener(subQuerySnapshot -> {
                                if (!subQuerySnapshot.isEmpty()) {
                                    // Matching document found
                                    Toast.makeText(this, "Event already exists in the Advertisement Event.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Create a new advertisement object
                                    eventAdvertModel advertisement = new eventAdvertModel(documentId, eventId, eventName.getText().toString(), eventLocation.getText().toString(), eventDate.getText().toString(), eventTime.getText().toString(),
                                            details.getText().toString(), organizerName.getText().toString(), Objects.requireNonNull(fAuth.getCurrentUser()).getUid(), eventPoster);
                                    advertisementEventRef.add(advertisement)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    String documentId = documentReference.getId();
                                                    documentReference.update("eventId", documentId);
                                                    showPopupDialog();
                                                    new Handler().postDelayed(() -> {
                                                        Intent intent = new Intent(viewBookedEvents.this, advertActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }, 3000);

                                                }
                                            }).addOnFailureListener(e -> Toast.makeText(viewBookedEvents.this, "Failed to add event advertisement", Toast.LENGTH_SHORT).show());

                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to check AdvertisementEvent: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(this, "No advertisements found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to retrieve advertisements: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
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
                                                emptyRv.setVisibility(View.GONE);
                                                bookedRv.setVisibility(View.VISIBLE);
                                            } else {
                                                emptyRv.setVisibility(View.VISIBLE);
                                                bookedRv.setVisibility(View.GONE);
                                            }
                                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                ServicesDetails.cartModel bookedService = document.toObject(ServicesDetails.cartModel.class);
                                                bookedList.add(bookedService);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.d("TAG", "Error fetching BookedServices: " + e.getMessage()));
                        } else {
                            Log.d("TAG", "Event not found");
                        }
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.getMessage()));
    }

    private void addAdvertSubCollection(DocumentReference documentReference, String documentId) {

        CollectionReference collectionReference = documentReference.collection("AdvertPlatforms");

        String costTxt = cost1.getText().toString();
        String reachText = reach1.getText().toString();
        String detailsText = description1.getText().toString();
        if (checkBoxBillboard.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxt, reachText, detailsText, "Billboard", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Billboard Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxtm = cost2.getText().toString();
        String reachTextm = reach2.getText().toString();
        String detailsTextm = description2.getText().toString();
        if (checkBoxMatatu.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxtm, reachTextm, detailsTextm, "Matatu", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Matatu Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxtv = cost3.getText().toString();
        String reachTextv = reach3.getText().toString();
        String detailsTextv = description3.getText().toString();
        if (checkBoxScreens.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxtv, reachTextv, detailsTextv, "Video", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Video Stands Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxto = cost4.getText().toString();
        String reachTexto = reach4.getText().toString();
        String detailsTexto = description4.getText().toString();
        if (checkBoxSocials.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxto, reachTexto, detailsTexto, "Online", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Online Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }


    }

    private void checkUserAccessLevel() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        //get user type
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if ("Corporate".equals(userType)) {
                                advertise.setVisibility(View.VISIBLE);
                                delete.setVisibility(View.VISIBLE);
                                bookedServices.setVisibility(View.VISIBLE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                advertise.setVisibility(View.GONE);
                                delete.setVisibility(View.GONE);
                                bookedServices.setVisibility(View.GONE);
                            } else {
                                // Handle other user types if needed
                                Log.d("TAG", "User is neither Corporate nor Musician");
                            }

                        }
                    } else {
                        Log.e("TaskListAdapter", "Error getting document", task.getException());
                    }
                });

    }

    private void fetchBookedUsers(String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // Step 1: Fetch the BookedEvents document by eventId
        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Step 2: Iterate through the documents in BookedEvents that match the eventId
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String bookedEventId = documentSnapshot.getId();

                            // Step 3: Access the BookedBidders subcollection for each BookedEvent
                            fStore.collection("BookedEvents")
                                    .document(bookedEventId)
                                    .collection("BookedBidders")
                                    .get()
                                    .addOnSuccessListener(bidderSnapshots -> {
                                        if (!bidderSnapshots.isEmpty()) {
                                            bookedBidders.clear();
                                            for (DocumentSnapshot bidderSnapshot : bidderSnapshots) {
                                                ServicesDetails.bookedBiddersModel model = bidderSnapshot.toObject(ServicesDetails.bookedBiddersModel.class);
                                                bookedBidders.add(model);
                                            }
                                            bookedAdapter.notifyDataSetChanged();
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

    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

    }

    private void displayCurrentDateTime() {
        // Get the current date and time using Calendar
        Calendar calendar = Calendar.getInstance();

        // Define date and time formats
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // Get formatted date and time as strings
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Set the date and time to the TextViews
        advertisementDate.setText(currentDate);
        advertisementTime.setText(currentTime);
    }

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(viewBookedEvents.this);
            popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popupDialog.setCancelable(false);
            popupDialog.setContentView(R.layout.popup_layout2);

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