package com.example.eventmuziki;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class profileActivity extends AppCompatActivity {

    TextView name, phone, email;
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

        // Back Button
        back.setOnClickListener(v -> finish());

        // Logic For Logout Button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
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

                                // Set the user details in the respective TextViews
                                name.setText(userName);
                                phone.setText(userNumber);
                                email.setText(userEmail);

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

        /*
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Handle the result of the image selection
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                startCrop(imageUri);
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    imageView.setImageURI(resultUri);
                    uploadImageToFirebase(resultUri);
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                if (cropError != null) {
                    Log.e("Profile", "Crop error: ", cropError);
                    Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                // Handle the case when no image is selected or user canceled the operation
                //Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                Log.d("Profile", "No image selected");
            }

        }




    }
    private void startCrop(Uri imageUri) {
        String destinationFileName = UUID.randomUUID().toString() + ".jpg";
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(1080, 1080)
                .start(this);
    }

    private void uploadImageToFirebase(Uri resultUri) {
        StorageReference storageRef = fStorage.getReference();
        StorageReference profileRef = storageRef.child("profile_pictures/" + FirebaseAuth.getInstance().getUid() + ".jpg");

        profileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                fStore.collection("Users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .update("profilePicture", imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(profileActivity.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(profileActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(profileActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                });
    }


         */
    }
}