package com.example.myapplication5.kotlin
import com.example.myapplication4.java.NewBookingFragment
import nl.joery.timerangepicker.TimeRangePicker

class TimePicker {
    fun hello(picker:nl.joery.timerangepicker.TimeRangePicker,isLastArgument:Boolean,newbookingfragment: NewBookingFragment){
        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
//                Log.i("aa", "Start time: " + startTime)
                if (isLastArgument) {
                    newbookingfragment.upDateUi(1)
                }



            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
//                Log.i("aa", "End time: " + endTime)
                if (isLastArgument) {
                    newbookingfragment.upDateUi(2)
                }
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
//                Log.i("aa", "Duration: " + duration.durationMinutes)

            }
        })

        picker.setOnDragChangeListener(object : TimeRangePicker.OnDragChangeListener {
            override fun onDragStart(thumb: TimeRangePicker.Thumb): Boolean {
                // Do something on start dragging

                return true // Return false to disallow the user from dragging a handle.
            }

            override fun onDragStop(thumb: TimeRangePicker.Thumb) {
                if (isLastArgument) {
                    newbookingfragment.upDateUi(3)
                }
            }
        })
    }
}