package com.example.jon.eventpro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment
{
    private Button btnSettings;

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

        return view;
    }

}
