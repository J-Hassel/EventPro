package com.example.jon.eventpro.java;

public class Event
{
    private int imageID;
    private String title, date, time, location, address, price, about;

    public Event()
    {

    }

    public Event(int imageID, String about)
    {
        this.imageID = imageID;
        this.about = about;
    }

    public int getImageID()
    {
        return imageID;
    }

    public String getAbout()
    {
        return about;
    }

//    public String getAddress()
//    {
//        return address;
//    }
}
