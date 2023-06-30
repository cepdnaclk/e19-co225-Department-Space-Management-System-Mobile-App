package com.example.myapplication4.java;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication4.kotlin.WeekCalendar;
import com.example.myapplication5.kotlin.TimePicker;
import com.kizitonwose.calendar.view.WeekCalendarView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.joery.timerangepicker.TimeRangePicker;

public class NewBookingFragment extends Fragment {
    private boolean[] isLectureHallAvailable = new boolean[6];
        private String selected_lecture_hall="";
        private int startTime;
        private ImageView lecture_hall1_green;
        private ImageView lecture_hall1_red;
    private ImageView lecture_hall2_green;
    private ImageView lecture_hall2_red;
        private ImageView base_image1;
    private int endTime;

    private WeekCalendar weekCalendar;
    private WeekCalendarView caland;
        private Toolbar exsevenview;

        private TimeRangePicker picker;
        private LocalDate date;
        private boolean tempVariable;
        private Button bookButton;
        private TextView start_time;
        private TextView end_time;
    private LinearLayout layout_start_time;
    private LinearLayout layout_end_time;
    private ViewPager viewPager;
    private ImageSliderAdapter sliderAdapter;
    private LinearLayout dotsLayout;
    private List<Integer> imageList;


        public NewBookingFragment(){
            tempVariable=false;
        }
        public NewBookingFragment(LocalDate  date,int startTime,int endTime,String selected_lecture_hall){
            tempVariable=true;
            this.date=date;
            this.startTime=startTime;
            this.endTime=endTime;
            this.selected_lecture_hall=selected_lecture_hall;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.fragment_new_booking, container, false);

            //slider
            viewPager = rootView.findViewById(R.id.viewPager);
            dotsLayout = rootView.findViewById(R.id.dotsLayout);
            imageList = Arrays.asList(R.drawable.p2_ship_default, R.drawable.p2_ship_default_1, R.drawable.p2_ship_default_2,R.drawable.p2_ship_default_2); // Add your image resource IDs here
            sliderAdapter = new ImageSliderAdapter(requireContext(), imageList, dotsLayout);
            viewPager.setAdapter(sliderAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    updateDots(position);
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Empty implementation
                }
            });

            //slider-end


            //calendar
            weekCalendar =new WeekCalendar();
            if (tempVariable) {
                weekCalendar.setSelectedDate(date);
            }else{
                weekCalendar.setSelectedDate(LocalDate.now());
            }
            caland=rootView.findViewById(R.id.calendarView);
            exsevenview=rootView.findViewById(R.id.exSevenToolbar);
            weekCalendar.weekcalendarcaller(caland,exsevenview,true,NewBookingFragment.this);
            //calendar-end

            //timepicker
            TimePicker timePicker =new TimePicker();
            picker=rootView.findViewById(R.id.picker);
            timePicker.hello(picker,true,NewBookingFragment.this);
            if (tempVariable) {
                picker.setStartTimeMinutes(startTime);
                picker.setEndTimeMinutes(endTime);
            }
            //timepicker-end



            bookButton = rootView.findViewById(R.id.bookbutton);
            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected_lecture_hall.equals("")) {
                        Toast.makeText(getContext(),"Please select a lecture hall",Toast.LENGTH_SHORT).show();
                    }else if(!isLectureHallAvailable[Integer.parseInt(selected_lecture_hall.substring(12,13))]){
                        Toast.makeText(getContext(),"Already Booked",Toast.LENGTH_SHORT).show();
                    }else if(!isWithinNext30Days(weekCalendar.getSelectedDate(),picker.getStartTimeMinutes())){
                        Toast.makeText(getContext(),"Select a date within 30 days",Toast.LENGTH_SHORT).show();
                    }else {
                        FirebaseHandler.localToFirebase(getContext().getApplicationContext(), weekCalendar.getSelectedDate().toString(),
                                picker.getStartTimeMinutes(), picker.getEndTimeMinutes(), selected_lecture_hall,NewBookingFragment.this);
                    }
                }
            });

            start_time=rootView.findViewById(R.id.start_time);
            end_time=rootView.findViewById(R.id.end_time);
            layout_start_time=rootView.findViewById(R.id.linear_layout_start);
            layout_end_time=rootView.findViewById(R.id.linear_layout_end);

            lecture_hall1_red =rootView.findViewById(R.id.lecture_hall1_red);
            lecture_hall1_green =rootView.findViewById(R.id.lecture_hall1_green);
            lecture_hall2_red =rootView.findViewById(R.id.lecture_hall2_red);
            lecture_hall2_green =rootView.findViewById(R.id.lecture_hall2_green);
            lecture_hall1_red.setImageAlpha(104);
            lecture_hall1_green.setImageAlpha(104);
            lecture_hall2_red.setImageAlpha(104);
            lecture_hall2_green.setImageAlpha(104);
            base_image1=rootView.findViewById(R.id.image_areas);

            if(selected_lecture_hall.equals("lecture_hall1")){
                lecture_hall1_red.setImageAlpha(225);
                lecture_hall1_green.setImageAlpha(225);

                lecture_hall2_red.setImageAlpha(104);
                lecture_hall2_green.setImageAlpha(104);

            } else if (selected_lecture_hall.equals("lecture_hall2")) {
                selected_lecture_hall="lecture_hall2";
                lecture_hall2_red.setImageAlpha(225);
                lecture_hall2_green.setImageAlpha(225);

                lecture_hall1_red.setImageAlpha(104);
                lecture_hall1_green.setImageAlpha(104);

            } else if (selected_lecture_hall.equals("lecture_hall3")) {

            } else if (selected_lecture_hall.equals("lecture_hall4")) {

            } else if (selected_lecture_hall.equals("lecture_hall5")) {

            } else if (selected_lecture_hall.equals("lecture_hall6")) {

            }
            base_image1.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch (View v, MotionEvent ev){
                    final int action = ev.getAction();
                    // (1)
                    final int evX = (int) ev.getX();
                    final int evY = (int) ev.getY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                            break;
                        case MotionEvent.ACTION_UP:
                            base_image1.setDrawingCacheEnabled(true);
                            Bitmap hotspots = Bitmap.createBitmap(base_image1.getDrawingCache());
                            base_image1.setDrawingCacheEnabled(false);
                            int tolerance = 100;
                            int color=hotspots.getPixel(evX, evY);
                            if(Math.abs(color+3328)<tolerance){
                                selected_lecture_hall="lecture_hall1";
                                lecture_hall1_red.setImageAlpha(225);
                                lecture_hall1_green.setImageAlpha(225);

                                lecture_hall2_red.setImageAlpha(104);
                                lecture_hall2_green.setImageAlpha(104);
                            } else if (Math.abs(color+1172700)<tolerance) {
                                selected_lecture_hall="lecture_hall2";
                                lecture_hall2_red.setImageAlpha(225);
                                lecture_hall2_green.setImageAlpha(225);

                                lecture_hall1_red.setImageAlpha(104);
                                lecture_hall1_green.setImageAlpha(104);
                            }else if (Math.abs(color+12695861)<tolerance) {
                                selected_lecture_hall="lecture_hall3";
                            }else if (Math.abs(color+14438067)<tolerance) {
                                selected_lecture_hall="lecture_hall4";
                            }else if (Math.abs(color+8421505)<tolerance) {
                                selected_lecture_hall="lecture_hall5";
                            }
                            Toast.makeText(getContext(), Integer.toString(Color.RED)+" "+Integer.toString(hotspots.getPixel(evX, evY)), Toast.LENGTH_SHORT).show();

                    }
                    return true;
                }
            });

            return rootView;
        }

    public void upDateUi(Integer flagStartTimeChange) {


        int selected_start_time = picker.getStartTimeMinutes();
        int selected_end_time = picker.getEndTimeMinutes();
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
//        if (isLectureHallAvailable[1]) {
//            lecture_hall1_red.setVisibility(View.INVISIBLE);
//            lecture_hall1_green.setVisibility(View.VISIBLE);
//
//        } else {
//            lecture_hall1_red.setVisibility(View.VISIBLE);
//            lecture_hall1_green.setVisibility(View.INVISIBLE);
//        }
//
//        if (isLectureHallAvailable[2]) {
//            lecture_hall2_red.setVisibility(View.INVISIBLE);
//            lecture_hall2_green.setVisibility(View.VISIBLE);
//
//        } else {
//            lecture_hall2_red.setVisibility(View.VISIBLE);
//            lecture_hall2_green.setVisibility(View.INVISIBLE);
//        }
//        if (flagStartTimeChange==1) {
//            start_time.setText(CustomAdapter.convertTimeToAMPM(selected_start_time));
//            layout_end_time.setVisibility(View.GONE);
//            layout_start_time.setVisibility(View.VISIBLE);
//        } else if(flagStartTimeChange==2) {
//            end_time.setText(CustomAdapter.convertTimeToAMPM(selected_end_time));
//            layout_start_time.setVisibility(View.GONE);
//            layout_end_time.setVisibility(View.VISIBLE);
//        } else if (flagStartTimeChange==3) {
//
//            layout_start_time.setVisibility(View.VISIBLE);
//            layout_end_time.setVisibility(View.VISIBLE);
//        }
    }
    public static boolean isWithinNext30Days(LocalDate date, int minutes) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime givenDateTime = LocalDateTime.of(date, LocalTime.of(0, 0)).plusMinutes(minutes);

        // Check if the selected date and time have already passed today
        if (now.isAfter(givenDateTime) && now.toLocalDate().isEqual(date)) {
            return false;
        }

        long differenceInMinutes = ChronoUnit.MINUTES.between(now, givenDateTime);

        return differenceInMinutes >= 0 && differenceInMinutes <= (30 * 24 * 60);
    }
    private void updateDots(int position) {
        for (int i = 0; i < imageList.size(); i++) {
            ImageView dot = (ImageView) dotsLayout.getChildAt(i);
            if (dot != null) {
                dot.setImageResource(i == position ? R.drawable.indicator_dot_selected : R.drawable.indicator_dot);
            }
        }
    }
}