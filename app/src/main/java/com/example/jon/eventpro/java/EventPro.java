package com.example.jon.eventpro.java;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class EventPro extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
