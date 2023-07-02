package com.example.myapplication4.java;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.java.notificationServer.FCMNotificationSender;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.FragmentTransaction;
public class HomeFragment extends Fragment {
    private Button searchBySpaceButton;
    private Button searchByTimeButton;
    private Button signOutButton;
    private Button responsiblePersons;
    private ImageButton settingsButton;
    private Boolean isAdmin;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);



        drawerLayout = rootView.findViewById(R.id.drawer_layout);
        navigationView = rootView.findViewById(R.id.navigation_view);
        settingsButton=  rootView.findViewById(R.id.settingsButton);
        searchBySpaceButton = rootView.findViewById(R.id.searchBySpaceButton);
        searchByTimeButton = rootView.findViewById(R.id.searchByTimeButton);
        signOutButton = rootView.findViewById(R.id.log_out);
        responsiblePersons=rootView.findViewById(R.id.responsiblepersons);
        username=rootView.findViewById(R.id.username);
        username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


        if(FirebaseHandler.isAdminUser(getContext())){
            responsiblePersons.setVisibility(View.GONE);
        }



        searchBySpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBySpaceStep1Fragment searchBySpaceStep1Fragment = new SearchBySpaceStep1Fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, searchBySpaceStep1Fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                signOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SignOutActivity.class);
                        startActivity(intent);
                    }
                });
                responsiblePersons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResponsiblePersonsFragment responsiblePersonsFragment = new ResponsiblePersonsFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, responsiblePersonsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        });

        searchByTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchByTimeStep1Fragment searchByTimeStep1Fragment = new SearchByTimeStep1Fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, searchByTimeStep1Fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle menu item clicks here
                // You can replace the fragment or perform any other action based on the clicked item
                switch (item.getItemId()) {
                    // Add more menu items as needed
                }

                // Close the side menu after handling the click
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Close the side menu when clicking outside
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerLayout.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setClickable(false);
            }
        });

        return rootView;
    }
}