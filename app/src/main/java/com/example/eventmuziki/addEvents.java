package com.example.eventmuziki;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceModels.serviceDetailModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class addEvents extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton backArrow, cancel_icon;
    Button addEvent;
    ImageView imageView,locationBtn;
    EditText inputTaskName, eventDetails, otherCategory;
    TextView datePicker, timePickerFrom, timePickerTo, organizerName;
    EditText amountTxt, location;
    FirebaseFirestore fStore;
    String startTime, endTime;
    Spinner spinnerCategory, carSpinner, soundSpinner, cateringSpinner;
    FirebaseStorage fStorage;

    Uri imageUri = null;
    String eventId;

    GoogleMap mMap;
    LatLng selectedLocation;
    ScrollView scrollView;
    RelativeLayout searchMap, locationMap;
    ImageButton editBtn, addPosterBtn, deleteBtn;

    LinearLayout carRental, photography, catering, costumes, paSystem,decorations, contentCreators, sponsors,
            carRentalDetails, photographyDetails, cateringDetails, costumesDetails, paSystemDetails, decoDetails, contentDetails, sponsorsDetails;

    TextView carTxt, photographyTxt, cateringTxt, costumesTxt, paSystemTxt, viewAllServices, decorTxt, contentTxt, sponsorsTxt;
    ImageView carIcon, photographyIcon, cateringIcon, costumesIcon, paSystemIcon, decorIcon, contentIcon, sponsorsIcon;

    EditText pickUpLocation, dropOffLocation, pickUpDate, dropOffDate,
            event_location, event_date, duration, price, costumeType, costumeSize, costumeQuantity,
            cateringLocation, cateringDate, guestNumber, cuisineType,
            djLocation, djDate, djDuration,
            decoLocation, decoDate, decoDuration, decoPrice,
            creatorName, creatorSocials, contentDuration, contentPrice,
            sponsorEventCategory, sponsorsLocation, currentSponsor, sponsorsPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_events);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        InitializeViews();

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        backArrow.setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(addEvents.this, eventsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        viewAllServices.setOnClickListener(v -> {
            startActivity(new Intent(addEvents.this, categoryOptions.class));
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.GONE);
                // titleTxt.setText("Select Location");
                searchMap.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                locationMap.setVisibility(View.VISIBLE);

                if (selectedLocation != null) {
                    String locations = getLocationName(selectedLocation);
                    location.setText(locations);
                } else {
                    Toast.makeText(addEvents.this, "Please select a location", Toast.LENGTH_SHORT).show();
                }
            }

        });
        /*
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                titleTxt.setText("Add Events");
                searchMap.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                locationMap.setVisibility(View.GONE);
            }
        });
         */

    // Initialize ArrayAdapter and set it to the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle selection here
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                if (selectedCategory.equalsIgnoreCase("Other")){
                    otherCategory.setVisibility(View.VISIBLE);
                }else{
                    otherCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
                Toast.makeText(addEvents.this, "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Car_Rental, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carSpinner.setAdapter(adapter1);

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                Log.d("Selected Category", selectedCategory);
                carRental.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
                Toast.makeText(addEvents.this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sound_services, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundSpinner.setAdapter(adapter2);

        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = adapterView.getItemAtPosition(i).toString();
                // Do something with the selected category
                Log.d("Selected Category", selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // set text to empty if nothing is selected
                Log.d("Selected Category", "Nothing selected");
            }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.catering_type, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cateringSpinner.setAdapter(adapter3);

        cateringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = adapterView.getItemAtPosition(i).toString();
                // Do something with the selected category
                Log.d("Selected Category", selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // set text to empty if nothing is selected
                Log.d("Selected Category", "Nothing selected");
            }
        });

        timePickerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialogFrom();
            }
        });
        timePickerTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialogTo();
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                // Get the event details from the EditText fields
                String eventName = inputTaskName.getText().toString();
                String eventDetail = eventDetails.getText().toString();
                String date = datePicker.getText().toString();
                String time = timePickerFrom.getText().toString()+" - "+timePickerTo.getText().toString();
                String locations = location.getText().toString();
                String amount = amountTxt.getText().toString();
                String category = "";
                String organizer = organizerName.getText().toString();

                // check if the spinner has option Other and set the category accordingly
                if (spinnerCategory.getSelectedItem().toString().equalsIgnoreCase("Other")){
                    category = otherCategory.getText().toString();
                } else {
                    category = spinnerCategory.getSelectedItem().toString();
                }

                if (eventName.isEmpty() || eventDetail.isEmpty() || date.isEmpty() || amount.isEmpty()) {
                        // Show an error message if any of the fields are empty
                    Toast.makeText(addEvents.this, "Please fill in required fields", Toast.LENGTH_SHORT).show();

                } else {

                    // Add the event to Firestore
                    eventModel event = new eventModel("",eventName, eventDetail, date, time, locations, amount, category, "" , organizer);
                    // Add the event to Firestore
                    fStore.collection("Events")
                            .add(event)
                            .addOnSuccessListener(documentReference -> {

                                String userId = FirebaseAuth.getInstance().getUid();
                                String eventId = documentReference.getId();

                                documentReference.update("eventId", eventId);
                                documentReference.update("creatorID", userId);

                                uploadEventPoster(documentReference);
                                Intent intent = new Intent(addEvents.this, eventsActivity.class);
                                startActivity(intent);
                                finish();
                                // Add subcollections for other services if details are provided
                                addSubCollections(documentReference);

                            }).addOnFailureListener(e -> {
                                Toast.makeText(addEvents.this, "Failed to add event", Toast.LENGTH_SHORT).show();

                            });
                }

            }

        });
        addPosterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(addEvents.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click here
                deletePoster();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPosterBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
                cancel_icon.setVisibility(View.VISIBLE);
            }
        });
        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPosterBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.VISIBLE);
                cancel_icon.setVisibility(View.GONE);
            }
        });

        // fetch and display organizer name
        fetchOrganizerName();
        // category clicks
        setupCategoryClicks();

    }

    private void deletePoster() {
        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to 'profile_pictures/<FILENAME>.jpg'
        final StorageReference profileRef = storageRef.child("event_posters/" + FirebaseAuth.getInstance().getUid() + ".jpg");
        // Delete the file from Firebase Storage
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Remove the profile picture URL from FireStore
                FirebaseFirestore.getInstance().collection("Events")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update("eventPoster", null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Set default image in ImageView
                                imageView.setImageResource(R.drawable.profile_image); // Replace with your default image resource
                                Toast.makeText(addEvents.this, "Profile photo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addEvents.this, "Failed to remove profile picture URL", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addEvents.this, "Failed to delete profile photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitializeViews() {

        imageView = findViewById(R.id.eventPoster);
        inputTaskName = findViewById(R.id.inputTaskName);
        eventDetails = findViewById(R.id.eventDetails);
        datePicker = findViewById(R.id.datePicker);
        timePickerFrom = findViewById(R.id.timePickerFrom);
        timePickerTo = findViewById(R.id.timePickerTo);
        locationBtn = findViewById(R.id.LocationBtn);
        amountTxt = findViewById(R.id.amountTxt);
        addEvent = findViewById(R.id.add_event);
        organizerName = findViewById(R.id.organizerNameTv);
        spinnerCategory = findViewById(R.id.spinner_category);
        soundSpinner = findViewById(R.id.sound_Services);
        scrollView = findViewById(R.id.scroll_view);
        locationMap = findViewById(R.id.location_map);
        location = findViewById(R.id.locationTxt);
        otherCategory = findViewById(R.id.other_category);
        carSpinner = findViewById(R.id.car_spinner);
        viewAllServices = findViewById(R.id.viewAllServices);
        decorations = findViewById(R.id.decorations);
        contentCreators = findViewById(R.id.contentCreator);
        sponsors = findViewById(R.id.sponsorship);

        carRental = findViewById(R.id.carRental);
        photography = findViewById(R.id.photography);
        catering = findViewById(R.id.catering);
        costumes = findViewById(R.id.costumes);
        paSystem = findViewById(R.id.paSystem);
        carRentalDetails = findViewById(R.id.carRentalDetails);
        photographyDetails = findViewById(R.id.photographyDetails);
        cateringDetails = findViewById(R.id.cateringDetails);
        costumesDetails = findViewById(R.id.costumesDetails);
        paSystemDetails = findViewById(R.id.paSystemDetails);
        decoDetails = findViewById(R.id.decorationDetails);
        contentDetails = findViewById(R.id.contentCreatorsDetails);
        sponsorsDetails = findViewById(R.id.sponsorshipDetails);
        cateringSpinner = findViewById(R.id.cateringServices);

        carTxt = findViewById(R.id.carTxt);
        photographyTxt = findViewById(R.id.photographyTxt);
        cateringTxt = findViewById(R.id.cateringTxt);
        costumesTxt = findViewById(R.id.costumeTxt);
        paSystemTxt = findViewById(R.id.paSystemTxt);
        carIcon = findViewById(R.id.carIcon);
        photographyIcon = findViewById(R.id.photographyIcon);
        cateringIcon = findViewById(R.id.cateringIcon);
        costumesIcon = findViewById(R.id.costumeIcon);
        paSystemIcon = findViewById(R.id.paSystemIcon);
        decorTxt = findViewById(R.id.decoTxt);
        contentTxt = findViewById(R.id.contentTxt);
        sponsorsTxt = findViewById(R.id.sponsorshipTxt);
        contentIcon = findViewById(R.id.contentIcon);
        sponsorsIcon = findViewById(R.id.sponsorshipIcon);
        decorIcon = findViewById(R.id.decoIcon);
        editBtn = findViewById(R.id.edit_poster);
        addPosterBtn = findViewById(R.id.addPosterBtn);
        deleteBtn = findViewById(R.id.delete_poster);
        cancel_icon = findViewById(R.id.cancel_edit);

        pickUpLocation = findViewById(R.id.pickUpLocation);
        dropOffLocation = findViewById(R.id.dropOffLocation);
        pickUpDate = findViewById(R.id.pickUpDate);
        dropOffDate = findViewById(R.id.dropOffDate);

        event_location = findViewById(R.id.photography_location);
        event_date = findViewById(R.id.photography_date);
        duration = findViewById(R.id.photography_duration);
        price = findViewById(R.id.price);

        cateringLocation = findViewById(R.id.catering_location);
        cateringDate = findViewById(R.id.catering_date);
        guestNumber = findViewById(R.id.number_of_guests);
        cuisineType = findViewById(R.id.cuisine_type);

        costumeType = findViewById(R.id.costume_type);
        costumeSize = findViewById(R.id.size);
        costumeQuantity = findViewById(R.id.quantity);

        djLocation = findViewById(R.id.dj_location);
        djDate = findViewById(R.id.dj_date);
        djDuration = findViewById(R.id.dj_duration);

        decoLocation = findViewById(R.id.deco_location);
        decoDate = findViewById(R.id.deco_date);
        decoDuration = findViewById(R.id.deco_duration);
        decoPrice = findViewById(R.id.deco_price);

        creatorName = findViewById(R.id.creatorName);
        creatorSocials = findViewById(R.id.creatorSocials);
        contentDuration = findViewById(R.id.creator_duration);
        contentPrice = findViewById(R.id.creator_price);

        sponsorEventCategory = findViewById(R.id.sponsor_category);
        currentSponsor = findViewById(R.id.sponsor_current);
        sponsorsLocation = findViewById(R.id.sponsor_location);
        sponsorsPrice = findViewById(R.id.sponsor_amount);

    }

    private void setupCategoryClicks() {
        carRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set text and icon color to white
                carTxt.setTextColor(getResources().getColor(R.color.orange));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.orange));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);

            }
        });
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.orange));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.orange));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
            }
        });
        catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.orange));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.orange));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
            }
        });
        costumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.orange));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.orange));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
            }
        });
        paSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.orange));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                paSystemIcon.setColorFilter(getResources().getColor(R.color.orange));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
            }
        });
        decorations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.orange));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.orange));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.VISIBLE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);

            }
        });
        contentCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.orange));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.orange));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.VISIBLE);
                sponsorsDetails.setVisibility(View.GONE);
            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.orange));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.orange));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.VISIBLE);

            }
        });
    }

    private void addSubCollections(DocumentReference documentReference) {
        CollectionReference servicesCollection = documentReference.collection("EventServices");

        // Add Car Rental subcollection
        String pickUpLocationText = pickUpLocation.getText().toString();
        String dropOffLocationText = dropOffLocation.getText().toString();
        String pickUpDateText = pickUpDate.getText().toString();
        String dropOffDateText = dropOffDate.getText().toString();
        String carType = carSpinner.getSelectedItem().toString();
        if (!pickUpLocationText.isEmpty() && !dropOffLocationText.isEmpty() && !pickUpDateText.isEmpty() && !dropOffDateText.isEmpty()) {
            serviceDetailModel.carHire cars = new serviceDetailModel.carHire(pickUpLocationText, dropOffLocationText, pickUpDateText, dropOffDateText, carType,"Car Rental");
            servicesCollection.add(cars).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Car Rental details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add car rental details");
            });
        }

        // Add Photography subcollection
        String priceText = price.getText().toString();
        String eventLocationText = event_location.getText().toString();
        String eventDateText = event_date.getText().toString();
        String durationText = duration.getText().toString();
        if (!priceText.isEmpty() && !eventLocationText.isEmpty() && !eventDateText.isEmpty() && !durationText.isEmpty()) {
            serviceDetailModel.hirePhotographer photographer = new serviceDetailModel.hirePhotographer(priceText, eventLocationText, eventDateText, durationText, "Photographer");
            servicesCollection.add(photographer).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Photography details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add photography details");
            });
        }

        // Add Catering subcollection
        String cateringLocationText = cateringLocation.getText().toString();
        String cateringDateText = cateringDate.getText().toString();
        String guestNumberText = guestNumber.getText().toString();
        String cuisineTypeText = cuisineType.getText().toString();
        String cateringType = cateringSpinner.getSelectedItem().toString();
        if (!cateringLocationText.isEmpty() && !cateringDateText.isEmpty() && !guestNumberText.isEmpty()) {

            serviceDetailModel.hireCatering catering = new serviceDetailModel.hireCatering(cateringLocationText, cateringDateText, guestNumberText, cuisineTypeText, cateringType,"Catering");
            servicesCollection.add(catering).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Catering details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add catering details");
            });
        }

        // Add Costumes subcollection
        String costumeTypeText = costumeType.getText().toString();
        String costumeSizeText = costumeSize.getText().toString();
        String costumeQuantityText = costumeQuantity.getText().toString();
        if (!costumeTypeText.isEmpty() && !costumeSizeText.isEmpty() && !costumeQuantityText.isEmpty()) {

            serviceDetailModel.hireCostumes hireCostumes = new serviceDetailModel.hireCostumes(costumeTypeText, costumeSizeText, costumeQuantityText, "Costumes");
            servicesCollection.add(hireCostumes).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Costumes details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add costumes details");
            });
        }

        // Add PA System subcollection
        String djLocationText = djLocation.getText().toString();
        String djDateText = djDate.getText().toString();
        String djDurationText = djDuration.getText().toString();
        String soundSpin = soundSpinner.getSelectedItem().toString();
        if (!djLocationText.isEmpty() && !djDateText.isEmpty() && !djDurationText.isEmpty()) {

            serviceDetailModel.hirePA hirePA = new serviceDetailModel.hirePA(djLocationText, djDateText, djDurationText, soundSpin, "Sound");
            servicesCollection.add(hirePA).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "PA system details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add PA system details");
            });
        }

        // Add Decorations subcollection
        String decorationLocationText = decoLocation.getText().toString();
        String decorationDateText = decoDate.getText().toString();
        String decorationDurationText = decoDuration.getText().toString();
        String decorationPriceText = decoPrice.getText().toString();
        if (!decorationLocationText.isEmpty() && !decorationDateText.isEmpty() && !decorationDurationText.isEmpty() && !decorationPriceText.isEmpty()) {
            serviceDetailModel.hireDeco hireDecorations = new serviceDetailModel.hireDeco(decorationLocationText, decorationDateText, decorationDurationText, decorationPriceText, "Decorations");
            servicesCollection.add(hireDecorations).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Decorations details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add decorations details");
            });

        }

        // Add Content Creators subcollection
        String creatorNameText = creatorName.getText().toString();
        String creatorSocialsText = creatorSocials.getText().toString();
        String contentDurationText = contentDuration.getText().toString();
        String contentPriceText = contentPrice.getText().toString();
        if (!creatorNameText.isEmpty() && !creatorSocialsText.isEmpty() && !contentDurationText.isEmpty() && !contentPriceText.isEmpty()) {
            serviceDetailModel.hireContent hireContent = new serviceDetailModel.hireContent(creatorNameText, creatorSocialsText, contentDurationText, contentPriceText, "Influencers");
            servicesCollection.add(hireContent).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Content Creators details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add content creators details");
            });
        }

        // Add Sponsors subcollection
        String sponsorsEventCategory = sponsorEventCategory.getText().toString();
        String currentSponsorText = currentSponsor.getText().toString();
        String sponsorLocationText = sponsorsLocation.getText().toString();
        String sponsorPriceText = sponsorsPrice.getText().toString();
        if (!sponsorsEventCategory.isEmpty() && !currentSponsorText.isEmpty() && !sponsorLocationText.isEmpty() && !sponsorPriceText.isEmpty()) {
            serviceDetailModel.hireSponsors hireSponsors = new serviceDetailModel.hireSponsors(sponsorsEventCategory, currentSponsorText, sponsorLocationText, sponsorPriceText, "Sponsors");
            servicesCollection.add(hireSponsors).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Sponsors details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add sponsors details");
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadEventPoster(DocumentReference documentReference) {
        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference posterRef = storageRef.child("event_posters/" + eventId + ".jpg");

        posterRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                eventId = documentReference.getId();
                                DocumentReference documentReference = fStore.collection("Events").document(eventId);
                                String imageUrl = uri.toString();
                                documentReference.update("eventPoster", imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("EventPoster", "Event poster updated successfully");
                                                Glide.with(addEvents.this).load(imageUrl).into(imageView);
                                                Intent intent = new Intent(addEvents.this, eventsActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(addEvents.this, "Failed to update event poster", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addEvents.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchOrganizerName() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    organizerName.setText(name);
                } else {
                    Toast.makeText(addEvents.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(addEvents.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDateDialog() {
        // Show the date picker dialog
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Note: month is 0-based, so add 1 to it if you want a 1-based month
                datePicker.setText(String.format("%s : %s : %s",String.valueOf(dayOfMonth), String.valueOf(month + 1), String.valueOf(year)));
            }
        },year,month,day);
        // Set the minimum date to the current date
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void openTimeDialogFrom() {
        // Show another TimePickerDialog to select the end time
        TimePickerDialog endTimeDialog = new TimePickerDialog(addEvents.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int endHourOfDay, int endMinute) {
                startTime = String.valueOf(endHourOfDay) + " : " + String.valueOf(endMinute);
                // Display the selected time span or update UI as needed
                timePickerFrom.setText(startTime);
            }
        },12, 00,true);
        endTimeDialog.show();
    }

    private void openTimeDialogTo() {
        // Show another TimePickerDialog to select the end time
        TimePickerDialog endTimeDialog = new TimePickerDialog(addEvents.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int endHourOfDay, int endMinute) {
                endTime = String.valueOf(endHourOfDay) + " : " + String.valueOf(endMinute);
                // Display the selected time span or update UI as needed
                timePickerTo.setText(endTime);
            }
        },12, 00,true);
        endTimeDialog.show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLocation = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(initialLocation).title("Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialLocation));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            selectedLocation = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title(location.getText().toString()));
        });
    }

    private String getLocationName(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                location.setText((CharSequence) addresses);
                Address address = addresses.get(0);
                return address.getAddressLine(0); // You can format this as needed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }
}