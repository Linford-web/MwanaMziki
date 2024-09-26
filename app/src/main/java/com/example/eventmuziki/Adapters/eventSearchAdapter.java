package com.example.eventmuziki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.Models.eventModel;
import com.example.eventmuziki.R;
import com.example.eventmuziki.chatRoom;
import com.example.eventmuziki.eventBidding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class eventSearchAdapter extends FirestoreRecyclerAdapter<eventModel, eventSearchAdapter.ViewHolder> {

    Context context;

    public eventSearchAdapter(FirestoreRecyclerOptions<eventModel> option, Context context) {
        super(option);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventDate;
        ImageView posterTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_Name);
            eventDate = itemView.findViewById(R.id.event_Date);
            posterTv = itemView.findViewById(R.id.poster_Tv);

        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull eventModel model) {

        holder.eventName.setText(model.getEventName());
        holder.eventDate.setText(model.getDate());

        String eventId = model.getEventId();
        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String eventPoster = documentSnapshot.getString("eventPoster");
                            if (eventPoster != null && !eventPoster.isEmpty()) {
                                Glide.with(holder.posterTv.getContext())
                                        .load(eventPoster)
                                        .placeholder(R.drawable.cover)
                                        .error(R.drawable.cover)
                                        .into(holder.posterTv);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, eventBidding.class);
                intent.putExtra("eventModel", model);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_view3, parent, false);
        return new ViewHolder(view);
    }



}
