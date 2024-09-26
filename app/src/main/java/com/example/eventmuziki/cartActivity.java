package com.example.eventmuziki;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.eventAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.carAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cartAdapter;
import com.example.eventmuziki.Adapters.serviceAdapters.cartEventAdapter;
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
    Button linkBtn, addEvent, cancelLink, bookServiceBtn;
    RecyclerView linkedRv;

    ArrayList<ServicesDetails.cartModel> cartList;
    cartAdapter adapter;

    ArrayList<eventModel> myEventList;
    cartEventAdapter adapter1;

    String userId;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    Dialog popupDialog;

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
        addEvent = findViewById(R.id.serviceBtn);
        cancelLink = findViewById(R.id.cancelLinkBtn);
        bookServiceBtn = findViewById(R.id.confirmBtn);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cartList = new ArrayList<>();
        adapter = new cartAdapter( cartList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cartRv.setLayoutManager(layoutManager);
        cartRv.setAdapter(adapter);

        myEventList = new ArrayList<>();
        adapter1 = new cartEventAdapter(myEventList, this);
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
            startActivity(new Intent(cartActivity.this, categoryOptions.class));
        });

        adapter1.setOnItemClickListener(new cartEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                eventModel event = myEventList.get(position);
                String eventId = event.getEventId();
                if (eventId == null) {
                    Toast.makeText(getApplicationContext(), "Event ID is null", Toast.LENGTH_SHORT).show();
                } else {
                    storeCartItemsInEvent(eventId);
                    bookServices();
                }

            }
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

        bookServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookServices();
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
                            linkedLayout.setVisibility(View.GONE);
                        }
                        cartList.clear();

                        double totalAmount = 0.0;

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ServicesDetails.cartModel car = documentSnapshot.toObject(ServicesDetails.cartModel.class);
                            cartList.add(car);

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

    // Method to add the cart items to the "Booked Services" collection
    private void bookServices() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (ServicesDetails.cartModel cartItem : cartList) {
            // Create a new document in the "Booked Services" collection
            db.collection("BookedServices").add(cartItem)
                    .addOnSuccessListener(documentReference -> {

                        // After booking the service, delete it from the Cart collection
                        FirebaseFirestore.getInstance()
                                .collection("Cart")
                                .document(cartItem.getCartId())  // Assuming cart items have a cartId field
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Item successfully deleted from Cart
                                        cartList.remove(cartItem);  // Remove from the list in memory
                                        adapter.notifyDataSetChanged();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure in deleting from Cart
                                        Toast.makeText(getApplicationContext(), "Failed to delete from Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        showPopupDialog(findViewById(android.R.id.content));
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(cartActivity.this, categoryOptions.class);
                            // clear the previous activity stack
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }, 3000);


                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors here
                        Toast.makeText(cartActivity.this, "Error booking service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showPopupDialog(View view) {
        if (isFinishing() || isDestroyed()) return; // Prevent showing the dialog if the activity is not in a valid state

        if (popupDialog == null) { // Initialize the dialog only once
            popupDialog = new Dialog(view.getContext());
            popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popupDialog.setCancelable(false);
            popupDialog.setContentView(R.layout.popup_layout);

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

    // Method to fetch cart items and store them in the "Booked Services" subcollection under the event
    private void storeCartItemsInEvent(String eventId) {
        // Get the current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch cart items from Firestore
        FirebaseFirestore.getInstance().collection("Cart")
                .whereEqualTo("bookerId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Convert each document to a cartModel object
                                ServicesDetails.cartModel cartItem = documentSnapshot.toObject(ServicesDetails.cartModel.class);

                                // Add each cart item to the Booked Services subcollection of the clicked event
                                FirebaseFirestore.getInstance()
                                        .collection("Events")  // Assuming events are stored in "Events" collection
                                        .document(eventId)
                                        .collection("BookedServices")
                                        .add(cartItem)
                                        .addOnSuccessListener(documentReference -> {
                                            // After booking the service, delete it from the Cart collection
                                            FirebaseFirestore.getInstance()
                                                    .collection("Cart")
                                                    .document(cartItem.getCartId())  // Assuming cart items have a cartId field
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Item successfully deleted from Cart
                                                            cartList.remove(cartItem);  // Remove from the list in memory
                                                            adapter.notifyDataSetChanged();  // Refresh the RecyclerView
                                                            showPopupDialog(findViewById(android.R.id.content));
                                                            new Handler().postDelayed(() -> {
                                                                Intent intent = new Intent(cartActivity.this, categoryOptions.class);
                                                                // clear the previous activity stack
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }, 3000);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Handle failure in deleting from Cart
                                                            Toast.makeText(getApplicationContext(), "Failed to delete from Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure
                                            Toast.makeText(getApplicationContext(), "Failed to add service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No cart items found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(Throwable::printStackTrace);
    }


}