package com.example.jon.eventpro.java.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;
import com.example.jon.eventpro.java.RecyclerViewAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    private DrawerLayout drawerLayout;
    private NavigationView navDrawer;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton btnCreate;
    private Toolbar toolbar;

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


        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        drawerLayout = view.findViewById(R.id.drawer_layout);
        navDrawer = view.findViewById(R.id.nav_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setupDrawerContent(navDrawer);





        btnCreate = view.findViewById(R.id.button_create_event);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
            }
        });

        //setting up recyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(listEvent, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(adapter.getItemCount() == 0)
            initRecyclerView();



        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action should open or close the drawer.
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
            case R.id.login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.information:
                startActivity(new Intent(getActivity(), InformationActivity.class));
        }

        //menu items will not be highlighted
        menuItem.setCheckable(false);

        drawerLayout.closeDrawers();
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
