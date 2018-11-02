package com.tamada.chatdemo.models;

/**
 * Created by inventbird on 31/10/17.
 */

public class MessagesModel {
    private String fromName;
    private String fromId;
    private String message;

    public MessagesModel() {
    }

    public MessagesModel(String fromName, String fromId, String message) {
        this.fromName = fromName;
        this.fromId = fromId;
        this.message = message;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
