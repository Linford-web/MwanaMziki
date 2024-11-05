package com.example.eventmuziki;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.dashAdapter;
import com.example.eventmuziki.Models.serviceNameModel;

import java.util.ArrayList;
import java.util.Objects;

public class walletActivity extends AppCompatActivity {

    RecyclerView menu;
    dashAdapter menuAdapter;
    ImageButton back, notification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallet);


        back = findViewById(R.id.back_arrow);
        notification = findViewById(R.id.notificationBtn);
        menu = findViewById(R.id.walletMenu);


        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        back.setOnClickListener(v -> {
            finish();
        });
        notification.setOnClickListener(v -> {
            // Handle notification button click
        });
        // Set up RecyclerView
        setUpRecyclerView();




    }

    private void setUpRecyclerView() {
        ArrayList<serviceNameModel> menuItems = new ArrayList<>();

        menuItems.add(new serviceNameModel("Withdraw", R.drawable.withdraw_icon, walletActivity.class));
        menuItems.add(new serviceNameModel("Deposit", R.drawable.deposit_icon, walletActivity.class));
        menuItems.add(new serviceNameModel("Expenses", R.drawable.transaction, walletActivity.class));
        menuItems.add(new serviceNameModel("Receipts", R.drawable.receipt, walletActivity.class));

        menuAdapter = new dashAdapter(walletActivity.this, menuItems);
        menu.setLayoutManager(new GridLayoutManager(this, 4));
        menu.setAdapter(menuAdapter);
    }



}