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

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventModel;
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

public class addEvents extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton backArrow, cancel;
    Button addPoster, addEvent;
    ImageView imageView,locationBtn;
    EditText inputTaskName, eventDetails;
    TextView datePicker, timePicker, organizerName, titleTxt;
    EditText amountTxt, location;
    FirebaseFirestore fStore;
    String dueTime;
    private Spinner spinnerCategory;
    FirebaseStorage fStorage;

    Uri imageUri = null;
    String eventId;

    GoogleMap mMap;
    LatLng selectedLocation;
    ScrollView scrollView;
    RelativeLayout searchMap, locationMap;

    LinearLayout carRental, photography, catering, costumes, paSystem,
            carRentalDetails, photographyDetails, cateringDetails,
            costumesDetails, paSystemDetails;

    TextView carTxt, photographyTxt, cateringTxt, costumesTxt, paSystemTxt;
    ImageView carIcon, photographyIcon, cateringIcon, costumesIcon, paSystemIcon;

    EditText pickUpLocation, dropOffLocation, pickUpDate, dropOffDate,
            event_location, event_date, duration, price, costumeType, costumeSize, costumeQuantity,
            cateringLocation, cateringDate, guestNumber, cuisineType,
            djLocation, djDate, djDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_events);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        addPoster = findViewById(R.id.addPosterBtn);
        imageView = findViewById(R.id.eventPoster);
        inputTaskName = findViewById(R.id.inputTaskName);
        eventDetails = findViewById(R.id.eventDetails);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        locationBtn = findViewById(R.id.LocationBtn);
        amountTxt = findViewById(R.id.amountTxt);
        addEvent = findViewById(R.id.add_event);
        organizerName = findViewById(R.id.organizerNameTv);
        spinnerCategory = findViewById(R.id.spinner_category);
        cancel = findViewById(R.id.cancel_search_location);
        titleTxt = findViewById(R.id.titleTv);
        scrollView = findViewById(R.id.scroll_view);
        locationMap = findViewById(R.id.location_map);
        location = findViewById(R.id.locationTxt);

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

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.GONE);
                titleTxt.setText("Select Location");
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
                Log.d("Selected Category", selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
                Toast.makeText(addEvents.this, "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
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
                String time = timePicker.getText().toString();
                String locations = location.getText().toString();
                String amount = amountTxt.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String organizer = organizerName.getText().toString();

                if (eventName.isEmpty() || eventDetail.isEmpty() || date.isEmpty() || time.isEmpty() || amount.isEmpty()) {
                        // Show an error message if any of the fields are empty
                    Toast.makeText(addEvents.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

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

                                if (imageUri != null) {
                                    uploadEventPoster(documentReference);
                                } else {

                                    // Ensures that the success_layout disappears after 1 seconds
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(addEvents.this, eventsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);
                                    Toast.makeText(addEvents.this, "Event Added", Toast.LENGTH_SHORT).show();
                                }
                                // Add subcollections for other services if details are provided
                                addSubCollections(documentReference);

                            }).addOnFailureListener(e -> {
                                Toast.makeText(addEvents.this, "Failed to add event", Toast.LENGTH_SHORT).show();

                            });
                }

            }

        });
        addPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(addEvents.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        carRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set text and icon color to white
                carTxt.setTextColor(getResources().getColor(R.color.orange));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.orange));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));

                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.VISIBLE);

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

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.orange));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.VISIBLE);
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

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.orange));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.VISIBLE);
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

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.orange));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.VISIBLE);
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

                paSystemIcon.setColorFilter(getResources().getColor(R.color.orange));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.VISIBLE);
            }
        });

        // fetch and display organizer name
        fetchOrganizerName();

    }

    private void addSubCollections(DocumentReference documentReference) {
        // Add Car Rental subcollection
        String pickUpLocationText = pickUpLocation.getText().toString();
        String dropOffLocationText = dropOffLocation.getText().toString();
        String pickUpDateText = pickUpDate.getText().toString();
        String dropOffDateText = dropOffDate.getText().toString();
        if (!pickUpLocationText.isEmpty() && !dropOffLocationText.isEmpty() && !pickUpDateText.isEmpty() && !dropOffDateText.isEmpty()) {
            HashMap<String, Object> carRentalDetails = new HashMap<>();
            carRentalDetails.put("pickUpLocation", pickUpLocationText);
            carRentalDetails.put("dropOffLocation", dropOffLocationText);
            carRentalDetails.put("pickUpDate", pickUpDateText);
            carRentalDetails.put("dropOffDate", dropOffDateText);

            documentReference.collection("CarRental").add(carRentalDetails)
                    .addOnSuccessListener(documentRef -> Log.d("Subcollection", "Car rental details added"))
                    .addOnFailureListener(e -> Log.d("Subcollection", "Failed to add car rental details"));
        }

        // Add Photography subcollection
        String priceText = price.getText().toString();
        String eventLocationText = event_location.getText().toString();
        String eventDateText = event_date.getText().toString();
        String durationText = duration.getText().toString();
        if (!priceText.isEmpty() && !eventLocationText.isEmpty() && !eventDateText.isEmpty() && !durationText.isEmpty()) {
            HashMap<String, Object> photographyDetails = new HashMap<>();
            photographyDetails.put("price", priceText);
            photographyDetails.put("photography_location", eventLocationText);
            photographyDetails.put("photography_date", eventDateText);
            photographyDetails.put("duration", durationText);

            documentReference.collection("Photography").add(photographyDetails)
                    .addOnSuccessListener(documentRef -> Log.d("Subcollection", "Photography details added"))
                    .addOnFailureListener(e -> Log.d("Subcollection", "Failed to add photography details"));
        }

        // Add Catering subcollection
        String cateringLocationText = cateringLocation.getText().toString();
        String cateringDateText = cateringDate.getText().toString();
        String guestNumberText = guestNumber.getText().toString();
        if (!cateringLocationText.isEmpty() && !cateringDateText.isEmpty() && !guestNumberText.isEmpty()) {
            HashMap<String, Object> cateringDetails = new HashMap<>();
            cateringDetails.put("cateringLocation", cateringLocationText);
            cateringDetails.put("cateringDate", cateringDateText);
            cateringDetails.put("guestNumber", guestNumberText);
            cateringDetails.put("cuisineType", cuisineType.getText().toString());

            documentReference.collection("Catering").add(cateringDetails)
                    .addOnSuccessListener(documentRef -> Log.d("Subcollection", "Catering details added"))
                    .addOnFailureListener(e -> Log.d("Subcollection", "Failed to add catering details"));
        }

        // Add Costumes subcollection
        String costumeTypeText = costumeType.getText().toString();
        String costumeSizeText = costumeSize.getText().toString();
        String costumeQuantityText = costumeQuantity.getText().toString();
        if (!costumeTypeText.isEmpty() && !costumeSizeText.isEmpty() && !costumeQuantityText.isEmpty()) {
            HashMap<String, Object> costumesDetails = new HashMap<>();
            costumesDetails.put("costumeType", costumeTypeText);
            costumesDetails.put("costumeSize", costumeSizeText);
            costumesDetails.put("costumeQuantity", costumeQuantityText);

            documentReference.collection("Costumes").add(costumesDetails)
                    .addOnSuccessListener(documentRef -> Log.d("Subcollection", "Costumes details added"))
                    .addOnFailureListener(e -> Log.d("Subcollection", "Failed to add costumes details"));
        }

        // Add PA System subcollection
        String djLocationText = djLocation.getText().toString();
        String djDateText = djDate.getText().toString();
        String djDurationText = djDuration.getText().toString();
        if (!djLocationText.isEmpty() && !djDateText.isEmpty() && !djDurationText.isEmpty()) {
            HashMap<String, Object> paSystemDetails = new HashMap<>();
            paSystemDetails.put("djLocation", djLocationText);
            paSystemDetails.put("djDate", djDateText);
            paSystemDetails.put("djDuration", djDurationText);

            documentReference.collection("DjSystem").add(paSystemDetails)
                    .addOnSuccessListener(documentRef -> Log.d("Subcollection", "PA system details added"))
                    .addOnFailureListener(e -> Log.d("Subcollection", "Failed to add PA system details"));
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
                                                Toast.makeText(addEvents.this, "Event poster updated successfully", Toast.LENGTH_SHORT).show();
                                                Glide.with(addEvents.this).load(imageUrl).into(imageView);

                                                // Ensures that the success_layout disappears after 1 seconds
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(addEvents.this, eventsActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, 2000);
                                                Toast.makeText(addEvents.this, "Event Added", Toast.LENGTH_SHORT).show();



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
                datePicker.setText(String.format("%s : %s : %s", String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth)));
            }
        },year,month,day);
        // Set the minimum date to the current date
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void openTimeDialog() {
        // Show another TimePickerDialog to select the end time
        TimePickerDialog endTimeDialog = new TimePickerDialog(addEvents.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int endHourOfDay, int endMinute) {
                dueTime = String.valueOf(endHourOfDay) + " : " + String.valueOf(endMinute);
                // Display the selected time span or update UI as needed
                timePicker.setText(dueTime);
            }
        },12, 10,true);
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