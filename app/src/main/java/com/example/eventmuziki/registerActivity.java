package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    EditText name, email, password, number;;
    Button registerBtn;
    CheckBox bossCheck, musicianCheck;
    ProgressBar progressbar;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        registerBtn = findViewById(R.id.registerBtn);
        bossCheck = findViewById(R.id.bossCheck);
        musicianCheck = findViewById(R.id.musicianCheck);
        number = findViewById(R.id.phone);
        progressbar = findViewById(R.id.progressbar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // check boxes logic
        musicianCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    bossCheck.setChecked(false);
                }

            }
        });
        bossCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    musicianCheck.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(name);
                checkField(email);
                checkField(number);
                checkField(password);

                //checkbox validation
                if (!(bossCheck.isChecked() || musicianCheck.isChecked())){
                    Toast.makeText(registerActivity.this, "Select User Type", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (valid){

                    progressbar.setVisibility(View.VISIBLE);
                    // start the user registration process
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user =fAuth.getCurrentUser();

                                    Toast.makeText(registerActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    assert user != null;
                                    DocumentReference df = fStore.collection("Users")
                                            .document(user.getUid());
                                    Map<String,Object> User= new HashMap<>();
                                    User.put("userid",user.getUid());
                                    User.put("name", name.getText().toString());
                                    User.put("number", number.getText().toString());
                                    User.put("email", email.getText().toString());

                                    // specify Access Level
                                    if (bossCheck.isChecked()){
                                        User.put("userType", "Corporate");
                                    }
                                    if (musicianCheck.isChecked()){
                                        User.put("userType", "Musician");
                                    }
                                    df.set(User);
                                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                    finish();
                                    progressbar.setVisibility(View.GONE);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(registerActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });

                }



            }

        });

    }

    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

    }
}