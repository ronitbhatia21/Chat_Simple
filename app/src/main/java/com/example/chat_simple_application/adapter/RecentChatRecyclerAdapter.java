package com.example.chat_simple_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_simple_application.ChatActivity;
import com.example.chat_simple_application.R;
import com.example.chat_simple_application.model.ChatRoomModel;
import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.AndroidUtil;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel,RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherUserFromChatRoom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
     //if sent by me it is true
                   boolean lastMessageSendByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());

                   UserModel otheruserModel= task.getResult().toObject(UserModel.class);
                   FirebaseUtil.getOtherProfilePicStorageRef(otheruserModel.getUserId()).getDownloadUrl()
                           .addOnCompleteListener(t -> {
                               if ((t.isSuccessful())){
                                   Uri uri= t.getResult();
                                   AndroidUtil.setProfilePic(context,uri,holder.profileImage);
                               }
                           });
                   holder.username.setText(otheruserModel.getUsername());
                   if(lastMessageSendByMe){
                       holder.last_message.setText("You: "+model.getLastmessage());

                   }else
                     holder.last_message.setText(model.getLastmessage());
                   holder.lastmessagetime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));
                   holder.itemView.setOnClickListener(v -> {
                       Intent intent= new Intent(context, ChatActivity.class);
                       AndroidUtil.passUserModelAsIntent(intent,otheruserModel);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);
                   });



               }
                });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new ChatRoomModelViewHolder(view);
    }


    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username,last_message,lastmessagetime;
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage= itemView.findViewById(R.id.profile_view);
            username=itemView.findViewById(R.id.user_chat_text);
            last_message=itemView.findViewById(R.id.last_message_text);
            lastmessagetime=itemView.findViewById(R.id.last_message_time_text);

        }
    }
}
