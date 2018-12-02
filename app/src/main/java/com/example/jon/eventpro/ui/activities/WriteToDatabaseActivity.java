package com.example.jon.eventpro.ui.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jon.eventpro.models.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class WriteToDatabaseActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Events");
    StorageReference imageStorage = FirebaseStorage.getInstance().getReference();
    final DatabaseReference eventDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            ArrayList<Event> events = this.getIntent().getExtras().getParcelableArrayList("Events");
            for (int i = 0; i < events.size(); ++i) {
                System.out.println(events.get(i));
                uploadImage(events.get(i));
            }
            finish();
        } catch (NullPointerException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void uploadImage(final Event event) {
        final String pushID = eventDatabase.push().getKey();
        final StorageReference image_filepath = imageStorage.child("event_images").child(pushID + ".jpg");
        try {
            URL i_url = new URL(event.getImage());
            InputStream is = i_url.openStream();
            //uploading image to fire base
            UploadTask uploadTask = image_filepath.putStream(is);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            event.setImage(uri.toString());
                            //System.out.println("finish");
                            myRef.push().setValue(event);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}