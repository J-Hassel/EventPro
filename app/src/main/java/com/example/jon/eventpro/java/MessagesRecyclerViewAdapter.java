package com.example.jon.eventpro.java;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;

import java.util.ArrayList;

public class MessagesRecyclerViewAdapter// extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder>
{
//    private ArrayList<Event> listMessages = new ArrayList<>();
//    private Context context;
//
//    public MessagesRecyclerViewAdapter(ArrayList<Event> listMessages, Context mContext)
//    {
//        this.listMessages = listMessages;
//        this.context = mContext;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//    {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final MessagesRecyclerViewAdapter.ViewHolder viewHolder, int position)
//    {
//        viewHolder.image.setImageResource(listMessages.get(position).getImageID());
//        viewHolder.title.setText(listMessages.get(position).getTitle());
//        viewHolder.date.setText(listMessages.get(position).getDate());
//        viewHolder.location.setText(listMessages.get(position).getLocation());
//
//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                context.startActivity(new Intent(context, EventActivity.class));
//                Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return listMessages.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder
//    {
//
//        ImageView image;
//        TextView title, date, location;
//        LinearLayout parentLayout;
//
//        public ViewHolder(@NonNull View itemView)
//        {
//            super(itemView);
//            image = itemView.findViewById(R.id.event_item_image);
//            title = itemView.findViewById(R.id.event_item_title);
//            date = itemView.findViewById(R.id.event_item_date);
//            location = itemView.findViewById(R.id.event_item_location);
//            parentLayout = itemView.findViewById(R.id.parent_layout);
//        }
//    }
}
