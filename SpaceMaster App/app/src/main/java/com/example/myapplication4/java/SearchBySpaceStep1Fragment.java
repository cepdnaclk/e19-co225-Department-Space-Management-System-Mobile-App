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
import java.util.List;


public class SearchBySpaceStep1Fragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchSpaceTimeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_by_space_step1, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> spaceNames = new ArrayList<>();
        spaceNames.add("1");
        spaceNames.add("2");
        spaceNames.add("3");
        spaceNames.add("4");
        spaceNames.add("5");
        spaceNames.add("6");
        spaceNames.add("7");
        adapter = new SearchSpaceTimeAdapter(spaceNames, new SearchSpaceTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                // Handle the item click event
                Fragment fragment = new SearchBySpaceStep2Fragment(item);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
