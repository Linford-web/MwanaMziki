package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.advertAdapter;
import com.example.eventmuziki.Adapters.advertAdapter2;
import com.example.eventmuziki.Models.AdvertisementDetails;
import com.example.eventmuziki.Models.eventAdvertModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class advertActivity extends AppCompatActivity {

    ImageButton back;
    RecyclerView myAdvertsRv;
    FloatingActionButton addAdvertBtn;
    ArrayList<AdvertisementDetails.advertModel> adverts1;
    advertAdapter2 adapter1;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    LinearLayout addAdvertTv, advertRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advert);

        back = findViewById(R.id.back_arrow);
        addAdvertBtn = findViewById(R.id.addAdvertBtn);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        addAdvertTv = findViewById(R.id.emptyRecyclerviewTv);
        advertRv = findViewById(R.id.advertRv);
        myAdvertsRv = findViewById(R.id.myAdvertsRv);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        addAdvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(advertActivity.this, addAdverts.class);
                startActivity(intent);
            }
        });

        adverts1 = new ArrayList<>();
        adapter1 = new advertAdapter2(adverts1, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myAdvertsRv.setLayoutManager(layoutManager1);
        myAdvertsRv.setAdapter(adapter1);

        // fetch my adverts
        fetchMyAdverts();


    }

    private void fetchMyAdverts() {
        fStore.collection("Advertisements")
                .whereEqualTo("creatorId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            myAdvertsRv.setVisibility(View.VISIBLE);
                            addAdvertTv.setVisibility(View.GONE);
                        }else {
                            myAdvertsRv.setVisibility(View.GONE);
                            addAdvertTv.setVisibility(View.VISIBLE);
                        }
                        adverts1.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AdvertisementDetails.advertModel advert = documentSnapshot.toObject(AdvertisementDetails.advertModel.class);
                            adverts1.add(advert);
                        }
                        adapter1.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> Toast.makeText(advertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }



}