package com.example.chat_simple_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.AndroidUtil;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseUtil.isLoggedIn() && getIntent().hasExtra("userId")){
            //from notification
            String userId= getIntent().getStringExtra("userId");
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()&& task.getResult().exists()){
                                UserModel model=task.getResult().toObject(UserModel.class);
                                Intent mainIntent= new Intent(SplashActivity.this,MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(mainIntent);

                                Intent intent= new Intent(SplashActivity.this, ChatActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                AndroidUtil.passUserModelAsIntent(intent,model);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }
else{


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (FirebaseUtil.isLoggedIn()) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {


                        Intent intent = new Intent(SplashActivity.this, LoginMobileNumber.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }, 1000);

        }
    }
}