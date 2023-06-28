package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication4.R;
import com.example.myapplication4.databinding.FragmentHomeBinding;
import com.example.myapplication4.kotlin.KotlinFile2;
import com.kizitonwose.calendar.view.WeekCalendarView;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }
}