package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.FragmentTransaction;
public class HomeFragment extends Fragment {
    private Button searchBySpaceButton;
    private Button searchByTimeButton;
    private Boolean isAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);


        searchBySpaceButton = rootView.findViewById(R.id.searchBySpaceButton);
        searchByTimeButton = rootView.findViewById(R.id.searchByTimeButton);


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
        return rootView;
    }
}