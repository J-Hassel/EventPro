package com.example.jon.eventpro.java.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;
import com.example.jon.eventpro.java.EventRecyclerViewAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment
{

    private SearchView searchBar;
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private ArrayList<Event> listEvent = new ArrayList<Event>();


    public SearchFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = view.findViewById(R.id.search_bar);


        //setting up recyclerView
        recyclerView = view.findViewById(R.id.rv_search);
        adapter = new EventRecyclerViewAdapter(listEvent, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(adapter.getItemCount() == 0)
            initRecyclerView();

        return view;
    }


    private void initRecyclerView()
    {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        listEvent.add(new Event(R.drawable.testing2, "EVENT 1 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "JAN 13", "Boston"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 2 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "OCT 5", "New York"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 3 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "APR 5", "Orange County"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 4 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "MAR 29", "Charleston"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 5 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "FEB 15", "Tallahassee"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 6 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "JUN 5", "Paris"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 7 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "JUL 9", "Tampa"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 8 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "MAR 5", "Salt Lake City"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 9 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "MAY 5", "Jupiter"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 10 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "OCT 25", "Miami"));
        listEvent.add(new Event(R.drawable.testing2, "EVENT 11 TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE TITLE", "NOV 5", "Palm Beach"));
    }
}
