package com.example.jon.eventpro.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private CircleImageView profileImage;
    private TextView displayName, userLocation, userAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        ImageButton btnSettings = findViewById(R.id.button_settings);
        btnSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ProfileActivity.this, ProfileSettingsActivity.class));
            }
        });

        profileImage = findViewById(R.id.profile_image);
        displayName = findViewById(R.id.tv_name);
        userLocation = findViewById(R.id.tv_location);
        userAbout = findViewById(R.id.tv_about);

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        userDatabase.keepSynced(true);  //for offline capabilities
        userDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String about = dataSnapshot.child("about").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();


                if(!image.equals("default"))
                {
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_profile_image).into(profileImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError(Exception e)
                        {
                            Picasso.get().load(image).placeholder(R.drawable.default_profile_image).into(profileImage);
                        }
                    });
                }
                displayName.setText(name);
                userLocation.setText(location);
                userAbout.setText(about);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.profile_settings:
//                startActivity(new Intent(this, ProfileSettingsActivity.class));
//                return true;
//
//            case R.id.profile_sign_out:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
