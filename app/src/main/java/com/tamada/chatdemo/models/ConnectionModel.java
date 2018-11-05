package com.tamada.chatdemo.models;

/**
 * Created by LSN-ANDROID2 on 05-11-2018.
 */

public class ConnectionModel {
    private String connectionId;
    private MessagesModel messagesModel;

    public ConnectionModel(String connectionId, MessagesModel messagesModel) {
        this.connectionId = connectionId;
        this.messagesModel = messagesModel;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public MessagesModel getMessagesModel() {
        return messagesModel;
    }

    public void setMessagesModel(MessagesModel messagesModel) {
        this.messagesModel = messagesModel;
    }
}
