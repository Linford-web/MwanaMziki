package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.advertAdapter;
import com.example.eventmuziki.Models.advertisementModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class advertActivity extends AppCompatActivity {

    ImageButton back;
    RecyclerView allAdvertsRv;
    FloatingActionButton addAdvertBtn;
    advertAdapter adapter;
    ArrayList<advertisementModel> adverts;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advert);

        back = findViewById(R.id.back_arrow);
        allAdvertsRv = findViewById(R.id.allAdvertsRv);
        addAdvertBtn = findViewById(R.id.addAdvertBtn);
        fStore = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addAdvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(advertActivity.this, addAdverts.class);
                startActivity(intent);
            }
        });

        adverts = new ArrayList<>();
        adapter = new advertAdapter(adverts);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        allAdvertsRv.setLayoutManager(layoutManager);
        allAdvertsRv.setAdapter(adapter);

        // fetch adverts
        fetchAdverts();

    }

    private void fetchAdverts() {
        String userId = FirebaseAuth.getInstance().getUid();

        fStore.collection("Advertisements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        advertisementModel advert = documentSnapshot.toObject(advertisementModel.class);
                        adverts.add(advert);
                    }
                    adapter.notifyDataSetChanged();
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(advertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}