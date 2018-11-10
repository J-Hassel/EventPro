package com.example.jon.eventpro.java.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity
{
    private DatabaseReference rootReference;
    private FirebaseAuth auth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        rootReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

//TODO: VID 28
//        rootReference.child("Messaging").child(currentUid).addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.hasChild())
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        })
    }
}
