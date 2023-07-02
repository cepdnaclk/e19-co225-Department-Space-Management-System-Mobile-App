package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.MonthCalendar;
import com.example.myapplication4.kotlin.MonthCalendarforSpace;
import com.kizitonwose.calendar.view.CalendarView;

public class SearchBySpaceStep2Fragment extends Fragment {
    private ListView listView;
    private CalendarView caland;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search_by_space_step2, container, false);

        MonthCalendarforSpace monthCalendar =new MonthCalendarforSpace();
        caland=rootView.findViewById(R.id.calendarView);
//        Toast.makeText(getContext(), String.valueOf(FirebaseHandler.isAdminUser(getContext().getApplicationContext())), Toast.LENGTH_SHORT).show();
        monthCalendar.monthcalendarcaller(caland,rootView,getContext(),FirebaseHandler.isAdminUser(getContext().getApplicationContext()));
        return rootView;
    }
}
