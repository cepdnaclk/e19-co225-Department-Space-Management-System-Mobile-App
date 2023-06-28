package com.example.myapplication4.kotlin

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.example.myapplication4.R
import com.example.myapplication4.databinding.MonthCalendarDayBinding
import com.example.myapplication4.databinding.MonthCalendarHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.sample.shared.displayText
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.YearMonth

class MonthCalendar {
    private var selectedDate: LocalDate? = null
    fun monthcalendarcaller(calendarView: CalendarView) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val bind = MonthCalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        }
        //                        menuItem.isVisible = selectedDate != null
                    }
                }
            }
        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val day = data

                container.bind.exOneDayText.text = day.date.dayOfMonth.toString()
                if (day.position == DayPosition.MonthDate) {
                    // Show the month dates. Remember that views are reused!
                    container.bind.exOneDayText.visibility = View.VISIBLE
                    if (day.date == selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        container.bind.exOneDayText.setTextColor(Color.WHITE)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        container.bind.exOneDayText.setTextColor(Color.BLACK)
                        container.bind.exOneDayText.background = null
                    }

                } else {
                    // Hide in and out dates
                    container.bind.exOneDayText.visibility = View.INVISIBLE
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val daysOfWeek = daysOfWeek(firstDayOfWeek = java.time.DayOfWeek.SUNDAY)
        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)


        class MonthViewContainer(view: View) : ViewContainer(view) {
            val binding =MonthCalendarHeaderBinding.bind(view)
            val textView = view.findViewById<TextView>(R.id.exTwoHeaderText)
            val legendLayout = binding.legendLayout.root
        }
        calendarView.monthHeaderBinder =
        object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                container.textView.text = data.yearMonth.displayText()
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = data.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].name.toString().substring(0,3)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                        }
                }
            }
        }
    }
}