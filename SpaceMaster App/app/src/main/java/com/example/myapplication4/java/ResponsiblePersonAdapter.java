package com.example.myapplication4.java;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication4.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class ResponsiblePersonAdapter extends RecyclerView.Adapter<ResponsiblePersonAdapter.ViewHolder> {
    private Context context;
    private List<HashMap<String, String>> responsiblePersonList;

    public ResponsiblePersonAdapter(Context context, List<HashMap<String, String>> responsiblePersonList) {
        this.context = context;
        this.responsiblePersonList = responsiblePersonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_responsible_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> responsiblePerson = responsiblePersonList.get(position);

        String name = responsiblePerson.get("name");

        holder.nameTextView.setText(name);

        // Set checkbox state
        holder.checkbox.setChecked(responsiblePersonList.get(position).get("checked").equals("true"));

        // Handle checkbox clicks
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the checked state in your data structure (e.g., responsiblePersonList)
            responsiblePersonList.get(position).put("checked", isChecked ? "true" : "false");

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String adminJson = gson.toJson(responsiblePersonList);
            // Save the adminJson to SharedPreferences
            editor.putString("adminList", adminJson);
            editor.apply();
        });
    }

    @Override
    public int getItemCount() {
        return responsiblePersonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
