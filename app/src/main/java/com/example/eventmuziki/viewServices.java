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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class viewServices extends AppCompatActivity {

    ImageView carImage, soundImage, cateringImage, photographerImage, decorationImage, costumeImage, sponsorImage, influencerImage;

    TextView carModel, carType, carColor, carTransmission, carSeats, carStatus, carDriver, carPrice, carExtraPrice, carDetails,
            soundPackage, instrumentName, instrumentType, areaCoverage, soundHirePrice, soundExtraPrice, soundSetup, soundConnectivity, soundDelivery, soundStatus, soundDetails,
            cateringName, cateringPackaged, cateringCuisine, cateringService, cateringNumber,cateringPrice, cateringDetail, cateringBooking, cateringSetUp, cateringStaff, cateringTransport, coordinator,cateringTheme, cateringPolicy,cateringStatus,
            photoPackage, photographerNumbers, photoNumbers, photoFormat, photoProcessTime, photoDelivery, portfolio,photoPackagePrice, photoPackagePriceHourly, specialEquipment, photoStatus, photoAdvanceBooking,
            decoName, decoPackage, decoTheme, decoEvent, decoDetails, decoCustomization, decoContact, decoSetup, decoAmount, decoStatus, decoCancellationPolicy,
            sponsorName, sponsorType, sponsorAge, sponsorIndustry, sponsorInterests, sponsorEvent, sponsorPromotion, sponsorAmount, sponsorAudience, sponsorPreBooking, sponsorGuide, sponsorStatus, sponsorCancellation,
            influencerHandle ,influencerPlatform, influencerSubscribers, influencerAge,influencerGender, location , influencerPackage, influencerContent, influencerPosts,schedule ,theme ,freedom ,collaboration ,cancellation, coverage, status,
            costumeName, costumeGender, costumeAgeGroup, costumeDetails, costumeSize, productAmount, material, customization, cleaning, duration, buyPrice, hirePrice, lateFee, delivery, policy, costumeStatus;


    LinearLayout carRentalCardView, soundSystemCardView, cateringCardView, photographerCardView, decorationCardView, costumeCardView, sponsorCardView, influencerCardView;

    Button addCar, addSound, addCatering, addPhotography, addDecoration, addCostume, addSponsor, addInfluencer,
            editCar, editSound, editCatering, editPhotography, editDecoration, editCostume, editSponsor, editInfluencer;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    String userId;
    private Dialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_services);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        addCar = findViewById(R.id.addCar);
        addSound = findViewById(R.id.addSound);
        addCatering = findViewById(R.id.addCatering);
        addPhotography = findViewById(R.id.addPhotography);
        addDecoration = findViewById(R.id.addDecoration);
        addCostume = findViewById(R.id.addCostume);
        addSponsor = findViewById(R.id.addSponsor);
        addInfluencer = findViewById(R.id.addInfluencer);

        editCar = findViewById(R.id.editCar);
        editSound = findViewById(R.id.editSound);
        editCatering = findViewById(R.id.editCatering);
        editPhotography = findViewById(R.id.editPhotography);
        editDecoration = findViewById(R.id.editDecoration);
        editCostume = findViewById(R.id.editCostume);
        editSponsor = findViewById(R.id.editSponsor);
        editInfluencer = findViewById(R.id.editInfluencer);

        carRentalCardView = findViewById(R.id.carRentalCardView);
        soundSystemCardView = findViewById(R.id.soundSystemCardView);
        cateringCardView = findViewById(R.id.cateringCardView);
        photographerCardView = findViewById(R.id.photographyCardView);
        decorationCardView = findViewById(R.id.decorationCardView);
        costumeCardView = findViewById(R.id.costumeCardView);
        sponsorCardView = findViewById(R.id.sponsorCardView);
        influencerCardView = findViewById(R.id.influencerCardView);

        carImage = findViewById(R.id.carRentalImage);
        carModel = findViewById(R.id.carModel);
        carType = findViewById(R.id.carType);
        carColor = findViewById(R.id.carColor);
        carTransmission = findViewById(R.id.carTransmission);
        carSeats = findViewById(R.id.carSeats);
        carStatus = findViewById(R.id.carStatus);
        carDriver = findViewById(R.id.carDriver);
        carPrice = findViewById(R.id.carPrice);
        carExtraPrice = findViewById(R.id.carExtraPrice);
        carDetails = findViewById(R.id.carDetails);

        soundImage = findViewById(R.id.soundImage);
        soundPackage = findViewById(R.id.soundPackage);
        instrumentName = findViewById(R.id.instrumentName);
        instrumentType = findViewById(R.id.instrumentType);
        areaCoverage = findViewById(R.id.areaCoverage);
        soundHirePrice = findViewById(R.id.soundHirePrice);
        soundExtraPrice = findViewById(R.id.soundExtraPrice);
        soundSetup = findViewById(R.id.soundSetup);
        soundConnectivity = findViewById(R.id.soundConnectivity);
        soundDelivery = findViewById(R.id.soundDelivery);
        soundStatus = findViewById(R.id.soundStatus);
        soundDetails = findViewById(R.id.soundDetails);

        cateringImage = findViewById(R.id.cateringImage);
        cateringName = findViewById(R.id.cateringName);
        cateringPackaged = findViewById(R.id.cateringPackaged);
        cateringCuisine = findViewById(R.id.cateringCuisine);
        cateringService = findViewById(R.id.cateringService);
        cateringNumber = findViewById(R.id.cateringNumber);
        cateringPrice = findViewById(R.id.cateringPackagePrice);
        cateringDetail = findViewById(R.id.cateringDetail);
        cateringBooking = findViewById(R.id.cateringBooking);
        cateringSetUp = findViewById(R.id.cateringSetup);
        cateringStaff = findViewById(R.id.cateringStaff);
        cateringTransport = findViewById(R.id.cateringTransportationAmount);
        coordinator = findViewById(R.id.cateringCoordinator);
        cateringTheme = findViewById(R.id.cateringTheme);
        cateringPolicy = findViewById(R.id.cateringCancelPolicy);
        cateringStatus = findViewById(R.id.cateringStatus);

        photographerImage = findViewById(R.id.photographyImage);
        photoPackage = findViewById(R.id.photoPackageName);
        photographerNumbers = findViewById(R.id.noPhotographers);
        photoNumbers = findViewById(R.id.noPhotos);
        photoFormat = findViewById(R.id.photoFormat);
        photoProcessTime = findViewById(R.id.photoDeliveryTime);
        photoDelivery = findViewById(R.id.photoDelivery);
        portfolio = findViewById(R.id.photoPortfolioLink);
        photoPackagePrice = findViewById(R.id.photoPackagePrice);
        photoPackagePriceHourly = findViewById(R.id.photoPricePerHour);
        specialEquipment = findViewById(R.id.photoSpecialEquipment);
        photoStatus = findViewById(R.id.photoStatus);
        photoAdvanceBooking = findViewById(R.id.photoAdvancedBooking);

        decorationImage = findViewById(R.id.decorationImage);
        decoName = findViewById(R.id.decorationName);
        decoPackage = findViewById(R.id.decorationPackage);
        decoTheme = findViewById(R.id.decorationTheme);
        decoEvent = findViewById(R.id.decorationEvent);
        decoDetails = findViewById(R.id.decorationDetails);
        decoCustomization = findViewById(R.id.decorationCustomization);
        decoContact = findViewById(R.id.decorationEmergency);
        decoSetup = findViewById(R.id.decorationSetupTime);
        decoAmount = findViewById(R.id.decorationAmount);
        decoStatus = findViewById(R.id.decorationStatus);
        decoCancellationPolicy = findViewById(R.id.decorationCancellationPolicy);

        sponsorImage = findViewById(R.id.sponsorImage);
        sponsorName = findViewById(R.id.sponsorName);
        sponsorType = findViewById(R.id.sponsorType);
        sponsorAge = findViewById(R.id.sponsorAge);
        sponsorIndustry = findViewById(R.id.sponsorIndustry);
        sponsorInterests = findViewById(R.id.sponsorInterests);
        sponsorPromotion = findViewById(R.id.sponsorPromotion);
        sponsorAmount = findViewById(R.id.sponsorAmount);
        sponsorAudience = findViewById(R.id.sponsorAudience);
        sponsorPreBooking = findViewById(R.id.sponsorPreBooking);
        sponsorStatus = findViewById(R.id.sponsorStatus);
        sponsorGuide = findViewById(R.id.sponsorGuide);
        sponsorCancellation = findViewById(R.id.sponsorCancellationPolicy);
        sponsorEvent = findViewById(R.id.sponsorEvent);

        influencerImage = findViewById(R.id.influencerImage);
        influencerHandle = findViewById(R.id.influencerHandle);
        influencerPlatform = findViewById(R.id.influencerPlatform);
        influencerSubscribers = findViewById(R.id.influencerSubscribers);
        influencerAge = findViewById(R.id.influencerAge);
        influencerGender = findViewById(R.id.influencerGender);
        location = findViewById(R.id.influencerLocation);
        influencerPackage = findViewById(R.id.influencerPackage);
        influencerContent = findViewById(R.id.influencerContent);
        influencerPosts = findViewById(R.id.influencerPosts);
        schedule = findViewById(R.id.influencerSchedule);
        theme = findViewById(R.id.influencerTheme);
        freedom = findViewById(R.id.influencerFreedom);
        collaboration = findViewById(R.id.influencerCollaboration);
        cancellation = findViewById(R.id.influencerCancellation);
        coverage = findViewById(R.id.influencerCoverage);
        status = findViewById(R.id.influencerStatus);

        costumeImage = findViewById(R.id.costumeImage);
        costumeName = findViewById(R.id.costumeName);
        costumeGender = findViewById(R.id.costumeGender);
        costumeAgeGroup = findViewById(R.id.costumeAgeGroup);
        costumeDetails = findViewById(R.id.costumeDetails);
        costumeSize = findViewById(R.id.costumeSize);
        productAmount = findViewById(R.id.costumeProductAmount);
        material = findViewById(R.id.costumeMaterial);
        customization = findViewById(R.id.costumeCustomization);
        cleaning = findViewById(R.id.costumeCleaning);
        duration = findViewById(R.id.costumeDuration);
        buyPrice = findViewById(R.id.costumeBuyPrice);
        hirePrice = findViewById(R.id.costumeHirePrice);
        lateFee = findViewById(R.id.costumeLateFee);
        delivery = findViewById(R.id.costumeDelivery);
        policy = findViewById(R.id.costumePolicy);
        costumeStatus = findViewById(R.id.costumeStatus);

        // Get the car details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String serviceType = intent.getStringExtra("service");
            String productId = intent.getStringExtra("product");
            String creatorId = intent.getStringExtra("creatorId");

            fetchDetails(productId, creatorId);

            addCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = carModel.getText().toString();
                    String type = carType.getText().toString();
                    String price = carPrice.getText().toString();

                    ServicesDetails.cartModel carModel = new ServicesDetails.cartModel(name, type, price, creatorId, productId, userId, "Car Rental");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fStore.collection("Cart")
                                            .add(carModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                // Delay for 3 seconds before moving to the cartActivity
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e ->
                                                    Toast.makeText(viewServices.this, "Error adding car to cart", Toast.LENGTH_SHORT).show()
                                            );
                                }
                            });

                }
            });
            addSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = instrumentName.getText().toString();
                    String instrument = soundPackage.getText().toString();
                    String price = soundHirePrice.getText().toString();


                    ServicesDetails.cartModel soundModel = new ServicesDetails.cartModel(name, instrument, price, creatorId, productId, userId, "Sound System");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    fStore.collection("Cart")
                                            .add(soundModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);

                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding sound to cart", Toast.LENGTH_SHORT).show());

                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(viewServices.this, "Error checking cart", Toast.LENGTH_SHORT).show();
                            });
                }
            });
            addCatering.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = cateringName.getText().toString();
                    String packaged = cateringPackaged.getText().toString();
                    String price = cateringPrice.getText().toString();

                    ServicesDetails.cartModel cateringModel = new ServicesDetails.cartModel(name, packaged, price, creatorId, productId, userId, "Catering");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fStore.collection("Cart")
                                            .add(cateringModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding catering to cart", Toast.LENGTH_SHORT).show());
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(viewServices.this, "Error checking cart", Toast.LENGTH_SHORT).show();
                            });
                }
            });
            addPhotography.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = photoPackage.getText().toString();
                    String numbers = photoStatus.getText().toString();
                    String price = photoPackagePrice.getText().toString();

                    ServicesDetails.cartModel photographyModel = new ServicesDetails.cartModel(name, numbers, price, creatorId, productId, userId, "Photography");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    fStore.collection("Cart")
                                            .add(photographyModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);

                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding photography to cart", Toast.LENGTH_SHORT).show());
                                }
                            });

                }
            });
            addDecoration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = decoName.getText().toString();
                    String packaged = decoPackage.getText().toString();
                    String price = decoAmount.getText().toString();

                    ServicesDetails.cartModel decorationModel = new ServicesDetails.cartModel(name, packaged, price, creatorId, productId, userId, "Decoration");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fStore.collection("Cart")
                                            .add(decorationModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding decoration to cart", Toast.LENGTH_SHORT).show());

                                }
                            });

                }
            });
            addCostume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = costumeName.getText().toString();
                    String materials = material.getText().toString();
                    String price = hirePrice.getText().toString();

                    ServicesDetails.cartModel costumeModel = new ServicesDetails.cartModel(name, materials, price, creatorId, productId, userId, "Costume");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fStore.collection("Cart")
                                            .add(costumeModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding costume to cart", Toast.LENGTH_SHORT).show());
                                }
                            });

                }
            });
            addSponsor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = sponsorName.getText().toString();
                    String type = sponsorType.getText().toString();
                    String price = sponsorAmount.getText().toString();

                    ServicesDetails.cartModel sponsorModel = new ServicesDetails.cartModel(name, type, price, creatorId, productId, userId, "Sponsor");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fStore.collection("Cart")
                                            .add(sponsorModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding sponsor to cart", Toast.LENGTH_SHORT).show());
                                }
                            });


                }
            });
            addInfluencer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = influencerHandle.getText().toString();
                    String platform = influencerPlatform.getText().toString();
                    String price = collaboration.getText().toString();

                    ServicesDetails.cartModel influencerModel = new ServicesDetails.cartModel(name, platform, price, creatorId, productId, userId, "Influencer");
                    fStore.collection("Cart")
                            .whereEqualTo("productId", productId)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(viewServices.this, "Product already in cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    fStore.collection("Cart")
                                            .add(influencerModel)
                                            .addOnSuccessListener(documentReference -> {
                                                showPopupDialog(view);
                                                new Handler().postDelayed(() -> {
                                                    Intent intent = new Intent(viewServices.this, cartActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }, 3000);
                                            }).addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error adding influencer to cart", Toast.LENGTH_SHORT).show());
                                }
                            });

                }
            });

            // Display the correct card view based on service type
            if (serviceType != null) {
                switch (serviceType) {
                    case "Car Rental":
                        carRentalCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(carRentalCardView);
                        break;
                    case "Sound":
                        soundSystemCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(soundSystemCardView);
                        break;
                    case "Catering":
                        cateringCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(cateringCardView);
                        break;
                    case "Photographer":
                        photographerCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(photographerCardView);
                        break;
                    case "Decorations":
                        decorationCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(decorationCardView);
                        break;
                    case "Costumes":
                        costumeCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(costumeCardView);
                        break;
                    case "Sponsors":
                        sponsorCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(sponsorCardView);
                        break;
                    case "Influencer":
                        influencerCardView.setVisibility(View.VISIBLE);
                        hideOtherCardViews(influencerCardView);
                        break;
                    default:
                        Toast.makeText(this, "Unknown service type", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(this, "Failed to get service details", Toast.LENGTH_SHORT).show();
            }

            // Set up toolbar
            Toolbar toolbar = findViewById(R.id.top_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            findViewById(R.id.back_arrow).setOnClickListener(v -> {
                finish();
            });


        }


    }


    private void hideOtherCardViews(View visibleCardView) {
            // Hide all card views except the one passed as the argument
            carRentalCardView.setVisibility(visibleCardView == carRentalCardView ? View.VISIBLE : View.GONE);
            soundSystemCardView.setVisibility(visibleCardView == soundSystemCardView ? View.VISIBLE : View.GONE);
            cateringCardView.setVisibility(visibleCardView == cateringCardView ? View.VISIBLE : View.GONE);
            photographerCardView.setVisibility(visibleCardView == photographerCardView ? View.VISIBLE : View.GONE);
            decorationCardView.setVisibility(visibleCardView == decorationCardView ? View.VISIBLE : View.GONE);
            costumeCardView.setVisibility(visibleCardView == costumeCardView ? View.VISIBLE : View.GONE);
            sponsorCardView.setVisibility(visibleCardView == sponsorCardView ? View.VISIBLE : View.GONE);
            influencerCardView.setVisibility(visibleCardView == influencerCardView ? View.VISIBLE : View.GONE);
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

    private void fetchDetails(String productId, String creatorId) {
        if (productId == null || productId.isEmpty()) {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show();
            return;
        }
        fStore.collection("Services")
                .whereEqualTo("creatorId", creatorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String serviceId = document.getId();
                            String serviceCategory = document.getString("category");

                            // Now fetch the specific document from Products subcollection
                            fStore.collection("Services")
                                    .document(serviceId)
                                    .collection("Products")
                                    .document(productId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Costumes")) {
                                            // Fetch and display product details
                                            costumeName.setText(documentSnapshot.getString("costumeName"));
                                            costumeGender.setText(documentSnapshot.getString("gender"));
                                            costumeAgeGroup.setText(documentSnapshot.getString("ageGroup"));
                                            costumeDetails.setText(documentSnapshot.getString("details"));
                                            costumeSize.setText(documentSnapshot.getString("size"));
                                            productAmount.setText(documentSnapshot.getString("productAmount"));
                                            material.setText(documentSnapshot.getString("material"));
                                            customization.setText(documentSnapshot.getString("customization"));
                                            cleaning.setText(documentSnapshot.getString("cleaning"));
                                            duration.setText(documentSnapshot.getString("duration"));
                                            buyPrice.setText(documentSnapshot.getString("buyPrice"));
                                            hirePrice.setText(documentSnapshot.getString("hirePrice"));
                                            lateFee.setText(documentSnapshot.getString("lateFee"));
                                            delivery.setText(documentSnapshot.getString("delivery"));
                                            policy.setText(documentSnapshot.getString("policy"));
                                            costumeStatus.setText(documentSnapshot.getString("status"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.costume_icon)
                                                        .error(R.drawable.costume_icon)
                                                        .into(costumeImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addCostume.setVisibility(View.GONE);
                                                editCostume.setVisibility(View.VISIBLE);
                                            }else {
                                                addCostume.setVisibility(View.VISIBLE);
                                                editCostume.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Influencers")) {
                                            // Fetch and display product details
                                            influencerHandle.setText(documentSnapshot.getString("handle"));
                                            influencerPlatform.setText(documentSnapshot.getString("platform"));
                                            influencerSubscribers.setText(documentSnapshot.getString("subscribers"));
                                            influencerAge.setText(documentSnapshot.getString("age"));
                                            influencerGender.setText(documentSnapshot.getString("gender"));
                                            location.setText(documentSnapshot.getString("location"));
                                            influencerPackage.setText(documentSnapshot.getString("package"));
                                            influencerContent.setText(documentSnapshot.getString("content"));
                                            influencerPosts.setText(documentSnapshot.getString("posts"));
                                            schedule.setText(documentSnapshot.getString("schedule"));
                                            theme.setText(documentSnapshot.getString("theme"));
                                            freedom.setText(documentSnapshot.getString("freedom"));
                                            collaboration.setText(documentSnapshot.getString("collaboration"));
                                            cancellation.setText(documentSnapshot.getString("cancellation"));
                                            coverage.setText(documentSnapshot.getString("coverage"));
                                            status.setText(documentSnapshot.getString("status"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.content_icon)
                                                        .error(R.drawable.content_icon)
                                                        .into(influencerImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addInfluencer.setVisibility(View.GONE);
                                                editInfluencer.setVisibility(View.VISIBLE);
                                            }else {
                                                addInfluencer.setVisibility(View.VISIBLE);
                                                editInfluencer.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Sponsors")) {
                                            // Fetch and display product details
                                            sponsorName.setText(documentSnapshot.getString("name"));
                                            sponsorType.setText(documentSnapshot.getString("type"));
                                            sponsorAge.setText(documentSnapshot.getString("age"));
                                            sponsorIndustry.setText(documentSnapshot.getString("industry"));
                                            sponsorInterests.setText(documentSnapshot.getString("interests"));
                                            sponsorPromotion.setText(documentSnapshot.getString("promotion"));
                                            sponsorAmount.setText(documentSnapshot.getString("amount"));
                                            sponsorAudience.setText(documentSnapshot.getString("audience"));
                                            sponsorPreBooking.setText(documentSnapshot.getString("preBooking"));
                                            sponsorGuide.setText(documentSnapshot.getString("guide"));
                                            sponsorStatus.setText(documentSnapshot.getString("status"));
                                            sponsorCancellation.setText(documentSnapshot.getString("cancellation"));
                                            sponsorEvent.setText(documentSnapshot.getString("event"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.sponsorship_icon)
                                                        .error(R.drawable.sponsorship_icon)
                                                        .into(sponsorImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addSponsor.setVisibility(View.GONE);
                                                editSponsor.setVisibility(View.VISIBLE);
                                            }else {
                                                addSponsor.setVisibility(View.VISIBLE);
                                                editSponsor.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Decorations")) {
                                            // Fetch and display product details
                                            decoName.setText(documentSnapshot.getString("name"));
                                            decoPackage.setText(documentSnapshot.getString("package"));
                                            decoTheme.setText(documentSnapshot.getString("theme"));
                                            decoEvent.setText(documentSnapshot.getString("event"));
                                            decoDetails.setText(documentSnapshot.getString("details"));
                                            decoCustomization.setText(documentSnapshot.getString("customization"));
                                            decoContact.setText(documentSnapshot.getString("emergency"));
                                            decoSetup.setText(documentSnapshot.getString("setUp"));
                                            decoAmount.setText(documentSnapshot.getString("amount"));
                                            decoStatus.setText(documentSnapshot.getString("status"));
                                            decoCancellationPolicy.setText(documentSnapshot.getString("cancellation"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.deco_icon)
                                                        .error(R.drawable.deco_icon)
                                                        .into(decorationImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addDecoration.setVisibility(View.GONE);
                                                editDecoration.setVisibility(View.VISIBLE);
                                            }else {
                                                addDecoration.setVisibility(View.VISIBLE);
                                                editDecoration.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Photographer")) {
                                            // Fetch and display product details
                                            photoPackage.setText(documentSnapshot.getString("packageName"));
                                            photographerNumbers.setText(documentSnapshot.getString("noPhotographers"));
                                            photoNumbers.setText(documentSnapshot.getString("noPhotos"));
                                            photoFormat.setText(documentSnapshot.getString("format"));
                                            photoProcessTime.setText(documentSnapshot.getString("deliveryTime"));
                                            photoDelivery.setText(documentSnapshot.getString("delivery"));
                                            portfolio.setText(documentSnapshot.getString("portfolioLink"));
                                            photoPackagePrice.setText(documentSnapshot.getString("pricePerHour"));
                                            photoPackagePriceHourly.setText(documentSnapshot.getString("photoExtraPrice"));
                                            photoAdvanceBooking.setText(documentSnapshot.getString("photoAdvancedBooking"));
                                            specialEquipment.setText(documentSnapshot.getString("preShoot"));
                                            photoStatus.setText(documentSnapshot.getString("status"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.camera_icon)
                                                        .error(R.drawable.camera_icon)
                                                        .into(photographerImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addPhotography.setVisibility(View.GONE);
                                                editPhotography.setVisibility(View.VISIBLE);
                                            }else {
                                                addPhotography.setVisibility(View.VISIBLE);
                                                editPhotography.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Catering")) {
                                            // Fetch and display product details
                                            cateringName.setText(documentSnapshot.getString("name"));
                                            cateringPackaged.setText(documentSnapshot.getString("packaged"));
                                            cateringCuisine.setText(documentSnapshot.getString("cuisine"));
                                            cateringService.setText(documentSnapshot.getString("service"));
                                            cateringNumber.setText(documentSnapshot.getString("number"));
                                            cateringPrice.setText(documentSnapshot.getString("packagePrice"));
                                            cateringDetail.setText(documentSnapshot.getString("detail"));
                                            cateringBooking.setText(documentSnapshot.getString("booking"));
                                            cateringSetUp.setText(documentSnapshot.getString("setup"));
                                            cateringStaff.setText(documentSnapshot.getString("staff"));
                                            cateringTransport.setText(documentSnapshot.getString("transportation"));
                                            coordinator.setText(documentSnapshot.getString("coordinator"));
                                            cateringTheme.setText(documentSnapshot.getString("theme"));
                                            cateringPolicy.setText(documentSnapshot.getString("cancelPolicy"));
                                            cateringStatus.setText(documentSnapshot.getString("status"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.fastfood_icon)
                                                        .error(R.drawable.fastfood_icon)
                                                        .into(cateringImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addCatering.setVisibility(View.GONE);
                                                editCatering.setVisibility(View.VISIBLE);
                                            }else {
                                                addCatering.setVisibility(View.VISIBLE);
                                                editCatering.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Sound")) {
                                            // Fetch and display product details
                                            soundPackage.setText(documentSnapshot.getString("packaged"));
                                            instrumentName.setText(documentSnapshot.getString("name"));
                                            instrumentType.setText(documentSnapshot.getString("type"));
                                            areaCoverage.setText(documentSnapshot.getString("area"));
                                            soundHirePrice.setText(documentSnapshot.getString("price"));
                                            soundExtraPrice.setText(documentSnapshot.getString("extraPrice"));
                                            soundSetup.setText(documentSnapshot.getString("setup"));
                                            soundConnectivity.setText(documentSnapshot.getString("wireless"));
                                            soundDelivery.setText(documentSnapshot.getString("delivery"));
                                            soundStatus.setText(documentSnapshot.getString("status"));
                                            soundDetails.setText(documentSnapshot.getString("details"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.dj_icon)
                                                        .error(R.drawable.dj_icon)
                                                        .into(soundImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addSound.setVisibility(View.GONE);
                                                editSound.setVisibility(View.VISIBLE);
                                            }else {
                                                addSound.setVisibility(View.VISIBLE);
                                                editSound.setVisibility(View.GONE);
                                            }

                                        }

                                        if (documentSnapshot.exists() && serviceCategory != null && serviceCategory.equalsIgnoreCase("Car Rental")) {
                                            // Fetch and display product details
                                            carModel.setText(documentSnapshot.getString("car_model"));
                                            carType.setText(documentSnapshot.getString("car_type"));
                                            carColor.setText(documentSnapshot.getString("car_color"));
                                            carTransmission.setText(documentSnapshot.getString("transmission"));
                                            carSeats.setText(documentSnapshot.getString("seats"));
                                            carStatus.setText(documentSnapshot.getString("status"));
                                            carDriver.setText(documentSnapshot.getString("driverProvided"));
                                            carPrice.setText(documentSnapshot.getString("price_per_hour"));
                                            carExtraPrice.setText(documentSnapshot.getString("price_per_extra_hour"));
                                            carDetails.setText(documentSnapshot.getString("car_details"));

                                            String imageUrl = documentSnapshot.getString("image");
                                            if (imageUrl != null) {
                                                Glide.with(viewServices.this)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.car_icon)
                                                        .error(R.drawable.car_icon)
                                                        .into(carImage);
                                            } else {
                                                Log.e("Image URL", "Image URL is null");
                                            }
                                            String userId = documentSnapshot.getString("creatorId");
                                            if (userId != null && userId.equalsIgnoreCase(fAuth.getCurrentUser().getUid())) {
                                                addCar.setVisibility(View.GONE);
                                                editCar.setVisibility(View.VISIBLE);
                                            }else {
                                                addCar.setVisibility(View.VISIBLE);
                                                editCar.setVisibility(View.GONE);
                                            }


                                        }

                                        else {
                                            Toast.makeText(viewServices.this, "No details found for this item", Toast.LENGTH_SHORT).show();
                                        }

                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(viewServices.this, "Error fetching product details", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(viewServices.this, "No service found for this category", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(viewServices.this, "Error fetching services", Toast.LENGTH_SHORT).show());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupDialog != null && popupDialog.isShowing()) {
            popupDialog.dismiss();
        }
    }

}