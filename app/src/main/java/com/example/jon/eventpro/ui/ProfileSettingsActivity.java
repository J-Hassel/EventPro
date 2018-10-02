package com.example.jon.eventpro.ui;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jon.eventpro.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private CircleImageView btnSelectImage;

    public static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

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

        btnSelectImage = findViewById(R.id.button_select_image);
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //what to do with image
    }

//    public void processImage()
//    { //crops image so it is a square
//
//    }
}
