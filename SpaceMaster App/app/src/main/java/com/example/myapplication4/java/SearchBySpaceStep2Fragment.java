package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.SpaceCalendar;
import com.kizitonwose.calendar.view.CalendarView;

public class SearchBySpaceStep2Fragment extends Fragment {
    private ListView listView;
    private CalendarView caland;
    private String lecture_hall;
    public SearchBySpaceStep2Fragment(String lecture_hall){
        this.lecture_hall=lecture_hall;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search_by_space_step2, container, false);

        SpaceCalendar monthCalendar =new SpaceCalendar();
        caland=rootView.findViewById(R.id.calendarView_space);
//        Toast.makeText(getContext(), String.valueOf(FirebaseHandler.isAdminUser(getContext().getApplicationContext())), Toast.LENGTH_SHORT).show();
        monthCalendar.monthcalendarcaller(caland,rootView,getContext(),lecture_hall,getParentFragmentManager());
        return rootView;
    }
}
