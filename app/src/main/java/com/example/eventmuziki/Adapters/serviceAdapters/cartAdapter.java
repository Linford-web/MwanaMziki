package com.example.eventmuziki.Adapters.serviceAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.serviceModels.ServicesDetails;
import com.example.eventmuziki.R;
import com.example.eventmuziki.cartActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    ArrayList<ServicesDetails.cartModel> carts;
    Context context;

    public cartAdapter(ArrayList<ServicesDetails.cartModel> carts, Context context) {
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
    public cartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                                .placeholder(R.drawable.blank_photo)
                                .error(R.drawable.blank_photo)
                                .into(holder.cover);
                    }
                }).addOnFailureListener(Throwable::printStackTrace);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the product ID for deletion from Firestore
                String productIdToDelete = cart.getCartId();

                // Delete the item from Firestore (if applicable)
                FirebaseFirestore.getInstance().collection("Cart")
                        .document(productIdToDelete)
                        .delete()
                        .addOnSuccessListener(aVoid -> {

                            // Show the popup dialog after deletion
                            showPopupDialog(holder.itemView);
                            // Remove the item from the local list after successful deletion
                            carts.remove(position);
                            // Notify the adapter about the removed item
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, carts.size());
                            // Parse the price and notify the listener
                            try {
                                double itemPrice = Double.parseDouble(cart.getPrice());
                                if (onItemDeleteListener != null) {
                                    onItemDeleteListener.onItemDeleted(itemPrice);
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            // Recalculate the total amount based on the remaining items
                            recalculateTotalAmount();
                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure of deletion
                            Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private void recalculateTotalAmount() {
        double totalAmount = 0.0;

        for (ServicesDetails.cartModel cart : carts) {
            String priceString = cart.getPrice();
            if (priceString != null && !priceString.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceString);
                    totalAmount += price;
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Handle conversion error
                }
            }
        }

        // Update the total in your cart activity
        ((cartActivity) context).updateTotalAmount(totalAmount);
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
