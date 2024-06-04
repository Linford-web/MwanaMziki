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

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class bookMusicianActivity extends AppCompatActivity {

    TextView eventName, eventLocation, eventDate, eventTime, get_name, bidAmountTxt, categoryTxt, aboutTxt, socialTxt, biddersName, organizerName, event, creator, bidder;
    Button bookNow;
    CircleImageView bidderProfile;
    ImageView back;

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
        event = findViewById(R.id.eventId);
        creator = findViewById(R.id.creatorId);
        bidder = findViewById(R.id.biddersId);
        back = findViewById(R.id.back_arrow);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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
            event.setText(bookEvent.getEventId());
            creator.setText(bookEvent.getCreatorID());
            bidder.setText(bookEvent.getBiddersId());

            String biddersId = bookEvent.getBiddersId();

            fetchDetails(biddersId);
        }

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handle the click event here
                bookMusicians();
            }
        });

    }

    private void bookMusicians() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        String name = this.eventName.getText().toString();
        String location = this.eventLocation.getText().toString();
        String date = this.eventDate.getText().toString();
        String time = this.eventTime.getText().toString();
        String category = this.categoryTxt.getText().toString();
        String about = this.aboutTxt.getText().toString();
        String socials = this.socialTxt.getText().toString();
        String id = this.event.getText().toString();
        String creatorId = this.creator.getText().toString();
        String bidderId = this.bidder.getText().toString();
        String bidderName = this.biddersName.getText().toString();
        String organizerName = this.organizerName.getText().toString();

        bookedEventsModel bookEvent = new bookedEventsModel(name, "", date, time, location, id, creatorId, "", category, bidderId, organizerName, bidderName, about, socials);

        fStore.collection("BookedEvents")
                .add(bookEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("bookedEventsId", documentReference.getId());

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