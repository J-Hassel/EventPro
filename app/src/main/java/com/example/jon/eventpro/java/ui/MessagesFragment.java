package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.jon.eventpro.R;

public class MessagesFragment extends Fragment
{
    public MessagesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        ImageButton btnComposeMsg = view.findViewById(R.id.button_compose_message);
        btnComposeMsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), PopupActivity.class));
            }
        });

        Button btn = view.findViewById(R.id.test_button);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), MessagingActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
