package com.tamada.chatdemo.models;

/**
 * Created by inventbird on 31/10/17.
 */

public class MessagesModel {
    private String id;
    private String fromName;
    private String fromId;
    private String message;
    private String messageReply;

    public MessagesModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessagesModel(String fromName, String fromId, String message) {
        this.fromName = fromName;
        this.fromId = fromId;
        this.message = message;
    }

    public String getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(String messageReply) {
        this.messageReply = messageReply;
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
