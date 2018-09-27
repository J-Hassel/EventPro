package com.example.jon.eventpro.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jon.eventpro.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    FrameLayout event1, event2, event3;

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
        event1 = view.findViewById(R.id.frame1);
        event1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Event 1 clicked", Toast.LENGTH_SHORT).show();
            }
        });

        event2 = view.findViewById(R.id.frame2);
        event2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Event 2 clicked", Toast.LENGTH_SHORT).show();
            }
        });

        event3 = view.findViewById(R.id.frame3);
        event3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Event 3 clicked", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

}
