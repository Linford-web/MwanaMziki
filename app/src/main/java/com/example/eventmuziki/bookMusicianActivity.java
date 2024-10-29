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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.biddersEventModel;
import com.example.eventmuziki.Models.bookedEventsModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class bookMusicianActivity extends AppCompatActivity {

    TextView eventName, eventLocation, eventDate, eventTime, get_name, bidAmountTxt, categoryTxt, aboutTxt,
            socialTxt, biddersName, organizerName;
    TextView details;
    Button bookNow;
    CircleImageView bidderProfile;
    ImageView back, poster;

    Dialog popupDialog;

    String userId;
    String email, phone, profile;

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


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                                            .error(R.drawable.cover)
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

    private void bookMusicians(String bidId, String biddersId, String organizerName, String eventId, String biddersName, String creatorId) {
        if (biddersId == null || biddersName == null || organizerName == null || eventId == null || creatorId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // Event details
        String name = this.eventName.getText().toString();
        String location = this.eventLocation.getText().toString();
        String date = this.eventDate.getText().toString();
        String time = this.eventTime.getText().toString();
        String category = this.categoryTxt.getText().toString();
        String about = this.aboutTxt.getText().toString();
        String socials = this.socialTxt.getText().toString();
        String details = this.details.getText().toString();

        // Create a new event object
        bookedEventsModel bookEvent = new bookedEventsModel(name, details, date, time, location, eventId, creatorId,
                bidId, category, biddersId, organizerName, biddersName, about, socials, "");

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Event already booked, now check if the bidder is already added
                            String bookedEventId = task.getResult().getDocuments().get(0).getId();

                            fStore.collection("BookedEvents")
                                    .document(bookedEventId)
                                    .collection("BookedBidders")
                                    .whereEqualTo("biddersId", biddersId)
                                    .get()
                                    .addOnSuccessListener(bidderQuerySnapshot -> {
                                        if (!bidderQuerySnapshot.isEmpty()) {
                                            // Bidder is already added
                                            Toast.makeText(bookMusicianActivity.this, "This bidder is already booked for this event", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Bidder not added, proceed to add the bidder
                                            addBiddersSubCollection(biddersName, biddersId, eventId, bookedEventId);
                                            showPopupDialog();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(bookMusicianActivity.this, "Failed to check bidder booking", Toast.LENGTH_SHORT).show();
                                        Log.e("Error", "Error checking bidder booking: " + e.getMessage());
                                    });

                        } else {
                            // Event not booked, proceed to book the event
                            fStore.collection("BookedEvents")
                                    .add(bookEvent)
                                    .addOnSuccessListener(documentReference -> {
                                        documentReference.update("bookedId", documentReference.getId());
                                        addBiddersSubCollection(biddersName, biddersId, eventId, documentReference.getId());

                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(bookMusicianActivity.this, "Failed to book event", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(bookMusicianActivity.this, "Error checking booking status", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "Error checking booking status: " + task.getException().getMessage());
                    }
                });
    }

    private void addBiddersSubCollection(String biddersName, String biddersId, String eventId, String bookedEventId) {

        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("userid", biddersId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Extract email, phone, and profile details from the document
                        DocumentSnapshot userSnapshot = queryDocumentSnapshots.getDocuments().get(0); // Assuming only one document
                        String email = userSnapshot.getString("email");
                        String phone = userSnapshot.getString("phone");
                        String profile = userSnapshot.getString("profilePicture");

                        // Create the bookedBiddersModel with the fetched details
                        ServicesDetails.bookedBiddersModel bidderData = new ServicesDetails.bookedBiddersModel(
                                biddersName, email, phone, profile, biddersId, eventId, "", bookedEventId);

                        // Add the bidder data to the BookedBidders subcollection under the specific booked event
                        FirebaseFirestore.getInstance()
                                .collection("BookedEvents")
                                .document(bookedEventId)
                                .collection("BookedBidders")
                                .add(bidderData)
                                .addOnSuccessListener(documentReference -> {
                                    if (documentReference != null) {
                                        // Optionally update the document with its own ID
                                        documentReference.update("docId", documentReference.getId());
                                        showPopupDialog();
                                        new Handler().postDelayed(() -> {
                                            Intent intent = new Intent(bookMusicianActivity.this, bookedEvents.class);
                                            startActivity(intent);
                                            finish();
                                        }, 2000);
                                    }
                                    Log.d("Success", "Bidder added to subcollection successfully");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Error", "Error adding bidder to subcollection: " + e.getMessage());
                                });

                    } else {
                        // If no user document is found, handle this case
                        Log.e("Error", "No user found with the given biddersId");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Error", "Error fetching user details: " + e.getMessage());
                });
    }

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(bookMusicianActivity.this);
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
                                            .placeholder(R.drawable.profile_image)
                                            .error(R.drawable.profile_image)
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