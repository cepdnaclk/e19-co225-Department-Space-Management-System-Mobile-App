package com.example.myapplication4.java;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication4.java.notificationServer.FCMNotificationSender;
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
import java.util.Arrays;
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
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("abc", "Error listening to data changes: " + error);
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        List<Map<String, Object>> hashMapList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : value) {
                            // Retrieve the booking data from each document
                            String uid = document.getString("uid");
                            String date = document.getString("date");
                            String startTime = document.getString("start_time");
                            String endTime = document.getString("end_time");
                            String lecture_hall = document.getString("lecture_hall");
                            String key = document.getString("key");
                            String user = document.getString("user");
                            List<String> uidresponsibles = (List<String>) document.get("uidresponsibles");

                            Map<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("uid", uid);
                            hashMap1.put("date", date);
                            hashMap1.put("start_time", startTime);
                            hashMap1.put("end_time", endTime);
                            hashMap1.put("lecture_hall", lecture_hall);
                            hashMap1.put("user", user);
                            hashMap1.put("key", key);
                            hashMap1.put("uidresponsibles", uidresponsibles);
                            hashMapList.add(hashMap1);
                        }

                        // Convert the list to JSON and store it in SharedPreferences
                        String json = new Gson().toJson(hashMapList);
                        editor.putString("data", json);
                        editor.apply();
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
        data1.put("uidresponsibles",getCheckedUids(context));
        spaces.document(dateTimeNow+uid).set(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context.getApplicationContext(),"Booking Sucessful",Toast.LENGTH_SHORT).show();
//                SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                List<Map<String, Object>> hashMapList=readLocal(context);
//                hashMapList.add(data1);
//                String json = new Gson().toJson(hashMapList);
//                editor.putString("data", json);
//                editor.apply();
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
        notifyme.document(uid+date+lecture_hall).set(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
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


        public static void fireBaseRemove(String lecture_hall,String date,String key,Context context){
            db = FirebaseFirestore.getInstance();
            db.collection("spaces").document(key)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            findNotifiers(context,date,lecture_hall);
//                            Log.i("abc",date+lecture_hall);
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        db.collection("admin")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle any errors
                        Log.e("abc", "Error listening to admin data changes: " + error);
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        List<HashMap<String, String>> adminList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : value) {
                            HashMap<String, String> adminMap = new HashMap<>();
                            String uid = document.getString("uid");
                            String name = document.getString("name");
                            adminMap.put("checked", "false");
                            adminMap.put("uid", uid);
                            adminMap.put("name", name);
                            adminList.add(adminMap);
                        }

                        // Convert the adminList to a JSON string
                        if(getAdminListSize(context)!=adminList.size()) {
                            Gson gson = new Gson();
                            String adminJson = gson.toJson(adminList);

                            // Save the adminJson to SharedPreferences
                            editor.putString("adminList", adminJson);
                            editor.apply();
                        }
                    }
                });
    }



        public static List<HashMap<String, String>> readAdminDetails(Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String adminJson = sharedPreferences.getString("adminList", "");

            // Convert the adminJson string to a List of HashMaps
            Gson gson = new Gson();
            return gson.fromJson(adminJson, new TypeToken<List<HashMap<String, String>>>() {}.getType());
        }
    public static int getAdminListSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String adminJson = sharedPreferences.getString("adminList", "");

        if (!adminJson.isEmpty()) {
            Gson gson = new Gson();
            List<HashMap<String, String>> adminList = gson.fromJson(adminJson, new TypeToken<List<HashMap<String, String>>>() {}.getType());
            return adminList.size();
        } else {
            return 0; // Return 0 if "adminList" does not exist
        }
    }

    public static List<String> getCheckedUids(Context context) {
        List<HashMap<String, String>> adminList = readAdminDetails(context);
        List<String> checkedUids = new ArrayList<>();

        for (HashMap<String, String> adminMap : adminList) {
            String uid = adminMap.get("uid");
            String checked = adminMap.get("checked");

            if (checked != null && checked.equals("true")) {
                checkedUids.add(uid);
            }
        }

        return checkedUids;
    }


    public static boolean isAdminUser(Context context) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String adminJson = sharedPreferences.getString("adminList", "");

        // Convert the adminJson string to a List of HashMaps
        Gson gson = new Gson();
        List<HashMap<String, String>> adminList = gson.fromJson(adminJson, new TypeToken<List<HashMap<String, String>>>() {}.getType());

        // Check if the currentUid is in the adminList
        if (adminList != null) {
            for (HashMap<String, String> adminMap : adminList) {
                String uid = adminMap.get("uid");
                if (uid != null && uid.equals(currentUid)) {
                    return true;
                }
            }
        }

        return false;
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

        db.collection("notifyme")
                .whereEqualTo("date", removeDate)
                .whereEqualTo("lecture_hall",removeLecture_hall)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> hashMapList = readLocal(context);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the booking data from each document
                            String uid = document.getString("uid");
                            int startTime = Integer.parseInt(document.getString("start_time"));
                            int endTime = Integer.parseInt(document.getString("end_time"));
                            boolean isConflict = false;
                            Log.i("abc",document.getString("start_time")+"Pleasebooking"+document.getString("end_time"));

                            for (Map<String, Object> booking : hashMapList) {
                                int start = Integer.parseInt((String) booking.get("start_time"));
                                int end = Integer.parseInt((String) booking.get("end_time"));

                                if(removeDate.equals((String) booking.get("date")) && removeLecture_hall.equals((String) booking.get("lecture_hall"))){
                                // Check for conflicts
                                if (startTime < end && start < endTime) {
                                    // There is a conflict with the current booking
//                                    Log.i("abc","conflic");
                                    isConflict = true;
                                    break;
                                }}
                            }

                            if (isConflict) {
                                // Conflict exists, handle accordingly
                            } else {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                database.collection("FCMtokens")
                                        .whereEqualTo("uid", uid)
                                        .get()
                                        .addOnCompleteListener(tas -> {
                                            if (tas.isSuccessful()) {
                                                for (QueryDocumentSnapshot documen : tas.getResult()) {
                                                    String recipientToken = documen.getString("token");
                                                    FCMNotificationSender fcmNotificationSender=new FCMNotificationSender();
                                                    fcmNotificationSender.sendNotification(recipientToken,"Time slot Available","leture hall: "+removeLecture_hall+"  date: "+removeDate);
                                                }
                                            } else {
                                            }
                                        });

                            }
                        }

                    } else {
                        // Handle errors
                        Log.e("abc", "Error getting Notifications: " + task.getException());
                    }

                });
    }
    public static String lectureHallNamingConversion(int position){
        List<String> spaceNames = new ArrayList<>();
        spaceNames.add("Computer Lab 1st Floor");
        spaceNames.add("Networking Lab 1st Floor");
        spaceNames.add("Digital Electronics Lab");
        spaceNames.add("Discussion Room");
        spaceNames.add("ESCAL");
        spaceNames.add("Free Space");
        spaceNames.add("Top Floor Computer Lab");
        return spaceNames.get(position-1);
    }
}
