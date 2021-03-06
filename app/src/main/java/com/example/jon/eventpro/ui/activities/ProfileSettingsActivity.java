package com.example.jon.eventpro.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileSettingsActivity extends AppCompatActivity
{

    public static final int PICK_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private DatabaseReference usersDatabase;
    private FirebaseUser currentUser;
    private ProgressDialog uploadImageDialog;
    private EditText displayName, userLocation, userStatus, userAbout;
    private CircleImageView btnSelectImage;
    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        usersDatabase.keepSynced(true);     //for offline capabilities
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
        userStatus = findViewById(R.id.et_status);
        userAbout = findViewById(R.id.et_about);


        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        usersDatabase.addValueEventListener(new ValueEventListener()
        {   //populating editTexts with current data from database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String about = dataSnapshot.child("about").getValue().toString();

                Picasso.get().load(image).placeholder(R.drawable.default_profile_image).into(btnSelectImage);
                displayName.setText(name);
                userLocation.setText(location);
                userStatus.setText(status);
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

                String name = displayName.getText().toString();
                String location = userLocation.getText().toString();
                String status = userStatus.getText().toString();
                String about = userAbout.getText().toString();

                if(TextUtils.isEmpty(status))
                    status = "Nothing here yet!";

                if(TextUtils.isEmpty(about))
                    about = "Nothing here yet!";

                usersDatabase.child("name").setValue(name);
                usersDatabase.child("location").setValue(location);
                usersDatabase.child("status").setValue(status);
                usersDatabase.child("about").setValue(about).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileSettingsActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
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
            {   //will not allow you to upload a picture unless you grant READ_EXTERNAL_STORAGE permissions
                if (ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {   //if we do not have permissions, then ask for them. Then either open the gallery or do nothing according to whether or not permissions were granted.
                    ActivityCompat.requestPermissions(ProfileSettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    onRequestPermissionsResult(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new int[] {PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED});
                }
                else
                {   //we have permissions, so open the gallery
                    //opens the gallery app when btnSelectImage is clicked
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE);
                }
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

            final StorageReference image_filepath = imageStorage.child("profile_images").child(currentUser.getUid() + ".jpg");
            final StorageReference thumbnail_filepath = imageStorage.child("profile_images").child("thumbnails").child(currentUser.getUid() + ".jpg");

            //getting full-res image bitmap
            File imageFile = new File(getRealPathFromUri(this, imageUri));
            String imagePath = imageFile.getAbsolutePath();
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
            try
            {   //to make sure photo stays in the correct orientation
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();

                if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
                    matrix.postRotate(90);
                else if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                    matrix.postRotate(180);
                else if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                    matrix.postRotate(270);

                imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch(Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] image_data = baos.toByteArray();

            //getting thumbnail bitmap
            baos = new ByteArrayOutputStream();
            try
            {
                Bitmap thumbBitmap = new Compressor(this)
                        .setQuality(80)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .compressToBitmap(imageFile);
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            final byte[] thumb_data = baos.toByteArray();


            //uploading full-res image and thumbnail image to firebase
            UploadTask imageUploadTask = image_filepath.putBytes(image_data);
            imageUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    image_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            String imageDownloadUrl = uri.toString();
                            usersDatabase.child("image").setValue(imageDownloadUrl);

                            UploadTask thumbUploadTask = thumbnail_filepath.putBytes(thumb_data);
                            thumbUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    thumbnail_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            String thumbDownloadUrl = uri.toString();
                                            usersDatabase.child("thumb_image").setValue(thumbDownloadUrl);

                                            uploadImageDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Toast.makeText(ProfileSettingsActivity.this, "There was an error uploading your image. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //opens the gallery app when btnSelectImage is clicked
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE);
                }
                else
                {
                    //permission denied
                }
                return;
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try
        {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
    }
}
