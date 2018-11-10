package com.example.jon.eventpro.java.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchFragment extends Fragment
{

    private SearchView searchBar;
    private RecyclerView eventsList;
    private DatabaseReference eventsDatabase;

    public SearchFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        eventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        searchBar = view.findViewById(R.id.search_bar);


        //setting up recyclerView
        eventsList = view.findViewById(R.id.events_list);
        eventsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder>
                (
                        Event.class,
                        R.layout.event_item,
                        HomeFragment.EventViewHolder.class,
                        eventsDatabase
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


    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        View view;

        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);

            view = itemView;
        }


        public void setImage(String image)
        {
            ImageView eventImage = view.findViewById(R.id.event_image);
            Picasso.get().load(image).placeholder(R.drawable.testing).into(eventImage);
        }

        public void setTitle(String title)
        {
            TextView eventTitle = view.findViewById(R.id.event_title);
            eventTitle.setText(title);
        }

        public void setDate(String date)
        {
            TextView eventDate = view.findViewById(R.id.event_date);
            eventDate.setText(date);
        }

        public void setLocation(String location)
        {
            TextView eventLocation = view.findViewById(R.id.event_location);
            eventLocation.setText(location);
        }
    }
}
