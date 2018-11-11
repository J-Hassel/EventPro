package com.example.jon.eventpro.java.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.Message;
import com.example.jon.eventpro.java.MessageRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity
{
    private DatabaseReference database;
    private Toolbar toolbar;
    private ImageButton btnSend;
    private EditText messageInput;
    private String currentUid, friendID, friendImage;
    private RecyclerView messagesList;

    private final List<Message> listMessage = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageRecyclerAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        database = FirebaseDatabase.getInstance().getReference();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendID = getIntent().getStringExtra("friendID");
        String friendName = getIntent().getStringExtra("friendName");

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
        toolbar.setTitle(friendName);

        messageInput = findViewById(R.id.message_input);
        btnSend = findViewById(R.id.button_send);

        //setting up recycler view
        messageAdapter = new MessageRecyclerAdapter(listMessage);
        messagesList = findViewById(R.id.messages_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setAdapter(messageAdapter);
        
        loadMessages();

        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage();
            }
        });
    }

    private void loadMessages()
    {
        database.child("Messages").child(currentUid).child(friendID).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Message message = dataSnapshot.getValue(Message.class);
                listMessage.add(message);

                //scrolls recycler view to bottom when a message is sent/received
                messageAdapter.notifyItemInserted(listMessage.size());
                messagesList.scrollToPosition(messageAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void sendMessage()
    {
        String message = messageInput.getText().toString();
        if(!TextUtils.isEmpty(message))
        {
            String currentUserRef = "Messages/" + currentUid + "/" + friendID;
            String friendUserRef = "Messages/" + friendID + "/" + currentUid;

            DatabaseReference userMessagePush = database.child("messages").child(currentUserRef).child(friendID).push();

            String pushID = userMessagePush.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", currentUid);


            Map messageUserMap = new HashMap();
            messageUserMap.put(currentUserRef + "/" + pushID, messageMap);
            messageUserMap.put(friendUserRef + "/" + pushID, messageMap);

            messageInput.setText("");


            database.updateChildren(messageUserMap, new DatabaseReference.CompletionListener()
            {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                {
                    if(databaseError != null)
                    {
                        Toast.makeText(MessagingActivity.this, "Database Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
