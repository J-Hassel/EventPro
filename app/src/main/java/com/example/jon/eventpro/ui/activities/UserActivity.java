package com.example.jon.eventpro.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity
{

    private TextView name, location, status;
    private CircleImageView image;
    private DatabaseReference friendsDatabase;
    private String currentUid, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendsDatabase.keepSynced(true);   //for offline capabilities

        image = findViewById(R.id.profile_image);
        name = findViewById(R.id.tv_name);
        location = findViewById(R.id.tv_location);
        status = findViewById(R.id.tv_status);

        userID = getIntent().getStringExtra("userID");
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        usersDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String userImage = dataSnapshot.child("image").getValue().toString();
                String userName = dataSnapshot.child("name").getValue().toString();
                String userLocation = dataSnapshot.child("location").getValue().toString();
                String userStatus = dataSnapshot.child("status").getValue().toString();


                Picasso.get().load(userImage).placeholder(R.drawable.default_profile_image).into(image);
                name.setText(userName);
                location.setText(userLocation);
                status.setText(userStatus);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        Button btnAddFriend = findViewById(R.id.button_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                friendsDatabase.child(currentUid).child(userID).child("date_added").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        friendsDatabase.child(userID).child(currentUid).child("date_added").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(UserActivity.this, "Added to friends list!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
