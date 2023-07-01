package com.example.myapplication4.java;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseHandler.firebaseToLocal("","",getApplicationContext());
        FirebaseHandler.getAdminDetails(getApplicationContext());
        FirebaseHandler.sendTokenToServer(getApplicationContext());

        String recipientToken  = "f98L18oiRZilQNm3j0YyWc:APA91bEzdz7Mx42Ci-w2ZJyFCT4IXXJuUDDL9xV-2ptcS6r2jZcHm3SzHCulRDv0w5k5epY9s_1M6DRX6tcZx1FC0pCyLjP9SeTNp8yClpzpEG6wiEgTNzmemi7_ff57vZ3UCIC6X7ew";

        // Set the server key obtained from the Firebase console
        FCMService fcmService = FCMClient.getClient().create(FCMService.class);

        FCMNotification notification = new FCMNotification("Title", "Mjessage");

        FCMRequest request = new FCMRequest(recipientToken, notification); // Replace with the target device's FCM token

        Call<FCMResponse> call = fcmService.sendNotification(request);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.isSuccessful()) {
                    FCMResponse fcmResponse = response.body();
                    if (fcmResponse != null) {
                        // Notification sent successfully
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                // Handle failure
            }
        });








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