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
import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.AndroidUtil;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.ViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserModel model) {
        holder.username.setText(model.getUsername());
        holder.phone_number.setText(model.getMobile());
        if (model.getUserId().equals(FirebaseUtil.currentUserId())) {
            holder.username.setText(model.getUsername()+"(Me)");
        }
        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if ((t.isSuccessful())){
                        Uri uri= t.getResult();
                        AndroidUtil.setProfilePic(context,uri,holder.profileImage);
                    }
                });
        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AndroidUtil.passUserModelAsIntent(intent,model);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row,parent,false);
    return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username,phone_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage= itemView.findViewById(R.id.profile_view);
            username=itemView.findViewById(R.id.user_text);
            phone_number=itemView.findViewById(R.id.mobile_text);

        }
    }
}
