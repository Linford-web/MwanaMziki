package com.example.eventmuziki;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Objects;

public class registerActivity extends AppCompatActivity {

    EditText name, email, password, number, conPassword;
    Button registerBtn;
    CheckBox bossCheck, musicianCheck, termsAndCondition, normalCheck;
    ProgressBar progressbar;
    ImageView passwordIcon, conPasswordIcon;
    CountryCodePicker countryCodePicker;
    TextView loginTxt;
    Spinner spinner;
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

        // Hide keyboard when the activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        loginTxt = findViewById(R.id.loginTxt);
        spinner = findViewById(R.id.spinner_category);
        termsAndCondition = findViewById(R.id.termsCheck);
        normalCheck = findViewById(R.id.normalCheck);
        registerBtn.setEnabled(false);
        registerBtn.setBackgroundColor(getResources().getColor(R.color.gray));

        // Initialize ArrayAdapter and set it to the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.User_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        termsAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                registerBtn.setEnabled(compoundButton.isChecked());
                registerBtn.setBackgroundColor(compoundButton.isChecked() ? getResources().getColor(R.color.orange) : getResources().getColor(R.color.gray));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle selection here
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Do something with the selected category
                Log.d("Selected Category", selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // set default value as blank
                //spinner.setSelection(0);
                // Do nothing if nothing is selected
                Log.d("Selected Category", "Nothing selected");
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        // country code picker
        countryCodePicker.registerCarrierNumberEditText(number);

        // check boxes logic
        musicianCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    bossCheck.setChecked(false);
                    spinner.setVisibility(View.VISIBLE);
                    bossCheck.setVisibility(View.GONE);
                    spinner.setSelection(0);

                }else {
                    bossCheck.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                }

            }
        });
        bossCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    musicianCheck.setChecked(false);
                    spinner.setVisibility(View.GONE);
                }
            }
        });
        normalCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    bossCheck.setChecked(false);
                    musicianCheck.setChecked(false);
                    spinner.setVisibility(View.GONE);
                } else {
                    bossCheck.setChecked(false);
                    musicianCheck.setChecked(false);
                    spinner.setVisibility(View.GONE);
                }
            }
        });

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
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPasswordShowing){
                    conPassword.setTransformationMethod(null);
                    conPasswordIcon.setImageResource(R.drawable.visibility_off_icon);
                    conPasswordShowing = false;
                }
                else {
                    conPassword.setTransformationMethod(new PasswordTransformationMethod());
                    conPasswordIcon.setImageResource(R.drawable.visibility_icon);
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
                if (!(bossCheck.isChecked() || musicianCheck.isChecked() || normalCheck.isChecked())){
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

                    String phone = countryCodePicker.getFullNumberWithPlus();
                    // Get the selected category from the Spinner

                    progressbar.setVisibility(View.VISIBLE);
                    // start the user registration process
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user = fAuth.getCurrentUser();
                                    String selected = spinner.getSelectedItem().toString();

                                    // Create UserModel object
                                    String userType;
                                    if (normalCheck.isChecked()) {
                                        userType = "Normal";
                                    } else if (bossCheck.isChecked()) {
                                        userType = "Organizer";
                                    } else if(musicianCheck.isChecked()) {
                                        userType = "Musician";
                                    } else {
                                        // Handle if no checkbox is checked
                                        userType = "Null";
                                    }

                                    String category = musicianCheck.isChecked() ? selected : "organizer";

                                    UserModel userModel = new UserModel("", Objects.requireNonNull(user).getUid(),
                                            name.getText().toString(),
                                            email.getText().toString(),
                                            phone,
                                            userType,
                                            category);
                                    // Save the UserModel to Firestore
                                    fStore.collection("Users").document(user.getUid())
                                            .set(userModel)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Redirect to MainDashboard
                                                    Intent intent = new Intent(registerActivity.this, MainDashboard.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    progressbar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(registerActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(registerActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                                    progressbar.setVisibility(View.INVISIBLE);
                                                }
                                            });

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(registerActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.INVISIBLE);
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
