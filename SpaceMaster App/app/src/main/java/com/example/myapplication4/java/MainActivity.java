package com.example.myapplication4.java;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication4.R;
import com.example.myapplication4.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseHandler.firebaseToLocal("","",getApplicationContext());
        FirebaseHandler.getAdminDetails(getApplicationContext());

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