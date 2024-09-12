package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class bookMusicianActivity extends AppCompatActivity {

    TextView eventName, eventLocation, eventDate, eventTime, get_name, bidAmountTxt, categoryTxt, aboutTxt,
            socialTxt, biddersName, organizerName;
    TextView details;
    Button bookNow;
    CircleImageView bidderProfile;
    ImageView back, poster;

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(), userName, userEmail, userPhoneNumber, profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_musician);

        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        get_name = findViewById(R.id.get_name);
        bidAmountTxt = findViewById(R.id.bidAmountTxt);
        categoryTxt = findViewById(R.id.categoryTxt);
        aboutTxt = findViewById(R.id.aboutTxt);
        socialTxt = findViewById(R.id.socialTxt);
        bookNow = findViewById(R.id.bookNowBtn);
        biddersName = findViewById(R.id.biddersNameTv);
        bidderProfile = findViewById(R.id.userProfileTv);
        organizerName = findViewById(R.id.organizerNameTv);
        back = findViewById(R.id.back_arrow);
        poster = findViewById(R.id.posterTv);
        details = findViewById(R.id.eventDescription);


        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            finish();
        });

        biddersEventModel bookEvent = getIntent().getParcelableExtra("biddersEventModel");
        if (bookEvent !=null){
            eventName.setText(bookEvent.getEventName());
            eventLocation.setText(bookEvent.getLocation());
            eventDate.setText(bookEvent.getDate());
            eventTime.setText(bookEvent.getTime());
            bidAmountTxt.setText(bookEvent.getBidAmount());
            biddersName.setText(bookEvent.getBiddersName());
            organizerName.setText(bookEvent.getOrganizerName());
            details.setText(bookEvent.getEventDetails());

            String biddersId = bookEvent.getBiddersId();

            // fetch details
            fetchDetails(biddersId);

            String eventId = bookEvent.getEventId();

            FirebaseFirestore.getInstance()
                    .collection("Events")
                    .document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                String eventPoster = documentSnapshot.getString("eventPoster");

                                if (eventPoster != null && !eventPoster.isEmpty()) {
                                    Glide.with(bookMusicianActivity.this)
                                            .load(eventPoster)
                                            .placeholder(R.drawable.cover)
                                            .error(R.drawable.poster1)
                                            .into(poster);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(bookMusicianActivity.this, "Failed To get Event Poster", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            userName = document.getString("name");
                            userEmail = document.getString("email");
                            profilePicture = document.getString("profilePicture");
                            userPhoneNumber = document.getString("number");

                            String userType = document.getString("userType");
                            if (userType != null) {
                                if (userType.equals("Corporate")) {
                                    // User is Corporate or Musician
                                    bookNow.setVisibility(View.VISIBLE);
                                }
                                else {
                                    // User is not Corporate or Musician
                                    bookNow.setVisibility(View.GONE);
                                }
                            }

                        }
                    } else {
                        Log.e("TaskListAdapter", "Error getting document", task.getException());
                        Toast.makeText(this, "Error Getting User name and email", Toast.LENGTH_SHORT).show();
                    }
                });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                bookMusicians(Objects.requireNonNull(bookEvent).getBidId(),
                        bookEvent.getBiddersId(), bookEvent.getOrganizerName(), bookEvent.getEventId(), bookEvent.getBiddersName(), bookEvent.getCreatorID());

            }
        });

    }

    private void bookMusicians( String bidId, String biddersId, String organizerName, String eventId, String biddersName, String creatorId) {
        if (biddersId == null || biddersName == null || organizerName == null || eventId == null || creatorId == null){
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        } else {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        String name = this.eventName.getText().toString();
        String location = this.eventLocation.getText().toString();
        String date = this.eventDate.getText().toString();
        String time = this.eventTime.getText().toString();
        String category = this.categoryTxt.getText().toString();
        String about = this.aboutTxt.getText().toString();
        String socials = this.socialTxt.getText().toString();
        String details = this.details.getText().toString();

        // Create a new event object with the provided data
        bookedEventsModel bookEvent = new bookedEventsModel(name, details, date, time, location, eventId, creatorId, bidId, category, biddersId, organizerName, biddersName, about, socials);

        fStore.collection("BookedEvents")
                .whereEqualTo("bidId", bidId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult() != null && !task.getResult().isEmpty()){
                                Toast.makeText(bookMusicianActivity.this, "You have already booked this event", Toast.LENGTH_SHORT).show();
                            }else {
                                fStore.collection("BookedEvents")
                                        .add(bookEvent)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.update("bookedEventId", documentReference.getId());

                                                new Handler().postDelayed(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        Intent intent = new Intent(bookMusicianActivity.this, bookedEvents.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, 2000);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(bookMusicianActivity.this, "Failed To Book Event", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });

        }

    }
    private void fetchDetails(String biddersId) {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("userid", biddersId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){

                            for ( DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                String about = documentSnapshot.getString("about");
                                String socials = documentSnapshot.getString("socials");
                                String category = documentSnapshot.getString("category");

                                aboutTxt.setText(about);
                                socialTxt.setText(socials);
                                categoryTxt.setText(category);

                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = documentSnapshot.getString("profilePicture");

                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(bookMusicianActivity.this)
                                            .load(profileImageUrl)
                                            .into(bidderProfile);

                                } else {
                                    // Handle the case when no profile photo is available
                                    Log.e("studentDetailsAdapter", "Profile Photo Not Uploaded");
                                }
                            }

                        }else {
                            Toast.makeText(bookMusicianActivity.this, "User document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(bookMusicianActivity.this, "Failed To fetch Students", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}