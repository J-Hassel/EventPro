package com.example.jon.eventpro.ui.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback
{
    GoogleMap mGoogleMap;
    MapView mapView;

    public MapFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map_fragment);
        if(mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(getContext());

        try
        {//TODO: MAKE THIS EVENT SPECIFIC
            // Moving camera to location
            CameraPosition Location = CameraPosition.builder().target(getLocationFromAddress(getContext(),"459 W College Avenue, Tallahassee, FL 32301" )).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Location));

            // Marker set on address
            googleMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getContext(),"459 W College Avenue, Tallahassee, FL 32301" )));


        }
        catch(Exception e)
        {
            Toast.makeText(getContext(), "unable to load map", Toast.LENGTH_SHORT).show();
        }

        // Making it so user cannot interact with the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);

    }

    /* Reference: https://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address */
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng point = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            point = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return point;
    }
}
