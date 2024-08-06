package com.example.chat_simple_application.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String mobile;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;


    public UserModel(String mobile, String username, Timestamp createdTimestamp, String userId) {
        this.mobile = mobile;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }
    public UserModel() {
        this.mobile = "";
        this.username = "";
        this.createdTimestamp = null;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
