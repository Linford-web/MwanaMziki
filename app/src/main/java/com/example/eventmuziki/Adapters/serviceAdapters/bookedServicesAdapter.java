package com.example.eventmuziki.Adapters.serviceAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class bookedServicesAdapter extends RecyclerView.Adapter<bookedServicesAdapter.ViewHolder> {

    ArrayList<ServicesDetails.cartModel> carts;
    Context context;

    public bookedServicesAdapter(ArrayList<ServicesDetails.cartModel> carts, Context context) {
        this.carts = carts;
        this.context = context;
    }


    public interface OnItemDeleteListener {
        void onItemDeleted(double itemPrice);
    }

    private OnItemDeleteListener onItemDeleteListener;

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.onItemDeleteListener = listener;
    }


    @NonNull
    @Override
    public bookedServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookedServicesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ServicesDetails.cartModel cart = carts.get(position);
        String productName = cart.getName();
        String productType = cart.getpType();
        String productPrice = cart.getPrice();
        String productImage = cart.getImage();

        holder.name.setText(productName);
        holder.packageName.setText(productType);
        holder.price.setText(productPrice);

        if (productImage != null) {
            Glide.with(context).load(productImage)
                    .placeholder(R.drawable.blank_photo)
                    .error(R.drawable.blank_photo)
                    .into(holder.cover);
        }else {
            holder.cover.setImageResource(R.drawable.blank_photo);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the product ID for deletion from Firestore
                String productIdToDelete = carts.get(position).getBookedServiceId();

                // Delete the item from the main BookedServices collection
                FirebaseFirestore.getInstance().collection("BookedServices")
                        .document(productIdToDelete)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // Now check each event's BookedServices subcollection and delete the corresponding service
                            FirebaseFirestore.getInstance().collection("Events")
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (DocumentSnapshot eventDoc : queryDocumentSnapshots) {
                                            String eventId = eventDoc.getId();
                                            // Access the BookedServices subcollection of each event
                                            FirebaseFirestore.getInstance().collection("Events")
                                                    .document(eventId)
                                                    .collection("BookedServices")
                                                    .document(productIdToDelete)
                                                    .delete()
                                                    .addOnSuccessListener(aVoid1 -> {
                                                        // Successfully deleted from the event's subcollection,
                                                        // you may log or handle if needed
                                                        Toast.makeText(context, "Deleted from event's BookedServices", Toast.LENGTH_SHORT).show();

                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Handle the failure of deletion in subcollection
                                                        Toast.makeText(context, "Failed to delete from event's BookedServices: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the failure of fetching events
                                        Toast.makeText(context, "Failed to fetch events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                            // Show the popup dialog after deletion
                            //showPopupDialog(holder.itemView);
                            // Remove the item from the local list after successful deletion
                            carts.remove(position);
                            // Notify the adapter about the removed item
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, carts.size());

                            // Parse the price and notify the listener to update the total
                            try {
                                double itemPrice = Double.parseDouble(cart.getPrice());
                                if (onItemDeleteListener != null) {
                                    onItemDeleteListener.onItemDeleted(itemPrice);
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure of deletion from BookedServices
                            Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        FirebaseFirestore.getInstance().collection("Users").document(cart.getCreatorId())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String bidderEmail = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        String name = documentSnapshot.getString("name");
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        holder.chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(holder.itemView.getContext(), chatRoom.class);
                                intent.putExtra("userId", cart.getCreatorId());
                                intent.putExtra("userName", name);
                                intent.putExtra("userEmail", bidderEmail);
                                intent.putExtra("userImage", profileImageUrl);
                                intent.putExtra("userPhone", phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                holder.itemView.getContext().startActivity(intent);

                            }
                        });

                    }
                }).addOnFailureListener(e -> Toast.makeText(context, "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        FirebaseFirestore.getInstance().collection("BookedServices")
                .whereEqualTo("creatorId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            holder.chat.setVisibility(View.GONE);
                        }else {
                            holder.chat.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "Error fetching booked services: " + e.getMessage()));


    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,packageName, price;
        ImageView cover;
        TextView delete;
        ImageView chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardName);
            packageName = itemView.findViewById(R.id.cardPackage);
            price = itemView.findViewById(R.id.cardPrice);
            cover = itemView.findViewById(R.id.cover);
            delete = itemView.findViewById(R.id.delete);
            chat = itemView.findViewById(R.id.chat);
        }
    }
    private void showPopupDialog(View view) {
        Dialog popupDialog = new Dialog(view.getContext());
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setCancelable(false);
        popupDialog.setContentView(R.layout.popup_layout);

        if (popupDialog.getWindow() != null) {
            popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        popupDialog.show();

        new Handler().postDelayed(() -> {
            if (popupDialog.isShowing()) {
                popupDialog.dismiss();
            }
        }, 3000);
    }
}
