package com.example.myapplication4.java;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetUserBookings {
    public static List<Map<String, Object>>  readUserBookingsOnDate(Context context,String date,Boolean isAdmin) {
        List<Map<String, Object>> hashMapList = FirebaseHandler.readLocal(context);
        String targetKey = "uid";  // Specify the target key to search for
        String targetKey2 = "date";
        Object targetValue = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Specify the target value to search for

        List<Map<String, Object>> resultList = new ArrayList<>();  // List to store matching maps

        for (Map<String, Object> map : hashMapList) {
            // Check if the map contains the specified key-value pair
            if (map.containsKey(targetKey2) && map.get(targetKey2).equals(date)) {
                if(isAdmin){
                    resultList.add(map);
                }else{
                if(map.containsKey(targetKey) && map.get(targetKey).equals(targetValue)) {
                    resultList.add(map);
                }
                }
            }
        }
        return resultList ;
    }
    public static List<String> readUserBookedDates(Context context,Boolean isAdmin){
        List<Map<String, Object>> hashMapList = FirebaseHandler.readLocal(context);
        String targetKey = "uid";  // Specify the target key to search for
        Object targetValue = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Specify the target value to search for
        List<String> dataList=new ArrayList<>();
        for (Map<String, Object> map : hashMapList) {
            // Check if the map contains the specified key-value pair
            if(isAdmin){
                dataList.add((String) map.get("date"));
            }else{
            if (map.containsKey(targetKey) && map.get(targetKey).equals(targetValue)) {
                dataList.add((String) map.get("date"));
            }}
        }
        return dataList;
    }

}
