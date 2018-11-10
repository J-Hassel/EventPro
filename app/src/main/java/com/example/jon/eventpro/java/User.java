package com.example.jon.eventpro.java;

import java.util.ArrayList;

public class User
{
    public String image, name, locaton, about, status, thumb_image;

    public User()
    {

    }

    public User(String image, String name, String locaton, String about)
    {
        this.image = image;
        this.name = name;
        this.locaton = locaton;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocaton()
    {
        return locaton;
    }

    public void setLocaton(String locaton)
    {
        this.locaton = locaton;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
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
}
