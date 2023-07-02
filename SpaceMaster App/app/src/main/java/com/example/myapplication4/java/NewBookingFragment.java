package com.example.myapplication4.java;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.joery.timerangepicker.TimeRangePicker;

public class NewBookingFragment extends Fragment {
    int posi;
    private boolean[] isLectureHallAvailable = new boolean[8];
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
        private Button notifyButton;
        private TextView start_time;
        private TextView end_time;
    private LinearLayout layout_start_time;
    private LinearLayout layout_end_time;
    private ViewPager viewPager;
    private ImageSliderAdapter sliderAdapter;
    private LinearLayout dotsLayout;
    private List<Integer> imageList;
    private List<List<Integer>> imageListList=new ArrayList<>();

    private ImageView[] imageViewBaseArray=new ImageView[4];
    private ImageView[] imageViewImageArray=new ImageView[4];

    private ImageView[] imageView1GreenArray=new ImageView[4];
    private ImageView[] imageView1RedArray=new ImageView[4];
    private ImageView[] imageView2GreenArray=new ImageView[4];
    private ImageView[] imageView2RedArray=new ImageView[4];
    private Boolean[] isset=new Boolean[4];


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

            //initialize to false
            for (int i = 0; i < 4; i++) {
                isset[i] = false;
            }
            //isset

            //slider
            viewPager = rootView.findViewById(R.id.viewPager);
            dotsLayout = rootView.findViewById(R.id.dotsLayout);
            imageList = Arrays.asList(R.drawable.a_base,R.drawable.a_image,R.drawable.a_1_green,R.drawable.a_1_red,R.drawable.a_2_green,R.drawable.a_2_red);
            imageListList.add(imageList);
            imageList = Arrays.asList(R.drawable.b_base,R.drawable.b_image,R.drawable.b_1_green,R.drawable.b_1_red,R.drawable.b_2_green,R.drawable.b_2_red);
            imageListList.add(imageList);
            imageList = Arrays.asList(R.drawable.c_base,R.drawable.c_image,R.drawable.c_1_green,R.drawable.c_1_red,R.drawable.c_2_green,R.drawable.c_2_red);
            imageListList.add(imageList);
            imageList = Arrays.asList(R.drawable.d_base,R.drawable.d_image,R.drawable.d_1_green,R.drawable.d_1_red,R.drawable.d_2_green,R.drawable.d_2_red);
            imageListList.add(imageList);

            sliderAdapter = new ImageSliderAdapter(requireContext(), imageListList, dotsLayout,NewBookingFragment.this);
            viewPager.setAdapter(sliderAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    updateDots(position);
                    posi=position;
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


            //buttons
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

            notifyButton = rootView.findViewById(R.id.notifybutton);
            notifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected_lecture_hall.equals("")) {
                        Toast.makeText(getContext(),"Please select a lecture hall",Toast.LENGTH_SHORT).show();
                    }else if(!isWithinNext30Days(weekCalendar.getSelectedDate(),picker.getStartTimeMinutes())){
                        Toast.makeText(getContext(),"Select a date within 30 days",Toast.LENGTH_SHORT).show();
                    }else {
                        FirebaseHandler.notifyMeFirebase(getContext().getApplicationContext(), weekCalendar.getSelectedDate().toString(),
                                picker.getStartTimeMinutes(), picker.getEndTimeMinutes(), selected_lecture_hall);
                    }
                }
            });
            //buttons-end

            start_time=rootView.findViewById(R.id.start_time);
            end_time=rootView.findViewById(R.id.end_time);
            layout_start_time=rootView.findViewById(R.id.linear_layout_start);
            layout_end_time=rootView.findViewById(R.id.linear_layout_end);

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
        if (isLectureHallAvailable[1]) {
//            Toast.makeText(getContext(),"sss",Toast.LENGTH_SHORT).show();
            if (isset[0]==true) {
                imageView1RedArray[0].setVisibility(View.INVISIBLE);
                imageView1GreenArray[0].setVisibility(View.VISIBLE);


            }

        } else {
            if (isset[0]==true) {
                imageView1RedArray[0].setVisibility(View.VISIBLE);
                imageView1GreenArray[0].setVisibility(View.INVISIBLE);
            }
        }

        if (isLectureHallAvailable[2]) {
            if (isset[0]==true) {
                imageView2RedArray[0].setVisibility(View.INVISIBLE);
                imageView2GreenArray[0].setVisibility(View.VISIBLE);


            }

        } else {
            if (isset[0]==true) {
                imageView2RedArray[0].setVisibility(View.VISIBLE);
                imageView2GreenArray[0].setVisibility(View.INVISIBLE);
            }
        }

        if (isLectureHallAvailable[3]) {
            if (isset[1]==true) {
                imageView1RedArray[1].setVisibility(View.INVISIBLE);
                imageView1GreenArray[1].setVisibility(View.VISIBLE);
            }

        } else {
            if (isset[1]==true) {
                imageView1RedArray[1].setVisibility(View.VISIBLE);
                imageView1GreenArray[1].setVisibility(View.INVISIBLE);
            }
        }
        if (isLectureHallAvailable[4]) {
            if (isset[1]==true) {
                imageView2RedArray[1].setVisibility(View.INVISIBLE);
                imageView2GreenArray[1].setVisibility(View.VISIBLE);
            }

        } else {
            if (isset[1]==true) {
                imageView2RedArray[1].setVisibility(View.VISIBLE);
                imageView2GreenArray[1].setVisibility(View.INVISIBLE);
            }
        }
        if (isLectureHallAvailable[5]) {
            if(isset[2]==true) {
                imageView1RedArray[2].setVisibility(View.INVISIBLE);
                imageView1GreenArray[2].setVisibility(View.VISIBLE);
            }

        } else {
            if(isset[2]==true) {
                imageView1RedArray[2].setVisibility(View.VISIBLE);
                imageView1GreenArray[2].setVisibility(View.INVISIBLE);
            }
        }
        if (isLectureHallAvailable[6]) {
            if(isset[2]==true) {
                imageView2RedArray[2].setVisibility(View.INVISIBLE);
                imageView2GreenArray[2].setVisibility(View.VISIBLE);
            }

        } else {
            if(isset[2]==true) {
                imageView2RedArray[2].setVisibility(View.VISIBLE);
                imageView2GreenArray[2].setVisibility(View.INVISIBLE);
            }
        }
        if (isLectureHallAvailable[7]) {
            if(isset[3]==true) {
                imageView1RedArray[3].setVisibility(View.INVISIBLE);
                imageView1GreenArray[3].setVisibility(View.VISIBLE);
            }

        } else {
            if(isset[3]==true) {
                imageView1RedArray[3].setVisibility(View.VISIBLE);
                imageView1GreenArray[3].setVisibility(View.INVISIBLE);
            }
    }




        if (flagStartTimeChange==1) {
            start_time.setText(CalendarAdapter.convertTimeToAMPM(selected_start_time));
            layout_end_time.setVisibility(View.GONE);
            layout_start_time.setVisibility(View.VISIBLE);
        } else if(flagStartTimeChange==2) {
            end_time.setText(CalendarAdapter.convertTimeToAMPM(selected_end_time));
            layout_start_time.setVisibility(View.GONE);
            layout_end_time.setVisibility(View.VISIBLE);
        } else if (flagStartTimeChange==3) {

            layout_start_time.setVisibility(View.VISIBLE);
            layout_end_time.setVisibility(View.VISIBLE);
        }


        updateButton();

    }

    public void updateButton(){
        if(!selected_lecture_hall.equals("")){
            if(isLectureHallAvailable[Integer.parseInt(selected_lecture_hall.substring(12, 13))]){
                bookButton.setVisibility(View.VISIBLE);
                notifyButton.setVisibility(View.GONE);
            }else{
                bookButton.setVisibility(View.GONE);
                notifyButton.setVisibility(View.VISIBLE);
            }

        }
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

    public void updateImageView(int position,ImageView imageViewBase,ImageView imageViewImage,ImageView imageView1Green,ImageView imageView1Red,ImageView imageView2Green,ImageView imageView2Red){
            imageViewBaseArray[position]=imageViewBase;
            imageViewImageArray[position]=imageViewImage;
            imageView1GreenArray[position]=imageView1Green;
            imageView1RedArray[position]=imageView1Red;
            imageView2GreenArray[position]=imageView2Green;
            imageView2RedArray[position]=imageView2Red;

            imageView1GreenArray[position].setImageAlpha(104);
            imageView1RedArray[position].setImageAlpha(104);
            imageView2GreenArray[position].setImageAlpha(104);
            imageView2RedArray[position].setImageAlpha(104);


        imageViewBaseArray[position].setOnTouchListener(new View.OnTouchListener()
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
                        imageViewBaseArray[position].setDrawingCacheEnabled(true);
                        Bitmap hotspots = Bitmap.createBitmap(imageViewBaseArray[position].getDrawingCache());
                        imageViewImageArray[position].setDrawingCacheEnabled(false);
                        int tolerance = 100;
                        int color=hotspots.getPixel(evX, evY);
                        if(Math.abs(color+10215067)<tolerance && isset[0]){
                            selected_lecture_hall="lecture_hall1";
                            imageView1GreenArray[0].setImageAlpha(190);
                            imageView1RedArray[0].setImageAlpha(190);

                            imageView2GreenArray[0].setImageAlpha(104);
                            imageView2RedArray[0].setImageAlpha(104);
                        } else if (Math.abs(color+3381208)<tolerance && isset[0]) {
                            selected_lecture_hall="lecture_hall2";
                            imageView1GreenArray[0].setImageAlpha(104);
                            imageView1RedArray[0].setImageAlpha(104);

                            imageView2GreenArray[0].setImageAlpha(190);
                            imageView2RedArray[0].setImageAlpha(190);
                        }else if (Math.abs(color+14535570)<tolerance && isset[1]) {
                            selected_lecture_hall="lecture_hall3";
                            imageView1GreenArray[1].setImageAlpha(190);
                            imageView1RedArray[1].setImageAlpha(190);

                            imageView2GreenArray[1].setImageAlpha(104);
                            imageView2RedArray[1].setImageAlpha(104);

                        }else if (Math.abs(color+4144960)<tolerance && isset[1]) {
                            selected_lecture_hall="lecture_hall4";
                            imageView1GreenArray[1].setImageAlpha(104);
                            imageView1RedArray[1].setImageAlpha(104);

                            imageView2GreenArray[1].setImageAlpha(190);
                            imageView2RedArray[1].setImageAlpha(190);
                        }else if (Math.abs(color+16776962)<tolerance && isset[2]) {
                            selected_lecture_hall="lecture_hall5";
                            imageView1GreenArray[2].setImageAlpha(190);
                            imageView1RedArray[2].setImageAlpha(190);

                            imageView2GreenArray[2].setImageAlpha(104);
                            imageView2RedArray[2].setImageAlpha(104);
                        }else if (Math.abs(color+1114347)<tolerance && isset[2]) {
                            selected_lecture_hall="lecture_hall6";
                            imageView1GreenArray[2].setImageAlpha(104);
                            imageView1RedArray[2].setImageAlpha(104);

                            imageView2GreenArray[2].setImageAlpha(190);
                            imageView2RedArray[2].setImageAlpha(190);
                        }else if (Math.abs(color+2182443)<tolerance && isset[3]) {
                            selected_lecture_hall="lecture_hall7";
                            imageView1GreenArray[3].setImageAlpha(190);
                            imageView1RedArray[3].setImageAlpha(190);

                            imageView2GreenArray[3].setImageAlpha(104);
                            imageView2RedArray[3].setImageAlpha(104);
                        }
                        upDateUi(3);

                }
                return true;
            }
        });
        isset[position]=true;
        upDateUi(3);
    }

}