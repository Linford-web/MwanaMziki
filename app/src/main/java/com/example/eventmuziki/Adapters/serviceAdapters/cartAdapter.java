package com.example.eventmuziki.Adapters.serviceAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    @NonNull
    @Override
    public cartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, int position) {
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


    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,packageName, price;
        ImageView cover;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardName);
            packageName = itemView.findViewById(R.id.cardPackage);
            price = itemView.findViewById(R.id.cardPrice);
            cover = itemView.findViewById(R.id.cover);

        }
    }
}
