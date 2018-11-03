package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.jon.eventpro.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessagesFragment extends Fragment
{
    private DatabaseReference usrDB;

    public MessagesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
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


        usrDB = FirebaseDatabase.getInstance().getReference().child("Users");

        RecyclerView messageThreadList = view.findViewById(R.id.message_thread_list);
        messageThreadList.setLayoutManager(new LinearLayoutManager(getContext()));


//        Button btn = view.findViewById(R.id.test_button);
//        btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                startActivity(new Intent(getActivity(), MessagingActivity.class));
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }
}
