package com.example.android.quakereport;

public class Quake {
    private String mMag;
    private String mPlace;
    private String mPlaceH;
    private String mDate;
    private String mTime;
    private String mUrl;

    public Quake(String mag, String place,String placeH, String date, String time, String url)
    {
        mMag=mag;
        mPlace=place;
        mPlaceH=placeH;
        mDate=date;
        mTime=time;
        mUrl=url;
    }

    public String getMag() {
        return mMag;
    }
    public String getPlace() {
        return mPlace;
    }

    public String getPlaceH() {
        return mPlaceH;
    }
    public String getDate() {
        return mDate;
    }
    public String getTime() {
        return mTime;
    }
    public String getUrl() {
        return mUrl;
    }


}

