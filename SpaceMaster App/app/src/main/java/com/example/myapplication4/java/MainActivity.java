package com.example.myapplication4.java;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseHandler.firebaseToLocal("","",getApplicationContext());
        FirebaseHandler.getAdminDetails(getApplicationContext());
        FirebaseHandler.sendTokenToServer(getApplicationContext());

        String deviceToken = "htiNIHX9RZinTTemCSFnoL:APA91bFX5DsLDiE_zKvLG3qEB7bhz-APS-NlNj3JgSlesVDO1msttbohdE-C1ooX0ZKJvp_GUWhDZEvQAfRwtyszL8_rTtDD1LifJg_hnGxStl8gZ4biBqTpKbLFrKBUBm4Tc51hxTS5";

                RemoteMessage message = new RemoteMessage.Builder(deviceToken)
                .setMessageId(UUID.randomUUID().toString())
                .addData("message", "Hello, this is a message!")
                .build();

        try {
            Log.d("FCM", "Message sent successfully.");
        } catch (IllegalArgumentException e) {
            Log.e("FCM", "Error sending message: " + e.getMessage(), e);
        }









        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.appointments) {
                replaceFragment(new AppointmentsFragment());
            }
            return true;


        });
        binding.floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new NewBookingFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void upDateUi(int selected_start_time, int selected_end_time){
        List<Map<String, Object>> hashMapList=FirebaseHandler.readLocal(getApplicationContext());
        boolean trueOrFalse=true;
        ImageView b=findViewById(R.id.image_1);
        ImageView c=findViewById(R.id.image_2);
        for (Map<String, Object> hashMap : hashMapList) {
            int startTime= (int) hashMap.get("start_time");
            int endTime= (int) hashMap.get("end_time");
            if(!((selected_start_time<startTime && selected_end_time<=startTime)
                    ||(selected_start_time>=endTime))){
                trueOrFalse=false;

            }
        }
        if(trueOrFalse){
            b.setVisibility(View.INVISIBLE);
            c.setVisibility(View.VISIBLE);

        }else{
            b.setVisibility(View.VISIBLE);
            c.setVisibility(View.INVISIBLE);

        }
    }
}