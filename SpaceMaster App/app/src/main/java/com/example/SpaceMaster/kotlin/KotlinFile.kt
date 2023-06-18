package com.example.myapplication5.kotlin
import android.util.Log
import com.example.SpaceMaster.java.MainActivity2
import nl.joery.timerangepicker.TimeRangePicker

class KotlinFile {
    fun hello(picker:nl.joery.timerangepicker.TimeRangePicker,inst:MainActivity2){
        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                Log.i("aa", "Start time: " + startTime)
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                Log.i("aa", "End time: " + endTime)
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                Log.i("aa", "Duration: " + duration.durationMinutes)
                inst.upDateUi(picker.startTimeMinutes, picker.endTimeMinutes)
            }
        })

        picker.setOnDragChangeListener(object : TimeRangePicker.OnDragChangeListener {
            override fun onDragStart(thumb: TimeRangePicker.Thumb): Boolean {
                // Do something on start dragging

                return true // Return false to disallow the user from dragging a handle.
            }

            override fun onDragStop(thumb: TimeRangePicker.Thumb) {
//                inst.trig()
            }
        })
    }
}