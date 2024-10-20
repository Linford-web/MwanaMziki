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
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.Models.eventAdvertModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class viewBookedEvents extends AppCompatActivity {
    ImageView profileTv;
    ImageButton back, cancel;
    TextView eventName, eventLocation, eventDate, eventTime, organizerName, eventDetails, advertisementDate, advertisementTime;
    Button advertise, completeAdvertisement;
    EditText details, title, contact, duration;
    TextView cost1, reach1, description1, cost2, reach2, description2, cost3, reach3, description3, cost4, reach4, description4;
    ArrayList<bookedEventsModel> bookedEvents;
    musicianNameAdapter bookedAdapter;
    RecyclerView recyclerView;
    namesAdapter namesAdapter;
    LinearLayout bookedLyt, namesLyt, detailsLyt;
    CheckBox checkBoxBillboard, checkBoxMatatu, checkBoxScreens, checkBoxSocials;
    String eventId, eventPoster;
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
                                            .error(R.drawable.poster1)
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

        bookedEvents = new ArrayList<>();
        bookedAdapter = new musicianNameAdapter(bookedEvents);
        namesAdapter = new namesAdapter(bookedEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookedAdapter);

        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookedLyt.setVisibility(View.GONE);
                namesLyt.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
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
                completeAdvertisement.setVisibility(View.GONE);
                advertise.setVisibility(View.VISIBLE);
                detailsLyt.setVisibility(View.VISIBLE);
            }
        });

        completeAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAdvertisements();
            }
        });

        // check user access level
        checkUserAccessLevel();
        // Call the method to display date and time
        displayCurrentDateTime();

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

        fStore.collection("Advertisements").add(model)
                .addOnSuccessListener(documentReference -> {
                    documentId = documentReference.getId();
                    documentReference.update("advertId", documentId);
                    // Add Advertisement subCollection
                    addAdvertSubCollection(documentReference, documentId);
                    // Add Event Advertisement subCollection
                    AddEventSubCollection(eventId);

                    Log.d("Success", "Added Advertisement");
                }).addOnFailureListener(e -> {
                    Toast.makeText(viewBookedEvents.this, "Error Adding Advertisement", Toast.LENGTH_SHORT).show();
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
                                    eventAdvertModel advertisement = new eventAdvertModel(documentId,eventId, eventName.getText().toString(), eventLocation.getText().toString(), eventDate.getText().toString(), eventTime.getText().toString(),
                                            details.getText().toString(), organizerName.getText().toString(), Objects.requireNonNull(fAuth.getCurrentUser()).getUid(), eventPoster);
                                    advertisementEventRef.add(advertisement)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
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
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                advertise.setVisibility(View.GONE);
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

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookedEvents.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        bookedEvents.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
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