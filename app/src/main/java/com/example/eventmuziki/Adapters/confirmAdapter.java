package com.example.eventmuziki.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.confirmPaymentModel;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.cartActivity;
import com.example.eventmuziki.managePayment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmAdapter extends RecyclerView.Adapter<confirmAdapter.ViewHolder> {

    ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents;
    Context context;

    public confirmAdapter(ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents, Context context) {
        this.bookedEvents = bookedEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public confirmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_bidder, parent, false);
        context = parent.getContext();
        return new confirmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmAdapter.ViewHolder holder, int position) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ServicesDetails.bookedBiddersModel bookedBidders = bookedEvents.get(position);

        holder.amount.setText(bookedBidders.getBidAmount());
        holder.name.setText(bookedBidders.getBiddersName());

        String profileImageUrl = bookedBidders.getProfile();
        String eventId = bookedBidders.getEventId();
        String bidId = bookedBidders.getDocId();
        String creatorId = bookedBidders.getCreatorId();


        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            // Load profile photo into otherImageView using Glide image loading library
            Glide.with(holder.itemView.getContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile_image)
                    .error(R.drawable.profile_image)
                    .into(holder.profile);

        } else {
            // Handle the case when no profile photo is available
            Log.e("confirmAdapter", "Profile Photo Not Uploaded");
        }

        CollectionReference bookedRef = FirebaseFirestore.getInstance().collection("BookedEvents");

        bookedRef.whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                String documentId = document.getId();
                                bookedRef.document(documentId)
                                        .collection("PaymentConfirmation")
                                        .whereEqualTo("creatorId", currentUserId)
                                        .whereEqualTo("docId", bidId)
                                        .whereEqualTo("creatorConfirmed", true)
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                               if (!queryDocumentSnapshots.isEmpty()) {
                                                   for (DocumentSnapshot documents : queryDocumentSnapshots.getDocuments()) {
                                                       holder.status.setText("Waiting");
                                                       holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                                                       holder.check.setChecked(true);

                                                       String paymentIds = documents.getString("paymentId");
                                                       checkIfBothUsersHaveConfirmed(holder, eventId, paymentIds);
                                                       Log.d("confirmAdapter", "Document exists");
                                                   }
                                               }else {
                                                   holder.status.setText("Pending");
                                                   holder.status.setTextColor(context.getResources().getColor(R.color.red));
                                                   holder.check.setChecked(false);
                                                   Log.d("confirmAdapter", "Document does not exist");
                                               }
                                            }
                                        }).addOnFailureListener(e -> Log.e("confirmAdapter", "Error getting Payment Id"));
                            }
                        }
                    }
                });

        // Handle checkbox interaction
        holder.check.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            String bidderId = bookedBidders.getBiddersId();
            String docId = bookedBidders.getDocId();

            FirebaseFirestore fStore = FirebaseFirestore.getInstance();

            if (isChecked) {
                // Creator confirms the payment
                fStore.collection("BookedEvents")
                        .whereEqualTo("eventId", eventId)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                        String documentId = document.getId();
                                        fStore.collection("BookedEvents")
                                                .document(documentId)
                                                .collection("PaymentConfirmation")
                                                .whereEqualTo("creatorId", currentUserId)
                                                .whereEqualTo("docId", docId)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (!queryDocumentSnapshots.isEmpty()) {

                                                            for (DocumentSnapshot documents : queryDocumentSnapshots.getDocuments()) {
                                                                String paymentIds = documents.getString("paymentId");
                                                                documents.getReference().update("creatorConfirmed", true);
                                                                // check if both users have confirmed
                                                                checkIfBothUsersHaveConfirmed(holder, eventId, paymentIds);
                                                            }


                                                        }else {
                                                            // Toast.makeText(context, "Already Confirmed", Toast.LENGTH_SHORT).show();
                                                            confirmPaymentModel model = new confirmPaymentModel("", creatorId, bidderId, bookedBidders.getBidAmount(),
                                                                    "Pending", eventId, docId, false, false);

                                                            // User has checked the checkbox, update Firestore to add details to the bookedEvents collection
                                                            fStore.collection("BookedEvents")
                                                                    .document(documentId)
                                                                    .collection("PaymentConfirmation")
                                                                    .add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            if (documentReference != null) {
                                                                                String paymentId = documentReference.getId();
                                                                                documentReference.update("paymentId", paymentId);
                                                                                documentReference.update("creatorConfirmed", true);
                                                                                holder.check.setChecked(true);
                                                                                holder.status.setText("Waiting");
                                                                                holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                                                                                Log.d("confirmAdapter", "Document exists");
                                                                                //Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                                                                // check if both users have confirmed
                                                                                checkIfBothUsersHaveConfirmed(holder, eventId, paymentId);

                                                                            }else {
                                                                                Log.d("confirmAdapter", "Error adding to Firestore");
                                                                                //Toast.makeText(context, "Error adding to Firestore", Toast.LENGTH_SHORT).show();
                                                                                holder.check.setChecked(false);
                                                                                holder.status.setText("Pending");
                                                                                holder.status.setTextColor(context.getResources().getColor(R.color.red));
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(e -> Toast.makeText(context, "Error adding to Firestore", Toast.LENGTH_SHORT).show());

                                                        }
                                                    }
                                                }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

                                    }
                                }else {
                                    Toast.makeText(context, "Error getting Event Id", Toast.LENGTH_SHORT).show();
                                    Log.d("confirmAdapter", "Error getting Event Id");
                                }
                            }

                        }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());


            } else {
                // Uncheck the checkbox, remove the confirmation from Firestore
                fStore.collection("BookedEvents")
                        .whereEqualTo("eventId", eventId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                        String documentId = document.getId();
                                        fStore.collection("BookedEvents")
                                                .document(documentId)
                                                .collection("PaymentConfirmation")
                                                .whereEqualTo("creatorId", currentUserId)
                                                .whereEqualTo("docId", bidId)
                                                .whereEqualTo("bidderConfirmed", true)
                                                .get()
                                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                    if (!queryDocumentSnapshots1.isEmpty()) {
                                                        for (DocumentSnapshot documents : queryDocumentSnapshots1.getDocuments()) {
                                                            documents.getReference().update("status", "Pending");
                                                            documents.getReference().update("creatorConfirmed", false)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    holder.check.setChecked(false);
                                                                                    holder.status.setText("Waiting");
                                                                                    holder.status.setTextColor(context.getResources().getColor(R.color.light_orange));
                                                                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

                                                        }
                                                    }else {
                                                        // Optionally, delete the event if bidderConfirmed is false, or perform any other actions:
                                                        fStore.collection("BookedEvents")
                                                                .document(documentId)
                                                                .collection("PaymentConfirmation")
                                                                .whereEqualTo("bidderConfirmed", false)
                                                                .get()
                                                                .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                                                    if (!queryDocumentSnapshots2.isEmpty()) {
                                                                        for (DocumentSnapshot snapshot : queryDocumentSnapshots2) {
                                                                            // Deleting the document if bidderConfirmed is already false
                                                                            snapshot.getReference().delete()
                                                                                    .addOnSuccessListener(aVoid -> {
                                                                                        holder.check.setChecked(false);
                                                                                        holder.status.setText("Pending");
                                                                                        holder.status.setTextColor(context.getResources().getColor(R.color.light_orange));
                                                                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                                                    })
                                                                                    .addOnFailureListener(e -> {
                                                                                        //Toast.makeText(context, "Error deleting event confirmation", Toast.LENGTH_SHORT).show();
                                                                                        Log.e("confirmAdapter", "Error deleting event confirmation", e);
                                                                                    });


                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Log.e("confirmAdapter", "Error fetching bidderConfirmed status", e);
                                                                    //Toast.makeText(context, "Error fetching bidderConfirmed status", Toast.LENGTH_SHORT).show();
                                                                });
                                                    }
                                                }).addOnFailureListener(e -> {
                                                    Log.e("confirmAdapter", "Error updating status", e);
                                                    //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });

                                    }
                                }
                            }
                        }).addOnFailureListener(e -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show());

            }
        });

    }

    private void checkIfBothUsersHaveConfirmed(confirmAdapter.ViewHolder holder, String eventId, String paymentId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                document.getReference().collection("PaymentConfirmation")
                                        .whereEqualTo("paymentId", paymentId)
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (DocumentSnapshot documents : queryDocumentSnapshots.getDocuments()) {
                                                        boolean bidderConfirmed = Boolean.TRUE.equals(documents.getBoolean("bidderConfirmed"));
                                                        boolean creatorConfirmed = Boolean.TRUE.equals(documents.getBoolean("creatorConfirmed"));

                                                        if (bidderConfirmed && creatorConfirmed) {
                                                            // Both confirmed, update status to "Confirmed"
                                                            documents.getReference().update("status", "Confirmed");
                                                            holder.status.setText("Confirmed");
                                                            holder.status.setTextColor(context.getResources().getColor(R.color.my_primary));
                                                            holder.check.setChecked(true);
                                                        }else {
                                                            holder.check.setChecked(true);
                                                            holder.status.setText("Waiting");
                                                            holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                                                        }
                                                    }
                                                } else {
                                                    Log.d("confirmAdapter", "Error getting Payment Id");
                                                    //Toast.makeText(context, "Error getting Payment Id", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(e -> Log.e("confirmAdapter", "Error getting Payment Id"));
                            }
                        } else {
                            Log.d("confirmAdapter", "Error getting Event Id");
                            //Toast.makeText(context, "Error getting Event Id", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e -> Log.e("confirmAdapter", "Error getting Event Id"));
    }
        @Override
    public int getItemCount() {
        return bookedEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView name, amount, status;
        CheckBox check;
        CardView container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.cardTv);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            check = itemView.findViewById(R.id.checkBox);
            container = itemView.findViewById(R.id.container);
            status = itemView.findViewById(R.id.status);

        }
    }
}
