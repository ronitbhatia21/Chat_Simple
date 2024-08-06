package com.example.chat_simple_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_simple_application.adapter.RecentChatRecyclerAdapter;
import com.example.chat_simple_application.model.ChatRoomModel;
import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    RecentChatRecyclerAdapter recyclerAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.chat_recycler);
        setupSearchRecyclerView();
        return view;
    }

    private void setupSearchRecyclerView() {

        Query query= FirebaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userIds",FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);

//                .whereLessThanOrEqualTo("username", searchUser + "\uf8ff");


        FirestoreRecyclerOptions<ChatRoomModel> options= new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class).build();

        recyclerAdapter = new RecentChatRecyclerAdapter(options,getContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recyclerAdapter!=null){
            recyclerAdapter.startListening();
        }else {
            setupSearchRecyclerView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(recyclerAdapter!=null){
            recyclerAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerAdapter!=null){
            recyclerAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (recyclerAdapter != null) {
            recyclerAdapter.stopListening(); // Stop listening to Firestore data
        }
    }
}