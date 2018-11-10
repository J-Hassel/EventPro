package com.example.jon.eventpro.java;

import com.example.jon.eventpro.R;

public class Event
{

    private int imageID;
    private String image, title, date, time, location, address, price, about;

    public Event()
    {

    }

    public Event(int imageID, String title, String date, String location)
    {
        this.imageID = imageID;
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public Event(String image, String title, String date, String time, String location, String address, String price, String about)
    {
        this.image = image;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.address = address;
        this.price = price;
        this.about = about;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }
}
