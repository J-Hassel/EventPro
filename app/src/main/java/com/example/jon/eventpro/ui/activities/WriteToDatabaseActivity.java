package com.example.jon.eventpro.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jon.eventpro.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WriteToDatabaseActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ArrayList<Event> events = this.getIntent().getExtras().getParcelableArrayList("Events");
            for (int i = 0; i < events.size(); ++i) {
                System.out.println(events.get(i));
                myRef.push().setValue(events.get(i));
            }
            finish();
        } catch (NullPointerException e) {
            e.printStackTrace();
            finish();
        }
    }
}
