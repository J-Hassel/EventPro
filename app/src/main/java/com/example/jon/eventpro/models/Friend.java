package com.example.jon.eventpro.models;

public class Friend
{
    public String thumb_image, name, status, date_added;

    public Friend()
    {

    }

    public Friend(String status)
    {
        this.status = status;
    }

    public String getThumbImage()
    {
        return thumb_image;
    }

    public void setThumbImage(String thumb_image)
    {
        this.thumb_image = thumb_image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDate_added()
    {
        return date_added;
    }

    public void setDate_added(String date_added)
    {
        this.date_added = date_added;
    }
}
