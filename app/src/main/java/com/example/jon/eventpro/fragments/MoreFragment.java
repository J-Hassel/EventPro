package com.example.jon.eventpro.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jon.eventpro.activities.LoginRegisterActivity;
import com.example.jon.eventpro.R;
import com.example.jon.eventpro.activities.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment
{
    private Button btnSettings, btnAccount, btnLoginRegister;

    public MoreFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_more, container, false);

        btnSettings = view.findViewById(R.id.button_settings);
        btnSettings.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

//        btnAccount = view.findViewById(R.id.button_account);
//        btnAccount.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                startActivity(new Intent(getActivity(), AccountActivity.class));
//            }
//        });

        btnLoginRegister = view.findViewById(R.id.button_login_register);
        btnLoginRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
            }
        });

        return view;
    }

}
