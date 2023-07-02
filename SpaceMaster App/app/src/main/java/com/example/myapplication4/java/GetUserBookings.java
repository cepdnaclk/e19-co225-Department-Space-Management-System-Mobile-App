package com.example.myapplication4.java;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetUserBookings {
    public static List<Map<String, Object>>  readUserBookingsOnDate(Context context,String date) {
        List<Map<String, Object>> hashMapList = FirebaseHandler.readLocal(context);
        Object userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Specify the target value to search for

        List<Map<String, Object>> resultList = new ArrayList<>();  // List to store matching maps

        for (Map<String, Object> map : hashMapList) {
            // Check if the map contains the specified key-value pair
            List<String> stringKeyList=new ArrayList<>();
            if (map.containsKey("uidresponsibles")) {
                Object value = map.get("uidresponsibles");
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        stringKeyList = (List<String>) value;
                    }
                }
            }

            if (map.containsKey("date") && map.get("date").equals(date)) {

                    if(map.containsKey("uidresponsibles") && stringKeyList.contains(userUid)) {
                        resultList.add(map);
                    }
                    if(map.containsKey("uid") && map.get("uid").equals(userUid)) {
                        resultList.add(map);
                    }
            }
        }
        return resultList ;
    }
    public static List<String> readUserBookedDates(Context context){
        List<Map<String, Object>> hashMapList = FirebaseHandler.readLocal(context);
        Object userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Specify the target value to search for
        List<String> dataList=new ArrayList<>();
        for (Map<String, Object> map : hashMapList) {
            // Check if the map contains the specified key-value pair
            List<String> stringKeyList=new ArrayList<>();
            if (map.containsKey("uidresponsibles")) {
                Object value = map.get("uidresponsibles");
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        stringKeyList = (List<String>) value;
                    }
                }
            }
            Log.i("abc", (String) map.get("uid"));

                if (map.containsKey("uidresponsibles") && stringKeyList.contains(userUid)) {
                    dataList.add((String) map.get("date"));
                }
                if (map.containsKey("uid") && map.get("uid").equals(userUid)) {
                    dataList.add((String) map.get("date"));
            }
        }
        return dataList;
    }

}
