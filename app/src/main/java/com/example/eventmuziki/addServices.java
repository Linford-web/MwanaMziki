package com.example.eventmuziki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.biddersAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cateringAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.costumeAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.djAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.photoAdapter;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.serviceModels.carModel;
import com.example.eventmuziki.Models.serviceModels.cateringModel;
import com.example.eventmuziki.Models.serviceModels.costumeModel;
import com.example.eventmuziki.Models.serviceModels.djModel;
import com.example.eventmuziki.Models.serviceModels.photoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class addServices extends AppCompatActivity {

    ImageButton back, addCarPhoto, addPaparaziPhoto, addCateringPhoto, addThriftPhoto, addDjPhoto, btnMinus, btnAdd, nextPhoto, previousPhoto, btnMinusc, btnAddc;
    ImageView carPhoto, paparazi, cateringPhoto, thriftPhoto, djPhoto, carClose, photographyClose, cateringClose, thriftClose, djClose, decorationClose, influencersClose, sponsorClose;
    EditText carModelTxt, carDetailsTxt,
            artist_name, social_media_photography, price_photography,
            catering_company_name, social_media_catering, no_of_people, price_catering,
            product_name, social_media_thrift, thrift_details, product_price,
            company_name, social_media_dj, price_dj, tvAmount, hourTv;

    Button seeAddCar, seeAddPhotography, seeAddCatering, seeAddThrift, seeAddDj, seeAddInfluencers, seeAddSponsor, seeAddDecoration;

    TextView categoryTxt;
    RecyclerView carRv, photographyRv, cateringRv, thriftRv, soundRv, decorationRv, influencersRv, sponsorRv;
    int amount = 0;
    int hour = 6;

    Spinner car_type, spinner_transmission, spinner_seats,
            spinner_catering_type,
            spinner_size, spinner_dj_services;

    Button carSubmit, photographySubmit, cateringSubmit, thriftSubmit, djSubmit ;

    LinearLayout addCarDetails, addPhotographyDetails, addCateringDetails, addThriftDetails, addDjDetails, addDecorationDetails, addInfluencersDetails ,addSponsorDetails,
            carDetailsTv, photographyDetailsTv, cateringDetailsTv, thriftDetailsTv, djDetailsTv, decorationDetailsTv, influencersDetailsTv, sponsorDetailsTv;

    String userCategory, userId;
    FirebaseFirestore fStore;

    ArrayList<biddersEventModel> bidEvents;
    biddersAdapter bidAdapter;

    ArrayList<costumeModel> costumeList = new ArrayList<>();
    costumeAdapter adapterCostume;

    ArrayList<carModel> carList = new ArrayList<>();
    carAdapter adapterCar;

    ArrayList<photoModel> photoList = new ArrayList<>();
    photoAdapter adapterPhoto;

    ArrayList<cateringModel> cateringList = new ArrayList<>();
    cateringAdapter adapterCatering;

    ArrayList<djModel> djList = new ArrayList<>();
    djAdapter adapterDj;


    private static final int PICK_IMAGE_REQUEST = 1;
    ArrayList<Uri> carPhotoUris = new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_services);
        // Initialize Views
        initializeViews();
        // Set Click Listener for Back Arrow
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Retrieve User's Category and Display Appropriate Layout
        retrieveUserCategory();
        // Set Click Listeners for Add Photo Buttons
        setupAddPhotoListeners();
        // Setup Image Navigation Buttons
        setupImageNavigation();
        // Setup Submit Buttons for Each Category
        setupSubmitButtons();
        // Setup Close Buttons for Each Category
        setupCloseButton();
        // Setup Add Buttons
        setupAddButtons();
        // Get User's Category
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        checkUserAccessLevel(userCategory);

        setupImageNavigation();

        fetchServicesCategory();

        // set up the RecyclerView and its adapter
        carList = new ArrayList<>();
        adapterCar = new carAdapter(carList);
        carRv.setLayoutManager(new GridLayoutManager(this, 2));
        carRv.setAdapter(adapterCar);

        costumeList = new ArrayList<>();
        adapterCostume = new costumeAdapter(costumeList);
        thriftRv.setLayoutManager(new GridLayoutManager(this, 2));
        thriftRv.setAdapter(adapterCostume);


        /*
        photoList = new ArrayList<>();
        adapterPhoto = new photoAdapter(photoList);
        photographyRv.setLayoutManager(new GridLayoutManager(this, 2));
        photographyRv.setAdapter(adapterPhoto);

        cateringList = new ArrayList<>();
        adapterCatering = new cateringAdapter(cateringList);
        cateringRv.setLayoutManager(new GridLayoutManager(this, 2));
        cateringRv.setAdapter(adapterCatering);

        djList = new ArrayList<>();
        adapterDj = new djAdapter(djList);
        soundRv.setLayoutManager(new GridLayoutManager(this, 2));
        soundRv.setAdapter(adapterDj);
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
            carModelTxt.setText("");
            carDetailsTxt.setText("");
            car_type.setSelection(0);
            spinner_transmission.setSelection(0);
            spinner_seats.setSelection(0);
            hourTv.setText("");
        });
        photographyClose.setOnClickListener(v -> {
            photographyDetailsTv.setVisibility(View.GONE);
            photographyRv.setVisibility(View.VISIBLE);
            seeAddPhotography.setVisibility(View.VISIBLE);
            photographyClose.setVisibility(View.GONE);
            artist_name.setText("");
            social_media_photography.setText("");
            price_photography.setText("");
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
            product_name.setText("");
            social_media_thrift.setText("");
            thrift_details.setText("");
            product_price.setText("");
        });
        djClose.setOnClickListener(v -> {
            djDetailsTv.setVisibility(View.GONE);
            soundRv.setVisibility(View.VISIBLE);
            seeAddDj.setVisibility(View.VISIBLE);
            djClose.setVisibility(View.GONE);
            company_name.setText("");
            social_media_dj.setText("");
            price_dj.setText("");
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

        btnAddc = findViewById(R.id.btn_addc);
        btnMinusc = findViewById(R.id.btn_minusc);
        hourTv = findViewById(R.id.tv_amountc);

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

        carModelTxt = findViewById(R.id.carModelTxt);
        carDetailsTxt = findViewById(R.id.carDetailsTxt);
        artist_name = findViewById(R.id.artist_name);
        social_media_photography = findViewById(R.id.social_media_photography);
        price_photography = findViewById(R.id.price_photography);
        catering_company_name = findViewById(R.id.catering_company_name);
        social_media_catering = findViewById(R.id.social_media_catering);
        no_of_people = findViewById(R.id.no_of_people);
        price_catering = findViewById(R.id.price_catering);
        product_name = findViewById(R.id.product_name);
        social_media_thrift = findViewById(R.id.social_media_thrift);
        thrift_details = findViewById(R.id.thrift_details);
        product_price = findViewById(R.id.product_price);
        tvAmount = findViewById(R.id.tv_amount);
        company_name = findViewById(R.id.company_name);
        social_media_dj = findViewById(R.id.social_media_dj);
        price_dj = findViewById(R.id.price_dj);
        car_type = findViewById(R.id.car_type);
        spinner_transmission = findViewById(R.id.spinner_transmission);
        spinner_seats = findViewById(R.id.spinner_seats);
        spinner_catering_type = findViewById(R.id.spinner_catering_type);
        spinner_size = findViewById(R.id.spinner_size);
        spinner_dj_services = findViewById(R.id.spinner_dj_services);
        carSubmit = findViewById(R.id.carSubmit);
        photographySubmit = findViewById(R.id.photographySubmit);
        cateringSubmit = findViewById(R.id.cateringSubmit);
        thriftSubmit = findViewById(R.id.thriftSubmit);
        djSubmit = findViewById(R.id.djSubmit);
        nextPhoto = findViewById(R.id.nextCarPhoto);
        previousPhoto = findViewById(R.id.previousCarPhoto);

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
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        carList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            carModel car = documentSnapshot.toObject(carModel.class);
                            carList.add(car);
                        }
                        adapterCar.notifyDataSetChanged();
                    }else {
                        Toast.makeText(addServices.this, "No category found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(addServices.this, "Failed to fetch category bidders", Toast.LENGTH_SHORT).show());


        fStore.collection("Services")
                .whereEqualTo("creatorId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        costumeList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            costumeModel costume = documentSnapshot.toObject(costumeModel.class);
                            costumeList.add(costume);
                        }
                        adapterCostume.notifyDataSetChanged();
                    }else {
                        Toast.makeText(addServices.this, "No category found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(addServices.this, "Failed to fetch category bidders", Toast.LENGTH_SHORT).show());



    }

    private void setupAddPhotoListeners() {
        addCarPhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
        });
        addPaparaziPhoto.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
        });
        addCateringPhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
        });
        addThriftPhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
        });
        addDjPhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
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

    private void setupImageNavigation() {
        previousPhoto.setOnClickListener(v -> {
            if (!carPhotoUris.isEmpty()) {
                currentImageIndex = (currentImageIndex - 1 + carPhotoUris.size()) % carPhotoUris.size();
                displayCurrentImage();
            }
        });

        nextPhoto.setOnClickListener(v -> {
            if (!carPhotoUris.isEmpty()) {
                currentImageIndex = (currentImageIndex + 1) % carPhotoUris.size();
                displayCurrentImage();
            }
        });
    }

    private void setupSubmitButtons() {
        carSubmit.setOnClickListener(v -> uploadCarDetails());
        photographySubmit.setOnClickListener(v -> uploadPhotographyDetails());
        cateringSubmit.setOnClickListener(v -> uploadCateringDetails());
        thriftSubmit.setOnClickListener(v -> uploadThriftDetails());
        djSubmit.setOnClickListener(v -> uploadDjDetails());
    }

    private void displayCurrentImage() {
        if (!carPhotoUris.isEmpty()) {
            carPhoto.setImageURI(carPhotoUris.get(currentImageIndex));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    carPhotoUris.add(imageUri);
                }
                currentImageIndex = 0;
                carPhoto.setImageURI(carPhotoUris.get(currentImageIndex));
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                carPhotoUris.add(imageUri);
                currentImageIndex = 0;
                carPhoto.setImageURI(carPhotoUris.get(currentImageIndex));
            }
        }
    }

    private String getFileExtension(Uri uri) {
        return Objects.requireNonNull(getContentResolver().getType(uri)).split("/")[1];
    }

    public void saveCarDetailsToFirestore(ArrayList<String> imageUrls) {

        DocumentReference newDocRef = fStore.collection("Services").document(); // Create a new document reference
        String documentId = newDocRef.getId(); // Get the generated document ID

        carModel car = new carModel( carModelTxt.getText().toString(),
                carDetailsTxt.getText().toString(), car_type.getSelectedItem().toString(), "Available" ,hourTv.getText().toString(),
                spinner_transmission.getSelectedItem().toString(), spinner_seats.getSelectedItem().toString(),
                categoryTxt.getText().toString(), userId, imageUrls);
        // Save the new document
        newDocRef.set(car)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        carDetailsTv.setVisibility(View.GONE);
                        carRv.setVisibility(View.VISIBLE);
                        seeAddCar.setVisibility(View.VISIBLE);
                        carClose.setVisibility(View.GONE);
                        carModelTxt.setText("");
                        carDetailsTxt.setText("");
                        car_type.setSelection(0);
                        spinner_transmission.setSelection(0);
                        spinner_seats.setSelection(0);
                        hourTv.setText("");
                        // refresh the recycler view
                        fetchServicesCategory();

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating document", Toast.LENGTH_SHORT).show());
    }

    private void uploadCarDetails() {

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("car_images");
        for (int i = 0; i < carPhotoUris.size(); i++) {
            Uri imageUri = carPhotoUris.get(i);
            final StorageReference imageRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrls.add(uri.toString());
                    if (imageUrls.size() == carPhotoUris.size()) {
                        Toast.makeText(addServices.this, "Car details uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(addServices.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        }
        saveCarDetailsToFirestore(imageUrls);
    }

    private void uploadPhotographyDetails() {

        // Implement upload logic for photography details
        DocumentReference newDocRef = fStore.collection("Services").document(); // Create a new document reference
        String documentId = newDocRef.getId(); // Get the generated document ID

        // Create a new document with the generated document ID
        photoModel photo = new photoModel( artist_name.getText().toString(), social_media_photography.getText().toString(),
                price_photography.getText().toString(), userCategory, "Available",userId, imageUrls);
        newDocRef.set(photo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        photographyDetailsTv.setVisibility(View.GONE);
                        photographyRv.setVisibility(View.VISIBLE);
                        seeAddPhotography.setVisibility(View.VISIBLE);
                        photographyClose.setVisibility(View.GONE);
                        artist_name.setText("");
                        social_media_photography.setText("");
                        price_photography.setText("");
                        // refresh the recycler view
                        fetchServicesCategory();

                    }
                }).addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating document", Toast.LENGTH_SHORT).show());
    }

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

    private void uploadThriftDetails() {


        DocumentReference newDocRef = fStore.collection("Services").document(); // Create a new document reference
        String documentId = newDocRef.getId(); // Get the generated document ID
        String price = "Ksh."+ product_price.getText().toString();

        costumeModel costume = new costumeModel(product_name.getText().toString(), social_media_thrift.getText().toString(), price,
                userCategory, "Available", thrift_details.getText().toString(), spinner_size.getSelectedItem().toString(),
                tvAmount.getText().toString(), documentId, userId, imageUrls);

        // Save the new document
        newDocRef.set(costume)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        thriftDetailsTv.setVisibility(View.GONE);
                        thriftRv.setVisibility(View.VISIBLE);
                        seeAddThrift.setVisibility(View.VISIBLE);
                        thriftClose.setVisibility(View.GONE);
                        product_name.setText("");
                        social_media_thrift.setText("");
                        thrift_details.setText("");
                        product_price.setText("");
                        // refresh the recycler view
                        fetchServicesCategory();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(addServices.this, "Error creating document", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadDjDetails() {

        // Implement upload logic for DJ details
        DocumentReference newDocRef = fStore.collection("Services").document(); // Create a new document reference
        String documentId = newDocRef.getId(); // Get the generated document ID

        // Create a new document with the generated document ID
        djModel dj = new djModel( company_name.getText().toString(), social_media_dj.getText().toString(), price_dj.getText().toString(),
                spinner_dj_services.getSelectedItem().toString(), userCategory, "Available", userId, imageUrls);
        // add to Firestore
        newDocRef.set(dj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        djDetailsTv.setVisibility(View.GONE);
                        soundRv.setVisibility(View.VISIBLE);
                        seeAddDj.setVisibility(View.VISIBLE);
                        djClose.setVisibility(View.GONE);
                        company_name.setText("");
                        social_media_dj.setText("");
                        price_dj.setText("");
                        // refresh the recycler view
                        fetchServicesCategory();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating document", Toast.LENGTH_SHORT).show());
    }

}