package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.KotlinFile2;
import com.kizitonwose.calendar.view.WeekCalendarView;

public class NewBookingFragment extends Fragment {




        private WeekCalendarView caland;
        private Toolbar exsevenview;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.fragment_new_booking, container, false);

            KotlinFile2 kotlinFile2=new KotlinFile2();
            caland=rootView.findViewById(R.id.calendarView);
            exsevenview=rootView.findViewById(R.id.exSevenToolbar);
            kotlinFile2.hello2(caland,exsevenview);

            // Inflate the layout for this fragment
            return rootView;
        }
}