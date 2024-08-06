package com.example.chat_simple_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {

    EditText usernameInput;
    Button next_act;
    ProgressBar progressBar;

    String mobileNumber;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        usernameInput=findViewById(R.id.username);
        next_act=findViewById(R.id.let_me_in);
        progressBar= findViewById(R.id.progress_circular);

        mobileNumber=getIntent().getExtras().getString("phone");
        getUsername();


        next_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername();
            }
        });
    }

    void setUsername(){

        String username= usernameInput.getText().toString();
        if (username.isEmpty() || username.length()<3){
            usernameInput.setError("username length should be more than 3 characters");
            return;
        }
        setInProgress(true);
        if(userModel!= null){// user has log in before username is there before
            userModel.setUsername(username);
        }else {
            userModel= new UserModel(mobileNumber,username, Timestamp.now(),FirebaseUtil.currentUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent= new Intent(LoginUserNameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        });

    }

    private void getUsername() {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
            if (task.isSuccessful()){
                userModel= task.getResult().toObject(UserModel.class);
                if(userModel!=null){ // data is there
                    usernameInput.setText(userModel.getUsername());
                }

            }else{

            }
            }
        });
    }


    void setInProgress(boolean inProgress){
        if(inProgress) { // true
            progressBar.setVisibility(View.VISIBLE);
            next_act.setVisibility(View.GONE);
        }    else{
            progressBar.setVisibility(View.GONE);
            next_act.setVisibility(View.VISIBLE);
        }

    }

}