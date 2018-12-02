package com.example.jon.eventpro.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.models.Event;
import com.example.jon.eventpro.ui.activities.CurlActivity;
import com.example.jon.eventpro.ui.activities.EventActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class SearchFragment extends Fragment
{

    private SearchView searchBar;
    private RecyclerView eventsList;
    private DatabaseReference eventsDatabase;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SearchFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = view.findViewById(R.id.search_bar);

        eventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        eventsDatabase.keepSynced(true);    //for offline capabilities

        //setting up recyclerView
        eventsList = view.findViewById(R.id.events_list);
        eventsList.setLayoutManager(new LinearLayoutManager(getContext()));

        //refreshing the recycler view on pull down
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //deleting all events from database
                eventsDatabase.removeValue();

                startActivity(new Intent(getActivity(), CurlActivity.class));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 10000);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        Query query = eventsDatabase.orderByChild("dateTime");
        FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder>
                (
                        Event.class,
                        R.layout.event_item,
                        HomeFragment.EventViewHolder.class,
                        query
                )
        {
            @Override
            protected void populateViewHolder(HomeFragment.EventViewHolder viewHolder, Event model, int position)
            {

                viewHolder.setImage(model.getImage());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDate(model.getDate());
                viewHolder.setLocation(model.getLocation());

                final String eventID = getRef(position).getKey();

                viewHolder.view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {   //passing eventID to EventActivity to reference it
                        Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                        eventIntent.putExtra("eventID", eventID);
                        startActivity(eventIntent);
                    }
                });
            }
        };

        eventsList.setAdapter(firebaseRecyclerAdapter);
    }
        //EventViewHolder class is in HomeFragment
}
