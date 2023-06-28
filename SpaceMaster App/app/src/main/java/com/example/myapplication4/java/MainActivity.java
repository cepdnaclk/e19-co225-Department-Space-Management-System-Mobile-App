package com.example.myapplication4.java;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myapplication4.R;
import com.example.myapplication4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.shorts) {
                replaceFragment(new ShortsFragment());
            }
            return true;


        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}