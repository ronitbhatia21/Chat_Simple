package com.example.chat_simple_application.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chat_simple_application.model.UserModel;

public class AndroidUtil {

  public static void showToast(Context context,String message){
      Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel userModel){
      intent.putExtra("username",userModel.getUsername());
      intent.putExtra("userId",userModel.getUserId());
      intent.putExtra("mobile",userModel.getMobile());
//      intent.putExtra("fcmtoken",userModel.getFcmToken());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
    UserModel userModel= new UserModel();
    userModel.setUsername(intent.getStringExtra("username"));
    userModel.setUserId(intent.getStringExtra("userId"));
    userModel.setMobile(intent.getStringExtra("mobile"));
//    userModel.setFcmToken(intent.getStringExtra("fcmtoken"));
    return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri,ImageView imageView){
      Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
