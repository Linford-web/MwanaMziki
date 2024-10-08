package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class splashScreen extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (fAuth.getCurrentUser() != null) {
                    startDashboardActivity();
                } else {

                Intent intent = new Intent(splashScreen.this, loginActivity.class);
                startActivity(intent);
                finish();

                }
            }
        },2000);
    }

    private void startDashboardActivity() {
        DocumentReference df = fStore.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("Corporate") || userType.equals("Musician")) {
                        // User is Corporate or Musician
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        finishAffinity();
                        finish();
                        Log.d("TAG", "Welcome " + userType );
                    } else {
                        // Handle other user types if needed
                        Log.d("TAG", "User is neither Corporate nor Musician");
                        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                        finish();
                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finishAffinity();
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
                // clear the back stack
                finishAffinity();
                finish();
            }
        });


    }
}