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
    private boolean paymentStatus;

    public boolean isPaid() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public UserModel() {
    }

    public UserModel(String id, String userName, String password, boolean paymentStatus) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.paymentStatus = paymentStatus;
    }

    public UserModel(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
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
