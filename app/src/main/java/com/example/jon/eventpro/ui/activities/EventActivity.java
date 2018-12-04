package com.example.jon.eventpro.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class EventActivity extends AppCompatActivity
{
    private ImageButton btnGoing;
//    private boolean isGoing = false;//TODO: get isGoing from user
    private TextView title, date, time, location, address, price, about, address2, lat, lon;
    private ImageView image;
    private DatabaseReference eventsDatabase;
    private String eventID;
    private GoogleMap googleMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //setting up views
        image = findViewById(R.id.iv_image);
        title = findViewById(R.id.tv_title);
        date = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);
        location = findViewById(R.id.tv_location);
        address = findViewById(R.id.tv_address);
        price = findViewById(R.id.tv_price);
        about = findViewById(R.id.tv_about);
        address2 = findViewById(R.id.tv_address2);
        lat = findViewById(R.id.tv_lat);
        lon = findViewById(R.id.tv_lon);



        eventID = getIntent().getStringExtra("eventID");
        eventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventID);
        eventsDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                try {
                    String eventImage = dataSnapshot.child("image").getValue().toString();
                    String eventTitle = dataSnapshot.child("title").getValue().toString();
                    String eventDate = dataSnapshot.child("date").getValue().toString();
                    String eventTime = dataSnapshot.child("time").getValue().toString();
                    String eventLocation = dataSnapshot.child("location").getValue().toString();
                    String eventAddress = dataSnapshot.child("address").getValue().toString();
                    String eventPrice = dataSnapshot.child("price").getValue().toString();
                    String eventAbout = dataSnapshot.child("about").getValue().toString();
                    String eventLat = dataSnapshot.child("lat").getValue().toString();
                    String eventLon = dataSnapshot.child("lon").getValue().toString();




                    Picasso.get().load(eventImage).placeholder(R.drawable.default_event_image).into(image);
                    title.setText(eventTitle);
                    date.setText(eventDate);
                    time.setText(eventTime);
                    location.setText(eventLocation);
                    address.setText(eventAddress);
                    price.setText(eventPrice);
                    about.setText(eventAbout);
                    address2.setText(eventAddress);
                    lat.setText(eventLat);
                    lon.setText(eventLon);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });



//        btnGoing = findViewById(R.id.button_going);
//        btnGoing.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(!isGoing)
//                {
//                    btnGoing.setImageResource(R.drawable.ic_going);
//                    isGoing = true;
//                }
//                else
//                {
//                    btnGoing.setImageResource(R.drawable.ic_not_going);
//                    isGoing = false;
//                }
//
//            }
//        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });


        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap gMap) {

                googleMap = gMap;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Float.parseFloat(lat.getText().toString()), Float.parseFloat(lon.getText().toString())), 14));  //move camera to location
                if (googleMap != null)
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(lat.getText().toString()), Float.parseFloat(lon.getText().toString()))));

                // Making it so user cannot interact with the map
                googleMap.getUiSettings().setAllGesturesEnabled(false);
            }
        });




//
//
//
//
//        FloatingActionButton btnDirections = findViewById(R.id.fab_directions);
//        btnDirections.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                String uri = "http://maps.google.co.in/maps?q=" + address2.getText().toString();
//                EventActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
//            }
//        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
