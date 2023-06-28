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
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.WeekCalendar;
import com.example.myapplication5.kotlin.TimePicker;
import com.kizitonwose.calendar.view.WeekCalendarView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import nl.joery.timerangepicker.TimeRangePicker;

public class SearchByTimeStep1Fragment extends Fragment {
    private TimeRangePicker picker;
    private WeekCalendarView caland;
    private Toolbar exsevenview;
    private Button submitTime;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private PopupWindow popupWindow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search_by_time_step1, container, false);

        WeekCalendar weekCalendar =new WeekCalendar();
        caland=rootView.findViewById(R.id.calendarView);
        exsevenview=rootView.findViewById(R.id.exSevenToolbar);
        weekCalendar.weekcalendarcaller(caland,exsevenview,false,new NewBookingFragment());

        TimePicker timePicker =new TimePicker();
        picker=rootView.findViewById(R.id.picker);
//        timePicker.hello(picker,NewBookingFragment);


        submitTime = rootView.findViewById(R.id.submitTime);

        List<Map<String, Object>> hashMapList=FirebaseHandler.readLocal(getContext());

        submitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), weekCalendar.getSelectedDate().toString()+String.valueOf(picker.getStartTimeMinutes()), Toast.LENGTH_SHORT).show();
                popupWindow.showAsDropDown(v);
            }});

        // Initialize the PopupWindow
        View popupView = inflater.inflate(R.layout.popup_content_layout, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(true);

        // Configure the RecyclerView
        recyclerView = popupView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(hashMapList, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                // Handle the item click event
                Toast.makeText(getActivity(), "Clicked item: " + item, Toast.LENGTH_SHORT).show();

                Fragment fragment=new NewBookingFragment(LocalDate.of(2023, 6, 1),360,560);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
                popupWindow.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);


        // Inflate the layout for this fragment
        return rootView;
    }
}