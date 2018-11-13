package com.example.jon.eventpro.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jon.eventpro.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreateEventActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    private int day, month, year, hour, minute;
    private int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;
    private EditText etDisplayDate;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //getting the current date and time
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        minute = 0;

        etDisplayDate = findViewById(R.id.et_date);
        etDisplayDate.setText(convertDate(month, day, year) + " at " + convertTime(hour, minute));

        //setting up toolbar with back arrow
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


        ImageView btnAddImage = findViewById(R.id.button_add_image);
        btnAddImage.setOnClickListener(new View.OnClickListener()
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
        btnAddImage.setColorFilter(cf);

        etDisplayDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, CreateEventActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });
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

        etDisplayDate.setText(convertDate(selectedMonth, selectedDay, selectedYear) + " at " + convertTime(selectedHour, selectedMinute));
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
        sdf.applyPattern("EEEE, MMMM d");

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

//                if(minute < 10)
//        return Integer.toString(hour) + ":0" + Integer.toString(minute) + " PM";
//            return Integer.toString(hour) + ":" + Integer.toString(minute) + " PM";
}
