package com.example.eventmuziki;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmuziki.Adapters.dashAdapter;
import com.example.eventmuziki.Adapters.menuAdapter;
import com.example.eventmuziki.Models.menuModel;
import com.example.eventmuziki.Models.serviceNameModel;
import com.example.eventmuziki.Models.walletModels.walletModel;

import java.util.ArrayList;
import java.util.Objects;

public class walletActivity extends AppCompatActivity {

    RecyclerView menu, depositMenuRv, withdrawMenuRv;
    ImageButton back, notification;
    LinearLayout depositLayout, withdrawLayout, transactionLayout, receiptsLayout,
            mPesaSection, visaSection, payPalSection, mPesaWithdrawSection, visaWithdrawSection, payPalWithdrawSection;

    ArrayList<walletModel.walletMenu> menuItem, menuItem2, menuItem3;
    menuAdapter adapter, depositAdapter, withdrawAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallet);

        back = findViewById(R.id.back_arrow);
        notification = findViewById(R.id.notificationBtn);
        menu = findViewById(R.id.walletMenu);
        depositLayout = findViewById(R.id.depositMoneySection);
        withdrawLayout = findViewById(R.id.withdrawMoneySection);
        transactionLayout = findViewById(R.id.transactionsSection);
        receiptsLayout = findViewById(R.id.receiptsLayout);
        mPesaSection = findViewById(R.id.mPesaSection);
        visaSection = findViewById(R.id.visaSection);
        payPalSection = findViewById(R.id.payPalSection);
        mPesaWithdrawSection = findViewById(R.id.withdrawMpesaSection);
        visaWithdrawSection = findViewById(R.id.withdrawVisaSection);
        payPalWithdrawSection = findViewById(R.id.payPalWithdrawSection);
        depositMenuRv = findViewById(R.id.depositMenuRv);
        withdrawMenuRv = findViewById(R.id.withdrawMenuRv);

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
        
        setUpDepositRecyclerview();
        
        setUpWithdrawRecyclerview();

        adapter.setOnItemClickListener(new menuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                if (itemName.equals("Deposit")) {
                    depositLayout.setVisibility(View.VISIBLE);
                    withdrawLayout.setVisibility(View.GONE);
                    transactionLayout.setVisibility(View.GONE);
                    receiptsLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Withdraw")) {
                    withdrawLayout.setVisibility(View.VISIBLE);
                    depositLayout.setVisibility(View.GONE);
                    transactionLayout.setVisibility(View.GONE);
                    receiptsLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("History")) {
                    transactionLayout.setVisibility(View.VISIBLE);
                    depositLayout.setVisibility(View.GONE);
                    withdrawLayout.setVisibility(View.GONE);
                    receiptsLayout.setVisibility(View.GONE);
                }
                if (itemName.equals("Reports")) {
                    receiptsLayout.setVisibility(View.VISIBLE);
                    depositLayout.setVisibility(View.GONE);
                    withdrawLayout.setVisibility(View.GONE);
                    transactionLayout.setVisibility(View.GONE);
                }
            }
        });







    }



    private void setUpDepositRecyclerview() {
        ArrayList<walletModel.walletMenu> depositItems = new ArrayList<>();
        depositItems.add(new walletModel.walletMenu("M-pesa", R.drawable.money_icon));
        depositItems.add(new walletModel.walletMenu("Visa", R.drawable.money_icon));
        depositItems.add(new walletModel.walletMenu("PayPal", R.drawable.money_icon));

        depositAdapter = new menuAdapter(this, depositItems);
        depositMenuRv.setLayoutManager(new GridLayoutManager(this, 3));
        depositMenuRv.setAdapter(depositAdapter);

        depositAdapter.setOnItemClickListener(new menuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                if (itemName.equals("M-pesa")) {
                    mPesaSection.setVisibility(View.VISIBLE);
                    visaSection.setVisibility(View.GONE);
                    payPalSection.setVisibility(View.GONE);
                } else if (itemName.equals("Visa")) {
                    visaSection.setVisibility(View.VISIBLE);
                    mPesaSection.setVisibility(View.GONE);
                    payPalSection.setVisibility(View.GONE);
                } else if (itemName.equals("PayPal")) {
                    payPalSection.setVisibility(View.VISIBLE);
                    mPesaSection.setVisibility(View.GONE);
                    visaSection.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setUpWithdrawRecyclerview() {
        ArrayList<walletModel.walletMenu> withdrawItems = new ArrayList<>();
        withdrawItems.add(new walletModel.walletMenu("M-pesa", R.drawable.money_icon));
        withdrawItems.add(new walletModel.walletMenu("Visa", R.drawable.money_icon));
        withdrawItems.add(new walletModel.walletMenu("PayPal", R.drawable.money_icon));

        withdrawAdapter = new menuAdapter(this, withdrawItems);
        withdrawMenuRv.setLayoutManager(new GridLayoutManager(this, 3));
        withdrawMenuRv.setAdapter(withdrawAdapter);

        withdrawAdapter.setOnItemClickListener(new menuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                if (itemName.equals("M-pesa")) {
                    mPesaWithdrawSection.setVisibility(View.VISIBLE);
                    visaWithdrawSection.setVisibility(View.GONE);
                    payPalWithdrawSection.setVisibility(View.GONE);
                }
                if (itemName.equals("Visa")) {
                    visaWithdrawSection.setVisibility(View.VISIBLE);
                    mPesaWithdrawSection.setVisibility(View.GONE);
                    payPalWithdrawSection.setVisibility(View.GONE);
                }
                if (itemName.equals("PayPal")) {
                    payPalWithdrawSection.setVisibility(View.VISIBLE);
                    mPesaWithdrawSection.setVisibility(View.GONE);
                    visaWithdrawSection.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setUpRecyclerView() {
        ArrayList<walletModel.walletMenu> menuItems = new ArrayList<>(); // Local array
        menuItems.add(new walletModel.walletMenu("History", R.drawable.transaction));
        menuItems.add(new walletModel.walletMenu("Deposit", R.drawable.deposit_icon));
        menuItems.add(new walletModel.walletMenu("Withdraw", R.drawable.withdraw_icon));
        menuItems.add(new walletModel.walletMenu("Reports", R.drawable.receipt));

        adapter = new menuAdapter(this, menuItems);
        menu.setLayoutManager(new GridLayoutManager(this, 4));
        menu.setAdapter(adapter);

    }


}