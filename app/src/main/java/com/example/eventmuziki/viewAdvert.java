package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.advertisementModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class viewAdvert extends AppCompatActivity {

    ImageView poster;
    ImageButton back;
    TextView eventNameTv, dateTv, timeTv, locationTv, detailsTv, organizerNameTv, planTv;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_advert);

        poster = findViewById(R.id.advertPoster);
        back = findViewById(R.id.back_arrow);
        eventNameTv = findViewById(R.id.eventNameTv);
        dateTv = findViewById(R.id.dateTv);
        timeTv = findViewById(R.id.timeTv);
        locationTv = findViewById(R.id.locationTv);
        detailsTv = findViewById(R.id.detailsTv);
        organizerNameTv = findViewById(R.id.organizerName);
        planTv = findViewById(R.id.placesTxt);
        updateBtn = findViewById(R.id.advertiseBtn);

        back.setOnClickListener(v -> finish());

        eventNameTv.setText(getIntent().getStringExtra("eventName"));
        dateTv.setText(getIntent().getStringExtra("eventDate"));
        timeTv.setText(getIntent().getStringExtra("eventTime"));
        locationTv.setText(getIntent().getStringExtra("eventLocation"));
        detailsTv.setText(getIntent().getStringExtra("eventDetails"));
        organizerNameTv.setText(getIntent().getStringExtra("organizerName"));
        planTv.setText(getIntent().getStringExtra("advertPlans"));

        String eventPoster = getIntent().getStringExtra("eventPoster");
        Glide.with(this).load(eventPoster).into(poster);


        // Get the advert ID from the Intent
        String advertId = getIntent().getStringExtra("advertId");

        // Fetch advert details from Firestore
        // fetchAdvertDetails(advertId);

    }

    private void fetchAdvertDetails(String advertId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("Advertisements")
                .document(advertId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            advertisementModel advert = document.toObject(advertisementModel.class);

                            // Set the advert details to the views
                            if (advert != null) {
                                eventNameTv.setText(advert.getEventName());
                                dateTv.setText(advert.getEventDate());
                                timeTv.setText(advert.getEventTime());
                                locationTv.setText(advert.getEventLocation());
                                detailsTv.setText(advert.getDetails());
                                organizerNameTv.setText(advert.getOrganizerName());
                                planTv.setText(advert.getAdvertPlans());

                                // Load poster image using Glide
                                Glide.with(this).load(advert.getPosterUrl()).into(poster);

                                String eventPoster = document.getString("eventPoster");

                                if (eventPoster != null && !eventPoster.isEmpty()) {
                                    Glide.with(viewAdvert.this)
                                            .load(eventPoster)
                                            .centerCrop()
                                            .placeholder(R.drawable.cover)
                                            .error(R.drawable.poster1)
                                            .into(poster);
                                }else {
                                    Toast.makeText(viewAdvert.this, "Failed to fetch event poster", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            // Handle the case when the document doesn't exist
                            Log.d("viewAdvert", "No such document");
                        }
                    } else {
                        // Handle the error
                        Log.e("viewAdvert", "Error fetching advert details", task.getException());
                    }
                });
    }
}