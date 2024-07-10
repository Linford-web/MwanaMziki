package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmuziki.Models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    EditText name, email, password, number, conPassword;
    Button registerBtn;
    CheckBox bossCheck, musicianCheck;
    ProgressBar progressbar;
    ImageView passwordIcon, conPasswordIcon;
    CountryCodePicker countryCodePicker;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    boolean valid = true;
    boolean passwordShowing = false;
    boolean conPasswordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        conPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        bossCheck = findViewById(R.id.bossCheck);
        musicianCheck = findViewById(R.id.musicianCheck);
        number = findViewById(R.id.phone);
        progressbar = findViewById(R.id.progressbar);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        countryCodePicker = findViewById(R.id.countryCodePicker);

        // country code picker
        countryCodePicker.registerCarrierNumberEditText(number);

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

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing){
                    password.setTransformationMethod(null);
                    passwordIcon.setImageResource(R.drawable.visibility_icon);
                    passwordShowing = false;
                }
                else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordIcon.setImageResource(R.drawable.visibility_off_icon);
                    passwordShowing = true;
                }
                // move cursor to the end of the password field
                password.setSelection(password.getText().length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPasswordShowing){
                    conPassword.setTransformationMethod(null);
                    conPasswordIcon.setImageResource(R.drawable.visibility_icon);
                    conPasswordShowing = false;
                }
                else {
                    conPassword.setTransformationMethod(new PasswordTransformationMethod());
                    conPasswordIcon.setImageResource(R.drawable.visibility_off_icon);
                    conPasswordShowing = true;
                }
                // move cursor to the end of the password field
                conPassword.setSelection(password.getText().length());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(name);
                checkField(email);
                checkField(number);
                checkField(password);
                checkField(conPassword);

                //checkbox validation
                if (!(bossCheck.isChecked() || musicianCheck.isChecked())){
                    Toast.makeText(registerActivity.this, "Select User Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password and confirm password is the same
                if (!(password.getText().toString().equals(conPassword.getText().toString()))){
                    Toast.makeText(registerActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    valid = true;
                }
                if (!countryCodePicker.isValidFullNumber()){
                    number.setError("Invalid Phone Number");
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
                                    // df.set(User);

                                    UserModel userModel = new UserModel("", user.getUid(), name.getText().toString(), email.getText().toString(), number.getText().toString());
                                    fStore.collection("Users").document(user.getUid()).set(userModel);

                                    Intent intent = new Intent(registerActivity.this, loginActivity.class);
                                    intent.putExtra("mobile", countryCodePicker.getFullNumberWithPlus());
                                    intent.putExtra("email", email.getText().toString());
                                    intent.putExtra("password", password.getText().toString());
                                    intent.putExtra("name", name.getText().toString());
                                    intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                                    startActivity(intent);
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