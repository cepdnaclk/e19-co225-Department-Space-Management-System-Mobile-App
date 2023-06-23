package com.example.myapplication4.java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication4.R;
import com.example.myapplication5.kotlin.KotlinFile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.joery.timerangepicker.TimeRangePicker;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TimeRangePicker picker;
    private String selectedDate;
    private FirebaseFirestore db;
    private CollectionReference spaces;
    Calendar calendar;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        calendar = Calendar.getInstance();
        KotlinFile kotlinFile=new KotlinFile();
        picker=findViewById(R.id.picker);
        kotlinFile.hello(picker,MainActivity2.this);

        DatePicker datePicker = findViewById(R.id.datePicker);
        datePicker.setMaxDate(1686767400000L);
        datePicker.init(2000, 2, 8, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int selectedYear = year;
                int selectedMonth = monthOfYear;
                int selectedDay = dayOfMonth;

                Log.i("abc", "date change");
//                textView.setText(selectedDate);
            }
        });




        ImageView b=findViewById(R.id.image_1);
        ImageView c=findViewById(R.id.image_2);

        ImageView a=findViewById(R.id.image);
        Toast.makeText(MainActivity2.this, Integer.toString(a.getMaxWidth()), Toast.LENGTH_SHORT).show();
        a.setOnTouchListener(new View.OnTouchListener()

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
                        ImageView img = findViewById (R.id.image_areas);
                        img.setDrawingCacheEnabled(true);
                        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
                        img.setDrawingCacheEnabled(false);
                        int tolerance = 25;
                        int color=hotspots.getPixel(evX, evY);
                        if(Math.abs(color+1172700)<100){
                            b.setVisibility(View.VISIBLE);
                            c.setVisibility(View.INVISIBLE);
                        } else if (Math.abs(color+8421505)<100) {
                            b.setVisibility(View.INVISIBLE);
                            c.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(MainActivity2.this, Integer.toString(Color.RED)+" "+Integer.toString(hotspots.getPixel(evX, evY)), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }
    @SuppressLint("DefaultLocale")
    public void trig(){
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        selectedDate = String.format("%04d%02d%02d%02d%02d%03d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        spaces = db.collection("lecture_hall1");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("uid",uid);
        data1.put("date",selectedDate.substring(0,8));
        data1.put("start_time",picker.getStartTimeMinutes());
        data1.put("end_time",picker.getEndTimeMinutes());
        data1.put("duration",picker.getDuration().getDurationMinutes());
        spaces.document(selectedDate+" "+uid).set(data1);
    }
    public void getData(int selected_start_time,int selected_end_time){

        db=FirebaseFirestore.getInstance();

        String targetDate = "20230528"; // Specify the target date in the format "YYYYMMDD"

        db.collection("lecture_hall1")
                .whereEqualTo("date", targetDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean trueOrFalse=true;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the booking data from each document
                            String uid = document.getString("uid");
                            String date = document.getString("date");
                            int startTime = document.getLong("start_time").intValue();
                            int endTime = document.getLong("end_time").intValue();
                            int duration = document.getLong("duration").intValue();
                            if(!((selected_start_time<startTime && selected_end_time<=startTime)
                            ||(selected_start_time>=endTime))){
                                trueOrFalse=false;

                            }

                            // Log the booking data
//                            Log.i("abc", "selected_start_time: " + selected_start_time);
//                            Log.i("abc", "selected_end_time: " + selected_end_time);
//                            Log.i("abc", "Start Time: " + startTime);
//                            Log.i("abc", "End Time: " + endTime);
//                            Log.i("abc", "Duration: " + duration);
                        }
                        upDateUi(trueOrFalse);
                    } else {

                        // Handle errors
                        Log.e("abc", "Error getting bookings: " + task.getException());
                    }
                });
    }


    public void upDateUi(Boolean trueOrFalse){
        ImageView b=findViewById(R.id.image_1);
        ImageView c=findViewById(R.id.image_2);
        if(trueOrFalse){
            b.setVisibility(View.INVISIBLE);
            c.setVisibility(View.VISIBLE);
//            Log.i("abc", "a");

        }else{
            b.setVisibility(View.VISIBLE);
            c.setVisibility(View.INVISIBLE);
//            Log.i("abc", "b");

        }
    }

}