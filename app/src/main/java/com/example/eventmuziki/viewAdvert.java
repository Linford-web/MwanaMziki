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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Adapters.advertAdapter;
import com.example.eventmuziki.Adapters.advertPosterAdapter;
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.Models.eventAdvertModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class viewAdvert extends AppCompatActivity {

    ImageView poster;
    ImageButton back;
    TextView advertTitle, advertDuration, dateTv, timeTv, detailsTv;
    Button deleteBtn;
    RecyclerView advertWalls, eventAdvertised;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ArrayList<AdvertisementDetails.posterModel> advertList;
    advertPosterAdapter adapter;

    ArrayList<eventAdvertModel> eventList;
    advertAdapter eventAdapter;
    LinearLayout emptyTv, emptyTvs, eventTv;

    Dialog popupDialog;
    String title, duration, date, time, details, image, advertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_advert);

        poster = findViewById(R.id.advertPoster);
        back = findViewById(R.id.back_arrow);
        advertTitle = findViewById(R.id.titleTv);
        advertDuration = findViewById(R.id.advertDuration);
        dateTv = findViewById(R.id.dateTv);
        timeTv = findViewById(R.id.timeTv);
        detailsTv = findViewById(R.id.detailsTv);
        deleteBtn = findViewById(R.id.deleteBtn);
        advertWalls = findViewById(R.id.advertWalls);
        emptyTv = findViewById(R.id.emptyTv);
        emptyTvs = findViewById(R.id.emptyTvs);
        eventTv = findViewById(R.id.eventsAdvertisementTv);
        eventAdvertised = findViewById(R.id.eventsAdvertisedRv);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(viewAdvert.this, advertActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        emptyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAdvert.this, addAdverts.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            duration = intent.getStringExtra("duration");
            date = intent.getStringExtra("date");
            time = intent.getStringExtra("time");
            details = intent.getStringExtra("details");
            image = intent.getStringExtra("image");
            advertId = intent.getStringExtra("advertId");

        }else {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        advertTitle.setText(title);
        advertDuration.setText(duration);
        dateTv.setText(date);
        timeTv.setText(time);
        detailsTv.setText(details);

        String eventPoster = getIntent().getStringExtra("image");
        if (eventPoster != null ){
            Glide.with(this)
                    .load(eventPoster)
                    .placeholder(R.drawable.cover)
                    .error(R.drawable.cover)
                    .into(poster);
        }else {
            poster.setImageResource(R.drawable.cover);
            Log.d("eventPoster", "Event poster is null");
        }


        advertList = new ArrayList<>();
        adapter = new advertPosterAdapter(advertList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        advertWalls.setLayoutManager(layoutManager);
        advertWalls.setAdapter(adapter);

        eventList = new ArrayList<>();
        eventAdapter = new advertAdapter(eventList);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        eventAdvertised.setLayoutManager(layoutManager1);
        eventAdvertised.setAdapter(eventAdapter);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAdvertisementAndSubcollections(advertId);
            }
        });

        fetchAdvertPlaces(advertId);
        fetchEventDetails();
        checkUserAccessLevel();


    }

    private void deleteAdvertisementAndSubcollections(String documentId) {

        // Reference to the main Advertisements document
        fStore.collection("Advertisements")
                .document(documentId)
                .collection("AdvertPlatforms")
                .get()
                .addOnSuccessListener(advertPlatformsSnapshots -> {
                    // Delete each document in the AdvertPlatforms subcollection
                    for (DocumentSnapshot platformDoc : advertPlatformsSnapshots) {
                        String platformId = platformDoc.getId();
                        fStore.collection("Advertisements")
                                .document(documentId)
                                .collection("AdvertPlatforms")
                                .document(platformId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Successfully deleted a platform document
                                    Log.d("TAG", "Platform subcollection deleted successfully");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to delete platform: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                    // After deleting AdvertPlatforms, proceed to delete EventAdvertisements subcollection
                    fStore.collection("Advertisements")
                            .document(documentId)
                            .collection("EventAdvertisements")
                            .get()
                            .addOnSuccessListener(eventAdvertisementsSnapshots -> {
                                // Delete each document in the EventAdvertisements subcollection
                                for (DocumentSnapshot eventDoc : eventAdvertisementsSnapshots) {
                                    String eventId = eventDoc.getId();
                                    fStore.collection("Advertisements")
                                            .document(documentId)
                                            .collection("EventAdvertisements")
                                            .document(eventId)
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                // Successfully deleted an event advertisement document
                                                Log.d("TAG", "Event advertisement subcollection deleted successfully");
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Failed to delete event advertisement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }

                                // After deleting all subcollections, delete the main document
                                fStore.collection("Advertisements")
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            showPopupDialog();
                                            new Handler().postDelayed(() -> {
                                                Intent intent = new Intent(viewAdvert.this, advertActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                                Log.d("TAG", "Advertisement deleted successfully");
                                            }, 3000);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Failed to delete advertisement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to fetch EventAdvertisements: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch AdvertPlatforms: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(this);
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

    private void fetchEventDetails() {
        fStore.collection("Advertisements")
                .document(advertId)
                .collection("EventAdvertisements")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        eventTv.setVisibility(View.VISIBLE);
                        emptyTvs.setVisibility(View.GONE);
                    } else {
                        eventTv.setVisibility(View.GONE);
                        emptyTvs.setVisibility(View.VISIBLE);
                    }
                    eventList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        eventAdvertModel event = documentSnapshot.toObject(eventAdvertModel.class);
                        eventList.add(event);
                    }
                    eventAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(viewAdvert.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void checkUserAccessLevel() {
        String userId = fAuth.getCurrentUser().getUid();

        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String accessLevel = documentSnapshot.getString("userType");
                        if (accessLevel != null && accessLevel.equals("Corporate")) {
                            eventTv.setVisibility(View.VISIBLE);
                        }
                        if (accessLevel != null && accessLevel.equals("Musician")) {
                            eventTv.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(viewAdvert.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void fetchAdvertPlaces(String advertId) {

        fStore.collection("Advertisements")
                .document(advertId)
                .collection("AdvertPlatforms")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        advertWalls.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);
                    }else {
                        advertWalls.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                    advertList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AdvertisementDetails.posterModel poster = documentSnapshot.toObject(AdvertisementDetails.posterModel.class);
                        advertList.add(poster);
                    }
                    adapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(viewAdvert.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}