package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.musicianNameAdapter;
import com.example.eventmuziki.Adapters.namesAdapter;
import com.example.eventmuziki.Models.advertisementModel;
import com.example.eventmuziki.Models.bookedEventsModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class viewBookedEvents extends AppCompatActivity {
    ImageView profileTv;
    ImageButton back, cancel;
    TextView eventName, eventLocation, eventDate, eventTime, organizerName;
    Button advertise, completeAdvertisement;
    EditText details;

    ArrayList<bookedEventsModel> bookedEvents;
    musicianNameAdapter bookedAdapter;
    RecyclerView recyclerView;
    namesAdapter namesAdapter;
    RecyclerView namesRv;
    LinearLayout bookedLyt, namesLyt;
    CheckBox checkBoxBillboard, checkBoxMatatu, checkBoxScreens, checkBoxSocials;
    String eventId, eventPoster;

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
        namesRv = findViewById(R.id.musicianNamesRv);
        bookedLyt = findViewById(R.id.bookedMusiciansLayout);
        namesLyt = findViewById(R.id.namesLayout);
        cancel = findViewById(R.id.close_icon);
        details = findViewById(R.id.detailsEditText);

        checkBoxBillboard = findViewById(R.id.check1);
        checkBoxMatatu = findViewById(R.id.check2);
        checkBoxScreens = findViewById(R.id.check3);
        checkBoxSocials = findViewById(R.id.check4);
        completeAdvertisement = findViewById(R.id.advertiseNow);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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

                                eventPoster = documentSnapshot.getString("eventPoster");

                                if (eventPoster != null && !eventPoster.isEmpty()) {
                                    Glide.with(viewBookedEvents.this)
                                            .load(eventPoster)
                                            .placeholder(R.drawable.cover)
                                            .error(R.drawable.poster1)
                                            .into(profileTv);
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

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        namesRv.setLayoutManager(layoutManager1);
        namesRv.setAdapter(namesAdapter);

        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookedLyt.setVisibility(View.GONE);
                namesLyt.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                completeAdvertisement.setVisibility(View.VISIBLE);
                advertise.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookedLyt.setVisibility(View.VISIBLE);
                namesLyt.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                completeAdvertisement.setVisibility(View.GONE);
                advertise.setVisibility(View.VISIBLE);
            }
        });

        completeAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Advertisements")
                        .whereEqualTo("eventId", eventId)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if(task.getResult() != null && !task.getResult().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "You have already booked this event", Toast.LENGTH_SHORT).show();
                                    }else {
                                        String selectedAdvertPlans = getSelectedAdvertPlans();
                                        if (selectedAdvertPlans.isEmpty()) {
                                            Toast.makeText(viewBookedEvents.this, "Please select at least one advertisement plan", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        // Create a new advertisement object
                                        advertisementModel advertisement = new advertisementModel("",eventId, eventName.getText().toString(), eventLocation.getText().toString(), eventDate.getText().toString(), eventTime.getText().toString(), details.getText().toString(), organizerName.getText().toString(), selectedAdvertPlans, FirebaseAuth.getInstance().getCurrentUser().getUid(), eventPoster);
                                        FirebaseFirestore.getInstance()
                                                .collection("Advertisements")
                                                .add(advertisement)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if (task.isSuccessful()) {
                                                            // Get the document ID
                                                            DocumentReference documentReference = task.getResult();
                                                            String documentId = documentReference.getId();
                                                            documentReference.update("advertId", documentId);

                                                            Intent intent = new Intent(viewBookedEvents.this, advertActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(viewBookedEvents.this, "Failed to create advertisement", Toast.LENGTH_SHORT).show();
                                                        Log.e("AdvertisementError", e.getMessage());
                                                    }
                                                });
                                    }
                                }else {
                                    Toast.makeText(viewBookedEvents.this, "Failed to fetch eventsId", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(viewBookedEvents.this, "Error fetching eventsId", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        // check user access level
        checkUserAccessLevel();

    }

    private String getSelectedAdvertPlans() {
        List<String> selectedPlans = new ArrayList<>();
        if (checkBoxBillboard.isChecked()) {
            selectedPlans.add("Billboard");
        }
        if (checkBoxMatatu.isChecked()) {
            selectedPlans.add("Matatu");
        }
        if (checkBoxScreens.isChecked()) {
            selectedPlans.add("Screens");
        }
        if (checkBoxSocials.isChecked()) {
            selectedPlans.add("Socials");
        }
        return selectedPlans.toString();
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
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        bookedEventsModel event = documentSnapshot.toObject(bookedEventsModel.class);
                        bookedEvents.add(event);
                    }
                    bookedAdapter.notifyDataSetChanged();
                });

    }

}