package com.tamada.chatdemo.models;

/**
 * Created by LSN-ANDROID2 on 05-11-2018.
 */

public class AuthModel {
    private String userId;
    private UserModel userModel;

    public AuthModel(String userId, UserModel userModel) {
        this.userId = userId;
        this.userModel = userModel;
    }

    public AuthModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
