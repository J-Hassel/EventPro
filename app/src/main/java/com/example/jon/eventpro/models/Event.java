package com.example.jon.eventpro.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable
{


    private int imageID;
    private String image, title, date, time, location, address, price, about, lat, lon;

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

    public Event(String image, String title, String date, String time, String location, String lat, String lon, String price, String about)
    {
        this.image = image;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.address = null;
        this.price = price;
        this.about = about;
        this.lat = lat;
        this.lon = lon;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel (Parcel out, int flags){
        out.writeString(image);
        out.writeString(title);
        out.writeString(date);
        out.writeString(time);
        out.writeString(location);
        out.writeString(address);
        out.writeString(price);
        out.writeString(about);
        out.writeString(lat);
        out.writeString(lon);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in){
            return new Event(in);
    }

    public Event[] newArray(int size){
            return new Event[size];
    }
};

    private Event(Parcel in) {
        image = in.readString();
        title = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        address = in.readString();
        price = in.readString();
        about = in.readString();
        lat = in.readString();
        lon = in.readString();
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

    public String getLat() { return lat;}

    public void setLat(String lat) { this.lat = lat;}

    public String getLon() { return lon;}

    public void setLon(String lon) { this.lat = lon;}

    @Override
    public String toString() {
        String _image = "image: " + this.image + '\n';
        String _title = "title: " + this.title + '\n';
        String _date = "date: " + this.date + '\n';
        String _location = "location: " + this.location + '\n';
        String _address = "address: " + this.address + '\n';
        String _price = "price: " + this.price + '\n';
        String _about = "about: " + this.about + '\n';
        String _lat = "lat: " + this.lat + '\n';
        String _lon = "lon: " + this.lon + '\n';
        return _image + _title + _date + _location + _address + _price + _about + _lat + _lon;
    }
}
