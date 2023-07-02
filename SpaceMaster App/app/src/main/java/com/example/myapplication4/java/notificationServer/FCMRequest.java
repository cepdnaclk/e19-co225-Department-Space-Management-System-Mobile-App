package com.example.myapplication4.java.notificationServer;

public class FCMRequest {
    private String to;
    private FCMNotification notification;

    public FCMRequest(String to, FCMNotification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public FCMNotification getNotification() {
        return notification;
    }
}
