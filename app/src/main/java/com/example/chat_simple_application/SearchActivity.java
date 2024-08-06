package com.example.chat_simple_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.chat_simple_application.adapter.SearchUserRecyclerAdapter;
import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {
    EditText useredit;
    ImageView back,search_edit;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter searchUserRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        useredit=findViewById(R.id.user_edit);
        back=findViewById(R.id.back);
        search_edit=findViewById(R.id.search_edit);
        recyclerView=findViewById(R.id.recyclerview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        search_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_user = useredit.getText().toString();
                if (search_user.isEmpty() || search_user.length() < 3) {
                    useredit.setError("Invalid Username");
                    return;

                }
                setupSearchRecyclerView(search_user);
            }

        });

    }
    private void setupSearchRecyclerView(String searchUser) {

        Query query= FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchUser)
        .whereLessThanOrEqualTo("username", searchUser + "\uf8ff");


        FirestoreRecyclerOptions<UserModel> options= new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        searchUserRecyclerAdapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(searchUserRecyclerAdapter);
        searchUserRecyclerAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(searchUserRecyclerAdapter!=null){
            searchUserRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(searchUserRecyclerAdapter!=null){
            searchUserRecyclerAdapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchUserRecyclerAdapter!=null){
            searchUserRecyclerAdapter.startListening();
        }
    }
}