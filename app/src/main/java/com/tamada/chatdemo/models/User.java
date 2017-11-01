package com.tamada.chatdemo.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by inventbird on 31/10/17.
 */

@IgnoreExtraProperties
public class User {
    private String id;
    private String userName;
    private String email;
    private String password;
    private boolean isPaid;

    public User() {
    }

    public User(String id, String userName, String email, String password, boolean isPaid) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isPaid = isPaid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
