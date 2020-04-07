package com.tamada.chatdemo.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by inventbird on 31/10/17.
 */

@IgnoreExtraProperties
public class UserModel {
    private String id;
    private String userName;
    private String password;
    private String phoneNumber;
    private String connectionId;

    public UserModel() {
    }

    public UserModel(String id, String userName, String phoneNumber, String password) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
