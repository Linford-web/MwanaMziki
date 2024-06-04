package com.example.eventmuziki;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    ImageView back, imageView, select, delete;
    Button logout;

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
        back = findViewById(R.id.back_arrow);
        imageView = findViewById(R.id.imageView);
        select = findViewById(R.id.updateProfileBtn);
        delete = findViewById(R.id.deleteProfileBtn);
        logout = findViewById(R.id.logoutBtn);
        aboutMe = findViewById(R.id.about);
        socials = findViewById(R.id.socials);
        category = findViewById(R.id.category);

        // Back Button
        back.setOnClickListener(v ->
                finish());

        // Logic For Logout Button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), startActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
                                String userNumber = document.getString("number");
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
                                    Toast.makeText(profileActivity.this, "No profile photo found", Toast.LENGTH_SHORT).show();
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

        if (userId != null) {
            // Fetch user
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String profileImageUrl = document.getString("profilePicture");
                                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                        Glide.with(profileActivity.this).load(profileImageUrl).into(imageView);
                                    } else {
                                        Toast.makeText(profileActivity.this, "No profile photo found", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(profileActivity.this, "User document not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


        }


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(profileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        // Logic for Delete Button
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfileImage();
            }
        });

    }

    private void deleteProfileImage() {
        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a reference to 'profile_pictures/<FILENAME>.jpg'
        final StorageReference profileRef = storageRef.child("profile_pictures/" + FirebaseAuth.getInstance().getUid() + ".jpg");

        // Delete the file from Firebase Storage
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Remove the profile picture URL from FireStore
                fStore.collection("Users")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update("profilePicture", null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Set default image in ImageView
                                imageView.setImageResource(R.drawable.profile_image); // Replace with your default image resource
                                Toast.makeText(profileActivity.this, "Profile photo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profileActivity.this, "Failed to remove profile picture URL", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileActivity.this, "Failed to delete profile photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri = null;
        if (data != null) {
            imageUri = data.getData();
        }
        imageView.setImageURI(imageUri);

        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference profileRef = storageRef.child("profile_photos/" + FirebaseAuth.getInstance().getUid() + ".jpg");

        if (imageUri != null) {
            profileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    fStore.collection("Users")
                                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .update("profilePicture", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(profileActivity.this, "Profile photo updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Update ImageView with the new image URL
                                                    Glide.with(profileActivity.this).load(imageUrl).into(imageView);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(profileActivity.this, "Failed to update profile photo", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }


    }

}