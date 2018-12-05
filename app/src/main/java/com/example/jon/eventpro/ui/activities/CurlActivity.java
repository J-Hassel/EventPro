package com.example.jon.eventpro.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.Date;

public class CurlActivity extends AppCompatActivity {

    private ArrayList<Event> event_arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Start Curl activity");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String currentDate = Calendar.getInstance().getTime().toString();
        String startDate = currentDate.split(" ")[5] + '-' + convertMonth(currentDate.split(" ")[1]) + '-' + currentDate.split(" ")[2] + 'T' + currentDate.split(" ")[3] + 'Z';
        String endDate = addMonth(startDate);
        String request = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=US&startDateTime=" + startDate + "&endDateTime=" + endDate + "&apikey=6xvARjg2m6rNWKqYIk9v5h7M3VAZXp8s";
        getRequest(request);
        Log.d("request_string", request);
    }

    private String convertMonth(String s)
    {
        switch(s)
        {
            case "Jan": return "01";
            case "Feb": return "02";
            case "Mar": return "03";
            case "Apr": return "04";
            case "May": return "05";
            case "Jun": return "06";
            case "Jul": return "07";
            case "Aug": return "08";
            case "Sep": return "09";
            case "Oct": return "10";
            case "Nov": return "11";
            case "Dec": return "12";
        }
        return "01";
    }

    private String addMonth(String startDate)
    {
        String newDate;
        int year = Integer.parseInt(startDate.split("-")[0]);
        int month = Integer.parseInt(startDate.split("-")[1]);
        int day = Integer.parseInt(startDate.split("-")[2].split("T")[0]);

        if(month == 12)
            newDate = Integer.toString(year + 1) + "-01-" + convert2Digit((day + 28) % 31) + 'T' + startDate.split("T")[1];
        else
            newDate = Integer.toString(year) + '-' + convert2Digit(month + 1) + '-' + convert2Digit((day + 28) % 31) + 'T' + startDate.split("T")[1];

        return newDate;
    }

    public String convert2Digit(int num)
    {
        String temp = Integer.toString(num);
        if(temp.length() == 1)
            return "0" + temp;
        return temp;
    }

    private void getRequest (String str)
    {
        String result, name, date_time, start_date, start_time, lat, lon, info, min_price, max_price, price_range, location, address, website;
        start_time = info = price_range = null;
        try {
            URL url = new URL(str);
            InputStreamReader is = new InputStreamReader(url.openStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(is, 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
                sb.append(line + "\n");

            result = sb.toString();
            JSONObject jo = new JSONObject(result);
            JSONObject embedded = jo.getJSONObject("_embedded");
            JSONArray events = embedded.getJSONArray("events");

            for(int i = 0; i < events.length(); ++i)
            {
                JSONObject temp = events.getJSONObject(i);
                name = temp.getString("name");

                website = temp.getString("url");

                JSONObject date = temp.getJSONObject("dates").getJSONObject("start");

                start_date = convertDateFormat(date.getString("localDate"));

                start_time = convertTime("12:00:00");
                if (date.has("localTime"))
                    start_time = convertTime(date.getString("localTime"));

                date_time = date.getString("localDate");
                if(date.has("dateTime"))
                    date_time = date.getString("dateTime");


                JSONArray venues = temp.getJSONObject("_embedded").getJSONArray("venues");

                location = venues.getJSONObject(0).getString("name");

                address = venues.getJSONObject(0).getJSONObject("address").getString("line1");
                if (venues.getJSONObject(0).getJSONObject("address").has("line2"))
                    address = address + ", " + venues.getJSONObject(0).getJSONObject("address").getString("line2");
                else if (venues.getJSONObject(0).getJSONObject("city").has("name")
                        && venues.getJSONObject(0).getJSONObject("state").has("name")
                        && venues.getJSONObject(0).has("postalCode"))
                {
                    address = address + ", " + venues.getJSONObject(0).getJSONObject("city").getString("name")
                            + ", " + venues.getJSONObject(0).getJSONObject("state").getString("stateCode")
                            + " " + venues.getJSONObject(0).getString("postalCode");
                }



                lat = venues.getJSONObject(0).getJSONObject("location").getString("latitude");
                lon = venues.getJSONObject(0).getJSONObject("location").getString("longitude");

                if (temp.has("info"))
                    info = temp.getString("info");
                else
                    info = "No description was provided.";

                if (temp.has("priceRanges"))
                {
                    JSONArray price = temp.getJSONArray("priceRanges");
                    min_price = Integer.toString(price.getJSONObject(0).getInt("min"));
                    max_price = Integer.toString(price.getJSONObject(0).getInt("max"));
                    price_range = min_price + '-' + max_price;
                }
                else
                    price_range = "No price information was provided.";

                int index = 0;
                JSONArray images = temp.getJSONArray("images");
                for (int j = 0; j < images.length(); ++j)
                {
                    int width = images.getJSONObject(j).getInt("width");
                    if (width == 1024)
                    {
                        index = j;
                        break;
                    }
                }
                String image_url = images.getJSONObject(index).getString("url");

                String image  = image_url;
                event_arr.add(new Event(image, name, date_time, start_date, start_time, location, address, lat, lon, price_range, info, website));
            }
            System.out.println("Start write to database");
            Intent intent = new Intent(CurlActivity.this, WriteToDatabaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Events", event_arr);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);

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
        try
        {
            Date d = sdf.parse(date);
            sdf.applyPattern(new_format);
            return sdf.format(d);
        }
        catch (ParseException e)
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
        try
        {
            Date d = sdf.parse(time);
            sdf.applyPattern(new_format);
            return sdf.format(d);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
