package com.example.eventmuziki;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.AdvertisementDetails;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class addAdverts extends AppCompatActivity {

    EditText title, contact, details, duration;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, terms;
    Button addAdvert;
    TextView cost1, reach1, description1,
            cost2, reach2, description2,
            cost3, reach3, description3,
            cost4, reach4, description4,
            viewTerms, advertisementDate, advertisementTime;
    ImageView advertImage;
    ImageButton cancelTerms;
    Button uploadImage, uploadVideo;
    LinearLayout detailsLayout, termsLayout;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseStorage fStorage;
    private Dialog popupDialog;
    boolean valid = true;
    String documentId;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_adverts);

        title = findViewById(R.id.title);
        contact = findViewById(R.id.contact);
        details = findViewById(R.id.details);
        duration = findViewById(R.id.duration);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        addAdvert = findViewById(R.id.addAdvertBtn);
        cost1 = findViewById(R.id.cost1);
        reach1 = findViewById(R.id.reach1);
        description1 = findViewById(R.id.content1);
        cost2 = findViewById(R.id.cost2);
        reach2 = findViewById(R.id.reach2);
        description2 = findViewById(R.id.content2);
        cost3 = findViewById(R.id.cost3);
        reach3 = findViewById(R.id.reach3);
        description3 = findViewById(R.id.content3);
        cost4 = findViewById(R.id.cost4);
        reach4 = findViewById(R.id.reach4);
        description4 = findViewById(R.id.content4);
        viewTerms = findViewById(R.id.conditions);
        terms = findViewById(R.id.terms);
        detailsLayout = findViewById(R.id.detailsLayout);
        termsLayout = findViewById(R.id.TermsAndConditions);
        cancelTerms = findViewById(R.id.cancel_terms);
        advertisementDate = findViewById(R.id.advertisementDate);
        advertisementTime = findViewById(R.id.advertisementTime);
        uploadImage = findViewById(R.id.upload_image);
        uploadVideo = findViewById(R.id.upload_video);
        advertImage = findViewById(R.id.advertImage);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStorage = FirebaseStorage.getInstance();

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.back_arrow).setOnClickListener(v -> {
            // clear the back stack and start a new activity
            Intent intent = new Intent(addAdverts.this, advertActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        // Set click listener for the addAdvert button
        addAdvert.setOnClickListener(v -> {
            addAdvertisements();
        });

        viewTerms.setOnClickListener(v -> {
            detailsLayout.setVisibility(View.GONE);
            termsLayout.setVisibility(View.VISIBLE);
        });
        cancelTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsLayout.setVisibility(View.VISIBLE);
                termsLayout.setVisibility(View.GONE);
            }
        });

        if (terms.isChecked()) {
            addAdvert.setEnabled(true);
        } else {
            addAdvert.setEnabled(false);
        }

        // Set a listener to the terms checkbox
        terms.setOnCheckedChangeListener((compoundButton, b) -> {
            if (terms.isChecked()) {
                addAdvert.setEnabled(true);
            } else {
                addAdvert.setEnabled(false);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle edit button click here
                ImagePicker.with(addAdverts.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        // Call the method to display date and time
        displayCurrentDateTime();
        // Upload the selected image to Firebase Storage
        uploadPoster(documentId);


    }


    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

    }

    private void addAdvertisements() {

        checkField(title);
        checkField(contact);
        checkField(details);
        checkField(duration);
        if (!valid) {
            // set error message
            return;
        }

        String titleText = title.getText().toString();
        String contactText = contact.getText().toString();
        String detailsText = details.getText().toString();
        String durationText = duration.getText().toString();
        String time = advertisementTime.getText().toString();
        String date = advertisementDate.getText().toString();

        AdvertisementDetails.advertModel model = new AdvertisementDetails.advertModel(titleText, contactText, date, time, detailsText, durationText, "", "", Objects.requireNonNull(fAuth.getCurrentUser()).getUid());

        fStore.collection("Advertisements").add(model)
                .addOnSuccessListener(documentReference -> {
                    documentId = documentReference.getId();
                    documentReference.update("advertId", documentId);
                    showPopupDialog();
                    addAdvertSubCollection(documentReference, documentId);
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(addAdverts.this, advertActivity.class);
                        startActivity(intent);
                        finish();
                    }, 3000);
                    // Dismiss the popup dialog
                    if (popupDialog != null && popupDialog.isShowing()) {
                        popupDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(addAdverts.this, "Error Adding Advertisement", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadPoster(String documentId) {
        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference posterRef = storageRef.child("advertisement_poster/" + documentId + ".jpg");

        if (imageUri == null) {
            Log.d("AdvertPoster", "No image selected");
        } else {
            posterRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DocumentReference documentReference = fStore.collection("Advertisements").document(documentId);
                                    String imageUrl = uri.toString();
                                    documentReference.update("image", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("EventPoster", "Event poster updated successfully");
                                                    Glide.with(addAdverts.this)
                                                            .load(imageUrl)
                                                            .placeholder(R.drawable.cover)
                                                            .error(R.drawable.cover)
                                                            .into(advertImage);
                                                    Intent intent = new Intent(addAdverts.this, eventsActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(addAdverts.this, "Failed to update event poster", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addAdverts.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void displayCurrentDateTime() {
        // Get the current date and time using Calendar
        Calendar calendar = Calendar.getInstance();

        // Define date and time formats
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // Get formatted date and time as strings
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Set the date and time to the TextViews
        advertisementDate.setText(currentDate);
        advertisementTime.setText(currentTime);
    }

    private void addAdvertSubCollection(DocumentReference documentReference, String documentId) {

        CollectionReference collectionReference = documentReference.collection("AdvertPlatforms");

        String costTxt = cost1.getText().toString();
        String reachText = reach1.getText().toString();
        String detailsText = description1.getText().toString();
        if (checkBox1.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxt, reachText, detailsText, "Billboard", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Billboard Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxtm = cost2.getText().toString();
        String reachTextm = reach2.getText().toString();
        String detailsTextm = description2.getText().toString();
        if (checkBox2.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxtm, reachTextm, detailsTextm, "Matatu", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Matatu Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxtv = cost3.getText().toString();
        String reachTextv = reach3.getText().toString();
        String detailsTextv = description3.getText().toString();
        if (checkBox3.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxtv, reachTextv, detailsTextv, "Video", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Video Stands Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }
        String costTxto = cost4.getText().toString();
        String reachTexto = reach4.getText().toString();
        String detailsTexto = description4.getText().toString();
        if (checkBox4.isChecked()) {
            AdvertisementDetails.posterModel model = new AdvertisementDetails.posterModel(costTxto, reachTexto, detailsTexto, "Online", documentId, "");
            collectionReference.add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String documentId = documentReference.getId();
                    documentReference.update("posterId", documentId);
                    Log.d("Success", "Added Online Places");
                }
            }).addOnFailureListener(e -> Log.d("Error", "Error Adding Billboard Places"));
        } else {
            Log.d("Error", "Error Adding Billboard subCollection");
        }


    }

    private void showPopupDialog() {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(addAdverts.this);
            popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popupDialog.setCancelable(false);
            popupDialog.setContentView(R.layout.popup_layout2);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri = null;
        if (data != null) {
            imageUri = data.getData();
        }
        advertImage.setImageURI(imageUri);

        // Upload the selected image to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference advertRef = storageRef.child("advertisement_poster/" + FirebaseAuth.getInstance().getUid() + ".jpg");

        if (imageUri != null) {
            advertRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            advertRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    FirebaseFirestore.getInstance().collection("Advertisements")
                                            .document()
                                            .update("image", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(addAdverts.this, "Advertisement Poster updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Update ImageView with the new image URL
                                                    Glide.with(addAdverts.this)
                                                            .load(imageUrl)
                                                            .placeholder(R.drawable.cover)
                                                            .error(R.drawable.cover)
                                                            .into(advertImage);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(addAdverts.this, "Failed to update Advertisement Poster", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addAdverts.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }


}
