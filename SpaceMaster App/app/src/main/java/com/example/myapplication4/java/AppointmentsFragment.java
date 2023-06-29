package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.MonthCalendar;
import com.example.myapplication4.kotlin.WeekCalendar;
import com.google.firebase.auth.FirebaseAuth;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.WeekCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppointmentsFragment extends Fragment {
    private ListView listView;
    private CalendarView caland;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_appointments, container, false);

        MonthCalendar monthCalendar =new MonthCalendar();
        caland=rootView.findViewById(R.id.calendarView);
        monthCalendar.monthcalendarcaller(caland,rootView,getContext(),FirebaseHandler.isAdminUser(getContext().getApplicationContext()));
//
//        listView = rootView.findViewById(R.id.list_view);
//        String[] data = {"Item 1", "Item 2", "Item 3","Item 1"};
//        List<String> dataList = Arrays.asList(data);
//        Button button = rootView.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomAdapter adapter = new CustomAdapter(getContext(), dataList);
//                listView.setAdapter(adapter);
//
//
//            }
//        });
//
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            // Handle item click
//            String selectedItem = (String) listView.getItemAtPosition(position);
//            // Perform actions based on the clicked item
//        });
//
//
//        int dpValue = 100; // Height of each item in dp
//        int itemCount = dataList.size()+1; // Number of items in the list
//
//        float density = getResources().getDisplayMetrics().density;
//        int itemHeightPx = (int) (dpValue * density);
//        int listViewHeightPx = itemHeightPx * itemCount;
//
//        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
//        layoutParams.height = listViewHeightPx;
//        listView.setLayoutParams(layoutParams);






        // Inflate the layout for this fragment
        return rootView;
    }
}