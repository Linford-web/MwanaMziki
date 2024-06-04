package com.example.eventmuziki;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Objects;

public class addEvents extends AppCompatActivity {

    ImageView backArrow;
    Button addPoster, addTime, addDate, addEvent;
    ImageView imageView;
    EditText inputTaskName, eventDetails;
    TextView datePicker, timePicker, locationBtn, organizerName;
    EditText amountTxt;
    FirebaseFirestore fStore;
    String dueTime;
    private Spinner spinnerCategory;
    FirebaseStorage fStorage;

    Uri imageUri = null;
    String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_events);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        addPoster = findViewById(R.id.addPosterBtn);
        imageView = findViewById(R.id.eventPoster);
        inputTaskName = findViewById(R.id.inputTaskName);
        eventDetails = findViewById(R.id.eventDetails);
        addTime = findViewById(R.id.timePickerBtn);
        addDate = findViewById(R.id.datePickerBtn);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        locationBtn = findViewById(R.id.LocationBtn);
        amountTxt = findViewById(R.id.amountTxt);
        addEvent = findViewById(R.id.add_event);
        organizerName = findViewById(R.id.organizerNameTv);
        spinnerCategory = findViewById(R.id.spinner_category);

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();



    // Initialize ArrayAdapter and set it to the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle selection here
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                Log.d("Selected Category", selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
                Toast.makeText(addEvents.this, "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                // Get the event details from the EditText fields
                String eventName = inputTaskName.getText().toString();
                String eventDetail = eventDetails.getText().toString();
                String date = datePicker.getText().toString();
                String time = timePicker.getText().toString();
                String location = locationBtn.getText().toString();
                String amount = amountTxt.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String organizer = organizerName.getText().toString();

                if (eventName.isEmpty() || eventDetail.isEmpty() || date.isEmpty() || time.isEmpty() || amount.isEmpty()) {
                        // Show an error message if any of the fields are empty
                    Toast.makeText(addEvents.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {

                    // Add the event to Firestore
                    eventModel event = new eventModel("",eventName, eventDetail, date, time, location, amount, category, "" , organizer);
                    // Add the event to Firestore
                    fStore.collection("Events")
                            .add(event)
                            .addOnSuccessListener(documentReference -> {

                                String userId = FirebaseAuth.getInstance().getUid();
                                String eventId = documentReference.getId();

                                documentReference.update("eventId", eventId);
                                documentReference.update("creatorID", userId);

                                if (imageUri != null) {
                                    uploadEventPoster(documentReference);
                                } else {


                                    // Ensures that the success_layout disappears after 1 seconds
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(addEvents.this, EventsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);
                                    Toast.makeText(addEvents.this, "Event Added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(addEvents.this, "Failed to add event", Toast.LENGTH_SHORT).show();

                            });
                }

            }

        });

        // fetch and display organizer name
        fetchOrganizerName();


        addPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(addEvents.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadEventPoster(DocumentReference documentReference) {
        // Upload the selected image to Firebase Storage
        StorageReference storageRef = fStorage.getReference();
        final StorageReference posterRef = storageRef.child("event_posters/" + eventId + ".jpg");

        posterRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                eventId = documentReference.getId();
                                DocumentReference eventRef = fStore.collection("Events").document(eventId);
                                String imageUrl = uri.toString();
                                eventRef.update("eventPoster", imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(addEvents.this, "Event poster updated successfully", Toast.LENGTH_SHORT).show();
                                                Glide.with(addEvents.this).load(imageUrl).into(imageView);

                                                // Ensures that the success_layout disappears after 1 seconds
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(addEvents.this, EventsActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, 2000);
                                                Toast.makeText(addEvents.this, "Event Added", Toast.LENGTH_SHORT).show();



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(addEvents.this, "Failed to update event poster", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addEvents.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchOrganizerName() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    organizerName.setText(name);
                } else {
                    Toast.makeText(addEvents.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(addEvents.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDateDialog() {
        // Show the date picker dialog
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Note: month is 0-based, so add 1 to it if you want a 1-based month
                datePicker.setText(String.format("%s : %s : %s", String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth)));
            }
        },year,month,day);
        // Set the minimum date to the current date
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void openTimeDialog() {
        // Show another TimePickerDialog to select the end time
        TimePickerDialog endTimeDialog = new TimePickerDialog(addEvents.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int endHourOfDay, int endMinute) {
                dueTime = String.valueOf(endHourOfDay) + " : " + String.valueOf(endMinute);
                // Display the selected time span or update UI as needed
                timePicker.setText(dueTime);
            }
        },12, 10,true);
        endTimeDialog.show();
    }

}