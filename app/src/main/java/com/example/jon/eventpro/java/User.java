package com.example.jon.eventpro.java;

import java.util.ArrayList;

public class User
{   //TODO: figure out what we need/dont need here
    private boolean isLoggedIn;
    private int userID;
    private String username, password, firstName, lastName, fullName, about;
    private ArrayList<String> interests;


    public boolean isLoggedIn()
    {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        isLoggedIn = loggedIn;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
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
