package com.example.myapplication5.kotlin
import com.example.myapplication4.java.NewBookingFragment
import nl.joery.timerangepicker.TimeRangePicker

class TimePicker {
<<<<<<< HEAD
    fun hello(picker:nl.joery.timerangepicker.TimeRangePicker,inst:MainActivity2){
=======
    fun hello(picker:nl.joery.timerangepicker.TimeRangePicker,isLastArgument:Boolean,newbookingfragment: NewBookingFragment){
>>>>>>> 4a18387d2e60ac2ed596e171540786d2b751c1c2
        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
//                Log.i("aa", "Start time: " + startTime)



            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
//                Log.i("aa", "End time: " + endTime)
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
//                Log.i("aa", "Duration: " + duration.durationMinutes)
                if (isLastArgument) {
                    newbookingfragment.upDateUi()
                }
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