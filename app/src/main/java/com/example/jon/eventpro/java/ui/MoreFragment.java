package com.example.jon.eventpro.java.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jon.eventpro.R;



public class MoreFragment extends Fragment
{
    private Button btnProfile, btnLogin, btnMap, btnInformation, btnSettings;

    public MoreFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_more, container, false);


        btnProfile = view.findViewById(R.id.button_profile);
        btnProfile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {   //TODO: check is user is logged in; if not launch login activity
//                if(user.isLoggedIn())
//                {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
//                }
//                else
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        btnLogin = view.findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

//        btnMap = view.findViewById(R.id.button_map);
//        btnMap.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                startActivity(new Intent(getActivity(), MapActivity.class));
//            }
//        });

        btnInformation = view.findViewById(R.id.button_information);
        btnInformation.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), InformationActivity.class));
            }
        });

        return view;
    }

}
