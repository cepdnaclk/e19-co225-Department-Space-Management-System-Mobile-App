package com.example.myapplication4.java.notificationServer;

public class FCMNotification {
    private String title;
    private String body;

    public FCMNotification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
