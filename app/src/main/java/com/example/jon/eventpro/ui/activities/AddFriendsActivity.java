package com.example.jon.eventpro.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendsActivity extends AppCompatActivity
{
    private RecyclerView usersList;
    private DatabaseReference usersDatabase;
    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

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

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDatabase.keepSynced(true);     //for offline capabilities

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersList = findViewById(R.id.users_list);
        usersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (
                        User.class,
                        R.layout.user_item,
                        UserViewHolder.class,
                        usersDatabase
                )
        {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position)
            {
                final String userID = getRef(position).getKey();

                viewHolder.setImage(model.getThumbImage());
                viewHolder.setDisplayName(model.getName());
                viewHolder.setStatus(model.getAbout());

                if(userID.equals(currentUid))
                {   //bypasses the onclick listener, so you cannot add yourself
                    viewHolder.setDisplayName("Me");
                    return;
                }

                viewHolder.view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent userIntent = new Intent(AddFriendsActivity.this, UserActivity.class);
                        userIntent.putExtra("userID", userID);
                        startActivity(userIntent);
                    }
                });
            }
        };

        usersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        View view;

        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);

            view = itemView;
        }

        public void setImage(String image)
        {
            CircleImageView userImage = view.findViewById(R.id.user_image);
            Picasso.get().load(image).placeholder(R.drawable.default_profile_image).into(userImage);
        }

        public void setDisplayName(String name)
        {
            TextView displayName = view.findViewById(R.id.user_name);
            displayName.setText(name);
        }

        public void setStatus(String status)
        {
            TextView userStatus = view.findViewById(R.id.user_status);
            userStatus.setText(status);
        }
    }
}
