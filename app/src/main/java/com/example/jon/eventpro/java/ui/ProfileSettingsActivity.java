package com.example.jon.eventpro.java.ui;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.maps.model.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity
{
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);


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

        ImageButton btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        CircleImageView btnSelectImage = findViewById(R.id.button_select_image);
        btnSelectImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   //opens the gallery app when btnSelectImage is clicked
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        //adding tint to btnSelectImage
        int color = getResources().getColor(R.color.gray);
        ColorFilter cf = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        btnSelectImage.setColorFilter(cf);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //what to do with image
    }
}
