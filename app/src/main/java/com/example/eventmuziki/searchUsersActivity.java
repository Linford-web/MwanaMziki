package com.example.eventmuziki;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.searchAdapter;
import com.example.eventmuziki.Models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class searchUsersActivity extends AppCompatActivity {

    EditText searchTxt;
    RecyclerView recyclerView;
    ImageButton back;
    ImageButton searchBtn;
    searchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_users);

        searchTxt = findViewById(R.id.searchUserInput);
        recyclerView = findViewById(R.id.searchRv);
        back = findViewById(R.id.back_arrow);
        searchBtn = findViewById(R.id.search_icon);
        searchTxt.requestFocus();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String searchTerm = searchTxt.getText().toString();
               if (searchTerm.isEmpty() || searchTerm.length()<3) {
                   searchTxt.setError("Enter at least 3 characters");
                   return;
               }
               setUpRecyclerView(searchTerm);
            }
        });

    }

    private void setUpRecyclerView(String searchTerm) {

        Query query = FirebaseFirestore.getInstance().collection("Users")
                .whereGreaterThanOrEqualTo("name", searchTerm);
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter = new searchAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }

}