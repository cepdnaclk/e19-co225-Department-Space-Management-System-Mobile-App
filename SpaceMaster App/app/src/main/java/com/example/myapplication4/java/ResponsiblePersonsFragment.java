package com.example.myapplication4.java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication4.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsiblePersonsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ResponsiblePersonAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_responsible_persons, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<HashMap<String,String>> adminDetails =FirebaseHandler.readAdminDetails(getContext());
        adapter = new ResponsiblePersonAdapter(getContext(),adminDetails);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}