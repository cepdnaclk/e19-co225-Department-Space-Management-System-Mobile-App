package com.example.myapplication4.java;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private FirebaseFirestore db;
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        saveTokenToSharedPreferences(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the incoming message
        if (remoteMessage.getData().containsKey("message")) {
            String message = remoteMessage.getData().get("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.i("abc",message);
        }
    }
    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", token);
        editor.apply();
    }
    @Override
    public void onMessageSent(String messageId) {
        super.onMessageSent(messageId);
        Log.i("abc", "Message sent successfully: " + messageId);
    }

    @Override
    public void onSendError(String messageId, Exception exception) {
        super.onSendError(messageId, exception);
        Log.i("abc", "Error sending message: " + messageId, exception);
    }
}
