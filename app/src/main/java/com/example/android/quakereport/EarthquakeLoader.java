package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    List<Earthquake> mEarthquakes = null;
    String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        mEarthquakes = QueryUtils.fetchEarthquakesData(mUrl);
        return mEarthquakes;
    }
}
