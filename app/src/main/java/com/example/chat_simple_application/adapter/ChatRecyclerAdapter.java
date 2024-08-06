package com.example.chat_simple_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_simple_application.R;
import com.example.chat_simple_application.model.ChatMessageModel;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel,ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if(model.getSenderId().equals(FirebaseUtil.currentUserId())){
            holder.left_chat.setVisibility(View.GONE);
            holder.right_chat.setVisibility(View.VISIBLE);
            holder.right_message.setText(model.getMessage());
        }else{
            holder.right_chat.setVisibility(View.GONE);
            holder.left_chat.setVisibility(View.VISIBLE);
            holder.left_message.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recycler_row,parent,false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout left_chat,right_chat;
        TextView left_message,right_message;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            left_chat=itemView.findViewById(R.id.left_chat_layout);
            right_chat=itemView.findViewById(R.id.right_chat_layout);
            left_message=itemView.findViewById(R.id.left_chat_textview);
            right_message=itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
