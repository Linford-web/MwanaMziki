package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class editProfile extends AppCompatActivity {

    EditText name, phoneNumber, email, aboutMe, socials;
    Spinner category;
    Button saveBtn;

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
        category = findViewById(R.id.spinner_category);
        saveBtn = findViewById(R.id.saveBtn);

        // Initialize ArrayAdapter and set it to the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Toast.makeText(editProfile.this, "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });

        name.setText(getIntent().getStringExtra("name"));
        phoneNumber.setText(getIntent().getStringExtra("number"));
        email.setText(getIntent().getStringExtra("email"));


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
                String music_category = category.getSelectedItem().toString();


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
                                    "socials", user_socials,
                                    "category", music_category
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

    }
}