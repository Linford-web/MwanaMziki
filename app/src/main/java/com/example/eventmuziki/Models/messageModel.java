package com.example.eventmuziki.Models;

public class messageModel {
    public String message;
    public String senderId;
    public String receiverId;
    public String messageId;

    public messageModel(String message, String senderId, String receiverId, String messageId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageId = messageId;
    }
    public messageModel() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
