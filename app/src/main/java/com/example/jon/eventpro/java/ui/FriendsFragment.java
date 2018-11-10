package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.jon.eventpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendsFragment extends Fragment
{
    private DatabaseReference messagingDB;
    private FirebaseAuth auth;


    public FriendsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        messagingDB = FirebaseDatabase.getInstance().getReference().child("Messaging");
        auth = FirebaseAuth.getInstance();

        ImageButton btnAddFriend = view.findViewById(R.id.button_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
            }
        });





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
