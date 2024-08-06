package com.example.chat_simple_application;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_simple_application.adapter.ChatRecyclerAdapter;
import com.example.chat_simple_application.model.ChatMessageModel;
import com.example.chat_simple_application.model.ChatRoomModel;
import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.AndroidUtil;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.common.net.MediaType;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
//import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    UserModel otherusermodel;
    TextView username_text;
    ImageView profile,message_send,back;
    EditText message_edit;
    RecyclerView recyclerView;
    ChatRecyclerAdapter chatRecyclerAdapter;
    ChatRoomModel chatRoomModel;
    String chatroomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherusermodel= AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId= FirebaseUtil.getChatRoomID(FirebaseUtil.currentUserId(),otherusermodel.getUserId()); // get both userId



        profile=findViewById(R.id.send_profile);
        username_text=findViewById(R.id.send_username_text);
        message_edit=findViewById(R.id.message_edit);
        back=findViewById(R.id.back_chat);
        message_send=findViewById(R.id.message_send);
        recyclerView= findViewById(R.id.recyler_v);

        FirebaseUtil.getOtherProfilePicStorageRef(otherusermodel.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if ((t.isSuccessful())){
                        Uri uri= t.getResult();
                        AndroidUtil.setProfilePic(this,uri,profile);
                    }
                });





        // get userModel
        username_text.setText(otherusermodel.getUsername());

        message_send.setOnClickListener(v -> {
            String message= message_edit.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessageToUser(message);

        });

        getOrCreateChatRoomModel();
        setUpChatRecyclerView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });
    }

    private void setUpChatRecyclerView() {
        Query query= FirebaseUtil.getChatRoomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ChatMessageModel> options= new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

         chatRecyclerAdapter= new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true); // message chat will be sent vertical straight
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoomModel.setLastmessage(message); // send by the user
        FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel);

        ChatMessageModel chatMessageModel= new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatRoomMessageReference(chatroomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    message_edit.setText("");
//                    sendNotification(message);
                }
            }
        });
    }

    private void getOrCreateChatRoomModel() {
        FirebaseUtil.getChatRoomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel=task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel== null){
                    //first time chat
                    chatRoomModel= new ChatRoomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherusermodel.getUserId()),
                            Timestamp.now(),
                    ""
                    );
                    FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel);
                }

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if(chatRecyclerAdapter!=null){
            chatRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatRecyclerAdapter!=null){
            chatRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(chatRecyclerAdapter!=null){
            chatRecyclerAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (chatRecyclerAdapter != null) {
            chatRecyclerAdapter.stopListening(); // Stop listening to Firestore data
        }
    }
//    void sendNotification(String message) {
//        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    UserModel currentuser = task.getResult().toObject(UserModel.class);
//                    if (currentuser != null) {
//                        try {
//                            JsonObject jsonObject = new JsonObject();
//                            JsonObject notification = new JsonObject();
//                            notification.addProperty("title", currentuser.getUsername());
//                            notification.addProperty("body", message);
//
//                            JsonObject dataObj = new JsonObject();
//                            dataObj.addProperty("userId", currentuser.getUserId());
//
//                            jsonObject.add("notification", notification);
//                            jsonObject.add("data", dataObj);
//                            jsonObject.addProperty("to", otherusermodel.getFcmToken());
//
//                            callApi(jsonObject);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    void callApi(JsonObject jsonObject) {
//        okhttp3.MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");
//
//        OkHttpClient client = new OkHttpClient();
//        String url = "https://fcm.googleapis.com/fcm/send";
//
//        Accesstoken accesstoken = new Accesstoken();
//        String accessToken = accesstoken.getAccessToken();
//
//        if (accessToken == null) {
//            Log.e("Error", "Failed to get access token");
//            return;
//        }
//
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .header("Authorization", "Bearer " + accessToken)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("Error", "Failed to send notification: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    Log.d("Success", "Notification sent successfully");
//                } else {
//                    Log.e("Error", "Failed to send notification: " + response.message());
//                }
//            }
//        });
//    }
}