package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.WeekCalendar;
import com.example.myapplication5.kotlin.TimePicker;
import com.kizitonwose.calendar.view.WeekCalendarView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.joery.timerangepicker.TimeRangePicker;

public class SearchByTimeStep1Fragment extends Fragment {
    private TimeRangePicker picker;
    private WeekCalendarView caland;
    private WeekCalendar weekCalendar;
    private Toolbar exsevenview;
    private Button submitTime;

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_by_time_step1, container, false);

        weekCalendar = new WeekCalendar();
        caland = rootView.findViewById(R.id.calendarView);
        exsevenview = rootView.findViewById(R.id.exSevenToolbar);
        weekCalendar.weekcalendarcaller(caland, exsevenview, false, new NewBookingFragment());
        weekCalendar.setSelectedDate(LocalDate.now());

        TimePicker timePicker = new TimePicker();
        picker = rootView.findViewById(R.id.picker);
        timePicker.hello(picker, false, new NewBookingFragment());

        submitTime = rootView.findViewById(R.id.submitTime);

        // Configure the RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        submitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> MapList = checkAvailability();
                adapter = new MyAdapter(MapList, new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String item) {
                        // Handle the item click event
                        Fragment fragment = new NewBookingFragment(weekCalendar.getSelectedDate(), picker.getStartTimeMinutes(), picker.getEndTimeMinutes(), item);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, fragment);
                        fragmentTransaction.commit();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public List<String> checkAvailability() {
        int selected_start_time = picker.getStartTimeMinutes();
        int selected_end_time = picker.getEndTimeMinutes();
        boolean[] isLectureHallAvailable = new boolean[6];
        Arrays.fill(isLectureHallAvailable, true);
        List<Map<String, Object>> hashMapList = FirebaseHandler.readLocal(getContext().getApplicationContext());
        for (Map<String, Object> hashMap : hashMapList) {
            if (weekCalendar.getSelectedDate().toString().equals((String) hashMap.get("date"))) {
                String lecture_hall = (String) hashMap.get("lecture_hall");
                int startTime = Integer.valueOf((String) hashMap.get("start_time"));
                int endTime = Integer.valueOf((String) hashMap.get("end_time"));
                if (!((selected_start_time < startTime && selected_end_time <= startTime)
                        || (selected_start_time >= endTime))) {
                    isLectureHallAvailable[Integer.parseInt(lecture_hall.substring(12, 13))] = false;
                }
            }
        }
        List<String> lectureHalls = new ArrayList<>();

        for (int i = 1; i < isLectureHallAvailable.length; i++) {
            if (isLectureHallAvailable[i]) {
                lectureHalls.add("lecture_hall" + i);
            }
        }
        return lectureHalls;
    }
}
