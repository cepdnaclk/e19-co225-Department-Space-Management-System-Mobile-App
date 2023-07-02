package com.example.myapplication4.java.notificationServer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.myapplication4.R;
import com.example.myapplication4.databinding.ActivityMainBinding;
import com.example.myapplication4.java.notificationServer.FCMClient;
import com.example.myapplication4.java.notificationServer.FCMNotification;
import com.example.myapplication4.java.notificationServer.FCMRequest;
import com.example.myapplication4.java.notificationServer.FCMResponse;
import com.example.myapplication4.java.notificationServer.FCMService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FCMNotificationSender {
    private FCMService fcmService;
    public FCMNotificationSender( ) {
        fcmService = FCMClient.getClient().create(FCMService.class);
    }

    public void sendNotification(String targetToken, String title, String message) {
        FCMNotification notification = new FCMNotification(title, message);
        FCMRequest request = new FCMRequest(targetToken, notification);

        Call<FCMResponse> call = fcmService.sendNotification(request);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.isSuccessful()) {
                    FCMResponse fcmResponse = response.body();
                    if (fcmResponse != null) {
                        Log.i("abc","Notification sent Sucessfully");
                    }
                } else {
                    Log.i("abc","Notification sending Failed");
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}

