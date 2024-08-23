package com.example.eventmuziki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.biddersAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cateringAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.costumeAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.djAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.photoAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class addServices extends AppCompatActivity {

    FirebaseStorage fStorage;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ImageButton back, addCarPhoto, addPaparaziPhoto, addCateringPhoto, addThriftPhoto, addDjPhoto,
            deleteCar, deleteThrift, deletePaparazi, deleteSound, deleteDecoration, deleteContent, deleteSponsorship,
            btnMinus, btnAdd, btnMinusc, btnAddc, btnAddD, btnMinusD, btnAddE, btnMinusE;
    ImageView carPhoto, paparazi, cateringPhoto, thriftPhoto, djPhoto,
            carClose, photographyClose, cateringClose, thriftClose, djClose, decorationClose, influencersClose, sponsorClose;
    EditText carDetailsTxt, carPriceTxt, carExtraPriceTxt,
            photographerNumbers, no_photos, delivery_time, portfolio_link, photo_package_price,price_per_hour, photo_extra_price, photo_advanced_booking,
            catering_company_name, social_media_catering, no_of_people, price_catering,
            costume_name, thrift_details, tvAmount, tvDuration, costume_buy, costume_hire, costume_lateFee, costume_delivery, costume_policy,
             equipmentName, soundDetails, areaCoverage, quantityEquipment, price_dj, extraSoundPrice, hourTv;

    CheckBox carCheckbox, checkBoxCustomization, checkBoxCleaning, checkBoxDelivery, checkBoxPhotoTravel, checkBoxPreShoot,
                checkBoxSetUp, checkBoxSoundDelivery, checkBoxWireless;
    Button seeAddCar, seeAddPhotography, seeAddCatering, seeAddThrift, seeAddDj, seeAddInfluencers, seeAddSponsor, seeAddDecoration;

    TextView categoryTxt;
    RecyclerView carRv, photographyRv, cateringRv, thriftRv, soundRv, decorationRv, influencersRv, sponsorRv;
    int amount = 0;
    int hour = 6;
    int duration = 1;

    Uri imageUri;

    Spinner car_type, car_model, car_color, spinner_transmission, spinner_seats,
            spinner_gender, spinner_ageGroup, spinner_size, spinner_material,
            spinner_package, spinner_format, delivery_method, special_equipment,
            spinner_catering_type
            , spinner_soundEquipment,spinner_djServices;

    Button carSubmit, photographySubmit, cateringSubmit, thriftSubmit, djSubmit ;

    LinearLayout addCarDetails, addPhotographyDetails, addCateringDetails, addThriftDetails, addDjDetails, addDecorationDetails, addInfluencersDetails ,addSponsorDetails,
            carDetailsTv, photographyDetailsTv, cateringDetailsTv, thriftDetailsTv, djDetailsTv, decorationDetailsTv, influencersDetailsTv, sponsorDetailsTv;

    String userCategory, userId;

    ArrayList<biddersEventModel> bidEvents;
    biddersAdapter bidAdapter;

    ArrayList<ServicesDetails.costumeModel> costumeList = new ArrayList<>();
    costumeAdapter adapterCostume;

    ArrayList<ServicesDetails.carModel> carList = new ArrayList<>();
    carAdapter adapterCar;

    ArrayList<ServicesDetails.photoModel> photoList = new ArrayList<>();
    photoAdapter adapterPhoto;

    ArrayList<ServicesDetails.cateringModel> cateringList = new ArrayList<>();
    cateringAdapter adapterCatering;

    ArrayList<ServicesDetails.soundModel> soundList = new ArrayList<>();
    djAdapter adapterSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_services);
        // Initialize Views
        initializeViews();

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), categoryOptions.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Retrieve User's Category and Display Appropriate Layout
        retrieveUserCategory();
        // Set Click Listeners for Add Photo Buttons
        setupAddPhotoListeners();
        // Setup Submit Buttons for Each Category
        setupSubmitButtons();
        // Setup Close Buttons for Each Category
        setupCloseButton();
        // Setup Add Buttons
        setupAddButtons();
        // Get User's Category
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // checkUserAccessLevel(userCategory);

        fetchServicesCategory();

        deleteServiceButtons();
        // set up the RecyclerView and its adapter
        carList = new ArrayList<>();
        adapterCar = new carAdapter(carList, this);
        carRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        carRv.setAdapter(adapterCar);

        costumeList = new ArrayList<>();
        adapterCostume = new costumeAdapter(costumeList, this);
        thriftRv.setLayoutManager(new GridLayoutManager(this, 2));
        thriftRv.setAdapter(adapterCostume);
        
        photoList = new ArrayList<>();
        adapterPhoto = new photoAdapter(photoList);
        photographyRv.setLayoutManager(new GridLayoutManager(this, 2));
        photographyRv.setAdapter(adapterPhoto);
        
        soundList = new ArrayList<>();
        adapterSound = new djAdapter(soundList);
        soundRv.setLayoutManager(new GridLayoutManager(this, 2));
        soundRv.setAdapter(adapterSound);
        /*
        

        cateringList = new ArrayList<>();
        adapterCatering = new cateringAdapter(cateringList);
        cateringRv.setLayoutManager(new GridLayoutManager(this, 2));
        cateringRv.setAdapter(adapterCatering);

       
         */

        // Set click listeners for add and minus image buttons
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount++;
                tvAmount.setText(String.valueOf(amount));
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 0) {
                    amount--;
                    tvAmount.setText(String.valueOf(amount));
                }
            }
        });

        btnAddD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration++;
                tvDuration.setText(String.valueOf(duration));
            }
        });
        btnMinusD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duration > 1) {
                    duration--;
                    tvDuration.setText(String.valueOf(duration));
                }
            }
        });

        btnAddc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour++;
                hourTv.setText(String.valueOf(hour));
            }
        });
        btnMinusc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hour > 6) {
                    hour--;
                    hourTv.setText(String.valueOf(hour));
                }
            }
        });
        
        btnAddE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                quantityEquipment.setText(String.valueOf(amount));
            }
        });
        btnMinusE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 0) {
                    amount--;
                    quantityEquipment.setText(String.valueOf(amount));
                }
            }
        });

        // check if the delivery checkbox is checked
        checkBoxDelivery.setOnClickListener(v -> {
            if (checkBoxDelivery.isChecked()) {
                costume_delivery.setVisibility(View.VISIBLE);
                costume_delivery.setText("");
            }else {
                costume_delivery.setVisibility(View.GONE);
                costume_delivery.setText("0");
            }
        });


    }

    private void setupAddButtons() {

        seeAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carDetailsTv.setVisibility(View.VISIBLE);
                carRv.setVisibility(View.GONE);
                seeAddCar.setVisibility(View.GONE);
                carClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddPhotography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photographyDetailsTv.setVisibility(View.VISIBLE);
                photographyRv.setVisibility(View.GONE);
                seeAddPhotography.setVisibility(View.GONE);
                photographyClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateringDetailsTv.setVisibility(View.VISIBLE);
                cateringRv.setVisibility(View.GONE);
                seeAddCatering.setVisibility(View.GONE);
                cateringClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddThrift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thriftDetailsTv.setVisibility(View.VISIBLE);
                thriftRv.setVisibility(View.GONE);
                seeAddThrift.setVisibility(View.GONE);
                thriftClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddDj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                djDetailsTv.setVisibility(View.VISIBLE);
                soundRv.setVisibility(View.GONE);
                seeAddDj.setVisibility(View.GONE);
                djClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddInfluencers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                influencersDetailsTv.setVisibility(View.VISIBLE);
                influencersRv.setVisibility(View.GONE);
                seeAddInfluencers.setVisibility(View.GONE);
                influencersClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sponsorDetailsTv.setVisibility(View.VISIBLE);
                sponsorRv.setVisibility(View.GONE);
                seeAddSponsor.setVisibility(View.GONE);
                sponsorClose.setVisibility(View.VISIBLE);
            }
        });
        seeAddDecoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorationDetailsTv.setVisibility(View.VISIBLE);
                decorationRv.setVisibility(View.GONE);
                seeAddDecoration.setVisibility(View.GONE);
                decorationClose.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupCloseButton() {

        carClose.setOnClickListener(v -> {
            carDetailsTv.setVisibility(View.GONE);
            carRv.setVisibility(View.VISIBLE);
            seeAddCar.setVisibility(View.VISIBLE);
            carClose.setVisibility(View.GONE);
            car_model.setSelection(0);
            car_color.setSelection(0);
            carPriceTxt.setText("");
            carExtraPriceTxt.setText("");
            carDetailsTxt.setText("");
            car_type.setSelection(0);
            spinner_transmission.setSelection(0);
            spinner_seats.setSelection(0);
            hourTv.setText("");
            carCheckbox.setChecked(false);
            amount = 0;
            hour = 6;
        });
        photographyClose.setOnClickListener(v -> {
            photographyDetailsTv.setVisibility(View.GONE);
            photographyRv.setVisibility(View.VISIBLE);
            seeAddPhotography.setVisibility(View.VISIBLE);
            photographyClose.setVisibility(View.GONE);
            photographerNumbers.setText("");
            no_photos.setText("");
            delivery_time.setText("");
            portfolio_link.setText("");
            photo_package_price.setText("");
            price_per_hour.setText("");
            photo_extra_price.setText("");
            photo_advanced_booking.setText("");
            checkBoxPhotoTravel.setChecked(false);
            checkBoxPreShoot.setChecked(false);
            spinner_package.setSelection(0);
            spinner_format.setSelection(0);
            delivery_method.setSelection(0);
            special_equipment.setSelection(0);
        });
        cateringClose.setOnClickListener(v -> {
            cateringDetailsTv.setVisibility(View.GONE);
            cateringRv.setVisibility(View.VISIBLE);
            seeAddCatering.setVisibility(View.VISIBLE);
            cateringClose.setVisibility(View.GONE);
            catering_company_name.setText("");
            social_media_catering.setText("");
            no_of_people.setText("");
            price_catering.setText("");
        });
        thriftClose.setOnClickListener(v -> {
            thriftDetailsTv.setVisibility(View.GONE);
            thriftRv.setVisibility(View.VISIBLE);
            seeAddThrift.setVisibility(View.VISIBLE);
            thriftClose.setVisibility(View.GONE);
            costume_name.setText("");
            thrift_details.setText("");
            costume_buy.setText("");
            costume_hire.setText("");
            costume_lateFee.setText("");
            costume_delivery.setText("");
            costume_policy.setText("");
            tvAmount.setText("");
            tvDuration.setText("");
            checkBoxCustomization.setChecked(false);
            checkBoxCleaning.setChecked(false);
            checkBoxDelivery.setChecked(false);
            spinner_gender.setSelection(0);
            spinner_ageGroup.setSelection(0);
            spinner_size.setSelection(0);
            spinner_material.setSelection(0);
        });
        djClose.setOnClickListener(v -> {
            djDetailsTv.setVisibility(View.GONE);
            soundRv.setVisibility(View.VISIBLE);
            seeAddDj.setVisibility(View.VISIBLE);
            djClose.setVisibility(View.GONE);
            equipmentName.setText("");
            soundDetails.setText("");
            areaCoverage.setText("");
            quantityEquipment.setText("");
            price_dj.setText("");
            extraSoundPrice.setText("");
            checkBoxSetUp.setChecked(false);
            checkBoxSoundDelivery.setChecked(false);
            checkBoxWireless.setChecked(false);
            spinner_soundEquipment.setSelection(0);
            spinner_djServices.setSelection(0);
        });
        influencersClose.setOnClickListener(v -> {
            influencersDetailsTv.setVisibility(View.GONE);
            influencersRv.setVisibility(View.VISIBLE);
            seeAddInfluencers.setVisibility(View.VISIBLE);
            influencersClose.setVisibility(View.GONE);

        });
        sponsorClose.setOnClickListener(v -> {
            sponsorDetailsTv.setVisibility(View.GONE);
            sponsorRv.setVisibility(View.VISIBLE);
            seeAddSponsor.setVisibility(View.VISIBLE);
            sponsorClose.setVisibility(View.GONE);

        });
        decorationClose.setOnClickListener(v -> {
            decorationDetailsTv.setVisibility(View.GONE);
            decorationRv.setVisibility(View.VISIBLE);
            seeAddDecoration.setVisibility(View.VISIBLE);
            decorationClose.setVisibility(View.GONE);

        });
    }

    private void initializeViews() {

        back = findViewById(R.id.back_arrow);
        fStorage = FirebaseStorage.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        btnAddc = findViewById(R.id.btn_addc);
        btnMinusc = findViewById(R.id.btn_minusc);
        hourTv = findViewById(R.id.tv_amountc);
        carCheckbox = findViewById(R.id.driverProvided);

        carClose = findViewById(R.id.closeCar);
        photographyClose = findViewById(R.id.closePhotography);
        cateringClose = findViewById(R.id.closeCatering);
        thriftClose = findViewById(R.id.closeThrift);
        djClose = findViewById(R.id.closeDj);
        decorationClose = findViewById(R.id.closeDecoration);
        influencersClose = findViewById(R.id.closeInfluencer);
        sponsorClose = findViewById(R.id.closeSponsor);

        seeAddCar = findViewById(R.id.seeAddCar);
        seeAddPhotography = findViewById(R.id.seeAddPhotography);
        seeAddCatering = findViewById(R.id.seeAddCatering);
        seeAddThrift = findViewById(R.id.seeAddThrift);
        seeAddDj = findViewById(R.id.seeAddDj);
        seeAddInfluencers = findViewById(R.id.seeAddInfluencer);
        seeAddSponsor = findViewById(R.id.seeAddSponsor);
        seeAddDecoration = findViewById(R.id.seeAddDecoration);

        addCarDetails = findViewById(R.id.addCarDetails);
        addPhotographyDetails = findViewById(R.id.AddPhotographyDetails);
        addCateringDetails = findViewById(R.id.addCateringDetails);
        addThriftDetails = findViewById(R.id.addThriftDetails);
        addDjDetails = findViewById(R.id.addDjDetails);
        addDecorationDetails = findViewById(R.id.addDecorationDetails);
        addInfluencersDetails = findViewById(R.id.addContentCreatorDetails);
        addSponsorDetails = findViewById(R.id.addSponsorDetails);

        addCarPhoto = findViewById(R.id.addCarPhoto);
        addPaparaziPhoto = findViewById(R.id.addPaparaziPhoto);
        addCateringPhoto = findViewById(R.id.addCateringPhoto);
        addThriftPhoto = findViewById(R.id.addThriftPhoto);
        addDjPhoto = findViewById(R.id.addDjPhoto);
        btnMinus = findViewById(R.id.btn_minus);
        btnAdd = findViewById(R.id.btn_add);

        carPhoto = findViewById(R.id.carPhoto);
        paparazi = findViewById(R.id.paparazi);
        cateringPhoto = findViewById(R.id.cateringPhoto);
        thriftPhoto = findViewById(R.id.thriftPhoto);
        djPhoto = findViewById(R.id.djPhoto);

        carPriceTxt = findViewById(R.id.carPrice_per_hour);
        carExtraPriceTxt = findViewById(R.id.carPrice_per_extra_hour);
        car_model = findViewById(R.id.car_model);
        car_color = findViewById(R.id.car_color);

        carDetailsTxt = findViewById(R.id.carDetailsTxt);
        catering_company_name = findViewById(R.id.catering_company_name);
        social_media_catering = findViewById(R.id.social_media_catering);
        no_of_people = findViewById(R.id.no_of_people);
        price_catering = findViewById(R.id.price_catering);
        tvAmount = findViewById(R.id.tv_amount);
        price_dj = findViewById(R.id.price_dj);
        car_type = findViewById(R.id.car_type);
        spinner_transmission = findViewById(R.id.spinner_transmission);
        spinner_seats = findViewById(R.id.spinner_seats);
        spinner_catering_type = findViewById(R.id.spinner_catering_type);
        carSubmit = findViewById(R.id.carSubmit);
        photographySubmit = findViewById(R.id.photographySubmit);
        cateringSubmit = findViewById(R.id.cateringSubmit);
        thriftSubmit = findViewById(R.id.thriftSubmit);
        djSubmit = findViewById(R.id.djSubmit);

        categoryTxt = findViewById(R.id.category_bidderTv);
        carDetailsTv = findViewById(R.id.carDetailsTv);
        photographyDetailsTv = findViewById(R.id.photographyDetailsTv);
        cateringDetailsTv = findViewById(R.id.cateringDetailsTv);
        thriftDetailsTv = findViewById(R.id.thriftDetailsTv);
        djDetailsTv = findViewById(R.id.djDetailsTv);
        decorationDetailsTv = findViewById(R.id.decorationDetailsTv);
        influencersDetailsTv = findViewById(R.id.influencerDetailsTv);
        sponsorDetailsTv = findViewById(R.id.sponsorDetailsTv);
        fStore = FirebaseFirestore.getInstance();

        carRv = findViewById(R.id.carRv);
        photographyRv = findViewById(R.id.photographyRv);
        cateringRv = findViewById(R.id.cateringRv);
        thriftRv = findViewById(R.id.thriftRv);
        soundRv = findViewById(R.id.soundRv);
        decorationRv = findViewById(R.id.decorationRv);
        influencersRv = findViewById(R.id.influencerRv);
        sponsorRv = findViewById(R.id.sponsorRv);

        deleteCar = findViewById(R.id.deleteCarPhoto);

        tvDuration = findViewById(R.id.tv_duration);
        tvAmount = findViewById(R.id.tv_amount);
        btnAddD = findViewById(R.id.btn_addD);
        btnMinusD = findViewById(R.id.btn_minusD);
        deleteThrift = findViewById(R.id.deleteCostumePhoto);
        costume_name = findViewById(R.id.costume_name);
        thrift_details = findViewById(R.id.thrift_details);
        costume_buy = findViewById(R.id.costume_buying_price);
        costume_hire = findViewById(R.id.costume_hire_price);
        costume_lateFee = findViewById(R.id.costume_late_fee);
        costume_delivery = findViewById(R.id.costume_delivery_price);
        costume_policy = findViewById(R.id.costume_return_policy);
        checkBoxCustomization = findViewById(R.id.checkbox_customization);
        checkBoxCleaning = findViewById(R.id.checkbox_cleaning);
        checkBoxDelivery = findViewById(R.id.checkbox_delivery);
        spinner_gender = findViewById(R.id.spinner_gender_costume);
        spinner_ageGroup = findViewById(R.id.age_group_costume);
        spinner_size = findViewById(R.id.spinner_size);
        spinner_material = findViewById(R.id.spinner_material);

        photographerNumbers = findViewById(R.id.tv_number_of_photographers);
        no_photos = findViewById(R.id.no_of_photos);
        delivery_time = findViewById(R.id.photo_delivery_time);
        portfolio_link = findViewById(R.id.social_media_photography);
        photo_package_price = findViewById(R.id.photography_package_price);
        price_per_hour = findViewById(R.id.price_photography);
        photo_extra_price = findViewById(R.id.extra_price_photography);
        photo_advanced_booking = findViewById(R.id.advanced_booking_time);
        deletePaparazi = findViewById(R.id.deleteThisPhoto);
        spinner_package = findViewById(R.id.spinner_photography_package);
        spinner_format = findViewById(R.id.spinner_photo_format);
        delivery_method = findViewById(R.id.spinner_photo_deliveryMethod);
        special_equipment = findViewById(R.id.photography_special_equipments);
        checkBoxPhotoTravel = findViewById(R.id.checkBoxTravel);
        checkBoxPreShoot = findViewById(R.id.checkBoxPreShoot);

        deleteSound = findViewById(R.id.deleteSoundPhoto);
        equipmentName = findViewById(R.id.equipment_name);
        soundDetails = findViewById(R.id.equipment_details);
        areaCoverage = findViewById(R.id.area_coverage);
        quantityEquipment = findViewById(R.id.tv_number_of_equipment);
        extraSoundPrice = findViewById(R.id.price_extra_sound);
        btnMinusE = findViewById(R.id.btn_minusE);
        btnAddE = findViewById(R.id.btn_addE);
        checkBoxSoundDelivery = findViewById(R.id.checkBoxDeliverySound);
        checkBoxWireless = findViewById(R.id.checkBoxWireless);
        checkBoxSetUp = findViewById(R.id.checkBoxSetup);
        spinner_soundEquipment = findViewById(R.id.spinner_equipment_type);
        spinner_djServices = findViewById(R.id.spinner_dj_services);



    }
    
    private void checkUserAccessLevel(String userCategory) {
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
                    if (userType.equals("Corporate")){
                        //fetchCategory(userCategory);
                    } if (userType.equals("Musician")) {
                        //fetchBiddersCategory(userId, userCategory);
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

    private void fetchServicesCategory() {

        fStore.collection("Services")
                .whereEqualTo("creatorId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                String serviceId = document.getId();
                                String serviceCategory = document.getString("category");
                                // Now fetch all documents under the Products subcollection
                                fStore.collection("Services")
                                        .document(serviceId)
                                        .collection("Products")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot productsSnapshot) {
                                                if (!productsSnapshot.isEmpty()) {
                                                    if (Objects.requireNonNull(serviceCategory).equalsIgnoreCase("Car Rental")){
                                                        carList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.carModel product = productDoc.toObject(ServicesDetails.carModel.class);
                                                            carList.add(product);
                                                        }
                                                        adapterCar.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Costumes")) {
                                                        costumeList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.costumeModel product = productDoc.toObject(ServicesDetails.costumeModel.class);
                                                            costumeList.add(product);
                                                        }
                                                        adapterCostume.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Photographer")){
                                                        photoList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.photoModel product = productDoc.toObject(ServicesDetails.photoModel.class);
                                                            photoList.add(product);
                                                        }
                                                        adapterPhoto.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Sound")) {
                                                        soundList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.soundModel product = productDoc.toObject(ServicesDetails.soundModel.class);
                                                            soundList.add(product);
                                                        }
                                                        adapterSound.notifyDataSetChanged();
                                                    }
                                                    /*
                                                  
                                                    if (serviceCategory.equalsIgnoreCase("Catering")) {
                                                        cateringList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.cateringModel product = productDoc.toObject(ServicesDetails.cateringModel.class);
                                                            cateringList.add(product);
                                                        }
                                                        adapterCatering.notifyDataSetChanged();
                                                    }

                                                    
                                                    if (serviceCategory.equalsIgnoreCase("Decorations")) {
                                                        decorationList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.decorationModel product = productDoc.toObject(ServicesDetails.decorationModel.class);
                                                            decorationList.add(product);
                                                        }
                                                        adapterDecoration.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Influencers")) {
                                                        contentList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.contentModel product = productDoc.toObject(ServicesDetails.contentModel.class);
                                                            contentList.add(product);
                                                            }
                                                        adapterContent.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Sponsorships")) {
                                                        sponsorshipList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.sponsorshipModel product = productDoc.toObject(ServicesDetails.sponsorshipModel.class);
                                                            sponsorshipList.add(product);
                                                            }
                                                        adapterSponsorship.notifyDataSetChanged();
                                                    }

                                                     */
                                                }
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle errors here
                                            Toast.makeText(addServices.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // No document with the given category found
                            Toast.makeText(addServices.this, "No documents found for the given category", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Toast.makeText(addServices.this, "Failed to fetch documents", Toast.LENGTH_SHORT).show();
                });


    }

    private void setupAddPhotoListeners() {
        addCarPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addPaparaziPhoto.setOnClickListener(v ->{
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addCateringPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addThriftPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addDjPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
    }

    private void deleteServiceButtons() {
        deleteCar.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Car", "No image selected");
            }
        });
        deleteThrift.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Thrift", "No image selected");
            }
        });
        deletePaparazi.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Paparazi", "No image selected");
            }
        });
    }

    private void deletePoster() {
        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to 'profile_pictures/<FILENAME>.jpg'
        final StorageReference profileRef = storageRef.child("service_posters/" + FirebaseAuth.getInstance().getUid() + ".jpg");
        // Delete the file from Firebase Storage
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Remove the profile picture URL from FireStore
                FirebaseFirestore.getInstance().collection("Services")
                        .whereEqualTo("creatorId", FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference docRef = fStore.collection("Products")
                                            .document(task.getResult().getDocuments().get(0).getId());
                                    docRef.update("eventPoster", null)
                                            .addOnSuccessListener(unused -> carPhoto.setImageResource(R.drawable.car_icon)).addOnFailureListener(e -> Toast.makeText(addServices.this, "Failed to delete profile picture URL", Toast.LENGTH_SHORT).show());
                                }else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                    Toast.makeText(addServices.this, "Failed to delete profile picture URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addServices.this, "Failed to delete profile photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserCategory() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    userCategory = documentSnapshot.getString("category");
                    categoryTxt.setText(userCategory);

                    if (userCategory != null){
                        if (userCategory.equalsIgnoreCase("Car Rental")) {
                            addCarDetails.setVisibility(View.VISIBLE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Photographer")) {
                            addPhotographyDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Catering")){
                            addCateringDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Costumes")){
                            addThriftDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Sound")){
                            addDjDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Influencers")) {
                            addInfluencersDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Decorations")) {
                            addDecorationDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                        } else if (userCategory.equalsIgnoreCase("Sponsors")) {
                            addSponsorDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                        } else {
                            // Method to hide all layouts
                            hideAllLayouts();
                            Toast.makeText(addServices.this, "No category found", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void hideAllLayouts() {
        addCarDetails.setVisibility(View.GONE);
        addPhotographyDetails.setVisibility(View.GONE);
        addCateringDetails.setVisibility(View.GONE);
        addThriftDetails.setVisibility(View.GONE);
        addDjDetails.setVisibility(View.GONE);
    }

    private void setupSubmitButtons() {
        carSubmit.setOnClickListener(v -> uploadCarDetails());
        photographySubmit.setOnClickListener(v -> uploadPhotographyDetails());
        // cateringSubmit.setOnClickListener(v -> uploadCateringDetails());
        thriftSubmit.setOnClickListener(view -> uploadThriftDetails());
        djSubmit.setOnClickListener(v -> uploadSoundDetails());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            carPhoto.setImageURI(imageUri);
            thriftPhoto.setImageURI(imageUri);
            paparazi.setImageURI(imageUri);
            cateringPhoto.setImageURI(imageUri);

        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadCarDetails() {

        String carModel = car_model.getSelectedItem().toString();
        String carColor = car_color.getSelectedItem().toString();
        String carType = car_type.getSelectedItem().toString();
        String carTransmission = spinner_transmission.getSelectedItem().toString();
        String seats = spinner_seats.getSelectedItem().toString();
        String carPrice = carPriceTxt.getText().toString();
        String carExtraPrice = carExtraPriceTxt.getText().toString();
        String driver = carCheckbox.isChecked() ? "Provided" : "Not Provided";

        ServicesDetails.carModel car = new ServicesDetails.carModel(carModel, carDetailsTxt.getText().toString(),
                carType, carColor, "Available", hourTv.getText().toString(),
                carTransmission, seats, driver, userId, carPrice, carExtraPrice, "");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(car)
                                .addOnSuccessListener(documentReference -> {
                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFields();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail service = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(service)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(car)
                                            .addOnSuccessListener(productRef -> {
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFields();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });


    }

    private void uploadThriftDetails() {

        String name = costume_name.getText().toString();
        String gender = spinner_gender.getSelectedItem().toString();
        String ageGroup = spinner_ageGroup.getSelectedItem().toString();
        String detail = thrift_details.getText().toString();
        String size = spinner_size.getSelectedItem().toString();
        String material = spinner_material.getSelectedItem().toString();
        String number = tvAmount.getText().toString();
        String duration = tvDuration.getText().toString();
        String buyPrice = costume_buy.getText().toString();
        String hirePrice = costume_hire.getText().toString();
        String lateFee = costume_lateFee.getText().toString();
        String deliveryPrice = costume_delivery.getText().toString();
        String policy = costume_policy.getText().toString();
        String customization = checkBoxCustomization.isChecked() ? "Yes" : "No";
        String cleaning = checkBoxCleaning.isChecked() ? "Yes" : "No";


        ServicesDetails.costumeModel thrift = new ServicesDetails.costumeModel(name, gender, ageGroup, detail, size, number,material, customization,cleaning, duration, buyPrice, hirePrice, lateFee, deliveryPrice, policy,"Available", userId,"");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add costume model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(thrift)
                                .addOnSuccessListener(documentReference -> {
                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsThrift();
                                    Toast.makeText(addServices.this, "Thrift added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail service = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(service)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(thrift)
                                            .addOnSuccessListener(productRef -> {
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsThrift();
                                                Toast.makeText(addServices.this, "Thrift added successfully", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadPhotographyDetails() {
        String packageName = spinner_package.getSelectedItem().toString();
        String noPhotographers = photographerNumbers.getText().toString();
        String noPhotos = no_photos.getText().toString();
        String format = spinner_format.getSelectedItem().toString();
        String deliveryTime = delivery_time.getText().toString();
        String delivery = delivery_method.getSelectedItem().toString();
        String portfolioLink = portfolio_link.getText().toString();
        String photoPackagePrice = photo_package_price.getText().toString();
        String pricePerHour = price_per_hour.getText().toString();
        String photoExtraPrice = photo_extra_price.getText().toString();
        String photoAdvancedBooking = photo_advanced_booking.getText().toString();
        String specialEquipment = special_equipment.getSelectedItem().toString();

        String travel;
        if (checkBoxPhotoTravel.isChecked()) {
            travel = "Willing to travel";
        }else {
            travel = "Not willing to travel";
        }
        String preShoot;
        if (checkBoxPreShoot.isChecked()) {
            preShoot = "Pre-Event meet up shoot";
        }else {
            preShoot = "Meet up for event only";
        }
        ServicesDetails.photoModel photos = new ServicesDetails.photoModel(packageName, noPhotographers, noPhotos, format, deliveryTime, delivery, portfolioLink, photoPackagePrice, pricePerHour, photoExtraPrice, photoAdvancedBooking, specialEquipment, "Available", userId, "", travel, preShoot);

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(photos)
                                .addOnSuccessListener(documentReference -> {
                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsPhotos();
                                    Toast.makeText(addServices.this, "Photography added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail service = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(service)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(photos)
                                            .addOnSuccessListener(productRef -> {
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsPhotos();
                                                Toast.makeText(addServices.this, "Photography added successfully", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void uploadSoundDetails() {

        String type = spinner_soundEquipment.getSelectedItem().toString();
        String name = equipmentName.getText().toString();
        String details = soundDetails.getText().toString();
        String area = areaCoverage.getText().toString();
        String quantity = quantityEquipment.getText().toString();
        String price = price_dj.getText().toString();
        String extraPrice = extraSoundPrice.getText().toString();
        String setup;
        String delivery;
        String wireless;
        String packaged = spinner_package.getSelectedItem().toString();

        if (checkBoxSetUp.isChecked()){
            setup = "Provided with Technician";
        } else {
            setup = "Not Provided";
        }
        if (checkBoxSoundDelivery.isChecked()){
            delivery = "Service Provider will deliver";
        } else {
            delivery = "Plan self delivery";
        }
        if (checkBoxWireless.isChecked()){
            wireless = "Wireless connection";
        } else {
            wireless = "Wired connection";
        }

        ServicesDetails.soundModel thrift = new ServicesDetails.soundModel(type, name,
                details, area, quantity, price, extraPrice, setup, delivery, wireless, packaged, userId, "");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add costume model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(thrift)
                                .addOnSuccessListener(documentReference -> {
                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsSound();
                                    Toast.makeText(addServices.this, "Thrift added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail service = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(service)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(thrift)
                                            .addOnSuccessListener(productRef -> {
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsSound();
                                                Toast.makeText(addServices.this, "Thrift added successfully", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    /*
    private void uploadCateringDetails() {


        DocumentReference newDocRef = fStore.collection("Services").document(); // Create a new document reference
        String documentId = newDocRef.getId(); // Get the generated document ID

        cateringModel catering = new cateringModel(catering_company_name.getText().toString(), social_media_catering.getText().toString(),
                no_of_people.getText().toString(), price_catering.getText().toString(), userCategory, "Available", spinner_catering_type.getSelectedItem().toString(), userId, imageUrls);
        // Save the new document
        newDocRef.set(catering)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        cateringDetailsTv.setVisibility(View.GONE);
                        cateringRv.setVisibility(View.VISIBLE);
                        seeAddCatering.setVisibility(View.VISIBLE);
                        cateringClose.setVisibility(View.GONE);
                        catering_company_name.setText("");
                        social_media_catering.setText("");
                        no_of_people.setText("");
                        price_catering.setText("");
                        // refresh the recycler view
                        fetchServicesCategory();
                    }

                }).addOnFailureListener(e -> {
                    Toast.makeText(addServices.this, "Error creating document", Toast.LENGTH_SHORT).show();
                });
    }

     */





    private void uploadPhotos(String id, String userCategory) {
        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference posterRef = storageRef.child("service_posters/" + id + ".jpg");

        if (imageUri == null) {
            Toast.makeText(addServices.this, "Uri is null", Toast.LENGTH_SHORT).show();
        }else {
            posterRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DocumentReference documentReference = fStore.collection("Products").document(id);
                                    String imageUrl = uri.toString();
                                    documentReference.update("servicePoster", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if (userCategory.equalsIgnoreCase("Car Rental")) {
                                                        Glide.with(addServices.this).load(imageUrl).into(carPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Photographer")) {
                                                        Glide.with(addServices.this).load(imageUrl).into(paparazi);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Catering")) {
                                                        Glide.with(addServices.this).load(imageUrl).into(cateringPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Costumes")) {
                                                        Glide.with(addServices.this).load(imageUrl).into(thriftPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Sound")) {
                                                        Glide.with(addServices.this).load(imageUrl).into(djPhoto);
                                                    }
                                                    Log.d("EventPoster", "Event poster updated successfully");

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(addServices.this, "Failed to update event poster", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addServices.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearInputFields() {
        carDetailsTv.setVisibility(View.GONE);
        carRv.setVisibility(View.VISIBLE);
        seeAddCar.setVisibility(View.VISIBLE);
        carClose.setVisibility(View.GONE);
        car_model.setSelection(0);
        car_color.setSelection(0);
        carPriceTxt.setText("");
        carExtraPriceTxt.setText("");
        carDetailsTxt.setText("");
        car_type.setSelection(0);
        carCheckbox.setChecked(false);
        spinner_transmission.setSelection(0);
        spinner_seats.setSelection(0);
        hourTv.setText("");
        fetchServicesCategory();


    }
    private void clearInputFieldsThrift(){
        thriftDetailsTv.setVisibility(View.GONE);
        thriftRv.setVisibility(View.VISIBLE);
        seeAddThrift.setVisibility(View.VISIBLE);
        thriftClose.setVisibility(View.GONE);
        costume_name.setText("");
        spinner_gender.setSelection(0);
        spinner_ageGroup.setSelection(0);
        thrift_details.setText("");
        spinner_size.setSelection(0);
        spinner_material.setSelection(0);
        checkBoxCustomization.setChecked(false);
        checkBoxCleaning.setChecked(false);
        checkBoxDelivery.setChecked(false);
        costume_buy.setText("");
        costume_hire.setText("");
        costume_lateFee.setText("");
        costume_delivery.setText("");
        costume_policy.setText("");
        tvAmount.setText("");
        tvDuration.setText("1");
        // refresh the recycler view
        fetchServicesCategory();

    }
    private void clearInputFieldsPhotos(){
        photographerNumbers.setText("");
        no_photos.setText("");
        delivery_time.setText("");
        portfolio_link.setText("");
        photo_package_price.setText("");
        price_per_hour.setText("");
        photo_extra_price.setText("");
        photo_advanced_booking.setText("");
        special_equipment.setSelection(0);
        checkBoxPhotoTravel.setChecked(false);
        checkBoxPreShoot.setChecked(false);
        spinner_package.setSelection(0);
        spinner_format.setSelection(0);
        delivery_method.setSelection(0);
        fetchServicesCategory();
    }
    private void clearInputFieldsSound(){
        equipmentName.setText("");
        soundDetails.setText("");
        areaCoverage.setText("");
        quantityEquipment.setText("");
        price_dj.setText("");
        extraSoundPrice.setText("");
        checkBoxSetUp.setChecked(false);
        checkBoxSoundDelivery.setChecked(false);
        checkBoxWireless.setChecked(false);
        spinner_soundEquipment.setSelection(0);
        spinner_package.setSelection(0);
        fetchServicesCategory();
        
    }

}