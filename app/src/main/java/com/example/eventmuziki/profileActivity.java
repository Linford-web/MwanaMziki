package com.example.eventmuziki;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class profileActivity extends AppCompatActivity {

    TextView name, phone, email, aboutMe, socials, category;
    ImageView imageView, closeReview, closeContact;
    LinearLayout bidderDetails, editBtn, walletBtn, eventsBtn, servicesBtn,
            advertsBtn, aboutBtn, contactBtn, reviewBtn, logoutBtn, contactDetails, reviewDetails;
    Button submitReview, messageAdmin;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        name = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);

        imageView = findViewById(R.id.imageView);
        aboutMe = findViewById(R.id.about);
        socials = findViewById(R.id.socials);
        category = findViewById(R.id.category);
        editBtn = findViewById(R.id.editProfileBtn);
        logoutBtn = findViewById(R.id.logOutBtn);
        walletBtn = findViewById(R.id.walletBtn);
        eventsBtn = findViewById(R.id.eventsBtn);
        servicesBtn = findViewById(R.id.servicesBtn);
        advertsBtn = findViewById(R.id.advertsBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        contactBtn = findViewById(R.id.contactBtn);
        reviewBtn = findViewById(R.id.reviewBtn);
        bidderDetails = findViewById(R.id.bidderDetailsLayout);
        contactDetails = findViewById(R.id.contactDetail);
        reviewDetails = findViewById(R.id.reviewDetails);
        submitReview = findViewById(R.id.reviewSubmit);
        closeReview = findViewById(R.id.closeBtn);
        closeContact = findViewById(R.id.closeContactBtn);
        messageAdmin = findViewById(R.id.messageAdmin);

        // check user access level
        checkUserAccessLevel();

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.left_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), editProfile.class);
                startActivity(intent);
            }
        });
        walletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), walletActivity.class);
                startActivity(intent);
            }
        });
        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), eventsActivity.class);
                startActivity(intent);
            }
        });
        servicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), categoryOptions.class);
                startActivity(intent);
            }
        });
        advertsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), advertActivity.class);
                startActivity(intent);
            }
        });
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDetails.setVisibility(View.VISIBLE);
            }
        });
        closeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDetails.setVisibility(View.GONE);
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDetails.setVisibility(View.VISIBLE);
            }
        });
        closeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDetails.setVisibility(View.GONE);
            }
        });


        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            // Fetch user details from FireStore "Users" collection
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve user details
                                String userName = document.getString("name");
                                String userNumber = document.getString("phone");
                                String userEmail = document.getString("email");
                                String Category = document.getString("category");
                                String about = document.getString("about");
                                String Socials = document.getString("socials");

                                // Set the user details in the respective TextViews
                                name.setText(userName);
                                phone.setText(userNumber);
                                email.setText(userEmail);
                                category.setText(Category);
                                aboutMe.setText(about);
                                socials.setText(Socials);

                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into imageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(imageView);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Toast.makeText(profileActivity.this, "Update Profile Photo", Toast.LENGTH_SHORT).show();
                                    Log.d("Profile", "No profile photo found");
                                }

                            } else {
                                Toast.makeText(profileActivity.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(profileActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

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
                                bidderDetails.setVisibility(View.GONE);
                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                bidderDetails.setVisibility(View.VISIBLE);
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