package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Friend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragment extends Fragment
{
    private RecyclerView friendsList;
    private DatabaseReference friendsDatabase, usersDatabase;
    private FirebaseUser currentUser;


    public FriendsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUid);
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //for offline capabilities
        friendsDatabase.keepSynced(true);
        usersDatabase.keepSynced(true);

        //setting up recyclerView
        friendsList = view.findViewById(R.id.friends_list);
        friendsList.setLayoutManager(new LinearLayoutManager(getContext()));


        ImageButton btnAddFriend = view.findViewById(R.id.button_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Friend, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FriendsViewHolder>
                (
                        Friend.class,
                        R.layout.user_item,
                        FriendsViewHolder.class,
                        friendsDatabase
                )
        {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, final Friend model, int position)
            {
                final String friendID = getRef(position).getKey();
                usersDatabase.child(friendID).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        String friendImage = dataSnapshot.child("thumb_image").getValue().toString();
                        String friendName = dataSnapshot.child("name").getValue().toString();
                        String friendStatus = dataSnapshot.child("status").getValue().toString();

                        viewHolder.setImage(friendImage);
                        viewHolder.setName(friendName);
                        viewHolder.setStatus(friendStatus);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

                viewHolder.view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent friendIntent = new Intent(getContext(), MessagingActivity.class);
                        friendIntent.putExtra("friendID", friendID);
                        friendIntent.putExtra("friendName", viewHolder.getName());
                        startActivity(friendIntent);
                    }
                });
            }
        };

        friendsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        public FriendsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView;
        }

        public void setImage(String thumbImage)
        {
            CircleImageView friendImage = view.findViewById(R.id.user_image);
            Picasso.get().load(thumbImage).placeholder(R.drawable.default_profile_pic).into(friendImage);
        }

        public void setName(String name)
        {
            TextView friendName = view.findViewById(R.id.user_name);
            friendName.setText(name);
        }

        public void setStatus(String status)
        {
            TextView friendStatus = view.findViewById(R.id.user_status);
            friendStatus.setText(status);
        }

        public String getName()
        {
            TextView friendName = view.findViewById(R.id.user_name);
            return friendName.getText().toString();
        }
    }
}
