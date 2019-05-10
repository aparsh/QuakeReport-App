package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

import static com.example.android.quakereport.MainActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */

    private QueryUtils() {
    }


    public static ArrayList<Quake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Quake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the {@link Event}
        return earthquakes;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static ArrayList<Quake> extractEarthquakes(String  SAMPLE_JSON_RESPONSE) {


        ArrayList<Quake> earthquakes = new ArrayList<>();
        if (TextUtils.isEmpty(SAMPLE_JSON_RESPONSE)) {
            return null;
        }
        try {

            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray earthquakearray = root.getJSONArray("features");

            for (int i=0;i<earthquakearray.length();i++)
            {
                JSONObject currentquake = earthquakearray.getJSONObject(i);
                JSONObject properties = currentquake.getJSONObject("properties");

                String mag = properties.getString("mag");
                String place = properties.getString("place");
                String time = properties.getString("time");
                String url = properties.getString("url");

                int k = place.indexOf(" of ");
                String place1;
                String place2;
                if (k>-1){
                place1 = place.substring(0,k+3);
                place2 = place.substring(k+4);}
                else
                {
                 place1="";
                 place2=place;
                }
                long timeInMilliseconds = Long.valueOf(time);
                Date dateObject = new Date(timeInMilliseconds);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                String timeformatted = timeFormat.format(dateObject);


                Quake earthquake = new Quake(mag,place1,place2,dateToDisplay,timeformatted,url);
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }


        return earthquakes;
    }

}