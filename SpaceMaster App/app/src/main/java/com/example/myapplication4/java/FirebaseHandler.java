package com.example.myapplication4.java;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class FirebaseHandler {

    private static FirebaseFirestore db;
    private static Calendar calendar;
    private static CollectionReference spaces;
    private static CollectionReference notifyme;
    private static String dateTimeNow;
    public static void firebaseToLocal(String targetDate, String spaces, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        db = FirebaseFirestore.getInstance();
        spaces = "spaces";

        db.collection(spaces)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> hashMapList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the booking data from each document
                            String uid = document.getString("uid");
                            String date = document.getString("date");
                            String startTime = document.getString("start_time");
                            String endTime = document.getString("end_time");
                            String lecture_hall =document.getString("lecture_hall");
                            String key=document.getString("key");
                            String user=document.getString("user");

                            Map<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("uid", uid);
                            hashMap1.put("date", date);
                            hashMap1.put("start_time", startTime);
                            hashMap1.put("end_time", endTime);
                            hashMap1.put("lecture_hall", lecture_hall);
                            hashMap1.put("user",user);
                            hashMap1.put("key",key);
                            hashMapList.add(hashMap1);

                        }

                        // Convert the list to JSON and store it in SharedPreferences
                        String json = new Gson().toJson(hashMapList);
                        editor.putString("data", json);
                        editor.apply();

                    } else {
                        // Handle errors
                        Log.e("abc", "Error getting bookings: " + task.getException());
                    }

                });
    }
    public static List<Map<String, Object>> readLocal(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString("data", null);
        List<Map<String, Object>> hashMapList = new ArrayList<>();

        if (jsonData != null) {
            // Data exists in SharedPreferences
            // Convert JSON string to your desired data structure
            hashMapList = new Gson().fromJson(jsonData, new TypeToken<List<Map<String, Object>>>(){}.getType());
//            Toast.makeText(context.getApplicationContext(), "xss",Toast.LENGTH_SHORT).show();

//            // Now you can access and use the retrieved data
//            for (Map<String, Object> hashMap : hashMapList) {
////                // Access the values from the hashMap
//                String uid = (String) hashMap.get("uid");
//                String date = (String) hashMap.get("date");
//                String startTime = (String) hashMap.get("start_time");
//                String endTime = (String) hashMap.get("end_time");
//                String lecture_hall = (String) hashMap.get("lecture_hall");
//
//                data.add(uid+date+startTime+endTime+lecture_hall);
//            }
//        } else {
//            // Data doesn't exist in SharedPreferences
//            // Handle the case when no data is found
//            // ...
        }
        return hashMapList;

    }
    @SuppressLint("DefaultLocale")
    public static void localToFirebase(Context context,String date,int startTime,int endTime,String lecture_hall,NewBookingFragment newBookingFragment){
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        dateTimeNow = String.format("%04d%02d%02d%02d%02d%03d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        spaces = db.collection("spaces");
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("uid",uid);
        data1.put("date", date);
        data1.put("start_time",Integer.toString(startTime));
        data1.put("end_time",Integer.toString(endTime));
        data1.put("lecture_hall",lecture_hall);
        data1.put("key",dateTimeNow+uid);
        data1.put("user",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        spaces.document(dateTimeNow+uid).set(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context.getApplicationContext(),"Booking Sucessful",Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                List<Map<String, Object>> hashMapList=readLocal(context);
                hashMapList.add(data1);
                String json = new Gson().toJson(hashMapList);
                editor.putString("data", json);
                editor.apply();
                newBookingFragment.upDateUi(3);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context.getApplicationContext(),"Booking UnSucessful",Toast.LENGTH_SHORT).show();
                    }
                });
        }


    @SuppressLint("DefaultLocale")
    public static void notifyMeFirebase(Context context,String date,int startTime,int endTime,String lecture_hall){
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        dateTimeNow = String.format("%04d%02d%02d%02d%02d%03d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        notifyme = db.collection("notifyme");
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("uid",uid);
        data1.put("date", date);
        data1.put("start_time",Integer.toString(startTime));
        data1.put("end_time",Integer.toString(endTime));
        data1.put("lecture_hall",lecture_hall);
        data1.put("key",dateTimeNow+uid);
        data1.put("user",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        notifyme.document(dateTimeNow+uid).set(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context.getApplicationContext(),"You will be notified",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context.getApplicationContext(),"Notification Unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                });
    }


        public static void fireBaseRemove(String key,Context context){
            db = FirebaseFirestore.getInstance();
            db.collection("spaces").document(key)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firebaseToLocal("","",context);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error deleting document", e);
                        }
                    });

        }
        public static void getAdminDetails(Context context) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            db.collection("admin")
                    .whereEqualTo("uid", currentUid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean isAdmin = !task.getResult().isEmpty();
                                editor.putBoolean("isAdmin", isAdmin);
                                editor.apply();

                            } else {
                                // An error occurred while accessing the "admin" collection
                            }
                        }
                    });
        }
    public static boolean isAdminUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAdmin", false);
    }
    public static void sendTokenToServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("uid",uid);
        data1.put("token",sharedPreferences.getString("FCMToken", ""));
        db.collection("FCMtokens").document(uid).set(data1);
    }

    public static void findNotifiers(Context context,String  removeDate,String removeLecture_hall){
        db = FirebaseFirestore.getInstance();

//        db.collection("spaces")
//                .whereEqualTo("date", removeDate)
//                .whereEqualTo("lecture_hall",removeLecture_hall)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        List<Map<String, Object>> hashMapList = new ArrayList<>();
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            // Retrieve the booking data from each document
//                            String uid = document.getString("uid");
//                            int startTime = Math.toIntExact((long) document.getLong("start_time"));
//                            int endTime = Math.toIntExact((long) document.getLong("end_time"));
//                            Map<String, Object> hashMap1 = new HashMap<>();
//                            hashMap.put("uid", uid);
//                            hashMap1.put("date", date);
//                            hashMap1.put("start_time", startTime);
//                            hashMap1.put("end_time", endTime);
//                            if(startTime<=findStartTime && end)
//
//                        }
//
//                    } else {
//                        // Handle errors
//                        Log.e("abc", "Error getting Notifications: " + task.getException());
//                    }
//
//                });
    }
}
