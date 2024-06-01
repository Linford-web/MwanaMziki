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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventmuziki.Models.eventModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class addEvents extends AppCompatActivity {

    ImageView backArrow;
    Button addPoster, addTime, addDate, addEvent;
    ImageView imageView;
    EditText inputTaskName, eventDetails;
    TextView datePicker, timePicker, locationBtn;
    EditText amountTxt;
    FirebaseFirestore fStore;
    String dueTime;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_events);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        addPoster = findViewById(R.id.addPosterBtn);
        imageView = findViewById(R.id.poster_image);
        inputTaskName = findViewById(R.id.inputTaskName);
        eventDetails = findViewById(R.id.eventDetails);
        addTime = findViewById(R.id.timePickerBtn);
        addDate = findViewById(R.id.datePickerBtn);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        locationBtn = findViewById(R.id.LocationBtn);
        amountTxt = findViewById(R.id.amountTxt);
        addEvent = findViewById(R.id.add_event);
        spinnerCategory = findViewById(R.id.spinner_category);

        fStore = FirebaseFirestore.getInstance();

/*
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
                Toast.makeText(addEvents.this, selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
                Toast.makeText(addEvents.this, "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });*/

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
                // String category = spinnerCategory.getSelectedItem().toString();

                if (eventName.isEmpty() || eventDetail.isEmpty() || date.isEmpty() || time.isEmpty() || amount.isEmpty()) {
                        // Show an error message if any of the fields are empty
                    Toast.makeText(addEvents.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {

                    // Add the event to Firestore
                    eventModel event = new eventModel(eventName, eventDetail, date, time, location, amount, "Jazz", "" );
                    // Add the event to Firestore
                    fStore.collection("Events")
                            .add(event)
                            .addOnSuccessListener(documentReference -> {

                                String userId = FirebaseAuth.getInstance().getUid();
                                String eventId = documentReference.getId();
                                documentReference.update("eventId", eventId);
                                documentReference.update("creatorID", userId);

                                // Ensures that the success_layout disappears after 1 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent =new Intent(addEvents.this, EventsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                },2000);
                                Toast.makeText(addEvents.this, "Event Added", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(addEvents.this, "Failed to add event", Toast.LENGTH_SHORT).show();

                            });
                }

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