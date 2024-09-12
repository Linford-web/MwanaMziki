package com.example.eventmuziki;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    ProgressBar progressbar;
    ImageView passwordIcon;
    boolean valid = true;
    TextView fPassword, registerTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean passwordShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        fPassword = findViewById(R.id.forgot_password);
        fAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        progressbar = findViewById(R.id.progressbar);
        passwordIcon = findViewById(R.id.passwordIcon);
        fStore = FirebaseFirestore.getInstance();
        registerTxt = findViewById(R.id.registerTxt);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordShowing){
                    password.setTransformationMethod(null);
                    passwordIcon.setImageResource(R.drawable.visibility_off_icon);
                    passwordShowing = false;
                }
                else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordIcon.setImageResource(R.drawable.visibility_icon);
                    passwordShowing = true;
                }
                // move cursor to the end of the password field
                password.setSelection(password.getText().length());
            }
        });

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar.setVisibility(View.VISIBLE);
                checkField(email);
                checkField(password);
                if (valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    checkUserAccessLevel(Objects.requireNonNull(authResult.getUser()).getUid());
                                    progressbar.setVisibility(View.INVISIBLE);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        });

        // Deals with the reset password logic
        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(loginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, "Error! Resent Link Not Sent."+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);

        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify User Access Level
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("Corporate") || userType.equals("Musician") || userType.equals("Normal")) {
                        // User is Corporate or Musician
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        finish();
                        Log.d("TAG", "Welcome " + userType );
                    } else {
                        // Handle other user types if needed
                        Log.d("TAG", "User is neither Corporate nor Musician");
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Failed to get document: " + e.getMessage());
                progressbar.setVisibility(View.INVISIBLE);
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