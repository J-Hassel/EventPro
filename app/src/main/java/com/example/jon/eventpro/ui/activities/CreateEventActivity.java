package com.example.jon.eventpro.ui.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jon.eventpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;


public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    private int day, month, year, hour, minute;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;
    private EditText etTitle, etDateTime, etLocation, etAddress, etCost, etAbout;
    private ImageButton btnAddImage;
    private DatabaseReference eventDatabase;
    private StorageReference imageStorage;
    private String pushID, imageDownloadUrl;
    private ProgressDialog uploadImageDialog;


    public static final int PICK_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        eventDatabase.keepSynced(true);
        imageStorage = FirebaseStorage.getInstance().getReference();
        imageDownloadUrl = "default";
        pushID = eventDatabase.push().getKey();

        // setting up edit texts
        btnAddImage = findViewById(R.id.button_add_image);
        etTitle = findViewById(R.id.et_title);
        etDateTime = findViewById(R.id.et_date);
        etLocation = findViewById(R.id.et_location);
        etAddress = findViewById(R.id.et_address);
        etCost = findViewById(R.id.et_cost);
        etAbout = findViewById(R.id.et_about);

        //getting the current date and time
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        minute = 0;

        etDateTime.setText(convertDate(month, day, year) + " at " + convertTime(hour, minute));

        //setting up toolbar with back arrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!imageDownloadUrl.equals("default"))
                    imageStorage.child("event_images").child(pushID + ".jpg").delete();
                onBackPressed();
            }
        });

        ImageButton btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String image, title, date, time, location, address, cost, about;
                image = imageDownloadUrl;
                title = etTitle.getText().toString();
                date = getDate(etDateTime.getText().toString());
                time = getTime(etDateTime.getText().toString());
                location = etLocation.getText().toString();
                address = etAddress.getText().toString();
                cost = etCost.getText().toString();
                about = etAbout.getText().toString();

                if(title.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty() || address.isEmpty() || cost.isEmpty() || about.isEmpty())
                    Toast.makeText(CreateEventActivity.this, "You must fill out all forms.", Toast.LENGTH_LONG).show();
                else
                {   //add the event to the database
                    Map eventMap = new HashMap();

                    eventMap.put("image", image);
                    eventMap.put("title", title);
                    eventMap.put("date", date);
                    eventMap.put("time", time);
                    eventMap.put("location", location);
                    eventMap.put("address", address);
                    eventMap.put("cost", cost);
                    eventMap.put("about", about);

                    eventDatabase.child(pushID).setValue(eventMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(CreateEventActivity.this, "Your event has been created!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                Toast.makeText(CreateEventActivity.this, "There was an error creating your event. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   //will not allow you to upload a picture unless you grant READ_EXTERNAL_STORAGE permissions
                if (ContextCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {   //if we do not have permissions, then ask for them. Then either open the gallery or do nothing according to whether or not permissions were granted.
                    ActivityCompat.requestPermissions(CreateEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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

        //adding tint to btnAddImage
        int color = getResources().getColor(R.color.gray);
        ColorFilter cf = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        btnAddImage.setColorFilter(cf);

        etDateTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, CreateEventActivity.this, year, month - 1, day);
                datePickerDialog.show();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            uploadImageDialog = new ProgressDialog(CreateEventActivity.this);
            uploadImageDialog.setTitle("Uploading Image");
            uploadImageDialog.setMessage("Please wait while we upload your image.");
            uploadImageDialog.setCanceledOnTouchOutside(false);
            uploadImageDialog.show();

            Uri imageUri = data.getData();

            final StorageReference image_filepath = imageStorage.child("event_images").child(pushID + ".jpg");

            //getting image bitmap
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


            //uploading image to firebase
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
                            imageDownloadUrl = uri.toString();
                            Picasso.get().load(imageDownloadUrl).placeholder(R.drawable.default_event_image).into(btnAddImage);
                            uploadImageDialog.dismiss();
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



    private String getDate(String s)
    {
        return s.split(" at ")[0];
    }

    private String getTime(String s)
    {
        return s.split(" at ")[1];
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        selectedYear = year;
        selectedMonth = month + 1;
        selectedDay = dayOfMonth;


        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this, CreateEventActivity.this, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        selectedHour = hourOfDay;
        selectedMinute = minute;

        etDateTime.setText(convertDate(selectedMonth, selectedDay, selectedYear) + " at " + convertTime(selectedHour, selectedMinute));
    }

    public String convertDate(int month, int day, int year)
    {
        String inputDate = month + "/" + day + "/" + year;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        Date date = null;
        try
        {
            date = sdf.parse(inputDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        sdf.applyPattern("EEEE, MMMM d, yyyy");

        return sdf.format(date);
    }

    public String convertTime(int hour, int minute)
    {   //converts 24hr time to 12hr time
        String middle = ":", amPM = " AM";

        if(hour == 0)           // 00:00 == 12:00 AM
            hour = 12;
        else if(hour == 12)     // 12:00 == 12:00 PM
            amPM = " PM";

        if(hour > 12)
        {   //for PM
            hour -= 12;
            amPM = " PM";
        }

        if(minute < 10)
            middle = ":0";

        return Integer.toString(hour) + middle + Integer.toString(minute) + amPM;
    }
}
