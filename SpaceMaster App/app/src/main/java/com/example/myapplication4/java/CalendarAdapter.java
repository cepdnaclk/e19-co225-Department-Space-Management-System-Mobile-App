package com.example.myapplication4.java;
import static android.media.CamcorderProfile.get;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication4.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{
    private List<Map<String, Object>> items;
    private LayoutInflater inflater;
    private Context context;
    public void removeItem(int position) {
        FirebaseHandler.fireBaseRemove((String) items.get(position).get("lecture_hall"),(String) items.get(position).get("date"),(String) items.get(position).get("key"), inflater.getContext());
        items.remove(position);
        notifyItemRemoved(position);
    }

    public CalendarAdapter(Context context, List<Map<String, Object>> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);
        this.context=context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item_calendar, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       List< HashMap<String, String>> responsiblePersonList=FirebaseHandler.readAdminDetails(context);
        String desiredUid=" ";
        try {
            desiredUid = ((List<String>) items.get(position).get("uidresponsibles")).get(0);
        }catch (Exception e){
            desiredUid=" ";
        }
        String responsibleName=" ";
        for (HashMap<String, String> responsiblePerson : responsiblePersonList) {
            if (responsiblePerson.containsKey("uid") && responsiblePerson.get("uid").equals(desiredUid)) {
                // Found the matching UID
                // You can access other values in the HashMap or perform additional actions here
                responsibleName = responsiblePerson.get("name");
                break;
            }
        }
        if(responsibleName.equals(" ")){
            holder.remove.setText(" ");
        }

        holder.responsible.setText(responsibleName);
        holder.lecture_hall.setText(FirebaseHandler.lectureHallNamingConversion((Integer.parseInt(((String)items.get(position).get("lecture_hall"))))) );
        holder.user.setText((String)items.get(position).get("user"));
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
        TextView user;
        TextView responsible;
        TextView remove;

        ViewHolder(View itemView) {
            super(itemView);
            lecture_hall = itemView.findViewById(R.id.text_view_lecture_hall);
            start_time=itemView.findViewById(R.id.text_view_start_time);
            end_time=itemView.findViewById(R.id.text_view_end_time);
            user=itemView.findViewById(R.id.text_view_user);
            responsible=itemView.findViewById(R.id.text_view_responsible);
            remove =itemView.findViewById(R.id.text_view_responsible_remove);

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
