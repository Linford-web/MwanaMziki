package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cateringAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.costumeAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.decoAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.djAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.influencerAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.photoAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.serviceDetailAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.sponsorAdapter;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class categoryOptions extends AppCompatActivity {

    ImageButton backArrow;
    Button addServiceBtn;

    LinearLayout carRental, photography, catering, costumes, paSystem, decoration, contentCreators, sponsorship,
            carRentalDetails, photographyDetails, cateringDetails,
            costumesDetails, paSystemDetails, decorationDetails, contentCreatorsDetails, sponsorshipDetails,addService;

    RecyclerView carRentalRv, photographyRentalRv, cateringRv, costumesRv, DjRv, decorationRv, contentCreatorsRv, sponsorshipRv, serviceRv;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    ArrayList<ServicesDetails.serviceDetail> serviceUserList = new ArrayList<>();
    serviceDetailAdapter userAdapter;

    ArrayList<ServicesDetails.carModel> carList = new ArrayList<>();
    carAdapter cars;
    ArrayList<ServicesDetails.cateringModel> cateringList = new ArrayList<>();
    cateringAdapter caterings;
    ArrayList<ServicesDetails.photoModel> photographerList = new ArrayList<>();
    photoAdapter adapterPhoto;
    ArrayList<ServicesDetails.soundModel> djList = new ArrayList<>();
    djAdapter adapterSound;
    ArrayList<ServicesDetails.decorationModel> decorationList = new ArrayList<>();
    decoAdapter adapterDecoration;
    ArrayList<ServicesDetails.influencerModel> contentList = new ArrayList<>();
    influencerAdapter adapterContent;
    ArrayList<ServicesDetails.sponsorModel> sponsorshipList = new ArrayList<>();
    sponsorAdapter adapterSponsorship;
    ArrayList<ServicesDetails.costumeModel> costumeList = new ArrayList<>();
    costumeAdapter costumeAdapters;

    TextView carTxt, photographyTxt, cateringTxt, costumesTxt, paSystemTxt, decoTxt, contentTxt, sponsorshipTxt, serviceTxt;
    ImageView carIcon, photographyIcon, cateringIcon, costumesIcon, paSystemIcon, decoIcon, contentIcon, sponsorshipIcon;

    BottomNavigationView bottomNavigationView;

    String car ="Car Rental", photo ="Photographer", food ="Catering", costume ="Costumes", pa ="Sound", decorations ="Decorations", content ="Influencers", sponsorships ="Sponsors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_options);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        backArrow = findViewById(R.id.back_arrow);
        carRental = findViewById(R.id.carRental);
        photography = findViewById(R.id.photography);
        catering = findViewById(R.id.catering);
        costumes = findViewById(R.id.costumes);
        paSystem = findViewById(R.id.paSystem);
        decoration = findViewById(R.id.decorations);
        contentCreators = findViewById(R.id.contentCreator);
        sponsorship = findViewById(R.id.sponsorship);

        carRentalDetails = findViewById(R.id.carRentalDetails);
        photographyDetails = findViewById(R.id.photographyDetails);
        cateringDetails = findViewById(R.id.cateringDetails);
        costumesDetails = findViewById(R.id.costumesDetails);
        paSystemDetails = findViewById(R.id.paSystemDetails);
        decorationDetails = findViewById(R.id.decorationDetails);
        contentCreatorsDetails = findViewById(R.id.contentCreatorsDetails);
        sponsorshipDetails = findViewById(R.id.sponsorshipDetails);

        carTxt = findViewById(R.id.carTxt);
        photographyTxt = findViewById(R.id.photographyTxt);
        cateringTxt = findViewById(R.id.cateringTxt);
        costumesTxt = findViewById(R.id.costumeTxt);
        paSystemTxt = findViewById(R.id.paSystemTxt);
        decoTxt = findViewById(R.id.decoTxt);
        contentTxt = findViewById(R.id.contentTxt);
        sponsorshipTxt = findViewById(R.id.sponsorshipTxt);
        serviceTxt = findViewById(R.id.serviceTxt);

        carIcon = findViewById(R.id.carIcon);
        photographyIcon = findViewById(R.id.photographyIcon);
        cateringIcon = findViewById(R.id.cateringIcon);
        costumesIcon = findViewById(R.id.costumeIcon);
        paSystemIcon = findViewById(R.id.paSystemIcon);
        decoIcon = findViewById(R.id.decoIcon);
        contentIcon = findViewById(R.id.contentIcon);
        sponsorshipIcon = findViewById(R.id.sponsorshipIcon);

        addService = findViewById(R.id.addService);
        addServiceBtn = findViewById(R.id.addServiceBtn);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.services);
        serviceRv = findViewById(R.id.serviceRv);
        carRentalRv = findViewById(R.id.carRentalRv);
        photographyRentalRv = findViewById(R.id.photographyRentalRv);
        cateringRv = findViewById(R.id.cateringRv);
        costumesRv = findViewById(R.id.costumesRv);
        DjRv = findViewById(R.id.DjRv);
        decorationRv = findViewById(R.id.decoRv);
        contentCreatorsRv = findViewById(R.id.contentCreatorsRv);
        sponsorshipRv = findViewById(R.id.sponsorshipRv);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        backArrow.setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.cartBtn).setOnClickListener(v -> {
            Intent intent = new Intent(categoryOptions.this, cartActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (view, insets) -> {
            view.setPadding(0, 0, 0,0);
            return insets;
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    return true;
                } else if (itemId == R.id.services) {
                    return true;
                }else if (itemId == R.id.search){
                    // Handle other menu items if needed
                }
                return false;
            }
        });

        serviceUserList = new ArrayList<>();
        userAdapter = new serviceDetailAdapter(serviceUserList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        serviceRv.setLayoutManager(layoutManager);
        serviceRv.setAdapter(userAdapter);

        carList = new ArrayList<>();
        cars = new carAdapter(carList, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        carRentalRv.setLayoutManager(layoutManager1);
        carRentalRv.setAdapter(cars);

        cateringList = new ArrayList<>();
        caterings = new cateringAdapter(cateringList, this);
        RecyclerView.LayoutManager grid = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cateringRv.setLayoutManager(grid);
        cateringRv.setAdapter(caterings);

        photographerList = new ArrayList<>();
        adapterPhoto = new photoAdapter(photographerList, this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        photographyRentalRv.setLayoutManager(layoutManager3);
        photographyRentalRv.setAdapter(adapterPhoto);

        djList = new ArrayList<>();
        adapterSound = new djAdapter(djList, this);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DjRv.setLayoutManager(layoutManager4);
        DjRv.setAdapter(adapterSound);

        contentList = new ArrayList<>();
        adapterContent = new influencerAdapter(contentList, this);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contentCreatorsRv.setLayoutManager(layoutManager5);
        contentCreatorsRv.setAdapter(adapterContent);

        decorationList = new ArrayList<>();
        adapterDecoration = new decoAdapter(decorationList, this);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        decorationRv.setLayoutManager(layoutManager6);
        decorationRv.setAdapter(adapterDecoration);

        sponsorshipList = new ArrayList<>();
        adapterSponsorship = new sponsorAdapter(sponsorshipList, this);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sponsorshipRv.setLayoutManager(layoutManager7);
        sponsorshipRv.setAdapter(adapterSponsorship);

        costumeList = new ArrayList<>();
        costumeAdapters = new costumeAdapter(costumeList, this);
        RecyclerView.LayoutManager layoutManager8 = new GridLayoutManager(this, 2);
        costumesRv.setLayoutManager(layoutManager8);
        costumesRv.setAdapter(costumeAdapters);

        carRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set text and icon color to white
                carTxt.setTextColor(getResources().getColor(R.color.orange));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.orange));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                carRentalDetails.setVisibility(View.VISIBLE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(car);

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
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.orange));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.VISIBLE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(photo);
                fetchServices(photo);

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
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.orange));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.VISIBLE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(food);
                fetchServices(food);
                //fetchProducts(food);
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
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.orange));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.VISIBLE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(costume);
                fetchServices(costume);
                //fetchProducts(costume);
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
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                paSystemIcon.setColorFilter(getResources().getColor(R.color.orange));
                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.VISIBLE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(pa);
                fetchServices(pa);
                //fetchProducts(pa);
            }
        });
        decoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decoTxt.setTextColor(getResources().getColor(R.color.orange));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.orange));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decorationDetails.setVisibility(View.VISIBLE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(decorations);
                fetchServices(decorations);
                //fetchProducts(decorations);
            }
        });
        contentCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.orange));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.black));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.orange));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.black));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.VISIBLE);
                sponsorshipDetails.setVisibility(View.GONE);
                serviceTxt.setText(content);
                fetchServices(content);
                //fetchProducts(content);

            }
        });
        sponsorship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carTxt.setTextColor(getResources().getColor(R.color.black));
                photographyTxt.setTextColor(getResources().getColor(R.color.black));
                cateringTxt.setTextColor(getResources().getColor(R.color.black));
                costumesTxt.setTextColor(getResources().getColor(R.color.black));
                paSystemTxt.setTextColor(getResources().getColor(R.color.black));
                decoTxt.setTextColor(getResources().getColor(R.color.black));
                contentTxt.setTextColor(getResources().getColor(R.color.black));
                sponsorshipTxt.setTextColor(getResources().getColor(R.color.orange));

                carIcon.setColorFilter(getResources().getColor(R.color.black));
                photographyIcon.setColorFilter(getResources().getColor(R.color.black));
                cateringIcon.setColorFilter(getResources().getColor(R.color.black));
                costumesIcon.setColorFilter(getResources().getColor(R.color.black));
                paSystemIcon.setColorFilter(getResources().getColor(R.color.black));
                decoIcon.setColorFilter(getResources().getColor(R.color.black));
                contentIcon.setColorFilter(getResources().getColor(R.color.black));
                sponsorshipIcon.setColorFilter(getResources().getColor(R.color.orange));

                carRentalDetails.setVisibility(View.GONE);
                photographyDetails.setVisibility(View.GONE);
                cateringDetails.setVisibility(View.GONE);
                costumesDetails.setVisibility(View.GONE);
                paSystemDetails.setVisibility(View.GONE);
                decorationDetails.setVisibility(View.GONE);
                contentCreatorsDetails.setVisibility(View.GONE);
                sponsorshipDetails.setVisibility(View.VISIBLE);
                serviceTxt.setText(sponsorships);
                fetchServices(sponsorships);

            }
        });

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categoryOptions.this, addServices.class);
                startActivity(intent);
            }
        });

        checkUserAccessLevel();
        fetchProducts(car);
        fetchProducts(photo);
        fetchProducts(food);
        fetchProducts(costume);
        fetchProducts(pa);
        fetchProducts(decorations);
        fetchProducts(content);
        fetchServices(car);

    }

    private void fetchProducts(String string) {
        // Clear lists outside the loop to avoid clearing them multiple times
        carList.clear();
        photographerList.clear();
        cateringList.clear();
        costumeList.clear();
        djList.clear();
        decorationList.clear();
        contentList.clear();
        sponsorshipList.clear();

        fStore.collection("Services")
                .whereEqualTo("category", string)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Use a counter to keep track of how many fetches are completed
                        int[] remainingFetches = {queryDocumentSnapshots.size()};

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String serviceId = document.getId();
                            String serviceCategory = document.getString("category");

                            fStore.collection("Services")
                                    .document(serviceId)
                                    .collection("Products")
                                    .get()
                                    .addOnSuccessListener(productsSnapshot -> {
                                        if (!productsSnapshot.isEmpty()) {
                                            for (DocumentSnapshot productDoc : productsSnapshot) {
                                                switch (serviceCategory) {
                                                    case "Car Rental":
                                                        ServicesDetails.carModel carProduct = productDoc.toObject(ServicesDetails.carModel.class);
                                                        carList.add(carProduct);
                                                        break;
                                                    case "Photographer":
                                                        ServicesDetails.photoModel photoProduct = productDoc.toObject(ServicesDetails.photoModel.class);
                                                        photographerList.add(photoProduct);
                                                        break;
                                                    case "Catering":
                                                        ServicesDetails.cateringModel cateringProduct = productDoc.toObject(ServicesDetails.cateringModel.class);
                                                        cateringList.add(cateringProduct);
                                                        break;
                                                    case "Costumes":
                                                        ServicesDetails.costumeModel costumeProduct = productDoc.toObject(ServicesDetails.costumeModel.class);
                                                        costumeList.add(costumeProduct);
                                                        break;
                                                    case "Sound":
                                                        ServicesDetails.soundModel soundProduct = productDoc.toObject(ServicesDetails.soundModel.class);
                                                        djList.add(soundProduct);
                                                        break;
                                                    case "Decorations":
                                                        ServicesDetails.decorationModel decorationProduct = productDoc.toObject(ServicesDetails.decorationModel.class);
                                                        decorationList.add(decorationProduct);
                                                        break;
                                                    case "Influencers":
                                                        ServicesDetails.influencerModel influencerProduct = productDoc.toObject(ServicesDetails.influencerModel.class);
                                                        contentList.add(influencerProduct);
                                                        break;
                                                    case "Sponsors":
                                                        ServicesDetails.sponsorModel sponsorProduct = productDoc.toObject(ServicesDetails.sponsorModel.class);
                                                        sponsorshipList.add(sponsorProduct);
                                                        break;
                                                    default:
                                                        // Handle any unexpected category
                                                        break;
                                                }
                                            }
                                        }

                                        // Decrement the counter and notify the adapters when all fetches are completed
                                        remainingFetches[0]--;
                                        if (remainingFetches[0] == 0) {
                                            notifyAllAdapters();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors in product fetching
                                        Toast.makeText(categoryOptions.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(categoryOptions.this, "No products found for this category", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(categoryOptions.this, "Error fetching services", Toast.LENGTH_SHORT).show();
                });
    }

    // Notify all adapters method
    private void notifyAllAdapters() {
        cars.notifyDataSetChanged();
        adapterPhoto.notifyDataSetChanged();
        caterings.notifyDataSetChanged();
        costumeAdapters.notifyDataSetChanged();
        adapterSound.notifyDataSetChanged();
        adapterDecoration.notifyDataSetChanged();
        adapterContent.notifyDataSetChanged();
        adapterSponsorship.notifyDataSetChanged();
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
                                addService.setVisibility(View.GONE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                addService.setVisibility(View.VISIBLE);
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

    void fetchServices(String category) {
        // Fetch services from Firestore and update the adapter
        fStore.collection("Services")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            serviceUserList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                ServicesDetails.serviceDetail service = document.toObject(ServicesDetails.serviceDetail.class);
                                serviceUserList.add(service);
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error getting documents: ", e);
                    }
                });

    }

}