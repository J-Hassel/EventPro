package com.example.jon.eventpro.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jon.eventpro.models.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurlActivity extends AppCompatActivity {

    private ArrayList<Event> event_arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Start Curl activity");
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getRequest("https://app.ticketmaster.com/discovery/v2/events.json?apikey=6xvARjg2m6rNWKqYIk9v5h7M3VAZXp8s");
    }

    private void getRequest (String str)
    {
        String result, name, start_date, start_time, lat, lon, info, min_price, max_price, price_range, location;
        start_time = info = price_range = null;

        try {
            URL url = new URL(str);
            InputStreamReader is = new InputStreamReader(url.openStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(is, 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            result = sb.toString();
            JSONObject jo = new JSONObject(result);
            JSONObject embedded = jo.getJSONObject("_embedded");
            JSONArray events = embedded.getJSONArray("events");
            for (int i = 0; i < events.length(); ++i)
            {
                JSONObject temp = events.getJSONObject(i);
                name = temp.getString("name");
                //System.out.println(name);
                JSONObject date = temp.getJSONObject("dates").getJSONObject("start");
                start_date = convertDateFormat(date.getString("localDate"));
                //System.out.println(start_date);
                if (date.has("localTime")) {
                    start_time = convertTime(date.getString("localTime"));
                    //System.out.println(start_time);
                }
                JSONArray venues = temp.getJSONObject("_embedded").getJSONArray("venues");
                location = venues.getJSONObject(0).getString("name");
               //System.out.println(location);
                lat = venues.getJSONObject(0).getJSONObject("location").getString("latitude");
                lon = venues.getJSONObject(0).getJSONObject("location").getString("longitude");
                //System.out.println(lat);
                //System.out.println(lon);
                if (temp.has("info")) {
                    info = temp.getString("info");
                    //System.out.println(info);
                }
                if (temp.has("priceRanges")) {
                    JSONArray price = temp.getJSONArray("priceRanges");
                    min_price = Integer.toString(price.getJSONObject(0).getInt("min"));
                    max_price = Integer.toString(price.getJSONObject(0).getInt("max"));
                    price_range = min_price + '-' + max_price;
                    //System.out.println(price_range);
                }
                String image = "gs://plucky-shore-218700.appspot.com/event_images/testing2.jpg";
                event_arr.add(new
                        Event(image, name, start_date, start_time, location, lat, lon, price_range, info));
            }
           // System.out.println(event_arr.size());
            System.out.println("Start write to database");
            Intent intent = new Intent(CurlActivity.this, WriteToDatabaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Events", event_arr);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        catch (Exception e)
        {
            System.out.println(e.getClass().getSimpleName());
        }

    }

    private String convertDateFormat(String date)
    {
        String old_format = "yyyy-MM-dd";
        String new_format = "EEEE, MMMM d, yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(old_format);
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern(new_format);
            return sdf.format(d);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String convertTime(String time)
    {
        String old_format = "HH:mm:ss";
        String new_format = "hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(old_format);
        try {
            Date d = sdf.parse(time);
            sdf.applyPattern(new_format);
            return sdf.format(d);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
