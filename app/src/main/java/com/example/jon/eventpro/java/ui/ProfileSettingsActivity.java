package com.example.jon.eventpro.java.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity
{

    public static final int PICK_IMAGE = 1;
    private DatabaseReference database;
    private FirebaseUser currentUser;
    private ProgressDialog saveDialog, uploadImageDialog;
    private EditText displayName, userLocation, userAbout;
    private CircleImageView btnSelectImage;
    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();

        database = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        imageStorage = FirebaseStorage.getInstance().getReference();


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

        btnSelectImage = findViewById(R.id.button_select_image);
        displayName = findViewById(R.id.et_name);
        userLocation = findViewById(R.id.et_location);
        userAbout = findViewById(R.id.et_about);


        database = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        database.addValueEventListener(new ValueEventListener()
        {   //populating editTexts with current data from database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String about = dataSnapshot.child("about").getValue().toString();

                Picasso.get().load(image).into(btnSelectImage);
                displayName.setText(name);
                userLocation.setText(location);
                userAbout.setText(about);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        ImageButton btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveDialog = new ProgressDialog(ProfileSettingsActivity.this);
                saveDialog.setTitle("Saving Changes");
                saveDialog.setMessage("Please wait while we update your profile!");
                saveDialog.setCanceledOnTouchOutside(false);
                saveDialog.show();

                String name = displayName.getText().toString();
                String location = userLocation.getText().toString();
                String about = userAbout.getText().toString();

                if(TextUtils.isEmpty(about))
                    about = "Nothing here yet!";

                database.child("name").setValue(name);
                database.child("location").setValue(location);
                database.child("about").setValue(about).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            saveDialog.dismiss();
                            finish();
                        }
                        else
                            Toast.makeText(ProfileSettingsActivity.this, "There was an error while saving your changes.", Toast.LENGTH_SHORT).show();
                    }
                });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            uploadImageDialog = new ProgressDialog(ProfileSettingsActivity.this);
            uploadImageDialog.setTitle("Uploading Image");
            uploadImageDialog.setMessage("Please wait while we upload your image.");
            uploadImageDialog.setCanceledOnTouchOutside(false);
            uploadImageDialog.show();

            Uri imageUri = data.getData();

            final StorageReference filepath = imageStorage.child("profile_images").child(currentUser.getUid() + ".jpg");
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            String downloadUrl = uri.toString();
                            database.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    uploadImageDialog.dismiss();
                                    if(!task.isSuccessful())
                                        Toast.makeText(ProfileSettingsActivity.this, "There was an error uploading your image. please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });


        }
    }
}
