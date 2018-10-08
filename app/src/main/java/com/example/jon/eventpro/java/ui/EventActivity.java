package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Event;

public class EventActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private Event event;
    private FloatingActionButton btnDirections;
    private ImageButton btnGoing;
    private boolean isGoing = false;//TODO: get isGoing from user

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Initializing event
        //event = new Event(params, params, etc);


        btnGoing = findViewById(R.id.button_going);
        btnGoing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isGoing)
                {
                    btnGoing.setImageResource(R.drawable.ic_going);
                    isGoing = true;
                }
                else
                {
                    btnGoing.setImageResource(R.drawable.ic_not_going);
                    isGoing = false;
                }

            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });


        btnDirections = findViewById(R.id.fab_directions);
        btnDirections.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   //TODO: check if user has google maps installed
                String uri = "http://maps.google.co.in/maps?q=" + "459 W College Avenue, Tallahassee, FL 32301";//event.getAddress();
                EventActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });
    }
}
