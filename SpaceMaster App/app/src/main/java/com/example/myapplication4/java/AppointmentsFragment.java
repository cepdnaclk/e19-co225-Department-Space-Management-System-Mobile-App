package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.MonthCalendar;
import com.example.myapplication4.kotlin.WeekCalendar;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.WeekCalendarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppointmentsFragment extends Fragment {
    private CalendarView caland;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_appointments, container, false);

        List<Map<String, Object>> hashMapList=FirebaseHandler.readLocal(getContext());
        String targetKey = "uid";  // Specify the target key to search for
        Object targetValue = "value";  // Specify the target value to search for

        List<Map<String, Object>> resultList = new ArrayList<>();  // List to store matching maps

        for (Map<String, Object> map : hashMapList) {
            // Check if the map contains the specified key-value pair
            if (map.containsKey(targetKey) && map.get(targetKey).equals(targetValue)) {
                resultList.add(map);  // Append the matching map to the resultList
            }
        }

        MonthCalendar monthCalendar =new MonthCalendar();
        caland=rootView.findViewById(R.id.calendarView);
        monthCalendar.monthcalendarcaller(caland);

        // Inflate the layout for this fragment
        return rootView;
    }
}