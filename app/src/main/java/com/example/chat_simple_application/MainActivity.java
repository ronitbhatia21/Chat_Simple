package com.example.chat_simple_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chat_simple_application.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {
    ImageView search;
    BottomNavigationView bottomNavigationView;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search=findViewById(R.id.search);
        bottomNavigationView=findViewById(R.id.bottom_nav);

//        frameLayout= findViewById(R.id.frame_layout);
        chatFragment= new ChatFragment();
        profileFragment= new ProfileFragment();

//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatFragment).commit();





        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.chats){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
                }if(item.getItemId()==R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,profileFragment).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.chats);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        getFCMToken();
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token= task.getResult();
                Log.i("My token:",token);
                FirebaseUtil.currentUserDetails().update("fcmToken",token);
            }
        });
    }
}