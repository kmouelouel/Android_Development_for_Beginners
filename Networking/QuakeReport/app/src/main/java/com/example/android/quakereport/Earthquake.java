package com.example.android.quakereport;

/**
 * Created by kmoue on 3/17/2017.
 */

public class Earthquake {
    // state:
    /* Migatitude*/
    private double mMagnitude;
    /*location of the earthquake*/
    private String mLocation;
    /*Date of the earthquake*/
    private long mtimeInMilliseconds ;
    // url for each
   private String mUrl;
    /*
    Constructor a new objet
    @param magnitude it is size of the earthquake
    @param location is the city location of the earthquake
    @param date is the date the earthquake happened
         */
    public Earthquake(double magnitude, String location, long timeInMilliseconds , String url){
        mMagnitude=magnitude;
        mLocation=location;
        mtimeInMilliseconds =timeInMilliseconds ;
        mUrl=url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long gettimeInMilliseconds () {
        return mtimeInMilliseconds ;
    }
    public String getUrl() {
        return mUrl;
    }
}
