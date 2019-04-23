package com.example.android.quakereport;

public class Earthquake {
    String location;
    double magnitude;
    long time;
    String url;

    public Earthquake(String location, double magnitude, long time, String url) {
        this.location = location;
        this.magnitude = magnitude;
        this.time = time;
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
