package com.example.jon.eventpro.models;

public class Message
{
    private String message, from;
    private long time;

    public Message()
    {

    }

    public Message(String message, String from, long time)
    {
        this.message = message;
        this.from = from;
        this.time = time;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }
}
