package com.example.SpaceMaster.java;

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

import com.example.SpaceMaster.R;
import com.example.myapplication5.kotlin.KotlinFile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.joery.timerangepicker.TimeRangePicker;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;

    List<Map<String, Object>> hashMapList = new ArrayList<>();
    private TimeRangePicker picker;
    private String selectedDate;
    private FirebaseFirestore db;
    private CollectionReference spaces;
    private final Object lock = new Object(); // Lock object for synchronization
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
        LocalDate todayDate=LocalDate.now();
        LocalDate maxDate =todayDate.plusDays(30);  // Specify your desired maximum date
        datePicker.setMinDate(todayDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        datePicker.setMaxDate(maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());


        datePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                hashMapList.clear();
                getData(String.format("%04d%02d%02d",year,monthOfYear+1,dayOfMonth),"b");
//                textView.setText(selectedDate);
//                Toast.makeText(MainActivity2.this, String.format("%04d%02d%02d",year,monthOfYear+1,dayOfMonth),Toast.LENGTH_SHORT).show();
                upDateUi(picker.getStartTimeMinutes(),picker.getEndTimeMinutes());

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
    public void getData(String targetDate,String lecture_hall){

        db=FirebaseFirestore.getInstance();
        // Specify the target date in the format "YYYYMMDD"
        lecture_hall="lecture_hall1";

        db.collection(lecture_hall)
                .whereEqualTo("date", targetDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<String, Object> hashMap1 = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the booking data from each document
                            String uid = document.getString("uid");
                            String date = document.getString("date");
                            int startTime = document.getLong("start_time").intValue();
                            int endTime = document.getLong("end_time").intValue();
                            int duration = document.getLong("duration").intValue();
                            hashMap1.put("uid",uid);
                            hashMap1.put("date",date);
                            hashMap1.put("start_time",startTime);
                            hashMap1.put("end_time", endTime);
                            hashMap1.put("duration", duration);
                            hashMapList.add(hashMap1);


                            // Log the booking data
//                            Log.i("abc", "selected_start_time: " + selected_start_time);
//                            Log.i("abc", "selected_end_time: " + selected_end_time);
                            Log.i("abc", "Start Time: " + startTime);
                            Log.i("abc", "End Time: " + endTime);
//                            Log.i("abc", "Duration: " + duration);
                        }
                    } else {

                        // Handle errors
                        Log.e("abc", "Error getting bookings: " + task.getException());
                    }
                    upDateUi(picker.getStartTimeMinutes(),picker.getEndTimeMinutes());

                });
    }


    public void upDateUi(int selected_start_time,int selected_end_time){
        boolean trueOrFalse=true;
        ImageView b=findViewById(R.id.image_1);
        ImageView c=findViewById(R.id.image_2);
        for (Map<String, Object> hashMap : hashMapList) {
            int startTime= (int) hashMap.get("start_time");
            int endTime= (int) hashMap.get("end_time");
            if(!((selected_start_time<startTime && selected_end_time<=startTime)
                    ||(selected_start_time>=endTime))){
                trueOrFalse=false;

            }
        }
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