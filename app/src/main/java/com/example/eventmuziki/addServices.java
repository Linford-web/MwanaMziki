package com.example.eventmuziki;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.example.eventmuziki.Adapters.serviceAdapters.bookedServicesAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cateringAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.costumeAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.decoAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.djAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.influencerAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.makeUpAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.mcAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.musicianAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.photoAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.sponsorAdapter;
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
    ImageButton back, addMusicianPhoto,addCarPhoto, addPaparaziPhoto, addCateringPhoto, addThriftPhoto, addDjPhoto, addInfluencersPhoto, addSponsorPhoto, addDecorationPhoto, addMakeUpPhoto, addMcPhoto,
            deleteCar, deleteThrift, deletePaparazi, deleteSound, deleteCatering, deleteContent, deleteSponsor, deleteDecoration, deleteMusician, deleteMakeUpPhoto, deleteMcPhoto,
            btnMinus, btnAdd, btnAddD, btnMinusD, btnAddE, btnMinusE, btnAddZ, btnMinusZ, btnAddM, btnMinusM, btnAddP, btnMinusP;
    ImageView musicianPhoto, carPhoto, paparazi, cateringPhoto, thriftPhoto, djPhoto, influencerPhoto, sponsorPhoto, decorationPhoto, makeUpPhoto, mcPhoto,
            musicClose, carClose, photographyClose, cateringClose, thriftClose, djClose, decorationClose, influencersClose, sponsorClose, makeUpClose, mcClose;
    EditText carDetailsTxt, carPriceTxt, carExtraPriceTxt,
            photographerNumbers, no_photos, delivery_time, portfolio_link, photo_package_price,price_per_hour, photo_extra_price, photo_advanced_booking,
            catering_company_name, no_of_people, cateringPackagePrice, catering_details,catering_transport, catering_cancel, tv_catering, no_staff,
            costume_name, thrift_details, tvAmount, tvDuration, costume_buy, costume_hire, costume_lateFee, costume_delivery, costume_policy,
             equipmentName, soundDetails, areaCoverage, quantityEquipment, price_dj, extraSoundPrice,
            influencerHandle, subscriber_count, collaborationFee, influencerCancellationPolicy, no_of_posts,
            sponsorName, brandingGuidelines, sponsorPreBooking, expectedAudience, sponsorCancellationPolicy, sponsorAmount,
            decoName, decoDetails, decoCancellationPolicy, tvDecoBooking, decoAmount, instrumentDetails, payRate, musicDetails, musicianPolicy,
            makeUpName, makeUpSpecialization, makeUpPrice, makeUpLink, makeUpBooking, makeUpPolicy,
            mcDuration, mcAudience, mcAmount, mcBooking, mcPolicy, mcExtraPrice;

    CheckBox carCheckbox, checkBoxCustomization, checkBoxCleaning, checkBoxDelivery, checkBoxPhotoTravel, checkBoxPreShoot,
                checkBoxSetUp, checkBoxSoundDelivery, checkBoxWireless,
            checkBoxCateringSetUp, checkBoxCateringStaff, checkBoxCateringTheme, checkBoxCateringEventManagement,
            eventCoverage, checkBoxDigitalPromotion,
            checkBoxDecoSetUp, checkBoxDecoCustomization, checkBoxDecoEmergency, musicInstrument, makeUpTravel, mcEquipments;
    Button seeAddMusic, seeAddCar, seeAddPhotography, seeAddCatering, seeAddThrift, seeAddDj, seeAddInfluencers, seeAddSponsor, seeAddDecoration, seeAddMakeUp, seeAddMc;

    TextView categoryTxt, addTxt, bookedTxt;
    RecyclerView musicianRv, carRv, photographyRv, cateringRv, thriftRv, soundRv, decorationRv, influencersRv, sponsorRv, makeUpRv, mcRv, bookedRv;
    int amount = 0;
    int hour = 6;
    int duration = 1;

    Uri imageUri;

    Spinner car_type, car_model, car_color, spinner_transmission, spinner_seats,
            spinner_gender, spinner_ageGroup, spinner_size, spinner_material,
            spinner_package, spinner_format, delivery_method, special_equipment,
            spinner_catering_package, spinner_cuisine_type, spinner_cateringServiceType
            ,spinner_soundEquipment,spinner_djServices,
            spinnerSocialMedia, spinnerAudienceAge, spinnerAudienceGender, spinnerAudienceLocation, spinnerSocialMediaPackage, spinnerContentType, spinnerPostingSchedule, spinnerContentTheme, spinnerContentFreedom,
            spinnerSponsorshipType, spinnerSponsorEventType, sponsorAge, sponsorIndustry, sponsorInterests,
            spinnerDecoPackage, spinnerDecoTheme, spinnerDecoEvent, spinnerMusicGenre, spinnerMakeUpType, spinnerMcSpecialization, spinnerMcRole;

    Button carSubmit, photographySubmit, cateringSubmit, thriftSubmit, djSubmit, influencerSubmit, sponsorSubmit, decorationSubmit, musicianSubmit, makeUpSubmit, mcSubmit;

    LinearLayout addMusicianDetails, addCarDetails, addPhotographyDetails, addCateringDetails, addThriftDetails, addDjDetails, addDecorationDetails, addInfluencersDetails ,addSponsorDetails, addMakeUpDetails, addMcDetails,
            musicDetailsTv, carDetailsTv, photographyDetailsTv, cateringDetailsTv, thriftDetailsTv, djDetailsTv, decorationDetailsTv, influencersDetailsTv, sponsorDetailsTv, makeUpDetailsTv, mcDetailsTv,
            addProduct, bookedProducts, add, book, emptyRv, optionView;

    String userCategory, userId;


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

    ArrayList<ServicesDetails.influencerModel> contentList = new ArrayList<>();
    influencerAdapter adapterContent;

    ArrayList<ServicesDetails.sponsorModel> sponsorList = new ArrayList<>();
    sponsorAdapter adapterSponsor;

    ArrayList<ServicesDetails.decorationModel> decorationList = new ArrayList<>();
    decoAdapter adapterDecoration;

    ArrayList<ServicesDetails.Musicians> musicList = new ArrayList<>();
    musicianAdapter adapterMusic;

    ArrayList<ServicesDetails.makeUpModel> makeUpList = new ArrayList<>();
    makeUpAdapter adapterMakeUp;

    ArrayList<ServicesDetails.cartModel> bookedList = new ArrayList<>();
    bookedServicesAdapter bookedAdapter;

    ArrayList<ServicesDetails.mcModel> mcList = new ArrayList<>();
    mcAdapter adapterMc;


    Dialog popupDialog;

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

        fetchServicesCategory();

        deleteServiceButtons();
        // set up the RecyclerView and its adapter

        musicList = new ArrayList<>();
        adapterMusic = new musicianAdapter(musicList, this);
        musicianRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        musicianRv.setAdapter(adapterMusic);

        carList = new ArrayList<>();
        adapterCar = new carAdapter(carList, this);
        carRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        carRv.setAdapter(adapterCar);

        costumeList = new ArrayList<>();
        adapterCostume = new costumeAdapter(costumeList, this);
        thriftRv.setLayoutManager(new GridLayoutManager(this, 2));
        thriftRv.setAdapter(adapterCostume);

        photoList = new ArrayList<>();
        adapterPhoto = new photoAdapter(photoList, this);
        photographyRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        photographyRv.setAdapter(adapterPhoto);

        soundList = new ArrayList<>();
        adapterSound = new djAdapter(soundList, this);
        soundRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        soundRv.setAdapter(adapterSound);

        cateringList = new ArrayList<>();
        adapterCatering = new cateringAdapter(cateringList, this);
        cateringRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cateringRv.setAdapter(adapterCatering);

        contentList = new ArrayList<>();
        adapterContent = new influencerAdapter(contentList, this);
        influencersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        influencersRv.setAdapter(adapterContent);

        sponsorList = new ArrayList<>();
        adapterSponsor = new sponsorAdapter(sponsorList, this);
        sponsorRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sponsorRv.setAdapter(adapterSponsor);

        decorationList = new ArrayList<>();
        adapterDecoration = new decoAdapter(decorationList, this);
        decorationRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        decorationRv.setAdapter(adapterDecoration);

        makeUpList = new ArrayList<>();
        adapterMakeUp = new makeUpAdapter(makeUpList, this);
        makeUpRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        makeUpRv.setAdapter(adapterMakeUp);

        bookedList = new ArrayList<>();
        bookedAdapter = new bookedServicesAdapter(bookedList, this);
        bookedRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookedRv.setAdapter(bookedAdapter);

        mcList = new ArrayList<>();
        adapterMc = new mcAdapter(mcList, this);
        mcRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mcRv.setAdapter(adapterMc);

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

        btnAddZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duration++;
                tv_catering.setText(String.valueOf(duration));
            }
        });
        btnMinusZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (duration > 1) {
                    duration--;
                    tv_catering.setText(String.valueOf(duration));
                }
            }
        });

        btnAddM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                tvDecoBooking.setText(String.valueOf(amount));
            }
        });
        btnMinusM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 0) {
                    amount--;
                    tvDecoBooking.setText(String.valueOf(amount));
                }
            }
        });

        btnAddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                photographerNumbers.setText(String.valueOf(amount));
            }
        });
        btnMinusP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 0) {
                    amount--;
                    photographerNumbers.setText(String.valueOf(amount));
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
        checkBoxCateringStaff.setOnClickListener(v -> {
            if (checkBoxCateringStaff.isChecked()) {
                no_staff.setVisibility(View.VISIBLE);
            }else {
                no_staff.setVisibility(View.GONE);
                no_staff.setText("0");
            }
        });
        musicInstrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicInstrument.isChecked()){
                    instrumentDetails.setVisibility(View.VISIBLE);
                }else {
                    instrumentDetails.setVisibility(View.GONE);
                    instrumentDetails.setText("");
                }
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.VISIBLE);
                book.setVisibility(View.GONE);
                // change color to orange
                addTxt.setTextColor(getResources().getColor(R.color.orange));
                bookedTxt.setTextColor(getResources().getColor(R.color.black));

            }
        });

        bookedProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.GONE);
                book.setVisibility(View.VISIBLE);
                // change color to orange
                addTxt.setTextColor(getResources().getColor(R.color.black));
                bookedTxt.setTextColor(getResources().getColor(R.color.orange));
            }
        });

        fetchBookedProducts();

    }

    private void fetchBookedProducts() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        fStore.collection("BookedServices")
                .whereEqualTo("creatorId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        bookedList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            emptyRv.setVisibility(View.GONE);
                            bookedRv.setVisibility(View.VISIBLE);
                        } else {
                            emptyRv.setVisibility(View.VISIBLE);
                            bookedRv.setVisibility(View.GONE);
                        }
                        bookedList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ServicesDetails.cartModel cartItem = documentSnapshot.toObject(ServicesDetails.cartModel.class);
                            bookedList.add(cartItem);
                        }
                        bookedAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "Error fetching booked services: " + e.getMessage()));
    }

    private void setupAddButtons() {

        seeAddMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcDetailsTv.setVisibility(View.VISIBLE);
                mcRv.setVisibility(View.GONE);
                seeAddMc.setVisibility(View.GONE);
                mcClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicDetailsTv.setVisibility(View.VISIBLE);
                musicianRv.setVisibility(View.GONE);
                seeAddMusic.setVisibility(View.GONE);
                musicClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carDetailsTv.setVisibility(View.VISIBLE);
                carRv.setVisibility(View.GONE);
                seeAddCar.setVisibility(View.GONE);
                carClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddPhotography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photographyDetailsTv.setVisibility(View.VISIBLE);
                photographyRv.setVisibility(View.GONE);
                seeAddPhotography.setVisibility(View.GONE);
                photographyClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateringDetailsTv.setVisibility(View.VISIBLE);
                cateringRv.setVisibility(View.GONE);
                seeAddCatering.setVisibility(View.GONE);
                cateringClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddThrift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thriftDetailsTv.setVisibility(View.VISIBLE);
                thriftRv.setVisibility(View.GONE);
                seeAddThrift.setVisibility(View.GONE);
                thriftClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddDj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                djDetailsTv.setVisibility(View.VISIBLE);
                soundRv.setVisibility(View.GONE);
                seeAddDj.setVisibility(View.GONE);
                djClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddInfluencers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                influencersDetailsTv.setVisibility(View.VISIBLE);
                influencersRv.setVisibility(View.GONE);
                seeAddInfluencers.setVisibility(View.GONE);
                influencersClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sponsorDetailsTv.setVisibility(View.VISIBLE);
                sponsorRv.setVisibility(View.GONE);
                seeAddSponsor.setVisibility(View.GONE);
                sponsorClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddDecoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorationDetailsTv.setVisibility(View.VISIBLE);
                decorationRv.setVisibility(View.GONE);
                seeAddDecoration.setVisibility(View.GONE);
                decorationClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
        seeAddMakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeUpDetailsTv.setVisibility(View.VISIBLE);
                makeUpRv.setVisibility(View.GONE);
                seeAddMakeUp.setVisibility(View.GONE);
                makeUpClose.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });

    }

    private void setupCloseButton() {

        mcClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcDetailsTv.setVisibility(View.GONE);
                mcRv.setVisibility(View.VISIBLE);
                seeAddMc.setVisibility(View.VISIBLE);
                mcClose.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                spinnerMcSpecialization.setSelection(0);
                spinnerMcRole.setSelection(0);
                mcDuration.setText("");
                mcAudience.setText("");
                mcAmount.setText("");
                mcBooking.setText("");
                mcPolicy.setText("");
                mcEquipments.setChecked(false);

            }
        });
        musicClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicDetailsTv.setVisibility(View.GONE);
                musicianRv.setVisibility(View.VISIBLE);
                seeAddMusic.setVisibility(View.VISIBLE);
                musicClose.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                instrumentDetails.setText("");
                musicInstrument.setChecked(false);
                spinnerMusicGenre.setSelection(0);
                musicDetails.setText("");
                payRate.setText("");
                musicDetails.setText("");
                musicianPolicy.setText("");
                optionView.setVisibility(View.VISIBLE);

            }
        });
        carClose.setOnClickListener(v -> {
            carDetailsTv.setVisibility(View.GONE);
            carRv.setVisibility(View.VISIBLE);
            seeAddCar.setVisibility(View.VISIBLE);
            carClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            car_type.setSelection(0);
            car_model.setSelection(0);
            car_color.setSelection(0);
            carPriceTxt.setText("");
            carExtraPriceTxt.setText("");
            carDetailsTxt.setText("");
            car_type.setSelection(0);
            spinner_transmission.setSelection(0);
            spinner_seats.setSelection(0);
            carCheckbox.setChecked(false);
            amount = 0;
            hour = 6;
            optionView.setVisibility(View.VISIBLE);
        });
        photographyClose.setOnClickListener(v -> {
            photographyDetailsTv.setVisibility(View.GONE);
            photographyRv.setVisibility(View.VISIBLE);
            seeAddPhotography.setVisibility(View.VISIBLE);
            photographyClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
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
            optionView.setVisibility(View.VISIBLE);
        });
        cateringClose.setOnClickListener(v -> {
            cateringDetailsTv.setVisibility(View.GONE);
            cateringRv.setVisibility(View.VISIBLE);
            seeAddCatering.setVisibility(View.VISIBLE);
            cateringClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            catering_company_name.setText("");
            no_of_people.setText("");
            cateringPackagePrice.setText("");
            catering_details.setText("");
            catering_transport.setText("");
            catering_cancel.setText("");
            checkBoxCateringSetUp.setChecked(false);
            checkBoxCateringStaff.setChecked(false);
            checkBoxCateringTheme.setChecked(false);
            checkBoxCateringEventManagement.setChecked(false);
            spinner_catering_package.setSelection(0);
            spinner_cuisine_type.setSelection(0);
            spinner_cateringServiceType.setSelection(0);
            tv_catering.setText("");
            optionView.setVisibility(View.VISIBLE);

        });
        thriftClose.setOnClickListener(v -> {
            thriftDetailsTv.setVisibility(View.GONE);
            thriftRv.setVisibility(View.VISIBLE);
            seeAddThrift.setVisibility(View.VISIBLE);
            thriftClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
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
            optionView.setVisibility(View.VISIBLE);
        });
        djClose.setOnClickListener(v -> {
            djDetailsTv.setVisibility(View.GONE);
            soundRv.setVisibility(View.VISIBLE);
            seeAddDj.setVisibility(View.VISIBLE);
            djClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
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
            optionView.setVisibility(View.VISIBLE);
        });
        influencersClose.setOnClickListener(v -> {
            influencersDetailsTv.setVisibility(View.GONE);
            influencersRv.setVisibility(View.VISIBLE);
            seeAddInfluencers.setVisibility(View.VISIBLE);
            influencersClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            influencerHandle.setText("");
            subscriber_count.setText("");
            collaborationFee.setText("");
            influencerCancellationPolicy.setText("");
            eventCoverage.setChecked(false);
            spinnerSocialMedia.setSelection(0);
            spinnerAudienceAge.setSelection(0);
            spinnerAudienceGender.setSelection(0);
            spinnerAudienceLocation.setSelection(0);
            spinnerSocialMediaPackage.setSelection(0);
            spinnerContentType.setSelection(0);
            spinnerPostingSchedule.setSelection(0);
            spinnerContentTheme.setSelection(0);
            spinnerContentFreedom.setSelection(0);
            no_of_posts.setText("");
            optionView.setVisibility(View.VISIBLE);

        });
        sponsorClose.setOnClickListener(v -> {
            sponsorDetailsTv.setVisibility(View.GONE);
            sponsorRv.setVisibility(View.VISIBLE);
            seeAddSponsor.setVisibility(View.VISIBLE);
            sponsorClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            sponsorName.setText("");
            brandingGuidelines.setText("");
            sponsorPreBooking.setText("");
            expectedAudience.setText("");
            sponsorCancellationPolicy.setText("");
            checkBoxDigitalPromotion.setChecked(false);
            spinnerSponsorshipType.setSelection(0);
            spinnerSponsorEventType.setSelection(0);
            sponsorAge.setSelection(0);
            sponsorIndustry.setSelection(0);
            sponsorInterests.setSelection(0);
            sponsorAmount.setText("");
            optionView.setVisibility(View.VISIBLE);

        });
        decorationClose.setOnClickListener(v -> {
            decorationDetailsTv.setVisibility(View.GONE);
            decorationRv.setVisibility(View.VISIBLE);
            seeAddDecoration.setVisibility(View.VISIBLE);
            decorationClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            decoName.setText("");
            decoDetails.setText("");
            decoCancellationPolicy.setText("");
            tvDecoBooking.setText("0");
            checkBoxDecoSetUp.setChecked(false);
            checkBoxDecoCustomization.setChecked(false);
            checkBoxDecoEmergency.setChecked(false);
            spinnerDecoPackage.setSelection(0);
            spinnerDecoTheme.setSelection(0);
            spinnerDecoEvent.setSelection(0);
            optionView.setVisibility(View.VISIBLE);

        });
        makeUpClose.setOnClickListener(v -> {
            makeUpDetailsTv.setVisibility(View.GONE);
            makeUpRv.setVisibility(View.VISIBLE);
            seeAddMakeUp.setVisibility(View.VISIBLE);
            makeUpClose.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            makeUpName.setText("");
            makeUpSpecialization.setText("");
            makeUpPrice.setText("");
            makeUpLink.setText("");
            makeUpBooking.setText("");
            makeUpPolicy.setText("");
            spinnerMakeUpType.setSelection(0);
            makeUpTravel.setChecked(false);
            optionView.setVisibility(View.VISIBLE);
        });

    }

    private void initializeViews() {

        back = findViewById(R.id.back_arrow);
        fStorage = FirebaseStorage.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        mcRv = findViewById(R.id.mcRv);
        mcPhoto = findViewById(R.id.mcPhoto);
        addMcPhoto = findViewById(R.id.addMcPhoto);
        deleteMcPhoto = findViewById(R.id.deleteMcPhoto);
        mcDetailsTv = findViewById(R.id.mcDetailsTv);
        mcSubmit = findViewById(R.id.mcSubmit);
        spinnerMcSpecialization = findViewById(R.id.spinnerMcEvents);
        spinnerMcRole = findViewById(R.id.spinnerMcRoles);
        mcDuration = findViewById(R.id.mcDuration);
        mcAudience = findViewById(R.id.mcAudience);
        mcAmount = findViewById(R.id.mcAmount);
        mcEquipments = findViewById(R.id.equipmentPref);
        mcBooking = findViewById(R.id.mcBooking);
        mcPolicy = findViewById(R.id.mcPolicy);

        carCheckbox = findViewById(R.id.driverProvided);
        addProduct = findViewById(R.id.addProduct);
        bookedProducts = findViewById(R.id.bookedProducts);
        add = findViewById(R.id.add);
        book = findViewById(R.id.booked);
        addTxt = findViewById(R.id.addProductTxt);
        bookedTxt = findViewById(R.id.bookedProductTxt);
        bookedRv = findViewById(R.id.bookedRv);
        emptyRv = findViewById(R.id.displayEmptyRv);
        optionView = findViewById(R.id.afterView);

        addMakeUpPhoto = findViewById(R.id.addMakeUpPhoto);
        deleteMakeUpPhoto = findViewById(R.id.deleteMakeUpPhoto);
        makeUpName = findViewById(R.id.makeupArtistName);
        makeUpSpecialization = findViewById(R.id.makeupSpecialization);
        makeUpPrice = findViewById(R.id.makeupPackagePrice);
        makeUpLink = findViewById(R.id.makeupPortfolioLink);
        makeUpBooking = findViewById(R.id.makeupLeadTime);
        makeUpPolicy = findViewById(R.id.makeUpPolicy);
        spinnerMakeUpType = findViewById(R.id.spinnerMakeUpPackage);
        makeUpTravel = findViewById(R.id.makeUpTravel);
        makeUpSubmit = findViewById(R.id.makeUpSubmit);
        makeUpPhoto = findViewById(R.id.makeupArtistPhoto);
        makeUpRv = findViewById(R.id.makeupArtistRv);
        makeUpClose = findViewById(R.id.closeMakeupArtist);
        makeUpDetailsTv = findViewById(R.id.makeUpDetailsTv);
        seeAddMakeUp = findViewById(R.id.seeAddMakeUp);

        carClose = findViewById(R.id.closeCar);
        photographyClose = findViewById(R.id.closePhotography);
        cateringClose = findViewById(R.id.closeCatering);
        thriftClose = findViewById(R.id.closeThrift);
        djClose = findViewById(R.id.closeDj);
        decorationClose = findViewById(R.id.closeDecoration);
        influencersClose = findViewById(R.id.closeInfluencer);
        sponsorClose = findViewById(R.id.closeSponsor);
        mcClose = findViewById(R.id.closeMc);

        seeAddMusic = findViewById(R.id.seeAddMusic);
        seeAddCar = findViewById(R.id.seeAddCar);
        seeAddPhotography = findViewById(R.id.seeAddPhotography);
        seeAddCatering = findViewById(R.id.seeAddCatering);
        seeAddThrift = findViewById(R.id.seeAddThrift);
        seeAddDj = findViewById(R.id.seeAddDj);
        seeAddInfluencers = findViewById(R.id.seeAddInfluencer);
        seeAddSponsor = findViewById(R.id.seeAddSponsor);
        seeAddDecoration = findViewById(R.id.seeAddDecoration);
        seeAddMc = findViewById(R.id.seeAddMc);

        addMusicianDetails = findViewById(R.id.addMusicianDetails);
        addCarDetails = findViewById(R.id.addCarDetails);
        addPhotographyDetails = findViewById(R.id.AddPhotographyDetails);
        addCateringDetails = findViewById(R.id.addCateringDetails);
        addThriftDetails = findViewById(R.id.addThriftDetails);
        addDjDetails = findViewById(R.id.addDjDetails);
        addDecorationDetails = findViewById(R.id.addDecorationDetails);
        addInfluencersDetails = findViewById(R.id.addContentCreatorDetails);
        addSponsorDetails = findViewById(R.id.addSponsorDetails);
        addMakeUpDetails = findViewById(R.id.addMakeUpDetails);
        addMcDetails = findViewById(R.id.addMcDetails);

        addMusicianPhoto = findViewById(R.id.addMusicianPhoto);
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
        no_of_people = findViewById(R.id.no_of_people);
        tvAmount = findViewById(R.id.tv_amount);
        price_dj = findViewById(R.id.price_dj);
        car_type = findViewById(R.id.car_type);
        spinner_transmission = findViewById(R.id.spinner_transmission);
        spinner_seats = findViewById(R.id.spinner_seats);
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

        deleteCatering = findViewById(R.id.deleteCateringPhoto);
        btnMinusZ = findViewById(R.id.btn_minusZ);
        btnAddZ = findViewById(R.id.btn_addZ);
        tv_catering = findViewById(R.id.tv_catering_time);
        cateringPackagePrice = findViewById(R.id.package_price_catering);
        catering_details = findViewById(R.id.catering_details);
        catering_transport = findViewById(R.id.catering_delivery_price);
        catering_cancel = findViewById(R.id.catering_cancellation_policy);
        spinner_catering_package = findViewById(R.id.spinner_catering_package);
        spinner_cuisine_type = findViewById(R.id.spinner_cuisine_type);
        spinner_cateringServiceType = findViewById(R.id.spinner_catering_service_type);
        catering_company_name = findViewById(R.id.catering_company_name);
        checkBoxCateringSetUp = findViewById(R.id.checkBoxCateringSetUp);
        checkBoxCateringEventManagement = findViewById(R.id.checkBoxEventCoordinationServices);
        checkBoxCateringStaff = findViewById(R.id.checkBoxWaitStaffIncluded);
        checkBoxCateringTheme = findViewById(R.id.checkBoxProvideThemedTable);
        no_staff = findViewById(R.id.no_of_staff);

        influencerSubmit = findViewById(R.id.influencerSubmit);
        addInfluencersPhoto = findViewById(R.id.addContentPhoto);
        deleteContent = findViewById(R.id.deleteContentPhoto);
        influencerPhoto = findViewById(R.id.influencerPhoto);
        influencerHandle = findViewById(R.id.influencer_handle);
        subscriber_count = findViewById(R.id.subscriber_count);
        collaborationFee = findViewById(R.id.content_fee);
        influencerCancellationPolicy = findViewById(R.id.booking_cancellation_policy);
        eventCoverage = findViewById(R.id.checkBoxEventCoverage);
        spinnerSocialMedia = findViewById(R.id.spinner_social_media);
        spinnerAudienceAge = findViewById(R.id.spinner_audience_age);
        spinnerAudienceGender = findViewById(R.id.spinner_audience_gender);
        spinnerAudienceLocation = findViewById(R.id.spinner_audience_Location);
        spinnerSocialMediaPackage = findViewById(R.id.spinner_social_media_package);
        spinnerContentType = findViewById(R.id.spinner_content_type);
        spinnerPostingSchedule = findViewById(R.id.spinner_posting_schedule);
        spinnerContentTheme = findViewById(R.id.spinner_content_theme);
        spinnerContentFreedom = findViewById(R.id.spinner_creativity_freedom);
        no_of_posts = findViewById(R.id.post_count);

        sponsorSubmit = findViewById(R.id.sponsorSubmit);
        addSponsorPhoto = findViewById(R.id.addSponsorPhoto);
        deleteSponsor = findViewById(R.id.deleteSponsorPhoto);
        sponsorPhoto = findViewById(R.id.sponsorPhoto);
        sponsorName = findViewById(R.id.sponsor_name);
        brandingGuidelines = findViewById(R.id.branding_guidelines);
        sponsorPreBooking = findViewById(R.id.branding_leadTime);
        expectedAudience = findViewById(R.id.sponsor_expected_audience);
        sponsorCancellationPolicy = findViewById(R.id.sponsor_cancellation_policy);
        checkBoxDigitalPromotion = findViewById(R.id.checkBoxDigitalPromotion);
        spinnerSponsorshipType = findViewById(R.id.spinner_sponsorship_type);
        spinnerSponsorEventType = findViewById(R.id.spinner_event_Category);
        sponsorAge = findViewById(R.id.spinner_sponsorship_audience_age);
        sponsorIndustry = findViewById(R.id.spinner_sponsorship_audience_industry);
        sponsorInterests = findViewById(R.id.spinner_sponsorship_audience_interest);
        sponsorAmount = findViewById(R.id.sponsor_con_amount);

        decorationPhoto = findViewById(R.id.decorationPhoto);
        deleteDecoration = findViewById(R.id.deleteDecorationPhoto);
        decorationSubmit = findViewById(R.id.decorationSubmit);
        addDecorationPhoto = findViewById(R.id.addDecorationPhoto);
        decoName = findViewById(R.id.decoration_company_name);
        decoDetails = findViewById(R.id.decoration_package_details);
        decoCancellationPolicy = findViewById(R.id.deco_cancellation_policy);
        checkBoxDecoSetUp = findViewById(R.id.checkbox_decoration_setup);
        checkBoxDecoCustomization = findViewById(R.id.checkbox_decoration_customization);
        checkBoxDecoEmergency = findViewById(R.id.checkbox_decoration_emergency);
        spinnerDecoPackage = findViewById(R.id.spinner_decoration_package);
        spinnerDecoTheme = findViewById(R.id.spinner_decoration_theme);
        spinnerDecoEvent = findViewById(R.id.spinner_decoration_event);
        tvDecoBooking = findViewById(R.id.tv_decoration_booking);
        btnAddM = findViewById(R.id.btn_addM);
        btnMinusM = findViewById(R.id.btn_minusM);
        decoAmount = findViewById(R.id.decoration_amount);
        btnAddP = findViewById(R.id.btn_addP);
        btnMinusP = findViewById(R.id.btn_minusP);
        photographerNumbers = findViewById(R.id.tv_number_of_photographers);

        musicianPhoto = findViewById(R.id.musicianPhoto);
        deleteMusician = findViewById(R.id.deleteMusicianPhoto);
        musicClose = findViewById(R.id.closeMusic);
        musicDetailsTv = findViewById(R.id.musicianDetailsTv);
        musicianRv = findViewById(R.id.musicianRv);
        musicianSubmit = findViewById(R.id.musicianSubmit);
        spinnerMusicGenre = findViewById(R.id.musicianGenre);
        musicInstrument = findViewById(R.id.instrumentProvided);
        instrumentDetails = findViewById(R.id.instrumentDetails);
        payRate = findViewById(R.id.musicianPricePerHour);
        musicDetails = findViewById(R.id.musicianDetailsTxt);
        musicianPolicy = findViewById(R.id.musicianPolicy);


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
                                                    if (Objects.requireNonNull(serviceCategory).equalsIgnoreCase("Music")){
                                                        musicList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.Musicians product = productDoc.toObject(ServicesDetails.Musicians.class);
                                                            musicList.add(product);
                                                            }
                                                        adapterMusic.notifyDataSetChanged();
                                                    }
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
                                                    if (serviceCategory.equalsIgnoreCase("Catering")) {
                                                        cateringList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.cateringModel product = productDoc.toObject(ServicesDetails.cateringModel.class);
                                                            cateringList.add(product);
                                                        }
                                                        adapterCatering.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Influencers")) {
                                                        contentList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.influencerModel product = productDoc.toObject(ServicesDetails.influencerModel.class);
                                                            contentList.add(product);
                                                        }
                                                        adapterContent.notifyDataSetChanged();
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
                                                    if (serviceCategory.equalsIgnoreCase("Sponsors")) {
                                                        sponsorList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.sponsorModel product = productDoc.toObject(ServicesDetails.sponsorModel.class);
                                                            sponsorList.add(product);
                                                            }
                                                        adapterSponsor.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("MakeUp")) {
                                                        makeUpList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.makeUpModel product = productDoc.toObject(ServicesDetails.makeUpModel.class);
                                                            makeUpList.add(product);
                                                            }
                                                        adapterMakeUp.notifyDataSetChanged();
                                                    }
                                                    if (serviceCategory.equalsIgnoreCase("Emcee")) {
                                                        mcList.clear();
                                                        for (DocumentSnapshot productDoc : productsSnapshot) {
                                                            // Process each product document
                                                            ServicesDetails.mcModel product = productDoc.toObject(ServicesDetails.mcModel.class);
                                                            mcList.add(product);
                                                        }
                                                        adapterMc.notifyDataSetChanged();
                                                    }

                                                }
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle errors here
                                            //Toast.makeText(addServices.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                                            Log.e("fetchServicesCategory", "Error fetching products: " + e.getMessage());
                                        });
                            }
                        } else {
                            // No document with the given category found
                            //Toast.makeText(addServices.this, "No documents found for the given category", Toast.LENGTH_SHORT).show();
                            Log.d("fetchServicesCategory", "No documents found for the given category");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Toast.makeText(addServices.this, "Failed to fetch documents", Toast.LENGTH_SHORT).show();
                    Log.e("fetchServicesCategory", "Error fetching documents: " + e.getMessage());
                });


    }

    private void setupAddPhotoListeners() {

        addMcPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        addMusicianPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
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
        addDecorationPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addInfluencersPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addSponsorPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        addMakeUpPhoto.setOnClickListener(v -> {
            ImagePicker.with(addServices.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

    }

    private void deleteServiceButtons() {

        deleteMcPhoto.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Musician", "No image selected");
            }
        });
        deleteMusician.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Musician", "No image selected");
            }

        });
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
        deleteSound.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Sound", "No image selected");
            }
        });
        deleteCatering.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Photo", "No image selected");
            }
        });
        deleteDecoration.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Decoration", "No image selected");
            }
        });
        deleteContent.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Content", "No image selected");
            }
        });
        deleteSponsor.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Sponsor", "No image selected");
            }
        });
        deleteMakeUpPhoto.setOnClickListener(v -> {
            if (imageUri != null) {
                deletePoster();
            } else {
                Log.d("add Service Makeup", "No image selected");
            }
        });

    }

    private void deletePoster() {
        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to 'profile_pictures/<FILENAME>.jpg'
        final StorageReference profileRef = storageRef.child("product_photos/" + FirebaseAuth.getInstance().getUid() + ".jpg");
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
                            addMusicianDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.VISIBLE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Emcee")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.VISIBLE);

                        } else if (userCategory.equalsIgnoreCase("Music")) {
                            addMusicianDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Photographer")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Catering")){
                            addMusicianDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Costumes")){
                            addMusicianDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Sound")){
                            addMusicianDetails.setVisibility(View.GONE);
                            addDjDetails.setVisibility(View.VISIBLE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Influencers")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Decorations")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("Sponsors")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

                        } else if (userCategory.equalsIgnoreCase("MakeUp")) {
                            addMusicianDetails.setVisibility(View.GONE);
                            addMakeUpDetails.setVisibility(View.VISIBLE);
                            addDjDetails.setVisibility(View.GONE);
                            addCarDetails.setVisibility(View.GONE);
                            addPhotographyDetails.setVisibility(View.GONE);
                            addCateringDetails.setVisibility(View.GONE);
                            addThriftDetails.setVisibility(View.GONE);
                            addDecorationDetails.setVisibility(View.GONE);
                            addInfluencersDetails.setVisibility(View.GONE);
                            addSponsorDetails.setVisibility(View.GONE);
                            addMcDetails.setVisibility(View.GONE);

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
        addDecorationDetails.setVisibility(View.GONE);
        addInfluencersDetails.setVisibility(View.GONE);
        addSponsorDetails.setVisibility(View.GONE);
        addMusicianDetails.setVisibility(View.GONE);
        addMakeUpDetails.setVisibility(View.GONE);
        addMcDetails.setVisibility(View.GONE);
    }

    private void setupSubmitButtons() {
        musicianSubmit.setOnClickListener(view -> uploadMusicianDetails());
        carSubmit.setOnClickListener(v -> uploadCarDetails());
        photographySubmit.setOnClickListener(v -> uploadPhotographyDetails());
        cateringSubmit.setOnClickListener(v -> uploadCateringDetails());
        thriftSubmit.setOnClickListener(view -> uploadThriftDetails());
        djSubmit.setOnClickListener(v -> uploadSoundDetails());
        decorationSubmit.setOnClickListener(v -> uploadDecorationDetails());
        influencerSubmit.setOnClickListener(v -> uploadInfluencersDetails());
        sponsorSubmit.setOnClickListener(v -> uploadSponsorDetails());
        makeUpSubmit.setOnClickListener(v -> uploadMakeUpDetails());
        mcSubmit.setOnClickListener(v -> uploadMcDetails());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is OK and the request code matches
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            musicianPhoto.setImageURI(imageUri);
            thriftPhoto.setImageURI(imageUri);
            paparazi.setImageURI(imageUri);
            cateringPhoto.setImageURI(imageUri);
            djPhoto.setImageURI(imageUri);
            influencerPhoto.setImageURI(imageUri);
            sponsorPhoto.setImageURI(imageUri);
            decorationPhoto.setImageURI(imageUri);
            mcPhoto.setImageURI(imageUri);
            carPhoto.setImageURI(imageUri);
            makeUpPhoto.setImageURI(imageUri);

            // Repeat for other ImageViews if necessary

            if (imageUri != null) {
                // Get reference to Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                // Create a unique path for each product photo
                final StorageReference photoRef = storageRef.child("product_photos/" + FirebaseAuth.getInstance().getUid() + "/" + System.currentTimeMillis() + ".jpg");

                // Upload the selected image to Firebase Storage
                photoRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                // Save the image URL to Firestore
                                FirebaseFirestore.getInstance().collection("Products")
                                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                        .update("image", imageUrl)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(addServices.this, "Product photo updated successfully", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(addServices.this, "Failed to update product photo", Toast.LENGTH_SHORT).show();
                                        });
                            });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(addServices.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadMcDetails() {
        String specialization = spinnerMcSpecialization.getSelectedItem().toString();
        String role = spinnerMcRole.getSelectedItem().toString();
        String duration = mcDuration.getText().toString();
        String audience = mcAudience.getText().toString();
        String amount = mcAmount.getText().toString();
        String equipment = mcEquipments.isChecked() ? "Yes" : "No";
        String booking = mcBooking.getText().toString();
        String policy = mcPolicy.getText().toString();

        ServicesDetails.mcModel mc = new ServicesDetails.mcModel(specialization, role, duration, audience, amount, equipment, booking, policy,
                "Available", FirebaseAuth.getInstance().getCurrentUser().getUid(), "", "", "Emcee");

        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(mc)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsMc();
                                    Toast.makeText(addServices.this, "Emcee added successfully", Toast.LENGTH_SHORT).show();

                                }).addOnFailureListener(e -> {
                                    Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show();
                                });
                    }else {
                        ServicesDetails.serviceDetail mcs = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(mcs).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        String documentId = documentReference.getId();
                                        documentReference.update("serviceId", documentId);

                                        documentReference.collection("Products")
                                                .add(mc).addOnSuccessListener(documentReference1 -> {
                                                    String docId = documentReference1.getId();
                                                    documentReference1.update("productId", docId);

                                                    showPopupDialog();
                                                    uploadPhotos(documentReference1.getId(), categoryTxt.getText().toString());
                                                    clearInputFieldsMc();
                                                    Log.d("TAG", "Emcee added successfully");
                                                }).addOnFailureListener(e -> {
                                                    Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show();
                                                });

                                    }
                                }).addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());

                    }
                });
    }

    private void uploadMusicianDetails() {
        String genre = spinnerMusicGenre.getSelectedItem().toString();
        String instrument = musicInstrument.isChecked() ? "Artist to provide their Instrument" : "Organizer to provide Instrument";
        String InstrumentDetail = instrumentDetails.getText().toString();
        String musicPayRate = payRate.getText().toString();
        String musicDetail = musicDetails.getText().toString();
        String policy = musicianPolicy.getText().toString();

       ServicesDetails.Musicians music = new ServicesDetails.Musicians(genre, instrument, InstrumentDetail,
               "Available", musicPayRate,musicDetail,policy,"","", FirebaseAuth.getInstance().getCurrentUser().getUid(),"Music");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(music)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsMusic();
                                    Toast.makeText(addServices.this, "Photography added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail photo = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(photo)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(music)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsMusic();
                                                Log.d("TAG", "Musician added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });



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
                carType, carColor, "Available", carTransmission, seats, driver, userId, carPrice, carExtraPrice, "", "", "Car Rental");

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
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFields();
                                    Log.d("TAG", "Car added successfully");
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
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFields();
                                                Log.d("TAG", "Car added successfully");
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


        ServicesDetails.costumeModel thrift = new ServicesDetails.costumeModel(name, gender, ageGroup, detail, size, number,material, customization,cleaning, duration, buyPrice, hirePrice, lateFee, deliveryPrice, policy,"Available", userId,"", "", "Costumes");

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
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsThrift();
                                    Toast.makeText(addServices.this, "Thrift added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail costumes = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(costumes)
                                .addOnSuccessListener(documentReference -> {

                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(thrift)
                                            .addOnSuccessListener(productRef -> {


                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsThrift();
                                                Log.d("TAG", "Thrift added successfully");
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
        ServicesDetails.photoModel photos = new ServicesDetails.photoModel(packageName, noPhotographers,
                noPhotos, format, deliveryTime, delivery, portfolioLink, photoPackagePrice, pricePerHour,
                photoExtraPrice, photoAdvancedBooking, specialEquipment, "Available", userId, "",
                travel, preShoot, "", "Photographers");

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
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsPhotos();
                                    Toast.makeText(addServices.this, "Photography added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail photo = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(photo)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(photos)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
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
                details, area, quantity, price, extraPrice, setup, delivery, wireless, packaged, "Available", userId, "", "", "Sound");

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
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsSound();
                                    Log.d("TAG", "Sound added successfully");
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail clad = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(clad)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(thrift)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsSound();
                                               Log.d("TAG", "Sound added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadCateringDetails() {

        String name = catering_company_name.getText().toString();
        String Package = spinner_catering_package.getSelectedItem().toString();
        String cuisine = spinner_cuisine_type.getSelectedItem().toString();
        String service = spinner_cateringServiceType.getSelectedItem().toString();
        String number = no_of_people.getText().toString();
        String packagePrice = cateringPackagePrice.getText().toString();
        String detail = catering_details.getText().toString();
        String booking = tv_catering.getText().toString();
        String setup;
        String staff;
        String numberStaff = no_staff.getText().toString();
        String coordinator;
        String theme;
        String transportation = catering_transport.getText().toString();
        String cancelPolicy = catering_cancel.getText().toString();
        if (checkBoxCateringSetUp.isChecked()){
            setup = "Set Up and Clean Up services provided";
        } else {
            setup = "Not Provided";
        }
        if (checkBoxCateringStaff.isChecked()){
            staff = "Catering Staff Provided";
        } else {
            staff = "Not Provided";
        }
        if (checkBoxCateringEventManagement.isChecked()){
            coordinator = "Event Management Provided";
        } else {
            coordinator = "Not Provided";
        }
        if (checkBoxCateringTheme.isChecked()){
            theme = "Customizable Theme Provided to customer preference";
        } else {
            theme = "Not Provided";
        }

        ServicesDetails.cateringModel food = new ServicesDetails.cateringModel( name,Package,cuisine,
                service ,number, packagePrice, detail, booking, setup, staff, numberStaff, coordinator, theme, transportation ,cancelPolicy,"Available", userId, "", "", "Catering");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(food)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsCatering();
                                    Log.d("TAG", "Catering added successfully");
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail foods = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(foods)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(food)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsCatering();
                                                Log.d("TAG", "Catering added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });


    }

    private void uploadInfluencersDetails() {

        String handle = influencerHandle.getText().toString();
        String platform = spinnerSocialMedia.getSelectedItem().toString();
        String subscribers = subscriber_count.getText().toString();
        String age = spinnerAudienceAge.getSelectedItem().toString();
        String gender = spinnerAudienceGender.getSelectedItem().toString();
        String location = spinnerAudienceLocation.getSelectedItem().toString();
        String Package = spinnerSocialMediaPackage.getSelectedItem().toString();
        String content = spinnerContentType.getSelectedItem().toString();
        String posts = no_of_posts.getText().toString();
        String schedule = spinnerPostingSchedule.getSelectedItem().toString();
        String theme = spinnerContentTheme.getSelectedItem().toString();
        String freedom = spinnerContentFreedom.getSelectedItem().toString();
        String collaboration = collaborationFee.getText().toString();
        String cancellation = influencerCancellationPolicy.getText().toString();
        String coverage = eventCoverage.getText().toString();

        ServicesDetails.influencerModel influencer = new ServicesDetails.influencerModel(handle ,platform, subscribers, age, gender, location , Package, content,posts,schedule ,theme ,freedom ,collaboration ,cancellation, coverage,"Available", userId, "", "", "Influencers");


        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add costume model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(influencer)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsInfluencer();
                                    Log.d("TAG", "Influencer added successfully");
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail contents = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(contents)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(influencer)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsInfluencer();
                                                Log.d("TAG", "Influencer added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadSponsorDetails() {
        String name = sponsorName.getText().toString();
        String type = spinnerSponsorshipType.getSelectedItem().toString();
        String event = spinnerSponsorEventType.getSelectedItem().toString();
        String age = sponsorAge.getSelectedItem().toString();
        String industry = sponsorIndustry.getSelectedItem().toString();
        String interests = sponsorInterests.getSelectedItem().toString();
        String promotion;
        String amount = sponsorAmount.getText().toString();
        String guide = brandingGuidelines.getText().toString();
        String preBooking = sponsorPreBooking.getText().toString();
        String audience = expectedAudience.getText().toString();
        String cancellation = sponsorCancellationPolicy.getText().toString();

        if (checkBoxDigitalPromotion.isChecked()) {
            promotion = "Digital Promotion";
        }else {
            promotion = "Non Promotional";
        }


        ServicesDetails.sponsorModel sponsor = new ServicesDetails.sponsorModel( name, type, event , age , industry, interests, promotion, amount, guide, preBooking , audience ,cancellation,"Available", userId, "", "", "Sponsors");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(sponsor)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsSponsor();
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
                                            .add(sponsor)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsSponsor();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadDecorationDetails() {

        String name = decoName.getText().toString();
        String Package = spinnerDecoPackage.getSelectedItem().toString();
        String theme = spinnerDecoTheme.getSelectedItem().toString();
        String event = spinnerDecoEvent.getSelectedItem().toString();
        String details = decoDetails.getText().toString();
        String customization = checkBoxDecoCustomization.isChecked() ? "Customized to customer preference" : "Non customizable";
        String emergency = checkBoxDecoEmergency.isChecked() ? "Decoration Delivery services" : "Non delivered";
        String setUp = checkBoxDecoSetUp.isChecked() ? "Provided" : "Not Provided";
        String time = tvDecoBooking.getText().toString();
        String cancellation = decoCancellationPolicy.getText().toString();
        String amounts = decoAmount.getText().toString();

        ServicesDetails.decorationModel deco = new ServicesDetails.decorationModel( name , Package , theme , event, details ,
                customization , emergency, setUp ,time , cancellation, "", amounts, userId, "", "", "Decorations");

        // Query to check if a document with the same userId exists
        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(deco)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsDeco();
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail foods = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(foods)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(deco)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsDeco();
                                                Log.d("TAG", "Decoration added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadMakeUpDetails() {
        String name = makeUpName.getText().toString();
        String speciality = makeUpSpecialization.getText().toString();
        String Package = spinnerMakeUpType.getSelectedItem().toString();
        String price = makeUpPrice.getText().toString();
        String link = makeUpLink.getText().toString();
        String booking = makeUpBooking.getText().toString();
        String policy = makeUpPolicy.getText().toString();
        String travel;

        if (makeUpTravel.isChecked()){
            travel = "Yes";
        } else {
            travel = "No";
        }

        ServicesDetails.makeUpModel beauty = new ServicesDetails.makeUpModel(name, speciality, Package, price, link, travel, booking, policy,
                "Available", FirebaseAuth.getInstance().getCurrentUser().getUid(), "", "", "MakeUp" );

        fStore.collection("Services")
                .whereEqualTo("creatorId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Document with same userId exists, add car model details to the 'Products' subcollection
                        DocumentSnapshot existingDocument = task.getResult().getDocuments().get(0);
                        existingDocument.getReference().collection("Products")
                                .add(beauty)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("productId", documentId);

                                    uploadPhotos(documentReference.getId(), categoryTxt.getText().toString());
                                    clearInputFieldsMakeUp();
                                    Log.d("TAG", "Product added successfully");
                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error adding product", Toast.LENGTH_SHORT).show());

                    } else {
                        // No document with the same userId exists, create new service document and add product details
                        ServicesDetails.serviceDetail photo = new ServicesDetails.serviceDetail(userId, categoryTxt.getText().toString(), "");

                        fStore.collection("Services")
                                .add(photo)
                                .addOnSuccessListener(documentReference -> {
                                    String documentId = documentReference.getId();
                                    documentReference.update("serviceId", documentId);

                                    documentReference.collection("Products")
                                            .add(beauty)
                                            .addOnSuccessListener(productRef -> {
                                                String docId = productRef.getId();
                                                productRef.update("productId", docId);

                                                showPopupDialog();
                                                uploadPhotos(productRef.getId(), categoryTxt.getText().toString());
                                                clearInputFieldsMakeUp();
                                                Log.d("TAG", "Product added successfully");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating product", Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(e -> Toast.makeText(addServices.this, "Error creating service", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    private void uploadPhotos(String id, String userCategory) {
        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference posterRef = storageRef.child("product_photos/" + id + ".jpg");

        if (imageUri == null) {
            //Toast.makeText(addServices.this, "Uri is null", Toast.LENGTH_SHORT).show();
            Log.d("EventPoster", "Uri is null");
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
                                                    if (userCategory.equalsIgnoreCase("Music")){
                                                        Glide.with(addServices.this)
                                                                .load(imageUrl)
                                                                .placeholder(R.drawable.music_icon)
                                                                .error(R.drawable.music_icon)
                                                                .into(musicianPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Car Rental")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.car_icon).error(R.drawable.car_icon).into(carPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Photographer")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.camera_icon).error(R.drawable.camera_icon).into(paparazi);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Catering")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.fastfood_icon).error(R.drawable.fastfood_icon).into(cateringPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Costumes")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.costume_icon).error(R.drawable.costume_icon).into(thriftPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Sound")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.dj_icon).error(R.drawable.dj_icon).into(djPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Influencers")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.content_icon).error(R.drawable.content_icon).into(influencerPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Sponsors")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.sponsorship_icon).error(R.drawable.sponsorship_icon).into(sponsorPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Decorations")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.deco_icon).error(R.drawable.deco_icon).into(decorationPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("MakeUp")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.makeup_icon).error(R.drawable.makeup_icon).into(makeUpPhoto);
                                                    }
                                                    if (userCategory.equalsIgnoreCase("Emcee")) {
                                                        Glide.with(addServices.this).load(imageUrl).placeholder(R.drawable.mc_icon).error(R.drawable.mc_icon).into(mcPhoto);
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

    private void clearInputFieldsMusic() {
        musicDetailsTv.setVisibility(View.GONE);
        musicianRv.setVisibility(View.VISIBLE);
        seeAddMusic.setVisibility(View.VISIBLE);
        musicClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        spinnerMusicGenre.setSelection(0);
        musicInstrument.setChecked(false);
        musicDetails.setText("");
        payRate.setText("");
        musicDetails.setText("");
        musicianPolicy.setText("");
        fetchServicesCategory();
    }
    private void clearInputFields() {
        carDetailsTv.setVisibility(View.GONE);
        carRv.setVisibility(View.VISIBLE);
        seeAddCar.setVisibility(View.VISIBLE);
        carClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        car_model.setSelection(0);
        car_color.setSelection(0);
        carPriceTxt.setText("");
        carExtraPriceTxt.setText("");
        carDetailsTxt.setText("");
        car_type.setSelection(0);
        carCheckbox.setChecked(false);
        spinner_transmission.setSelection(0);
        spinner_seats.setSelection(0);
        carPhoto.setImageResource(R.drawable.car_icon);
        fetchServicesCategory();


    }
    private void clearInputFieldsThrift(){
        thriftDetailsTv.setVisibility(View.GONE);
        thriftRv.setVisibility(View.VISIBLE);
        seeAddThrift.setVisibility(View.VISIBLE);
        thriftClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
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
        thriftPhoto.setImageResource(R.drawable.costume_icon);
        // refresh the recycler view
        fetchServicesCategory();

    }
    private void clearInputFieldsPhotos() {
        photographyDetailsTv.setVisibility(View.GONE);
        photographyRv.setVisibility(View.VISIBLE);
        seeAddPhotography.setVisibility(View.VISIBLE);
        photographyClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
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
        paparazi.setImageResource(R.drawable.camera_icon);
        fetchServicesCategory();
    }
    private void clearInputFieldsSound(){
        djDetailsTv.setVisibility(View.GONE);
        soundRv.setVisibility(View.VISIBLE);
        seeAddDj.setVisibility(View.VISIBLE);
        djClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        equipmentName.setText("");
        soundDetails.setText("");
        areaCoverage.setText("");
        quantityEquipment.setText("0");
        price_dj.setText("");
        extraSoundPrice.setText("");
        checkBoxSetUp.setChecked(false);
        checkBoxSoundDelivery.setChecked(false);
        checkBoxWireless.setChecked(false);
        spinner_soundEquipment.setSelection(0);
        spinner_package.setSelection(0);
        djPhoto.setImageResource(R.drawable.dj_icon);
        fetchServicesCategory();

    }
    private void clearInputFieldsCatering(){
        cateringDetailsTv.setVisibility(View.GONE);
        cateringRv.setVisibility(View.VISIBLE);
        seeAddCatering.setVisibility(View.VISIBLE);
        cateringClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        catering_company_name.setText("");
        no_of_people.setText("");
        cateringPackagePrice.setText("");
        catering_details.setText("");
        catering_transport.setText("");
        catering_cancel.setText("");
        checkBoxCateringSetUp.setChecked(false);
        checkBoxCateringStaff.setChecked(false);
        checkBoxCateringTheme.setChecked(false);
        checkBoxCateringEventManagement.setChecked(false);
        spinner_catering_package.setSelection(0);
        spinner_cuisine_type.setSelection(0);
        spinner_cateringServiceType.setSelection(0);
        tv_catering.setText("");
        cateringPhoto.setImageResource(R.drawable.fastfood_icon);
        // refresh the recycler view
        fetchServicesCategory();
    }
    private void clearInputFieldsInfluencer() {
        influencersDetailsTv.setVisibility(View.GONE);
        influencersRv.setVisibility(View.VISIBLE);
        seeAddInfluencers.setVisibility(View.VISIBLE);
        influencersClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        influencerHandle.setText("");
        subscriber_count.setText("");
        collaborationFee.setText("");
        eventCoverage.setChecked(false);
        spinnerSocialMedia.setSelection(0);
        spinnerAudienceAge.setSelection(0);
        spinnerAudienceGender.setSelection(0);
        spinnerAudienceLocation.setSelection(0);
        spinnerSocialMediaPackage.setSelection(0);
        spinnerContentType.setSelection(0);
        spinnerPostingSchedule.setSelection(0);
        spinnerContentTheme.setSelection(0);
        spinnerContentFreedom.setSelection(0);
        no_of_posts.setText("");
        influencerCancellationPolicy.setText("");
        carPhoto.setImageResource(R.drawable.car_icon);
        fetchServicesCategory();
    }
    private void clearInputFieldsSponsor() {
        sponsorDetailsTv.setVisibility(View.GONE);
        sponsorRv.setVisibility(View.VISIBLE);
        seeAddSponsor.setVisibility(View.VISIBLE);
        sponsorClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        sponsorName.setText("");
        brandingGuidelines.setText("");
        sponsorPreBooking.setText("");
        expectedAudience.setText("");
        sponsorCancellationPolicy.setText("");
        checkBoxDigitalPromotion.setChecked(false);
        spinnerSponsorshipType.setSelection(0);
        spinnerSponsorEventType.setSelection(0);
        sponsorAge.setSelection(0);
        sponsorIndustry.setSelection(0);
        sponsorInterests.setSelection(0);
        sponsorAmount.setText("");
        sponsorPhoto.setImageResource(R.drawable.sponsorship_icon);
        fetchServicesCategory();
    }
    private void clearInputFieldsDeco() {
        decorationDetailsTv.setVisibility(View.GONE);
        decorationRv.setVisibility(View.VISIBLE);
        seeAddDecoration.setVisibility(View.VISIBLE);
        decorationClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        decoName.setText("");
        decoDetails.setText("");
        decoCancellationPolicy.setText("");
        spinnerDecoPackage.setSelection(0);
        spinnerDecoTheme.setSelection(0);
        spinnerDecoEvent.setSelection(0);
        checkBoxDecoCustomization.setChecked(false);
        checkBoxDecoEmergency.setChecked(false);
        checkBoxDecoSetUp.setChecked(false);
        tvDecoBooking.setText("0");
        decorationPhoto.setImageResource(R.drawable.deco_icon);
        fetchServicesCategory();
    }
    private void clearInputFieldsMakeUp() {
        makeUpDetailsTv.setVisibility(View.GONE);
        makeUpRv.setVisibility(View.VISIBLE);
        seeAddMakeUp.setVisibility(View.VISIBLE);
        makeUpClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        makeUpName.setText("");
        makeUpSpecialization.setText("");
        spinnerMakeUpType.setSelection(0);
        makeUpPrice.setText("");
        makeUpLink.setText("");
        makeUpBooking.setText("");
        makeUpPolicy.setText("");
        makeUpTravel.setChecked(false);
        makeUpPhoto.setImageResource(R.drawable.makeup_icon);
    }
    private void clearInputFieldsMc() {
        mcDetailsTv.setVisibility(View.GONE);
        mcRv.setVisibility(View.VISIBLE);
        seeAddMc.setVisibility(View.VISIBLE);
        mcClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        spinnerMcSpecialization.setSelection(0);
        spinnerMcRole.setSelection(0);
        mcDuration.setText("");
        mcAudience.setText("");
        mcAmount.setText("");
        mcBooking.setText("");
        mcPolicy.setText("");
        mcEquipments.setChecked(false);

    }

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(addServices.this);
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