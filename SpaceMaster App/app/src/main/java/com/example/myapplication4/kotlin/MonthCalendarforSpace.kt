package com.example.myapplication4.kotlin

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.R
import com.example.myapplication4.databinding.MonthCalendarDayBinding
import com.example.myapplication4.databinding.MonthCalendarHeaderBinding
import com.example.myapplication4.java.CalendarAdapter
import com.example.myapplication4.java.GetUserBookings
import com.example.myapplication4.java.SwipeToDeleteCallback
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class MonthCalendarforSpace {

    private var selectedDate: LocalDate? = LocalDate.now()
    fun monthcalendarcaller(calendarView: CalendarView,rootView:View,Context:Context,isAdmin:Boolean) {

        var list: MutableList<String> = GetUserBookings.readUserBookedDates(Context)
        if (list.isNotEmpty()) {
            selectedDate = LocalDate.parse(list.first(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        var recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view)
        var dataList =  GetUserBookings.readUserBookingsOnDate(Context,selectedDate.toString())
        var adapter =
            CalendarAdapter(Context, dataList)
        var itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(Context);




        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val bind = MonthCalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        list= GetUserBookings.readUserBookedDates(Context)
                        val oldDate = selectedDate
                        selectedDate = day.date
                        calendarView.notifyDateChanged(day.date)
                        oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        itemTouchHelper.attachToRecyclerView(null);
                        dataList =  GetUserBookings.readUserBookingsOnDate(Context,selectedDate.toString())
                        adapter =
                            CalendarAdapter(
                                Context,
                                dataList
                            )
                        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
                        itemTouchHelper.attachToRecyclerView(recyclerView)
                        recyclerView.adapter = adapter;
                        recyclerView.layoutManager = LinearLayoutManager(Context);

//                        val dpValue = 90 // Height of each item in dp
//                        val itemCount = dataList.size + 3 // Number of items in the list
//                        val density = Context.resources.displayMetrics.density
//                        val itemHeightPx = (dpValue * density).toInt()
//                        val listViewHeightPx = itemHeightPx * itemCount
//                        val layoutParams = recyclerView.layoutParams
//                        layoutParams.height = listViewHeightPx
//                        recyclerView.layoutParams = layoutParams
            }}}
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
                        container.bind.exOneDayText.setTextColor(ContextCompat.getColor(Context, R.color.example_1_bg))
                        container.bind.exOneDayText.setBackgroundResource(R.drawable.example_1_today_bg)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        container.bind.exOneDayText.setTextColor(ContextCompat.getColor(Context,R.color.example_1_white) )
                        container.bind.exOneDayText.background = null
                    }
                    if (list.contains(day.date.toString()) && day.date!=selectedDate) {
                        // The date is present in the list
                        container.bind.exOneDayText.setTextColor(ContextCompat.getColor(Context, R.color.example_1_bg))
                        container.bind.exOneDayText.setBackgroundResource(R.drawable.example_1_selected_bg)

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
            val textView = view.findViewById<TextView>(R.id.exOneDayText)
            val textView2 = view.findViewById<TextView>(R.id.exOneMonthText)
            val legendLayout = binding.legendLayout.root
        }
        calendarView.monthHeaderBinder =
        object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                container.textView.text = data.yearMonth.year.toString()
                container.textView2.text=data.yearMonth.month.toString()
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