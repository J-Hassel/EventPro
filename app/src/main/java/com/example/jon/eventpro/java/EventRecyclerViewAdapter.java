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

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.ui.EventActivity;

import java.util.ArrayList;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>
{
    private ArrayList<Event> listEvent = new ArrayList<>();
    private Context context;

    public EventRecyclerViewAdapter(ArrayList<Event> listEvent, Context mContext)
    {
        this.listEvent = listEvent;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position)
    {
        viewHolder.image.setImageResource(listEvent.get(position).getImageID());
        viewHolder.title.setText(listEvent.get(position).getTitle());
        viewHolder.date.setText(listEvent.get(position).getDate());
        viewHolder.location.setText(listEvent.get(position).getLocation());
        
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(context, EventActivity.class));
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return listEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView title, date, location;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.event_item_image);
            title = itemView.findViewById(R.id.event_item_title);
            date = itemView.findViewById(R.id.event_item_date);
            location = itemView.findViewById(R.id.event_item_location);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
