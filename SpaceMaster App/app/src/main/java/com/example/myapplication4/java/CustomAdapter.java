package com.example.myapplication4.java;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication4.R;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private List<Map<String, Object>> items;
    private LayoutInflater inflater;
    public void removeItem(int position) {
        FirebaseHandler.fireBaseRemove((String) items.get(position).get("key"), inflater.getContext());
        items.remove(position);
        notifyItemRemoved(position);
    }

    public CustomAdapter(Context context, List<Map<String, Object>> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.lecture_hall.setText((String)items.get(position).get("lecture_hall"));
        holder.start_time.setText(convertTimeToAMPM(Integer.valueOf((String) items.get(position).get("start_time"))));
        holder.end_time.setText(convertTimeToAMPM(Integer.valueOf((String) items.get(position).get("end_time"))));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lecture_hall;
        TextView start_time;
        TextView end_time;

        ViewHolder(View itemView) {
            super(itemView);
            lecture_hall = itemView.findViewById(R.id.text_view_lecture_hall);
            start_time=itemView.findViewById(R.id.text_view_start_time);
            end_time=itemView.findViewById(R.id.text_view_end_time);

        }
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
