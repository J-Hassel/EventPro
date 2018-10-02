package com.example.jon.eventpro.java;

public class Event
{
    private int imageID;
    private String description;

    public Event()
    {

    }

    public Event(int imageID, String description)
    {
        this.imageID = imageID;
        this.description = description;
    }

    public int getImageID()
    {
        return imageID;
    }

    public String getDescription()
    {
        return description;
    }
}
