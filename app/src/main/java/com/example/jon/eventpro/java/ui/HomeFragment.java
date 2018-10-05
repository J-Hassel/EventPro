package com.example.jon.eventpro.java.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;
import com.example.jon.eventpro.java.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Event> listEvent = new ArrayList<Event>();

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //setting up recyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(listEvent, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(adapter.getItemCount() == 0)
            initRecyclerView();



        return view;
    }

    private void initRecyclerView()
    {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        listEvent.add(new Event(R.drawable.testing, "EVENT 1 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 2 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 3 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 4 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 5 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 6 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 7 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 8 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
        listEvent.add(new Event(R.drawable.testing, "EVENT 9 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE"));
    }

}
