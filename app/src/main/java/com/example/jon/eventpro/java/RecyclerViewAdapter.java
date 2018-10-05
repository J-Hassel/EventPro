package com.example.jon.eventpro.java;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.example.jon.eventpro.java.ui.EventActivity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Event> listEvent = new ArrayList<Event>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Event> listEvent, Context mContext)
    {
        this.listEvent = listEvent;
        this.mContext = mContext;
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
        viewHolder.text.setText(listEvent.get(position).getDescription());
        
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mContext.startActivity(new Intent(mContext, EventActivity.class));
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
        TextView text;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
