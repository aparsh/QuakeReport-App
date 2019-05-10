package com.example.android.quakereport;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
public class QuakeLoader extends AsyncTaskLoader<ArrayList<Quake>>{

    String murl;
    public QuakeLoader(Context context,String url) {
        super(context);
        murl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Quake> loadInBackground() {
        if (murl == null) {
            return null;
        }
        ArrayList<Quake> earthquakes = QueryUtils.fetchEarthquakeData(murl);
        return earthquakes;
    }
}
