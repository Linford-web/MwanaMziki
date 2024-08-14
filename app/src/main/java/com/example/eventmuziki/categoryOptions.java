package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class categoryOptions extends AppCompatActivity {

    ImageButton backArrow;
    Button addServiceBtn;

    LinearLayout carRental, photography, catering, costumes, paSystem, decoration, contentCreators, sponsorship,
            carRentalDetails, photographyDetails, cateringDetails,
            costumesDetails, paSystemDetails, decorationDetails, contentCreatorsDetails, sponsorshipDetails,addService;

    RecyclerView carRentalRv, photographyRentalRv, cateringRv, costumesRv, DjRv;

    TextView carTxt, photographyTxt, cateringTxt, costumesTxt, paSystemTxt, decoTxt, contentTxt, sponsorshipTxt;
    ImageView carIcon, photographyIcon, cateringIcon, costumesIcon, paSystemIcon, decoIcon, contentIcon, sponsorshipIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_options);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

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
}