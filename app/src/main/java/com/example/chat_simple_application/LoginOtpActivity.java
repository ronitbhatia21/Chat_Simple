package com.example.chat_simple_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chat_simple_application.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    EditText log_otp;
    Button next,resend;
    ProgressBar progressBar;
    String mobileNumber;
    Long timeoutSeconds= 60L;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance(); //Used for user authentication with Firebase.

    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;  //Used for phone number verification with Firebase.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mobileNumber= getIntent().getExtras().getString("mobile");
//        Toast.makeText(getApplicationContext(),mobile_number,Toast.LENGTH_SHORT).show();

                log_otp=findViewById(R.id.login_otp);
        next=findViewById(R.id.next);
        resend=findViewById(R.id.resend_otp);
        progressBar= findViewById(R.id.progress_circular);

        sendOtp(mobileNumber,false);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = log_otp.getText().toString();
           PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
           signIn(phoneAuthCredential);
           setInProgress(true);

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(mobileNumber,true);
            }
        });

    }


    void sendOtp(String mobileNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        // phoneAuthOptions- Configures phone number verification behavior (timeout, activity reference, callbacks).
        PhoneAuthOptions.Builder builder=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(mobileNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            // verification completed
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                              signIn(phoneAuthCredential);
                              setInProgress(false);
                            }

                            // failed
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                                setInProgress(false);
                            }

                            //otp sent to user
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode=s;
                                resendingToken=forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully");
                                setInProgress(false);
                            }
                        });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }


    }

    private void startResendTimer() {
        resend.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resend.setText("Resend OTP in " + timeoutSeconds + "seconds");
                        if (timeoutSeconds <= 0) {
                            timeoutSeconds = 60L;
                            timer.cancel();
                            resend.setEnabled(true);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {

        // login part and move to next activity

        setInProgress(true);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    setInProgress(false);
                    Intent intent= new Intent(LoginOtpActivity.this,LoginUserNameActivity.class);
                    intent.putExtra("phone",mobileNumber);
                    startActivity(intent);

                }else{
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                }

            }
        });

    }

    void setInProgress(boolean inProgress){
        if(inProgress) { // true
            progressBar.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        }    else{
                progressBar.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            }

    }
}