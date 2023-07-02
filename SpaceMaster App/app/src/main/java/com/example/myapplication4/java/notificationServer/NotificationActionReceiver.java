package com.example.myapplication4.java.notificationServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("ACTION_ACCEPT")) {
                // Handle accept action
                performAcceptAction();
            } else if (action.equals("ACTION_DECLINE")) {
                // Handle decline action
                performDeclineAction();
            }
        }
    }

    private void performAcceptAction() {
        // Perform the background work for accept action
        // For example, you can make a network request or update the database
    }

    private void performDeclineAction() {
        // Perform the background work for decline action
        // For example, you can update the server or perform some logic
    }
}
