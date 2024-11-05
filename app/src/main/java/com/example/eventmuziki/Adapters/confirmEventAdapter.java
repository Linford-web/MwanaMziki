package com.example.eventmuziki.Adapters;

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
import com.example.eventmuziki.managePayment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class confirmEventAdapter extends RecyclerView.Adapter<confirmEventAdapter.ViewHolder> {

    ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents;
    Context context;

    public confirmEventAdapter(ArrayList<ServicesDetails.bookedBiddersModel> bookedEvents, Context context) {
        this.bookedEvents = bookedEvents;
        this.context = context;
    }


    @NonNull
    @Override
    public confirmEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_event, parent, false);
        context = parent.getContext();
        return new confirmEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull confirmEventAdapter.ViewHolder holder, int position) {


        ServicesDetails.bookedBiddersModel bookedBidders = bookedEvents.get(position);

        holder.amount.setText(bookedBidders.getBidAmount());
        holder.name.setText(bookedBidders.getOrganizerName());

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String eventId = bookedBidders.getEventId();
        String docId = bookedBidders.getDocId();

       FirebaseFirestore.getInstance().collection("Events")
               .document(eventId)
               .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                       if (documentSnapshot.exists()) {
                           String poster = documentSnapshot.getString("eventPoster");
                           if (poster != null && !poster.isEmpty()) {
                               Glide.with(holder.itemView.getContext())
                                       .load(poster)
                                       .placeholder(R.drawable.cover)
                                       .error(R.drawable.cover)
                                       .into(holder.poster);
                           }else {
                               Log.e("confirmEventAdapter", "Poster Not Uploaded");
                           }
                       }

                   }
               }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());


       CollectionReference bookedRef = FirebaseFirestore.getInstance().collection("BookedEvents");

       bookedRef.whereEqualTo("eventId", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               if (!queryDocumentSnapshots.isEmpty()) {
                   for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                       String documentId = document.getId();

                       bookedRef.document(documentId)
                               .collection("PaymentConfirmation")
                               .whereEqualTo("bidderId", currentUserId)
                               .whereEqualTo("docId", docId)
                               .whereEqualTo("bidderConfirmed", true)
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
                                               Log.d("confirmEventAdapter", "Document exists");
                                           }

                                       }else {
                                           holder.status.setText("Pending");
                                           holder.status.setTextColor(context.getResources().getColor(R.color.red));
                                           holder.check.setChecked(false);
                                           Log.d("confirmEventAdapter", "Document does not exist");
                                       }

                                   }
                               }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                   }
               }else {
                   Log.d("confirmEventAdapter", "Document does not exist");
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });


        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                String creatorId = bookedBidders.getCreatorId();
                String bidderId = bookedBidders.getBiddersId();
                String docId = bookedBidders.getDocId();

                if (isChecked) {
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
                                                    .whereEqualTo("bidderId", currentUserId)
                                                    .whereEqualTo("docId", docId)
                                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            if (!queryDocumentSnapshots.isEmpty()) {

                                                                for (DocumentSnapshot documents : queryDocumentSnapshots.getDocuments()) {
                                                                    String paymentIds = documents.getString("paymentId");
                                                                    documents.getReference().update("bidderConfirmed", true);
                                                                    // check if both users have confirmed
                                                                    checkIfBothUsersHaveConfirmed(holder, eventId, paymentIds);

                                                                }
                                                            } else {

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
                                                                                    documentReference.update("bidderConfirmed", true);
                                                                                    holder.check.setChecked(true);
                                                                                    holder.status.setText("Waiting");
                                                                                    holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                                                                                    // check if both users have confirmed
                                                                                    checkIfBothUsersHaveConfirmed(holder, eventId, paymentId);
                                                                                    //Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                                                                    Log.d("confirmEventAdapter", "Updated");


                                                                                } else {
                                                                                    //Toast.makeText(context, "Error adding to Firestore", Toast.LENGTH_SHORT).show();
                                                                                    holder.check.setChecked(false);
                                                                                    holder.status.setText("Pending");
                                                                                    holder.status.setTextColor(context.getResources().getColor(R.color.red));
                                                                                    Log.d("confirmEventAdapter", "Error adding to Firestore");
                                                                                }
                                                                            }
                                                                        }).addOnFailureListener(e -> Toast.makeText(context, "Error adding to Firestore", Toast.LENGTH_SHORT).show());

                                                            }
                                                        }
                                                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                    }else {
                                        Toast.makeText(context, "Error getting Event Id", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

                }else {
                    fStore.collection("BookedEvents").whereEqualTo("eventId", eventId)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                            String documentIds = document.getId();
                                            fStore.collection("BookedEvents")
                                                    .document(documentIds)
                                                    .collection("PaymentConfirmation")
                                                    .whereEqualTo("bidderId", currentUserId)
                                                    .whereEqualTo("docId", docId)
                                                    .whereEqualTo("creatorConfirmed", true)
                                                    .get()
                                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                        if (!queryDocumentSnapshots1.isEmpty()) {
                                                            for (DocumentSnapshot documents : queryDocumentSnapshots1.getDocuments()) {
                                                                documents.getReference().update("status", "Pending");
                                                                // Update bidderConfirmed to false
                                                                documents.getReference().update("bidderConfirmed", false)
                                                                        .addOnSuccessListener(aVoid -> {
                                                                            // Update the UI to reflect that the bidder is no longer confirmed
                                                                            holder.check.setChecked(false);
                                                                            holder.status.setText("Waiting");
                                                                            holder.status.setTextColor(context.getResources().getColor(R.color.light_orange));
                                                                            //Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                                                            Log.d("confirmEventAdapter", "Updated");

                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Log.d("confirmEventAdapter", "Error updating bidder confirmation");
                                                                            //Toast.makeText(context, "Error updating bidder confirmation", Toast.LENGTH_SHORT).show();
                                                                        });
                                                            }
                                                        }else {
                                                            // Optionally, delete the event if creatorConfirmed is false, or perform any other actions:
                                                            fStore.collection("BookedEvents")
                                                                    .document(documentIds)
                                                                    .collection("PaymentConfirmation")
                                                                    .whereEqualTo("creatorConfirmed", false)
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
                                                                                            //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                                                            Log.d("confirmEventAdapter", "Deleted");
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            //Toast.makeText(context, "Error deleting event confirmation", Toast.LENGTH_SHORT).show();
                                                                                            Log.d("confirmEventAdapter", "Error deleting event confirmation");
                                                                                        });

                                                                            }
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        //Toast.makeText(context, "Error fetching bidderConfirmed status", Toast.LENGTH_SHORT).show();
                                                                        Log.d("confirmEventAdapter", "Error fetching bidderConfirmed status");
                                                                    });

                                                        }
                                                    }).addOnFailureListener(e -> {
                                                        //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.d("confirmEventAdapter", "Error getting Payment Id");
                                                    });


                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("confirmEventAdapter", "Error getting Event Id");
                                }
                            });

                }
            }
        });


    }

    private void checkIfBothUsersHaveConfirmed(confirmEventAdapter.ViewHolder holder, String eventId, String paymentId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                String documentId = document.getId();
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
                                                            Log.d("confirmEventAdapter", "Document does not exist");
                                                        }
                                                    }
                                                }else {
                                                    Log.d("confirmEventAdapter", "Error getting Payment Id");
                                                    //Toast.makeText(context, "Error getting Payment Id", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(e -> Log.d("confirmEventAdapter", "Error getting Payment Id"));
                            }
                        }else {
                            Log.d("confirmEventAdapter", "Error getting Event Id");
                            //Toast.makeText(context, "Error getting Event Id", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e -> Log.d("confirmEventAdapter", "Error getting Event Id"));

    }

    @Override
    public int getItemCount() {
        return bookedEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name, amount, status;
        CheckBox check;
        CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.posterTv);
            name = itemView.findViewById(R.id.names);
            amount = itemView.findViewById(R.id.amount);
            check = itemView.findViewById(R.id.checkBox);
            container = itemView.findViewById(R.id.container);
            status = itemView.findViewById(R.id.status);

        }
    }

}
