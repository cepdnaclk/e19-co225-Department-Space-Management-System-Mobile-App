package com.example.myapplication4.kotlin

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.myapplication4.R
import com.example.myapplication4.databinding.WeekCalendarDayBinding
import com.example.myapplication4.java.NewBookingFragment
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.sample.shared.displayText
import com.kizitonwose.calendar.sample.shared.getWeekPageTitle
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class WeekCalendar {
    private var setDate=false;
    private var selectedDate: LocalDate? = null
    fun setSelectedDate(date:LocalDate){
        selectedDate=date;
        setDate=true;
    }

    fun getSelectedDate(): LocalDate? {
        return selectedDate?: LocalDate.of(1970, 1, 1)
    }
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    fun weekcalendarcaller(calendarView: WeekCalendarView, exsevenview: Toolbar,islastArgument: Boolean,newbookingfragment: NewBookingFragment=NewBookingFragment()) {
//        class DayViewContainer(view: View) : ViewContainer(view) {
//            // Will be set when this container is bound. See the dayBinder.
//            lateinit var day: WeekDay
////                val bind = CalendarDayLayoutBinding.bind(view)
//            val bind= Example7CalendarDayBinding.bind(view)
//
////            val textView =view.findViewById<TextView>(R.id.calendarDayText)
//
//            init {
//                bind.exSevenDateText.setOnClickListener {
//                    if (day.position == WeekDayPosition.RangeDate) {
//                        if (selectedDate == day.date) {
//                            selectedDate = null
//                            calendarView.notifyDayChanged(day)
//                        } else {
//                            val oldDate = selectedDate
//                            selectedDate = day.date
//                            calendarView.notifyDateChanged(day.date)
//                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
//                        }
////                        menuItem.isVisible = selectedDate != null
//                    }
//                }
//            }
//        }
//
//
//
//        calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//            override fun bind(container: DayViewContainer, data: WeekDay) {
//                container.day = data
//                val day = data
//                val textView = container.bind.exSevenDateText
//                textView.text = day.date.dayOfMonth.toString()
//
//                val textViewx = container.bind.exSevenDayText
//                textViewx.text = "MON"
//
//
//                if (day.position == WeekDayPosition.RangeDate) {
//                    // Show the month dates. Remember that views are reused!
//                    textView.visibility = View.VISIBLE
//                    if (day.date == selectedDate) {
//                        // If this is the selected date, show a round background and change the text color.
//                        textView.setTextColor(Color.YELLOW)
//                        textViewx.setTextColor(Color.YELLOW)
//                        container.bind.exSevenSelectedView.visibility=View.VISIBLE
//                    } else {
//                        // If this is NOT the selected date, remove the background and reset the text color.
//                        textView.setTextColor(Color.WHITE)
//                        textViewx.setTextColor(Color.WHITE)
//                        container.bind.exSevenSelectedView.visibility=View.INVISIBLE
//                        textView.background = null
//                    }
////
////                    // Add a click listener to the day cell view to handle date selection.
////                    textView.setOnClickListener {
////                        selectedDate = day.date
////                        calendarView.notifyDayChanged(day)
////                    }
//                } else {
//                    // Hide in and out dates
//                    textView.visibility = View.INVISIBLE
//                }
//            }
//        }
//
//        val currentDate = LocalDate.now()
//        val currentMonth = YearMonth.now()
//        val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
//        val endDate = currentMonth.plusMonths(100).atEndOfMonth()  // Adjust as needed
//        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
//        calendarView.setup(startDate, endDate, firstDayOfWeek)
//        calendarView.scrollToWeek(currentDate)

//
//        class MonthViewContainer(view: View) : ViewContainer(view) {
//            val textView = view.findViewById<TextView>(R.id.exTwoHeaderText)
//        }
//        calendarView.monthHeaderBinder =
//            object : MonthHeaderFooterBinder<MonthViewContainer> {
//                override fun create(view: View) = MonthViewContainer(view)
//                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
//                    container.textView.text = data.yearMonth.toString()
//                }
//            }
//
//



    class DayViewContainer(view: View) : ViewContainer(view) {
    val bind = WeekCalendarDayBinding.bind(view)
    lateinit var day: WeekDay

    init {
        view.setOnClickListener {
            if (selectedDate != day.date) {
                val oldDate = selectedDate
                selectedDate = day.date
                calendarView.notifyDateChanged(day.date)
                oldDate?.let { calendarView.notifyDateChanged(it) }
            }
        }
    }

        fun bind(day: WeekDay) {
            this.day = day
            bind.weekDateText.text = dateFormatter.format(day.date)
            bind.weekDayText.text = day.date.dayOfWeek.displayText()

            val colorRes = if (day.date == selectedDate) {
                if(islastArgument) {
                    newbookingfragment.upDateUi(3);
                }
                R.color.example_7_yellow
            } else {
                R.color.example_7_white
            }
//            bind.exSevenDateText.setTextColor(view.context.getColorCompat(colorRes))
            bind.weekSelectedView.isVisible = day.date == selectedDate
        }
    }

        calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
        override fun create(view: View) = DayViewContainer(view)
        override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
    }

    calendarView.weekScrollListener = { weekDays ->
        exsevenview.title = getWeekPageTitle(weekDays)
    }

    val currentMonth = YearMonth.now()
        calendarView.setup(
        currentMonth.minusMonths(5).atStartOfMonth(),
        currentMonth.plusMonths(5).atEndOfMonth(),
        firstDayOfWeekFromLocale(),
    )
        calendarView.scrollToDate(LocalDate.now())
        if(setDate){
            selectedDate?.let { calendarView.scrollToDate(it) }
        }
    }

}
