package com.example.eventmuziki;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {

    EditText otpText;
    Button actionBtn;
    TextView resend, emailTxt, phoneTxt, cancelBtn;
    ProgressBar progressbar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    private boolean resendEnabled = false;
    private boolean otpSent = false;


    private int resendCounter = 60;
    String verificationCode, mobile, email, password, name, number, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);

        FirebaseApp.initializeApp(this);
        actionBtn = findViewById(R.id.verifyBtn);
        resend = findViewById(R.id.resendBtn);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        otpText = findViewById(R.id.otpEditTxt);
        progressbar = findViewById(R.id.progressbar);
        cancelBtn = findViewById(R.id.cancelBtn);
        fAuth = FirebaseAuth.getInstance();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPVerification.this, registerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        email = getIntent().getStringExtra("email");
        mobile = getIntent().getStringExtra("mobile");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");
        userType = getIntent().getStringExtra("userType");

        emailTxt.setText(email);
        phoneTxt.setText(mobile);

        sendOTPToPhone(mobile, false);

       resend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (resendEnabled){
                   resendCounter = 60;
                   resendEnabled = false;
                   sendOTPToPhone(mobile, true);
                   startResendTimer();
                   otpText.setText("");
               }
           }
       });

       actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otpSent){
                    String code = otpText.getText().toString();
                    if(code.isEmpty() || code.length() < 6){
                        Toast.makeText(OTPVerification.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
                        signInWithPhoneAuthCredential(credential);
                        setInProgress(true);
                    }
                } else {
                    String mobile = phoneTxt.getText().toString();
                    sendOTPToPhone(mobile, false);

                }

            }
        });

    }

    private void sendOTPToPhone(String mobile, boolean isResend) {
        setInProgress(true);
        startResendTimer();

        PhoneAuthOptions.Builder builders = PhoneAuthOptions.newBuilder(fAuth)
                .setPhoneNumber(mobile)
                .setTimeout(60l, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPVerification.this, "Something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        otpSent = true;
                        setInProgress(false);
                        actionBtn.setText("Verify OTP");
                        startResendTimer();
                        Toast.makeText(OTPVerification.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builders.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builders.build());
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        setInProgress(true);
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    // registerUser();
                    Toast.makeText(OTPVerification.this, "Verified Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(OTPVerification.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  void startResendTimer(){

        resendEnabled = true;
        resend.setTextColor(Color.parseColor("#FF0000"));

        new CountDownTimer(resendCounter * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText("Resend in (" +millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resend.setText("Resend Code");
                resend.setTextColor(Color.parseColor("#FF0000FF"));
            }
        }.start();

    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressbar.setVisibility(View.VISIBLE);
            actionBtn.setVisibility(View.INVISIBLE);

        } else {
            progressbar.setVisibility(View.GONE);
            actionBtn.setVisibility(View.VISIBLE);

        }

    }

    private void registerUser() {

        progressbar.setVisibility(View.VISIBLE);
        // start the user registration process
        fAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser user =fAuth.getCurrentUser();

                        Toast.makeText(OTPVerification.this, "Account Created", Toast.LENGTH_SHORT).show();

                        if (user != null){
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String,Object> User= new HashMap<>();
                            User.put("userid",user.getUid());
                            User.put("name", name);
                            User.put("number", number);
                            User.put("email", email);
                            User.put("userType", userType);

                            df.set(User);
                            startActivity(new Intent(getApplicationContext(), loginActivity.class));
                            finish();
                        }
                        progressbar.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(OTPVerification.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                    }
                });

    }

}