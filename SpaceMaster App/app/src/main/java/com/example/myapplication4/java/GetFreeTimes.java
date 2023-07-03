package com.example.myapplication4.java;

import android.content.Context;
import android.util.Log;

import com.example.myapplication4.java.GetUserBookings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetFreeTimes {
    public static List<String> getFreedates(Context context,String lecture_hall){
        LocalDate today = LocalDate.now();
        List<String> dates = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            if(!getFreeTimesonDate(context,today.plusDays(i).toString(),lecture_hall).isEmpty()){
            dates.add(today.plusDays(i).toString());
            }
        }
        return dates;
    }

    public static List<Map<String, Object>> getFreeTimesonDate(Context context, String selectedDate, String lecture_hall) {
        List<Map<String, Object>> userBookings = GetUserBookingsForFreeTime.readUserBookingsOnDate(context, selectedDate);
        List<Map<String, Object>> freeTimes = new ArrayList<>();

        // Set the start and end time for the day in minutes
        int startTime = 8 * 60; // 8 am -> 8 hours * 60 minutes
        int endTime = 17 * 60; // 5 pm -> 17 hours * 60 minutes

        // Sort the userBookings by start time
        Collections.sort(userBookings, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> booking1, Map<String, Object> booking2) {
                int startTime1 = Integer.parseInt((String)booking1.get("start_time"));
                int startTime2 = Integer.parseInt((String)booking2.get("start_time"));
                return Integer.compare(startTime1, startTime2);
            }
        });

        // Iterate over the bookings
        int previousEndTime = startTime;

        for (Map<String, Object> booking : userBookings) {
            String hall = (String) booking.get("lecture_hall");

            if (hall != null && hall.equals(lecture_hall)) {
                // Found a booking with the specified lecture_hall
                int bookingStartTime = Integer.parseInt((String) booking.get("start_time"));
                int bookingEndTime = Integer.parseInt((String) booking.get("end_time"));

                if(bookingStartTime<endTime && bookingStartTime>startTime){
                if (bookingStartTime > previousEndTime) {
                    // Add the free time interval
                    Map<String, Object> freeTime = new HashMap<>();
                    freeTime.put("start_time",  String.valueOf(previousEndTime));
                    freeTime.put("end_time", String.valueOf(bookingStartTime));
                    freeTime.put("lecture_hall",lecture_hall);
                    freeTime.put("date",selectedDate);
                    freeTimes.add(freeTime);
                }
                }

                // Update the previousEndTime to the end time of the current booking
                previousEndTime = bookingEndTime;

            }

        }
        if(previousEndTime<endTime && previousEndTime>=startTime){
        // Check if there is a gap between the last booking and the end time of the day
        if (endTime > previousEndTime) {
            // Add the free time interval
            Map<String, Object> freeTime = new HashMap<>();
            freeTime.put("start_time", String.valueOf(previousEndTime));
            freeTime.put("end_time", String.valueOf(endTime));
            freeTime.put("lecture_hall",lecture_hall);
            freeTime.put("date",selectedDate);
            freeTimes.add(freeTime);
        }
    }

        return freeTimes;
    }


}
