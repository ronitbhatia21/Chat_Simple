package com.example.chat_simple_application.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static boolean isLoggedIn(){
        if(currentUserId()!= null){
            return true;
        }
            return false;
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public  static DocumentReference getChatRoomReference(String chatroomId){
        return  FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatroomId){
        return  getChatRoomReference(chatroomId).collection("chats");
    }
    public static CollectionReference allChatRoomCollectionReference(){
        return  FirebaseFirestore.getInstance().collection("chatrooms");

    }
    public static String getChatRoomID(String userId1,String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }
        else {
            return userId2+"_"+userId1;
            
        }
    }

    public static DocumentReference getOtherUserFromChatRoom(List<String> userIds){
     if(userIds.get(0).equals(FirebaseUtil.currentUserId())){ // if first one is our userid next one will be of another user
       return allUserCollectionReference().document(userIds.get(1));

     }else {
         return allUserCollectionReference().document(userIds.get(0));
     }

    }
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
    public static String timestampToString(Timestamp timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        // Set the time zone to UTC+5:30
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Replace with your correct time zone
        return formatter.format(timestamp.toDate());

    }
    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserId());
    }
    public static StorageReference getOtherProfilePicStorageRef(String otheruserId){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otheruserId);
    }

}
