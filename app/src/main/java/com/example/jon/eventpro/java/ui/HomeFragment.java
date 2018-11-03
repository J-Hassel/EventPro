package com.example.jon.eventpro.java.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;
import com.example.jon.eventpro.java.EventRecyclerViewAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment
{
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 200;

    private DrawerLayout drawerLayout;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Event> listEvent = new ArrayList<Event>();

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
        final List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

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
        RecyclerView recyclerView = view.findViewById(R.id.rv_home);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(listEvent, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(adapter.getItemCount() == 0)
            initRecyclerView();


        // Inflate the layout for this fragment
        return view;
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
                if(auth.getCurrentUser() == null)
                    signIn();
                else
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;

            case R.id.sign_in_sign_up:
                signIn();
                break;

//            case R.id.sign_out:
//                signOut();
//                break;

            case R.id.information:
                startActivity(new Intent(getActivity(), InformationActivity.class));
                break;

            //temporary
            case R.id.check_sign_in:
                if(auth.getCurrentUser() == null)
                    displayMessage("You are not currently signed in");
                else
                    displayMessage("You are signed in");
                break;
        }

        //menu items will not be highlighted
        menuItem.setCheckable(false);
    }


    private void signIn()
    {
        if(auth.getCurrentUser() == null)
        {   //you have to be signed out to access sign in page
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        }
        else
            displayMessage("already signed in");
    }

    private void signOut()
    {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                            displayMessage(getString(R.string.signout_success));
                        else
                            displayMessage(getString(R.string.signout_failed));
                    }
                });
    }

    // -------------------------------- authentication helpers -------------------------------- \\
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {   //displays a message base on the result of sign in
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
            {
                displayMessage(getString(R.string.signin_success));

            }
            if(resultCode == RESULT_CANCELED)
                displayMessage(getString(R.string.signin_failed));
            return;
        }
        displayMessage(getString(R.string.unknown));
    }

    private void displayMessage(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }



    // ------------------------------- initialize recycler view with events -------------------------------- \\
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
