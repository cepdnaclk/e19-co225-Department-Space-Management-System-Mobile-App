package com.example.myapplication4.java;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication4.R;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {

    private List<Map<String, Object>> dataList;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, List<Map<String, Object>> dataList) {
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.lecture_hall = convertView.findViewById(R.id.text_view_lecture_hall);
            holder.start_time = convertView.findViewById(R.id.text_view_start_time);
            holder.end_time = convertView.findViewById(R.id.text_view_end_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lecture_hall.setText((String)getItem(position).get("lecture_hall"));
        holder.start_time.setText(convertTimeToAMPM(Integer.valueOf((String) getItem(position).get("start_time"))));
        holder.end_time.setText(convertTimeToAMPM(Integer.valueOf((String) getItem(position).get("end_time"))));

        return convertView;
    }

    private static class ViewHolder {
        TextView lecture_hall;
        TextView start_time;
        TextView end_time;
    }
    public static String convertTimeToAMPM(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;

        String format = (hours >= 12) ? "PM" : "AM";
        hours = (hours > 12) ? (hours - 12) : hours;
        hours = (hours == 0) ? 12 : hours;

        String timeString = String.format("%d:%02d %s", hours, minutes, format);
        return timeString;
    }
}
