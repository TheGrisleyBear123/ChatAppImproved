package com.example.androidstudiofinalproject;

import com.google.firebase.Timestamp;

public class Message {
    private String senderEmail;
    private String senderId;
    private String receiver;
    private String message;
    private long timestamp;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String senderId, String receiver, String message, long timestamp) {
        this.senderId = senderId;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(String senderId, String receiver, String message, long timestamp, String senderEmail) {
        this.senderId = senderId;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
