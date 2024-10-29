package com.example.eventmuziki;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class addEvents extends AppCompatActivity {

    ImageButton backArrow, cancel_icon, addBtnC, minusBtnC, addBtnE, minusBtnE;
    Button addEvent;
    ImageView imageView;
    EditText inputTaskName, eventDetails, otherCategory, tvCarTime, equipmentQuantity, decorationDuration, campaignDuration,
            musicGenre, musicDuration, musicInstrument, musicPrice;
    TextView datePicker, timePickerFrom, timePickerTo, organizerName;
    EditText amountTxt, location;
    FirebaseFirestore fStore;
    String startTime, endTime;
    Spinner spinnerCategory, spinnerMusicGenre,
            spinnerCarModel, carType, carColor, carSeats, carModelType,
            photoPackage, photoEquipment, photoDelivery, cateringPackage, cuisineType, cateringService, costumeAge, costumeSize,
            decorationPackage, decorationColor1, decorationColor2, decorationTheme
            ,soundPackage, equipmentType, makeUpPackage,
            influencerPackage, influencerTheme, creativityFreedom, sponsorsType, sponsorsIndustry;
    FirebaseStorage fStorage;

    CheckBox musicCheck, carCheck, photoCheck, cateringCheck, costumeCheck,
            soundCheck, decorationCheck, contentCheck, sponsorsCheck, costumeDelivery,
            soundDelivery, makeUpCheck, eventCoverage, travelCheck;
    Uri imageUri;
    String eventId;

    private Dialog popupDialog;
    int amount = 0;

    ScrollView scrollView;
    ImageButton editBtn, addPosterBtn, deleteBtn;

    LinearLayout music, carRental, photography, catering, costumes, paSystem,decorations, contentCreators, sponsors, makeUp,
            musicDetails, carRentalDetails, photographyDetails, cateringDetails, costumesDetails, paSystemDetails, decoDetails,
            contentDetails, sponsorsDetails, makeUpDetails;

    TextView musicTxt, carTxt, photographyTxt, cateringTxt, costumesTxt, paSystemTxt, decorTxt, contentTxt, sponsorsTxt, makeUpTxt;
    ImageView musicIcon, carIcon, photographyIcon, cateringIcon, costumesIcon, paSystemIcon, decorIcon, contentIcon, sponsorsIcon, makeUpIcon;

    EditText costumeQuantity, costumeDuration,guestNumber, photoDuration,
            currentSponsor, sponsorsPrice, makeUpDuration;

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

        ArrayAdapter<CharSequence> genre = ArrayAdapter.createFromResource(this,
                R.array.music_genres, android.R.layout.simple_spinner_item);
        genre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusicGenre.setAdapter(genre);

        spinnerMusicGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGenre = adapterView.getItemAtPosition(i).toString();
                // Do something with the selected genre
                if (selectedGenre.equalsIgnoreCase("Other")){
                    musicGenre.setVisibility(View.VISIBLE);
                }else{
                    musicGenre.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                                showPopupDialog(v);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        popupDialog.dismiss();
                                        Intent intent = new Intent(addEvents.this, eventsActivity.class);
                                        startActivity(intent);
                                        finish();

                                        addSubCollections(documentReference);
                                    }

                                }, 3000);

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

        addBtnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount++;
                tvCarTime.setText(String.valueOf(amount));
            }
        });
        minusBtnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 0) {
                    amount--;
                    tvCarTime.setText(String.valueOf(amount));
                }
            }
        });

        addBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount++;
                equipmentQuantity.setText(String.valueOf(amount));
            }
        });
        minusBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 0) {
                    amount--;
                    equipmentQuantity.setText(String.valueOf(amount));
                }
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
        amountTxt = findViewById(R.id.amountTxt);
        addEvent = findViewById(R.id.add_event);
        organizerName = findViewById(R.id.organizerNameTv);
        spinnerCategory = findViewById(R.id.spinner_category);
        scrollView = findViewById(R.id.scroll_view);
        location = findViewById(R.id.locationTxt);
        otherCategory = findViewById(R.id.other_category);
        decorations = findViewById(R.id.decorations);
        contentCreators = findViewById(R.id.contentCreator);
        sponsors = findViewById(R.id.sponsorship);

        music = findViewById(R.id.Music);
        carRental = findViewById(R.id.carRental);
        photography = findViewById(R.id.photography);
        catering = findViewById(R.id.catering);
        costumes = findViewById(R.id.costumes);
        paSystem = findViewById(R.id.paSystem);
        musicDetails = findViewById(R.id.musicDetails);
        carRentalDetails = findViewById(R.id.carRentalDetails);
        photographyDetails = findViewById(R.id.photographyDetails);
        cateringDetails = findViewById(R.id.cateringDetails);
        costumesDetails = findViewById(R.id.costumesDetails);
        paSystemDetails = findViewById(R.id.paSystemDetails);
        decoDetails = findViewById(R.id.decorationDetails);
        contentDetails = findViewById(R.id.contentCreatorsDetails);
        sponsorsDetails = findViewById(R.id.sponsorshipDetails);

        musicCheck = findViewById(R.id.checkMusic);
        musicGenre = findViewById(R.id.editTextGenre);
        musicDuration = findViewById(R.id.editTextDuration);
        musicInstrument = findViewById(R.id.editTextEquipment);
        musicPrice = findViewById(R.id.editTextPriceRange);
        spinnerMusicGenre = findViewById(R.id.spinnerGenre);

        musicTxt = findViewById(R.id.musicTxt);
        carTxt = findViewById(R.id.carTxt);
        photographyTxt = findViewById(R.id.photographyTxt);
        cateringTxt = findViewById(R.id.cateringTxt);
        costumesTxt = findViewById(R.id.costumeTxt);
        paSystemTxt = findViewById(R.id.paSystemTxt);
        musicIcon = findViewById(R.id.musicIcon);
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

        spinnerCarModel = findViewById(R.id.spinnerCarModel);
        carType = findViewById(R.id.spinnerCarType);
        carColor = findViewById(R.id.spinnerCarColor);
        carSeats = findViewById(R.id.spinnerCarSeats);
        carModelType = findViewById(R.id.car_spinner);
        tvCarTime = findViewById(R.id.tv_amountC);
        carCheck = findViewById(R.id.checkCar);
        addBtnC = findViewById(R.id.btn_addC);
        minusBtnC = findViewById(R.id.btn_minusC);

        photoCheck = findViewById(R.id.checkPhotography);
        photoDuration = findViewById(R.id.photographyDuration);
        photoDelivery = findViewById(R.id.spinner_photo_deliveryMethod);
        photoEquipment = findViewById(R.id.photographyEquipments);
        photoPackage = findViewById(R.id.spinner_photography_package);

        cateringCheck = findViewById(R.id.checkCatering);
        cateringPackage = findViewById(R.id.spinner_catering_package);
        cuisineType = findViewById(R.id.spinner_cuisine_type);
        cateringService = findViewById(R.id.spinner_catering_service_type);
        guestNumber = findViewById(R.id.cateringGuests);

        costumeCheck = findViewById(R.id.checkCostumes);
        costumeAge = findViewById(R.id.spinner_ageGroup);
        costumeSize = findViewById(R.id.spinner_size);
        costumeDelivery = findViewById(R.id.checkBoxCostumeDelivery);
        costumeDuration = findViewById(R.id.costumeDurations);
        costumeQuantity = findViewById(R.id.costumeQuantity);

        soundCheck = findViewById(R.id.checkSoundSystem);
        soundPackage = findViewById(R.id.spinnerSoundServices);
        equipmentType = findViewById(R.id.spinner_equipment_type);
        equipmentQuantity = findViewById(R.id.tv_equipment);
        soundDelivery = findViewById(R.id.checkBoxSoundDelivery);

        decorationCheck = findViewById(R.id.checkDecoration);
        decorationPackage = findViewById(R.id.spinnerDecorationPackage);
        decorationColor1 = findViewById(R.id.spinnerFirstColor);
        decorationColor2 = findViewById(R.id.spinnerSecondColor);
        decorationDuration = findViewById(R.id.decorationDuration);
        decorationTheme = findViewById(R.id.spinnerDecorationTheme);

        contentCheck = findViewById(R.id.checkContentCreators);
        influencerPackage = findViewById(R.id.spinner_content_package);
        campaignDuration = findViewById(R.id.campaignDuration);
        influencerTheme = findViewById(R.id.spinner_content_theme);
        eventCoverage = findViewById(R.id.checkBoxEventCoverage);
        creativityFreedom = findViewById(R.id.spinner_creativity_freedom);

        sponsorsCheck = findViewById(R.id.checkSponsorship);
        sponsorsType = findViewById(R.id.spinner_sponsorship_type);
        sponsorsIndustry = findViewById(R.id.spinner_sponsorship_industry);
        sponsorsPrice = findViewById(R.id.sponsor_amount);
        currentSponsor = findViewById(R.id.sponsor_current);
        addBtnE = findViewById(R.id.btn_addE);
        minusBtnE = findViewById(R.id.btn_minusE);

        makeUpCheck = findViewById(R.id.checkMakeUp);
        makeUpPackage = findViewById(R.id.spinnerMakeUpPackage);
        makeUpDuration = findViewById(R.id.duration);
        travelCheck = findViewById(R.id.checkBoxAvailability);
        makeUp = findViewById(R.id.makeUp);
        makeUpTxt = findViewById(R.id.makeupTxt);
        makeUpIcon = findViewById(R.id.makeupIcon);
        makeUpDetails = findViewById(R.id.makeUpDetails);

    }

    private void setupCategoryClicks() {
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicTxt.setTextColor(getResources().getColor(R.color.orange));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.orange));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.VISIBLE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);

            }
        });
        carRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set text and icon color to white
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.orange));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.orange));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);

            }
        });
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.orange));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.orange));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);
            }
        });
        catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.orange));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.orange));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);
            }
        });
        costumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.orange));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.orange));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);
            }
        });
        paSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.orange));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.orange));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.VISIBLE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);
            }
        });
        decorations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.orange));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.orange));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.VISIBLE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);

            }
        });
        contentCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.orange));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.orange));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.VISIBLE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.GONE);
            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.orange));
                makeUpTxt.setTextColor(getResources().getColor(R.color.black));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.orange));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.black));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.VISIBLE);
                makeUpDetails.setVisibility(View.GONE);

            }
        });
        makeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTxt.setTextColor(getResources().getColor(R.color.black));
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decorTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorsTxt.setTextColor(getResources().getColor(R.color.black));
                makeUpTxt.setTextColor(getResources().getColor(R.color.orange));

                musicIcon.setColorFilter(getResources().getColor(R.color.black));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decorIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorsIcon.setColorFilter(getResources().getColor(R.color.black));
                makeUpIcon.setColorFilter(getResources().getColor(R.color.orange));

                musicDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decoDetails.setVisibility(View.GONE);
                contentDetails.setVisibility(View.GONE);
                sponsorsDetails.setVisibility(View.GONE);
                makeUpDetails.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addSubCollections(DocumentReference documentReference) {
        CollectionReference servicesCollection = documentReference.collection("EventServices");

        // Add Music subcollection
        String musicGene= "";
        String musicTime = musicDuration.getText().toString();
        String musicEquipment = musicInstrument.getText().toString();
        String price = musicPrice.getText().toString();

        // check if the spinner has option Other and set the category accordingly
        if (spinnerMusicGenre.getSelectedItem().toString().equals("Other")) {
            musicGene = musicGenre.getText().toString();
        } else {
            musicGene = spinnerMusicGenre.getSelectedItem().toString();
        }
        if (musicCheck.isChecked()) {
            serviceDetailModel.hireMusician hireMusic = new serviceDetailModel.hireMusician(musicGene, musicTime, musicEquipment, price, "Music");
            servicesCollection.add(hireMusic).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Music details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add music details");
            });
        }

        // Add Car Rental subcollection
        String model = spinnerCarModel.getSelectedItem().toString();
        String type = carType.getSelectedItem().toString();
        String color = carColor.getSelectedItem().toString();
        String seats = carSeats.getSelectedItem().toString();
        String time = tvCarTime.getText().toString();
        String modelType = carModelType.getSelectedItem().toString();
        if (carCheck.isChecked()) {
            serviceDetailModel.carHire cars = new serviceDetailModel.carHire(model, type, color, seats, time, modelType,"Car Rental");
            servicesCollection.add(cars).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Car Rental details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add car rental details");
            });
        } else {
            Log.d("Subcollection", "Car rental checkbox not checked");
        }

        // Add Photography subcollection
        String Package  = photoPackage.getSelectedItem().toString();
        String duration = photoDuration.getText().toString();
        String equipment = photoEquipment.getSelectedItem().toString();
        String delivery = photoDelivery.getSelectedItem().toString();
        if (photoCheck.isChecked()) {
            serviceDetailModel.hirePhotographer photography = new serviceDetailModel.hirePhotographer(Package, duration, equipment, delivery,"Photographer");
            servicesCollection.add(photography).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Photography details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add photography details");
            });
        }else {
            Log.d("Subcollection", "Photography checkbox not checked");
        }

        // Add Catering subcollection
        String caterings = cateringPackage.getSelectedItem().toString();
        String cuisine = cuisineType.getSelectedItem().toString();
        String cateringGuests = guestNumber.getText().toString();
        String service = cateringService.getSelectedItem().toString();
        if (cateringCheck.isChecked()) {
            serviceDetailModel.hireCatering catering = new serviceDetailModel.hireCatering(caterings, cuisine, service, cateringGuests,"Catering");
            servicesCollection.add(catering).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Catering details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add catering details");
            });
        } else {
            Log.d("Subcollection", "Catering checkbox not checked");
        }

        // Add Costumes subcollection
        String costumeAges = costumeAge.getSelectedItem().toString();
        String costumeSizes = costumeSize.getSelectedItem().toString();
        String costumeQuantitys = costumeQuantity.getText().toString();
        String costumeDeliverys = costumeDelivery.isChecked() ? "Yes" : "No";
        String costumeDurations = costumeDuration.getText().toString();
        if (costumeCheck.isChecked()) {
            serviceDetailModel.hireCostumes hireCostumes = new serviceDetailModel.hireCostumes(costumeAges, costumeSizes, costumeQuantitys, costumeDeliverys, costumeDurations, "Costumes");
            servicesCollection.add(hireCostumes).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Costumes details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add costumes details");
            });
        }else {
            Log.d("Subcollection", "Costumes checkbox not checked");
        }

        // Add PA System subcollection
        String soundPackages = soundPackage.getSelectedItem().toString();
        String equipmentTypes = equipmentType.getSelectedItem().toString();
        String equipmentQuantitys = equipmentQuantity.getText().toString();
        String soundDeliverys = soundDelivery.isChecked() ? "Yes" : "No";
        if (soundCheck.isChecked()) {
            serviceDetailModel.hirePA hirePA = new serviceDetailModel.hirePA(soundPackages, equipmentTypes, equipmentQuantitys, soundDeliverys, "Sound");
            servicesCollection.add(hirePA).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "PA system details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add PA system details");
            });
        } else {
            Log.d("Subcollection", "PA system checkbox not checked");
        }

        // Add Decorations subcollection
        String decoPackage = decorationPackage.getSelectedItem().toString();
        String color1 = decorationColor1.getSelectedItem().toString();
        String color2 = decorationColor2.getSelectedItem().toString();
        String decoDuration = decorationDuration.getText().toString();
        String decoTheme = decorationTheme.getSelectedItem().toString();
        if (decorationCheck.isChecked()) {
            serviceDetailModel.hireDeco hireDecorations = new serviceDetailModel.hireDeco(decoPackage, color1, color2, decoDuration, decoTheme, "Decorations");
            servicesCollection.add(hireDecorations).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Decorations details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add decorations details");
            });
        } else {
            Log.d("Subcollection", "Decorations checkbox not checked");
        }

        // Add Content Creators subcollection
        String creatorPackage = influencerPackage.getSelectedItem().toString();
        String campaignDurations = campaignDuration.getText().toString();
        String creatorTheme = influencerTheme.getSelectedItem().toString();
        String creativity = creativityFreedom.getSelectedItem().toString();
        String eventCoverages = eventCoverage.isChecked() ? "Yes" : "No";
        if (contentCheck.isChecked()) {
            serviceDetailModel.hireContent hireContent = new serviceDetailModel.hireContent(creatorPackage, campaignDurations, creatorTheme, creativity, eventCoverages, "Influencers");
            servicesCollection.add(hireContent).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Content Creators details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add content creators details");
            });
        } else {
            Log.d("Subcollection", "Content Creators checkbox not checked");
        }

        // Add make Up sub Collection
        String makeUp = makeUpPackage.getSelectedItem().toString();
        String makeDuration = makeUpDuration.getText().toString();
        String makeUpTravel = travelCheck.isChecked() ? "Yes" : "No";

        if (makeUpCheck.isChecked()){
            serviceDetailModel.hireMakeUp hireMakeUp = new serviceDetailModel.hireMakeUp(makeUp, makeDuration, makeUpTravel, "MakeUp");
            servicesCollection.add(hireMakeUp).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Make Up details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add make up details");
            });
        } else {
            Log.d("Subcollection", "make Up CheckBox is not selected");
        }

        // Add Sponsors subcollection
        String sponsorType = sponsorsType.getSelectedItem().toString();
        String industry = sponsorsIndustry.getSelectedItem().toString();
        String sponsorshipAmount = sponsorsPrice.getText().toString();
        String currentSponsors = currentSponsor.getText().toString();
        if (sponsorsCheck.isChecked()) {
            serviceDetailModel.hireSponsors sponsors = new serviceDetailModel.hireSponsors(sponsorType, industry, sponsorshipAmount, currentSponsors, "Sponsors");
            servicesCollection.add(sponsors).addOnSuccessListener(documentReference1 -> {
                Log.d("Subcollection", "Sponsors details added");
            }).addOnFailureListener(e -> {
                Log.d("Subcollection", "Failed to add sponsors details");
            });
        }else {
            Log.d("Subcollection", "Sponsors checkbox not checked");
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

        if (imageUri == null) {
            Log.d("EventPoster", "No image selected");
        } else {
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
                                                    Log.d("Adverts", "Event poster updated successfully");
                                                    Glide.with(addEvents.this)
                                                            .load(imageUrl)
                                                            .placeholder(R.drawable.cover)
                                                            .error(R.drawable.cover)
                                                            .into(imageView);
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

    private void showPopupDialog(View view) {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(view.getContext());
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