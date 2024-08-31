package com.example.eventmuziki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfile extends AppCompatActivity {

    EditText name, phoneNumber, email, aboutMe, socials;
    Button saveBtn;
    ImageButton back;

    CircleImageView profileImage;
    ImageButton editBtn, deleteBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.nameTxt);
        phoneNumber = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);
        aboutMe = findViewById(R.id.aboutTxt);
        socials = findViewById(R.id.socialTxt);
        saveBtn = findViewById(R.id.saveBtn);
        back = findViewById(R.id.back_arrow);
        profileImage = findViewById(R.id.imageView);
        editBtn = findViewById(R.id.updateProfileBtn);
        deleteBtn = findViewById(R.id.deleteProfileBtn);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.back_arrow).setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(editProfile.this, profileActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    name.setText(document.getString("name"));
                                    phoneNumber.setText(document.getString("number"));
                                    email.setText(document.getString("email"));
                                    if (document.getString("about") != null || document.getString("socials") !=null || document.getString("category") != null) {
                                        aboutMe.setText(document.getString("about"));
                                        socials.setText(document.getString("socials"));
                                    } else {
                                        aboutMe.setText("");
                                        socials.setText("");
                                    }

                                    String profileImageUrl = document.getString("profilePicture");
                                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                        // Load profile photo into imageView using Glide or any other image loading library
                                        Glide.with(editProfile.this).load(profileImageUrl).into(profileImage);
                                    }else {
                                        Log.d("Profile", "No profile photo found");
                                    }
                                }
                            }
                        }
                    });
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click here
                ImagePicker.with(editProfile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click here
                deleteProfileImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                String userId = fAuth.getCurrentUser().getUid();

                String user_name = name.getText().toString();
                String user_phone = phoneNumber.getText().toString();
                String user_email = email.getText().toString();
                String user_about = aboutMe.getText().toString();
                String user_socials = socials.getText().toString();

                if (user_name.isEmpty() || user_phone.isEmpty() || user_email.isEmpty() || user_about.isEmpty() || user_socials.isEmpty()) {
                    // Show an error message if any of the fields are empty
                    Toast.makeText(editProfile.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {

                    fStore.collection("Users")
                            .document(userId)
                            .update(
                                    "name", user_name,
                                    "number", user_phone,
                                    "email", user_email,
                                    "about", user_about,
                                    "socials", user_socials

                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(editProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                                // Ensures that the success_layout disappears after 1 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(editProfile.this, profileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 2000);
                            }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(editProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
        // check user access level
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
                                // Back Button
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                                        finish();
                                    }
                                });
                                aboutMe.setVisibility(View.GONE);
                                socials.setVisibility(View.GONE);

                            } else if ("Musician".equalsIgnoreCase(userType)) {
                                // Back Button
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                                        finish();
                                    }
                                });
                                aboutMe.setVisibility(View.VISIBLE);
                                socials.setVisibility(View.VISIBLE);

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
                FirebaseFirestore.getInstance().collection("Users")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update("profilePicture", null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Set default image in ImageView
                                profileImage.setImageResource(R.drawable.profile_image); // Replace with your default image resource
                                Toast.makeText(editProfile.this, "Profile photo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editProfile.this, "Failed to remove profile picture URL", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editProfile.this, "Failed to delete profile photo", Toast.LENGTH_SHORT).show();
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
        profileImage.setImageURI(imageUri);

        // Upload the selected image to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
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

                                    FirebaseFirestore.getInstance().collection("Users")
                                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .update("profilePicture", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(editProfile.this, "Profile photo updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Update ImageView with the new image URL
                                                    Glide.with(editProfile.this).load(imageUrl).into(profileImage);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(editProfile.this, "Failed to update profile photo", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }


}