package com.example.myapplication4.kotlin

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.R
import com.example.myapplication4.java.NewBookingFragment
import com.example.myapplication4.java.SpaceCalendarAdapter
import java.time.LocalDate

object RecyclerViewSetupHelper {
    @JvmStatic
    fun setupRecyclerView(
        recyclerView: RecyclerView,
        dataList: List<Map<String, Any>>,
        context: Context,
        fragmentManager: FragmentManager
    ) {
        val adapter = SpaceCalendarAdapter(context, dataList)
        adapter.setOnItemClickListener(object : SpaceCalendarAdapter.OnItemClickListener {
            override fun onItemClick(item: Map<String, Any>) {
//                Toast.makeText(context, "Item clicked: ${item.toString()}", Toast.LENGTH_SHORT)
//                    .show()
                val dateString = item["date"].toString()
                val date = LocalDate.parse(dateString)
                val startTime = item["start_time"].toString().toInt()
                val endTime = item["end_time"].toString().toInt()
                val lectureHall = item["lecture_hall"].toString()
                val fragment = NewBookingFragment(date,startTime,endTime,lectureHall)

                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
