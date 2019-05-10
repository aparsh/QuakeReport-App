/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<Quake>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    quakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        mAdapter = new quakeAdapter(this, new ArrayList<Quake>());
        if (isConnected) {
            getLoaderManager().initLoader(1, null, this).forceLoad();
        }
        else {
            ProgressBar progress = (ProgressBar) findViewById(R.id.progress_circular);
            progress.setVisibility(View.GONE);

            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            TextView emptyview = (TextView) findViewById(R.id.emptytext);

            emptyview.setText("No internet");

            earthquakeListView.setEmptyView(emptyview);
        }


    }

    @Override
    public Loader<ArrayList<Quake>> onCreateLoader(int id, Bundle args) {
        return new QuakeLoader(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Quake>> loader, ArrayList<Quake> result) {
        ProgressBar progress = (ProgressBar) findViewById(R.id.progress_circular);
        progress.setVisibility(View.GONE);
        if (result == null) {
            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            TextView emptyview = (TextView) findViewById(R.id.emptytext);

            emptyview.setText("No Earthquakes to display");

            earthquakeListView.setEmptyView(emptyview);
            return;
        }
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter.addAll(result);

        earthquakeListView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Quake>> loader) {
        mAdapter.addAll(new ArrayList<Quake>());
    }
}

