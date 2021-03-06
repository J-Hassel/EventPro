package com.example.jon.eventpro.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.models.Event;
import com.example.jon.eventpro.ui.activities.CreateEventActivity;
import com.example.jon.eventpro.ui.activities.CurlActivity;
import com.example.jon.eventpro.ui.activities.EventActivity;
import com.example.jon.eventpro.ui.activities.InformationActivity;
import com.example.jon.eventpro.ui.activities.LoginActivity;
import com.example.jon.eventpro.ui.activities.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment
{
    private FirebaseAuth auth;
    private RecyclerView eventsList;
    private DatabaseReference eventsDatabase;
    private StorageReference eventImageStorage;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DrawerLayout drawerLayout;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //user authentication
        auth = FirebaseAuth.getInstance();
        eventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        eventsDatabase.keepSynced(true);    //for offline capabilities
        eventImageStorage = FirebaseStorage.getInstance().getReference().child("event_images");

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navDrawer = view.findViewById(R.id.nav_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setupDrawerContent(navDrawer);

        ImageButton btnCreate = view.findViewById(R.id.button_create_event);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
            }
        });

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

                //let the refresh symbol stay for 6 seconds
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefreshLayout.setRefreshing(false);
                        eventsList.smoothScrollToPosition(0);

                    }
                }, 12000);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser == null)
            startActivity(new Intent(getActivity(), LoginActivity.class));

        Query query = eventsDatabase.orderByChild("dateTime");
        FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, HomeFragment.EventViewHolder>
                (
                        Event.class,
                        R.layout.event_item,
                        EventViewHolder.class,
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



    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        View view;

        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);

            view = itemView;
        }
        

        public void setImage(final String image)
        {
            final ImageView eventImage = view.findViewById(R.id.event_image);

            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_event_image).into(eventImage, new Callback()
            {
                @Override
                public void onSuccess()
                {

                }

                @Override
                public void onError(Exception e)
                {
                    Picasso.get().load(image).placeholder(R.drawable.default_event_image).into(eventImage);
                }
            });
        }

        public void setTitle(String title)
        {
            TextView eventTitle = view.findViewById(R.id.event_title);
            eventTitle.setText(title);
        }

        public void setDate(String date)
        {
            TextView eventDate = view.findViewById(R.id.event_date);
            eventDate.setText(convertDateFormat(date));
        }

        public void setLocation(String location)
        {
            TextView eventLocation = view.findViewById(R.id.event_location);
            eventLocation.setText(location);
        }

        private String convertDateFormat(String date)
        {
            String new_format = "MMM\ndd";
            String old_format = "EEEE, MMMM d, yyyy";

            SimpleDateFormat sdf = new SimpleDateFormat(old_format);
            try {
                Date d = sdf.parse(date);
                sdf.applyPattern(new_format);
                return sdf.format(d);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    

    // -------------------------------- navigation drawer functions -------------------------------- \\
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action(menu icon) should open or close the drawer
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;

            case R.id.information:
                startActivity(new Intent(getActivity(), InformationActivity.class));
                break;

            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }

        //menu items will not be highlighted
        menuItem.setCheckable(false);
    }
}
