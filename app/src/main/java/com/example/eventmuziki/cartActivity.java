package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cartAdapter;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class cartActivity extends AppCompatActivity {

    RecyclerView cartRv;
    TextView total;
    LinearLayout emptyCartTv, totalLayout, linkedLayout, emptyLinkedTv;
    Button linkBtn, addEvent, cancelLink;
    RecyclerView linkedRv;

    ArrayList<ServicesDetails.cartModel> carList;
    cartAdapter adapter;

    ArrayList<eventModel> myEventList;
    eventAdapter adapter1;

    String userId;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        cartRv = findViewById(R.id.cartRv);
        total = findViewById(R.id.totalCartAmount);
        emptyCartTv = findViewById(R.id.emptyCartTv);
        totalLayout = findViewById(R.id.totalLayout);
        linkedLayout = findViewById(R.id.linkedLayout);
        emptyLinkedTv = findViewById(R.id.emptyEventsTv);
        linkBtn = findViewById(R.id.linkBtn);
        linkedRv = findViewById(R.id.linkedRv);
        addEvent = findViewById(R.id.addEventBtn);
        cancelLink = findViewById(R.id.cancelLinkBtn);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        carList = new ArrayList<>();
        adapter = new cartAdapter( carList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cartRv.setLayoutManager(layoutManager);
        cartRv.setAdapter(adapter);

        myEventList = new ArrayList<>();
        adapter1 = new eventAdapter(myEventList, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linkedRv.setLayoutManager(layoutManager1);
        linkedRv.setAdapter(adapter1);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        findViewById(R.id.back_arrow).setOnClickListener(v -> {
            finish();
        });

        addEvent.setOnClickListener(v -> {
            startActivity(new Intent(cartActivity.this, addEvents.class));
        });

        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkedRv.setVisibility(View.VISIBLE);
                linkBtn.setVisibility(View.GONE);
                cancelLink.setVisibility(View.VISIBLE);

            }
        });
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkedRv.setVisibility(View.GONE);
                linkBtn.setVisibility(View.VISIBLE);
                cancelLink.setVisibility(View.GONE);
            }
        });

        checkUserAccessLevel(userId);

        fetchUserEvents(userId);

        fetchCart();


    }

    private void fetchUserEvents(String userId) {
        fStore.collection("Events")
                .whereEqualTo("creatorID", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            emptyLinkedTv.setVisibility(View.GONE);
                        }
                        else {
                            emptyLinkedTv.setVisibility(View.VISIBLE);
                            linkedRv.setVisibility(View.GONE);
                        }
                        myEventList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            eventModel event = documentSnapshot.toObject(eventModel.class);
                            myEventList.add(event);
                        }
                        adapter1.notifyDataSetChanged();
                    }
                }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void fetchCart() {
        FirebaseFirestore.getInstance().collection("Cart")
                .whereEqualTo("bookerId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            cartRv.setVisibility(View.VISIBLE);
                            emptyCartTv.setVisibility(View.GONE);
                            totalLayout.setVisibility(View.VISIBLE);
                        }else {
                            cartRv.setVisibility(View.GONE);
                            emptyCartTv.setVisibility(View.VISIBLE);
                            totalLayout.setVisibility(View.GONE);
                        }
                        carList.clear();

                        double totalAmount = 0.0;

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ServicesDetails.cartModel car = documentSnapshot.toObject(ServicesDetails.cartModel.class);
                            carList.add(car);

                            // Calculate the total amount
                            String priceString = car.getPrice();
                            if (priceString != null && !priceString.isEmpty()) {
                                try {
                                    double price = Double.parseDouble(priceString);
                                    totalAmount += price;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle conversion error
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        total.setText(String.format("%.2f", totalAmount));
                    }
                }).addOnFailureListener(Throwable::printStackTrace);

    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);

        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify User Access Level
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("Corporate")) {
                        linkedLayout.setVisibility(View.VISIBLE);

                    } else if (userType.equals("Musician")) {
                        linkedLayout.setVisibility(View.GONE);

                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Failed to get document: " + e.getMessage());
            }
        });
    }

}