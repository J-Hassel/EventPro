package com.example.jon.eventpro.other;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    private List<Message> messageList;
    private FirebaseUser currentUser;
    private DatabaseReference userDatabase;

    public MessageRecyclerAdapter(List<Message> messageList)
    {
        this.messageList = messageList;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_SENDER:
                View viewSender = layoutInflater.inflate(R.layout.message_item_sender, parent, false);
                viewHolder = new SenderViewHolder(viewSender);
                break;
            case VIEW_TYPE_RECEIVER:
                View viewReceiver = layoutInflater.inflate(R.layout.message_item_receiver, parent, false);
                viewHolder = new ReceiverViewHolder(viewReceiver);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position)
    {
        Message c = messageList.get(position);
        String fromUser = c.getFrom();
        final String message = c.getMessage();

        String currentUid = currentUser.getUid();
        if(fromUser.equals(currentUid))
            configureSenderViewHolder((SenderViewHolder) viewHolder, position, message);
        else
        {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUser);
            userDatabase.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {   //to get image
                    String image = dataSnapshot.child("thumb_image").getValue().toString();
                    configureReceiverViewHolder((ReceiverViewHolder) viewHolder, position, message, image);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
//            configureReceiverViewHolder((ReceiverViewHolder) viewHolder, position, message);

        }
    }

    private void configureSenderViewHolder(SenderViewHolder viewHolder, int position, String message)
    {
        viewHolder.messageText.setText(message);
    }

    private void configureReceiverViewHolder(ReceiverViewHolder viewHolder, int position, String message, String image)
    {
        Picasso.get().load(image).placeholder(R.drawable.default_profile_image).into(viewHolder.friendImage);
        viewHolder.messageText.setText(message);
    }

    @Override
    public int getItemCount()
    {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(TextUtils.equals(messageList.get(position).getFrom(), FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return VIEW_TYPE_SENDER;
        else
            return VIEW_TYPE_RECEIVER;
    }

    private static class SenderViewHolder extends RecyclerView.ViewHolder
    {
        private TextView messageText;

        public SenderViewHolder(View itemView)
        {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }

    private static class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        private TextView messageText;
        private CircleImageView friendImage;

        public ReceiverViewHolder(View itemView)
        {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            friendImage =itemView.findViewById(R.id.message_profile_image);
        }
    }
}
