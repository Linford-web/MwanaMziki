package com.example.eventmuziki.Adapters.serviceAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookedServicesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ServicesDetails.cartModel cart = carts.get(position);
        String productName = cart.getName();
        String productType = cart.getpType();
        String productPrice = cart.getPrice();
        String productId = cart.getProductId();

        holder.name.setText(productName);
        holder.packageName.setText(productType);
        holder.price.setText(productPrice);

        FirebaseFirestore.getInstance().collection("Products")
                .document(productId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String productImage = documentSnapshot.getString("image");
                        Glide.with(context).load(productImage)
                                .placeholder(R.drawable.cover)
                                .error(R.drawable.camera_off)
                                .into(holder.cover);
                    }
                }).addOnFailureListener(Throwable::printStackTrace);

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



    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,packageName, price;
        ImageView cover;
        TextView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardName);
            packageName = itemView.findViewById(R.id.cardPackage);
            price = itemView.findViewById(R.id.cardPrice);
            cover = itemView.findViewById(R.id.cover);
            delete = itemView.findViewById(R.id.delete);
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
